你是一个有这20年经验丰富的架构师，认真阅读 “00. Enterprise API Gateway Solution - Spring Cloud API Gateway.md” 文档，然后基于“一个能满足2亿用户，8000 万用户同时在线的 互联网 SaaS 国际化产品，这个产品给全球用户使用， 工作时间用频繁，非工作时间是工作时间的访问量30%， 产品支持多终端，技术要求使用前后端分离项目，后端采用 Spring Cloud 和 Spring Cloud Aliababa ， 要采用开源、可商用、文档、社区活跃、支持好的技术”需求，第一步、完善 “三、核心设计下的功能设计的 路由转发模块” 这部分


作为有20年经验的架构师，第一步，请告诉我 如何实现下面需求：“基于 Spring Cloud Gateway 的路由规则配置
            * 支持基于 Path、Host、Header、Query、Method 等多维度的路由匹配
            * 支持正则表达式和模式匹配
            * 支持权重路由
            * 支持自定义路由谓词工厂” ，第二步，帮助我修改  scaffold.md 文件，让 cursor 或者其他 AI 按照需求生成代码；

根据你上面修改内容，把 “equired Files and Directories Structure 的 dove-api-gateway 结构也相应的更新 ”

帮助我修改  scaffold.md 文件，加入你对生成代码的 单元测试 代码的命令，让 cursor 或者其他 AI 按照需求生成代码 ，单元测试代码 要求覆盖你所有的代码的场景，不能有遗漏


请认真分析“00. Enterprise API Gateway Solution - Spring Cloud API Gateway.md” 的 “三、核心设计下功能设计的路由转发模块”内容 ， 同时，再次检查下 scaffold.md 关于生成代码的描述，要求cursor 或者其他 AI 工具 按照 scaffold.md  文档描述内容生成的项目和代码、测试用例代码 必须开箱即用。 如果 需要修改 scaffold.md 文档请帮我修改。 




# Step 1
第一步、请严格按照 scaffold.md 文档内容，在当前目录 生成项目及代码，生成好的代码必须是可以运行的

# Step 2
第一步，基于你上面已生成的内容（dove-parent 和 dove-api-gateway） 和 scaffold.md  要求生成的内容对比，找出没有生成的部分，然后告诉我 , 第二步，根据你没有生成的内容，继续 参照 scaffold.md文档 生成代码，

第一步，基于你上面已生成的内容（dove-parent 和 dove-api-gateway） 的内容， 你还有下面内容没有生成，请根据scaffold.md 内容，在 dove-api-gateway 项目里生成你未生成的代码。 "6. Kubernetes 配置：
service.yml
configmap.yml
secret.yml
启动脚本：
startup.sh
health-check.sh
cleanup.sh"

# Step 3
第一步，一步一步运行程序，有问题理解解决，知道 dove-api-gateway 启动成功，第二步，程序启动后，教我如何验证程序跑通了
第一步，一步一步运行程序，有问题理解解决，并将修正你生成的代码及 scaffold.md文件。”  这个命令里的
·

# Step 4 
请认真阅读 scaffold.md 文档， 分析 dove-parent 和 dove-api-gateway 两个项目，然后根据 “Test Requirement，Test Coverage Requirement，Test Best Practices, Test Dependencies” 生成测试相关的代码。





下一步，我们需要：
1. 修复所有的linter错误，这些错误主要是由于包路径不匹配和依赖问题导致的。
2. 添加必要的配置到application.yml文件中，包括MongoDB和其他服务的配置。
3. 一步，我们需要：创建必要的测试类来验证功能，必须覆盖所有的代码和场景，而且这些测试用例是运行并能生成测试报告的
4. 编写部署相关的文件（Dockerfile, docker-compose.yml等）。
5. 本地开发基于 Docker compose 安装依赖的中间件安装
6. 依赖中间件配置
7. 依赖中间件启动   




好的，我们来创建全面的测试类。我们需要为每个主要组件创建测试类，包括：
控制器测试
服务层测试
3. 仓库层测试
4. 配置测试
集成测试
让我们先创建测试目录结构：


我暂停“创建必要的测试类来验证功能，必须覆盖所有的代码和场景，而且这些测试用例是运行并能生成测试报告的” 这个命令， 因为你无限循环操作。 请你检查下 scaffold.md 文件关于 测试用例 编写内容。重新建必要的测试类来验证功能，必须覆盖所有的代码和场景，而且这些测试用例是运行并能生成测试报告的 