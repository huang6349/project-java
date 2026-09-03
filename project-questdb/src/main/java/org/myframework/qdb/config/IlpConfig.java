package org.myframework.qdb.config;

/**
 * QuestDB ILP 连接串包装类型
 * <p>
 * 避免向容器注册裸 {@code String} bean 被其他按类型注入误拿
 */
public record IlpConfig(String value) {
}
