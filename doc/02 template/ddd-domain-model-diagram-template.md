# 领域模型图模板
版本：[版本号]
日期：[日期]

## 1. 整体领域模型

### 1.1 核心领域模型
```mermaid
classDiagram
    class Entity1 {
        +属性1
        +属性2
        +行为1()
        +行为2()
    }
    
    class Entity2 {
        +属性1
        +属性2
        +行为1()
        +行为2()
    }
    
    class ValueObject1 {
        +属性1
        +属性2
        +验证()
    }

    Entity1 --> Entity2
    Entity1 --> "1" ValueObject1
```

### 1.2 值对象模型
```mermaid
classDiagram
    class ValueObject1 {
        +属性1
        +属性2
        +验证()
    }
    
    class ValueObject2 {
        +属性1
        +属性2
        +验证()
    }

    ValueObject1 --> ValueObject2
```

## 2. 限界上下文详情

### 2.1 [上下文名称1]
```mermaid
classDiagram
    class AggregateRoot {
        +属性1
        +属性2
        +行为1()
        +行为2()
    }
    
    class Entity {
        +属性1
        +属性2
        +行为1()
    }
    
    class ValueObject {
        +属性1
        +属性2
        +验证()
    }

    AggregateRoot --> Entity
    Entity --> ValueObject
```

### 2.2 [上下文名称2]
[使用相同的图表结构描述其他上下文]

## 3. 上下文交互模型

### 3.1 [流程名称1]
```mermaid
sequenceDiagram
    participant A as 参与者A
    participant B as 参与者B
    participant C as 参与者C
    
    A->>B: 请求1
    B->>C: 请求2
    C-->>B: 响应2
    B-->>A: 响应1
```

### 3.2 [流程名称2]
[使用相同的图表结构描述其他流程]

## 4. 聚合关系模型

### 4.1 [聚合名称1]
```mermaid
graph TB
    subgraph Aggregate1
        AR1[聚合根1] --> E1[实体1]
        AR1 --> E2[实体2]
        E1 --> VO1[值对象1]
    end
```

### 4.2 [聚合名称2]
[使用相同的图表结构描述其他聚合]

## 5. 状态转换图

### 5.1 [状态图名称1]
```mermaid
stateDiagram-v2
    [*] --> 状态1
    状态1 --> 状态2
    状态2 --> 状态3
    状态3 --> [*]
```

### 5.2 [状态图名称2]
[使用相同的图表结构描述其他状态转换]

## 6. 业务流程图

### 6.1 [业务流程1]
```mermaid
graph TB
    A[开始] --> B{判断}
    B -->|条件1| C[处理1]
    B -->|条件2| D[处理2]
    C --> E[结束]
    D --> E
```

### 6.2 [业务流程2]
[使用相同的图表结构描述其他业务流程]

## 7. 附录

### 7.1 图例说明
| 符号 | 含义 | 用法 |
|------|------|------|
| [符号1] | [含义1] | [用法1] |
| [符号2] | [含义2] | [用法2] |

### 7.2 变更历史
| 日期 | 版本 | 变更内容 | 作者 |
|------|------|----------|------|
| [日期] | [版本] | [变更内容] | [作者] | 