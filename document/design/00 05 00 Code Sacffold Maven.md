# 企业级 Maven 使用需求说明 (最终完善版)

## 一、项目背景与目标

1. **规模与使用场景**  
   - 面向 **2 亿注册用户**、**8000 万并发**访问的 **SaaS 国际化**平台，覆盖全球多时区、多语言环境。  
   - 需保证在 **工作时间高负载**、**非工作时间 30% 负载** 依然稳定运行。

2. **关键业务需求**  
   - 前后端分离，后端采用 **Spring Cloud + Spring Cloud Alibaba** 微服务；  
   - **精确复刻 JIRA Web 登录**流程，包括表单字段、Remember Me、登录错误提示、UI 交互等；  
   - 强调 **国际化 (i18n)**、**高并发**、**弹性扩容** 与 **安全合规**。

3. **Maven 的角色**  
   - 统一管理 **微服务依赖**（Spring Boot/Cloud/Alibaba、Security、Database、MQ、Cache 等）与 **构建插件**；  
   - 提供 **多模块/多仓库** 的父子项目结构，简化子服务的版本与插件配置；  
   - 支持企业级 CI/CD、许可证审查、质量审查、Docker 化部署。

---

## 二、Maven 需求与规范

### 企业级微服务项目结构
   - **dove-parent** (父POM项目)
     - 统一依赖版本管理
       - 定义 `<properties>` 统一管理版本号
       - 使用 `<dependencyManagement>` 声明第三方依赖
       - 引入 dove-dependencies BOM 管理内部模块
       - 锁定 Spring Boot/Cloud/Alibaba 等核心框架版本
     
     - 统一插件管理
       - 使用 `<pluginManagement>` 统一声明插件版本与配置
       - 配置 maven-compiler-plugin 指定 Java 版本
       - 配置 spring-boot-maven-plugin 支持打包
       - 配置 maven-source-plugin 生成源码包
       - 配置 maven-javadoc-plugin 生成文档
       - 配置 maven-checkstyle-plugin 检查代码规范
       - 配置 jacoco-maven-plugin 生成测试覆盖率报告
     
     - 统一构建配置  
       - 设置 `<packaging>pom</packaging>` 作为父项目
       - 配置 `<modules>` 声明子模块
       - 配置 `<profiles>` 区分开发/测试/生产环境
         - dev: 开发环境,默认激活,spring.profiles.active=dev
         - test: 测试环境,spring.profiles.active=test
         - prod: 生产环境,spring.profiles.active=prod
       - 配置 distributionManagement 指定发布仓库
       - 配置 repositories/pluginRepositories 指定依赖仓库
     
     - 统一代码规范
       - 配置 checkstyle 规则文件
       - 配置 EditorConfig 统一代码格式
       - 配置 PMD/SpotBugs 进行静态代码分析
       - 配置 Git pre-commit hooks 检查提交规范

   - **dove-dependencies** (依赖管理BOM)
     - 统一依赖版本管理
       - 设置 `<packaging>pom</packaging>` 作为BOM项目
       - 定义 `<properties>` 统一管理版本号
       - 使用 `<dependencyManagement>` 声明第三方依赖
       - 锁定 Spring Boot/Cloud/Alibaba 等核心框架版本
       - 锁定数据库、缓存、消息队列等中间件版本
       - 锁定工具类库、测试框架等通用依赖版本
     
     - 内部模块版本管理
       - 在 `<dependencyManagement>` 中声明所有内部模块
       
       - 统一管理技术框架层版本 (dove-framework)
         - dove-framework-core: 提供框架级基础功能,如IoC容器、AOP等底层支持
         - dove-framework-web: 提供MVC框架、过滤器、拦截器等Web框架功能
         - dove-framework-data: 提供ORM框架、数据源、事务等数据访问层框架
         - dove-framework-cache: 提供缓存抽象、多级缓存等缓存框架功能
         - dove-framework-security: 提供认证授权、安全过滤等安全框架功能
         - dove-framework-log: 提供日志框架集成、日志切面等日志框架功能
         - dove-framework-test: 提供单元测试、集成测试等测试框架功能
       
       - 统一管理公共工具层版本 (dove-common)
         - dove-common-core: 提供字符串、日期等基础工具类
         - dove-common-util: 提供文件、加密等通用工具类
         - dove-common-model: 提供DTO、VO等通用对象模型
         - dove-common-swagger: 提供Swagger文档生成工具
         - dove-common-redis: 提供Redis操作工具类
         - dove-common-mybatis: 提供Mybatis扩展工具类
         - dove-common-log: 提供日志记录工具类
         - dove-common-security: 提供安全工具类
         
       - 统一管理业务模块层版本
         - dove-module-system 系统管理版本
         - dove-module-auth 认证中心版本
         - dove-module-gateway API网关版本
         - dove-module-monitor 监控中心版本
         - dove-module-job 任务调度版本
         - dove-module-file 文件服务版本
         - dove-module-message 消息中心版本
         - dove-module-report 报表中心版本
         
       - 统一管理服务接口层版本
         - dove-api-system 系统接口版本
         - dove-api-auth 认证接口版本
         - dove-api-file 文件接口版本
         - dove-api-message 消息接口版本
     
     - 版本发布管理
       - 配置 distributionManagement 指定发布仓库
         - 配置 Nexus/Artifactory 私服仓库
           - SNAPSHOT 仓库用于开发版本
           - RELEASE 仓库用于稳定版本
           - 3rd-party 仓库存放第三方依赖
         - 配置 Maven Settings.xml 的 server 认证信息
         - 配置 CI/CD 流水线自动发布流程
       
       - 配置版本号规则
         - 主版本号.次版本号.修订号-SNAPSHOT
           - 开发阶段使用 SNAPSHOT 版本
           - 如 1.0.0-SNAPSHOT, 1.0.1-SNAPSHOT
         - 主版本号.次版本号.修订号-RELEASE  
           - 稳定版本使用 RELEASE 版本
           - 如 1.0.0-RELEASE, 1.0.1-RELEASE
         - 遵循语义化版本规范(Semantic Versioning)
           - 主版本号:不兼容的API修改
           - 次版本号:向下兼容的功能性新增
           - 修订号:向下兼容的问题修正
       
       - 定期发布稳定版本
         - 每月发布一次 RELEASE 版本
         - 版本发布前进行完整测试与审查
         - 生成版本变更文档(Release Notes)
         - 打 Tag 标记版本代码
         - 归档历史版本制品
     
     - 依赖版本管理
       - 锁定第三方依赖版本
         - 使用 Maven Bill of Materials(BOM)
         - 在 properties 中统一声明版本号
         - 使用 dependencyManagement 锁定版本
         - 定期检查并升级依赖版本
         - 使用 Maven Enforcer 插件强制版本
       
       - 管理内部模块版本
         - 统一管理所有内部模块版本号
         - 保持模块间版本一致性
         - 模块间依赖使用 ${project.version}
         - 避免循环依赖
     
     - 技术选型基线
       - 定期评估和更新技术栈
       - 建立依赖白名单机制
       - 进行依赖安全漏洞扫描
       - 评估开源许可证合规性
       - 建立依赖升级变更流程
       - 维护技术栈文档

   - **dove-framework** (技术框架层)
     - dove-framework-core: 核心框架
     - dove-framework-web: Web框架
     - dove-framework-data: 数据访问
     - dove-framework-cache: 缓存框架
     - dove-framework-security: 安全框架
     - dove-framework-log: 日志框架
     - dove-framework-test: 测试框架

   - **dove-common** (公共工具层)
     - dove-common-core: 核心工具类
     - dove-common-util: 通用工具类
     - dove-common-model: 通用领域模型
     - dove-common-swagger: API文档
     - dove-common-redis: Redis工具
     - dove-common-mybatis: Mybatis工具
     - dove-common-log: 日志工具
     - dove-common-security: 安全工具

   - **dove-modules** (业务模块层)
     - dove-module-system: 系统管理
     - dove-module-auth: 认证中心
     - dove-module-gateway: API网关
     - dove-module-monitor: 监控中心
     - dove-module-job: 任务调度
     - dove-module-file: 文件服务
     - dove-module-message: 消息中心
     - dove-module-report: 报表中心

   - **dove-api** (服务接口层)
     - dove-api-system: 系统接口
     - dove-api-auth: 认证接口
     - dove-api-file: 文件接口
     - dove-api-message: 消息接口

   - **dove-example** (示例工程)
     - 微服务架构最佳实践
     - 框架使用示例代码
     - 常见业务场景演示
     

### 2. 父 POM (Parent POM)

#### 2.1 基本配置信息
2.1.1 父项目名称: dove-parent  
2.1.2 包名：com.dove  
2.1.3 版本号：1.0.0  
2.1.4 描述：dove-parent 是 Dove 项目的父项目，用于统一管理依赖版本、插件版本、构建配置等。  
2.1.5 文件夹结构
   ```
   dove-parent
   ├── pom.xml                               # 父POM配置文件
   ├── .gitignore                            # Git忽略文件
   ├── README.md                             # 项目说明文档
   ├── LICENSE                               # 开源许可证
   ├── .mvn                                  # Maven Wrapper配置目录,用于统一管理Maven版本
   │   └── wrapper                          # wrapper配置文件夹
   │       ├── maven-wrapper.jar            # Maven Wrapper可执行JAR包,用于自动下载指定版本的Maven
   │       └── maven-wrapper.properties     # Maven Wrapper属性配置文件,指定Maven版本等配置
   ├── mvnw                                  # Maven Wrapper脚本(Unix)
   ├── mvnw.cmd                             # Maven Wrapper脚本(Windows)
   ├── docs                                 # 项目文档
   │   ├── images                           # 文档图片资源
   │   └── guides                           # 使用指南
   ├── scripts                              # 构建和部署脚本
   │   ├── build.sh                         # 构建脚本
   │   └── deploy.sh                        # 部署脚本
   └── src                                  # 源代码目录
       └── main
           └── resources
               ├── archetype-resources       # Maven原型资源
               └── META-INF
                   └── maven                 # Maven配置元数据
   ```

