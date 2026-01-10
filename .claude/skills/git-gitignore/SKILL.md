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

## 执行步骤

1. 检查当前目录是否存在 `.gitignore` 文件
2. 若存在，询问用户选择：覆盖或追加
3. 按模板顺序依次加载和处理模板
4. 合并所有模板内容
5. 追加项目特定规则
6. 写入 `.gitignore` 文件

## 模板列表

按以下顺序加载 8 个模板，每个模板必须生成对应的标题：

| 顺序 | 模板名称                              | 标题                | 特殊处理                 |
|:--:|-----------------------------------|-------------------|----------------------|
| 1  | Global/Windows.gitignore          | ### Windows ###   | 无                    |
| 2  | Global/macOS.gitignore            | ### MacOS ###     | `Icon[^M]` → `Icon*` |
| 3  | Global/JetBrains.gitignore        | ### JetBrains ### | 无                    |
| 4  | Global/VisualStudioCode.gitignore | ### VSCode ###    | 无                    |
| 5  | Java.gitignore                    | ### Java ###      | 无                    |
| 6  | Kotlin.gitignore                  | ### Kotlin ###    | 无                    |
| 7  | Gradle.gitignore                  | ### Gradle ###    | 无                    |
| 8  | Maven.gitignore                   | ### Maven ###     | 无                    |

## 模板获取规则

1. 优先使用：`https://cdn.jsdelivr.net/gh/github/gitignore@main/{模板名称}`
2. 备选使用：`https://raw.githubusercontent.com/github/gitignore/main/{模板名称}`
3. 若所有源均失败，使用内置的基础 Java/Maven 模板

## 标题生成规则

- 每个模板开头必须添加对应标题，格式为 `### 标题名称 ###`
- 使用上表定义的标题，**禁止使用模板原标题**
- 标题必须独占一行，前后各一个空行

## 模板处理规则

### macOS 模板特殊处理

将 `Icon[^M]` 替换为 `Icon*`，解决 IDEA 语法兼容性问题。

### 规则去重

1. 对所有模板的规则行进行**全局去重**
2. 规则行定义：非空且不以 `#` 开头的行
3. 保留每个规则**首次出现**的位置
4. **后续重复的规则及其关联注释直接跳过，不输出**
5. **不允许同一规则出现多次**

## 项目特定规则

在所有模板处理完成后，追加项目特定规则：

```gitignore
### Project ###
!libs/*.jar
```

此为唯一可额外添加的内容，同样参与去重判断。
