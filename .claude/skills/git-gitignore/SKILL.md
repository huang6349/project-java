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

1. **严格模板化**：仅使用官方模板内容，不添加模板中未包含的规则
2. **标题自定义**：所有模板内容使用用户定义的标题，不使用模板原标题
3. **去重处理**：合并重复规则，保留先出现的规则
4. **跨平台兼容**：确保 Windows/macOS/Linux 均可使用

## 模板定义

按以下顺序加载模板，并为每个模板设置指定标题：

| 顺序 | 模板名称                              | 用户定义标题                   | 说明     |
|:--:|-----------------------------------|--------------------------|--------|
| 1  | Global/Windows.gitignore          | ### Windows ###          | 系统级忽略  |
| 2  | Global/macOS.gitignore            | ### MacOS ###            | 系统级忽略  |
| 3  | Global/JetBrains.gitignore        | ### JetBrains ###        | IDE 忽略 |
| 4  | Global/VisualStudioCode.gitignore | ### VisualStudioCode ### | IDE 忽略 |
| 5  | Java.gitignore                    | ### Java ###             | 语言模板   |
| 6  | Kotlin.gitignore                  | ### Kotlin ###           | 语言模板   |
| 7  | Gradle.gitignore                  | ### Gradle ###           | 构建工具   |
| 8  | Maven.gitignore                   | ### Maven ###            | 构建工具   |

## 模板获取

通过 URL 获取官方模板原始内容：

```
https://xget.xi-xu.me/gh/github/gitignore/raw/refs/heads/main/{模板名称}
```

**执行规则**：

1. 按顺序加载模板 1-8
2. 将模板原始内容包裹在对应的用户定义标题中
3. 去重：遍历所有规则，重复规则仅保留第一个

## 项目特定规则

在模板之后追加以下自定义规则：

```gitignore
### Project ###
!libs/*.jar
```

**注意**：项目特定规则是唯一可额外添加的内容，务必保持简洁。
