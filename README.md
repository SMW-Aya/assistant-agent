# SRTP Agent Backend

基于 Spring Boot 3.4.4 + Spring AI Alibaba DashScope 的课程助教后端工程骨架。

## 技术栈

- Java 21
- Spring Boot 3.4.4
- Spring AI Alibaba DashScope `1.1.2.2`
- Knife4j / SpringDoc OpenAPI
- Lombok / Hutool

## 目录结构

- `backend/`：后端服务
- `docs/`：架构文档
- `deploy/`：部署相关文件

后端关键包（`backend/src/main/java/com/srtp/agent`）：

- `controller`：接口层
- `common`：通用请求/响应/错误码
- `exception`：自定义异常与全局异常处理
- `config`：跨域、日志拦截、Jackson 配置
- `invoke`：启动时 AI 调用示例

## 环境要求

- JDK 21（推荐 Corretto 21）
- Maven 3.9+

> 项目已在 `pom.xml` 中通过 `maven-enforcer-plugin` 限制 Java 版本为 21。

## 本地配置

主配置：`backend/src/main/resources/application.yml`

- 端口：`8123`
- 上下文：`/api`
- 激活 profile：`local`

本地私有配置：`backend/src/main/resources/application-local.yml`

示例：

```yml
spring:
  ai:
    dashscope:
      api-key: ${DASHSCOPE_API_KEY:}
      chat:
        options:
          model: qwen-plus
```

推荐把密钥放在环境变量中，而不是写死到 yml。

可在 `backend/.env.example` 参考格式，并在本地 shell 导出：

```bash
export DASHSCOPE_API_KEY=你的真实Key
```

## 启动项目

```bash
cd backend
mvn clean spring-boot:run
```

如果本机 `~/.m2` 有权限问题，可用：

```bash
cd backend
mvn -Dmaven.repo.local=.m2/repository clean spring-boot:run
```

## 接口文档

启动成功后访问：

- Swagger UI: `http://localhost:8123/api/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8123/api/v3/api-docs`
- 健康检查: `http://localhost:8123/api/health`

## 已实现的工程基础能力

- 统一响应包装：`BaseResponse` / `ResultUtils`
- 自定义异常：`BusinessException`
- 全局异常处理：`GlobalExceptionHandler`
- 请求包装类：`BaseRequest` / `PageRequest`
- 分页响应：`PageResult`
- 全局跨域配置：`WebMvcCorsConfig`
- 请求日志拦截：`RequestLogInterceptor`（含 `X-Trace-Id`）
- Jackson 时间序列化配置：`JacksonConfig`

## 注意事项

- `application-local.yml` 已加入 `.gitignore`，请勿提交真实密钥。
- `SpringAiAiInvoke` 会在服务启动时触发一次大模型调用，请确保 key 可用。
