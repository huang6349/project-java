-- noinspection SqlNoDataSourceInspectionForFile
-- noinspection SqlDialectInspectionForFile

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_system
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_system`
(
    `id`               bigint                             NOT NULL AUTO_INCREMENT COMMENT '主键',
    `configs`          text                               NULL COMMENT '配置信息',
    `extras`           text                               NULL COMMENT '额外信息',
    `desc`             varchar(512)                       NULL COMMENT '备注',
    `create_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`          bigint   DEFAULT 0                 NULL COMMENT '更新版本',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT '系统信息';

-- ----------------------------
-- Table structure for tb_system_record
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_system_record`
(
    `id`               bigint                             NOT NULL AUTO_INCREMENT COMMENT '主键',
    `configs`          text                               NULL COMMENT '配置信息',
    `extras`           text                               NULL COMMENT '额外信息',
    `desc`             varchar(512)                       NULL COMMENT '备注',
    `create_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`          bigint   DEFAULT 0                 NULL COMMENT '更新版本',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT '系统记录';

-- ----------------------------
-- Table structure for tb_notify_category
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_notify_category`
(
    `id`               bigint                             NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`             varchar(256)                       NOT NULL COMMENT '类别名称',
    `code`             varchar(256)                       NOT NULL COMMENT '类别代码',
    `metadata`         text                               NULL COMMENT '类别模型',
    `configs`          text                               NULL COMMENT '配置信息',
    `extras`           text                               NULL COMMENT '额外信息',
    `desc`             varchar(512)                       NULL COMMENT '备注',
    `status`           tinyint  DEFAULT 0                 NOT NULL COMMENT '类别状态(0-启用, 1-禁用)',
    `create_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`          bigint   DEFAULT 0                 NULL COMMENT '更新版本',
    `is_deleted`       tinyint  DEFAULT 0                 NOT NULL COMMENT '是否删除(0-未删, 1-已删)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT '消息类别';

-- ----------------------------
-- Table structure for tb_ai_document
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_ai_document`
(
    `id`               bigint                             NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`             varchar(256)                       NOT NULL COMMENT '文档名称',
    `code`             varchar(256)                       NOT NULL COMMENT '文档代码',
    `configs`          text                               NULL COMMENT '配置信息',
    `extras`           text                               NULL COMMENT '额外信息',
    `desc`             varchar(512)                       NULL COMMENT '备注',
    `status`           tinyint  DEFAULT 0                 NOT NULL COMMENT '文档状态(0-启用, 1-禁用)',
    `create_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`          bigint   DEFAULT 0                 NULL COMMENT '更新版本',
    `is_deleted`       tinyint  DEFAULT 0                 NOT NULL COMMENT '是否删除(0-未删, 1-已删)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT '文档信息';

-- ----------------------------
-- Table structure for tb_ai_document_chunk
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_ai_document_chunk`
(
    `id`               bigint                             NOT NULL AUTO_INCREMENT COMMENT '主键',
    `document_id`      bigint                             NOT NULL COMMENT '文档主键(所属文档)',
    `content`          text                               NOT NULL COMMENT '分片内容',
    `sort`             tinyint  DEFAULT 0                 NOT NULL COMMENT '分片顺序',
    `configs`          text                               NULL COMMENT '配置信息',
    `extras`           text                               NULL COMMENT '额外信息',
    `create_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`          bigint   DEFAULT 0                 NULL COMMENT '更新版本',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT '分片信息';
