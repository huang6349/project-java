-- noinspection SqlNoDataSourceInspectionForFile
-- noinspection SqlDialectInspectionForFile
-- noinspection SqlResolveForFile

-- ----------------------------
-- Table structure for tb_example
-- ----------------------------
CREATE TABLE IF NOT EXISTS "tb_example"
(
    "id"               int8                                 NOT NULL,
    "name"             varchar(256)                         NOT NULL,
    "code"             varchar(256)                         NOT NULL,
    "configs"          text                                 NULL,
    "extras"           text                                 NULL,
    "desc"             varchar(512)                         NULL,
    "status"           varchar(2) DEFAULT '0'               NOT NULL,
    "create_time"      timestamp  DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "update_time"      timestamp  DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "version"          int8                                 NULL,
    "is_deleted"       varchar(2) DEFAULT '0'               NOT NULL,
    CONSTRAINT "pk_example" PRIMARY KEY (id)
);

COMMENT ON COLUMN "tb_example"."id"                         IS '主键';
COMMENT ON COLUMN "tb_example"."name"                       IS '示例名称';
COMMENT ON COLUMN "tb_example"."code"                       IS '示例代码';
COMMENT ON COLUMN "tb_example"."configs"                    IS '配置信息';
COMMENT ON COLUMN "tb_example"."extras"                     IS '额外信息';
COMMENT ON COLUMN "tb_example"."desc"                       IS '备注';
COMMENT ON COLUMN "tb_example"."status"                     IS '示例状态(0-启用, 1-禁用)';
COMMENT ON COLUMN "tb_example"."create_time"                IS '创建时间';
COMMENT ON COLUMN "tb_example"."update_time"                IS '更新时间';
COMMENT ON COLUMN "tb_example"."version"                    IS '更新版本';
COMMENT ON COLUMN "tb_example"."is_deleted"                 IS '是否删除(0-未删, 1-已删)';
COMMENT ON TABLE  "tb_example"                              IS '示例信息';

-- ----------------------------
-- Trigger of tb_example
-- ----------------------------
CREATE TRIGGER "tg_example"
BEFORE UPDATE ON "tb_example"
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();