#### 2.2 父 POM项目 核心配置要求
     - 统一依赖版本管理
       - 定义 `<properties>` 统一管理版本号
       - 使用 `<dependencyManagement>` 声明第三方依赖
       - 引入 dove-dependencies BOM 管理内部模块
       - 锁定 Spring Boot/Cloud/Alibaba 等核心框架版本
     
     - 统一插件管理
       - 使用 `<pluginManagement>` 统一声明插件版本与配置
       - 配置 maven-compiler-plugin 指定 Java 版本
       - 配置 spring-boot-maven-plugin 支持打包
       - 配置 maven-source-plugin 生成源码包
       - 配置 maven-javadoc-plugin 生成文档
       - 配置 maven-checkstyle-plugin 检查代码规范
       - 配置 jacoco-maven-plugin 生成测试覆盖率报告
     
     - 统一构建配置  
       - 设置 `<packaging>pom</packaging>` 作为父项目
       - 配置 `<modules>` 声明子模块
       - 配置 `<profiles>` 区分开发/测试/生产环境
       - 配置 distributionManagement 指定发布仓库
       - 配置 repositories/pluginRepositories 指定依赖仓库
     
     - 统一代码规范
       - 配置 checkstyle 规则文件
       - 配置 EditorConfig 统一代码格式
       - 配置 PMD/SpotBugs 进行静态代码分析
       - 配置 Git pre-commit hooks 检查提交规范

#### 2.1.1 父项目名称: dove-parent  
2.1.2 包名：com.dove  
2.1.3 版本号：1.0.0  
2.1.4 描述：dove-parent 是 Dove 项目的父项目，用于统一管理依赖版本、插件版本、构建配置等。  
2.1.5 文件夹结构  
#### 2.3 依赖管理 (dependencyManagement)  
   - **Spring Cloud 基础依赖**
     - `spring-boot-starter-parent:3.1.5`
     - `spring-cloud-dependencies:2022.0.4`
     - `spring-cloud-alibaba-dependencies:2022.0.0.0`

   - **Spring Cloud 核心组件**
     - `spring-cloud-starter-bootstrap:4.0.4`
     - `spring-cloud-starter-loadbalancer:4.0.4`
     - `spring-cloud-starter-openfeign:4.0.4`
     - `spring-cloud-starter-gateway:4.0.4`

   - **Spring Cloud Alibaba 组件**
     - `spring-cloud-starter-alibaba-nacos-discovery:2022.0.0.0`
     - `spring-cloud-starter-alibaba-nacos-config:2022.0.0.0`
     - `spring-cloud-starter-alibaba-sentinel:2022.0.0.0`
     - `spring-cloud-starter-alibaba-seata:2022.0.0.0`

   - **安全认证**
     - `spring-boot-starter-security:3.1.5`
     - `spring-security-oauth2-authorization-server:1.1.3`
     - `spring-security-oauth2-resource-server:6.1.5`
     - `jjwt-api:0.11.5`
     - `jjwt-impl:0.11.5`
     - `keycloak-spring-boot-starter:23.0.3`
     - `spring-security-saml2-service-provider:6.1.5`
     - `spring-security-crypto:6.1.5`
     - `passay:1.6.3`
     - `otp:1.0.1`
     - `spring-security-test:6.1.5`

   - **验证码服务**
     - `kaptcha:2.3.3`

   - **数据存储与缓存**
     - `mariadb-java-client:3.1.4`
     - `spring-boot-starter-data-redis:3.1.5`
     - `redisson-spring-boot-starter:3.23.5`
     - `spring-boot-starter-data-jpa:3.1.5`
     - `hibernate-core:6.2.13.Final`
     - `hikaricp:5.0.1`
     - `spring-session-data-redis:3.1.2`
     - `spring-session-jdbc:3.1.2`
     - `spring-session-hazelcast:3.1.2`

   - **数据库迁移**
     - `flyway-core:9.22.3`
     - `flyway-mysql:9.22.3`

   - **响应式编程**
     - `spring-boot-starter-webflux:3.1.5`
     - `reactor-test:3.5.11`
     - `reactor-tools:3.5.11`
     - `spring-data-redis-reactive:3.1.5`

   - **缓存**
     - `spring-boot-starter-cache:3.1.5`
     - `ehcache:3.10.8`
     - `caffeine:3.1.8`

   - **消息队列**
     - `rocketmq-spring-boot-starter:2.2.3`
     - `spring-kafka:3.0.12`
     - `spring-cloud-starter-stream-kafka:4.0.4`

   - **监控与日志**
     - `spring-boot-starter-actuator:3.1.5`
     - `micrometer-registry-prometheus:1.11.5`
     - `spring-boot-starter-log4j2:3.1.5`
     - `logstash-logback-encoder:7.4`
     - `spring-boot-admin-starter-server:3.1.7`
     - `spring-boot-admin-starter-client:3.1.7`
     - `prometheus-client:0.16.0`

   - **国际化与模板**
     - `spring-boot-starter-thymeleaf:3.1.5`
     - `thymeleaf-extras-springsecurity6:3.1.2.RELEASE`
     - `spring-boot-starter-validation:3.1.5`
     - `freemarker:2.3.32`
     - `velocity-engine-core:2.3`
     - `thymeleaf-spring6:3.1.2.RELEASE`

   - **工具类库**
     - `commons-lang3:3.13.0`
     - `guava:32.1.2-jre`
     - `fastjson2:2.0.40`
     - `mapstruct:1.5.5.Final`
     - `lombok:1.18.30`
     - `javassist:3.29.2-GA`

   - **API文档**
     - `springdoc-openapi-starter-webmvc-ui:2.2.0`
     - `springdoc-openapi-starter-webflux-ui:2.2.0`

   - **任务调度**
     - `xxl-job-core:2.4.0`
     - `spring-boot-starter-quartz:3.1.5`
     - `spring-boot-starter-batch:3.1.5`

   - **测试框架**
     - `spring-boot-starter-test:3.1.5`
     - `testcontainers:1.19.1`
     - `mockito-core:5.7.0`
     - `junit-jupiter:5.10.0`
     - `spring-cloud-starter-contract-verifier:4.0.4`
     - `jmh-core:1.37`

   - **链路追踪**
     - `micrometer-tracing-bridge-brave:1.1.5`
     - `skywalking-agent:9.0.0`

   - **开发工具**
     - `spring-boot-devtools:3.1.5`
     - `spring-boot-configuration-processor:3.1.5`
     - `spring-boot-properties-migrator:3.1.5`
     - `arthas-spring-boot-starter:3.7.1`

   - **分布式事务**
     - `seata-spring-boot-starter:1.7.1`

   - **文件存储与文档处理**
     - `minio-java:8.5.6`
     - `aws-java-sdk-s3:1.12.261`
     - `poi-ooxml:5.2.4`
     - `easyexcel:3.3.2`
     - `aspose-words:23.10`
     - `itext7-core:8.0.2`

   - **搜索引擎**
     - `spring-boot-starter-data-elasticsearch:3.1.5`
     - `lucene-core:9.8.0`
     - `hibernate-search-orm-orm6:6.2.1.Final`

   - **安全与加密**
     - `jasypt-spring-boot-starter:3.0.5`
     - `shield-core:1.0.1`
     - `sensitive-core:1.0.0`
     - `encryption-spring-boot-starter:1.0.0`

   - **通信**
     - `grpc-spring-boot-starter:2.15.0.RELEASE`
     - `spring-boot-starter-mail:3.1.5`

   - **报表工具**
     - `jasperreports:6.20.6`
     - `xdocreport:2.0.4`

#### 2.3 插件管理 (pluginManagement)  
   - **编译与资源处理**：
     - `maven-compiler-plugin:3.11.0` - Java 17编译配置
     - `maven-resources-plugin:3.3.1` - 资源文件处理
     - `maven-source-plugin:3.3.0` - 源码打包

   - **测试与质量**：
     - `maven-surefire-plugin:3.1.2` - 单元测试执行
     - `maven-failsafe-plugin:3.1.2` - 集成测试执行
     - `jacoco-maven-plugin:0.8.10` - 代码覆盖率分析
     - `sonar-maven-plugin:3.9.1` - SonarQube代码质量分析
     - `maven-checkstyle-plugin:3.3.0` - 代码风格检查
     - `spotbugs-maven-plugin:4.7.3.6` - Bug静态分析

   - **依赖管理与安全**：
     - `versions-maven-plugin:2.16.1` - 依赖版本检查更新
     - `maven-enforcer-plugin:3.4.1` - 依赖版本与环境强制检查
     - `dependency-check-maven:8.4.0` - OWASP安全漏洞扫描
     - `license-maven-plugin:2.2.0` - 依赖许可证检查

   - **容器化部署**：
     - `jib-maven-plugin:3.3.2` - Google容器构建工具
     - `dockerfile-maven-plugin:1.4.13` - Docker镜像构建
     - `fabric8-maven-plugin:4.4.2` - K8s部署支持

   - **文档与发布**：
     - `maven-javadoc-plugin:3.6.0` - JavaDoc生成
     - `maven-deploy-plugin:3.1.1` - 制品发布
     - `maven-release-plugin:3.0.1` - 版本发布管理

   - **Profile配置**：
     - 开发环境(dev): 快速编译,跳过测试
     - 测试环境(test): 启用单元测试,代码覆盖率检查
     - 预发环境(staging): 完整测试,质量检查
     - 生产环境(prod): 完整构建,安全扫描,制品签名

#### 2.4 许可证合规  
   - 父 POM 中记录各依赖的 LICENSE 信息（Apache 2.0、MIT、BSD 等），禁止接入可能冲突商业化的 GPLv3 等；
   - 使用 Helix 公司的 LICENSE 许可证:
     ```
     Copyright (c) 2023 Helix Corporation
     
     Permission is hereby granted, free of charge, to any person obtaining a copy
     of this software and associated documentation files (the "Software"), to deal
     in the Software without restriction, including without limitation the rights
     to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
     copies of the Software, and to permit persons to whom the Software is
     furnished to do so, subject to the following conditions:
     
     The above copyright notice and this permission notice shall be included in all
     copies or substantial portions of the Software.
     
     THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
     IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
     FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
     AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
     LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
     OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
     SOFTWARE.
     ```
   - 定期审阅第三方依赖，升级或替换存在安全/许可证风险的库：
     - 每月使用 `mvn versions:display-dependency-updates` 检查依赖更新
     - 配置 `maven-enforcer-plugin` 强制依赖版本检查规则
     - 使用 `dependency-check-maven` 定期扫描 CVE 漏洞
     - 建立依赖白名单制度，新增依赖需评审其许可证
     - 使用 `license-maven-plugin` 自动检查依赖许可证
     - 配置 SonarQube 定期扫描，关注依赖相关安全告警
     - 建立升级变更流程：本地验证 -> 测试环境 -> 预发布 -> 生产

