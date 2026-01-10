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

## 输出方式

- 将生成的 `.gitignore` 文件写入当前目录
- 如果文件已存在，询问用户是覆盖还是追加

## 模板顺序

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

若首选失败，自动降级至备选源。若所有源均失败，使用内置的基础 Java/Maven 模板。

## Global/macOS.gitignore 模板特殊处理

原模板中的 `Icon[^M]` 行存在兼容性问题：

- `[^M]` 是控制字符 `0x0D`（回车）的显示表示
- IDEA 会提示语法错误
- 将 `Icon[^M]` 替换为 `Icon*` 以支持通配符匹配

## 规则合并原则

- **标题自定义**：使用上表定义的标题，不使用模板原标题
- **去重处理**：对 gitignore 规则行去重，仅保留首个出现的版本
    - 所有行先进行标准化（去除首尾空白）
    - **只对规则行去重**：不以 `#` 开头且非空的行视为规则
    - 注释行和空行不参与去重判断，但会保留在首次出现的位置
    - 后续模板中重复的规则及其关联注释均跳过
- **全局去重**：所有模板中的重复规则统一去重，保留首个出现的版本

## 项目特定规则

在模板之后追加：

```gitignore
### Project ###
!libs/*.jar
```

此为唯一可额外添加的内容，同样遵循去重规则。
