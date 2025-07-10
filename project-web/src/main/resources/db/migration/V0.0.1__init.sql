-- noinspection SqlNoDataSourceInspectionForFile
-- noinspection SqlDialectInspectionForFile

CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW."update_time" = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- ----------------------------
-- Table structure for tb_tenant
-- ----------------------------
CREATE TABLE IF NOT EXISTS "tb_tenant"
(
    "id"               int8                                NOT NULL,
    "name"             varchar(256)                        NOT NULL,
    "code"             varchar(256)                        NOT NULL,
    "category"         int2                                NULL,
    "address"          varchar(256)                        NULL,
    "configs"          text                                NULL,
    "extras"           text                                NULL,
    "desc"             varchar(512)                        NULL,
    "status"           int2      DEFAULT 0                 NOT NULL,
    "create_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "update_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "version"          int8      DEFAULT 0                 NOT NULL,
    "is_deleted"       int2      DEFAULT 0                 NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON COLUMN "tb_tenant"."id"                         IS '主键';
COMMENT ON COLUMN "tb_tenant"."name"                       IS '租户名称';
COMMENT ON COLUMN "tb_tenant"."code"                       IS '租户代码';
COMMENT ON COLUMN "tb_tenant"."category"                   IS '租户类别（枚举字典）';
COMMENT ON COLUMN "tb_tenant"."address"                    IS '租户地址';
COMMENT ON COLUMN "tb_tenant"."configs"                    IS '配置信息';
COMMENT ON COLUMN "tb_tenant"."extras"                     IS '额外信息';
COMMENT ON COLUMN "tb_tenant"."desc"                       IS '备注';
COMMENT ON COLUMN "tb_tenant"."status"                     IS '租户状态(0-启用, 1-禁用)';
COMMENT ON COLUMN "tb_tenant"."create_time"                IS '创建时间';
COMMENT ON COLUMN "tb_tenant"."update_time"                IS '更新时间';
COMMENT ON COLUMN "tb_tenant"."version"                    IS '更新版本';
COMMENT ON COLUMN "tb_tenant"."is_deleted"                 IS '是否删除(0-未删, 1-已删)';

COMMENT ON TABLE "tb_tenant"                               IS '租户信息';

-- ----------------------------
-- Trigger of tb_tenant
-- ----------------------------
CREATE TRIGGER "tg_tenant"
    BEFORE UPDATE ON "tb_tenant"
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- ----------------------------
-- Table structure for tb_tenant_assoc
-- ----------------------------
CREATE TABLE IF NOT EXISTS "tb_tenant_assoc"
(
    "id"               int8                                NOT NULL,
    "tenant_id"        int8                                NOT NULL,
    "assoc"            varchar(256)                        NOT NULL,
    "assoc_id"         int8                                NOT NULL,
    "effective"        int2      DEFAULT 0                 NOT NULL,
    "effective_time"   timestamp                           NULL,
    "category"         int2      DEFAULT 0                 NULL,
    "desc"             varchar(512)                        NULL,
    "create_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "update_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "version"          int8      DEFAULT 0                 NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON COLUMN "tb_tenant_assoc"."id"                   IS '主键';
COMMENT ON COLUMN "tb_tenant_assoc"."tenant_id"            IS '租户主键(所属租户)';
COMMENT ON COLUMN "tb_tenant_assoc"."assoc"                IS '关联表名';
COMMENT ON COLUMN "tb_tenant_assoc"."assoc_id"             IS '关联主键';
COMMENT ON COLUMN "tb_tenant_assoc"."effective"            IS '限制时间(0-不限制, 1-限制)';
COMMENT ON COLUMN "tb_tenant_assoc"."effective_time"       IS '有效时间';
COMMENT ON COLUMN "tb_tenant_assoc"."category"             IS '关联类别(枚举字典)';
COMMENT ON COLUMN "tb_tenant_assoc"."desc"                 IS '备注';
COMMENT ON COLUMN "tb_tenant_assoc"."create_time"          IS '创建时间';
COMMENT ON COLUMN "tb_tenant_assoc"."update_time"          IS '更新时间';
COMMENT ON COLUMN "tb_tenant_assoc"."version"              IS '更新版本';

COMMENT ON TABLE "tb_tenant_assoc"                         IS '租户关联';

-- ----------------------------
-- Trigger of tb_tenant_assoc
-- ----------------------------
CREATE TRIGGER "tg_tenant_assoc"
    BEFORE UPDATE ON "tb_tenant_assoc"
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- ----------------------------
-- Table structure for tb_tenant_record
-- ----------------------------
CREATE TABLE IF NOT EXISTS "tb_tenant_record"
(
    "id"               int8                                NOT NULL,
    "tenant_id"        int8                                NOT NULL,
    "configs"          text                                NULL,
    "extras"           text                                NULL,
    "desc"             varchar(512)                        NULL,
    "create_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "update_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "version"          int8      DEFAULT 0                 NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON COLUMN "tb_tenant_record"."id"                  IS '主键';
COMMENT ON COLUMN "tb_tenant_record"."tenant_id"           IS '租户主键(所属租户)';
COMMENT ON COLUMN "tb_tenant_record"."configs"             IS '配置信息';
COMMENT ON COLUMN "tb_tenant_record"."extras"              IS '额外信息';
COMMENT ON COLUMN "tb_tenant_record"."desc"                IS '备注';
COMMENT ON COLUMN "tb_tenant_record"."create_time"         IS '创建时间';
COMMENT ON COLUMN "tb_tenant_record"."update_time"         IS '更新时间';
COMMENT ON COLUMN "tb_tenant_record"."version"             IS '更新版本';

COMMENT ON TABLE "tb_tenant_record"                        IS '租户记录';

-- ----------------------------
-- Trigger of tb_tenant_record
-- ----------------------------
CREATE TRIGGER "tg_tenant_record"
    BEFORE UPDATE ON "tb_tenant_record"
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- ----------------------------
-- Table structure for tb_perm
-- ----------------------------
CREATE TABLE IF NOT EXISTS "tb_perm"
(
    "id"               int8                                NOT NULL,
    "name"             varchar(256)                        NOT NULL,
    "code"             varchar(256)                        NOT NULL,
    "configs"          text                                NULL,
    "extras"           text                                NULL,
    "desc"             varchar(512)                        NULL,
    "status"           int2      DEFAULT 0                 NOT NULL,
    "create_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "update_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "version"          int8      DEFAULT 0                 NOT NULL,
    "is_deleted"       int2      DEFAULT 0                 NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON COLUMN "tb_perm"."id"                           IS '主键';
COMMENT ON COLUMN "tb_perm"."name"                         IS '权限名称';
COMMENT ON COLUMN "tb_perm"."code"                         IS '权限代码';
COMMENT ON COLUMN "tb_perm"."configs"                      IS '配置信息';
COMMENT ON COLUMN "tb_perm"."extras"                       IS '额外信息';
COMMENT ON COLUMN "tb_perm"."desc"                         IS '备注';
COMMENT ON COLUMN "tb_perm"."status"                       IS '权限状态(0-启用, 1-禁用)';
COMMENT ON COLUMN "tb_perm"."create_time"                  IS '创建时间';
COMMENT ON COLUMN "tb_perm"."update_time"                  IS '更新时间';
COMMENT ON COLUMN "tb_perm"."version"                      IS '更新版本';
COMMENT ON COLUMN "tb_perm"."is_deleted"                   IS '是否删除(0-未删, 1-已删)';

COMMENT ON TABLE "tb_perm"                                 IS '权限信息';

-- ----------------------------
-- Trigger of tb_perm
-- ----------------------------
CREATE TRIGGER "tg_perm"
    BEFORE UPDATE ON "tb_perm"
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- ----------------------------
-- Records of tb_perm
-- ----------------------------
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000000, '上帝权限', '*');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000101, '租户管理', '@tenant:*');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000102, '租户查询', '@tenant:query');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000103, '租户新增', '@tenant:add');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000104, '租户修改', '@tenant:update');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000105, '租户删除', '@tenant:delete');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000201, '权限管理', '@perm:*');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000202, '权限查询', '@perm:query');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000203, '权限新增', '@perm:add');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000204, '权限修改', '@perm:update');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000205, '权限删除', '@perm:delete');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000301, '部门管理', '@dept:*');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000302, '部门查询', '@dept:query');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000303, '部门新增', '@dept:add');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000304, '部门修改', '@dept:update');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000305, '部门删除', '@dept:delete');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000401, '角色管理', '@role:*');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000402, '角色查询', '@role:query');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000403, '角色新增', '@role:add');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000404, '角色修改', '@role:update');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000405, '角色删除', '@role:delete');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000406, '角色授权', '@role:auth');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000501, '用户管理', '@user:*');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000502, '用户查询', '@user:query');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000503, '用户新增', '@user:add');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000504, '用户修改', '@user:update');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000505, '用户删除', '@user:delete');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000506, '用户授权', '@user:auth');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000601, '文件管理', '@file:*');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000602, '文件查询', '@file:query');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000701, '消息管理', '@notify:*');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000702, '消息查询', '@notify:query');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000703, '消息新增', '@notify:add');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000704, '消息修改', '@notify:update');
INSERT INTO "tb_perm" ("id", "name", "code") VALUES (10000000000000705, '消息删除', '@notify:delete');

