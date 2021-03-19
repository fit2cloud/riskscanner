CREATE TABLE IF NOT EXISTS role (
    id          varchar(50)  NOT NULL COMMENT '角色 ID',
    name        varchar(64)  NOT NULL COMMENT '角色名称',
    description varchar(255) DEFAULT NULL COMMENT '角色描述',
    type        varchar(50)  DEFAULT NULL COMMENT '角色类型, (system)',
    create_time bigint(13)   NOT NULL COMMENT '创建时间',
    update_time bigint(13)   NOT NULL COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS system_parameter (
    param_key   varchar(64)                       NOT NULL COMMENT '参数名称',
    param_value varchar(2046)                     DEFAULT NULL COMMENT '参数值',
    type        varchar(100)                      NOT NULL DEFAULT 'text' COMMENT '参数类型',
    sort        int(5)                            DEFAULT NULL COMMENT '排序',
    PRIMARY KEY (param_key)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS user (
    id                   varchar(50)  NOT NULL COMMENT 'ID',
    name                 varchar(64)  NOT NULL COMMENT '用户名',
    email                varchar(64)  NOT NULL COMMENT '邮箱',
    password             varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '密码',
    status               varchar(50)  NOT NULL COMMENT '状态',
    create_time          bigint(13)   NOT NULL COMMENT '创建时间',
    update_time          bigint(13)   NOT NULL COMMENT '修改时间',
    language             varchar(30)  DEFAULT NULL,
    last_account_id      varchar(50)  DEFAULT NULL,
    phone                varchar(50)  DEFAULT NULL COMMENT '电话',
    source               varchar(50)  DEFAULT NULL COMMENT '来源',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS user_role (
    id          varchar(50) NOT NULL COMMENT 'ID',
    user_id     varchar(50) NOT NULL COMMENT '用户 ID',
    role_id     varchar(50) NOT NULL COMMENT '角色 ID',
    source_id   varchar(50) DEFAULT NULL COMMENT '来源信息',
    create_time bigint(13)  NOT NULL COMMENT '创建时间',
    update_time bigint(13)  NOT NULL COMMENT '修改时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE user_key (
    id          varchar(50) NOT NULL DEFAULT '' COMMENT 'ID',
    user_id     varchar(50) NOT NULL COMMENT '用户 ID',
    access_key  varchar(50) NOT NULL COMMENT 'access key',
    secret_key  varchar(50) NOT NULL COMMENT 'secret key',
    create_time bigint(13)  NOT NULL COMMENT '创建时间',
    status      varchar(10)          DEFAULT NULL COMMENT '状态',
    PRIMARY KEY (id),
    UNIQUE KEY IDX_AK (access_key),
    KEY IDX_USER_ID (user_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS operation_log (
  id                  varchar(64)         NOT NULL,
  resource_user_id    varchar(64)         NOT NULL DEFAULT '' COMMENT '资源拥有者 ID',
  resource_user_name  varchar(100)        NOT NULL DEFAULT '' COMMENT '资源拥有者名称',
  resource_type       varchar(45)         NOT NULL DEFAULT '' COMMENT '资源类型',
  resource_id         varchar(64)         DEFAULT NULL COMMENT '资源 ID',
  resource_name       varchar(128)        DEFAULT NULL COMMENT '资源名称',
  operation           varchar(45)         NOT NULL DEFAULT '' COMMENT '操作',
  time                bigint(13)          NOT NULL COMMENT '操作时间',
  message             mediumtext          DEFAULT NULL COMMENT '操作信息',
  source_ip           varchar(15)         DEFAULT NULL COMMENT '操作方IP',
  PRIMARY KEY (id),
  KEY IDX_USER_ID (resource_user_id),
  KEY IDX_OP (operation),
  KEY IDX_RES_ID (resource_id),
  KEY IDX_RES_NAME (resource_name),
  KEY IDX_USER_NAME (resource_user_name)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS message_task (
    id                  int(10)           NOT NULL AUTO_INCREMENT,
    type                varchar(50)       DEFAULT NULL COMMENT '消息类型',
    event               varchar(255)      DEFAULT NULL COMMENT '通知事件类型',
    user_id             varchar(512)      DEFAULT NULL COMMENT '接收人 ID',
    task_type           varchar(64)       DEFAULT NULL COMMENT '任务类型',
    identification      varchar(255)      DEFAULT NULL COMMENT '凭证',
    template            TEXT              DEFAULT NULL COMMENT '模板',
    is_set              tinyint(1)        DEFAULT NULL COMMENT '是否设置',
    create_time         bigint(13)        DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS message_order (
    id                        varchar(64)         NOT NULL,
    message_task_id           int(10)             DEFAULT NULL COMMENT '消息任务 ID',
    account_id                varchar(64)         DEFAULT NULL COMMENT '云账号 ID',
    account_name              varchar(64)         DEFAULT NULL COMMENT '云账号名称',
    status                    varchar(64)         DEFAULT NULL COMMENT '消息通知状态',
    create_time               bigint(13)          DEFAULT NULL COMMENT '创建时间',
    send_time                 bigint(13)          DEFAULT NULL COMMENT '发送消息通知时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS message_order_item (
     id                        int(10)             NOT NULL AUTO_INCREMENT,
     message_order_id          varchar(64)         DEFAULT NULL COMMENT '消息订单 ID',
     task_id                   varchar(64)         DEFAULT NULL COMMENT '扫描任务 ID',
     task_name                 varchar(64)         DEFAULT NULL COMMENT '扫描任务名称',
     status                    varchar(64)         DEFAULT NULL COMMENT '消息通知状态',
     create_time               bigint(13)          DEFAULT NULL COMMENT '创建时间',
     send_time                 bigint(13)          DEFAULT NULL COMMENT '发送消息通知时间',
     PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