### 3. dove-dependencies (依赖管理BOM)
#### 3.1 基本信息
     - 包名：com.dove
     - 版本号：1.0.0
     - 描述：dove-dependencies 是 Dove 项目的依赖管理BOM，用于统一管理依赖版本、插件版本、构建配置等。
     - 文件夹结构：
       ```
       dove-dependencies
       ├── pom.xml                               # BOM配置文件
       ├── .gitignore                            # Git忽略文件
       ├── README.md                             # 项目说明文档
       ├── LICENSE                               # 开源许可证
       ├── docs                                  # 项目文档
       │   ├── dependencies                      # 依赖说明文档
       │   │   ├── spring-cloud.md              # Spring Cloud依赖说明
       │   │   ├── spring-boot.md               # Spring Boot依赖说明
       │   │   ├── database.md                  # 数据库相关依赖说明
       │   │   └── tools.md                     # 工具类依赖说明
       │   └── guides                           # 使用指南
       │       ├── quick-start.md               # 快速开始
       │       └── best-practices.md            # 最佳实践
       ├── scripts                              # 构建和部署脚本
       │   ├── version-check.sh                 # 版本检查脚本
       │   ├── dependency-analyze.sh            # 依赖分析脚本
       │   └── license-check.sh                 # 许可证检查脚本
       └── src                                  # 源代码目录
           └── main
               └── resources
                   ├── META-INF
                   │   └── maven                # Maven配置元数据
                   │       └── com.dove
                   │           └── dove-dependencies
                   │               └── pom.xml   # BOM配置备份
                   └── version-rules.xml        # 版本管理规则配置
       
       - pom.xml
       - src/main/resources/META-INF/maven/com.dove/dove-dependencies/pom.xml
       ```

     
#### POM项目 核心配置要求
     - 统一依赖版本管理
       - 设置 `<packaging>pom</packaging>` 作为BOM项目
       - 定义 `<properties>` 统一管理版本号
       - 使用 `<dependencyManagement>` 声明第三方依赖
       - 锁定 Spring Boot/Cloud/Alibaba 等核心框架版本
       - 锁定数据库、缓存、消息队列等中间件版本
       - 锁定工具类库、测试框架等通用依赖版本
     
     - 内部模块版本管理
       - 在 `<dependencyManagement>` 中声明所有内部模块
       - 统一管理技术框架层版本
         - dove-framework-core 核心框架版本
         - dove-framework-web Web框架版本
         - dove-framework-data 数据访问版本
         - dove-framework-cache 缓存框架版本
         - dove-framework-security 安全框架版本
         - dove-framework-log 日志框架版本
         - dove-framework-test 测试框架版本
       
       - 统一管理公共工具层版本
         - dove-common-core 核心工具类版本
         - dove-common-util 通用工具类版本
         - dove-common-model 通用领域模型版本
         - dove-common-swagger API文档版本
         - dove-common-redis Redis工具版本
         - dove-common-mybatis Mybatis工具版本
         - dove-common-log 日志工具版本
         - dove-common-security 安全工具版本
         
       - 统一管理业务模块层版本
         - dove-module-system 系统管理版本
         - dove-module-auth 认证中心版本
         - dove-module-gateway API网关版本
         - dove-module-monitor 监控中心版本
         - dove-module-job 任务调度版本
         - dove-module-file 文件服务版本
         - dove-module-message 消息中心版本
         - dove-module-report 报表中心版本
         
       - 统一管理服务接口层版本
         - dove-api-system 系统接口版本
         - dove-api-auth 认证接口版本
         - dove-api-file 文件接口版本
         - dove-api-message 消息接口版本
     
     - 版本发布管理
       - 配置 distributionManagement 指定发布仓库
         - 配置 Nexus/Artifactory 私服仓库
           - SNAPSHOT 仓库用于开发版本
           - RELEASE 仓库用于稳定版本
           - 3rd-party 仓库存放第三方依赖
         - 配置 Maven Settings.xml 的 server 认证信息
         - 配置 CI/CD 流水线自动发布流程
       
       - 配置版本号规则
         - 主版本号.次版本号.修订号-SNAPSHOT
           - 开发阶段使用 SNAPSHOT 版本
           - 如 1.0.0-SNAPSHOT, 1.0.1-SNAPSHOT
         - 主版本号.次版本号.修订号-RELEASE  
           - 稳定版本使用 RELEASE 版本
           - 如 1.0.0-RELEASE, 1.0.1-RELEASE
         - 遵循语义化版本规范(Semantic Versioning)
           - 主版本号:不兼容的API修改
           - 次版本号:向下兼容的功能性新增
           - 修订号:向下兼容的问题修正
       
       - 定期发布稳定版本
         - 每月发布一次 RELEASE 版本
         - 版本发布前进行完整测试与审查
         - 生成版本变更文档(Release Notes)
         - 打 Tag 标记版本代码
         - 归档历史版本制品
     
     - 依赖版本管理
       - 锁定第三方依赖版本
         - 使用 Maven Bill of Materials(BOM)
         - 在 properties 中统一声明版本号
         - 使用 dependencyManagement 锁定版本
         - 定期检查并升级依赖版本
         - 使用 Maven Enforcer 插件强制版本
       
       - 管理内部模块版本
         - 统一管理所有内部模块版本号
         - 保持模块间版本一致性
         - 模块间依赖使用 ${project.version}
         - 避免循环依赖
     
     - 技术选型基线
       - 定期评估和更新技术栈
       - 建立依赖白名单机制
       - 进行依赖安全漏洞扫描
       - 评估开源许可证合规性
       - 建立依赖升级变更流程
       - 维护技术栈文档
### 2. 子模块 (微服务) 设计
#### 2.0 架构图（Architecture Diagram）

系统架构采用分层设计,各层职责和交互关系如下:

1. **全球化接入层**
   - Global LB: 全球负载均衡,就近接入
   - CDN: 静态资源加速,降低延迟
   - 流量分发到就近的 API Gateway

2. **网关层 (API Gateway)**
   - Spring Cloud Gateway 作为统一入口
   - 提供认证授权、限流、路由等公共能力
   - 与认证服务(Auth Service)交互进行token校验
   - 根据路由规则转发请求到对应微服务

3. **微服务集群**
   - 认证服务(Auth Service):
     * 处理登录认证请求
     * 生成和验证 token
     * 调用用户服务获取用户信息
     * 调用安全服务进行安全检查
     * 使用 Redis 存储 token 和会话信息
   
   - 用户服务(User Service): 
     * 提供用户信息的CRUD操作
     * 与 MariaDB 交互存储用户数据
     * 被认证服务调用获取用户信息
     * 被安全服务调用获取用户权限

   - 安全服务(Security Service):
     * 提供安全策略检查
     * 记录安全审计日志到 Kafka
     * 被认证服务调用进行登录安全检查
     * 从用户服务获取用户权限信息

   - 配置服务(Config Service):
     * 从 Nacos 获取配置信息
     * 为其他服务提供配置管理
     * 配置变更实时推送

4. **数据层**
   - MariaDB: 存储用户数据、权限数据等
   - Redis: 缓存 token、会话等数据
   - Kafka: 收集安全日志、审计日志

5. **基础设施层**
   - Kubernetes: 容器编排与服务管理
   - Nacos: 服务注册与配置管理
   - 监控告警: 系统运行状态监控
   - 日志中心: 统一日志收集分析

```mermaid
graph TD
    subgraph 接入层[全球化接入层]
        LB[Global LB]
        CDN[CDN]
    end

    subgraph 网关层[API Gateway]
        GW[Spring Cloud Gateway]
        GW --> Auth[认证授权]
        GW --> Rate[限流控制]
        GW --> Route[路由转发]
    end

    subgraph 应用层[微服务集群]
        AS[认证服务/Auth Service]
        US[用户服务/User Service]
        SS[安全服务/Security Service]
        CS[配置服务/Config Service]
    end

    subgraph 数据层[数据存储]
        DB[(MariaDB)]
        Cache[(Redis)]
        MQ[Kafka]
    end

    subgraph 基础设施[Cloud Native]
        K8S[Kubernetes]
        Nacos[注册配置中心]
        Monitor[监控告警]
        Log[日志中心]
    end

    LB --> CDN
    CDN --> GW
    GW --> AS
    GW --> US
    GW --> SS
    GW --> CS

    AS --> DB
    AS --> Cache
    US --> DB
    SS --> DB
    SS --> MQ
    CS --> Nacos
```


