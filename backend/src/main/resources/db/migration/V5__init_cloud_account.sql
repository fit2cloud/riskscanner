CREATE TABLE IF NOT EXISTS plugin (
  id                      varchar(50)              NOT NULL,
  name                    varchar(100)             DEFAULT NULL UNIQUE,
  icon                    varchar(256)             DEFAULT NULL,
  update_time             bigint(13)               DEFAULT NULL,
  PRIMARY KEY ( id )
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 ROW_FORMAT = DYNAMIC;

insert into plugin ( id, name, icon, update_time) values ('fit2cloud-qcloud-plugin', '腾讯云', 'qcloud.png', concat(unix_timestamp(now()), '001'));
insert into plugin ( id, name, icon, update_time) values ('fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'));
insert into plugin ( id, name, icon, update_time) values ('fit2cloud-huawei-plugin', '华为云', 'fusion.png', concat(unix_timestamp(now()), '001'));

CREATE TABLE IF NOT EXISTS cloud_account (
   id                         varchar(50)         NOT NULL,
   name                       varchar(128)        DEFAULT NULL UNIQUE,
   credential                 longtext            DEFAULT NULL,
   plugin_id                  varchar(50)         DEFAULT NULL,
   plugin_name                varchar(100)        DEFAULT NULL,
   plugin_icon                varchar(256)        DEFAULT NULL,
   status                     varchar(10)         DEFAULT NULL,
   create_time                bigint(13)          DEFAULT NULL,
   update_time                bigint(13)          DEFAULT NULL,
   creator                    varchar(128)        DEFAULT NULL,
   regions                    longtext            DEFAULT NULL,
   PRIMARY KEY (id),
   KEY IDX_PLUGIN ( plugin_name )
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_bin;