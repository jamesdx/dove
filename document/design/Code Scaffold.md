# 企业级 Maven 使用规范与需求说明

## 1. 背景

在大型企业项目中，由于团队规模庞大、微服务数量众多，依赖与版本管理往往非常复杂。为了提升团队协作效率、降低重复配置、避免版本冲突，需要对 Maven 的使用做出一套清晰且统一的规范。本需求文档旨在指导团队在企业级环境下，如何以最佳实践的方式使用 Maven。

---

## 2. 需求目标

1. **统一依赖与版本管理**  
   - 通过 `parent` POM 或 BOM (Bill of Materials) 实现对项目中核心依赖、插件的统一管理，减少重复配置与冲突。
   - 确保所有子模块遵循相同的技术基线（如 Java 版本、Spring Boot 版本、Spring Cloud 版本等）。

2. **标准化项目结构**  
   - 采用多模块 (multi-module) 父子项目结构，或者在分布式环境下维持公共父 POM/BOM，并让各微服务独立仓库继承该公共父级。
   - 统一存放公共工具类、实体 (Entity/DTO) 等，让团队成员可以在公共库 (common-libs) 中维护并复用通用代码。

3. **减少重复配置与重复劳动**  
   - 通过在父 POM `<pluginManagement>` 中统一声明编译、测试、打包等插件的版本与配置，降低子模块的配置负担。
   - 为团队提供可复用的脚手架 (Code Scaffold)，简化新微服务的创建流程。

4. **适配不同环境的部署**  
   - 利用 Maven Profile 或 Spring Cloud Config 等方式区分 `dev/test/stage/prod` 等环境。
   - 在 CI/CD 流水线中可通过 `mvn -P...` 或相应方式自动选择打包策略及依赖。

5. **可扩展、可演进**  
   - 容易引入和升级常用的企业级组件（日志、监控、熔断限流、消息队列等），随项目规模与需求演进而不断扩展。

---

## 3. 需求范围

1. **项目结构**  
   - 要求在根目录有一个 `parent` POM（或 BOM），统一管理依赖。
   - 典型多模块示例：
     ```
     enterprise-parent
     ├── pom.xml                # 父 POM
     ├── common-libs            # 公共库模块
     ├── service-user           # 用户微服务
     ├── service-order          # 订单微服务
     ├── service-product        # 产品微服务
     └── service-gateway        # 网关微服务 (可选)
     ```
2. **父 POM 功能**  
   - `<dependencyManagement>`：锁定依赖版本，如 Spring Boot、Spring Cloud、数据库驱动、日志框架等。
   - `<pluginManagement>`：集中管理常用插件（如编译、测试、Docker 打包、静态检查等）的版本与默认配置。
   - `<properties>`：定义 `java.version`、`spring.boot.version` 等全局属性。
   - `<modules>`：列出所有子模块。
3. **子模块设计**  
   - 每个子模块需继承父 POM，在 `<parent>` 中指定 `groupId/artifactId/version`。
   - 根据实际业务场景添加所需依赖，不要随意添加无关依赖，避免臃肿。
   - 公共库 (common-libs) 存放通用工具类、实体、异常定义等，仅包含真正需要复用的内容。

4. **依赖管理**  
   - 强制使用统一的版本：子模块不允许自行“升级”父 POM 中已锁定的依赖版本，若确有需要，需团队评审后在父 POM 中变更。
   - 对仅少量子模块使用的特殊依赖，尽量**不要**放到父 POM 的 `<dependencies>`，而是只在对应子模块的 `<dependencies>` 中声明（父 POM 中仅管理版本）。

5. **编译与构建**  
   - 要求支持至少 `Java 17` 版本。
   - 推荐使用 `maven-compiler-plugin`、`maven-surefire-plugin`、`maven-failsafe-plugin` 等进行编译与测试，版本统一由父 POM 配置管理。
   - 对于容器化需求，统一使用类似 `dockerfile-maven-plugin` 或 `spring-boot-maven-plugin`（内含镜像构建）进行 Docker 镜像构建。

6. **多环境支持**  
   - 推荐使用 **Spring Cloud Config** / **Nacos Config** 等方式来管理应用配置，Maven Profile 仅在必要时使用（如测试覆盖率、跳过测试、启用特定插件等）。
   - 要求所有子模块提供至少 `application-dev.yml` 与 `application-prod.yml` 两种基础配置文件。

7. **CI/CD 流程**  
   - 在持续集成 (CI) 环境下，优先通过 `mvn clean install` 或 `mvn clean package` 完成多模块打包，并自动运行单元测试。
   - 在产物仓库 (Nexus/Artifactory) 中进行统一的制品管理；版本号一旦发布，不允许子模块私自修改并重复发布。

8. **文档与示例**  
   - 父 POM 中的主要配置、依赖管理原则需在团队文档中说明。
   - 提供至少一个“新微服务”脚手架示例，让新人快速上手。

---

## 4. 验收标准

1. **可编译、可运行**  
   - 使用 `mvn clean install` 可成功编译所有子模块，无版本冲突和编译错误。
   - 任意子模块可单独打包并运行 (如 `service-user`)，输出预期的业务功能或测试结果。

2. **统一规范**  
   - 父 POM 中 `java.version`、`spring-boot.version`、`spring-cloud.version` 与子模块保持一致，防止出现多个版本冲突。
   - 父 POM 中 `<pluginManagement>` 下的配置能被子模块正确继承；子模块仅对个别需要差异化的插件进行“覆盖”配置。

3. **文档清晰**  
   - 提供 README 或团队 Wiki，列明如何添加新微服务模块、如何继承父 POM、如何管理公共依赖版本等。
   - CI/CD 流程文档明确，开发者能够在流水线上自动触发构建、测试、打包、发布镜像。

4. **可持续演进**  
   - 能够随时在父 POM 中升级关键依赖或插件版本，而不会影响主流程的可编译性。
   - 当某子模块需要拆分或升级为独立仓库时，也能平滑迁移并继续复用公共 Parent/BOM。

---

## 5. 附录

- **Maven 官方文档**: [https://maven.apache.org/](https://maven.apache.org/)  
- **Spring Boot 与 Spring Cloud 官方 BOM**: [https://start.spring.io/](https://start.spring.io/)  
- **Docker 打包插件**:  
  - [Spotify Docker Maven Plugin](https://github.com/spotify/dockerfile-maven)  
  - [Fabric8 Maven Plugin](https://github.com/fabric8io/docker-maven-plugin)  
- **其他工具**：SonarQube、Jacoco、Checkstyle、SpotBugs 等，可在 `<pluginManagement>` 中集中配置。

---

> **总结**：  
> 该需求文档旨在确保大型企业在采用 Maven 进行多模块或分布式微服务项目时，能够统一依赖与版本管理、减少重复工作，并保持对项目的可控与可维护性。请各团队成员在开发新微服务或维护既有服务时，严格遵守以上规范与验收标准，一旦有特殊需求或版本冲突，务必在团队内部评审后再做变更。