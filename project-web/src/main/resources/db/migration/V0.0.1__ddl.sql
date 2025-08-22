-- noinspection SqlNoDataSourceInspectionForFile
-- noinspection SqlDialectInspectionForFile

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_tenant
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_tenant`
(
    `id`               bigint                             NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`             varchar(256)                       NOT NULL COMMENT '租户名称',
    `code`             varchar(256)                       NOT NULL COMMENT '租户代码',
    `category`         tinyint                            NULL COMMENT '租户类别（枚举字典）',
    `address`          varchar(256)                       NULL COMMENT '租户地址',
    `configs`          text                               NULL COMMENT '配置信息',
    `extras`           text                               NULL COMMENT '额外信息',
    `desc`             varchar(512)                       NULL COMMENT '备注',
    `status`           tinyint  DEFAULT 0                 NOT NULL COMMENT '租户状态(0-启用, 1-禁用)',
    `create_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`          bigint   DEFAULT 0                 NULL COMMENT '更新版本',
    `is_deleted`       tinyint  DEFAULT 0                 NOT NULL COMMENT '是否删除(0-未删, 1-已删)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT '租户信息';

-- ----------------------------
-- Table structure for tb_tenant_assoc
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_tenant_assoc`
(
    `id`               bigint                             NOT NULL AUTO_INCREMENT COMMENT '主键',
    `tenant_id`        bigint                             NOT NULL COMMENT '租户主键(所属租户)',
    `assoc`            varchar(256)                       NOT NULL COMMENT '关联表名',
    `assoc_id`         bigint                             NOT NULL COMMENT '关联主键',
    `effective`        tinyint  DEFAULT 0                 NOT NULL COMMENT '限制时间(0-不限制, 1-限制)',
    `effective_time`   datetime                           NULL COMMENT '有效时间',
    `category`         tinyint  DEFAULT 0                 NULL COMMENT '关联类别(枚举字典)',
    `desc`             varchar(512)                       NULL COMMENT '备注',
    `create_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`          bigint   DEFAULT 0                 NULL COMMENT '更新版本',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT '租户关联';

-- ----------------------------
-- Table structure for tb_tenant_record
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_tenant_record`
(
    `id`               bigint                             NOT NULL AUTO_INCREMENT COMMENT '主键',
    `tenant_id`        bigint                             NOT NULL COMMENT '租户主键(所属租户)',
    `configs`          text                               NULL COMMENT '配置信息',
    `extras`           text                               NULL COMMENT '额外信息',
    `desc`             varchar(512)                       NULL COMMENT '备注',
    `create_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`          bigint   DEFAULT 0                 NULL COMMENT '更新版本',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT '租户记录';

-- ----------------------------
-- Table structure for tb_perm
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_perm`
(
    `id`               bigint                             NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`             varchar(256)                       NOT NULL COMMENT '权限名称',
    `code`             varchar(256)                       NOT NULL COMMENT '权限代码',
    `configs`          text                               NULL COMMENT '配置信息',
    `extras`           text                               NULL COMMENT '额外信息',
    `desc`             varchar(512)                       NULL COMMENT '备注',
    `status`           tinyint  DEFAULT 0                 NOT NULL COMMENT '权限状态(0-启用, 1-禁用)',
    `create_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`          bigint   DEFAULT 0                 NULL COMMENT '更新版本',
    `is_deleted`       tinyint  DEFAULT 0                 NOT NULL COMMENT '是否删除(0-未删, 1-已删)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT '权限信息';

-- ----------------------------
-- Records of tb_perm
-- ----------------------------
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000101, '租户管理', '@tenant:*');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000102, '租户查询', '@tenant:query');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000103, '租户新增', '@tenant:add');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000104, '租户修改', '@tenant:update');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000105, '租户删除', '@tenant:delete');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000201, '权限管理', '@perm:*');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000202, '权限查询', '@perm:query');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000203, '权限新增', '@perm:add');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000204, '权限修改', '@perm:update');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000205, '权限删除', '@perm:delete');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000301, '部门管理', '@dept:*');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000302, '部门查询', '@dept:query');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000303, '部门新增', '@dept:add');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000304, '部门修改', '@dept:update');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000305, '部门删除', '@dept:delete');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000401, '角色管理', '@role:*');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000402, '角色查询', '@role:query');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000403, '角色新增', '@role:add');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000404, '角色修改', '@role:update');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000405, '角色删除', '@role:delete');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000406, '角色授权', '@role:auth');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000501, '用户管理', '@user:*');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000502, '用户查询', '@user:query');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000503, '用户新增', '@user:add');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000504, '用户修改', '@user:update');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000505, '用户删除', '@user:delete');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000506, '用户授权', '@user:auth');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000601, '文件管理', '@file:*');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000602, '文件查询', '@file:query');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000701, '消息管理', '@notify:*');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000702, '消息查询', '@notify:query');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000703, '消息新增', '@notify:add');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000704, '消息修改', '@notify:update');
INSERT INTO `tb_perm` (`id`, `name`, `code`) VALUES (10000000000000705, '消息删除', '@notify:delete');