#### 2.1 dove-common  
##### 2.1.0 核心要求
- dove-common 是 dove-parent  的子模块， 但是独立的文件夹和git 仓库
##### 2.1.1 基本信息
     - 2.1.1.1 包名：com.dove
     - 2.1.1.2 版本号：1.0.0
     - 2.1.1.3 描述：dove-dependencies 是 Dove 项目的依赖管理BOM，用于统一管理依赖版本、插件版本、构建配置等。
     - 2.1.1.4 文件夹结构：
     ```
       - dove-common
         - dove-common-core
           ├── pom.xml                          # 核心模块POM
           ├── src/main/java/com/dove/common/core
           │   ├── utils                        # 工具类包
           │   │   ├── StringUtils.java         # 字符串工具类:提供字符串操作、验证、转换等通用方法
           │   │   ├── DateUtils.java           # 日期工具类:提供日期格式化、计算、转换等功能
           │   │   ├── NumberUtils.java         # 数字工具类:提供数值转换、计算、验证等功能
           │   │   ├── CollectionUtils.java     # 集合工具类:提供集合操作、转换等功能
           │   │   ├── MapUtils.java            # Map工具类:提供Map相关操作方法
           │   │   ├── JsonUtils.java           # JSON工具类:提供JSON序列化和反序列化功能
           │   │   └── RegexUtils.java          # 正则工具类:提供常用正则表达式验证
           │   ├── crypto                       # 加密工具包
           │   │   ├── AESUtils.java            # AES加密工具:提供AES对称加密算法实现
           │   │   ├── RSAUtils.java            # RSA加密工具:提供RSA非对称加密算法实现
           │   │   ├── SM2Utils.java            # 国密SM2工具:提供国密SM2非对称加密算法实现
           │   │   ├── SM3Utils.java            # 国密SM3工具:提供国密SM3哈希算法实现
           │   │   ├── SM4Utils.java            # 国密SM4工具:提供国密SM4对称加密算法实现
           │   │   ├── DigestUtils.java         # 摘要工具类:提供MD5/SHA等摘要算法
           │   │   └── Base64Utils.java         # Base64工具类:提供Base64编解码功能
           │   ├── i18n                         # 国际化工具包
           │   │   ├── LocaleUtils.java         # 国际化工具:提供语言、地区、时区等国际化处理
           │   │   └── MessageUtils.java        # 消息工具:提供国际化消息获取、格式化等功能
           │   ├── thread                       # 线程工具包
           │   │   ├── ThreadPoolConfig.java    # 线程池配置:提供可配置的线程池参数和创建方法
           │   │   ├── ThreadPoolMonitor.java   # 线程池监控:提供线程池运行状态监控和统计
           │   │   ├── AsyncUtils.java          # 异步工具类:提供异步任务处理功能
           │   │   └── ThreadLocalUtils.java    # ThreadLocal工具类:提供线程变量管理
           │   ├── limiter                      # 限流工具包
           │   │   ├── TokenBucketLimiter.java  # 令牌桶限流:基于令牌桶算法的限流实现
           │   │   ├── LeakyBucketLimiter.java  # 漏桶限流:基于漏桶算法的限流实现
           │   │   ├── SlidingWindowLimiter.java # 滑动窗口限流:基于滑动窗口的限流实现
           │   │   └── RateLimiterUtils.java    # 限流工具类:提供通用限流功能
           │   └── id                           # ID生成工具包
           │       ├── SnowflakeIdGenerator.java # 雪花ID生成器:Twitter雪花算法全局唯一ID生成
           │       ├── UUIDGenerator.java        # UUID生成器:提供UUID生成和转换功能
           │       ├── SequenceGenerator.java    # 序列生成器:提供自增序列生成功能
           │       └── IdWorker.java            # ID工作器:提供多种ID生成策略
           └── src/test/java                    # 单元测试目录
               ├── benchmark                     # 性能测试包
               │   ├── StringUtilsBenchmark.java # 字符串工具性能测试
               │   ├── JsonUtilsBenchmark.java   # JSON工具性能测试
               │   └── ThreadPoolBenchmark.java  # 线程池性能测试
               ├── security                      # 安全测试包
               │   ├── CryptoUtilsTest.java      # 加密工具安全测试
               │   ├── ThreadSafetyTest.java     # 线程安全性测试
               │   └── InputValidationTest.java  # 输入验证安全测试
               └── unit                          # 单元测试包
                   ├── StringUtilsTest.java      # 字符串工具单元测试
                   ├── DateUtilsTest.java        # 日期工具单元测试
                   └── CollectionUtilsTest.java  # 集合工具单元测试
           
         - dove-common-web
           ├── pom.xml                          # Web模块POM
           ├── src/main/java/com/dove/common/web
           │   ├── response                     # 响应处理包
           │   │   ├── Result.java              # 统一响应封装:统一API返回结果格式
           │   │   ├── ResultCode.java          # 状态码定义:统一定义API响应状态码
           │   │   ├── PageResult.java          # 分页结果封装:统一分页查询结果格式
           │   │   └── ResponseUtils.java       # 响应工具类:提供响应处理工具方法
           │   ├── exception                    # 异常处理包
           │   │   ├── GlobalExceptionHandler.java # 全局异常处理:统一异常处理和响应
           │   │   ├── BusinessException.java    # 业务异常定义:自定义业务异常类型
           │   │   ├── SystemException.java     # 系统异常定义:系统级异常类型
           │   │   └── ExceptionUtils.java      # 异常工具类:提供异常处理工具方法
           │   ├── validator                    # 校验器包
           │   │   ├── ValidatorConfig.java     # 校验配置:配置统一参数校验规则
           │   │   ├── CustomValidator.java     # 自定义校验器:扩展的自定义校验规则
           │   │   ├── ValidationUtils.java     # 校验工具类:提供参数校验工具方法
           │   │   └── ValidatorGroups.java     # 校验分组:定义不同场景的校验分组
           │   └── security                     # Web安全包
           │       ├── XssFilter.java           # XSS过滤器:防止XSS攻击的输入过滤
           │       ├── CsrfInterceptor.java     # CSRF拦截器:防止CSRF攻击的请求验证
           │       ├── SecurityUtils.java       # 安全工具类:提供Web安全相关工具方法
           │       └── WebSecurityConfig.java   # Web安全配置:配置Web安全规则
           └── src/test/java                    # 单元测试目录
               ├── benchmark                     # 性能测试包
               │   ├── ResponseBenchmark.java    # 响应处理性能测试
               │   └── ValidatorBenchmark.java   # 校验器性能测试
               ├── security                      # 安全测试包
               │   ├── XssFilterTest.java        # XSS过滤器安全测试
               │   └── CsrfInterceptorTest.java  # CSRF拦截器安全测试
               └── unit                          # 单元测试包
                   ├── ResultTest.java           # 响应封装单元测试
                   └── ValidatorTest.java        # 校验器单元测试
           
         - dove-common-model
           ├── pom.xml                          # 模型模块POM
           ├── src/main/java/com/dove/common/model
           │   ├── base                         # 基础模型包
           │   │   ├── BaseEntity.java          # 基础实体类:所有数据库实体的基类
           │   │   ├── BaseDTO.java             # 基础DTO:所有数据传输对象的基类
           │   │   ├── BaseVO.java              # 基础VO:所有视图对象的基类
           │   │   └── BaseEnum.java            # 基础枚举:所有枚举类型的基类
           │   ├── query                        # 查询模型包
           │   │   ├── QueryCondition.java      # 查询条件:通用查询条件封装
           │   │   ├── PageQuery.java           # 分页查询:通用分页查询参数
           │   │   └── SortQuery.java           # 排序查询:通用排序查询参数
           │   └── validation                   # 校验模型包
           │       ├── ValidationGroups.java     # 校验分组:定义不同场景的校验分组
           │       └── ValidationConstants.java  # 校验常量:定义校验相关常量
           └── src/test/java                    # 单元测试目录
               ├── benchmark                     # 性能测试包
               │   └── ModelBenchmark.java       # 模型对象性能测试
               └── unit                          # 单元测试包
                   ├── BaseEntityTest.java       # 基础实体单元测试
                   └── QueryConditionTest.java   # 查询条件单元测试
           
         - dove-common-cache
           ├── pom.xml                          # 缓存模块POM
           ├── src/main/java/com/dove/common/cache
           │   ├── config                       # 缓存配置包
           │   │   ├── CacheConfig.java         # 缓存配置:统一缓存配置和管理
           │   │   ├── RedisConfig.java         # Redis配置:Redis连接和操作配置
           │   │   └── CacheConstants.java      # 缓存常量:定义缓存相关常量
           │   ├── lock                         # 分布式锁包
           │   │   ├── RedisLock.java           # Redis分布式锁:基于Redis的分布式锁实现
           │   │   ├── ZookeeperLock.java       # ZK分布式锁:基于Zookeeper的分布式锁实现
           │   │   └── LockUtils.java          # 锁工具类:提供分布式锁工具方法
           │   └── monitor                      # 缓存监控包
           │       ├── CacheMonitor.java        # 缓存监控:缓存使用状况监控和统计
           │       └── MonitorUtils.java        # 监控工具类:提供缓存监控工具方法
           └── src/test/java                    # 单元测试目录
               ├── benchmark                     # 性能测试包
               │   ├── RedisBenchmark.java       # Redis操作性能测试
               │   └── LockBenchmark.java        # 分布式锁性能测试
               ├── security                      # 安全测试包
               │   └── CacheSecurityTest.java    # 缓存安全测试
               └── unit                          # 单元测试包
                   ├── RedisLockTest.java        # Redis锁单元测试
                   └── CacheMonitorTest.java     # 缓存监控单元测试
           
         - dove-common-storage
           ├── pom.xml                          # 存储模块POM
           ├── src/main/java/com/dove/common/storage
           │   ├── file                         # 文件处理包
           │   │   ├── FileUtils.java           # 文件工具类:文件操作和处理工具方法
           │   │   ├── FileUploader.java        # 文件上传器:统一的文件上传处理
           │   │   ├── ImageUtils.java          # 图片工具类:图片处理工具方法
           │   │   └── VideoUtils.java          # 视频工具类:视频处理工具方法
           │   ├── document                     # 文档处理包
           │   │   ├── ExcelUtils.java          # Excel工具类:Excel文件的读写操作
           │   │   ├── PdfUtils.java            # PDF工具类:PDF文件的生成和处理
           │   │   ├── WordUtils.java           # Word工具类:Word文档处理
           │   │   └── CsvUtils.java            # CSV工具类:CSV文件处理
           │   └── oss                          # 对象存储包
           │       ├── OssConfig.java           # OSS配置:对象存储服务配置
           │       ├── OssTemplate.java         # OSS操作模板:统一的对象存储操作接口
           │       ├── MinioTemplate.java       # MinIO操作模板:MinIO存储实现
           │       └── AliyunOssTemplate.java   # 阿里云OSS模板:阿里云OSS实现
           └── src/test/java                    # 单元测试目录
               ├── benchmark                     # 性能测试包
               │   ├── FileUploadBenchmark.java  # 文件上传性能测试
               │   └── OssBenchmark.java         # OSS操作性能测试
               ├── security                      # 安全测试包
               │   └── StorageSecurityTest.java  # 存储安全测试
               └── unit                          # 单元测试包
                   ├── FileUtilsTest.java        # 文件工具单元测试
                   └── OssTemplateTest.java      # OSS模板单元测试
           
         - dove-common-log
           ├── pom.xml                          # 日志模块POM
           ├── src/main/java/com/dove/common/log
           │   ├── aspect                       # 日志切面包
           │   │   ├── LogAspect.java           # 日志切面:通过AOP记录操作日志
           │   │   ├── AuditAspect.java         # 审计切面:通过AOP记录审计日志
           │   │   └── LogAnnotation.java       # 日志注解:自定义日志记录注解
           │   ├── trace                        # 链路追踪包
           │   │   ├── TraceUtils.java          # 链路追踪工具:分布式链路追踪工具
           │   │   └── MDCUtils.java            # MDC工具类:日志上下文工具
           │   └── elk                          # ELK集成包
           │       ├── LogstashAppender.java    # ELK日志配置:Logstash日志收集配置
           │       └── ElkConfig.java           # ELK配置:ELK集成配置
           └── src/test/java                    # 单元测试目录
               ├── benchmark                     # 性能测试包
               │   └── LogBenchmark.java         # 日志记录性能测试
               ├── security                      # 安全测试包
               │   └── LogSecurityTest.java      # 日志安全测试
               └── unit                          # 单元测试包
                   ├── LogAspectTest.java        # 日志切面单元测试
                   └── TraceUtilsTest.java       # 链路追踪单元测试
           
         - dove-common-security
           ├── pom.xml                          # 安全模块POM
           ├── src/main/java/com/dove/common/security
           │   ├── mask                         # 数据脱敏包
           │   │   ├── SensitiveDataUtils.java  # 数据脱敏工具:敏感信息加密和脱敏处理
           │   │   └── MaskStrategy.java        # 脱敏策略:不同类型数据的脱敏策略
           │   ├── crypto                       # 安全加密包
           │   │   ├── PasswordEncoder.java     # 密码加密器:密码加密和验证工具
           │   │   └── CryptoUtils.java         # 加密工具类:通用加密解密工具
           │   └── auth                         # 认证授权包
           │       ├── SecurityUtils.java        # 安全工具类:安全上下文和工具方法
           │       ├── JwtUtils.java            # JWT工具类:JWT令牌处理工具
           │       └── OAuth2Utils.java         # OAuth2工具类:OAuth2认证工具
           └── src/test/java                    # 单元测试目录
               ├── benchmark                     # 性能测试包
               │   ├── CryptoBenchmark.java      # 加密解密性能测试
               │   └── JwtBenchmark.java         # JWT处理性能测试
               ├── security                      # 安全测试包
               │   ├── PasswordStrengthTest.java # 密码强度测试
               │   └── TokenSecurityTest.java    # 令牌安全测试
               └── unit                          # 单元测试包
                   ├── MaskStrategyTest.java     # 脱敏策略单元测试
                   └── SecurityUtilsTest.java    # 安全工具单元测试
           
         - dove-common-swagger
           ├── pom.xml                          # Swagger模块POM
           ├── src/main/java/com/dove/common/swagger
           │   ├── config                       # Swagger配置包
           │   │   ├── SwaggerConfig.java       # Swagger配置:API文档自动生成配置
           │   │   └── SwaggerProperties.java   # Swagger属性:Swagger配置属性
           │   └── plugin                       # Swagger插件包
           │       ├── ApiVersionPlugin.java     # API版本插件:API版本控制插件
           │       └── SecurityPlugin.java       # 安全插件:API文档安全控制插件
           └── src/test/java                    # 单元测试目录
               ├── benchmark                     # 性能测试包
               │   └── SwaggerBenchmark.java     # Swagger性能测试
               └── unit                          # 单元测试包
                   ├── SwaggerConfigTest.java    # Swagger配置单元测试
                   └── ApiVersionTest.java       # API版本单元测试
      ``` 
