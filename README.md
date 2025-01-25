# dove

# Dove Login System

基于Spring Cloud Alibaba的微服务登录认证系统。

## 项目结构

```
dove/
├── dove-common/           # 公共模块
├── dove-auth-service/     # 认证服务
├── dove-gateway/         # 网关服务
└── dove-auth-ui/         # 前端项目
```

## 技术栈

- Spring Boot 3.2.3
- Spring Cloud 2023.0.0
- Spring Cloud Alibaba 2022.0.0.0
- Spring Security
- MySQL 8.0
- Redis 7.x
- Nacos 2.2.0
- MyBatis Plus 3.5.5
- JWT

## 开发环境要求

- JDK 17+
- Maven 3.8+
- Docker & Docker Compose
- Node.js 18+ (用于前端开发)

## 快速开始

1. 启动基础服务
```bash
docker-compose up -d
```

2. 初始化数据库
```bash
# 等待MySQL完全启动后执行
docker exec -i dove-mysql mysql -uroot -proot123 < sql/init.sql
```

3. 构建项目
```bash
mvn clean package -DskipTests
```

4. 启动服务
```bash
# 启动认证服务
java -jar dove-auth-service/target/dove-auth-service-1.0.0-SNAPSHOT.jar

# 启动网关服务
java -jar dove-gateway/target/dove-gateway-1.0.0-SNAPSHOT.jar
```

5. 访问服务
- Nacos控制台: http://localhost:8848/nacos
- API文档: http://localhost:8080/doc.html

## 项目特性

- 分布式架构，支持水平扩展
- 基于JWT的无状态认证
- 完整的用户认证和授权功能
- 支持多种登录方式
- 细粒度的权限控制
- 完善的接口文档
- 统一的异常处理
- 多环境配置支持

## 开发指南

详细的开发指南请参考 [document/](document/) 目录下的文档：

- [Login System BRD.md](document/01.requirements/Login%20System%20BRD.md) - 业务需求文档
- [Login System PRD.md](document/02.design/Login%20System%20PRD.md) - 产品需求文档

## 贡献指南

1. Fork 本仓库
2. 创建你的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交你的改动 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开一个 Pull Request

## 许可证

[MIT](LICENSE)