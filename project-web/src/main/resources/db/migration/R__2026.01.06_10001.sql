SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Records of tb_role
-- ----------------------------
REPLACE INTO `tb_role` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000000, '管理员', 'admin', NOW(), NOW());
REPLACE INTO `tb_role` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000001, '普通用户', 'user', NOW(), NOW());

-- ----------------------------
-- Records of tb_perm
-- ----------------------------
-- 系统管理
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000001, '系统管理', '@system:*', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000002, '系统查询', '@system:query', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000004, '系统修改', '@system:update', NOW(), NOW());
-- 租户管理
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000101, '租户管理', '@tenant:*', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000102, '租户查询', '@tenant:query', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000103, '租户新增', '@tenant:add', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000104, '租户修改', '@tenant:update', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000105, '租户删除', '@tenant:delete', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000106, '租户授权', '@tenant:auth', NOW(), NOW());
-- 权限管理
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000201, '权限管理', '@perm:*', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000202, '权限查询', '@perm:query', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000203, '权限新增', '@perm:add', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000204, '权限修改', '@perm:update', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000205, '权限删除', '@perm:delete', NOW(), NOW());
-- 部门管理
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000301, '部门管理', '@dept:*', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000302, '部门查询', '@dept:query', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000303, '部门新增', '@dept:add', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000304, '部门修改', '@dept:update', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000305, '部门删除', '@dept:delete', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000306, '部门授权', '@dept:auth', NOW(), NOW());
-- 角色管理
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000401, '角色管理', '@role:*', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000402, '角色查询', '@role:query', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000403, '角色新增', '@role:add', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000404, '角色修改', '@role:update', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000405, '角色删除', '@role:delete', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000406, '角色授权', '@role:auth', NOW(), NOW());
-- 用户管理
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000501, '用户管理', '@user:*', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000502, '用户查询', '@user:query', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000503, '用户新增', '@user:add', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000504, '用户修改', '@user:update', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000505, '用户删除', '@user:delete', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000506, '用户授权', '@user:auth', NOW(), NOW());
-- 文件管理
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000601, '文件管理', '@file:*', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000602, '文件查询', '@file:query', NOW(), NOW());
-- 消息管理
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000701, '消息管理', '@notify:*', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000702, '消息查询', '@notify:query', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000703, '消息新增', '@notify:add', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000704, '消息修改', '@notify:update', NOW(), NOW());
REPLACE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000705, '消息删除', '@notify:delete', NOW(), NOW());

-- ----------------------------
-- Records of tb_perm_assoc
-- ----------------------------
-- 管理员
REPLACE INTO `tb_perm_assoc` (`id`, `perm_id`, `assoc`, `assoc_id`, `tenant_id`, `create_time`, `update_time`) VALUES (10000000000000001, 10000000000000001, 'tb_role', 10000000000000000, 0, NOW(), NOW());
REPLACE INTO `tb_perm_assoc` (`id`, `perm_id`, `assoc`, `assoc_id`, `tenant_id`, `create_time`, `update_time`) VALUES (10000000000000101, 10000000000000101, 'tb_role', 10000000000000000, 0, NOW(), NOW());
REPLACE INTO `tb_perm_assoc` (`id`, `perm_id`, `assoc`, `assoc_id`, `tenant_id`, `create_time`, `update_time`) VALUES (10000000000000201, 10000000000000201, 'tb_role', 10000000000000000, 0, NOW(), NOW());
REPLACE INTO `tb_perm_assoc` (`id`, `perm_id`, `assoc`, `assoc_id`, `tenant_id`, `create_time`, `update_time`) VALUES (10000000000000301, 10000000000000301, 'tb_role', 10000000000000000, 0, NOW(), NOW());
REPLACE INTO `tb_perm_assoc` (`id`, `perm_id`, `assoc`, `assoc_id`, `tenant_id`, `create_time`, `update_time`) VALUES (10000000000000401, 10000000000000401, 'tb_role', 10000000000000000, 0, NOW(), NOW());
REPLACE INTO `tb_perm_assoc` (`id`, `perm_id`, `assoc`, `assoc_id`, `tenant_id`, `create_time`, `update_time`) VALUES (10000000000000501, 10000000000000501, 'tb_role', 10000000000000000, 0, NOW(), NOW());
REPLACE INTO `tb_perm_assoc` (`id`, `perm_id`, `assoc`, `assoc_id`, `tenant_id`, `create_time`, `update_time`) VALUES (10000000000000601, 10000000000000601, 'tb_role', 10000000000000000, 0, NOW(), NOW());
REPLACE INTO `tb_perm_assoc` (`id`, `perm_id`, `assoc`, `assoc_id`, `tenant_id`, `create_time`, `update_time`) VALUES (10000000000000701, 10000000000000701, 'tb_role', 10000000000000000, 0, NOW(), NOW());

-- ----------------------------
-- Records of tb_user
-- ----------------------------
REPLACE INTO `tb_user` (`id`, `username`, `password`, `salt`, `create_time`, `update_time`) VALUES (10000000000000000, 'admin', '$2a$10$QyjZ4c6BaPD3693XArMopey0RPoKkiIfqHAxaxapijuYbK9takS.a', '$2a$10$QyjZ4c6BaPD3693XArMope', NOW(), NOW());

SET FOREIGN_KEY_CHECKS = 1;
