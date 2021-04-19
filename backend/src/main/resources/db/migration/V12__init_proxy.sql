CREATE TABLE IF NOT EXISTS proxy
(
    id                           int(11)             NOT NULL AUTO_INCREMENT,
    proxy_type                   varchar(50)         DEFAULT NULL,
    proxy_ip                     varchar(50)         DEFAULT NULL,
    proxy_port                   varchar(50)         DEFAULT NULL,
    proxy_name                   varchar(50)         DEFAULT NULL,
    proxy_password               varchar(50)         DEFAULT NULL,
    create_time                  bigint(13)          DEFAULT NULL,
    update_time                  bigint(13)          DEFAULT NULL,
    PRIMARY KEY ( id )
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

ALTER TABLE cloud_account add proxy_id int(11) DEFAULT NULL;