-- ----------------------------
-- Table structure for tb_perm_assoc
-- ----------------------------
CREATE TABLE IF NOT EXISTS "tb_perm_assoc"
(
    "id"               int8                                NOT NULL,
    "perm_id"          int8                                NOT NULL,
    "assoc"            varchar(256)                        NOT NULL,
    "assoc_id"         int8                                NOT NULL,
    "effective"        int2      DEFAULT 0                 NOT NULL,
    "effective_time"   timestamp                           NULL,
    "category"         int2      DEFAULT 0                 NULL,
    "desc"             varchar(512)                        NULL,
    "create_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "update_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "version"          int8      DEFAULT 0                 NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON COLUMN "tb_perm_assoc"."id"                     IS '主键';
COMMENT ON COLUMN "tb_perm_assoc"."perm_id"                IS '权限主键(所属权限)';
COMMENT ON COLUMN "tb_perm_assoc"."assoc"                  IS '关联表名';
COMMENT ON COLUMN "tb_perm_assoc"."assoc_id"               IS '关联主键';
COMMENT ON COLUMN "tb_perm_assoc"."effective"              IS '限制时间(0-不限制, 1-限制)';
COMMENT ON COLUMN "tb_perm_assoc"."effective_time"         IS '有效时间';
COMMENT ON COLUMN "tb_perm_assoc"."category"               IS '关联类别(枚举字典)';
COMMENT ON COLUMN "tb_perm_assoc"."desc"                   IS '备注';
COMMENT ON COLUMN "tb_perm_assoc"."create_time"            IS '创建时间';
COMMENT ON COLUMN "tb_perm_assoc"."update_time"            IS '更新时间';
COMMENT ON COLUMN "tb_perm_assoc"."version"                IS '更新版本';

COMMENT ON TABLE "tb_perm_assoc"                           IS '权限关联';

-- ----------------------------
-- Trigger of tb_perm_assoc
-- ----------------------------
CREATE TRIGGER "tg_perm_assoc"
    BEFORE UPDATE ON "tb_perm_assoc"
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- ----------------------------
-- Records of tb_perm_assoc
-- ----------------------------
INSERT INTO "tb_perm_assoc" ("id", "perm_id", "assoc", "assoc_id") VALUES (10000000000000000, 10000000000000000, 'tb_user', 10000000000000000);

-- ----------------------------
-- Table structure for tb_dept
-- ----------------------------
CREATE TABLE IF NOT EXISTS "tb_dept"
(
    "id"               int8                                NOT NULL,
    "parent_id"        int8      DEFAULT 0                 NOT NULL,
    "path"             varchar(512)                        NOT NULL,
    "sort"             int4      DEFAULT 0                 NOT NULL,
    "name"             varchar(256)                        NOT NULL,
    "code"             varchar(256)                        NOT NULL,
    "configs"          text                                NULL,
    "extras"           text                                NULL,
    "desc"             varchar(512)                        NULL,
    "status"           int2      DEFAULT 0                 NOT NULL,
    "tenant_id"        int8                                NOT NULL,
    "create_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "update_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "version"          int8      DEFAULT 0                 NOT NULL,
    "is_deleted"       int2      DEFAULT 0                 NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON COLUMN "tb_dept"."id"                           IS '主键';
COMMENT ON COLUMN "tb_dept"."parent_id"                    IS '父级节点(父级主键)';
COMMENT ON COLUMN "tb_dept"."path"                         IS '节点路径';
COMMENT ON COLUMN "tb_dept"."sort"                         IS '节点顺序';
COMMENT ON COLUMN "tb_dept"."name"                         IS '部门名称';
COMMENT ON COLUMN "tb_dept"."code"                         IS '部门代码';
COMMENT ON COLUMN "tb_dept"."configs"                      IS '配置信息';
COMMENT ON COLUMN "tb_dept"."extras"                       IS '额外信息';
COMMENT ON COLUMN "tb_dept"."desc"                         IS '备注';
COMMENT ON COLUMN "tb_dept"."status"                       IS '部门状态(0-启用, 1-禁用)';
COMMENT ON COLUMN "tb_dept"."tenant_id"                    IS '租户主键(所属租户)';
COMMENT ON COLUMN "tb_dept"."create_time"                  IS '创建时间';
COMMENT ON COLUMN "tb_dept"."update_time"                  IS '更新时间';
COMMENT ON COLUMN "tb_dept"."version"                      IS '更新版本';
COMMENT ON COLUMN "tb_dept"."is_deleted"                   IS '是否删除(0-未删, 1-已删)';

COMMENT ON TABLE "tb_dept"                                 IS '部门信息';

-- ----------------------------
-- Trigger of tb_dept
-- ----------------------------
CREATE TRIGGER "tg_dept"
    BEFORE UPDATE ON "tb_dept"
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- ----------------------------
-- Table structure for tb_dept_assoc
-- ----------------------------
CREATE TABLE IF NOT EXISTS "tb_dept_assoc"
(
    "id"               int8                                NOT NULL,
    "dept_id"          int8                                NOT NULL,
    "assoc"            varchar(256)                        NOT NULL,
    "assoc_id"         int8                                NOT NULL,
    "effective"        int2      DEFAULT 0                 NOT NULL,
    "effective_time"   timestamp                           NULL,
    "category"         int2      DEFAULT 0                 NULL,
    "desc"             varchar(512)                        NULL,
    "tenant_id"        int8                                NOT NULL,
    "create_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "update_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "version"          int8      DEFAULT 0                 NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON COLUMN "tb_dept_assoc"."id"                     IS '主键';
COMMENT ON COLUMN "tb_dept_assoc"."dept_id"                IS '部门主键(所属部门)';
COMMENT ON COLUMN "tb_dept_assoc"."assoc"                  IS '关联表名';
COMMENT ON COLUMN "tb_dept_assoc"."assoc_id"               IS '关联主键';
COMMENT ON COLUMN "tb_dept_assoc"."effective"              IS '限制时间(0-不限制, 1-限制)';
COMMENT ON COLUMN "tb_dept_assoc"."effective_time"         IS '有效时间';
COMMENT ON COLUMN "tb_dept_assoc"."category"               IS '关联类别(枚举字典)';
COMMENT ON COLUMN "tb_dept_assoc"."desc"                   IS '备注';
COMMENT ON COLUMN "tb_dept_assoc"."tenant_id"              IS '租户主键(所属租户)';
COMMENT ON COLUMN "tb_dept_assoc"."create_time"            IS '创建时间';
COMMENT ON COLUMN "tb_dept_assoc"."update_time"            IS '更新时间';
COMMENT ON COLUMN "tb_dept_assoc"."version"                IS '更新版本';

COMMENT ON TABLE "tb_dept_assoc"                           IS '部门关联';

-- ----------------------------
-- Trigger of tb_dept_assoc
-- ----------------------------
CREATE TRIGGER "tg_dept_assoc"
    BEFORE UPDATE ON "tb_dept_assoc"
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
CREATE TABLE IF NOT EXISTS "tb_role"
(
    "id"               int8                                NOT NULL,
    "name"             varchar(256)                        NOT NULL,
    "code"             varchar(256)                        NOT NULL,
    "configs"          text                                NULL,
    "extras"           text                                NULL,
    "desc"             varchar(512)                        NULL,
    "status"           int2      DEFAULT 0                 NOT NULL,
    "create_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "update_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "version"          int8      DEFAULT 0                 NOT NULL,
    "is_deleted"       int2      DEFAULT 0                 NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON COLUMN "tb_role"."id"                           IS '主键';
COMMENT ON COLUMN "tb_role"."name"                         IS '角色名称';
COMMENT ON COLUMN "tb_role"."code"                         IS '角色代码';
COMMENT ON COLUMN "tb_role"."configs"                      IS '配置信息';
COMMENT ON COLUMN "tb_role"."extras"                       IS '额外信息';
COMMENT ON COLUMN "tb_role"."desc"                         IS '备注';
COMMENT ON COLUMN "tb_role"."status"                       IS '角色状态(0-启用, 1-禁用)';
COMMENT ON COLUMN "tb_role"."create_time"                  IS '创建时间';
COMMENT ON COLUMN "tb_role"."update_time"                  IS '更新时间';
COMMENT ON COLUMN "tb_role"."version"                      IS '更新版本';
COMMENT ON COLUMN "tb_role"."is_deleted"                   IS '是否删除(0-未删, 1-已删)';

COMMENT ON TABLE "tb_role"                                 IS '角色信息';

-- ----------------------------
-- Trigger of tb_role
-- ----------------------------
CREATE TRIGGER "tg_role"
    BEFORE UPDATE ON "tb_role"
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- ----------------------------
-- Records of tb_role
-- ----------------------------
INSERT INTO "tb_role" ("id", "name", "code") VALUES (10000000000000000, '管理员', 'admin');

-- ----------------------------
-- Table structure for tb_role_assoc
-- ----------------------------
CREATE TABLE IF NOT EXISTS "tb_role_assoc"
(
    "id"               int8                                NOT NULL,
    "role_id"          int8                                NOT NULL,
    "assoc"            varchar(256)                        NOT NULL,
    "assoc_id"         int8                                NOT NULL,
    "effective"        int2      DEFAULT 0                 NOT NULL,
    "effective_time"   timestamp                           NULL,
    "category"         int2      DEFAULT 0                 NULL,
    "desc"             varchar(512)                        NULL,
    "tenant_id"        int8                                NOT NULL,
    "create_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "update_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "version"          int8      DEFAULT 0                 NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON COLUMN "tb_role_assoc"."id"                     IS '主键';
COMMENT ON COLUMN "tb_role_assoc"."role_id"                IS '角色主键(所属角色)';
COMMENT ON COLUMN "tb_role_assoc"."assoc"                  IS '关联表名';
COMMENT ON COLUMN "tb_role_assoc"."assoc_id"               IS '关联主键';
COMMENT ON COLUMN "tb_role_assoc"."effective"              IS '限制时间(0-不限制, 1-限制)';
COMMENT ON COLUMN "tb_role_assoc"."effective_time"         IS '有效时间';
COMMENT ON COLUMN "tb_role_assoc"."category"               IS '关联类别(枚举字典)';
COMMENT ON COLUMN "tb_role_assoc"."desc"                   IS '备注';
COMMENT ON COLUMN "tb_role_assoc"."tenant_id"              IS '租户主键(所属租户)';
COMMENT ON COLUMN "tb_role_assoc"."create_time"            IS '创建时间';
COMMENT ON COLUMN "tb_role_assoc"."update_time"            IS '更新时间';
COMMENT ON COLUMN "tb_role_assoc"."version"                IS '更新版本';

COMMENT ON TABLE "tb_role_assoc"                           IS '角色关联';

-- ----------------------------
-- Trigger of tb_role_assoc
-- ----------------------------
CREATE TRIGGER "tg_role_assoc"
    BEFORE UPDATE ON "tb_role_assoc"
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
CREATE TABLE IF NOT EXISTS "tb_user"
(
    "id"               int8                                NOT NULL,
    "username"         varchar(256)                        NOT NULL,
    "password"         varchar(256)                        NULL,
    "salt"             varchar(256)                        NULL,
    "mobile"           varchar(256)                        NULL,
    "email"            varchar(256)                        NULL,
    "configs"          text                                NULL,
    "extras"           text                                NULL,
    "desc"             varchar(512)                        NULL,
    "login_time"       timestamp                           NULL,
    "status"           int2      DEFAULT 0                 NOT NULL,
    "tenant_id"        int8                                NOT NULL,
    "create_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "update_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "version"          int8      DEFAULT 0                 NOT NULL,
    "is_deleted"       int2      DEFAULT 0                 NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON COLUMN "tb_user"."id"                           IS '主键';
COMMENT ON COLUMN "tb_user"."username"                     IS '用户帐号';
COMMENT ON COLUMN "tb_user"."password"                     IS '用户密码';
COMMENT ON COLUMN "tb_user"."salt"                         IS '盐';
COMMENT ON COLUMN "tb_user"."mobile"                       IS '手机号码';
COMMENT ON COLUMN "tb_user"."email"                        IS '用户邮箱';
COMMENT ON COLUMN "tb_user"."configs"                      IS '配置信息';
COMMENT ON COLUMN "tb_user"."extras"                       IS '额外信息';
COMMENT ON COLUMN "tb_user"."desc"                         IS '备注';
COMMENT ON COLUMN "tb_user"."login_time"                   IS '登录时间';
COMMENT ON COLUMN "tb_user"."status"                       IS '用户状态(0-启用, 1-禁用)';
COMMENT ON COLUMN "tb_user"."tenant_id"                    IS '租户主键(默认租户)';
COMMENT ON COLUMN "tb_user"."create_time"                  IS '创建时间';
COMMENT ON COLUMN "tb_user"."update_time"                  IS '更新时间';
COMMENT ON COLUMN "tb_user"."version"                      IS '更新版本';
COMMENT ON COLUMN "tb_user"."is_deleted"                   IS '是否删除(0-未删, 1-已删)';

COMMENT ON TABLE "tb_user"                                 IS '用户信息';

-- ----------------------------
-- Trigger of tb_user
-- ----------------------------
CREATE TRIGGER "tg_user"
    BEFORE UPDATE ON "tb_user"
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO "tb_user" ("id", "username", "password", "salt", "tenant_id") VALUES (10000000000000000, 'admin', '$2a$10$QyjZ4c6BaPD3693XArMopey0RPoKkiIfqHAxaxapijuYbK9takS.a', '$2a$10$QyjZ4c6BaPD3693XArMope', 10000000000000000);

-- ----------------------------
-- Table structure for tb_user_assoc
-- ----------------------------
CREATE TABLE IF NOT EXISTS "tb_user_assoc"
(
    "id"               int8                                NOT NULL,
    "user_id"          int8                                NOT NULL,
    "assoc"            varchar(256)                        NOT NULL,
    "assoc_id"         int8                                NOT NULL,
    "effective"        int2      DEFAULT 0                 NOT NULL,
    "effective_time"   timestamp                           NULL,
    "category"         int2      DEFAULT 0                 NULL,
    "desc"             varchar(512)                        NULL,
    "create_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "update_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "version"          int8      DEFAULT 0                 NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON COLUMN "tb_user_assoc"."id"                     IS '主键';
COMMENT ON COLUMN "tb_user_assoc"."user_id"                IS '用户主键(所属用户)';
COMMENT ON COLUMN "tb_user_assoc"."assoc"                  IS '关联表名';
COMMENT ON COLUMN "tb_user_assoc"."assoc_id"               IS '关联主键';
COMMENT ON COLUMN "tb_user_assoc"."effective"              IS '限制时间(0-不限制, 1-限制)';
COMMENT ON COLUMN "tb_user_assoc"."effective_time"         IS '有效时间';
COMMENT ON COLUMN "tb_user_assoc"."category"               IS '关联类别(枚举字典)';
COMMENT ON COLUMN "tb_user_assoc"."desc"                   IS '备注';
COMMENT ON COLUMN "tb_user_assoc"."create_time"            IS '创建时间';
COMMENT ON COLUMN "tb_user_assoc"."update_time"            IS '更新时间';
COMMENT ON COLUMN "tb_user_assoc"."version"                IS '更新版本';

COMMENT ON TABLE "tb_user_assoc"                           IS '用户关联';

-- ----------------------------
-- Trigger of tb_user_assoc
-- ----------------------------
CREATE TRIGGER "tg_user_assoc"
    BEFORE UPDATE ON "tb_user_assoc"
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- ----------------------------
-- Table structure for tb_user_record
-- ----------------------------
CREATE TABLE IF NOT EXISTS "tb_user_record"
(
    "id"               int8                                NOT NULL,
    "user_id"          int8                                NOT NULL,
    "configs"          text                                NULL,
    "extras"           text                                NULL,
    "desc"             varchar(512)                        NULL,
    "create_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "update_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "version"          int8      DEFAULT 0                 NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON COLUMN "tb_user_record"."id"                    IS '主键';
COMMENT ON COLUMN "tb_user_record"."user_id"               IS '用户主键(所属用户)';
COMMENT ON COLUMN "tb_user_record"."configs"               IS '配置信息';
COMMENT ON COLUMN "tb_user_record"."extras"                IS '额外信息';
COMMENT ON COLUMN "tb_user_record"."desc"                  IS '备注';
COMMENT ON COLUMN "tb_user_record"."create_time"           IS '创建时间';
COMMENT ON COLUMN "tb_user_record"."update_time"           IS '更新时间';
COMMENT ON COLUMN "tb_user_record"."version"               IS '更新版本';

COMMENT ON TABLE "tb_user_record"                          IS '用户记录';

-- ----------------------------
-- Trigger of tb_user_record
-- ----------------------------
CREATE TRIGGER "tg_user_record"
    BEFORE UPDATE ON "tb_user_record"
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- ----------------------------
-- Table structure for tb_file
-- ----------------------------
CREATE TABLE IF NOT EXISTS "tb_file"
(
    "id"               int8                                NOT NULL,
    "url"              varchar(512)                        NOT NULL,
    "size"             int8                                NULL,
    "filename"         varchar(256)                        NULL,
    "orig_filename"    varchar(256)                        NULL,
    "base_path"        varchar(256)                        NULL,
    "path"             varchar(256)                        NULL,
    "ext"              varchar(32)                         NULL,
    "content_type"     varchar(128)                        NULL,
    "platform"         varchar(32)                         NULL,
    "th_url"           varchar(512)                        NULL,
    "th_filename"      varchar(256)                        NULL,
    "th_size"          int8                                NULL,
    "th_content_type"  varchar(128)                        NULL,
    "object_id"        varchar(32)                         NULL,
    "object_type"      varchar(32)                         NULL,
    "metadata"         text                                NULL,
    "user_metadata"    text                                NULL,
    "th_metadata"      text                                NULL,
    "th_user_metadata" text                                NULL,
    "attr"             text                                NULL,
    "file_acl"         varchar(32)                         NULL,
    "th_file_acl"      varchar(32)                         NULL,
    "hash_info"        text                                NULL,
    "upload_id"        varchar(128)                        NULL,
    "upload_status"    int8                                NULL,
    "status"           int2      DEFAULT 0                 NOT NULL,
    "create_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "update_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "version"          int8      DEFAULT 0                 NOT NULL,
    "is_deleted"       int2      DEFAULT 0                 NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON COLUMN "tb_file"."id"                           IS '主键';
COMMENT ON COLUMN "tb_file"."url"                          IS '文件访问地址';
COMMENT ON COLUMN "tb_file"."size"                         IS '文件大小，单位字节';
COMMENT ON COLUMN "tb_file"."filename"                     IS '文件名称';
COMMENT ON COLUMN "tb_file"."orig_filename"                IS '原始文件名';
COMMENT ON COLUMN "tb_file"."base_path"                    IS '基础存储路径';
COMMENT ON COLUMN "tb_file"."path"                         IS '存储路径';
COMMENT ON COLUMN "tb_file"."ext"                          IS '文件扩展名';
COMMENT ON COLUMN "tb_file"."content_type"                 IS 'MIME类型';
COMMENT ON COLUMN "tb_file"."platform"                     IS '存储平台';
COMMENT ON COLUMN "tb_file"."th_url"                       IS '缩略图访问路径';
COMMENT ON COLUMN "tb_file"."th_filename"                  IS '缩略图名称';
COMMENT ON COLUMN "tb_file"."th_size"                      IS '缩略图大小，单位字节';
COMMENT ON COLUMN "tb_file"."th_content_type"              IS '缩略图MIME类型';
COMMENT ON COLUMN "tb_file"."object_id"                    IS '文件所属对象ID';
COMMENT ON COLUMN "tb_file"."object_type"                  IS '文件所属对象类型，例如用户头像，评价图片';
COMMENT ON COLUMN "tb_file"."metadata"                     IS '文件元数据';
COMMENT ON COLUMN "tb_file"."user_metadata"                IS '文件用户元数据';
COMMENT ON COLUMN "tb_file"."th_metadata"                  IS '缩略图元数据';
COMMENT ON COLUMN "tb_file"."th_user_metadata"             IS '缩略图用户元数据';
COMMENT ON COLUMN "tb_file"."attr"                         IS '附加属性';
COMMENT ON COLUMN "tb_file"."file_acl"                     IS '文件ACL';
COMMENT ON COLUMN "tb_file"."th_file_acl"                  IS '缩略图文件ACL';
COMMENT ON COLUMN "tb_file"."hash_info"                    IS '哈希信息';
COMMENT ON COLUMN "tb_file"."upload_id"                    IS '上传ID，仅在手动分片上传时使用';
COMMENT ON COLUMN "tb_file"."upload_status"                IS '上传状态，仅在手动分片上传时使用，1：初始化完成，2：上传完成';
COMMENT ON COLUMN "tb_file"."status"                       IS '文件状态(0-启用, 1-禁用)';
COMMENT ON COLUMN "tb_file"."create_time"                  IS '创建时间';
COMMENT ON COLUMN "tb_file"."update_time"                  IS '更新时间';
COMMENT ON COLUMN "tb_file"."version"                      IS '更新版本';
COMMENT ON COLUMN "tb_file"."is_deleted"                   IS '是否删除(0-未删, 1-已删)';

COMMENT ON TABLE "tb_file"                                 IS '文件信息';

-- ----------------------------
-- Trigger of tb_file
-- ----------------------------
CREATE TRIGGER "tg_file"
    BEFORE UPDATE ON "tb_file"
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- ----------------------------
-- Table structure for tb_file_part
-- ----------------------------
CREATE TABLE IF NOT EXISTS "tb_file_part"
(
    "id"               int8                                NOT NULL,
    "platform"         varchar(32)                         NULL,
    "e_tag"            varchar(256)                        NULL,
    "part_number"      int2                                NULL,
    "part_size"        int8                                NULL,
    "hash_info"        text                                NULL,
    "upload_id"        varchar(128)                        NULL,
    "create_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "update_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "version"          int8      DEFAULT 0                 NOT NULL,
    "is_deleted"       int2      DEFAULT 0                 NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON COLUMN "tb_file_part"."id"                     IS '主键';
COMMENT ON COLUMN "tb_file_part"."platform"               IS '存储平台';
COMMENT ON COLUMN "tb_file_part"."e_tag"                  IS '分片ETag';
COMMENT ON COLUMN "tb_file_part"."part_number"            IS '分片号。每一个上传的分片都有一个分片号，一般情况下取值范围是1~10000';
COMMENT ON COLUMN "tb_file_part"."part_size"              IS '文件大小，单位字节';
COMMENT ON COLUMN "tb_file_part"."hash_info"              IS '哈希信息';
COMMENT ON COLUMN "tb_file_part"."upload_id"              IS '上传ID，仅在手动分片上传时使用';
COMMENT ON COLUMN "tb_file_part"."create_time"            IS '创建时间';
COMMENT ON COLUMN "tb_file_part"."update_time"            IS '更新时间';
COMMENT ON COLUMN "tb_file_part"."version"                IS '更新版本';
COMMENT ON COLUMN "tb_file_part"."is_deleted"             IS '是否删除(0-未删, 1-已删)';

COMMENT ON TABLE "tb_file_part"                           IS '文件分片';

-- ----------------------------
-- Trigger of tb_file_part
-- ----------------------------
CREATE TRIGGER "tg_file_part"
    BEFORE UPDATE ON "tb_file_part"
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- ----------------------------
-- Table structure for tb_notify_category
-- ----------------------------
CREATE TABLE IF NOT EXISTS "tb_notify_category"
(
    "id"               int8                                NOT NULL,
    "name"             varchar(256)                        NOT NULL,
    "code"             varchar(256)                        NOT NULL,
    "configs"          text                                NULL,
    "extras"           text                                NULL,
    "desc"             varchar(512)                        NULL,
    "status"           int2      DEFAULT 0                 NOT NULL,
    "create_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "update_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "version"          int8      DEFAULT 0                 NOT NULL,
    "is_deleted"       int2      DEFAULT 0                 NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON COLUMN "tb_notify_category"."id"                IS '主键';
COMMENT ON COLUMN "tb_notify_category"."name"              IS '类别名称';
COMMENT ON COLUMN "tb_notify_category"."code"              IS '类别代码';
COMMENT ON COLUMN "tb_notify_category"."configs"           IS '配置信息';
COMMENT ON COLUMN "tb_notify_category"."extras"            IS '额外信息';
COMMENT ON COLUMN "tb_notify_category"."desc"              IS '备注';
COMMENT ON COLUMN "tb_notify_category"."status"            IS '类别状态(0-启用, 1-禁用)';
COMMENT ON COLUMN "tb_notify_category"."create_time"       IS '创建时间';
COMMENT ON COLUMN "tb_notify_category"."update_time"       IS '更新时间';
COMMENT ON COLUMN "tb_notify_category"."version"           IS '更新版本';
COMMENT ON COLUMN "tb_notify_category"."is_deleted"        IS '是否删除(0-未删, 1-已删)';

COMMENT ON TABLE "tb_notify_category"                      IS '消息类别';

-- ----------------------------
-- Trigger of tb_notify_category
-- ----------------------------
CREATE TRIGGER "tg_notify_category"
    BEFORE UPDATE ON "tb_notify_category"
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- ----------------------------
-- Table structure for tb_ai_document
-- ----------------------------
CREATE TABLE IF NOT EXISTS "tb_ai_document"
(
    "id"               int8                                NOT NULL,
    "name"             varchar(256)                        NOT NULL,
    "code"             varchar(256)                        NOT NULL,
    "configs"          text                                NULL,
    "extras"           text                                NULL,
    "desc"             varchar(512)                        NULL,
    "status"           int2      DEFAULT 0                 NOT NULL,
    "create_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "update_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "version"          int8      DEFAULT 0                 NOT NULL,
    "is_deleted"       int2      DEFAULT 0                 NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON COLUMN "tb_ai_document"."id"                    IS '主键';
COMMENT ON COLUMN "tb_ai_document"."name"                  IS '文档名称';
COMMENT ON COLUMN "tb_ai_document"."code"                  IS '文档代码';
COMMENT ON COLUMN "tb_ai_document"."configs"               IS '配置信息';
COMMENT ON COLUMN "tb_ai_document"."extras"                IS '额外信息';
COMMENT ON COLUMN "tb_ai_document"."desc"                  IS '备注';
COMMENT ON COLUMN "tb_ai_document"."status"                IS '文档状态(0-启用, 1-禁用)';
COMMENT ON COLUMN "tb_ai_document"."create_time"           IS '创建时间';
COMMENT ON COLUMN "tb_ai_document"."update_time"           IS '更新时间';
COMMENT ON COLUMN "tb_ai_document"."version"               IS '更新版本';
COMMENT ON COLUMN "tb_ai_document"."is_deleted"            IS '是否删除(0-未删, 1-已删)';

COMMENT ON TABLE "tb_ai_document"                          IS '文档信息';

-- ----------------------------
-- Trigger of tb_ai_document
-- ----------------------------
CREATE TRIGGER "tg_ai_document"
    BEFORE UPDATE ON "tb_ai_document"
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- ----------------------------
-- Table structure for tb_ai_document_chunk
-- ----------------------------
CREATE TABLE IF NOT EXISTS "tb_ai_document_chunk"
(
    "id"               int8                                NOT NULL,
    "document_id"      int8                                NOT NULL,
    "content"          text                                NOT NULL,
    "sort"             int4      DEFAULT 0                 NULL,
    "configs"          text                                NULL,
    "extras"           text                                NULL,
    "create_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "update_time"      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "version"          int8      DEFAULT 0                 NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON COLUMN "tb_ai_document_chunk"."id"              IS '主键';
COMMENT ON COLUMN "tb_ai_document_chunk"."document_id"     IS '文档主键(所属文档)';
COMMENT ON COLUMN "tb_ai_document_chunk"."content"         IS '分片内容';
COMMENT ON COLUMN "tb_ai_document_chunk"."sort"            IS '分片顺序';
COMMENT ON COLUMN "tb_ai_document_chunk"."configs"         IS '配置信息';
COMMENT ON COLUMN "tb_ai_document_chunk"."extras"          IS '额外信息';
COMMENT ON COLUMN "tb_ai_document_chunk"."create_time"     IS '创建时间';
COMMENT ON COLUMN "tb_ai_document_chunk"."update_time"     IS '更新时间';
COMMENT ON COLUMN "tb_ai_document_chunk"."version"         IS '更新版本';

COMMENT ON TABLE "tb_ai_document_chunk"                    IS '分片信息';

-- ----------------------------
-- Trigger of tb_ai_document_chunk
-- ----------------------------
CREATE TRIGGER "tg_ai_document_chunk"
    BEFORE UPDATE ON "tb_ai_document_chunk"
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp();
