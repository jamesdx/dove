# 
# 整体项目结构
```
dove-project/                                      # 项目根目录
├── .github/                              # GitHub配置目录
│   ├── workflows/                        # GitHub Actions工作流
│   │   ├── build.yml                    # 构建流程
│   │   ├── deploy.yml                   # 部署流程
│   │   └── test.yml                     # 测试流程
│   └── ISSUE_TEMPLATE/                  # Issue模板
│
├── dove-parent/                         # 依赖管理项目
│   ├── checkstyle/                     # 代码风格检查配置
│   │   └── checkstyle.xml             # Checkstyle规则
│   ├── pmd/                           # PMD静态代码分析配置
│   │   └── ruleset.xml               # PMD规则集
│   └── pom.xml                       # 统一依赖管理
│
├── dove-common/                          # 公共模块项目
│   ├── dove-common-core/                # 核心工具模块
│   │   ├── src/main/java/com/dove/common/core/
│   │   │   ├── annotation/             # 注解类
│   │   │   ├── constant/              # 常量定义
│   │   │   ├── context/              # 上下文
│   │   │   ├── exception/            # 异常类
│   │   │   ├── utils/               # 工具类
│   │   │   └── validation/          # 验证工具
│   │   └── pom.xml
│   │
│   ├── dove-common-redis/              # Redis工具模块
│   │   ├── src/main/java/com/dove/common/redis/
│   │   │   ├── config/              # Redis配置
│   │   │   ├── service/            # Redis服务
│   │   │   └── utils/             # Redis工具
│   │   └── pom.xml
│   │
│   ├── dove-common-security/           # 安全工具模块
│   │   ├── src/main/java/com/dove/common/security/
│   │   │   ├── annotation/         # 安全注解
│   │   │   ├── config/            # 安全配置
│   │   │   ├── handler/          # 处理器
│   │   │   └── utils/           # 安全工具
│   │   └── pom.xml
│   │
│   ├── dove-common-log/               # 日志模块
│   │   ├── src/main/java/com/dove/common/log/
│   │   │   ├── annotation/        # 日志注解
│   │   │   ├── aspect/          # 日志切面
│   │   │   ├── event/          # 日志事件
│   │   │   └── service/       # 日志服务
│   │   └── pom.xml
│   │
│   ├── dove-common-swagger/          # Swagger文档模块
│   │   ├── src/main/java/com/dove/common/swagger/
│   │   │   ├── config/           # Swagger配置
│   │   │   └── plugins/         # Swagger插件
│   │   └── pom.xml
│   │
│   └── pom.xml                      # Common父POM
│
├── dove-auth/                         # 认证服务项目
│   ├── src/main/java/com/dove/auth/
│   │   ├── config/                   # 配置类
│   │   ├── controller/              # 控制器
│   │   │   ├── LoginController.java # 登录控制器
│   │   │   ├── OAuth2Controller.java # OAuth2控制器
│   │   │   └── SSOController.java   # SSO控制器
│   │   ├── service/                # 服务层
│   │   │   ├── impl/             # 服务实现
│   │   │   └── AuthService.java  # 认证服务接口
│   │   ├── repository/           # 数据访问层
│   │   └── model/               # 数据模型
│   ├── src/main/resources/
│   │   ├── application.yml     # 应用配置
│   │   ├── bootstrap.yml      # 启动配置
│   │   └── i18n/             # 国际化资源
│   └── pom.xml
│
├── dove-gateway/                    # 网关服务项目
│   ├── src/main/java/com/dove/gateway/
│   │   ├── config/               # 配置类
│   │   ├── filter/              # 过滤器
│   │   │   ├── AuthFilter.java # 认证过滤器
│   │   │   ├── RateLimitFilter.java # 限流过滤器
│   │   │   └── LoggingFilter.java # 日志过滤器
│   │   ├── handler/           # 处理器
│   │   │   ├── ErrorHandler.java # 错误处理
│   │   │   └── FallbackHandler.java # 降级处理
│   │   └── security/         # 安全配置
│   └── pom.xml
│
├── dove-user/                    # 用户服务项目
│   ├── src/main/java/com/dove/user/
│   │   ├── controller/        # 控制器
│   │   │   ├── UserController.java
│   │   │   └── ProfileController.java
│   │   ├── service/          # 服务层
│   │   │   ├── UserService.java
│   │   │   └── impl/
│   │   ├── repository/      # 数据访问层
│   │   └── model/          # 数据模型
│   └── pom.xml
│
├── dove-security/              # 安全服务项目
│   ├── src/main/java/com/dove/security/
│   │   ├── controller/      # 控制器
│   │   ├── service/        # 服务层
│   │   │   ├── MFAService.java # 多因素认证服务
│   │   │   ├── CaptchaService.java # 验证码服务
│   │   │   └── impl/
│   │   ├── repository/    # 数据访问层
│   │   └── model/        # 数据模型
│   └── pom.xml
│
├── dove-monitor/              # 监控服务项目
│   ├── src/main/java/com/dove/monitor/
│   │   ├── config/         # 配置类
│   │   ├── endpoint/      # 监控端点
│   │   │   ├── MetricsEndpoint.java
│   │   │   └── HealthEndpoint.java
│   │   └── metrics/      # 指标收集
│   │       ├── LoginMetrics.java
│   │       └── UserMetrics.java
│   └── pom.xml
│
├── dove-web/                 # 前端项目
│   ├── src/
│   │   ├── api/            # API接口
│   │   │   ├── auth/      # 认证相关API
│   │   │   ├── user/     # 用户相关API
│   │   │   └── system/   # 系统相关API
│   │   ├── assets/        # 静态资源
│   │   │   ├── images/   # 图片资源
│   │   │   ├── styles/  # 样式文件
│   │   │   └── icons/   # 图标资源
│   │   ├── components/   # 组件
│   │   │   ├── login/   # 登录相关组件
│   │   │   │   ├── LoginForm.tsx
│   │   │   │   ├── MFAVerification.tsx
│   │   │   │   └── SSOLogin.tsx
│   │   │   └── common/  # 通用组件
│   │   ├── hooks/      # 自定义hooks
│   │   ├── layouts/    # 布局
│   │   ├── locales/   # 国际化资源
│   │   │   ├── en-US/
│   │   │   ├── zh-CN/
│   │   │   └── ja-JP/
│   │   ├── pages/    # 页面
│   │   │   ├── login/
│   │   │   ├── sso/
│   │   │   └── error/
│   │   ├── stores/   # 状态管理
│   │   ├── styles/  # 样式
│   │   ├── types/   # TypeScript类型
│   │   └── utils/   # 工具类
│   ├── public/     # 公共资源
│   ├── tests/     # 测试文件
│   │   ├── unit/
│   │   └── e2e/
│   ├── .env      # 环境变量
│   ├── package.json
│   └── tsconfig.json
│
├── dove-i18n/                # 国际化资源项目
│   ├── messages/           # 多语言消息
│   │   ├── en_US/        # 英文
│   │   ├── zh_CN/       # 简体中文
│   │   └── ja_JP/      # 日文
│   └── pom.xml
│
├── dove-cache/              # 缓存服务项目
│   ├── src/main/java/com/dove/cache/
│   │   ├── config/       # 缓存配置
│   │   ├── service/     # 缓存服务
│   │   └── utils/      # 缓存工具
│   └── pom.xml
│
├── docker/                # Docker配置
│   ├── auth/            # 认证服务Docker
│   ├── gateway/        # 网关服务Docker
│   └── nginx/         # Nginx配置
│
├── k8s/                # Kubernetes配置
│   ├── auth/         # 认证服务K8s
│   ├── gateway/     # 网关服务K8s
│   └── ingress/    # Ingress配置
│
├── scripts/         # 脚本文件
│   ├── build/     # 构建脚本
│   ├── deploy/   # 部署脚本
│   └── test/    # 测试脚本
│
├── docs/          # 项目文档
│   ├── api/     # API文档
│   ├── design/ # 设计文档
│   └── guide/  # 使用指南
│
├── .gitignore    # Git忽略文件
├── README.md    # 项目说明
└── LICENSE     # 开源协议
```


# 项目结构说明：

1. **核心服务模块**
   - `dove-parent`: 它是 统一依赖管理，包含代码质量检查配置, 它 是maven  项目的 父项目，其他项目为子项目，其他子项目要继承 dove-parent项目，
   - `dove-common`: 公共功能模块，包含核心工具、Redis、安全等，继承 `dove-parent`
   - `dove-auth`: 认证服务，处理用户登录、OAuth2和SSO，继承 `dove-parent`
   - `dove-gateway`: API网关，负责路由、认证、限流等，继承 `dove-parent`
   - `dove-user`: 用户服务，管理用户信息和配置，继承 `dove-parent`
   - `dove-security`: 安全服务，处理MFA、验证码等，继承 `dove-parent`
   - `dove-monitor`: 监控服务，收集系统指标，继承 `dove-parent`

2. **前端模块**
   - `dove-web`: React前端项目，采用TypeScript开发
   - 支持国际化、主题定制、响应式设计

3. **部署配置**
   - `docker`: 容器化配置文件
   - `k8s`: Kubernetes部署配置
   - `scripts`: 自动化脚本

4. **文档与规范**
   - `docs`: 项目文档
   - `.github`: CI/CD和Issue模板
