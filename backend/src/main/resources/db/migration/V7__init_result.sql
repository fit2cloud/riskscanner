CREATE TABLE IF NOT EXISTS task
(
    id                            varchar(50)           NOT NULL,
    status                        varchar(20)           DEFAULT NULL,
    apply_user                    varchar(50)           DEFAULT NULL,
    create_time                   bigint(13)            DEFAULT NULL,
    task_name                     varchar(256)          DEFAULT NULL,
    description                   varchar(255)          DEFAULT NULL,
    cron                          varchar(128)          DEFAULT NULL,
    trigger_id                    varchar(255)          DEFAULT NULL,
    prev_fire_time                bigint(20)            DEFAULT NULL,
    last_fire_time                bigint(20)            DEFAULT NULL,
    type                          varchar(32)           DEFAULT NULL,
    severity                      varchar(32)           DEFAULT NULL,
    rule_id                       varchar(50)           DEFAULT NULL,
    rule_tags                     varchar(256)          DEFAULT NULL,
    account_id                    varchar(50)           DEFAULT NULL,
    plugin_id                     varchar(40)           DEFAULT NULL,
    plugin_name                   varchar(128)          DEFAULT NULL,
    plugin_icon                   varchar(128)          DEFAULT NULL,
    resource_types                varchar(256)          DEFAULT NULL,
    resources_sum                 bigint(13)            DEFAULT NULL,
    return_sum                    bigint(13)            DEFAULT NULL,
    cron_desc                     varchar(512)          DEFAULT NULL,
    scan_type                     varchar(32)           DEFAULT NULL,
    PRIMARY KEY ( id ),
    KEY IDX_STATUS ( status ),
    KEY IDX_APPLY_USER ( apply_user ),
    KEY IDX_TYPE ( type ),
    KEY IDX_CREATE_TIME ( create_time )
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;


CREATE TABLE IF NOT EXISTS task_item
(
    id                           varchar(50)         NOT NULL,
    task_id                      varchar(50)         DEFAULT NULL,
    rule_id                      varchar(50)         DEFAULT NULL,
    details                      longtext            DEFAULT NULL,
    tags                         longtext            DEFAULT NULL,
    custom_data                  longtext            DEFAULT NULL,
    status                       varchar(20)         DEFAULT NULL,
    count                        int(11)             DEFAULT 1,
    region_id                    varchar(128)        DEFAULT NULL ,
    region_name                  varchar(128)        DEFAULT NULL,
    severity                     varchar(32)         DEFAULT NULL,
    account_id                   varchar(50)         DEFAULT NULL,
    account_url                  varchar(128)        DEFAULT NULL,
    account_label                varchar(128)        DEFAULT NULL,
    create_time                  bigint(13)          DEFAULT NULL,
    update_time                  bigint(13)          DEFAULT NULL,
    PRIMARY KEY ( id ),
    KEY IDX_task_id ( task_id ),
    KEY IDX_rule_id ( rule_id )
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;


CREATE TABLE IF NOT EXISTS task_item_log
(
    id                           int(11)             NOT NULL AUTO_INCREMENT,
    task_item_id                 varchar(50)         DEFAULT NULL,
    resource_id                  varchar(50)         DEFAULT NULL,
    create_time                  bigint(13)          DEFAULT NULL,
    operator                     varchar(100)        DEFAULT NULL,
    operation                    varchar(255)        DEFAULT NULL,
    output                       mediumtext          DEFAULT NULL,
    result                       tinyint(1)          DEFAULT NULL,
    PRIMARY KEY ( id ),
    KEY IDX_ITEM_ID ( task_item_id ),
    KEY IDX_RES_ID ( resource_id )
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;


CREATE TABLE IF NOT EXISTS task_item_resource
(
    id                          int(11)             NOT NULL AUTO_INCREMENT,
    task_id                     varchar(50)         DEFAULT NULL,
    task_item_id                varchar(50)         DEFAULT NULL,
    resource_type               varchar(64)         DEFAULT NULL,
    resource_name               varchar(128)        DEFAULT NULL,
    dir_name                    varchar(128)        DEFAULT NULL,
    resource_id                 varchar(50)         DEFAULT NULL,
    resource_command            longtext            DEFAULT NULL,
    resource_command_action     longtext            DEFAULT NULL,
    PRIMARY KEY ( id ),
    KEY IDX_ITEM_ID ( task_item_id ),
    KEY IDX_RES_ID ( resource_id ),
    KEY IDX_task_id ( task_id )
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS resource
(
    id                         varchar(50)         NOT NULL,
    resource_name              varchar(128)        DEFAULT NULL,
    dir_name                   varchar(128)        DEFAULT NULL,
    resource_status            varchar(45)         DEFAULT NULL,
    resource_type              varchar(64)         DEFAULT NULL,
    custodian_run_log          longtext            DEFAULT NULL,
    metadata                   longtext            DEFAULT NULL,
    resources                  longtext            DEFAULT NULL,
    resources_sum              bigint(13)          DEFAULT NULL,
    return_sum                 bigint(13)          DEFAULT NULL,
    plugin_id                  varchar(40)         DEFAULT NULL,
    plugin_name                varchar(40)         DEFAULT NULL,
    plugin_icon                varchar(128)        DEFAULT NULL,
    account_id                 varchar(50)         DEFAULT NULL,
    region_id                  varchar(128)        DEFAULT NULL,
    region_name                varchar(128)        DEFAULT NULL,
    severity                   varchar(32)         DEFAULT NULL,
    create_time                bigint(13)          DEFAULT NULL,
    update_time                bigint(13)          DEFAULT NULL,
    resource_command           longtext            DEFAULT NULL,
    resource_command_action    longtext            DEFAULT NULL,
    PRIMARY KEY ( id )
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS resource_rule
(
    resource_id                varchar(50) NOT NULL DEFAULT '',
    rule_id                    varchar(50)          DEFAULT NULL,
    apply_user                 varchar(50)          DEFAULT NULL,
    PRIMARY KEY ( resource_id ),
    KEY IDX_rule_id ( rule_id ),
    KEY IDX_APPLY_USER ( apply_user )
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS resource_item
(
    id                         varchar(50)         NOT NULL,
    resource_id                varchar(50)         DEFAULT NULL,
    f2c_id                     varchar(256)        DEFAULT NULL,
    resource_type              varchar(64)         DEFAULT NULL,
    plugin_id                  varchar(40)         DEFAULT NULL,
    plugin_name                varchar(40)         DEFAULT NULL,
    plugin_icon                varchar(128)        DEFAULT NULL,
    account_id                 varchar(50)         DEFAULT NULL,
    region_id                  varchar(128)        DEFAULT NULL,
    region_name                varchar(128)        DEFAULT NULL,
    severity                   varchar(32)         DEFAULT NULL,
    create_time                bigint(13)          DEFAULT NULL,
    update_time                bigint(13)          DEFAULT NULL,
    resource                   longtext            DEFAULT NULL,
    PRIMARY KEY ( id )
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;