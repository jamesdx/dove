# 登录系统领域事件图
版本：v1.0.1
日期：2024-03-21

## 1. 事件流概述

### 1.1 整体事件流
```mermaid
graph LR
    subgraph 认证上下文
        AE1[认证成功]
        AE2[认证失败]
        AE3[风险警告]
    end
    
    subgraph 会话上下文
        SE1[会话创建]
        SE2[会话过期]
        SE3[会话注销]
    end
    
    subgraph 审计上下文
        LE1[操作记录]
        LE2[风险记录]
        LE3[合规记录]
    end
    
    AE1 --> SE1
    AE2 --> LE2
    SE1 --> LE1
    SE2 --> LE1
    SE3 --> LE1
    AE3 --> LE2
```

### 1.2 事件分类
| 事件类型 | 所属上下文 | 触发条件 | 影响范围 |
|---------|-----------|----------|----------|
| 认证事件 | 认证上下文 | 用户认证操作 | 全局 |
| 会话事件 | 会话上下文 | 会话状态变更 | 用户级 |
| 审计事件 | 审计上下文 | 安全相关操作 | 系统级 |

## 2. 核心领域事件

### 2.1 认证事件
```mermaid
classDiagram
    class DomainEvent {
        +UUID eventId
        +DateTime timestamp
        +String type
        +publish()
    }
    
    class AuthenticationSucceededEvent {
        +UUID userId
        +AuthType authType
        +AuthContext context
        +process()
    }
    
    class AuthenticationFailedEvent {
        +UUID userId
        +String reason
        +int attempts
        +process()
    }
    
    class RiskDetectedEvent {
        +UUID userId
        +RiskLevel level
        +String description
        +process()
    }
    
    DomainEvent <|-- AuthenticationSucceededEvent
    DomainEvent <|-- AuthenticationFailedEvent
    DomainEvent <|-- RiskDetectedEvent
```

### 2.2 会话事件
```mermaid
classDiagram
    class DomainEvent {
        +UUID eventId
        +DateTime timestamp
        +String type
        +publish()
    }
    
    class SessionCreatedEvent {
        +UUID sessionId
        +UUID userId
        +SessionContext context
        +process()
    }
    
    class SessionExpiredEvent {
        +UUID sessionId
        +DateTime expiryTime
        +process()
    }
    
    class SessionTerminatedEvent {
        +UUID sessionId
        +String reason
        +process()
    }
    
    DomainEvent <|-- SessionCreatedEvent
    DomainEvent <|-- SessionExpiredEvent
    DomainEvent <|-- SessionTerminatedEvent
```

## 3. 事件处理流程

### 3.1 认证成功流程
```mermaid
sequenceDiagram
    participant AS as AuthService
    participant EP as EventPublisher
    participant SS as SessionService
    participant AL as AuditLogger
    
    AS->>EP: 发布认证成功事件
    EP->>SS: 处理会话创建
    EP->>AL: 记录审计日志
    
    alt 需要通知
        EP->>NS: 发送通知
    end
```

### 3.2 会话管理流程
```mermaid
sequenceDiagram
    participant SS as SessionService
    participant EP as EventPublisher
    participant AL as AuditLogger
    participant CM as CacheManager
    
    SS->>EP: 发布会话事件
    EP->>AL: 记录审计日志
    EP->>CM: 更新缓存
    
    alt 异常会话
        EP->>SS: 触发会话清理
    end
```

## 4. 事件订阅关系

### 4.1 认证事件订阅
```mermaid
graph TB
    subgraph Publishers
        AE[认证事件]
    end
    
    subgraph Subscribers
        SS[会话服务]
        AL[审计日志]
        RM[风险监控]
        NS[通知服务]
    end
    
    AE --> SS
    AE --> AL
    AE --> RM
    AE --> NS
```

### 4.2 会话事件订阅
```mermaid
graph TB
    subgraph Publishers
        SE[会话事件]
    end
    
    subgraph Subscribers
        AL[审计日志]
        CM[缓存管理]
        CS[清理服务]
    end
    
    SE --> AL
    SE --> CM
    SE --> CS
```

## 5. 事件状态流转

### 5.1 认证事件状态
```mermaid
stateDiagram-v2
    [*] --> Published
    Published --> Processing
    Processing --> Completed
    Processing --> Failed
    Failed --> Retry
    Retry --> Processing
    Completed --> [*]
```

### 5.2 会话事件状态
```mermaid
stateDiagram-v2
    [*] --> Created
    Created --> Active
    Active --> Expired
    Active --> Terminated
    Expired --> [*]
    Terminated --> [*]
```

## 6. 事件处理策略

### 6.1 事件处理规则
| 事件类型 | 处理优先级 | 重试策略 | 失败处理 |
|---------|-----------|----------|----------|
| 认证成功 | 高 | 3次，指数退避 | 告警通知 |
| 认证失败 | 中 | 2次，固定间隔 | 记录日志 |
| 会话创建 | 高 | 3次，指数退避 | 强制清理 |
| 会话过期 | 低 | 1次，立即重试 | 异步清理 |

### 6.2 事件存储策略
| 事件类型 | 存储时间 | 存储方式 | 清理策略 |
|---------|----------|----------|----------|
| 认证事件 | 30天 | 数据库 | 定期归档 |
| 会话事件 | 7天 | 数据库 | 定期清理 |
| 审计事件 | 180天 | 数据库 | 定期归档 |

## 7. 事件监控

### 7.1 监控指标
| 指标 | 描述 | 警告阈值 | 监控周期 |
|------|------|----------|----------|
| 事件处理延迟 | 从发布到处理完成的时间 | >500ms | 实时 |
| 事件处理成功率 | 成功处理的事件百分比 | <99% | 分钟 |
| 事件积压数量 | 待处理的事件队列长度 | >1000 | 实时 |

### 7.2 告警规则
| 规则 | 条件 | 级别 | 通知方式 |
|------|------|------|----------|
| 高延迟 | 延迟>1s | 警告 | 邮件 |
| 处理失败 | 失败率>5% | 严重 | 短信+邮件 |
| 队列积压 | 积压>5000 | 紧急 | 电话+短信 |

## 8. 附录

### 8.1 事件Schema
```json
{
    "DomainEvent": {
        "eventId": "UUID",
        "timestamp": "DateTime",
        "type": "String",
        "version": "String",
        "data": "Object"
    }
}
```

### 8.2 变更历史
| 日期 | 版本 | 变更内容 | 作者 |
|------|------|----------|------|
| 2024-03-21 | v1.0.1 | 初始版本 | DDD Expert | 