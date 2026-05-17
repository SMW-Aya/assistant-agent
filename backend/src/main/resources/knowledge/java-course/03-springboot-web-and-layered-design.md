# Java 后端学习笔记 03：Spring Boot Web 与分层设计（详细版）

## 1. 学习目标
- 掌握 Spring Boot Web 请求处理主链路。
- 建立 Controller / Service / Repository 的职责边界。
- 实现统一响应、统一异常、参数校验、日志审计等基础能力。
- 形成可扩展、可维护的项目工程规范。

## 2. Spring Boot Web 请求处理主链路
1. 前端发送 HTTP 请求。
2. `DispatcherServlet` 接收并路由。
3. 通过 `HandlerMapping` 找到目标 Controller 方法。
4. 参数解析与数据绑定（Query、Path、Body）。
5. 执行拦截器、校验器、AOP。
6. Controller 调用 Service。
7. Service 调用 Repository / Mapper 查询或写库。
8. 返回 DTO/VO，经序列化输出 JSON。

## 3. 分层架构设计
### 3.1 Controller 层
职责：
- 接收参数。
- 参数校验。
- 权限校验入口（可结合拦截器或注解）。
- 返回统一响应对象。

反例：
- Controller 中写 SQL。
- Controller 中写事务。

### 3.2 Service 层
职责：
- 承载业务逻辑。
- 业务规则校验。
- 编排多个外部依赖（DB、缓存、消息、模型服务）。

### 3.3 Repository / Mapper 层
职责：
- 纯数据访问。
- 保持 SQL 与数据结构职责单一。

## 4. DTO / VO / Entity 分离
- Entity：数据库实体，贴近表结构。
- DTO：接口输入参数对象。
- VO：接口输出展示对象。
- 避免 Entity 直接暴露给前端，降低字段泄漏与耦合风险。

## 5. 统一响应结构
建议格式：
- `code`：业务状态码
- `message`：说明信息
- `data`：业务数据

这样前端可统一处理成功与失败分支，减少重复判断。

## 6. 全局异常处理
使用 `@RestControllerAdvice` + `@ExceptionHandler`：
- 捕获业务异常（如参数非法、资源不存在）。
- 捕获系统异常（如 NPE、SQL 异常）。
- 映射统一错误码。

注意：
- 日志记录要带 traceId / 请求路径 / 关键参数。
- 对用户返回友好信息，对日志保留技术细节。

## 7. 参数校验
### 7.1 常用注解
- `@NotNull`、`@NotBlank`、`@Size`、`@Min`、`@Max`、`@Email`

### 7.2 使用方式
- Controller 参数加 `@Valid`。
- 复杂场景可自定义校验注解。

### 7.3 常见坑
- 忘记加 `@Valid` 导致注解不生效。
- 校验失败异常未统一处理。

## 8. 跨域与拦截器
### 8.1 CORS
- 本地前后端分离调试需放开对应来源。
- 生产环境不要无脑 `*`，应配置白名单。

### 8.2 拦截器
- 记录请求日志、耗时、用户标识。
- 可做幂等校验、防重复提交等通用能力。

## 9. 文档与可观测性
- 使用 Knife4j / Swagger 自动生成 API 文档。
- 统一接口命名规范与注释。
- 输出结构化日志，便于 ELK/可观测平台检索。

## 10. 课程助教 Agent 场景下的分层示例
- Controller：`/chat` 接口，接收问题、会话 ID。
- Service：调用 Agent 编排（memory + rag + tool）。
- Repository：存储会话历史、反馈记录、知识检索日志。
- Integration：调用 DashScope / 向量检索服务。

## 11. 常见架构问题
- 把大模型调用逻辑写在 Controller，导致难复用难测试。
- 不区分“业务异常”和“系统异常”，错误码混乱。
- 没有统一日志切面，线上问题排查困难。

## 12. 练习与实验任务
1. 实现课程查询接口：分页、过滤、排序。
2. 完成统一异常处理并定义错误码字典。
3. 增加请求日志拦截器并输出耗时。
4. 使用 Swagger 验证接口可读性。

## 13. 面试问答
- Spring Boot 自动配置原理是什么？
- 为什么要做分层设计？
- 全局异常处理和局部 try-catch 如何取舍？
- DTO/VO/Entity 为什么要分离？

## 14. 本章总结
分层设计是后端项目工程化的关键。写得快不难，写得稳、可扩展、可维护才是核心竞争力。你后续的 RAG、工具调用、MCP 集成都必须建立在这个地基上。
