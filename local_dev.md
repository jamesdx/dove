# Dove API Gateway 本地开发指南

## 环境要求

- JDK 17
- Maven 3.9+
- Docker Desktop
- Git

## 本地开发环境搭建

### 1. 克隆代码仓库

```bash
git clone <repository-url>
cd dove
```

### 2. 启动依赖服务

使用 Docker Compose 启动必要的基础服务:

```bash
cd dove-api-gateway
docker-compose up -d
```

这将启动以下服务:
- Nacos (配置中心和服务发现): localhost:8848
- MySQL: localhost:3306
- Redis: localhost:6379

### 3. 初始化数据库

等待 MySQL 完全启动后，执行以下命令初始化数据库:

```bash
mysql -h localhost -P 3306 -u root -proot123 nacos_config < src/main/resources/schema.sql
mysql -h localhost -P 3306 -u root -proot123 nacos_config < src/main/resources/data.sql
```

### 4. 编译和启动应用

```bash
mvn clean package -DskipTests
mvn spring-boot:run
```

应用将在 http://localhost:8080 启动

## 验证部署

### 1. 检查服务健康状态

```bash
curl http://localhost:8080/actuator/health
```

### 2. 检查 Nacos 服务注册

访问 Nacos 控制台: http://localhost:8848/nacos
- 用户名: nacos
- 密码: nacos

在服务列表中应该能看到 `dove-api-gateway` 服务。

### 3. 检查监控指标

```bash
# 查看所有可用的 Actuator 端点
curl http://localhost:8080/actuator

# 查看 Prometheus 指标
curl http://localhost:8080/actuator/prometheus

# 查看断路器状态
curl http://localhost:8080/actuator/circuitbreakers

# 查看限流器状态
curl http://localhost:8080/actuator/ratelimiters
```

## 常见问题

1. 如果端口 8080 被占用:
   - 修改 `application.yml` 中的 `server.port` 配置

2. 如果 Nacos 连接失败:
   - 检查 Docker 容器状态: `docker ps`
   - 查看 Nacos 日志: `docker logs dove-nacos`

3. 如果数据库连接失败:
   - 确保 MySQL 容器正常运行
   - 检查数据库用户名密码配置
   - 验证数据库和表是否正确创建

## 开发建议

1. 使用 dev profile 进行本地开发:
   ```bash
   mvn spring-boot:run -Dspring.profiles.active=dev
   ```

2. 修改配置后重启服务:
   ```bash
   mvn spring-boot:run
   ```

3. 查看应用日志:
   ```bash
   tail -f logs/spring.log
   ``` 