---
name: crud-development
description: |
  自动生成 CRUD 模块的代码文件。

  适用场景：
  - 创建新的业务模块
  - 编写 Controller、Service、Entity 等代码
  - 快速生成 DDD 分层架构代码

  触发词：创建模块、CRUD、开发功能、生成实体
---

# CRUD 开发技能

## 功能概述

自动生成 CRUD 模块的代码文件，基于项目 DDD 分层架构。

## 交互步骤

### 第一步：收集基础信息

**原则**：每次只问一个问题，按顺序逐一收集。

**🚫 严格禁止**：一次性列出所有问题让用户填写。

**✅ 正确做法**：用户回答一个问题后，再问下一个问题。

| 阶段 | 问题                          | 存储变量      |
|----|-----------------------------|-----------|
| Q1 | 请输入模块名（小驼峰格式，默认提示：example）： | `module`  |
| Q2 | 请输入模块中文名称（默认提示：示例）：         | `name`    |
| Q3 | 请输入包路径：                     | `package` |

**变量自动转换**：

- `{Module}` = 首字母大写（`module` → `Example`）
- `{MODULE}` = 全大写加下划线（`module` → `EXAMPLE`）

### 第二步：确认生成文件

**必问**：请选择需要生成的文件（使用 AskUserQuestion，可多选）：

- Entity（实体类）
- Queries（查询参数类）
- BO（业务对象类）
- Service（服务接口）
- ServiceImpl（服务实现）
- Controller（控制器）

### 第三步：生成代码文件

**模板来源**：`.claude/templates/code-patterns.md`

根据用户选择生成以下文件：

| 文件                         | 包路径                      | 条件             |
|----------------------------|--------------------------|----------------|
| `{Module}.java`            | `{package}.domain`       | 必选             |
| `{Module}Queries.java`     | `{package}.request`      | 必选             |
| `{Module}BO.java`          | `{package}.request`      | 必选             |
| `{Module}Service.java`     | `{package}.service`      | 选中 Service     |
| `{Module}ServiceImpl.java` | `{package}.service.impl` | 选中 ServiceImpl |
| `{Module}Controller.java`  | `{package}.web`          | 选中 Controller  |

**生成规则**：

- 文件写入到 `project-web` 模块
- 替换模板中所有变量占位符
- 保持代码格式和注释完整

### 第四步：展示生成报告

代码生成完成后，向用户展示以下格式的完成报告：

```text
✅ {Module} 模块生成完成

📁 文件位置（{package}/）
├── domain/
│   └── {Module}.java          实体类
├── request/
│   ├── {Module}Queries.java   查询参数
│   └── {Module}BO.java        业务对象
├── service/
│   ├── {Module}Service.java   服务接口
│   └── impl/
│       └── {Module}ServiceImpl.java 服务实现
└── web/
    └── {Module}Controller.java 控制器

⚙️  变量说明
   {package}  包路径
   {Module}   类名（Example）
   {module}   模块名（example）
   {MODULE}   表常量（EXAMPLE）
   {name}     中文名（示例）
```

## 变量替换速查表

详见：`.claude/templates/code-patterns.md` → 变量替换速查表
