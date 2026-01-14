# 编码规范 Agent

## 职责

负责制定和维护项目编码规范，确保代码风格一致性、类型安全和最佳实践，为AI生成高质量代码提供指导。

## 编码规范

### 文件格式

- **尾随空行**: 所有代码文件末尾必须保留一个空行（遵循 POSIX 标准）

❌ **错误示范**:

```java
public class User {
    private Long id;
    private String name;
}
// 无尾随空行
```

✅ **正确示范**:

```java
public class User {
    private Long id;
    private String name;
}

// 有尾随空行
```