##### 2.1.2 功能描述

1. dove-common-core 核心功能模块
   - 提供各类通用工具类
     - 字符串、日期、数字、集合等基础工具类
     - JSON序列化/反序列化工具
     - 正则表达式验证工具
   - 提供加密安全工具包
     - AES/RSA等对称/非对称加密
     - 国密SM2/SM3/SM4算法实现
     - 摘要算法(MD5/SHA)和Base64编码
   - 提供国际化支持工具
     - 语言、地区、时区等国际化处理
     - 国际化消息获取和格式化
   - 提供线程管理工具
     - 可配置线程池和监控
     - 异步任务处理
     - ThreadLocal变量管理
   - 提供限流工具实现
     - 令牌桶、漏桶算法
     - 滑动窗口限流
   - 提供全局唯一ID生成
     - 雪花算法
     - UUID生成
     - 序列号生成

2. dove-common-web Web功能模块
   - 统一响应处理
     - 统一API返回格式
     - 统一状态码定义
     - 分页结果封装
   - 全局异常处理
     - 统一异常处理
     - 自定义业务异常
     - 系统异常定义
   - 参数校验功能
     - 统一校验规则配置
     - 自定义校验规则
     - 分组校验支持
   - Web安全防护
     - XSS攻击防护
     - CSRF攻击防护
     - Web安全配置

3. dove-common-model 模型功能模块
   - 提供统一的数据模型定义
   - 提供DTO/VO等传输对象
   - 提供统一的实体类基类

4. dove-common-log 日志功能模块
   - 统一日志记录
     - 统一日志格式定义
     - 日志切面自动记录
     - MDC上下文管理
   - 链路追踪集成
     - 分布式链路追踪
     - 调用链路分析
     - 性能监控支持
   - 日志采集配置
     - ELK日志采集
     - 日志文件管理
     - 日志级别动态调整
   - 审计日志功能
     - 操作日志记录
     - 安全审计日志
     - 日志查询分析

5. dove-common-redis 缓存功能模块
   - Redis操作封装
     - 统一的Redis操作模板
     - 分布式锁实现
     - 缓存注解支持
   - 缓存管理功能
     - 缓存策略配置
     - 缓存监控统计
     - 缓存预热功能
   - 会话管理支持
     - 分布式Session
     - Token存储管理
     - 用户状态缓存

6. dove-common-security 安全功能模块
   - 数据安全防护
     - 敏感数据脱敏
     - 数据加密解密
     - 安全传输保护
   - 认证授权支持
     - JWT令牌管理
     - OAuth2集成
     - SSO单点登录
   - 安全防护功能
     - XSS/CSRF防护
     - SQL注入防护
     - 接口防刷限制

7. dove-common-swagger 接口文档模块
   - Swagger配置管理
     - API文档生成
     - 接口版本控制
     - 文档安全访问
   - 文档增强功能
     - 接口分组管理
     - 文档导出功能
     - 在线调试支持
   - 扩展插件支持
     - 自定义注解支持
     - 文档模板定制
     - 多语言支持

#### 2.2 dove-auth 
##### 2.2.0 核心要求
##### 2.2.1 基本信息
   - 包名: com.dove.auth
   - 版本号: 1.0.0
   - 父项目: dove-parent
   - 依赖管理: dove-dependencies
   - 文件夹结构：
    ```
    dove-auth
    ├── src
    │   ├── main
    │   │   ├── java
    │   │   │   └── com.dove.auth
    │   │   │       ├── api                    # 对外接口
    │   │   │       │   ├── dto               # 数据传输对象
    │   │   │       │   │   ├── LoginRequest.java      # 登录请求DTO
    │   │   │       │   │   └── LoginResponse.java     # 登录响应DTO  
    │   │   │       │   └── facade            # 外观接口
    │   │   │       │       └── AuthFacade.java        # 认证服务外观接口
    │   │   │       ├── config                # 配置类
    │   │   │       │   ├── oauth2           # OAuth2配置
    │   │   │       │   │   ├── OAuth2Config.java      # OAuth2主配置
    │   │   │       │   │   └── OAuth2Properties.java  # OAuth2配置属性
    │   │   │       │   ├── security         # 安全配置
    │   │   │       │   │   ├── SecurityConfig.java    # 安全主配置
    │   │   │       │   │   └── WebSecurityConfig.java # Web安全配置
    │   │   │       │   └── web              # Web配置
    │   │   │       │       └── WebMvcConfig.java      # MVC配置
    │   │   │       ├── constant             # 常量定义
    │   │   │       │   ├── AuthConstants.java         # 认证常量
    │   │   │       │   └── SecurityConstants.java     # 安全常量
    │   │   │       ├── controller           # 控制器
    │   │   │       │   ├── login           # 登录相关
    │   │   │       │   │   ├── LoginController.java   # 登录控制器
    │   │   │       │   │   └── LogoutController.java  # 登出控制器
    │   │   │       │   ├── oauth           # OAuth相关
    │   │   │       │   │   └── OAuth2Controller.java  # OAuth2控制器
    │   │   │       │   └── session         # 会话相关
    │   │   │       │       └── SessionController.java # 会话控制器
    │   │   │       ├── core                 # 核心模块
    │   │   │       │   ├── auth            # 认证核心
    │   │   │       │   │   ├── AuthenticationManager.java # 认证管理器
    │   │   │       │   │   └── AuthorizationManager.java  # 授权管理器
    │   │   │       │   ├── session         # 会话核心
    │   │   │       │   │   └── SessionManager.java    # 会话管理器
    │   │   │       │   └── token           # Token处理
    │   │   │       │       ├── JwtTokenGenerator.java # JWT生成器
    │   │   │       │       └── TokenValidator.java    # Token验证器
    │   │   │       ├── domain              # 领域模型
    │   │   │       │   ├── entity         # 实体类
    │   │   │       │   │   ├── User.java           # 用户实体
    │   │   │       │   │   └── LoginLog.java       # 登录日志实体
    │   │   │       │   └── vo             # 值对象
    │   │   │       │       └── LoginVO.java         # 登录值对象
    │   │   │       ├── enums               # 枚举定义
    │   │   │       │   ├── LoginType.java          # 登录类型枚举
    │   │   │       │   └── LoginStatus.java        # 登录状态枚举
    │   │   │       ├── exception           # 异常定义
    │   │   │       │   ├── AuthException.java      # 认证异常
    │   │   │       │   └── LoginException.java     # 登录异常
    │   │   │       ├── repository          # 数据访问层
    │   │   │       │   ├── UserRepository.java     # 用户数据访问接口
    │   │   │       │   └── LoginLogRepository.java # 登录日志数据访问接口
    │   │   │       ├── security            # 安全相关
    │   │   │       │   ├── filter         # 安全过滤器
    │   │   │       │   │   ├── JwtAuthFilter.java  # JWT认证过滤器
    │   │   │       │   │   └── LoginFilter.java    # 登录过滤器
    │   │   │       │   └── handler        # 安全处理器
    │   │   │       │       ├── LoginSuccessHandler.java  # 登录成功处理器
    │   │   │       │       └── LoginFailureHandler.java  # 登录失败处理器
    │   │   │       ├── service             # 业务服务
    │   │   │       │   ├── auth           # 认证服务
    │   │   │       │   │   ├── AuthService.java         # 认证服务接口
    │   │   │       │   │   └── AuthServiceImpl.java     # 认证服务实现
    │   │   │       │   ├── oauth          # OAuth服务
    │   │   │       │   │   ├── OAuth2Service.java       # OAuth2服务接口
    │   │   │       │   │   └── OAuth2ServiceImpl.java   # OAuth2服务实现
    │   │   │       │   └── user           # 用户服务
    │   │   │       │       ├── UserService.java         # 用户服务接口
    │   │   │       │       └── UserServiceImpl.java     # 用户服务实现
    │   │   │       └── util                # 工具类
    │   │   │           ├── PasswordEncoder.java    # 密码加密工具
    │   │   │           └── TokenUtils.java         # Token工具类
    │   │   └── resources
    │   │       ├── i18n                    # 国际化资源
    │   │       │   ├── messages_zh_CN.properties  # 中文资源
    │   │       │   └── messages_en_US.properties  # 英文资源
    │   │       ├── mapper                  # MyBatis映射
    │   │       │   ├── UserMapper.xml            # 用户映射文件
    │   │       │   └── LoginLogMapper.xml        # 登录日志映射文件
    │   │       └── application.yml         # 应用配置
    │   └── test                           # 测试代码
    │       └── java
    │           └── com.dove.auth
    │               ├── controller         # 控制器测试
    │               │   ├── LoginControllerTest.java    # 登录控制器测试
    │               │   └── OAuth2ControllerTest.java   # OAuth2控制器测试
    │               └── service           # 服务测试
    │                   ├── AuthServiceTest.java        # 认证服务测试
    │                   └── UserServiceTest.java        # 用户服务测试
    └── pom.xml                          # 项目依赖
    ```
