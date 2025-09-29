SET NAMES utf8mb4;

-- ----------------------------
-- Records of tb_perm
-- ----------------------------
-- 系统管理
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000001, '系统管理', '@system:*', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000002, '系统查询', '@system:query', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000004, '系统修改', '@system:update', now(), now());
-- 租户管理
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000101, '租户管理', '@tenant:*', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000102, '租户查询', '@tenant:query', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000103, '租户新增', '@tenant:add', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000104, '租户修改', '@tenant:update', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000105, '租户删除', '@tenant:delete', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000106, '租户授权', '@tenant:auth', now(), now());
-- 权限管理
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000201, '权限管理', '@perm:*', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000202, '权限查询', '@perm:query', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000203, '权限新增', '@perm:add', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000204, '权限修改', '@perm:update', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000205, '权限删除', '@perm:delete', now(), now());
-- 部门管理
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000301, '部门管理', '@dept:*', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000302, '部门查询', '@dept:query', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000303, '部门新增', '@dept:add', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000304, '部门修改', '@dept:update', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000305, '部门删除', '@dept:delete', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000306, '部门授权', '@dept:auth', now(), now());
-- 角色管理
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000401, '角色管理', '@role:*', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000402, '角色查询', '@role:query', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000403, '角色新增', '@role:add', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000404, '角色修改', '@role:update', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000405, '角色删除', '@role:delete', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000406, '角色授权', '@role:auth', now(), now());
-- 用户管理
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000501, '用户管理', '@user:*', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000502, '用户查询', '@user:query', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000503, '用户新增', '@user:add', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000504, '用户修改', '@user:update', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000505, '用户删除', '@user:delete', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000506, '用户授权', '@user:auth', now(), now());
-- 文件管理
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000601, '文件管理', '@file:*', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000602, '文件查询', '@file:query', now(), now());
-- 消息管理
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000701, '消息管理', '@notify:*', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000702, '消息查询', '@notify:query', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000703, '消息新增', '@notify:add', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000704, '消息修改', '@notify:update', now(), now());
INSERT IGNORE INTO `tb_perm` (`id`, `name`, `code`, `create_time`, `update_time`) VALUES (10000000000000705, '消息删除', '@notify:delete', now(), now());
