CREATE TABLE IF NOT EXISTS role (
    id              varchar(50)         NOT NULL,
    name            varchar(64)         NOT NULL,
    description     varchar(255)        DEFAULT NULL,
    type            varchar(50)         DEFAULT NULL,
    create_time     bigint(13)          NOT NULL,
    update_time     bigint(13)          NOT NULL,
    PRIMARY KEY ( id )
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS system_parameter (
    param_key       varchar(64)               NOT NULL,
    param_value     varchar(512)              DEFAULT NULL,
    type            varchar(100)              NOT NULL DEFAULT 'text',
    sort            int(5)                    DEFAULT NULL,
    PRIMARY KEY ( param_key )
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS user (
    id                   varchar(50)    NOT NULL,
    name                 varchar(64)    NOT NULL,
    email                varchar(64)    NOT NULL,
    password             varchar(256)   COLLATE utf8mb4_bin DEFAULT NULL,
    status               varchar(50)    NOT NULL,
    create_time          bigint(13)     NOT NULL,
    update_time          bigint(13)     NOT NULL,
    language             varchar(30)    DEFAULT NULL,
    last_account_id      varchar(50)    DEFAULT NULL,
    phone                varchar(50)    DEFAULT NULL,
    source               varchar(50)    DEFAULT NULL,
    PRIMARY KEY ( id )
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS user_role (
    id              varchar(50)     NOT NULL,
    user_id         varchar(50)     NOT NULL,
    role_id         varchar(50)     NOT NULL,
    source_id       varchar(50)     DEFAULT NULL,
    create_time     bigint(13)      NOT NULL,
    update_time     bigint(13)      NOT NULL,
    PRIMARY KEY ( id )
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE user_key (
    id              varchar(50)     NOT NULL,
    user_id         varchar(50)     NOT NULL,
    access_key      varchar(50)     NOT NULL,
    secret_key      varchar(50)     NOT NULL,
    create_time     bigint(13)      NOT NULL,
    status          varchar(10)     DEFAULT NULL,
    PRIMARY KEY ( id ),
    UNIQUE KEY IDX_AK ( access_key ),
    KEY IDX_USER_ID ( user_id )
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS operation_log (
  id                  varchar(64)         NOT NULL,
  resource_user_id    varchar(64)         NOT NULL DEFAULT '',
  resource_user_name  varchar(100)        NOT NULL DEFAULT '',
  resource_type       varchar(45)         NOT NULL DEFAULT '',
  resource_id         varchar(64)         DEFAULT NULL,
  resource_name       varchar(128)        DEFAULT NULL,
  operation           varchar(45)         NOT NULL DEFAULT '',
  time                bigint(13)          NOT NULL,
  message             mediumtext          DEFAULT NULL,
  source_ip           varchar(15)         DEFAULT NULL,
  PRIMARY KEY ( id ),
  KEY IDX_USER_ID ( resource_user_id ),
  KEY IDX_OP ( operation ),
  KEY IDX_RES_ID ( resource_id ),
  KEY IDX_RES_NAME ( resource_name ),
  KEY IDX_USER_NAME ( resource_user_name )
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS message_task (
    id                  int(10)           NOT NULL AUTO_INCREMENT,
    type                varchar(50)       DEFAULT NULL,
    event               varchar(255)      DEFAULT NULL,
    user_id             varchar(512)      DEFAULT NULL,
    task_type           varchar(64)       DEFAULT NULL,
    identification      varchar(255)      DEFAULT NULL,
    template            TEXT              DEFAULT NULL,
    is_set              tinyint(1)        DEFAULT NULL,
    create_time         bigint(13)        DEFAULT NULL,
    PRIMARY KEY ( id )
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS message_order (
    id                        varchar(64)         NOT NULL,
    message_task_id           int(10)             DEFAULT NULL,
    account_id                varchar(64)         DEFAULT NULL,
    account_name              varchar(64)         DEFAULT NULL,
    status                    varchar(64)         DEFAULT NULL,
    create_time               bigint(13)          DEFAULT NULL,
    send_time                 bigint(13)          DEFAULT NULL,
    PRIMARY KEY ( id )
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS message_order_item (
     id                        int(10)             NOT NULL AUTO_INCREMENT,
     message_order_id          varchar(64)         DEFAULT NULL,
     task_id                   varchar(64)         DEFAULT NULL,
     task_name                 varchar(64)         DEFAULT NULL,
     status                    varchar(64)         DEFAULT NULL,
     create_time               bigint(13)          DEFAULT NULL,
     send_time                 bigint(13)          DEFAULT NULL,
     PRIMARY KEY ( id )
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

