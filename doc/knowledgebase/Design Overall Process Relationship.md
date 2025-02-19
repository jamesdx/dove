flowchart LR

%% =============================
%% 大的分组：需求输入 (左) & 设计文档 (右)
%% =============================

subgraph INPUT[需求阶段输入]
    direction TB

    %% AGroup: 业务需求(二级)
    subgraph AGroup[业务需求]
        A(业务需求)
        A1(用户故事)
        A2(业务流程)
        A3(业务规则)
        A4(验收标准)
        A5(角色权限需求)
        A --> A1
        A --> A2
        A --> A3
        A --> A4
        A --> A5
    end

    %% BGroup: 非功能性需求(二级)
    subgraph BGroup[非功能性需求]
        B(非功能性需求)
        B1(性能/SLA)
        B2(安全&合规)
        B3(数据治理)
        B4(可扩展性)
        B5(灾备&BCP指标)
        B --> B1
        B --> B2
        B --> B3
        B --> B4
        B --> B5
    end

    %% CGroup: 约束&假设(二级)
    subgraph CGroup[约束 & 假设]
        C(约束&假设)
        C1(技术栈限制)
        C2(时间/成本/团队)
        C3(组织审批要求)
        C --> C1
        C --> C2
        C --> C3
    end

    %% DGroup: 外部系统需求
    subgraph DGroup[外部系统需求]
        D(外部系统需求)
        D1(第三方API/SDK)
        D2(通信协议/ACL)
        D3(事件/消息依赖)
        D --> D1
        D --> D2
        D --> D3
    end

    %% EGroup: 前端/UI需求
    subgraph EGroup[前端/UI/UX 需求]
        E(前端需求)
        E1(品牌规范)
        E2(信息架构&兼容)
        E3(可用性&访问性)
        E --> E1
        E --> E2
        E --> E3
    end

    %% FGroup: 测试目标
    subgraph FGroup[测试目标]
        F(测试目标)
        F1(覆盖率目标)
        F2(性能测试范围)
        F3(安全测试范围)
        F4(迭代频率/CI需求)
        F --> F1
        F --> F2
        F --> F3
        F --> F4
    end
end

%% =============================
%% 设计文档 (六大板块 + 二级要素)
%% =============================
subgraph DESIGN[软件架构设计文档]
    %% 1. DDD 战略设计
    subgraph S1[1. DDD 战略设计]
        S1A(核心域/支撑域/通用域)
        S1B(限界上下文)
        S1C(上下文映射)
        S1D(防腐层 ACL)
        S1E(统一语言)
    end

    %% 2. DDD 战术设计
    subgraph S2[2. DDD 战术设计]
        S2A(实体 Entity)
        S2B(值对象 Value Object)
        S2C(聚合/聚合根)
        S2D(领域事件)
        S2E(领域服务)
        S2F(应用服务)
        S2G(仓储 Repository)
    end

    %% 3. API 设计
    subgraph S3[3. API 设计]
        S3A(API规范 REST/gRPC)
        S3B(API网关)
        S3C(认证&授权)
        S3D(API版本管理)
    end

    %% 4. 系统架构(NFR)
    subgraph S4[4. 系统架构NFR]
        subgraph S4_1[4.1 性能 & 扩展]
            S4_1A(缓存 Caching)
            S4_1B(CQRS)
            S4_1C(负载均衡)
            S4_1D(自动伸缩)
        end

        subgraph S4_2[4.2 数据架构]
            S4_2A(DB选型 SQL/NoSQL)
            S4_2B(表结构设计 Schema)
            S4_2C(索引 Indexing)
            S4_2D(分片&读写分离)
            S4_2E(数据缓存)
            subgraph S4_2_6[4.2.6 数据治理 & 合规]
                S4_2_6A(数据质量)
                S4_2_6B(元数据管理)
                S4_2_6C(数据生命周期)
                S4_2_6D(合规GDPR/CCPA)
            end
        end

        subgraph S4_3[4.3 部署架构]
            S4_3A(CI/CD)
            S4_3B(容器化)
            S4_3C(编排 Orchestration)
            S4_3D(环境管理)
            subgraph S4_3_x[4.3.x 灾备 & BCP]
                S4_3_xA(多机房/多活)
                S4_3_xB(备份RPO/RTO)
                S4_3_xC(故障切换Failover)
                S4_3_xD(应急演练)
            end
        end

        subgraph S4_4[4.4 运维监控]
            S4_4A(日志Logging)
            S4_4B(监控Monitoring)
            S4_4C(告警Alerting)
            S4_4D(链路追踪Tracing)
            S4_4E(故障自愈AutoHealing)
        end

        subgraph S4_5[4.5 安全设计]
            S4_5A(身份认证/授权)
            S4_5B(数据加密)
            S4_5C(API安全)
            S4_5D(安全审计)
            S4_5E(零信任)
        end

        subgraph S4_6[4.6 第三方集成]
            S4_6A(集成方式)
            S4_6B(防腐层Adapter)
            S4_6C(错误重试补偿)
            S4_6D(集成安全)
            S4_6E(熔断/限流)
            S4_6F(集成监控日志)
        end
    end

    %% 5. 前端设计
    subgraph S5[5.前端设计]
        S5A(信息架构)
        S5B(用户流程)
        S5C(视觉设计)
        S5D(交互设计)
        S5E(前端技术栈)
    end

    %% 6. 测试设计
    subgraph S6[6测试设计]
        S6A(单元测试)
        S6B(契约测试)
        S6C(集成测试)
        S6D(端到端 E2E)
        S6E(非功能测试)
    end
