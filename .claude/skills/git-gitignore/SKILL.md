---
name: git-gitignore
description: |
  自动生成符合此代码库的 .gitignore 文件。

  适用场景：
  - 初始化新项目时创建 .gitignore
  - 为现有项目补充或更新忽略规则

  触发词：gitignore
---

# .gitignore 生成规范

## 核心原则

1. **固定组合**：按固定顺序加载全部官方模板
2. **平台无关**：确保跨平台兼容（Windows/macOS/Linux）
3. **IDE 友好**：包含常见 IDE 的忽略规则
4. **语言栈覆盖**：覆盖 Java/Kotlin 生态常用技术栈

## 模板加载顺序

按以下顺序加载官方模板（从 github/gitignore）：

| 顺序 | 模板                      | 说明     |
|:--:|-------------------------|--------|
| 1  | Global/Windows          | 系统级忽略  |
| 2  | Global/macOS            | 系统级忽略  |
| 3  | Global/JetBrains        | IDE 忽略 |
| 4  | Global/VisualStudioCode | IDE 忽略 |
| 5  | Java                    | 语言模板   |
| 6  | Kotlin                  | 语言模板   |
| 7  | Gradle                  | 构建工具模板 |
| 8  | Maven                   | 构建工具模板 |

## URL 模板

```
https://xget.xi-xu.me/gh/github/gitignore/raw/refs/heads/main/{Path}.gitignore
```

## 项目特定规则

在模板之后追加以下自定义规则：

```gitignore
### Project ###
!libs/*.jar
```

## 执行步骤

1. 按顺序加载 1-8 号模板并设置标题：
    - ### Windows ###
    - ### MacOS ###
    - ### JetBrains ###
    - ### VisualStudioCode ###
    - ### Java ###
    - ### Kotlin ###
    - ### Gradle ###
    - ### Maven ###
2. 追加自定义规则
3. 合并重复规则：
    - 遍历生成的规则列表，若规则已存在则跳过
    - 保留先加入的规则，避免重复
