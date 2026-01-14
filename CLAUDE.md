# CLAUDE.md

本文档为 Claude Code (claude.ai/code) 在此代码库中工作时提供指导。

## 项目概述

**project-java** - 基于 Spring Boot 2.7.12 + Java 17 的多模块企业级项目模板。

## Skills 索引

| Skill        | 触发词            | 触发条件                | 说明                        |
|--------------|----------------|---------------------|---------------------------|
| CRUD 开发技能    | 创建模块、CRUD、开发功能 | `/crud-development` | 提供标准化的增删改查代码生成能力          |
| gitignore 技能 | gitignore      | `/git-gitignore`    | 自动生成符合此代码库的 .gitignore 文件 |

## Agents 索引

| Agent      | 触发条件 | 说明                              |
|------------|------|---------------------------------|
| 编码规范 Agent | 自动触发 | 制定和维护项目编码规范，确保代码风格一致性、类型安全和最佳实践 |

## 技术栈

| 类别    | 技术                                   |
|-------|--------------------------------------|
| 核心框架  | Spring Boot 2.7.12, Maven 多模块        |
| ORM   | MyBatis-Flex 1.11.3                  |
| 搜索引擎  | Easy-ES 2.1.0 (Elasticsearch 7.17.8) |
| 认证授权  | Sa-Token 1.41.0                      |
| AI 框架 | Solon 3.6.2, Qdrant 向量存储             |
| 对象存储  | MinIO                                |
| 工具库   | Hutool 5.8.38, Lombok                |

## 项目结构

```
project-java/
├── project-framework/    # 核心框架模块
├── project-web/          # 主业务应用模块（入口）
├── project-es/           # Elasticsearch 模块
├── project-ai/           # AI/MCP 模块
├── project-test/         # 测试模块
└── scripts/              # Docker 部署脚本
```

## 架构模式

### DDD 分层结构

```
modules/{module}/
├── domain/          # 实体类 (继承 SuperEntity)
├── enums/           # 枚举类
├── request/         # BO 和 Query 类
├── service/         # 服务接口
│   └── impl/        # 服务实现
├── web/             # 控制器
└── response/        # VO (部分模块特有)
```

**注意**：`Mapper` 由 MyBatis-Flex 编译时自动生成，位于 `target/` 目录。

### 核心基类

| 类                   | 用途           |
|---------------------|--------------|
| `QueryController`   | CRUD 控制器：查询  |
| `SaveController`    | CRUD 控制器：保存  |
| `UpdateController`  | CRUD 控制器：更新  |
| `DeleteController`  | CRUD 控制器：删除  |
| `ApiResponse<T>`    | 统一 API 响应包装器 |
| `BusinessException` | 业务异常         |
| `SuperEntity`       | 基础实体         |

### REST API 端点规范

| 方法     | 端点             | 描述    |
|--------|----------------|-------|
| GET    | /_query/paging | 分页查询  |
| GET    | /_query        | 列表查询  |
| GET    | /{id}          | 按编号查询 |
| POST   | /              | 保存    |
| PUT    | /              | 更新    |
| DELETE | /{id}          | 按编号删除 |

### 权限控制

```java
@PreCheckPermission(value = {"{}:query", "{}:view"}, mode = PreMode.OR)
```

权限格式：`{entity}:{action}`，例如 `user:query`

## 配置说明

### 数据库连接

通过 `application-*.yml` 配置：

- Elasticsearch
- Qdrant
- MySQL
- Redis
- MinIO

### 包名约定

- `org.myframework.*` - 框架核心代码
- `org.huangyalong.*` - 业务逻辑

## 开发注意事项

1. **编码规范检查**: 所有代码修改必须经过编码规范 Agent 审核，确保代码风格一致性、类型安全和最佳实践
2. **自动建表**: 项目默认启用 Auto-Table，会根据实体类自动创建 MySQL 表
3. **响应格式**: 所有 API 响应使用 `ApiResponse<T>` 包装
4. **字典系统**: 使用 `@EnumDict` 注解实现枚举字典
5. **事件总线**: 基于 Redis 的 EventBus 用于分布式事件
6. **跳过测试**: 根 pom.xml 默认设置 `skipTests=true`
