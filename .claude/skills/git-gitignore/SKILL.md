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

## 模板加载顺序

按以下顺序加载模板：

| 顺序 | 模板名称                              | 标题                |
|:--:|-----------------------------------|-------------------|
| 1  | Global/Windows.gitignore          | ### Windows ###   |
| 2  | Global/macOS.gitignore            | ### MacOS ###     |
| 3  | Global/JetBrains.gitignore        | ### JetBrains ### |
| 4  | Global/VisualStudioCode.gitignore | ### VSCode ###    |
| 5  | Java.gitignore                    | ### Java ###      |
| 6  | Kotlin.gitignore                  | ### Kotlin ###    |
| 7  | Gradle.gitignore                  | ### Gradle ###    |
| 8  | Maven.gitignore                   | ### Maven ###     |

## 模板获取

按优先级获取模板内容：

1. `https://cdn.jsdelivr.net/gh/github/gitignore@main/{模板名称}`
2. `https://raw.githubusercontent.com/github/gitignore/main/{模板名称}`

若首选失败，自动降级至备选源。

## 规则合并原则

- **标题自定义**：使用上表定义的标题，不使用模板原标题
- **去重处理**：相同规则仅保留首个出现的版本
    - 比较时忽略首尾空白
    - 注释行和空行不参与去重
- **顺序执行**：后续模板不会覆盖已存在的规则

## 项目特定规则

在模板之后追加：

```gitignore
### Project ###
!libs/*.jar
```

此为唯一可额外添加的内容，同样遵循去重规则。
