flowchart TD
    %% 前期文档
    subgraph PP["项目建议书 Project Proposal"]
        direction TB
        PP1[市场机遇] --> PP2[项目创意]
        PP2 --> PP3[初步方案]
        PP3 --> PP4[投资预估]
        PP2 --> PP5[预期收益]
        PP1 --> PP6[竞品分析]
    end

    subgraph FS["可行性研究报告"]
        direction TB
        FS1[技术可行性] --> FS2[技术方案]
        FS1 --> FS3[技术风险]
        FS4[经济可行性] --> FS5[成本收益]
        FS4 --> FS6[投资回报]
        FS7[运营可行性] --> FS8[资源需求]
        FS7 --> FS9[组织保障]
    end

    subgraph PC["立项报告"]
        direction TB
        PC1[项目概述] --> PC2[项目目标]
        PC2 --> PC3[关键里程碑]
        PC2 --> PC4[预算规划]
        PC1 --> PC5[团队组织]
        PC5 --> PC6[职责分工]
        PC1 --> PC7[风险清单]
    end

    %% 需求分析文档
    subgraph BRD["BRD业务需求文档"]
        direction TB
        B1[项目背景] --> B2[业务目标]
        B2 --> B3[市场机会]
        B3 --> B4[商业模式]
        B4 --> B5[收益预估]
        B2 --> B6[项目范围]
        B6 --> B7[时间节点]
        B4 --> B8[风险评估]
        
        %% 新增业务流程部分
        B9[流程概览] --> B10[详细流程]
        B10 --> B11[角色职责]
        
        %% 新增业务用例部分
        B12[用例概述] --> B13[详细用例]
        B13 --> B14[用例关系]
        
        %% 新增用户故事部分
        B15[Epic列表] --> B16[用户故事]
        B16 --> B17[验收标准]
        
        %% 内部关联
        B2 --> B9
        B9 --> B12
        B12 --> B15
        B6 --> B15
    end

    subgraph PRD["PRD产品需求文档"]
        direction TB
        P1[用户画像] --> P2[用户场景]
        P2 --> P3[功能需求]
        P3 --> P4[界面原型]
        P4 --> P5[交互流程]
        P3 --> P6[业务规则]
        P3 --> P7[非功能需求]
        P7 --> P8[性能指标]
    end

    %% 技术设计文档
    subgraph FSD["FSD功能规格说明"]
        direction TB
        F1[功能架构] --> F2[模块划分]
        F2 --> F3[功能描述]
        F3 --> F4[处理流程]
        F4 --> F5[异常处理]
        F3 --> F6[业务规则]
        F2 --> F7[模块接口]
    end

    subgraph TSD["TSD技术规格说明"]
        direction TB
        T1[技术选型] --> T2[系统架构]
        T2 --> T3[部署架构]
        T2 --> T4[数据架构]
        T4 --> T5[存储方案]
        T2 --> T6[安全方案]
        T6 --> T7[性能方案]
    end

    %% 详细设计文档
    subgraph DBD["数据库设计"]
        direction TB
        D1[概念模型] --> D2[逻辑模型]
        D2 --> D3[物理模型]
        D3 --> D4[表结构]
        D4 --> D5[索引设计]
        D4 --> D6[存储过程]
        D3 --> D7[数据迁移]
    end

    subgraph APID["API接口设计"]
        direction TB
        A1[接口规范] --> A2[认证授权]
        A2 --> A3[接口清单]
        A3 --> A4[请求参数]
        A4 --> A5[响应数据]
        A3 --> A6[错误码]
        A3 --> A7[限流策略]
    end

    %% 项目建议书到可行性研究
    PP2 -.-> FS1
    PP3 -.-> FS2
    PP4 -.-> FS4
    PP5 -.-> FS5

    %% 可行性研究到立项
    FS2 -.-> PC1
    FS5 -.-> PC4
    FS8 -.-> PC5

    %% 立项到BRD
    PC1 -.-> B1
    PC2 -.-> B2
    PC4 -.-> B5
    PC7 -.-> B8

    %% BRD到PRD
    B2 -.-> P1
    B3 -.-> P2
    B4 -.-> P3
    B6 -.-> P3

    %% PRD到FSD/TSD
    P3 -.-> F1
    P6 -.-> F6
    P5 -.-> F4
    P7 -.-> T7

    %% FSD到TSD/API
    F1 -.-> T2
    F2 -.-> T4
    F7 -.-> A1

    %% TSD到数据库
    T4 -.-> D1
    T5 -.-> D3

    %% FSD/DB到API
    F3 -.-> A3
    D4 -.-> A4
    D4 -.-> A5

    %% 新增 IPD 阶段门
    subgraph IPD["IPD Phase Gates"]
        direction TB
        IPD1[概念阶段门] --> IPD2[计划阶段门]
        IPD2 --> IPD3[开发阶段门]
        IPD3 --> IPD4[验证阶段门]
        IPD4 --> IPD5[发布阶段门]
    end

    %% 新增敏捷工件
    subgraph AGILE["Agile Artifacts"]
        direction TB
        AG1[产品待办] --> AG2[迭代待办]
        AG2 --> AG3[燃尽图]
        AG2 --> AG4[迭代评审]
        AG4 --> AG5[回顾会议]
    end

    %% 新增DDD战略设计
    subgraph DDD["Domain Driven Design"]
        direction TB
        DD1[限界上下文] --> DD2[上下文映射]
        DD2 --> DD3[领域事件]
        DD3 --> DD4[聚合根]
        DD1 --> DD5[通用语言]
    end

    %% IPD阶段门关联
    IPD1 -.-> PP
    IPD2 -.-> FS
    IPD3 -.-> PRD
    IPD4 -.-> TSD

    %% 敏捷工件关联
    AG1 -.-> BRD
    AG2 -.-> PRD
    AG4 -.-> FSD

    %% DDD关联
    DD1 -.-> PRD
    DD2 -.-> TSD
    DD3 -.-> APID

    %% 更新与PRD的关联
    B16 -.-> P1[用户画像]
    B13 -.-> P2[用户场景]
    B10 -.-> P3[功能需求]
    B17 -.-> P7[非功能需求]

    %% 更新与FSD的关联
    B10 -.-> F1[功能架构]
    B11 -.-> F2[模块划分]
    B14 -.-> F3[功能描述]

    linkStyle default stroke:#333,stroke-width:2px
    linkStyle 40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62 stroke:#ff6b6b,stroke-width:2px,stroke-dasharray: 5 5

    classDef default fill:#f9f9f9,stroke:#333,stroke-width:1px
    classDef crossDep fill:#fff0f0,stroke:#ff6b6b,stroke-width:2px