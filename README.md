# Code Agent CLI

一个基于 Java 21 的命令行 AI 助手应用，用于与本地 LLM 服务器进行交互对话。

## 特性

- 基于 OpenAI 兼容 API 的本地 LLM 通信
- 流式响应支持
- 会话上下文记忆
- 交互式命令行界面
- 使用 Java 21 虚拟线程提升并发性能

## 技术栈

| 技术 | 版本 |
|------|------|
| Java | 21 |
| Gradle | 7+ |
| Guice | 7.0.0 |
| FastJSON2 | 2.0.60 |
| JLine | 3.30.0 |
| Lombok | 1.18.42 |

## 项目结构

```
code-agent-cli/
├── src/main/java/space/lambdadriver/
│   ├── App.java                    # 主入口
│   ├── AppModule.java              # 依赖注入配置
│   ├── model/
│   │   └── ChatMessage.java        # 聊天消息数据模型
│   └── util/
│       ├── LlmClient.java          # LLM API 客户端
│       └── HttpClientUtils.java    # HTTP 工具类
├── build.gradle                    # Gradle 构建配置
└── settings.gradle                  # 项目设置
```

## 前置要求

- Java 21 或更高版本
- Gradle 7+（或使用项目自带的 gradlew）
- 本地 LLM 服务器运行在 `http://127.0.0.1:9981`

## 构建

使用 Gradle Wrapper 构建：

```bash
./gradlew clean shadowJar
```

或在 Windows 上：

```bash
gradlew.bat clean shadowJar
```

构建完成后，可执行 JAR 文件位于 `build/libs/` 目录。

## 运行

```bash
java -jar build/libs/code-agent-cli-1.0-SNAPSHOT.jar
```

## 配置

默认 LLM 服务器地址：`http://127.0.0.1:9981/v1/chat/completions`

如需修改，请编辑 `src/main/java/space/lambdadriver/util/LlmClient.java` 中的 API URL。

## 许可证

MIT License