-- ----------------------------
-- Table structure for tb_perm_assoc
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_perm_assoc`
(
    `id`               bigint                             NOT NULL AUTO_INCREMENT COMMENT '主键',
    `perm_id`          bigint                             NOT NULL COMMENT '权限主键(所属权限)',
    `assoc`            varchar(256)                       NOT NULL COMMENT '关联表名',
    `assoc_id`         bigint                             NOT NULL COMMENT '关联主键',
    `effective`        tinyint  DEFAULT 0                 NOT NULL COMMENT '限制时间(0-不限制, 1-限制)',
    `effective_time`   datetime                           NULL COMMENT '有效时间',
    `category`         tinyint  DEFAULT 0                 NULL COMMENT '关联类别(枚举字典)',
    `desc`             varchar(512)                       NULL COMMENT '备注',
    `tenant_id`        bigint                             NULL COMMENT '租户主键(所属租户)',
    `create_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`          bigint   DEFAULT 0                 NULL COMMENT '更新版本',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT '权限关联';

-- ----------------------------
-- Records of tb_perm_assoc
-- ----------------------------
INSERT INTO `tb_perm_assoc` (`id`, `perm_id`, `assoc`, `assoc_id`) VALUES (10000000000000101, 10000000000000101, 'tb_role', 10000000000000000);
INSERT INTO `tb_perm_assoc` (`id`, `perm_id`, `assoc`, `assoc_id`) VALUES (10000000000000201, 10000000000000201, 'tb_role', 10000000000000000);
INSERT INTO `tb_perm_assoc` (`id`, `perm_id`, `assoc`, `assoc_id`) VALUES (10000000000000301, 10000000000000301, 'tb_role', 10000000000000000);
INSERT INTO `tb_perm_assoc` (`id`, `perm_id`, `assoc`, `assoc_id`) VALUES (10000000000000401, 10000000000000401, 'tb_role', 10000000000000000);
INSERT INTO `tb_perm_assoc` (`id`, `perm_id`, `assoc`, `assoc_id`) VALUES (10000000000000501, 10000000000000501, 'tb_role', 10000000000000000);
INSERT INTO `tb_perm_assoc` (`id`, `perm_id`, `assoc`, `assoc_id`) VALUES (10000000000000601, 10000000000000601, 'tb_role', 10000000000000000);
INSERT INTO `tb_perm_assoc` (`id`, `perm_id`, `assoc`, `assoc_id`) VALUES (10000000000000701, 10000000000000701, 'tb_role', 10000000000000000);

-- ----------------------------
-- Table structure for tb_dept
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_dept`
(
    `id`               bigint                             NOT NULL AUTO_INCREMENT COMMENT '主键',
    `parent_id`        bigint   DEFAULT 0                 NOT NULL COMMENT '父级节点(父级主键)',
    `path`             varchar(512)                       NOT NULL COMMENT '节点路径',
    `sort`             tinyint  DEFAULT 0                 NOT NULL COMMENT '节点顺序',
    `name`             varchar(256)                       NOT NULL COMMENT '部门名称',
    `code`             varchar(256)                       NOT NULL COMMENT '部门代码',
    `configs`          text                               NULL COMMENT '配置信息',
    `extras`           text                               NULL COMMENT '额外信息',
    `desc`             varchar(512)                       NULL COMMENT '备注',
    `status`           tinyint  DEFAULT 0                 NOT NULL COMMENT '部门状态(0-启用, 1-禁用)',
    `tenant_id`        bigint                             NOT NULL COMMENT '租户主键(所属租户)',
    `create_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`          bigint   DEFAULT 0                 NULL COMMENT '更新版本',
    `is_deleted`       tinyint  DEFAULT 0                 NOT NULL COMMENT '是否删除(0-未删, 1-已删)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT '部门信息';

-- ----------------------------
-- Table structure for tb_dept_assoc
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_dept_assoc`
(
    `id`               bigint                             NOT NULL AUTO_INCREMENT COMMENT '主键',
    `dept_id`          bigint                             NOT NULL COMMENT '部门主键(所属部门)',
    `assoc`            varchar(256)                       NOT NULL COMMENT '关联表名',
    `assoc_id`         bigint                             NOT NULL COMMENT '关联主键',
    `effective`        tinyint  DEFAULT 0                 NOT NULL COMMENT '限制时间(0-不限制, 1-限制)',
    `effective_time`   datetime                           NULL COMMENT '有效时间',
    `category`         tinyint  DEFAULT 0                 NULL COMMENT '关联类别(枚举字典)',
    `desc`             varchar(512)                       NULL COMMENT '备注',
    `tenant_id`        bigint                             NULL COMMENT '租户主键(所属租户)',
    `create_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`          bigint   DEFAULT 0                 NULL COMMENT '更新版本',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT '部门关联';

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_role`
(
    `id`               bigint                             NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`             varchar(256)                       NOT NULL COMMENT '角色名称',
    `code`             varchar(256)                       NOT NULL COMMENT '角色代码',
    `configs`          text                               NULL COMMENT '配置信息',
    `extras`           text                               NULL COMMENT '额外信息',
    `desc`             varchar(512)                       NULL COMMENT '备注',
    `status`           tinyint  DEFAULT 0                 NOT NULL COMMENT '角色状态(0-启用, 1-禁用)',
    `create_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`          bigint   DEFAULT 0                 NULL COMMENT '更新版本',
    `is_deleted`       tinyint  DEFAULT 0                 NOT NULL COMMENT '是否删除(0-未删, 1-已删)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT '角色信息';

-- ----------------------------
-- Records of tb_role
-- ----------------------------
INSERT INTO `tb_role` (`id`, `name`, `code`) VALUES (10000000000000000, '管理员', 'admin');
INSERT INTO `tb_role` (`id`, `name`, `code`) VALUES (10000000000000001, '普通用户', 'user');

-- ----------------------------
-- Table structure for tb_role_assoc
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_role_assoc`
(
    `id`               bigint                             NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_id`          bigint                             NOT NULL COMMENT '角色主键(所属角色)',
    `assoc`            varchar(256)                       NOT NULL COMMENT '关联表名',
    `assoc_id`         bigint                             NOT NULL COMMENT '关联主键',
    `effective`        tinyint  DEFAULT 0                 NOT NULL COMMENT '限制时间(0-不限制, 1-限制)',
    `effective_time`   datetime                           NULL COMMENT '有效时间',
    `category`         tinyint  DEFAULT 0                 NULL COMMENT '关联类别(枚举字典)',
    `desc`             varchar(512)                       NULL COMMENT '备注',
    `tenant_id`        bigint                             NULL COMMENT '租户主键(所属租户)',
    `create_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`          bigint   DEFAULT 0                 NULL COMMENT '更新版本',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT '角色关联';

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_user`
(
    `id`               bigint                             NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`         varchar(256)                       NOT NULL COMMENT '用户帐号',
    `password`         varchar(256)                       NULL COMMENT '用户密码',
    `salt`             varchar(256)                       NULL COMMENT '盐',
    `mobile`           varchar(256)                       NULL COMMENT '手机号码',
    `email`            varchar(256)                       NULL COMMENT '用户邮箱',
    `configs`          text                               NULL COMMENT '配置信息',
    `extras`           text                               NULL COMMENT '额外信息',
    `desc`             varchar(512)                       NULL COMMENT '备注',
    `login_time`       datetime                           NULL COMMENT '登录时间',
    `status`           tinyint  DEFAULT 0                 NOT NULL COMMENT '用户状态(0-启用, 1-禁用)',
    `tenant_id`        bigint                             NULL COMMENT '租户主键(默认租户)',
    `create_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`          bigint   DEFAULT 0                 NULL COMMENT '更新版本',
    `is_deleted`       tinyint  DEFAULT 0                 NOT NULL COMMENT '是否删除(0-未删, 1-已删)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT '用户信息';

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` (`id`, `username`, `password`, `salt`) VALUES (10000000000000000, 'admin', '$2a$10$QyjZ4c6BaPD3693XArMopey0RPoKkiIfqHAxaxapijuYbK9takS.a', '$2a$10$QyjZ4c6BaPD3693XArMope');

-- ----------------------------
-- Table structure for tb_user_assoc
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_user_assoc`
(
    `id`               bigint                             NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`          bigint                             NOT NULL COMMENT '用户主键(所属用户)',
    `assoc`            varchar(256)                       NOT NULL COMMENT '关联表名',
    `assoc_id`         bigint                             NOT NULL COMMENT '关联主键',
    `effective`        tinyint  DEFAULT 0                 NOT NULL COMMENT '限制时间(0-不限制, 1-限制)',
    `effective_time`   datetime                           NULL COMMENT '有效时间',
    `category`         tinyint  DEFAULT 0                 NULL COMMENT '关联类别(枚举字典)',
    `desc`             varchar(512)                       NULL COMMENT '备注',
    `create_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`          bigint   DEFAULT 0                 NULL COMMENT '更新版本',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT '用户关联';

-- ----------------------------
-- Table structure for tb_user_record
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_user_record`
(
    `id`               bigint                             NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`          bigint                             NOT NULL COMMENT '用户主键(所属用户)',
    `configs`          text                               NULL COMMENT '配置信息',
    `extras`           text                               NULL COMMENT '额外信息',
    `desc`             varchar(512)                       NULL COMMENT '备注',
    `create_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time`      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`          bigint   DEFAULT 0                 NULL COMMENT '更新版本',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT '用户记录';
