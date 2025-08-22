-- noinspection SqlNoDataSourceInspectionForFile
-- noinspection SqlDialectInspectionForFile

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_file
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_file`
(
    `id`               bigint                             NOT NULL AUTO_INCREMENT COMMENT '主键',
    `url`              varchar(512)                       NOT NULL COMMENT '文件访问地址',
    `size`             bigint                             NULL COMMENT '文件大小，单位字节',
    `filename`         varchar(256)                       NULL COMMENT '文件名称',
    `orig_filename`    varchar(256)                       NULL COMMENT '原始文件名',
    `base_path`        varchar(256)                       NULL COMMENT '基础存储路径',
    `path`             varchar(256)                       NULL COMMENT '存储路径',
    `ext`              varchar(32)                        NULL COMMENT '文件扩展名',
    `content_type`     varchar(128)                       NULL COMMENT 'MIME类型',
    `platform`         varchar(32)                        NULL COMMENT '存储平台',
    `th_url`           varchar(512)                       NULL COMMENT '缩略图访问路径',
    `th_filename`      varchar(256)                       NULL COMMENT '缩略图名称',
    `th_size`          bigint                             NULL COMMENT '缩略图大小，单位字节',
    `th_content_type`  varchar(128)                       NULL COMMENT '缩略图MIME类型',
    `object_id`        varchar(32)                        NULL COMMENT '文件所属对象ID',
    `object_type`      varchar(32)                        NULL COMMENT '文件所属对象类型，例如用户头像，评价图片',
    `metadata`         text                               NULL COMMENT '文件元数据',
    `user_metadata`    text                               NULL COMMENT '文件用户元数据',
    `th_metadata`      text                               NULL COMMENT '缩略图元数据',
    `th_user_metadata` text                               NULL COMMENT '缩略图用户元数据',
    `attr`             text                               NULL COMMENT '附加属性',
    `file_acl`         varchar(32)                        NULL COMMENT '文件ACL',
    `th_file_acl`      varchar(32)                        NULL COMMENT '缩略图文件ACL',
    `hash_info`        text                               NULL COMMENT '哈希信息',
    `upload_id`        varchar(128)                       NULL COMMENT '上传ID，仅在手动分片上传时使用',
    `upload_status`    tinyint                            NULL COMMENT '上传状态，仅在手动分片上传时使用，1：初始化完成，2：上传完成',
    `status`           tinyint  DEFAULT 0                 NOT NULL COMMENT '文件状态(0-未使用, 1-已使用)',
    `create_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`          bigint   DEFAULT 0                 NULL COMMENT '更新版本',
    `is_deleted`       tinyint  DEFAULT 0                 NOT NULL COMMENT '是否删除(0-未删, 1-已删)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT '文件信息';

-- ----------------------------
-- Table structure for tb_file_part
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_file_part`
(
    `id`               bigint                             NOT NULL AUTO_INCREMENT COMMENT '主键',
    `platform`         varchar(32)                        NULL COMMENT '存储平台',
    `e_tag`            varchar(256)                       NULL COMMENT '分片ETag',
    `part_number`      tinyint                            NULL COMMENT '分片号。每一个上传的分片都有一个分片号，一般情况下取值范围是1~10000',
    `part_size`        bigint                             NULL COMMENT '文件大小，单位字节',
    `hash_info`        text                               NULL COMMENT '哈希信息',
    `upload_id`        varchar(128)                       NULL COMMENT '上传ID，仅在手动分片上传时使用',
    `create_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`          bigint   DEFAULT 0                 NULL COMMENT '更新版本',
    `is_deleted`       tinyint  DEFAULT 0                 NOT NULL COMMENT '是否删除(0-未删, 1-已删)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT '文件分片';
