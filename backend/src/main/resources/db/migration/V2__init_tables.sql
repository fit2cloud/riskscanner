CREATE TABLE IF NOT EXISTS `role` (
    `id`          varchar(50) NOT NULL COMMENT 'Role ID',
    `name`        varchar(64) NOT NULL COMMENT 'Role name',
    `description` varchar(255) DEFAULT NULL COMMENT 'Role description',
    `type`        varchar(50)  DEFAULT NULL COMMENT 'Role type, (system)',
    `create_time` bigint(13)  NOT NULL COMMENT 'Create timestamp',
    `update_time` bigint(13)  NOT NULL COMMENT 'Update timestamp',
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `system_parameter` (
    `param_key`   varchar(64) CHARACTER SET utf8mb4 NOT NULL COMMENT 'Parameter name',
    `param_value` varchar(255)                               DEFAULT NULL COMMENT 'Parameter value',
    `type`        varchar(100)                      NOT NULL DEFAULT 'text' COMMENT 'Parameter type',
    `sort`        int(5)                                     DEFAULT NULL COMMENT 'Sort',
    PRIMARY KEY (`param_key`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `user` (
    `id`                   varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT 'User ID',
    `name`                 varchar(64) NOT NULL COMMENT 'User name',
    `email`                varchar(64) NOT NULL COMMENT 'E-Mail address',
    `password`             varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
    `status`               varchar(50) NOT NULL COMMENT 'User status',
    `create_time`          bigint(13)  NOT NULL COMMENT 'Create timestamp',
    `update_time`          bigint(13)  NOT NULL COMMENT 'Update timestamp',
    `language`             varchar(30)  DEFAULT NULL,
    `last_account_id`      varchar(50)  DEFAULT NULL,
    `phone`                varchar(50)  DEFAULT NULL COMMENT 'Phone number of user',
    `source`               varchar(50)  DEFAULT NULL COMMENT 'Source of user',
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `user_role` (
    `id`          varchar(50) NOT NULL COMMENT 'ID of user''s role info',
    `user_id`     varchar(50) NOT NULL COMMENT 'User ID of this user-role info',
    `role_id`     varchar(50) NOT NULL COMMENT 'Role ID of this user-role info',
    `source_id`   varchar(50) DEFAULT NULL COMMENT 'The (system) ID of this user-role info',
    `create_time` bigint(13)  NOT NULL COMMENT 'Create timestamp',
    `update_time` bigint(13)  NOT NULL COMMENT 'Update timestamp',
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;

CREATE TABLE `user_key` (
    `id`          varchar(50) NOT NULL DEFAULT '' COMMENT 'user_key ID',
    `user_id`     varchar(50) NOT NULL COMMENT '用户ID',
    `access_key`  varchar(50) NOT NULL COMMENT 'access_key',
    `secret_key`  varchar(50) NOT NULL COMMENT 'secret key',
    `create_time` bigint(13)  NOT NULL COMMENT '创建时间',
    `status`      varchar(10)          DEFAULT NULL COMMENT '状态',
    PRIMARY KEY (`id`),
    UNIQUE KEY `IDX_AK` (`access_key`),
    KEY `IDX_USER_ID` (`user_id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `notice`(
   `EVENT` VARCHAR(100) NOT NULL,
   `TEST_ID` VARCHAR(100) NOT NULL,
   `NAME` VARCHAR(100) NOT NULL,
   `EMAIL` VARCHAR(100) NOT NULL,
   `ENABLE` VARCHAR(40) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `operation_log` (
  `id` varchar(64) NOT NULL,
  `resource_user_id` varchar(64) NOT NULL DEFAULT '' COMMENT '资源拥有者ID',
  `resource_user_name` varchar(100) NOT NULL DEFAULT '' COMMENT '资源拥有者名称',
  `resource_type` varchar(45) NOT NULL DEFAULT '' COMMENT '资源类型',
  `resource_id` varchar(64) DEFAULT NULL COMMENT '资源ID',
  `resource_name` varchar(128) DEFAULT NULL COMMENT '资源名称',
  `operation` varchar(45) NOT NULL DEFAULT '' COMMENT '操作',
  `time` bigint(13) NOT NULL COMMENT '操作时间',
  `message` mediumtext COMMENT '操作信息',
  `source_ip` varchar(15) DEFAULT NULL COMMENT '操作方IP',
  PRIMARY KEY (`id`),
  KEY `IDX_USER_ID` (`resource_user_id`),
  KEY `IDX_OP` (`operation`),
  KEY `IDX_RES_ID` (`resource_id`),
  KEY `IDX_RES_NAME` (`resource_name`),
  KEY `IDX_USER_NAME` (`resource_user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