end

%% 连接关系
A1 --> S1A & S1B & S2A & S2B & S3A & S5A & S5B
A2 --> S1B & S2C
A3 --> S2D & S2E
A4 --> S6A & S6C
A5 --> S3C & S4_5A
B1 --> S4_1A & S4_1C & S4_1D & S6E
B2 --> S4_5 & S4_2_6D
B3 --> S4_2_6A & S4_2_6B & S4_2_6C
B5 --> S4_3_xA & S4_3_xB & S4_3_xC
D1 --> S4_6A & S4_6B & S4_6C & S4_6D
E1 --> S5C
F1 --> S6A
F2 --> S6E
F3 --> S6E
F4 --> S4_3A

%% 样式
classDef inputcolor fill:#fff8cc,stroke:#bbb,stroke-width:1px,color:#000
classDef designcolor fill:#e2f1ff,stroke:#88b4d3,stroke-width:1px,color:#000

class AGroup,BGroup,CGroup,DGroup,EGroup,FGroup,A,B,C,D,E,F,A1,A2,A3,A4,A5,B1,B2,B3,B4,B5,C1,C2,C3,D1,D2,D3,E1,E2,E3,F1,F2,F3,F4 inputcolor
class S1,S1A,S1B,S1C,S1D,S1E,S2,S2A,S2B,S2C,S2D,S2E,S2F,S2G,S3,S3A,S3B,S3C,S3D,S4,S4_1,S4_1A,S4_1B,S4_1C,S4_1D,S4_2,S4_2A,S4_2B,S4_2C,S4_2D,S4_2E,S4_2_6,S4_2_6A,S4_2_6B,S4_2_6C,S4_2_6D,S4_3,S4_3A,S4_3B,S4_3C,S4_3D,S4_3_x,S4_3_xA,S4_3_xB,S4_3_xC,S4_3_xD,S4_4,S4_4A,S4_4B,S4_4C,S4_4D,S4_4E,S4_5,S4_5A,S4_5B,S4_5C,S4_5D,S4_5E,S4_6,S4_6A,S4_6B,S4_6C,S4_6D,S4_6E,S4_6F,S5,S5A,S5B,S5C,S5D,S5E,S6,S6A,S6B,S6C,S6D,S6E designcolor