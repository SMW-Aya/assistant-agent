# Java 后端学习笔记 02：集合、泛型与 Stream（详细版）

## 1. 学习目标
- 掌握 List / Set / Map 的底层结构与选型逻辑。
- 理解泛型在类型安全与 API 设计中的价值。
- 熟练使用 Stream 进行过滤、分组、聚合、排序。
- 能避免集合与流式处理中的常见性能与并发陷阱。

## 2. 集合框架全景图
Java 集合分为两大族：
- `Collection`：单值集合（List、Set、Queue）
- `Map`：键值对集合

学习集合不只看 API，还要看：
- 时间复杂度
- 内存占用
- 有序性
- 线程安全策略

## 3. List 深入
### 3.1 ArrayList
- 底层动态数组。
- 随机访问快：`O(1)`。
- 中间插入删除可能触发元素搬移：`O(n)`。

### 3.2 LinkedList
- 双向链表。
- 头尾插入删除快。
- 随机访问慢：`O(n)`。

### 3.3 选型建议
- 绝大多数读多写少场景用 `ArrayList`。
- 频繁首尾操作可考虑 `LinkedList` 或 `ArrayDeque`。

## 4. Set 深入
### 4.1 HashSet
- 基于 `HashMap` 实现。
- 核心价值：去重。
- 无序。

### 4.2 LinkedHashSet
- 在 Hash 结构上维护插入顺序。

### 4.3 TreeSet
- 基于红黑树，天然有序。
- 插入、删除、查找 `O(log n)`。

## 5. Map 深入
### 5.1 HashMap 核心机制
- 数组 + 链表/红黑树。
- hash 冲突时使用链表或树化结构。
- JDK 8 之后链表过长会树化，降低最坏查询复杂度。

### 5.2 LinkedHashMap
- 维护插入顺序或访问顺序。
- 适合实现 LRU 缓存。

### 5.3 TreeMap
- key 有序，适合区间查询。

### 5.4 ConcurrentHashMap
- 并发场景下优于 `Collections.synchronizedMap`。
- 读写性能与安全性平衡更好。

## 6. 泛型原理与实践
### 6.1 为什么需要泛型
- 编译期防止类型错误。
- 减少强制类型转换。
- 提升 API 可读性。

### 6.2 类型擦除
- Java 泛型在编译后会被擦除为原始类型。
- 运行期不能直接拿到泛型参数的具体类型（除反射某些场景）。

### 6.3 通配符与 PECS
- `<? extends T>`：生产者，适合读取。
- `<? super T>`：消费者，适合写入。
- 口诀：PECS。

## 7. Stream API 系统化使用
### 7.1 中间操作
- `filter`、`map`、`flatMap`、`distinct`、`sorted`、`peek`

### 7.2 终止操作
- `collect`、`forEach`、`count`、`reduce`、`findFirst`

### 7.3 Collectors 高频能力
- `toList`、`toSet`、`toMap`
- `groupingBy`
- `partitioningBy`
- `joining`
- `summarizingInt`

### 7.4 示例：课程成绩统计
- 计算平均分、最高分、及格率。
- 按班级分组统计通过人数。
- 输出 Top 10 学生。

## 8. 性能与可读性平衡
- Stream 不等于一定更快。
- 对复杂多步逻辑，过度链式调用会降低可读性。
- 性能敏感路径可用 for 循环。

## 9. 并发与集合安全
- `ArrayList` 在并发写场景不安全。
- 读多写少可考虑 `CopyOnWriteArrayList`。
- Map 并发写优先 `ConcurrentHashMap`。

## 10. 常见错误清单
- 在增强 for 循环中直接 `remove`。
- `toMap` key 冲突没处理 merge 函数。
- 误用 `parallelStream` 造成线程争用。
- 把含副作用逻辑塞到 `map/peek`。

## 11. 课程助教 Agent 中的集合实战
- 用 `Map<String, List<KnowledgeChunk>>` 管理按主题分组的知识片段。
- 用 `Set<String>` 做关键词去重。
- 用 `List<Message>` 管理会话窗口上下文。

## 12. 练习与实验建议
1. 实现课程列表分页后的多条件筛选与分组统计。
2. 设计并实现 LRU 缓存（可用 `LinkedHashMap`）。
3. 对比 for 循环与 Stream 在 10w 数据量下的性能。

## 13. 面试问答
- `HashMap` 扩容机制是什么？
- `HashMap` 与 `ConcurrentHashMap` 区别？
- `ArrayList` 和 `LinkedList` 如何选型？
- `parallelStream` 什么时候不该用？

## 14. 本章总结
集合与泛型是后端业务代码最常用的组合；Stream 是提升表达力的重要工具。工程中必须把“结构选择、复杂度、并发安全、可读性”放在一起权衡。
