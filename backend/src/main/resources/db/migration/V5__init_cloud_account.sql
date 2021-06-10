CREATE TABLE IF NOT EXISTS plugin (
    `id`                      varchar(50)              NOT NULL COMMENT '插件ID',
    `name`                    varchar(100)             DEFAULT NULL UNIQUE COMMENT '名称',
    `icon`                    varchar(256)             DEFAULT NULL COMMENT '插件图标',
    `update_time`             bigint(13)               DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 ROW_FORMAT = DYNAMIC;

insert into `plugin` ( `id`, `name`, `icon`, `update_time`) values ('fit2cloud-qcloud-plugin', '腾讯云', 'qcloud.png', concat(unix_timestamp(now()), '001'));
insert into `plugin` ( `id`, `name`, `icon`, `update_time`) values ('fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'));
insert into `plugin` ( `id`, `name`, `icon`, `update_time`) values ('fit2cloud-huawei-plugin', '华为云', 'fusion.png', concat(unix_timestamp(now()), '001'));

CREATE TABLE IF NOT EXISTS cloud_account (
    `id`                         varchar(50)         NOT NULL DEFAULT '' COMMENT '云账号ID',
    `name`                       varchar(128)        DEFAULT NULL UNIQUE COMMENT '云账号名称',
    `credential`                 longtext            DEFAULT NULL COMMENT '云账号凭证',
    `plugin_id`                  varchar(50)         DEFAULT NULL COMMENT '插件ID',
    `plugin_name`                varchar(100)        DEFAULT NULL COMMENT '插件名称',
    `plugin_icon`                varchar(256)        DEFAULT NULL COMMENT '插件图标',
    `status`                     varchar(10)         DEFAULT NULL COMMENT '云账号状态',
    `create_time`                bigint(13)          DEFAULT NULL COMMENT '创建时间',
    `update_time`                bigint(13)          DEFAULT NULL COMMENT '更新时间',
    `creator`                    varchar(128)        DEFAULT NULL COMMENT '创建人',
    `regions`                    longtext            DEFAULT NULL COMMENT '区域',
    PRIMARY KEY (`id`),
    KEY `IDX_PLUGIN` (`plugin_name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_bin;
