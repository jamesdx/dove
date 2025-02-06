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

### 1. 父 POM (Parent POM)

1. **依赖管理 (dependencyManagement)**  
   - **Spring Cloud 基础依赖**
     - `spring-boot-starter-parent:3.1.5`
     - `spring-cloud-dependencies:2022.0.4`
     - `spring-cloud-alibaba-dependencies:2022.0.0.0`

   - **Spring Cloud 核心组件**
     - `spring-cloud-starter-bootstrap:4.0.4`
     - `spring-cloud-starter-loadbalancer:4.0.4`
     - `spring-cloud-starter-openfeign:4.0.4`

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

   - **数据存储与缓存**
     - `mariadb-java-client:3.1.4`
     - `spring-boot-starter-data-redis:3.1.5`
     - `redisson-spring-boot-starter:3.23.5`
     - `spring-boot-starter-data-jpa:3.1.5`
     - `hibernate-core:6.2.13.Final`

   - **消息队列**
     - `rocketmq-spring-boot-starter:2.2.3`
     - `spring-kafka:3.0.12`

   - **监控日志**
     - `spring-boot-starter-actuator:3.1.5`
     - `micrometer-registry-prometheus:1.11.5`
     - `spring-boot-starter-log4j2:3.1.5`
     - `logstash-logback-encoder:7.4`

   - **国际化支持**
     - `spring-boot-starter-thymeleaf:3.1.5`
     - `thymeleaf-extras-springsecurity6:3.1.2.RELEASE`
     - `spring-boot-starter-validation:3.1.5`

   - **工具类库**
     - `commons-lang3:3.13.0`
     - `guava:32.1.2-jre`
     - `fastjson2:2.0.40`
     - `mapstruct:1.5.5.Final`
     - `lombok:1.18.30`

   - **API文档**
     - `springdoc-openapi-starter-webmvc-ui:2.2.0`
     - `springdoc-openapi-starter-webflux-ui:2.2.0`  // 如果使用WebFlux

   - **分布式组件**
     - `spring-boot-starter-cache:3.1.5`
     - `caffeine:3.1.8`  // 本地缓存
     - `xxl-job-core:2.4.0`  // 分布式任务调度

   - **测试框架**
     - `spring-boot-starter-test:3.1.5`
     - `testcontainers:1.19.1`  // 容器化测试
     - `mockito-core:5.7.0`
     - `junit-jupiter:5.10.0`

   - **链路追踪**
     - `micrometer-tracing-bridge-brave:1.1.5`
     - `spring-cloud-starter-sleuth:3.1.9`
     - `spring-cloud-sleuth-zipkin:3.1.9`

2. **插件管理 (pluginManagement)**  
   - **编译**：`maven-compiler-plugin` (Java 17)  
   - **测试**：`maven-surefire-plugin` / `maven-failsafe-plugin` + Jacoco (单元与集成测试覆盖率)  
   - **容器化**：Docker (fabric8 / dockerfile-maven-plugin / jib)，Helm/K8s (如 `io.fabric8` 或 `com.google.cloud.tools.jib`)  
   - **质量审计**：SonarQube (sonar-maven-plugin), Checkstyle, OWASP Dependency Check  
   - **Profile**：可定义 `dev`, `test`, `staging`, `prod` 等 Profile 控制编译/打包/日志级别

3. **许可证合规**  
   - 父 POM 中记录各依赖的 LICENSE 信息（Apache 2.0、MIT、BSD 等），禁止接入可能冲突商业化的 GPLv3 等；  
   - 定期审阅第三方依赖，升级或替换存在安全/许可证风险的库。

### 2. 子模块 (微服务) 设计

1. **common-libs**  
   - 公用工具：加解密、日期处理、HTTP/JSON 工具；  
   - 多语言资源文件：`messages_en.properties`, `messages_zh.properties` …；  
   - 公共 DTO、异常类、日志格式封装。

2. **auth-service**  
   - 模拟 **JIRA Web 登录**：  
     - **表单字段**：`username`, `password`, `rememberMe`；  
     - **错误提示**：与 JIRA 保持一致，如 "Sorry, your username and password are incorrect."；  
     - **Remember Me**：Cookie 存储 + Session/Token；  
     - 防暴力破解策略 (锁定或延时)；  
     - 多语言支持，根据用户 locale 返回对应提示。  
   - 可选：**2FA**(二次验证)、OAuth2 / SSO 整合。

3. **gateway-service**  
   - **API 网关**：基于 Spring Cloud Gateway / Netflix Zuul；  
   - 集成 **Sentinel / Resilience4j** 进行熔断限流，跨域处理；  
   - 统一鉴权过滤器，与 `auth-service` 配合校验用户登录态；  
   - 日志与追踪 (Sleuth/Zipkin/Micrometer) 可在此集中处理。

4. **其他服务**  
   - 业务领域服务 (user-service, product-service, order-service...)；  
   - 各自管理数据库或外部存储；  
   - 引入 `common-libs` 共享 DTO、工具、异常定义；  
   - 依赖父 POM 版本锁定，一致的 Java 17、测试、Docker 打包策略。

---

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