##### 2.2.2 需求描述
###### 2.2.2.1 功能描述
  - 2.2.2.1.1 模块职责描述
   - 用户认证与授权管理
     - 账号密码登录
       - 支持用户名/邮箱/手机号登录
       - 密码加密存储(BCrypt)
       - 防暴力破解(错误次数限制)
       - Remember Me 14天免登录
     - OAuth2/SSO集成
       - 支持OAuth2标准协议
       - 对接企业内部SSO系统
       - 支持多种授权模式
     - 双因素认证(2FA)
       - 支持Google Authenticator
       - 支持短信验证码
       - 支持邮箱验证码
     - 第三方登录
       - 支持企业微信登录
       - 支持钉钉扫码登录
       - 支持自定义第三方登录
   - 会话管理
     - Token签发与验证
     - 会话状态维护
     - 单点登录支持
     - Remember Me功能
   - 安全防护
     - 密码加密存储
     - 防暴力破解
     - 登录日志审计
     - 异常行为检测
   - 用户体验
     - 多语言支持
     - 验证码服务
     - 密码重置流程
     - 登录状态保持
  - 3.1.2 核心流程
    ```mermaid
    sequenceDiagram
        participant C as Client
        participant G as Gateway
        participant A as Auth Service
        participant U as User Service
        participant R as Redis
        
        C->>G: 登录请求
        G->>A: 转发认证
        A->>U: 获取用户信息
        U-->>A: 返回用户数据
        A->>A: 验证密码
        A->>R: 存储会话
        A-->>G: 返回Token
        G-->>C: 登录成功
    ```    
###### 2.2.2.2 非功能性需求
1. 高性能设计
   - 异步非阻塞
   - 本地缓存优化
   - 线程池管理
   - 资源池化复用

2. 高可用保障
   - 熔断降级
   - 限流保护
   - 负载均衡
   - 失败重试

3. 可扩展性
   - 插件化架构
   - SPI机制支持
   - 自定义扩展点
   - 模块化设计

4. 安全性
   - 数据加密
   - 访问控制
   - 安全审计
   - 防攻击机制

5. 可维护性
   - 统一异常处理
   - 规范化日志
   - 监控告警
   - 文档完善
##### 2.2.3 模块设计
###### 2.2.3.1 核心类设计
   - 用户认证与授权管理 
     - 账号密码登录
       - 支持用户名/邮箱/手机号登录
         - 核心类:
           - LoginTypeDetector: 登录类型检测器,通过正则表达式判断登录标识类型
           - UserRepository: 用户数据访问接口,提供多字段查询方法
           - AuthenticationProvider: 认证提供者,实现多字段登录认证逻辑
           - LoginTypeEnum: 登录类型枚举,定义支持的登录方式
           - LoginRequest: 登录请求DTO,包含登录标识和密码等信息
           - LoginResponse: 登录响应DTO,包含token和用户信息等
           - GatewayAuthenticationFilter: 网关认证过滤器,处理登录请求转发
           - AuthenticationService: 认证服务,处理具体的认证逻辑
           - TokenGenerator: Token生成器,生成JWT等token
         - 实现方案:
           - GatewayAuthenticationFilter 拦截登录请求并转发到Auth Service
           - AuthenticationService 调用UserRepository获取用户信息
           - AuthenticationProvider 验证密码
           - TokenGenerator 生成token
           - Redis存储会话信息
           - 返回token给客户端
       - 密码加密存储(BCrypt)
         - 核心类:
           - PasswordEncoder: 密码编码器接口
           - BCryptPasswordEncoder: BCrypt实现的密码编码器
           - SecurityConfig: 安全配置类
           - PasswordValidator: 密码校验器,检查密码强度
           - PasswordHistoryService: 密码历史服务,防止重复使用旧密码
         - 实现方案:
           - 使用 BCryptPasswordEncoder 进行密码加密,强度可配置
           - 密码只存储密文,验证时使用 matches 方法比对
           - 密码更新时校验强度和历史记录
           - 支持定期强制修改密码
       - 防暴力破解(错误次数限制)
         - 核心类:
           - LoginAttemptService: 登录尝试服务
           - RedisTemplate: Redis操作模板
           - LoginFailureHandler: 登录失败处理器
           - IpBlockService: IP封禁服务
           - SecurityAuditLogger: 安全审计日志记录
         - 实现方案:
           - Redis记录失败次数,key格式为 login:fail:{username}
           - 超过阈值锁定账号,登录成功则清除记录
           - 支持IP级别的封禁
           - 记录详细的安全审计日志
       - Remember Me 14天免登录
         - 核心类:
           - RememberMeServices: 记住我服务接口
           - PersistentTokenRepository: 持久化token仓库
           - RememberMeAuthenticationFilter: 记住我认证过滤器
           - TokenCleanupService: 过期token清理服务
           - DeviceInfoService: 设备信息服务
         - 实现方案:
           - 基于数据库存储remember-me token
           - 用户勾选时生成token并写入cookie
           - token有效期可配置
           - 定期清理过期token
           - 记录设备信息用于安全控制
     - OAuth2/SSO集成
       - 核心类:
           - OAuth2LoginConfigurer: OAuth2登录配置器
           - OAuth2UserService: OAuth2用户服务
           - OAuth2AuthorizationRequestResolver: 授权请求解析器
           - OAuth2TokenService: OAuth2令牌服务
           - OAuth2ClientRepository: OAuth2客户端配置仓库
       - 实现方案:
           - 集成Spring Security OAuth2
           - 支持标准OAuth2协议流程
           - 可扩展对接不同的OAuth2提供商
           - 统一的令牌管理
           - 客户端配置动态管理
     - 双因素认证(2FA)
       - 核心类:
           - TwoFactorAuthenticationProvider: 双因素认证提供者
           - GoogleAuthenticator: Google验证器
           - SmsCodeService: 短信验证码服务
           - EmailCodeService: 邮箱验证码服务
           - TwoFactorConfigService: 2FA配置服务
       - 实现方案:
           - 支持多种验证方式(Google/短信/邮箱)
           - 验证码临时存储于Redis
           - 可配置是否强制开启2FA
           - 支持备用验证码
           - 支持动态切换验证方式
     - 第三方登录
       - 核心类:
           - ThirdPartyAuthenticationProvider: 第三方认证提供者
           - WeChatLoginService: 企业微信登录服务
           - DingTalkLoginService: 钉钉登录服务
           - ThirdPartyUserMapper: 第三方用户映射器
           - ThirdPartyConfigService: 第三方配置服务
       - 实现方案:
           - 实现第三方平台的OAuth流程
           - 统一的用户信息映射机制
           - 支持自定义登录提供商
           - 配置动态管理
           - 支持账号绑定/解绑
   - 会话管理
     - 核心类:
       - TokenService: Token服务
       - SessionRegistry: 会话注册表
       - ConcurrentSessionFilter: 并发会话过滤器
       - SessionMonitor: 会话监控器
       - SessionCleanupService: 会话清理服务
       - RedisSessionRepository: Redis会话存储
     - 实现方案:
       - JWT或Session实现会话管理
       - Redis存储会话状态
       - 支持会话并发控制
       - 实时监控会话状态
       - 定期清理过期会话
   - 安全防护
     - 核心类:
       - SecurityAuditLogger: 安全审计日志
       - BruteForceProtector: 暴力破解防护
       - AbnormalLoginDetector: 异常登录检测
       - SecurityEventPublisher: 安全事件发布器
       - SecurityAlertService: 安全告警服务
     - 实现方案:
       - 完整的安全审计日志
       - 多维度的攻击防护
       - 异常行为实时检测
       - 安全事件实时通知
       - 支持自定义告警规则
   - 用户体验
     - 核心类:
       - MessageSource: 国际化消息源
       - CaptchaService: 验证码服务
       - PasswordResetService: 密码重置服务
       - UserPreferenceService: 用户偏好服务
       - NotificationService: 通知服务
     - 实现方案:
       - 基于Spring国际化支持
       - 多种验证码生成方案
       - 标准的密码重置流程
       - 个性化配置持久化
       - 多渠道消息通知
###### 2.2.3.2 数据模型设计

1. 用户认证相关表
   - sys_user_third: 第三方用户关联表
     - id: bigint(20) 主键
     - user_id: bigint(20) 用户ID
     - third_type: varchar(20) 第三方类型(WECHAT/DINGTALK)
     - third_id: varchar(100) 第三方用户ID
     - third_data: text 第三方用户数据
     - bind_time: datetime 绑定时间
     
   - sys_user_password_history: 密码历史表
     - id: bigint(20) 主键
     - user_id: bigint(20) 用户ID 
     - password: varchar(100) 历史密码
     - create_time: datetime 创建时间


3. 安全审计相关表
   - sys_login_log: 登录日志表
     - id: bigint(20) 主键
     - user_id: bigint(20) 用户ID
     - login_type: varchar(20) 登录类型
     - login_ip: varchar(50) 登录IP
     - login_location: varchar(100) 登录地点
     - browser: varchar(50) 浏览器
     - os: varchar(50) 操作系统
     - device_id: varchar(100) 设备ID
     - status: tinyint(1) 状态(0-失败,1-成功)
     - msg: varchar(255) 提示消息
     - login_time: datetime 登录时间

   - sys_operation_log: 操作日志表
     - id: bigint(20) 主键
     - user_id: bigint(20) 用户ID
     - module: varchar(50) 操作模块
     - operation: varchar(50) 操作类型
     - method: varchar(100) 方法名
     - params: text 请求参数
     - time: bigint(20) 执行时长
     - ip: varchar(50) 操作IP
     - location: varchar(255) 操作地点
     - status: int(1) 操作状态
     - error_msg: text 错误消息
     - create_time: datetime 创建时间

   - sys_security_audit: 安全审计表
     - id: bigint(20) 主键
     - user_id: bigint(20) 用户ID
     - event_type: varchar(50) 事件类型
     - event_time: datetime 事件时间
     - ip: varchar(50) IP地址
     - details: text 详细信息
     - risk_level: tinyint(1) 风险等级

