CREATE TABLE IF NOT EXISTS scan_history
(
    id                           int(11)             NOT NULL AUTO_INCREMENT,
    account_id                   varchar(50)         DEFAULT NULL,
    create_time                  bigint(13)          DEFAULT NULL,
    operator                     varchar(100)        DEFAULT NULL,
    resources_sum                bigint(13)          DEFAULT NULL,
    return_sum                   bigint(13)          DEFAULT NULL,
    scan_score                   int(3)              DEFAULT NULL,
    PRIMARY KEY ( id ),
    KEY IDX_ITEM_ID ( account_id )
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS scan_task_history
(
    id                           int(11)             NOT NULL AUTO_INCREMENT,
    scan_id                      int(11)             DEFAULT NULL,
    task_id                      varchar(50)         DEFAULT NULL,
    operation                    varchar(255)        DEFAULT NULL,
    output                       mediumtext          DEFAULT NULL,
    resources_sum                bigint(13)          DEFAULT NULL,
    return_sum                   bigint(13)          DEFAULT NULL,
    scan_score                   int(3)              DEFAULT NULL,
    PRIMARY KEY ( id )
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;