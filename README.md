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
- `app`：AI 助教核心应用服务（聊天 / RAG）
- `advisor`：对话增强器（敏感词、日志等）
- `rag`：知识库加载与向量库初始化
- `chatmemory`：会话记忆存储（Redis / 文件）

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
  data:
    redis:
      host: localhost
      port: 6379
      database: 0
      password: 1234555A
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

## 已实现的 AI / RAG 能力

- AI 助教主流程：`AssistantApp#doChat`
- 多轮对话记忆：`MessageWindowChatMemory + RedisChatMemoryRepository`
- 检索增强问答（RAG）：`AssistantApp#doChatWithRag`
- RAG 组装：`RetrievalAugmentationAdvisor + VectorStoreDocumentRetriever(topK=10)`
- 知识库来源：`backend/src/main/resources/knowledge/java-course/*.md`
- 启动时向量化：`AssistantAppVectorStoreConfig` 调用 `EmbeddingModel` 构建 `SimpleVectorStore`

## 测试与验证

执行指定测试：

```bash
cd backend
mvn -q -Dtest=AssistantAppTest test
```

只验证 RAG：

```bash
cd backend
mvn -q -Dtest=AssistantAppTest#doChatWithRag test
```

判定 RAG 生效的关键日志：

- `SimpleVectorStore : Calling EmbeddingModel for document id = ...`
- `MyLoggerAdvisor call cost=...`
- `AssistantApp : content: ...`

## 注意事项

- `application-local.yml` 已加入 `.gitignore`，请勿提交真实密钥。
- 该工程当前使用外部 DashScope 模型服务，网络不通时测试可能报错：
  - `ResourceAccessException`
  - `UnresolvedAddressException`
- `controller` 当前只有健康检查接口；聊天/RAG 逻辑已在 `AssistantApp` 完成，若需 HTTP 调用需新增业务 Controller。
- `SpringAiAiInvoke` 默认未启用（`@Component` 已注释），需要时可打开用于连通性自检。