4. 会话管理相关表
   - sys_user_token: 用户令牌表
     - id: bigint(20) 主键
     - user_id: bigint(20) 用户ID
     - token: varchar(255) 令牌值
     - token_type: varchar(20) 令牌类型(JWT/Session)
     - refresh_token: varchar(255) 刷新令牌
     - device_type: varchar(20) 设备类型
     - expire_time: datetime 过期时间
     - create_time: datetime 创建时间

   - sys_user_online: 在线用户表
     - id: bigint(20) 主键
     - user_id: bigint(20) 用户ID
     - token_id: varchar(255) 令牌ID
     - browser: varchar(50) 浏览器
     - os: varchar(50) 操作系统
     - device_id: varchar(100) 设备ID
     - ip: varchar(50) 登录IP
     - location: varchar(255) 登录地点
     - start_time: datetime 登录时间
     - last_access_time: datetime 最后访问时间
     - expire_time: datetime 过期时间

   - sys_user_device: 用户设备表
     - id: bigint(20) 主键
     - user_id: bigint(20) 用户ID
     - device_id: varchar(100) 设备ID
     - device_type: varchar(20) 设备类型
     - device_name: varchar(100) 设备名称
     - last_login_time: datetime 最后登录时间
     - is_trusted: tinyint(1) 是否受信任设备
     - status: tinyint(1) 状态

###### 2.2.3.3 测试用例设计
1. 登录认证测试
   - 核心测试类:
     - LoginControllerTest: 登录接口测试
       - testLoginWithUsername(): 测试用户名登录
       - testLoginWithEmail(): 测试邮箱登录 
       - testLoginWithPhone(): 测试手机号登录
       - testLoginWithInvalidCredentials(): 测试无效凭证
       - testLoginWithLockedAccount(): 测试账号锁定
       - testRememberMeLogin(): 测试记住我功能
       - testLoginFlow(): 测试完整登录流程(Gateway->Auth->User->Redis)
       - testLoginTypeDetection(): 测试登录类型检测
       - testPasswordStrengthValidation(): 测试密码强度校验
       - testPasswordHistory(): 测试密码历史记录
     
     - AuthenticationServiceTest: 认证服务测试
       - testPasswordValidation(): 测试密码验证
       - testBruteForceProtection(): 测试暴力破解防护
       - testLoginAttemptLocking(): 测试登录尝试锁定
       - testTokenGeneration(): 测试Token生成
       - testSessionManagement(): 测试会话管理
       - testIpBasedBlocking(): 测试IP封禁
       - testSecurityAuditLogging(): 测试安全审计日志
       - testDeviceInfoTracking(): 测试设备信息追踪

     - OAuth2LoginTest: OAuth2登录测试
       - testOAuth2Flow(): 测试OAuth2流程
       - testTokenExchange(): 测试令牌交换
       - testUserInfoMapping(): 测试用户信息映射
       - testClientValidation(): 测试客户端验证
       - testDynamicClientConfig(): 测试动态客户端配置
       - testTokenManagement(): 测试令牌管理

2. 双因素认证测试
   - 核心测试类:
     - TwoFactorAuthTest: 2FA测试
       - testGoogleAuthenticator(): 测试Google验证器
       - testSmsVerification(): 测试短信验证
       - testEmailVerification(): 测试邮箱验证
       - testBackupCodes(): 测试备用码
       - testSwitchAuthMethod(): 测试切换验证方式
       - testForcedTwoFactor(): 测试强制2FA
       - testTwoFactorConfig(): 测试2FA配置管理

3. 第三方登录测试
   - 核心测试类:
     - ThirdPartyLoginTest: 第三方登录测试
       - testWeChatLogin(): 测试企业微信登录
       - testDingTalkLogin(): 测试钉钉登录
       - testAccountBinding(): 测试账号绑定
       - testAccountUnbinding(): 测试账号解绑
       - testUserInfoMapping(): 测试用户信息映射
       - testDynamicProviderConfig(): 测试动态提供商配置
       - testCustomProvider(): 测试自定义登录提供商

4. 会话管理测试
   - 核心测试类:
     - SessionManagementTest: 会话管理测试
       - testTokenValidation(): 测试Token验证
       - testConcurrentSessions(): 测试并发会话
       - testSessionExpiry(): 测试会话过期
       - testSessionCleanup(): 测试会话清理
       - testRedisSessionStorage(): 测试Redis会话存储
       - testSessionMonitoring(): 测试会话监控
       - testSessionRegistry(): 测试会话注册表

5. 安全防护测试
   - 核心测试类:
     - SecurityProtectionTest: 安全防护测试
       - testAuditLogging(): 测试审计日志
       - testAbnormalLoginDetection(): 测试异常登录检测
       - testSecurityAlerts(): 测试安全告警
       - testCustomRules(): 测试自定义规则
       - testMultiDimensionalProtection(): 测试多维度攻击防护
       - testRealTimeDetection(): 测试实时检测
       - testAlertNotification(): 测试告警通知

6. 用户体验测试
   - 核心测试类:
     - UserExperienceTest: 用户体验测试
       - testI18nMessages(): 测试国际化消息
       - testCaptchaValidation(): 测试验证码
       - testPasswordReset(): 测试密码重置
       - testUserPreferences(): 测试用户偏好
       - testNotificationChannels(): 测试多渠道通知
       - testPersonalizedConfig(): 测试个性化配置
       - testMessageTemplates(): 测试消息模板

7. 高可用性测试
   - 核心测试类:
     - HighAvailabilityTest: 高可用测试
       - testAsyncNonBlocking(): 测试异步非阻塞
       - testLocalCache(): 测试本地缓存
       - testThreadPoolManagement(): 测试线程池管理
       - testResourcePooling(): 测试资源池化
       - testCircuitBreaker(): 测试熔断降级
       - testRateLimiting(): 测试限流保护
       - testLoadBalancing(): 测试负载均衡
       - testFailureRetry(): 测试失败重试

####### 3.3.2.1 测试方案概述
1. 单元测试
   - 使用JUnit 5编写测试用例
   - Mockito模拟外部依赖
   - AssertJ进行断言验证
   - 测试覆盖率要求>80%

2. 集成测试
   - 使用TestContainers管理测试环境
   - Spring Boot Test支持
   - 模拟真实环境配置
   - 端到端API测试

3. 性能测试
   - JMeter压力测试
   - 验证高并发场景
   - 监控系统指标
   - 测试限流熔断

4. 安全测试
   - OWASP安全测试
   - 渗透测试
   - 弱密码测试
   - SQL注入测试

5. 自动化测试
   - CI/CD流水线集成
   - 自动化测试报告
   - 测试环境管理
   - 回归测试策略



#### 2.3 dove-user

##### 2.3.1 需求描述
###### 2.3.1.1 功能描述
  - 模块职责
    - 用户信息管理
    - 用户偏好设置
    - 权限管理
    - 用户数据同步

  - 核心流程
    ```mermaid
    sequenceDiagram
        participant C as Client
        participant G as Gateway
        participant U as User Service
        participant DB as Database
        participant Cache as Redis
        
        C->>G: 获取用户信息
        G->>U: 转发请求
        U->>Cache: 查询缓存
        alt 缓存命中
            Cache-->>U: 返回数据
        else 缓存未命中
            U->>DB: 查询数据库
            DB-->>U: 返回数据
            U->>Cache: 更新缓存
        end
        U-->>G: 返回用户信息
        G-->>C: 响应
    ```
###### 2.3.1.2 非功能描述

1. 性能需求
   - 响应时间
     - API平均响应时间 < 100ms
     - 95%请求响应时间 < 200ms 
     - 99.9%请求响应时间 < 500ms
   - 并发能力
     - 单实例支持 1000 QPS
     - 可水平扩展支持更高并发
     - 用户数据缓存命中率 > 95%
   - 可用性
     - 服务可用性 99.99%
     - 故障恢复时间 < 1分钟
     - 支持多活部署

2. 安全需求
   - 数据安全
     - 敏感数据加密存储
     - 传输数据TLS加密
     - 定期数据备份
   - 访问控制
     - 基于RBAC的权限控制
     - API访问鉴权
     - 操作日志审计
   - 防护措施
     - SQL注入防护
     - XSS防护
     - CSRF防护

3. 可维护性
   - 代码规范
     - 遵循阿里巴巴Java开发规范
     - 单元测试覆盖率 > 80%
     - 代码注释完整规范
   - 监控运维
     - 接入APM监控
     - 全链路日志追踪
     - 系统指标监控告警
   - 文档支持
     - 详细的API文档
     - 运维手册
     - 故障处理手册

4. 扩展性
   - 技术扩展
     - 支持插件化扩展
     - 预留技术升级接口
     - 模块间低耦合
   - 业务扩展
     - 支持自定义用户属性
     - 支持灵活的权限模型
     - 支持多租户隔离

5. 兼容性
   - 系统兼容
     - 支持主流Linux发行版
     - 支持容器化部署
     - 支持云原生架构
   - 数据兼容
     - 支持主流数据库
     - 支持数据迁移工具
     - 版本向下兼容


##### 详细设计
###### 2.3.2.1 数据模型设计 (Data Model Design)

- 2.3.2.1.1 数据模型设计需要满足以下要点:

1. 用户管理
   - 支持用户基础信息管理 (dove_user表)
     * 用户名、密码、邮箱、手机号等基础信息
     * 用户状态、类型、租户等管理属性
     * 最后登录时间、IP等统计信息
     * 创建人、创建时间、更新人、更新时间等审计字段
     * 版本号字段用于乐观锁控制
   
   - 支持用户详细信息扩展 (dove_user_info表)
     * 真实姓名、头像、性别等个人信息
     * 生日、地址等扩展信息
     * 自定义字段支持(JSON格式)
     * 创建人、创建时间、更新人、更新时间等审计字段
     * 版本号字段用于乐观锁控制
   
   - 支持用户安全信息管理 (dove_user_security表)
     * 密码修改历史记录(最近5次)
     * 安全问题答案(加密存储)
     * MFA认证配置(支持多种认证方式)
     * 登录IP白名单(支持IP段)
     * 安全等级设置(高中低)
     * 创建人、创建时间、更新人、更新时间等审计字段
     * 版本号字段用于乐观锁控制
   
   - 支持用户会话管理 (dove_user_session表)
     * 会话Token记录(JWT格式)
     * 登录设备信息(设备指纹)
     * 会话状态跟踪(在线、离线、强制下线)
     * 会话超时设置(可配置)
     * 并发会话控制(最大会话数)
     * 创建时间、更新时间、过期时间等时间字段
   
   - 支持用户组织架构管理 (dove_department表)
     * 部门树形结构(左右值编码)
     * 部门基本信息(编码、名称、简称)
     * 部门负责人(支持多负责人)
     * 上下级关系(层级限制)
     * 部门权限范围(数据权限)
     * 创建人、创建时间、更新人、更新时间等审计字段
     * 版本号字段用于乐观锁控制
   
   - 支持用户权限角色管理 (dove_role, dove_permission表)
     * 角色定义与分配(支持多角色)
     * 权限项配置(细粒度权限控制)
     * 角色-权限关系(多对多)
     * 用户-角色关系(多对多)
     * 权限继承体系(支持权限传递)
     * 创建人、创建时间、更新人、更新时间等审计字段
     * 版本号字段用于乐观锁控制



