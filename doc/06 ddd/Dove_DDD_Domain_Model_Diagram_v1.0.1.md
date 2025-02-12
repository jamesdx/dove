# 登录系统领域模型图
版本：v1.0.1
日期：2024-03-21

## 1. 整体领域模型

### 1.1 核心领域模型
```mermaid
classDiagram
    class Authentication {
        +UUID id
        +AuthType type
        +AuthStatus status
        +DateTime timestamp
        +validate()
        +approve()
        +reject()
    }
    
    class Credential {
        +UUID id
        +CredentialType type
        +String value
        +DateTime expiry
        +verify()
        +update()
        +expire()
    }
    
    class Session {
        +UUID id
        +UUID userId
        +SessionStatus status
        +DateTime expiryTime
        +create()
        +validate()
        +terminate()
    }
    
    class AccessToken {
        +String token
        +TokenType type
        +Set~String~ scope
        +DateTime expiryTime
        +validate()
        +refresh()
        +revoke()
    }

    Authentication --> Credential
    Authentication --> "1" AuthenticationContext
    Session --> AccessToken
    Session --> "1" SessionContext
```

### 1.2 值对象模型
```mermaid
classDiagram
    class AuthenticationContext {
        +DeviceInfo deviceInfo
        +Location location
        +DateTime timestamp
    }
    
    class AuthenticationResult {
        +boolean success
        +String reason
        +RiskLevel riskLevel
    }
    
    class SessionContext {
        +String deviceId
        +String userAgent
        +String ipAddress
    }
    
    class TokenMetadata {
        +DateTime issuedAt
        +String issuer
        +String audience
    }

    AuthenticationContext --> DeviceInfo
    AuthenticationContext --> Location
    AccessToken --> TokenMetadata
```

## 2. 限界上下文详情

### 2.1 认证上下文
```mermaid
classDiagram
    class AuthenticationRoot {
        +UUID id
        +AuthType type
        +AuthStatus status
        +validate()
        +approve()
        +reject()
    }
    
    class AuthenticationService {
        +authenticate()
        +validateCredential()
        +assessRisk()
    }
    
    class AuthenticationEvent {
        +UUID eventId
        +DateTime timestamp
        +publish()
    }
    
    class RiskAssessor {
        +assess()
        +calculateScore()
    }

    AuthenticationRoot --> Credential
    AuthenticationService --> AuthenticationRoot
    AuthenticationRoot --> AuthenticationEvent
    AuthenticationService --> RiskAssessor
```

### 2.2 会话上下文
```mermaid
classDiagram
    class SessionRoot {
        +UUID id
        +SessionStatus status
        +create()
        +validate()
        +terminate()
    }
    
    class SessionManager {
        +createSession()
        +validateSession()
        +terminateSession()
    }
    
    class TokenService {
        +generateToken()
        +validateToken()
        +revokeToken()
    }

    SessionRoot --> AccessToken
    SessionManager --> SessionRoot
    SessionManager --> TokenService
```

## 3. 上下文交互模型

### 3.1 认证流程
```mermaid
sequenceDiagram
    participant C as Client
    participant AS as AuthService
    participant SS as SessionService
    participant TS as TokenService
    
    C->>AS: 认证请求
    AS->>AS: 验证凭证
    AS->>AS: 风险评估
    AS->>SS: 创建会话
    SS->>TS: 生成令牌
    TS-->>SS: 返回令牌
    SS-->>AS: 返回会话
    AS-->>C: 认证响应
```

### 3.2 会话管理流程
```mermaid
sequenceDiagram
    participant C as Client
    participant SS as SessionService
    participant TS as TokenService
    participant AS as AuditService
    
    C->>SS: 验证会话
    SS->>TS: 验证令牌
    TS-->>SS: 令牌有效
    SS->>AS: 记录审计
    SS-->>C: 会话有效
```

## 4. 聚合关系模型

### 4.1 认证聚合
```mermaid
graph TB
    subgraph Authentication Aggregate
        AR[认证聚合根] --> C[Credential]
        AR --> AC[AuthContext]
        AR --> AR2[AuthResult]
        C --> CT[CredentialType]
    end
    
    subgraph Session Aggregate
        SR[会话聚合根] --> AT[AccessToken]
        SR --> SC[SessionContext]
        AT --> TM[TokenMetadata]
    end
```

### 4.2 领域服务交互
```mermaid
graph TB
    subgraph Domain Services
        AS[AuthService] --> AR[AuthRepository]
        AS --> RA[RiskAssessor]
        AS --> EP[EventPublisher]
        
        SS[SessionService] --> SR[SessionRepository]
        SS --> TS[TokenService]
        SS --> AL[AuditLogger]
    end
```

## 5. 事件流模型

### 5.1 认证事件流
```mermaid
graph LR
    E1[AuthSucceeded] --> H1[SessionManager]
    E1 --> H2[AuditLogger]
    E2[AuthFailed] --> H3[LockoutManager]
    E2 --> H4[RiskAnalyzer]
```

### 5.2 会话事件流
```mermaid
graph LR
    E1[SessionCreated] --> H1[TokenGenerator]
    E1 --> H2[AuditLogger]
    E2[SessionExpired] --> H3[Cleanup]
    E2 --> H4[Notifier]
```

## 6. 状态转换图

### 6.1 认证状态
```mermaid
stateDiagram-v2
    [*] --> Initiated
    Initiated --> Validating
    Validating --> Approved
    Validating --> Rejected
    Approved --> Completed
    Rejected --> Failed
    Failed --> [*]
    Completed --> [*]
```

### 6.2 会话状态
```mermaid
stateDiagram-v2
    [*] --> Created
    Created --> Active
    Active --> Expired
    Active --> Terminated
    Expired --> [*]
    Terminated --> [*]
```

## 7. 附录

### 7.1 图例说明
| 符号 | 含义 | 用法 |
|------|------|------|
| 实线箭头 | 关联关系 | 表示对象之间的直接关联 |
| 虚线箭头 | 依赖关系 | 表示对象之间的依赖调用 |
| 实线框 | 聚合边界 | 表示聚合的范围 |
| 双线框 | 限界上下文 | 表示上下文的边界 |

### 7.2 变更历史
| 日期 | 版本 | 变更内容 | 作者 |
|------|------|----------|------|
| 2024-03-21 | v1.0.1 | 初始版本 | DDD Expert | 