2.3.2.1.2 数据表设计 (Database Table Design):

根据用户管理、安全管理、系统管理和业务扩展的需求,设计以下数据表:

1. 用户管理相关表
   - dove_user: 用户基础信息表
     * id: bigint(20) - 主键,雪花算法生成
     * username: varchar(50) - 用户名,唯一索引
     * password: varchar(128) - 密码,加密存储
     * email: varchar(100) - 邮箱,唯一索引
     * mobile: varchar(20) - 手机号,唯一索引
     * status: tinyint - 状态(0-禁用,1-启用,2-锁定)
     * type: tinyint - 用户类型(0-普通用户,1-管理员)
     * tenant_id: bigint(20) - 租户ID
     * dept_id: bigint(20) - 部门ID
     * two_factor_enabled: tinyint - 是否启用双因素认证(0-禁用,1-启用)
     * two_factor_type: varchar(20) - 双因素认证类型(GOOGLE/SMS/EMAIL)
     * two_factor_secret: varchar(100) - 双因素认证密钥
     * two_factor_recovery_codes: varchar(512) - 恢复码列表(JSON)
     * created_by: bigint(20) - 创建人ID
     * created_time: datetime - 创建时间
     * updated_by: bigint(20) - 更新人ID
     * updated_time: datetime - 更新时间
     * deleted: tinyint - 逻辑删除标记
     * version: int - 乐观锁版本号

   - dove_user_info: 用户详细信息表
     * id: bigint(20) - 主键,关联dove_user.id
     * real_name: varchar(50) - 真实姓名
     * avatar: varchar(255) - 头像URL
     * gender: tinyint - 性别(0-未知,1-男,2-女)
     * birthday: date - 生日
     * country: varchar(50) - 国家
     * province: varchar(50) - 省份
     * city: varchar(50) - 城市
     * address: varchar(255) - 详细地址
     * timezone: varchar(50) - 时区
     * language: varchar(20) - 语言偏好
     * extend_fields: text - 扩展字段(JSON)
     * created_by: bigint(20) - 创建人ID
     * created_time: datetime - 创建时间
     * updated_by: bigint(20) - 更新人ID
     * updated_time: datetime - 更新时间
     * version: int - 乐观锁版本号

   - dove_user_security: 用户安全信息表
     * id: bigint(20) - 主键,关联dove_user.id
     * mfa_enabled: tinyint - 是否启用MFA
     * mfa_key: varchar(128) - MFA密钥
     * mfa_backup_codes: varchar(512) - MFA备用码
     * password_policy: varchar(255) - 密码策略(JSON)
     * password_reset_token: varchar(100) - 密码重置token
     * token_expire_time: datetime - token过期时间
     * login_attempts: int - 登录尝试次数
     * lock_until: datetime - 锁定截止时间
     * security_questions: varchar(512) - 安全问题(JSON)
     * created_time: datetime - 创建时间
     * updated_time: datetime - 更新时间


   - dove_department: 部门组织架构表
     * id: bigint(20) - 主键,雪花算法生成
     * parent_id: bigint(20) - 父部门ID
     * dept_code: varchar(50) - 部门编码
     * dept_name: varchar(100) - 部门名称
     * dept_short_name: varchar(50) - 部门简称
     * leader_ids: varchar(255) - 负责人ID列表
     * left_code: int - 左值编码
     * right_code: int - 右值编码
     * level: int - 层级深度
     * sort: int - 排序号
     * data_scope: varchar(255) - 数据权限范围
     * tenant_id: bigint(20) - 租户ID
     * created_by: bigint(20) - 创建人ID
     * created_time: datetime - 创建时间
     * updated_by: bigint(20) - 更新人ID
     * updated_time: datetime - 更新时间
     * deleted: tinyint - 逻辑删除标记
     * version: int - 乐观锁版本号

   - dove_role: 角色表
     * id: bigint(20) - 主键,雪花算法生成
     * role_code: varchar(50) - 角色编码
     * role_name: varchar(100) - 角色名称
     * role_type: tinyint - 角色类型(0-系统角色,1-自定义角色)
     * status: tinyint - 状态(0-禁用,1-启用)
     * data_scope: varchar(255) - 数据权限范围
     * remark: varchar(255) - 备注说明
     * tenant_id: bigint(20) - 租户ID
     * created_by: bigint(20) - 创建人ID
     * created_time: datetime - 创建时间
     * updated_by: bigint(20) - 更新人ID
     * updated_time: datetime - 更新时间
     * deleted: tinyint - 逻辑删除标记
     * version: int - 乐观锁版本号

   - dove_permission: 权限表
     * id: bigint(20) - 主键,雪花算法生成
     * parent_id: bigint(20) - 父权限ID
     * perm_code: varchar(100) - 权限编码
     * perm_name: varchar(100) - 权限名称
     * perm_type: tinyint - 权限类型(0-目录,1-菜单,2-按钮)
     * path: varchar(255) - 路由地址
     * component: varchar(255) - 组件路径
     * perms: varchar(255) - 权限标识
     * icon: varchar(100) - 图标
     * sort: int - 排序号
     * visible: tinyint - 是否可见
     * status: tinyint - 状态(0-禁用,1-启用)
     * tenant_id: bigint(20) - 租户ID
     * created_by: bigint(20) - 创建人ID
     * created_time: datetime - 创建时间
     * updated_by: bigint(20) - 更新人ID
     * updated_time: datetime - 更新时间
     * deleted: tinyint - 逻辑删除标记
     * version: int - 乐观锁版本号

   - dove_user_role: 用户角色关联表
     * id: bigint(20) - 主键,雪花算法生成
     * user_id: bigint(20) - 用户ID
     * role_id: bigint(20) - 角色ID
     * tenant_id: bigint(20) - 租户ID
     * created_time: datetime - 创建时间

   - dove_role_permission: 角色权限关联表
     * id: bigint(20) - 主键,雪花算法生成
     * role_id: bigint(20) - 角色ID
     * permission_id: bigint(20) - 权限ID
     * tenant_id: bigint(20) - 租户ID
     * created_time: datetime - 创建时间



## 三、关键特性与高并发支持

1. **高并发 (8000 万在线)**  
   - 利用 **Nacos/Eureka** 做服务注册与发现，保证多实例水平扩容；  
   - **Sentinel** (或 Resilience4j) 在网关与核心服务处进行限流/熔断；  
   - **消息队列** (RocketMQ/Kafka) 分解高峰流量，处理异步操作；  
   - **分布式缓存** (Redis) 缓解数据库读写压力。

2. **国际化 (i18n)**  
   - 通过 `common-libs` 提供多语言资源，Spring 会根据请求 locale 选取对应翻译；  
   - 登录错误提示、UI 提示文本都可在不同语言文件中维护。

3. **大规模 CI/CD**  
   - 在 Jenkins / GitLab CI 中：  
     - 检出 -> `mvn clean package` -> 单元测试 + 集成测试 -> 生成 Jar / Docker 镜像 -> 推送镜像仓库；  
   - **分环境**：`-Pdev` / `-Pprod` / … 切换配置；  
   - **灰度发布**：先在小部分节点更新镜像，验证通过后全量发布。

4. **JIRA Web 登录还原**  
   - UI/交互：前端可完全复刻 JIRA 样式；后端提供相同参数名称与错误码；  
   - **Remember Me** Cookie：可设定 14 天或自定义有效期；  
   - **锁定策略**：在 `auth-service` 层面设定若多次密码错误则锁定 X 分钟 / 超过阈值触发验证码。

---

## 四、验收与标准

1. **功能验收**  
   - 登录/注销功能在多语言环境下正常；错误提示与 JIRA 文案一致；  
   - gateway -> auth-service -> user-service 等调用通路正常。

2. **性能验证**  
   - JMeter / Gatling 模拟 8000 万并发：  
     - 测试登录接口 TPS、平均响应时间、错误率；  
     - 限流是否生效，gateway-service 不会因超载崩溃。

3. **安全与许可证合规**  
   - SonarQube/Checkstyle/OWASP Dependency Check 报告无高危漏洞；  
   - 所有依赖的 LICENSE 无商业冲突，含有足够文档说明。

4. **国际化与多时区**  
   - 切换语言后，登录页面与错误信息同步更新；  
   - 不同时区用户能够正常访问，时间/日期显示正确。

5. **可维护与扩展**  
   - 如需拆分新微服务，只需**继承父 POM** 即可获取统一依赖和插件；  
   - 能够平滑升级 Spring Boot/Cloud/Alibaba 版本以获取最新社区支持。

---

## 五、总结




本需求文档描述了在 **2 亿用户、8000 万并发**的 **SaaS 国际化**场景下，如何使用 **Maven** 来统一管理 **多微服务**（基于 Spring Cloud & Alibaba）所需的依赖、插件和构建流程，并详细说明了 **JIRA Web 登录**的关键细节、国际化支持、高并发应对策略，以及 CI/CD、许可证合规等要求。  

请团队成员在落地实施时，参照此文档完成以下要点：

1. **建立父 POM**：包含 `<dependencyManagement>` 与 `<pluginManagement>`；  
2. **创建子模块**：`common-libs`, `auth-service`, `gateway-service`, 及各业务服务；  
3. **配置 CI/CD**：在流水线自动执行 Maven 构建与测试、镜像化，满足灰度发布与版本管理需求；  
4. **验证功能**：确保 JIRA 风格登录完全无误，多语言、Remember Me、错误提示、锁定策略均正确实现；  
5. **高并发演练**：使用压测工具测试在极端流量下的系统稳健性，结合限流熔断、消息队列、分布式缓存等手段进行调优。  

通过本需求，企业可在大型分布式团队协同下，快速、规范地构建并持续迭代一个国际化、高并发、高可靠的微服务产品。

---

**本次更新内容 (第 10 次最终完善)：**  
1. **整合**：前九次所有要点，包括许可证合规、二次验证、错误提示示例、Cookie 过期策略等；  
2. **细化**：锁定/延迟防暴力破解、灰度发布与版本管理；  
3. **完善**：对 SaaS 国际化多租户、高并发注册发现、消息队列的使用，并明确验收标准。  