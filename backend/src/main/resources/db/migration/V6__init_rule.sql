CREATE TABLE IF NOT EXISTS `rule` (
    `id`                         varchar(50)         NOT NULL,
    `name`                       varchar(50)         DEFAULT NULL UNIQUE COMMENT '规则名称',
    `status`                     tinyint(1)          DEFAULT 1 COMMENT '规则状态(启用1，停用0)',
    `severity`                   varchar(32)         DEFAULT NULL COMMENT '风险等级',
    `description`                varchar(255)        DEFAULT NULL COMMENT '`描述',
    `script`                     mediumtext          DEFAULT NULL COMMENT '脚本',
    `parameter`                  varchar(1024)       DEFAULT NULL COMMENT '参数',
    `plugin_id`                  varchar(64)         DEFAULT NULL COMMENT '插件ID',
    `plugin_name`                varchar(64)         DEFAULT NULL COMMENT '云平台名称',
    `plugin_icon`                varchar(128)        DEFAULT NULL COMMENT '云平台图标',
    `last_modified`              bigint(14)          DEFAULT NULL COMMENT '上次更新时间',
    `flag`                       tinyint(1)          NOT NULL DEFAULT 0 COMMENT '是否内置',
    `scan_type`                  varchar(32)         DEFAULT NULL COMMENT '扫描类型',
    PRIMARY KEY (`id`),
    KEY `IDX_NAME` (`name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `rule_tag` (
    `tag_key`                    varchar(50)         NOT NULL DEFAULT '' UNIQUE COMMENT '标签标识',
    `tag_name`                   varchar(100)        NOT NULL DEFAULT '' UNIQUE COMMENT '标签名',
    `_index`                     int(11)             NOT NULL AUTO_INCREMENT COMMENT '索引',
    `flag`                       tinyint(1)          NOT NULL DEFAULT 0 COMMENT '是否内置',
    PRIMARY KEY (`tag_key`),
    KEY `IDX_KEY_NAME` (`tag_name`),
    KEY `IDX_INDEX` (`_index`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;


CREATE TABLE IF NOT EXISTS `rule_tag_mapping` (
    `id`                         int(10)             NOT NULL AUTO_INCREMENT,
    `rule_id`                    varchar(50)         NOT NULL DEFAULT '' COMMENT '规则ID',
    `tag_key`                    varchar(50)         NOT NULL DEFAULT '' COMMENT '标签标识',
    PRIMARY KEY (`id`),
    KEY `IDX_RULE_ID` (`rule_id`),
    KEY `IDX_TAG_KEY` (`tag_key`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;


CREATE TABLE IF NOT EXISTS `rule_type` (
    `id`                         varchar(50)         NOT NULL,
    `rule_id`                    varchar(50)         NOT NULL DEFAULT '' COMMENT '规则ID',
    `resource_type`              varchar(50)         NOT NULL DEFAULT '' COMMENT '资源类型',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `rule_account_parameter` (
    `id`                         int(11)             NOT NULL AUTO_INCREMENT,
    `account_id`                 varchar(50)         DEFAULT NULL COMMENT '云账号ID',
    `rule_id`                    varchar(50)         DEFAULT NULL COMMENT '规则ID',
    `parameter`                  varchar(1024)       DEFAULT NULL COMMENT '参数',
    `regions`                    longtext            DEFAULT NULL COMMENT '区域',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `rule_group` (
    `id`                         int(10)             NOT NULL AUTO_INCREMENT,
    `name`                       varchar(50)         DEFAULT NULL COMMENT '规则组名称',
    `description`                varchar(256)        DEFAULT NULL COMMENT '规则组描述',
    `level`                      varchar(64)         DEFAULT NULL COMMENT '风险级别',
    `plugin_id`                  varchar(64)         DEFAULT NULL COMMENT '插件ID',
    `flag`                       tinyint(1)          NOT NULL DEFAULT 0 COMMENT '是否内置',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `rule_group_mapping` (
    `id`                         int(10)             NOT NULL AUTO_INCREMENT,
    `rule_id`                    varchar(50)         DEFAULT NULL COMMENT '规则ID',
    `group_id`                   varchar(50)         DEFAULT NULL COMMENT '规则组ID',
    PRIMARY KEY (`id`),
    KEY `IDX_RULE_ID` (`rule_id`),
    KEY `IDX_GROUP_ID` (`group_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `rule_inspection_report` (
    `id`                         int(10)             NOT NULL AUTO_INCREMENT,
    `project`                    varchar(256)        DEFAULT NULL COMMENT '检查项目',
    `item_sort_first_level`      varchar(50)         DEFAULT NULL COMMENT '检查项一级分类',
    `item_sort_second_level`     varchar(50)         DEFAULT NULL COMMENT '检查项二级分类',
    `improvement`                varchar(512)        DEFAULT NULL COMMENT '改进建议',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `rule_inspection_report_mapping` (
    `id`                         int(10)             NOT NULL AUTO_INCREMENT,
    `rule_id`                    varchar(50)         DEFAULT NULL COMMENT '规则ID',
    `report_id`                  varchar(50)         DEFAULT NULL COMMENT '等保合规检查报告ID',
    PRIMARY KEY (`id`),
    KEY `IDX_RULE_ID` (`rule_id`),
    KEY `IDX_REPORT_ID` (`report_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;


INSERT INTO rule_tag (tag_key, tag_name, _index, flag) VALUES ('safety', '安全', 1, 1);
INSERT INTO rule_tag (tag_key, tag_name, _index, flag) VALUES ('cost', '费用', 2, 1);
INSERT INTO rule_tag (tag_key, tag_name, _index, flag) VALUES ('tagging', '标签', 3, 1);


INSERT INTO rule_group (`name`, `description`, `level`, `plugin_id`, `flag`) VALUES ('Aliyun 等保预检', '等保合规检查（全称为等级保护合规检查）为您提供了全面覆盖通信网络、区域边界、计算环境和管理中心的网络安全检查。', '等保三级', 'fit2cloud-aliyun-plugin', 1);
SELECT @groupId1 := LAST_INSERT_ID();
INSERT INTO rule_group (`name`, `description`, `level`, `plugin_id`, `flag`) VALUES ('Aliyun CIS合规检查', 'CIS（Center for Internet Security）合规检查能力，为您动态且持续地监控您保有在云上的资源是否符合 CIS Control 网络安全架构要求。', '高风险', 'fit2cloud-aliyun-plugin', 1);
SELECT @groupId2 := LAST_INSERT_ID();
INSERT INTO rule_group (`name`, `description`, `level`, `plugin_id`, `flag`) VALUES ('Aliyun OSS合规基线', 'OSS 合规检查为您提供全方位的对象存储资源检查功能。', '高风险', 'fit2cloud-aliyun-plugin', 1);
SELECT @groupId3 := LAST_INSERT_ID();

INSERT INTO rule_inspection_report VALUES (1, '应保证网络设备的业务处理能力满足业务高峰期需要。', '安全通信网络', '网络架构', '您可以通过VPC控制台，定期查看当前资源配额使用情况。');
INSERT INTO rule_inspection_report VALUES (2, '应保证网络各个部分的带宽满足业务高峰期需要。', '安全通信网络', '网络架构', '您可以根据业务实际情况在创建实例是申请带宽，云监控支持通过监控弹性公网IP支持网络带宽监控。另可以配置 DDoS 原生防护能力，保障业务高可用性。');
INSERT INTO rule_inspection_report VALUES (3, '应划分不同的网络区域，并按照方便管理和控制的原则为各网络区域分配地址。', '安全通信网络', '网络架构', '您可以基于业务需求划分不同的安全域，配置IP地址范围、配置路由表和网关等。');
INSERT INTO rule_inspection_report VALUES (4, '应避免将重要网络区域部署在边界处，重要网络区域与其他网络区域之间应采取可靠的技术隔离手段。', '安全通信网络', '网络架构', '您需要配置访问控制策略，可配置云防火墙实现VPC内部东西流量隔离，提升VPC安全性。');
INSERT INTO rule_inspection_report VALUES (5, '应提供通信线路、关键网络设备的硬件冗余，保证系统的可用性。', '安全通信网络', '网络架构', '您需要配置负载均衡产品，保障在流量波动情况下不中断对外服务。');
INSERT INTO rule_inspection_report VALUES (6, '应采用密码技术保证通信过程中数据的完整性。', '安全通信网络', '通信传输', '您需要设置证书，保障互联网通信的安全性和数据完整性。');
INSERT INTO rule_inspection_report VALUES (7, '应采用密码技术保证通信过程中数据的保密性。', '安全通信网络', '通信传输', '您需要设置证书，保障互联网通信的安全性和数据完整性。');
INSERT INTO rule_inspection_report VALUES (8, '可基于可信根对通信设备的系统引导程序、系统程序、重要配置参数和通信应用程序等进行可信验证，并在应用程序的所有执行环节进行动态可信验证，在检测到其可信性受到破坏后进行报警，并将验证结果形成审计记录送至安全管理中心，并进行动态关联感知。', '安全通信网络', '可信验证', '因该指标只要求“可”，不是强制要求项。');
INSERT INTO rule_inspection_report VALUES (9, '应保证跨越边界的访问和数据流通过边界设备提供的受控接口进行通信。', '安全区域边界', '边界防护', '您可以通过IPSEC VPN远程访问，使业务数据可以在公网上通过IP加密信道进行传输，同时访问使用SSL VPN，保障通信链路中数据的保密性。');
INSERT INTO rule_inspection_report VALUES (10, '应能够对非授权设备私自联到内部网络的行为进行限制或检查。', '安全区域边界', '边界防护', '您需要部署第三方网络接入控制系统，限制运维终端等设备非法接入到内部网络，云防火墙支持云服务器从内对外访问控制策略配置。');
INSERT INTO rule_inspection_report VALUES (11, '应能够对内部用户非授权联到外部网络的行为进行限制或检查。', '安全区域边界', '边界防护', '部署云防火墙实现南北向和东西向访问的网络流量分析，全网流量可视化，对主动外联行为的分析和阻断，配置开通、变更白名单策略。\n部署云安全中心实现非授权联到外部恶意域名、IP地址的行为进行检查和拦截。\n如果是IDC环境，您需要根据云下资产对象，独立部署第三方网络接入控制系统。');
INSERT INTO rule_inspection_report VALUES (12, '应限制无线网络的使用，保证无线网络通过受控的边界设备接入内部网络。', '安全区域边界', '边界防护', '您可以根据云下无线网络环境，部署第三方网络接入控制系统，配置禁用无线网卡的策略，无线网络的使用按照客户需求和具体应用场景而定应限制无线网络的使用，保证无线网络通过受控的边界设备接入内部网络。');
INSERT INTO rule_inspection_report VALUES (13, '应在网络边界或区域之间根据访问控制策略设置访问控制规则，默认情况下除允许通信外受控接口拒绝所有通信。', '安全区域边界', '访问控制', '云服务客户配置ACL访问控制策略，访问控制粒度为端口级。\n部署云防火墙实现统一管理互联网到业务的南北向访问策略和业务与业务之间的东西向微隔离策略，达到端口级访问控制粒度。');
INSERT INTO rule_inspection_report VALUES (14, '应删除多余或无效的访问控制规则，优化访问控制列表，并保证访问控制规则数量最小化。', '安全区域边界', '访问控制', '您需要根据业务需求优化访问控制列表。\n部署云防火墙实现策略命中计数功能，确保没有无效的冗余策略，云防火墙访问控制策略可配置优先级，优化访问控制列表。');
INSERT INTO rule_inspection_report VALUES (15, '应对源地址、目的地址、源端口、目的端口和协议等进行检查，以允许/拒绝数据包进出。', '安全区域边界', '访问控制', '您需要根据业务需求配置恰当的访问控制策略表。\n部署云防火墙实现对进出访问控制策略进行严格设置，访问控制策略包括源类型、访问源、目的类型、目的、协议类型、目的端口、应用协议、动作、描述和优先级。');
INSERT INTO rule_inspection_report VALUES (16, '应能根据会话状态信息为进出数据流提供明确的允许/拒绝访问的能力。', '安全区域边界', '访问控制', '部署云安全中心企业版实现网络流量和主机中的入侵行为和文件样本进行实时检测和防御。\n部署云防火墙实现对互联网上的恶意流量入侵活动和常规攻击行为进行实时阻断和拦截。\n部署Web应用防火墙实现流量检测、过滤、清洗后再代理转发到应用服务器。\n部署DDoS高防实现抵御各类基于网络层、传输层及应用层的DDoS攻击，秒级启动流量清洗，过滤掉攻击流量支持全自动检测和攻击策略匹配，实时防护，清洗服务可用性99.95%，可定制99.99%。');
INSERT INTO rule_inspection_report VALUES (17, '应对进出网络的数据流实现基于应用协议和应用内容的访问控制。', '安全区域边界', '访问控制', '部署云防火墙实现跨VPC数据流的应用协议、内容的访问控制。\n部署Web应用防火墙实现应用级协议和内容访问控制。\n部署DDoS高防实现所有业务流量进行清洗，支持网络四层和七层防护。');
INSERT INTO rule_inspection_report VALUES (18, '应在关键网络节点处检测、防止或限制从外部发起的网络攻击行为。', '安全区域边界', '入侵防范', '部署云安全中心企业版实现网络流量和主机中的入侵行为和文件样本进行实时检测和防御。\n部署云防火墙实现对互联网上的恶意流量入侵活动和常规攻击行为进行实时阻断和拦截。\n部署Web应用防火墙实现流量检测、过滤、清洗后再代理转发到应用服务器。\n部署DDoS高防实现抵御各类基于网络层、传输层及应用层的DDoS攻击，秒级启动流量清洗，过滤掉攻击流量支持全自动检测和攻击策略匹配，实时防护，清洗服务可用性99.95%，可定制99.99%。');
INSERT INTO rule_inspection_report VALUES (19, '应在关键网络节点处检测、防止或限制从内部发起的网络攻击行为。', '安全区域边界', '入侵防范', '部署云安全中心企业版实现主机系统层和应用层的主动外联和恶意攻击行为，对进程、网络异常行为进行预警。\n部署云防火墙实现检测东西向流量，配置相应的安全策略，阻断防止从内部发起的网络攻击行为。');
INSERT INTO rule_inspection_report VALUES (20, '应采取技术措施对网络行为进行分析，实现对网络攻击特别是新型网络攻击行为的分析。', '安全区域边界', '入侵防范', '部署云安全中心企业版实现挖矿、勒索、蠕虫、DDoS木马等基于新型网络攻击的攻击预警。\n部署云防火墙对云上进出网络的恶意流量进行实时检测与阻断，支持防御挖矿蠕虫等新型网络攻击，并通过积累大量恶意攻击样本，形成精准防御规则；云防火墙入侵检测功能支持发现挖矿蠕虫感染事件。');
INSERT INTO rule_inspection_report VALUES (21, '当检测到攻击行为时，记录攻击源IP、攻击类型、攻击目的、攻击时间，在发生严重入侵事件时应提供报警。', '安全区域边界', '入侵防范', '部署云安全中心企业版实现检测到威胁时，记录事件账号/源地址、攻击类型、攻击时间、事件级别以及处置建议，同时支持自动化攻击溯源，展示攻击过程。\n部署云防火墙实现检测攻击行为，提供网络阻断功能，记录风险级别、事件名称、防御状态、源IP、目的IP、方向、判断来源、发生时间和动作。\n部署Web应用防火墙实现HTTP和HTTPS流量攻击，记录攻击事件类型、攻击URL、来源IP、目的Host、时间、Get请求内容和拦截动作。\n部署DDoS高防实现DDoS攻击时，记录攻击类型、攻击目标、攻击时间、攻击流量峰值和清洗防护结果。');
INSERT INTO rule_inspection_report VALUES (22, '应在关键网络节点处对恶意代码进行检测和清除，并维护恶意代码防护机制的升级和更新。', '安全区域边界', '恶意代码和垃圾邮件防范', '部署云安全中心实现蠕虫病毒、勒索病毒、木马、网站后门等恶意代码的检测和隔离清除，定期升级相关恶意代码规则库。\n部署云防火墙实现网络入侵防护和恶意代码防范。\n部署Web应用防火墙实现流量恶意代码检测提供恶意代码检测功能，恶意代码规则库定期更新。');
INSERT INTO rule_inspection_report VALUES (23, '应在关键网络节点处对垃圾邮件进行检测和防护，并维护垃圾邮件防护机制的升级和更新。', '安全区域边界', '恶意代码和垃圾邮件防范', '您需求加固自身邮件服务器具备防垃圾邮件功能或部署第三方邮件防护系统。');
INSERT INTO rule_inspection_report VALUES (24, '应在网络边界、重要网络节点进行安全审计，审计覆盖到每个用户，对重要的用户行为和重要安全事件进行审计。', '安全区域边界', '安全审计', '部署云安全中心企业版实现主机登录日志、进程启动、网络连接、端口快照、账户快照、暴力破解、网络会话、DNS解析、WEB会话、安全告警、漏洞和基线日志进行审计。\n部署云防火墙实现日志审计模块记录所有流量日志、事件日志和操作日志。\n部署Web应用防火墙对攻击日志支持日志实时查询分析。\n部署堡垒机实现对云端虚拟主机ECS资产进行运维权限管控及运维审计。\n部署数据库审计产品实现对云上数据库访问行为进行监控，分析危险操作及可疑行为。');
INSERT INTO rule_inspection_report VALUES (25, '审计记录应包括事件的日期和时间、用户、事件类型、事件是否成功及其他与审计相关的信息。', '安全区域边界', '安全审计', '部署云安全中心企业版实现检测到威胁时，记录事件账号/源地址、攻击类型、攻击时间、事件级别以及处置建议。\n部署云防火墙实现日志记录事件被扫描到的时间、威胁类型、出/入方向、源IP和目的IP、应用类型、严重性等级以及动作状态等信息；\n部署Web应用防火墙实现记录攻击事件类型、攻击URL、来源IP、目的Host、时间、Get请求内容和拦截动作。\n部署DDoS高防实现检测到DDoS攻击时，记录攻击类型、攻击目标、攻击时间、攻击流量峰值和清洗防护结果。\n部署堡垒机实现日志记录，包括重要性、时间、日志类型、日志内容、用户、源IP地址和结果。');
INSERT INTO rule_inspection_report VALUES (26, '应对审计记录进行保护，定期备份，避免受到未预期的删除、修改或覆盖等。', '安全区域边界', '安全审计', '部署云安全中心、云防火墙、Web应用防火墙、DDoS高防、堡垒机和数据库审计等安全产品实现日志分析功能，依托日志服务产品，可存储6个月内的日志数据提供实时日志分析能力。');
INSERT INTO rule_inspection_report VALUES (27, '应能对远程访问的用户行为、访问互联网的用户行为等单独进行行为审计和数据分析。', '安全区域边界', '安全审计', '部署云安全中心、云防火墙、Web应用防火墙、DDoS高防、堡垒机和数据库审计等安全产品实现日志分析功能，依托日志服务产品，可存储6个月内的日志数据提供实时日志分析能力。');
INSERT INTO rule_inspection_report VALUES (28, '可基于可信根对边界设备的系统引导程序、系统程序、重要配置参数和边界防护应用程序等进行可信验证，并在应用程序的所有执行环节进行动态可信验证，在检测到其可信性受到破坏后进行报警，并将验证结果形成审计记录送至安全管理中心，并进行动态关联感知。', '安全区域边界', '可信验证', '因该指标只要求“可”，不是强制要求项。');
INSERT INTO rule_inspection_report VALUES (29, '应对登录的用户进行身份标识和鉴别，身份标识具有唯一性，身份鉴别信息具有复杂度要求并定期更换。', '安全计算环境', '身份鉴别', '部署云安全中心企业版实现对云服务客户登录的配置和密码复杂度进行定期安全检查，并提供安全建议并进行预警。\n部署堡垒机实现口令策略支持8个字符以上，必须包含大写字母、小写字母、数字及特殊符号，用户身份有唯一标识，口令90天定期跟换，新用户强制更改口令。');
INSERT INTO rule_inspection_report VALUES (30, '应具有登录失败处理功能，应配置并启用结束会话、限制非法登录次数和当登录连接超时自动退出等相关措施。', '安全计算环境', '身份鉴别', '部署堡垒机实现登录失败处理功能配置，建议配置云服务客户最大登录失败5次，临时锁定30分钟，登录连接超时时间为30分钟。\n部署云安全中心实现登录失败防御配置，支持云服务客户配置最大登录失败10次数，临时阻断连接1天、3天和7天。');
INSERT INTO rule_inspection_report VALUES (31, '当进行远程管理时，应采取必要措施，防止鉴别信息在网络传输过程中被窃听。', '安全计算环境', '身份鉴别', '部署堡垒机实现远程连接至云服务器时，采用安全的SSH方式进行远程登录。\n部署云安全中心实现对远程管理的配置进行检查，防止不安全的配置导致传输被窃听。');
INSERT INTO rule_inspection_report VALUES (32, '应采用口令、密码技术、生物技术等两种或两种以上组合的鉴别技术对用户进行身份鉴别，且其中一种鉴别技术至少应使用密码技术来实现。', '安全计算环境', '身份鉴别', '部署堡垒机实现作为唯一入口管理服务器，支持包括虚拟MFA、短信验证码在内的多因子认证。\n部署云安全中心实现对云平台配置提供基线核查，能够实时发现主账号双因素认证风险。');
INSERT INTO rule_inspection_report VALUES (33, '应对登录的用户分配账户和权限。', '安全计算环境', '访问控制', '您需求根据业务应用系统进行安全配置。需基于RAM对云服务进行合理授权。部署堡垒机实现作为唯一入口管理服务器。部署云安全中心实现对基线配置进行核查，识别潜在的账户、权限配置隐患。');
INSERT INTO rule_inspection_report VALUES (34, '应重命名或删除默认账户，修改默认账户的默认口令。', '安全计算环境', '访问控制', '您需要根据自身安全基线重命名或删除多余过期账户，或增强其口令。\n部署云安全中心实现安全基线的持续检查，告警。');
INSERT INTO rule_inspection_report VALUES (35, '应及时删除或停用多余的、过期的账户，避免共享账户的存在。', '安全计算环境', '访问控制', '部署堡垒机实现云服务客户对无用、多余账户会锁定或删除管理。\n部署云安全中心实现对平台账户进行安全配置检查。');
INSERT INTO rule_inspection_report VALUES (36, '应授予管理用户所需的最小权限，实现管理用户的权限分离。', '安全计算环境', '访问控制', '部署堡垒机实现基于用户角色分配权限，实现三权分立，角色分为超级管理员、审计员和运维员。\n部署云安全中心实现对账户权限配置的安全检查，云服务客户权限分离的检查。');
INSERT INTO rule_inspection_report VALUES (37, '应由授权主体配置访问控制策略，访问控制策略规定主体对客体的访问规则。', '安全计算环境', '访问控制', '您需要基于云产品配置指南和业务应用系统需求合理配置。');
INSERT INTO rule_inspection_report VALUES (38, '访问控制的粒度应达到主体为用户级或进程级，客体为文件、数据库表级。', '安全计算环境', '访问控制', '您需要基于云产品配置指南和业务应用系统需求合理配置。');
INSERT INTO rule_inspection_report VALUES (39, '应在关键网络节点处检测、防止或限制从内部发起的网络攻击行为。', '安全计算环境', '访问控制', '因现使用的操作系统安全级别无法达到B类强制保护级，操作系统侧自身暂无法实现强制访问控制。');
INSERT INTO rule_inspection_report VALUES (40, '应启用安全审计功能，审计覆盖到每个用户，对重要的用户行为和重要安全事件进行审计。', '安全计算环境', '安全审计', '部署堡垒机实现对所有云服务客户的虚拟主机操作进行审计，系统自身日志本地保存。\n部署数据库审计产品实现对数据库风险操作行为进行记录，提供细粒度审计数据库访问行为。\n部署云安全中心实现对账户登录进行记录，提供细粒度审计登录时间、登录账户、登录结果的记录，并通过报表进行实时监控预警。');
INSERT INTO rule_inspection_report VALUES (41, '审计记录应包括事件的日期、时间、类型、主体标识、客体标识和结果等。', '安全计算环境', '安全审计', '部署堡垒机实现日志记录，包括重要性、时间、日志类型、日志内容、用户、源IP地址和结果。\n部署数据库审计产品实现多维度线索分析，包括会话行为、SQL行为、风险行为和政策性报表。\n部署云安全中心实现日志记录，包括主机登录日志、进程启动、网络连接、账户快照、端口快照、DNS、Web访问、网站会话、云平台操作等日志结果。');
INSERT INTO rule_inspection_report VALUES (42, '应对审计记录进行保护，定期备份，避免受到未预期的删除、修改或覆盖等。', '安全计算环境', '安全审计', '部署堡垒机、数据库审计产品实现日志实时推送至日志服务，审计记录可按需调整存储空间，支持6个月以上保存期。\n部署云安全中心实现日志记录保存期为6个月，同时支持对操作审计日志配置进行基线核查。');
INSERT INTO rule_inspection_report VALUES (43, '应对审计进程进行保护，防止未经授权的中断。', '安全计算环境', '安全审计', '部署堡垒机、数据库审计产品实现日志实时推送至日志服务。\n部署云安全中心实现对操作审计日志配置进行基线核查。');
INSERT INTO rule_inspection_report VALUES (44, '应遵循最小安装的原则，仅安装需要的组件和应用程序。', '安全计算环境', '入侵防范', '云安全中心客户端在上线前，均经过内部的安全审核，并进行安全加固，遵循最小化安装原则。');
INSERT INTO rule_inspection_report VALUES (45, '应关闭不需要的系统服务、默认共享和高危端口。', '安全计算环境', '入侵防范', '部署云安全中心企业版开启云平台配置检查对主机、SLB、RDS、OSS等云产品进行高危配置、端口的基线检查。');
INSERT INTO rule_inspection_report VALUES (46, '应通过设定终端接入方式或网络地址范围对通过网络进行管理的管理终端进行限制。', '安全计算环境', '入侵防范', '部署云安全中心企业版开启云平台配置检查对主机、SLB、RDS、OSS等云产品进行高危配置、端口的基线检查。');
INSERT INTO rule_inspection_report VALUES (47, '应提供数据有效性检验功能，保证通过人机接口输入或通过通信接口输入的内容符合系统设定要求。', '安全计算环境', '入侵防范', '您需对业务系统对输入数据的有效性进行验证，过滤特殊字符。');
INSERT INTO rule_inspection_report VALUES (48, '应能发现可能存在的已知漏洞，并在经过充分测试评估后，及时修补漏洞。', '安全计算环境', '入侵防范', '部署云安全中心企业版实现漏洞修复，组件支持Linux漏洞、Windows漏洞、Web-CMS漏洞、应用漏洞和应急漏洞的一键修复功能，支持安全基线检测策略，支持平台安全配置等检查，能够实时检测已知漏洞。\n部署漏洞扫描实现资产威胁检测，发现云服务客户业务系统关联资产，实现自动化漏洞渗透测试和敏感内容监测。\n进行渗透测试服务实现模拟黑客对云服务客户业务系统进行安全测试，发现安全缺陷和漏洞，并提出修复建议。');
INSERT INTO rule_inspection_report VALUES (49, '应能够检测到对重要节点进行入侵的行为，并在发生严重入侵事件时提供报警。', '安全计算环境', '入侵防范', '部署云安全中心企业版实现检测到对重要节点入侵行为并进行告警，主要包括异常登录检测、网站后门查杀（Webshell）、主机异常行为检测（进程异常行为和异常网络连接检测）、主机系统及应用的关键文件篡改检测和异常账号检测等。\n部署Web应用防火墙实现将Web流量引流到WAF上，由WAF将流量进行检测、过滤、清洗后再代理转发到应用服务器。');
INSERT INTO rule_inspection_report VALUES (50, '应采用免受恶意代码攻击的技术措施或主动免疫可信验证机制及时识别入侵和病毒行为，并将其有效阻断。', '安全计算环境', '恶意代码和垃圾邮件防范', '部署云安全中心企业版通过安全加固镜像部署代理，实现对恶意的代码实时拦截功能，在系统内核层面，识别恶意代码，并进行主动拦截。');
INSERT INTO rule_inspection_report VALUES (51, '可基于可信根对计算设备的系统引导程序、系统程序、重要配置参数和应用程序等进行可信验证，并在应用程序的所有执行环节进行动态可信验证，在检测到其可信性受到破坏后进行报警，并将验证结果形成审计记录送至安全管理中心，并进行动态关联感知。', '安全计算环境', '可信验证', '您需要在使用云产品支持常用产品开启加密链路功能。');
INSERT INTO rule_inspection_report VALUES (52, '应采用密码技术保证重要数据在传输过程中的完整性，包括但不限于鉴别数据、重要业务数据、重要审计数据、重要配置数据、重要视频数据和重要个人信息等。', '安全计算环境', '数据完整性', '您需要在使用云产品时，启用各云产品自带的完整性校验功能。');
INSERT INTO rule_inspection_report VALUES (53, '应采用密码技术保证重要数据在存储过程中的完整性，包括但不限于鉴别数据、重要业务数据、重要审计数据、重要配置数据、重要视频数据和重要个人信息等。', '安全计算环境', '数据完整性', '您需要在使用云产品时，启用各云产品自带的完整性校验功能。');
INSERT INTO rule_inspection_report VALUES (54, '应采用密码技术保证重要数据在传输过程中的保密性，包括但不限于鉴别数据、重要业务数据和重要个人信息等。', '安全计算环境', '数据保密性', '您需要在使用云产品时，如SLB、CDN、OSS、RDS等常用产品时开启加密链路功能。\n部署证书服务支持云上签发第三方CA证书颁发机构的SSL证书，实现Https。');
INSERT INTO rule_inspection_report VALUES (55, '应采用密码技术保证重要数据在存储过程中的保密性，包括但不限于鉴别数据、重要业务数据和重要个人信息等。', '安全计算环境', '数据保密性', '您需选择、配置恰当的存储加密方式。');
INSERT INTO rule_inspection_report VALUES (56, '应提供重要数据的本地数据备份与恢复功能。', '安全计算环境', '数据备份恢复', '您需对重要的数据进行本地备份。');
INSERT INTO rule_inspection_report VALUES (57, '应提供异地实时备份功能，利用通信网络将重要数据实时备份至备份场地。', '安全计算环境', '数据备份恢复', '您需根据自身重要数据和业务数据配置恰当备份策略，实现实时备份。');
INSERT INTO rule_inspection_report VALUES (58, '应提供重要数据处理系统的热冗余，保证系统的高可用性。', '安全计算环境', '数据备份恢复', '云产品基于飞天分布式操作系统高可用架构，存储产品支持高可用性，支持用于基于业务处理能力，按照需求动态调整资源，保证系统高可用。');
INSERT INTO rule_inspection_report VALUES (59, '应保证鉴别信息所在的存储空间被释放或重新分配前得到完全清除。', '安全计算环境', '剩余信息保护', '云平台对存储过云服务客户数据的内存和磁盘，一旦释放和回收，其上的残留信息将被自动进行零值覆盖，对于重用或报废的物理设备，对存储介质进行覆写、消磁或折弯等数据清除处理。');
INSERT INTO rule_inspection_report VALUES (60, '应保证存有敏感数据的存储空间被释放或重新分配前得到完全清除。', '安全计算环境', '剩余信息保护', '云平台支持不同云服务客户内存和持久化存储空间相对独立，当资源释放时，云服务客户空间会被释放和清除。');
INSERT INTO rule_inspection_report VALUES (61, '应仅采集和保存业务必需的用户个人信息。', '安全计算环境', '个人信息保护', '您需根据部署的应用系统功能建设相应的个人信息清除机制。\n部署敏感数据保护实现对个人信息进行发现、分类和保护。');
INSERT INTO rule_inspection_report VALUES (62, '应禁止未授权访问和非法使用用户个人信息。', '安全计算环境', '个人信息保护', '由您根据部署的应用系统功能建设相应的个人信息保护机制。');
INSERT INTO rule_inspection_report VALUES (63, '应对系统管理员进行身份鉴别，只允许其通过特定的命令或操作界面进行系统管理操作，并对这些操作进行审计。', '安全管理中心', '系统管理', '您需基于用户角色进行配置，实现用户三权分立。');
INSERT INTO rule_inspection_report VALUES (64, '应通过系统管理员对系统的资源和运行进行配置、控制和管理，包括用户身份、系统资源配置、系统加载和启动、系统运行的异常处理、数据和设备的备份与恢复等。', '安全管理中心', '系统管理', '您需基于云产品配置指南根据业务需求合理配置系统资源。\n部署云安全中心 实现对系统异常进行处理。');
INSERT INTO rule_inspection_report VALUES (65, '应对审计管理员进行身份鉴别，只允许其通过特定的命令或操作界面进行安全审计操作，并对这些操作进行审计。', '安全管理中心', '审计管理', '您需基于用户角色进行配置，实现用户三权分立。\n部署堡垒机 实现对审计管理操作进行审计。\n部署数据库审计产品实现对数据库操作进行审计。');
INSERT INTO rule_inspection_report VALUES (66, '应通过审计管理员对审计记录应进行分析，并根据分析结果进行处理，包括根据安全审计策略对审计记录进行存储、管理和查询等。', '安全管理中心', '审计管理', '您需基于云产品配置指南根据业务需求合理配置。\n部署堡垒机 实现对审计记录进行查询、分析和管理，审计记录支持转存到日志服务中。\n部署数据库审计产品 实现对审计记录进行查询、分析和管理，审计记录存储在日志服务中。');
INSERT INTO rule_inspection_report VALUES (67, '应对安全管理员进行身份鉴别，只允许其通过特定的命令或操作界面进行安全管理操作，并对这些操作进行审计。', '安全管理中心', '安全管理', '您需基于用户角色，实现用户三权分立。安全管理员基于安全控制台通过安全产品对云资源进行安全管理操作。');
INSERT INTO rule_inspection_report VALUES (68, '应通过安全管理员对系统中的安全策略进行配置，包括安全参数的设置，主体、客体进行统一安全标记，对主体进行授权，配置可信验证策略等。', '安全管理中心', '安全管理', '您需基于用户角色，实现用户三权分立。\n部署云安全中心实现基线核查、安全策略、访问控制策略统一配置。');
INSERT INTO rule_inspection_report VALUES (69, '应划分出特定的管理区域，对分布在网络中的安全设备或安全组件进行管控。', '安全管理中心', '集中管控', '您需制定安全策略，划分特定的管理区域。');
INSERT INTO rule_inspection_report VALUES (70, '应能够建立一条安全的信息传输路径，对网络中的安全设备或安全组件进行管理。', '安全管理中心', '集中管控', '您需基于云产品配置指南根据业务需求合理配置。云平台支持全链路通信进行SSL/TLS安全加密处理，通过Https进行管理。');
INSERT INTO rule_inspection_report VALUES (71, '应对网络链路、安全设备、网络设备和服务器等的运行状况进行集中监测。', '安全管理中心', '集中管控', '您需基于云产品配置指南根据业务需求合理配置。云监控支持针对负载均衡、弹性IP地址、DDoS高防、云服务器等运行状态创建监测规则，并进行集中监测和报警。');
INSERT INTO rule_inspection_report VALUES (72, '应对分散在各个设备上的审计数据进行收集汇总和集中分析，并保证审计记录的留存时间符合法律法规要求。', '安全管理中心', '集中管控', '您需基于云产品配置指南根据业务需求合理配置。云安全产品支持将自身采集安全审计记录在安全控制台上展示，并支持将审计记录统一保存到云服务客户指定的日志服务或存储空间，日志留存时间满足6个月。');
INSERT INTO rule_inspection_report VALUES (73, '应对安全策略、恶意代码、补丁升级等安全相关事项进行集中管理。', '安全管理中心', '集中管控', '您需基于云产品配置指南根据业务需求合理配置。\n部署云安全中心实现云平台配置核查、漏洞检测和修复、基线核查、云安全补丁修复，并对安全相关事件进行集中管理。');
INSERT INTO rule_inspection_report VALUES (74, '应能对网络中发生的各类安全事件进行识别、报警和分析。', '安全管理中心', '集中管控', '您需基于云产品配置指南根据业务需求合理配置。\n部署云安全中心企业版实现自动采集计算、数据库、负载均衡等等多种资产，收集多种日志数据，对重点安全威胁时管控，对各类安全事件进行识别、分析和告警，告警方式包括短信、邮件、钉钉等。\n部署云防火墙实现对互联网上的恶意流量入侵活动和常规攻击行为进行实时阻断和拦截。\n部署Web应用防火墙实现将Web流量引流到WAF上，由WAF将流量进行检测、过滤、清洗后再代理转发到应用服务器。\n部署DDoS高防实现所有业务流量进行清洗，支持网络四层和七层防护。');
INSERT INTO rule_inspection_report VALUES (75, '应在虚拟化网络边界部署访问控制机制，并设置访问控制规则。', '安全区域边界', '访问控制', '您需配置恰当访问控制策略。\n部署云防火墙实现统一管理互联网到业务的南北向访问策略和业务与业务之间的东西向微隔离策略，访问控制粒度可达端口级。');
INSERT INTO rule_inspection_report VALUES (76, '应在不同等级的网络区域边界部署访问控制机制，设置访问控制规则。', '安全区域边界', '访问控制', '您需配置恰当访问控制策略。\n部署云防火墙实现对VPC边界防火墙、互联网边界防火墙以及主机边界防火墙的访问控制，用于检测和控制两个VPC间的通信流量、限制主机对内、外双向的未授权访问和ECS实例间的未授权访问。');
INSERT INTO rule_inspection_report VALUES (77, '应对云服务商和云服务客户在远程管理时执行的特权命令进行审计，至少包括虚拟机删除、虚拟机重启。', '安全区域边界', '安全审计', '您需基于云产品配置指南根据业务需求合理配置。\n部署堡垒机实现操作审计、职权管控、安全认证功能，记录所有运维操作记录、Linux命令审计、Windows操作录像。\n部署云安全中心实现云服务客户所有资产安全事件的综合分析。');
INSERT INTO rule_inspection_report VALUES (78, '应保证云服务商对云服务客户系统和数据的操作可被云服务客户审计。', '安全区域边界', '安全审计', '您需基于云产品配置指南根据业务需求合理配置。云服务商对云服务客户系统的操作需提交工单，通过云服务客户授权后进行操作，相关操作行为通过云服务客户的管理平台进行审计。');
INSERT INTO rule_inspection_report VALUES (79, '当远程管理云计算平台中设备时，管理终端和云计算平台之间应建立双向身份验证机制。', '安全计算环境', '身份鉴别', '您需基于云产品配置指南根据业务需求合理配置。可通过云平台RAM授权和AccessKey，实现密钥访问云平台API，如果双方均为合法证书则建立双向加密通道。');
INSERT INTO rule_inspection_report VALUES (80, '应确保云服务客户数据、用户个人信息等存储于中国境内，如需出境应遵循国家相关规定。', '安全计算环境', '数据完整性', '您需遵守业务数据不出境的规定，若出境遵循国家相关规定。');
INSERT INTO rule_inspection_report VALUES (81, '云服务客户应在本地保存其业务数据的备份。', '安全计算环境', '数据备份恢复', '您需进行本地备份业务数据。');
INSERT INTO rule_inspection_report VALUES (82, '应根据云服务商和云服务客户的职责划分，收集各自控制部分的审计数据并实现各自的集中审计。', '安全建设管理', '集中管控', '您需基于云产品配置指南根据业务需求合理配置。\n部署堡垒机实现对所有云服务客户的操作进行审计，操作系统自身日志本地保存。\n部署数据库审计产品实现对数据库风险操作行为进行记录，提供细粒度审计数据库访问行为。云客户安全能力。');
INSERT INTO rule_inspection_report VALUES (83, '应根据云服务商和云服务客户的职责划分，实现各自控制部分，包括虚拟化网络、虚拟机、虚拟化安全设备等的运行状况的集中监测。', '安全建设管理', '集中管控', '您需基于云产品配置指南根据业务需求合理配置。云监控支持针对负载均衡、弹性IP地址、DDoS高防、云服务器等运行状态创建监测规则，并进行集中监测和报警。');
INSERT INTO rule_inspection_report VALUES (84, '应选择安全合规的云服务商，其所提供的云计算平台应为其所承载的业务应用系统提供相应等级的安全保护能力。', '安全建设管理', '云服务商选择', '您可以根据业务需求选择合规的云服务商。云平台提供的公共云平台安全保护等级为第三级，并通过公安部指定测评机构等保测评。');
INSERT INTO rule_inspection_report VALUES (85, '应在服务水平协议中规定云服务的各项服务内容和具体技术指标。', '安全建设管理', '云服务商选择', '您在选定云服务商后，需签订相关服务等级协议。云平台各安全产品均提供服务等级协议。');
INSERT INTO rule_inspection_report VALUES (86, '应在服务水平协议中规定云服务商的权限与责任，包括管理范围、职责划分、访问授权、隐私保护、行为准则、违约责任等。', '安全建设管理', '云服务商选择', '您在选定云服务商后，需签订相关服务等级协议。云平台各安全产品均提供服务等级协议。');
INSERT INTO rule_inspection_report VALUES (87, '应在服务水平协议中规定服务合约到期时，完整提供云服务客户数据，并承诺相关数据在云计算平台上清除。', '安全建设管理', '云服务商选择', '您在选定云服务商后，需签订相关服务等级协议。云平台各安全产品均提供服务等级协议。');
INSERT INTO rule_inspection_report VALUES (88, '应与选定的云服务商签署保密协议，要求其不得泄露云服务客户数据。', '安全建设管理', '云服务商选择', '您在选定云服务商后，需签订相关服务等级协议。云平台各安全产品均提供服务等级协议。');
INSERT INTO rule_inspection_report VALUES (89, '应确保供应商的选择符合国家有关规定。', '安全建设管理', '供应链管理', '您在选择第三方安全产品接入时，应明确供应商是否满足国家相关规定。公共云平台基础设施供应商选择均按照国家相关规定，对供应商资质、诚信以及产品情况进行审核筛选，云平台提供云服务均按照国家相关规定对外售卖。');
INSERT INTO rule_inspection_report VALUES (90, '需要多因素身份验证。无论是现场管理还是由第三方提供商管理，都需要对所有系统上的所有用户账号进行多因素身份验证。', 'IAM', '访问控制', '检测RAM用户是否开通MFA二次验证登录。若未开通，会导致该规则不合规。开启多因素认证后，配置审计在10分钟内感知到您的修改并自动启动审计。');
INSERT INTO rule_inspection_report VALUES (91, '维护网络设备的标准安全配置。维护所有被授权网络设备的安全配置。该安全配置需符合标准且记录在案。', '安全通信网络', '网络架构', 'ECS不能为经典网络，必须挂载到VPC。');
INSERT INTO rule_inspection_report VALUES (92, '确保仅审核通过的端口、协议和服务正在运行。确保只有符合条件的网络端口、协议和服务在系统上运行。该端口、协议和服务经过业务需求验证，对系统进行监听。拒绝通过未经授权的端口进行通信。拒绝通过未经授权的TCP、UDP端口或应用程序通信进行通信，以确保只有经过授权的协议才能跨越组织每个网络边界进行通信。', '网络', '网络架构', '将授权对象为0.0.0.0/0的安全组入方向规则的授权策略调整为拒绝或者修改授权对象。');
INSERT INTO rule_inspection_report VALUES (93, '确保安全组配置了最小粒度的规则。', '安全通信网络', '网络架构', '删除授权策略为允许，授权对象为0.0.0.0/0安全组入方向规则。');
INSERT INTO rule_inspection_report VALUES (94, '加密静态敏感信息。使用工具加密所有静态敏感信息。该工具采用未集成到操作系统中的辅助身份验证机制才能访问信息。', '安全计算环境', '安全审计', '如果您账号下所有处于关联状态的云盘若未加密，则会导致该规则不合规。');
INSERT INTO rule_inspection_report VALUES (95, '确保仅审核通过的端口、协议和服务正在运行。确保只有符合条件的网络端口、协议和服务在系统上运行。该端口、协议和服务经过业务需求验证，对系统进行监听。', '安全计算环境', '安全审计', '将授权对象为0.0.0.0/0的安全组入方向规则的授权策略调整为拒绝或者修改授权对象。');
INSERT INTO rule_inspection_report VALUES (96, '确保服务器端加密设置为“用服务密钥加密”。', '安全计算环境', '安全数据保护', '查看您账号下的资源是否启用了加密，若未加密，会导致该规则不合规。');
INSERT INTO rule_inspection_report VALUES (97, '确保RDS实例不对外开放。', '安全计算环境', '安全数据保护', '查看您账号下的RDS实例是否对外开放，若已开放，会导致该规则不合规。');


INSERT INTO rule VALUES ('028b8362-08f2-404c-8e15-935426bb8545', 'Aliyun RDS实例公网访问扫描', 1, 'HighRisk', 'Aliyun  检测您账号下RDS实例不允许任意来源公网访问，视为“合规”，否则视为“不合规”', 'policies:\n    # 检测您账号下RDS实例不允许任意来源公网访问，视为“合规”，否则视为“不合规”。\n    - name: aliyun-rds-internet-access\n      resource: aliyun.rds\n      filters:\n        - type: internet-access\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"启用公网访问\",\"defaultValue\":\"true\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('083d24e2-881f-488b-b120-8f2cd961707f', 'Aliyun SecurityGroup安全组配置扫描', 1, 'HighRisk', 'Aliyun  账号下ECS安全组配置不为“0.0.0.0/0”，视为“合规”，否则属于“不合规”', 'policies:\n    # 账号下ECS安全组配置不为“0.0.0.0/0”，视为“合规”。\n    - name: aliyun-sg-source-cidr-ip\n      resource: aliyun.security-group\n      filters:\n        - type: source-cidr-ip\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"目标IP地址段\",\"defaultValue\":\"\\\"0.0.0.0/0\\\"\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('0b2ece35-a17e-4584-ac2d-0b11483d04fb', 'Aliyun EIP带宽峰值扫描', 1, 'HighRisk', 'Aliyun  检测您账号下的弹性IP实例是否达到最低带宽要求，是视为“合规”，否则视为“不合规”', 'policies:\n    # 检测您账号下的弹性IP实例是否达到最低带宽要求。\n    - name: aliyun-eip-bandwidth\n      resource: aliyun.eip\n      filters:\n        - type: bandwidth\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"按带宽计费的公网型实例的带宽峰值\",\"defaultValue\":\"10\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('1299a29b-e19d-4186-93fd-a18ed1b2584a', 'Aliyun SLB负载均衡HTTPS监听扫描', 1, 'HighRisk', 'Aliyun  SLB负载均衡开启HTTPS监听，视为“合规”，否则属于“不合规”', 'policies:\n    # 负载均衡开启HTTPS监听，视为“合规”。\n    - name: aliyun-slb-listener\n      resource: aliyun.slb\n      filters:\n        - type: listener\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"负载均衡实例前端使用的协议\",\"defaultValue\":\"https\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('2533542d-5422-4bd5-8849-6a69ec05a874', 'Aliyun Disk磁盘加密状态扫描', 1, 'HighRisk', 'Aliyun 账号下所有的磁盘均已加密；若您配置阈值，则磁盘加密的Id需存在您列出的阈值中，视为“合规”，否则视为“不合规”', 'policies:\n    # 账号下所有的磁盘是否加密。\n    - name: aliyun-encrypted-disk\n      resource: aliyun.disk\n      filters:\n        - type: encrypted\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"磁盘是否加密( true/false )\",\"defaultValue\":\"false\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('29763f5e-ef4c-431d-b44f-39cd1b5b5363', 'Aliyun Redis实例网络类型扫描', 1, 'HighRisk', 'Aliyun  检测您账号下的Redis实例是否运行在VPC网络环境下，是视为“合规”，否则视为“不合规”', 'policies:\n    # 检测您账号下的Redis实例是否运行在VPC网络环境下。\n    - name: aliyun-rds-network-type\n      resource: aliyun.redis\n      filters:\n        - type: network-type\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"实例的网络类型:CLASSIC经典网络/VPC网络\",\"defaultValue\":\"VPC\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('2adbae64-6403-4dfb-92ab-637354da49f8', 'Aliyun PolarDB实例公网访问扫描', 1, 'HighRisk', 'Aliyun  账号下PolarDB实例不允许任意来源公网访问，视为“合规”，否则视为“不合规”', 'policies:\n    # 账号下Polar实例不允许任意来源公网访问，视为“合规”。\n    - name: aliyun-polardb-internet-access\n      resource: aliyun.polardb\n      filters:\n        - type: internet-access\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"公网访问\",\"defaultValue\":\"true\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('339cf3fc-f9d9-457e-ac72-40d37c402bdf', 'Aliyun SLB负载均衡实例公网IP扫描', 1, 'HighRisk', 'Aliyun  SLB负载均衡实例未直接绑定公网IP，视为“合规”。该规则仅适用于 IPv4 协议', 'policies:\n    # 负载均衡实例未直接绑定公网IP，视为“合规”。该规则仅适用于 IPv4 协议。\n    - name: aliyun-slb-address-type\n      resource: aliyun.slb\n      filters:\n        - type: address-type\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"公网IP/内网IP( internet/intranet )\",\"defaultValue\":\"internet\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('3e5d47ac-86b6-40d1-a191-1b2ff2496118', 'Aliyun ECS实例网络类型扫描', 1, 'HighRisk', 'Aliyun  账号下所有ECS实例已关联到VPC；若您配置阈值，则关联的VpcId需存在您列出的阈值中，视为“合规”，否则视为“不合规”', 'policies:\n    # 账号下所有ECS实例已关联到VPC；若您配置阈值，则关联的VpcId需存在您列出的阈值中，视为“合规”。\n    - name: aliyun-ecs-instance-network-type\n      resource: aliyun.ecs\n      filters:\n        - type: instance-network-type\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"实例的网络类型:classic经典网络/vpc网络\",\"defaultValue\":\"vpc\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('429f8396-e04b-49d9-8b38-80647ac87e66', 'Aliyun OSS公开读取访问权限扫描', 1, 'HighRisk', 'Aliyun  查看您的OSS存储桶是否不允许公开读取访问权限。如果某个OSS存储桶策略或存储桶 ACL 允许公开读取访问权限，则该存储桶不合规', 'policies:\n    # 查看您的OSS存储桶是否不允许公开读取访问权限。\n    - name: aliyun-oss-public-read\n      resource: aliyun.oss\n      filters:\n        - type: global-grants\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"公开读取访问权限\",\"defaultValue\":\"public-read\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('44343a84-39e2-4fbc-b8c5-d3ac06186501', 'Aliyun MongoDB实例网络类型扫描', 1, 'HighRisk', 'Aliyun  检测您账号下的MongoDB实例是否运行在VPC网络环境下，是视为“合规”，否则视为“不合规”', 'policies:\n    # 检测您账号下的MongoDB实例是否运行在VPC网络环境下。\n    - name: aliyun-mongodb-network-type\n      resource: aliyun.mongodb\n      filters:\n        - type: network-type\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"实例的网络类型:Classic经典网络/VPC网络\",\"defaultValue\":\"VPC\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('594e7673-c0db-40a4-9a0c-f70f0e58cc62', 'Aliyun SLB带宽峰值扫描', 1, 'HighRisk', 'Aliyun  检测您账号下的负载均衡实例是否达到最低带宽要求，是视为“合规”，否则视为“不合规”', 'policies:\n    # 检测您账号下的负载均衡实例是否达到最低带宽要求。\n    - name: aliyun-slb-bandwidth\n      resource: aliyun.slb\n      filters:\n        - type: bandwidth\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"按带宽计费的公网型实例的带宽峰值\",\"defaultValue\":\"10\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('6fd132c0-b4df-4685-b132-5441d1aef2f8', 'Aliyun ECS实例公网IP扫描', 1, 'HighRisk', 'Aliyun ECS实例未直接绑定公网IP，视为“合规”，否则属于“不合规”。该规则仅适用于 IPv4 协议', 'policies:\n    # ECS实例未直接绑定公网IP，视为“合规”。该规则仅适用于 IPv4 协议。\n    - name: aliyun-ecs-public-ipaddress\n      resource: aliyun.ecs\n      filters:\n        - type: public-ip-address', '[]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('70c1e701-b87a-4e4d-8648-3db7ecc2c066', 'Aliyun RAM用户MFA扫描', 1, 'HighRisk', 'Aliyun 检测RAM用户是否开通MFA二次验证登录，开通视为“合规”，否则属于“不合规”', 'policies:\n    # 检测RAM用户是否开通MFA二次验证登录。\n    - name: aliyun-ram-mfa\n      resource: aliyun.ram\n      filters:\n        - type: mfa\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"RAM用户是否开通MFA二次验证登录(true/false)\",\"defaultValue\":\"false\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('7d323895-f07b-4845-8b3d-01c78180f270', 'Aliyun MongoDB实例公网访问扫描', 1, 'HighRisk', 'Aliyun  账号下MongoDB实例不允许任意来源公网访问，视为“合规”，否则视为“不合规”', 'policies:\n    # 账号下MongoDB实例不允许任意来源公网访问，视为“合规”。\n    - name: aliyun-mongodb-internet-access\n      resource: aliyun.mongodb\n      filters:\n        - type: internet-access\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"公网访问\",\"defaultValue\":\"true\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('88a77028-0e2a-4201-a713-ded3a94864f9', 'Aliyun SLB公网访问扫描', 1, 'HighRisk', 'Aliyun  账号下SLB不允许任意来源公网访问，视为“合规”，否则视为“不合规”', 'policies:\n    # 账号下SLB不允许任意来源公网访问，视为“合规”。\n    - name: aliyun-slb-acls\n      resource: aliyun.slb\n      filters:\n        - type: acls\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"公网访问\",\"defaultValue\":\"true\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('8c635fda-7f89-4d5c-b0f4-2116f1b65554', 'Aliyun OSS防盗链扫描', 1, 'HighRisk', 'Aliyun  检测OSS Bucket是否开启防盗链开关，已开通视为“合规”，否则视为“不合规”', 'policies:\n    # 检测OSS Bucket是否开启防盗链开关，已开通视为“合规”。\n    - name: aliyun-oss-bucket-referer\n      resource: aliyun.oss\n      filters:\n        - type: bucket-referer\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"是否开启防盗链开关\",\"defaultValue\":\"true\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('9d94781e-922d-48c3-90a1-393dc79f2442', 'Aliyun OSS存储桶加密扫描', 1, 'HighRisk', 'Aliyun  查看并确认您的OSS存储桶启用了默认加密，未开启则该存储桶不合规', 'policies:\n    # 查看并确认您的OSS存储桶是否启用了默认加密。\n    - name: aliyun-oss-encryption\n      resource: aliyun.oss\n      filters:\n        - type: encryption', '[]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('ae65e90c-124c-4a81-8081-746d47f44e8f', 'Aliyun RDS实例高可用状态扫描', 1, 'HighRisk', 'Aliyun 账号下RDS实例具备高可用能力，视为“合规”，否则属于“不合规”', 'policies:\n    # 账号下RDS实例具备高可用能力，视为“合规”，否则属于“不合规”。\n    - name: aliyun-rds-high-availability\n      resource: aliyun.rds\n      filters:\n        - type: high-availability', '[]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('ba1edc8f-0944-4ebb-a953-f655aa710e84', 'Aliyun CDN域名HTTPS监听扫描', 1, 'HighRisk', 'Aliyun  检测CDN域名是否开启HTTPS监听，若开启视为“合规”，否则视为“不合规”', 'policies:\n    # 检测CDN域名是否开启HTTPS监听，若开启视为“合规”。\n    - name: aliyun-cdn-ssl-protocol\n      resource: aliyun.cdn\n      filters:\n        - type: ssl-protocol\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"https开关。 \\\"on\\\"：已开启。\\\"off\\\"：未开启\",\"defaultValue\":\"\\\"off\\\"\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('beda16d0-93fd-4366-9ebf-f5ce1360cd60', 'Aliyun RDS实例多可用区扫描', 1, 'HighRisk', 'Aliyun 账号下RDS实例支持多可用区，视为“合规”，否则视为“不合规”', 'policies:\n    # 账号下RDS实例支持多可用区，视为“合规”。\n    - name: aliyun-rds-available-zones\n      resource: aliyun.rds\n      filters:\n        - type: available-zones\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"是否支持多可用区\",\"defaultValue\":\"false\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('c57f055e-fd84-4af3-ba97-892a8fdc1fed', 'Aliyun PolarDB实例网络类型扫描', 1, 'HighRisk', 'Aliyun  检测您账号下的PolarDB实例是否运行在VPC网络环境下，是视为“合规”，否则视为“不合规”', 'policies:\n    # 检测您账号下的polardb实例是否运行在vpc网络环境下。\n    - name: aliyun-polardb-dbcluster-network-type\n      resource: aliyun.polardb\n      filters:\n        - type: dbcluster-network-type\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"实例的网络类型:Classic经典网络/VPC网络\",\"defaultValue\":\"VPC\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('c95fde94-53a5-4658-98a4-56a0c6d951d4', 'Aliyun RDS实例的访问模式扫描', 1, 'HighRisk', 'Aliyun  检测您账号下的RDS实例是否启用数据库代理状态链接形式，Safe视为“合规”，Standard属于“不合规”', 'policies:\n    # 检测您账号下的RDS实例是否启用数据库代理状态链接形式，Safe视为“合规”，Standard属于“不合规”。\n    - name: aliyun-rds-connection-mode\n      resource: aliyun.rds\n      filters:\n       - type: connection-mode\n         value: ${{value}}', '[{\"key\":\"value\",\"name\":\"实例的访问模式:Standard/Safe\",\"defaultValue\":\"Safe\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('d690be79-2e8c-4054-bbe6-496bd29e91fe', 'Aliyun RDS实例白名单扫描', 1, 'HighRisk', 'Aliyun  测您账号下RDS数据库实例是否启用安全白名单功能，已开通视为“合规”，否则视为“不合规”', 'policies:\n    #检测您账号下RDS数据库实例是否启用安全白名单功能，已开通视为“合规”。\n    - name: aliyun-rds-security-ip-mode\n      resource: aliyun.rds\n      filters:\n        - type: security-ip-mode\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"白名单模式:normal通用模式/safety高安全模式\",\"defaultValue\":\"safety\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('d826c85d-cb42-4824-ab13-6d7a8026d9ae', 'Aliyun OSS公开写入访问权限扫描', 1, 'HighRisk', 'Aliyun  查看OSS存储桶是否不允许公开写入访问权限。如果某个OSS存储桶策略或存储桶 ACL 允许公开写入访问权限，则该存储桶不合规', 'policies:\n    # 查看OSS存储桶是否不允许公开写入访问权限。\n    - name: aliyun-oss-public-read-write\n      resource: aliyun.oss\n      filters:\n        - type: global-grants\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"公开写入访问权限\",\"defaultValue\":\"public-read-write\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('df4fb45c-f9bc-4c8e-996d-036c9d2f1800', 'Aliyun SecurityGroup高危安全组扫描', 1, 'HighRisk', 'Aliyun  检测安全组是否开启风险端口，未开启视为“合规”，否则属于“不合规”', 'policies:\n  #扫描开放以下高危端口的安全组：\n  #(20,21,22,25,80,773,765,1733,1737,3306,3389,7333,5732,5500)\n  - name: aliyun-security-group\n    resource: aliyun.security-group\n    description: |\n      Add Filter all security groups, filter ports\n      [20,21,22,25,80,773,765,1733,1737,3306,3389,7333,5732,5500]\n      on 0.0.0.0/0 or\n      [20,21,22,25,80,773,765, 1733,1737,3306,3389,7333,5732,5500]\n      on ::/0 (IPv6)\n    filters:\n        - or:\n            - type: ingress\n              IpProtocol: \"-1\"\n              Ports: ${{ipv4_port}}\n              Cidr: \"0.0.0.0/0\"\n            - type: ingress\n              IpProtocol: \"-1\"\n              Ports: ${{ipv6_port}}\n              Cidr: \"::/0\"', '[{\"key\":\"ipv4_port\",\"name\":\"ipv4端口\",\"defaultValue\":\"[20,21,22,25,80,773,765, 1733,1737,3306,3389,7333,5732,5500]\",\"required\":true},{\"key\":\"ipv6_port\",\"name\":\"ipv6端口\",\"defaultValue\":\"[20,21,22,25,80,773,765, 1733,1737,3306,3389,7333,5732,5500]\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('e054787c-5826-4242-8450-b0daa926ea40', 'Aliyun RDS实例网络类型扫描', 1, 'HighRisk', 'Aliyun  账号下RDS实例已关联到VPC；若您配置阈值，则关联的VpcId需存在您列出的阈值中，视为“合规”，否则视为“不合规”', 'policies:\n    # 账号下RDS实例已关联到VPC；若您配置阈值，则关联的VpcId需存在您列出的阈值中，视为“合规”。\n    - name: aliyun-rds-instance-network-type\n      resource: aliyun.rds\n      filters:\n        - type: instance-network-type\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"实例的网络类型:Classic经典网络/VPC网络\",\"defaultValue\":\"VPC\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('e2d51fc6-2ec2-4d17-bf87-13a3df90ea5d', 'Aliyun SLB负载均衡实例关联到VPC扫描', 1, 'HighRisk', 'Aliyun  账号下负载均衡实例已关联到VPC；若您配置阈值，则关联的VpcId需存在您列出的阈值中，视为“合规”，否则视为“不合规”', 'policies:\n    # 账号下负载均衡实例已关联到VPC；若您配置阈值，则关联的VpcId需存在您列出的阈值中，视为“合规”。\n    - name: aliyun-slb-network-type\n      resource: aliyun.slb\n      filters:\n        - type: network-type\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"负载均衡实例的网络类型(不等于)\",\"defaultValue\":\"vpc\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('fdef013f-ce14-468a-9af4-1c0fabc7e6e1', 'Aliyun OSS对象存储桶冗余存储扫描', 1, 'HighRisk', 'Aliyun  检测您账号下的对象存储桶是否启用同城冗余存储（数据容灾类型），若开启视为“合规”，否则视为”不合规”', 'policies:\n    # 检测您账号下的对象存储桶是否启用同城冗余存储，若开启视为“合规”。\n    - name: aliyun-oss-data-redundancy-type\n      resource: aliyun.oss\n      filters:\n        - type: data-redundancy-type\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"Bucket的数据容灾类型。有效值：LRS、ZRS\",\"defaultValue\":\"ZRS\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');
INSERT INTO rule VALUES ('ff153eea-2628-440b-b054-186d6f5a7708', 'Aliyun Redis实例公网访问扫描', 1, 'HighRisk', 'Aliyun  账号下Redis实例不允许任意来源公网访问，视为“合规”，否则视为“不合规”', 'policies:\n    # 账号下Redis实例不允许任意来源公网访问，视为“合规”。\n    - name: aliyun-redis-internet-access\n      resource: aliyun.redis\n      filters:\n        - type: internet-access\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"公网访问\",\"defaultValue\":\"true\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), 1, 'custodian');


INSERT INTO rule_tag_mapping VALUES (1, 'd690be79-2e8c-4054-bbe6-496bd29e91fe', 'safety');
INSERT INTO rule_tag_mapping VALUES (2, '6fd132c0-b4df-4685-b132-5441d1aef2f8', 'safety');
INSERT INTO rule_tag_mapping VALUES (3, 'c95fde94-53a5-4658-98a4-56a0c6d951d4', 'safety');
INSERT INTO rule_tag_mapping VALUES (4, 'fdef013f-ce14-468a-9af4-1c0fabc7e6e1', 'safety');
INSERT INTO rule_tag_mapping VALUES (5, '8c635fda-7f89-4d5c-b0f4-2116f1b65554', 'safety');
INSERT INTO rule_tag_mapping VALUES (6, '0b2ece35-a17e-4584-ac2d-0b11483d04fb', 'safety');
INSERT INTO rule_tag_mapping VALUES (7, '594e7673-c0db-40a4-9a0c-f70f0e58cc62', 'safety');
INSERT INTO rule_tag_mapping VALUES (8, '44343a84-39e2-4fbc-b8c5-d3ac06186501', 'safety');
INSERT INTO rule_tag_mapping VALUES (9, '29763f5e-ef4c-431d-b44f-39cd1b5b5363', 'safety');
INSERT INTO rule_tag_mapping VALUES (10, 'e054787c-5826-4242-8450-b0daa926ea40', 'safety');
INSERT INTO rule_tag_mapping VALUES (11, 'e2d51fc6-2ec2-4d17-bf87-13a3df90ea5d', 'safety');
INSERT INTO rule_tag_mapping VALUES (12, 'c57f055e-fd84-4af3-ba97-892a8fdc1fed', 'safety');
INSERT INTO rule_tag_mapping VALUES (13, 'ff153eea-2628-440b-b054-186d6f5a7708', 'safety');
INSERT INTO rule_tag_mapping VALUES (14, '88a77028-0e2a-4201-a713-ded3a94864f9', 'safety');
INSERT INTO rule_tag_mapping VALUES (15, '7d323895-f07b-4845-8b3d-01c78180f270', 'safety');
INSERT INTO rule_tag_mapping VALUES (16, '339cf3fc-f9d9-457e-ac72-40d37c402bdf', 'safety');
INSERT INTO rule_tag_mapping VALUES (17, 'd826c85d-cb42-4824-ab13-6d7a8026d9ae', 'safety');
INSERT INTO rule_tag_mapping VALUES (18, '429f8396-e04b-49d9-8b38-80647ac87e66', 'safety');
INSERT INTO rule_tag_mapping VALUES (19, 'ba1edc8f-0944-4ebb-a953-f655aa710e84', 'safety');
INSERT INTO rule_tag_mapping VALUES (20, '1299a29b-e19d-4186-93fd-a18ed1b2584a', 'safety');
INSERT INTO rule_tag_mapping VALUES (21, 'ae65e90c-124c-4a81-8081-746d47f44e8f', 'safety');
INSERT INTO rule_tag_mapping VALUES (22, '70c1e701-b87a-4e4d-8648-3db7ecc2c066', 'safety');
INSERT INTO rule_tag_mapping VALUES (23, '3e5d47ac-86b6-40d1-a191-1b2ff2496118', 'safety');
INSERT INTO rule_tag_mapping VALUES (24, '2533542d-5422-4bd5-8849-6a69ec05a874', 'safety');
INSERT INTO rule_tag_mapping VALUES (25, '083d24e2-881f-488b-b120-8f2cd961707f', 'safety');
INSERT INTO rule_tag_mapping VALUES (26, '9d94781e-922d-48c3-90a1-393dc79f2442', 'safety');
INSERT INTO rule_tag_mapping VALUES (27, '028b8362-08f2-404c-8e15-935426bb8545', 'safety');
INSERT INTO rule_tag_mapping VALUES (28, 'df4fb45c-f9bc-4c8e-996d-036c9d2f1800', 'safety');
INSERT INTO rule_tag_mapping VALUES (29, 'beda16d0-93fd-4366-9ebf-f5ce1360cd60', 'safety');
INSERT INTO rule_tag_mapping VALUES (30, '2adbae64-6403-4dfb-92ab-637354da49f8', 'safety');


INSERT INTO rule_inspection_report_mapping VALUES (1, '0b2ece35-a17e-4584-ac2d-0b11483d04fb', '2');
INSERT INTO rule_inspection_report_mapping VALUES (2, '594e7673-c0db-40a4-9a0c-f70f0e58cc62', '2');
INSERT INTO rule_inspection_report_mapping VALUES (3, '44343a84-39e2-4fbc-b8c5-d3ac06186501', '3');
INSERT INTO rule_inspection_report_mapping VALUES (4, '44343a84-39e2-4fbc-b8c5-d3ac06186501', '4');
INSERT INTO rule_inspection_report_mapping VALUES (5, '44343a84-39e2-4fbc-b8c5-d3ac06186501', '5');
INSERT INTO rule_inspection_report_mapping VALUES (6, '44343a84-39e2-4fbc-b8c5-d3ac06186501', '6');
INSERT INTO rule_inspection_report_mapping VALUES (7, '44343a84-39e2-4fbc-b8c5-d3ac06186501', '7');
INSERT INTO rule_inspection_report_mapping VALUES (8, '29763f5e-ef4c-431d-b44f-39cd1b5b5363', '3');
INSERT INTO rule_inspection_report_mapping VALUES (9, '29763f5e-ef4c-431d-b44f-39cd1b5b5363', '4');
INSERT INTO rule_inspection_report_mapping VALUES (10, '29763f5e-ef4c-431d-b44f-39cd1b5b5363', '5');
INSERT INTO rule_inspection_report_mapping VALUES (11, '29763f5e-ef4c-431d-b44f-39cd1b5b5363', '6');
INSERT INTO rule_inspection_report_mapping VALUES (12, '29763f5e-ef4c-431d-b44f-39cd1b5b5363', '7');
INSERT INTO rule_inspection_report_mapping VALUES (13, 'e054787c-5826-4242-8450-b0daa926ea40', '3');
INSERT INTO rule_inspection_report_mapping VALUES (14, 'e054787c-5826-4242-8450-b0daa926ea40', '4');
INSERT INTO rule_inspection_report_mapping VALUES (15, 'e054787c-5826-4242-8450-b0daa926ea40', '5');
INSERT INTO rule_inspection_report_mapping VALUES (16, 'e054787c-5826-4242-8450-b0daa926ea40', '6');
INSERT INTO rule_inspection_report_mapping VALUES (17, 'e054787c-5826-4242-8450-b0daa926ea40', '7');
INSERT INTO rule_inspection_report_mapping VALUES (18, 'e2d51fc6-2ec2-4d17-bf87-13a3df90ea5d', '3');
INSERT INTO rule_inspection_report_mapping VALUES (19, 'e2d51fc6-2ec2-4d17-bf87-13a3df90ea5d', '4');
INSERT INTO rule_inspection_report_mapping VALUES (20, 'e2d51fc6-2ec2-4d17-bf87-13a3df90ea5d', '5');
INSERT INTO rule_inspection_report_mapping VALUES (21, 'e2d51fc6-2ec2-4d17-bf87-13a3df90ea5d', '6');
INSERT INTO rule_inspection_report_mapping VALUES (22, 'e2d51fc6-2ec2-4d17-bf87-13a3df90ea5d', '7');
INSERT INTO rule_inspection_report_mapping VALUES (23, 'c57f055e-fd84-4af3-ba97-892a8fdc1fed', '3');
INSERT INTO rule_inspection_report_mapping VALUES (24, 'c57f055e-fd84-4af3-ba97-892a8fdc1fed', '4');
INSERT INTO rule_inspection_report_mapping VALUES (25, 'c57f055e-fd84-4af3-ba97-892a8fdc1fed', '5');
INSERT INTO rule_inspection_report_mapping VALUES (26, 'c57f055e-fd84-4af3-ba97-892a8fdc1fed', '6');
INSERT INTO rule_inspection_report_mapping VALUES (27, 'c57f055e-fd84-4af3-ba97-892a8fdc1fed', '7');
INSERT INTO rule_inspection_report_mapping VALUES (28, 'ff153eea-2628-440b-b054-186d6f5a7708', '10');
INSERT INTO rule_inspection_report_mapping VALUES (29, 'ff153eea-2628-440b-b054-186d6f5a7708', '13');
INSERT INTO rule_inspection_report_mapping VALUES (30, '88a77028-0e2a-4201-a713-ded3a94864f9', '10');
INSERT INTO rule_inspection_report_mapping VALUES (31, '88a77028-0e2a-4201-a713-ded3a94864f9', '13');
INSERT INTO rule_inspection_report_mapping VALUES (32, '7d323895-f07b-4845-8b3d-01c78180f270', '10');
INSERT INTO rule_inspection_report_mapping VALUES (33, '7d323895-f07b-4845-8b3d-01c78180f270', '13');
INSERT INTO rule_inspection_report_mapping VALUES (34, '339cf3fc-f9d9-457e-ac72-40d37c402bdf', '10');
INSERT INTO rule_inspection_report_mapping VALUES (35, '339cf3fc-f9d9-457e-ac72-40d37c402bdf', '13');
INSERT INTO rule_inspection_report_mapping VALUES (36, 'd826c85d-cb42-4824-ab13-6d7a8026d9ae', '10');
INSERT INTO rule_inspection_report_mapping VALUES (37, 'd826c85d-cb42-4824-ab13-6d7a8026d9ae', '13');
INSERT INTO rule_inspection_report_mapping VALUES (38, '429f8396-e04b-49d9-8b38-80647ac87e66', '10');
INSERT INTO rule_inspection_report_mapping VALUES (39, '429f8396-e04b-49d9-8b38-80647ac87e66', '13');
INSERT INTO rule_inspection_report_mapping VALUES (40, 'ba1edc8f-0944-4ebb-a953-f655aa710e84', '52');
INSERT INTO rule_inspection_report_mapping VALUES (41, '1299a29b-e19d-4186-93fd-a18ed1b2584a', '52');
INSERT INTO rule_inspection_report_mapping VALUES (42, 'ae65e90c-124c-4a81-8081-746d47f44e8f', '2');
INSERT INTO rule_inspection_report_mapping VALUES (43, 'ae65e90c-124c-4a81-8081-746d47f44e8f', '56');
INSERT INTO rule_inspection_report_mapping VALUES (44, 'ae65e90c-124c-4a81-8081-746d47f44e8f', '57');
INSERT INTO rule_inspection_report_mapping VALUES (45, 'ae65e90c-124c-4a81-8081-746d47f44e8f', '58');
INSERT INTO rule_inspection_report_mapping VALUES (46, '70c1e701-b87a-4e4d-8648-3db7ecc2c066', '32');
INSERT INTO rule_inspection_report_mapping VALUES (47, '70c1e701-b87a-4e4d-8648-3db7ecc2c066', '90');
INSERT INTO rule_inspection_report_mapping VALUES (48, '3e5d47ac-86b6-40d1-a191-1b2ff2496118', '3');
INSERT INTO rule_inspection_report_mapping VALUES (49, '3e5d47ac-86b6-40d1-a191-1b2ff2496118', '4');
INSERT INTO rule_inspection_report_mapping VALUES (50, '3e5d47ac-86b6-40d1-a191-1b2ff2496118', '5');
INSERT INTO rule_inspection_report_mapping VALUES (51, '3e5d47ac-86b6-40d1-a191-1b2ff2496118', '6');
INSERT INTO rule_inspection_report_mapping VALUES (52, '3e5d47ac-86b6-40d1-a191-1b2ff2496118', '7');
INSERT INTO rule_inspection_report_mapping VALUES (53, '3e5d47ac-86b6-40d1-a191-1b2ff2496118', '91');
INSERT INTO rule_inspection_report_mapping VALUES (54, '2533542d-5422-4bd5-8849-6a69ec05a874', '53');
INSERT INTO rule_inspection_report_mapping VALUES (55, '2533542d-5422-4bd5-8849-6a69ec05a874', '94');
INSERT INTO rule_inspection_report_mapping VALUES (56, '083d24e2-881f-488b-b120-8f2cd961707f', '10');
INSERT INTO rule_inspection_report_mapping VALUES (57, '083d24e2-881f-488b-b120-8f2cd961707f', '13');
INSERT INTO rule_inspection_report_mapping VALUES (58, '083d24e2-881f-488b-b120-8f2cd961707f', '92');
INSERT INTO rule_inspection_report_mapping VALUES (59, '083d24e2-881f-488b-b120-8f2cd961707f', '93');
INSERT INTO rule_inspection_report_mapping VALUES (60, '083d24e2-881f-488b-b120-8f2cd961707f', '95');
INSERT INTO rule_inspection_report_mapping VALUES (61, '9d94781e-922d-48c3-90a1-393dc79f2442', '53');
INSERT INTO rule_inspection_report_mapping VALUES (62, '9d94781e-922d-48c3-90a1-393dc79f2442', '55');
INSERT INTO rule_inspection_report_mapping VALUES (63, '9d94781e-922d-48c3-90a1-393dc79f2442', '96');
INSERT INTO rule_inspection_report_mapping VALUES (64, '028b8362-08f2-404c-8e15-935426bb8545', '97');
INSERT INTO rule_inspection_report_mapping VALUES (65, 'df4fb45c-f9bc-4c8e-996d-036c9d2f1800', '9');
INSERT INTO rule_inspection_report_mapping VALUES (66, 'df4fb45c-f9bc-4c8e-996d-036c9d2f1800', '46');
INSERT INTO rule_inspection_report_mapping VALUES (67, 'df4fb45c-f9bc-4c8e-996d-036c9d2f1800', '92');
INSERT INTO rule_inspection_report_mapping VALUES (68, 'df4fb45c-f9bc-4c8e-996d-036c9d2f1800', '93');
INSERT INTO rule_inspection_report_mapping VALUES (69, 'df4fb45c-f9bc-4c8e-996d-036c9d2f1800', '95');
INSERT INTO rule_inspection_report_mapping VALUES (70, '2adbae64-6403-4dfb-92ab-637354da49f8', '10');
INSERT INTO rule_inspection_report_mapping VALUES (71, '2adbae64-6403-4dfb-92ab-637354da49f8', '13');


INSERT INTO rule_group_mapping VALUES (1, 'd690be79-2e8c-4054-bbe6-496bd29e91fe', @groupId1);
INSERT INTO rule_group_mapping VALUES (2, '6fd132c0-b4df-4685-b132-5441d1aef2f8', @groupId1);
INSERT INTO rule_group_mapping VALUES (3, 'c95fde94-53a5-4658-98a4-56a0c6d951d4', @groupId1);
INSERT INTO rule_group_mapping VALUES (4, 'fdef013f-ce14-468a-9af4-1c0fabc7e6e1', @groupId1);
INSERT INTO rule_group_mapping VALUES (5, 'fdef013f-ce14-468a-9af4-1c0fabc7e6e1', @groupId3);
INSERT INTO rule_group_mapping VALUES (6, '8c635fda-7f89-4d5c-b0f4-2116f1b65554', @groupId1);
INSERT INTO rule_group_mapping VALUES (7, '8c635fda-7f89-4d5c-b0f4-2116f1b65554', @groupId3);
INSERT INTO rule_group_mapping VALUES (8, '0b2ece35-a17e-4584-ac2d-0b11483d04fb', @groupId1);
INSERT INTO rule_group_mapping VALUES (9, '594e7673-c0db-40a4-9a0c-f70f0e58cc62', @groupId1);
INSERT INTO rule_group_mapping VALUES (10, '44343a84-39e2-4fbc-b8c5-d3ac06186501', @groupId1);
INSERT INTO rule_group_mapping VALUES (11, '29763f5e-ef4c-431d-b44f-39cd1b5b5363', @groupId1);
INSERT INTO rule_group_mapping VALUES (12, 'e054787c-5826-4242-8450-b0daa926ea40', @groupId1);
INSERT INTO rule_group_mapping VALUES (13, 'e2d51fc6-2ec2-4d17-bf87-13a3df90ea5d', @groupId1);
INSERT INTO rule_group_mapping VALUES (14, 'e2d51fc6-2ec2-4d17-bf87-13a3df90ea5d', @groupId2);
INSERT INTO rule_group_mapping VALUES (15, 'c57f055e-fd84-4af3-ba97-892a8fdc1fed', @groupId1);
INSERT INTO rule_group_mapping VALUES (16, 'ff153eea-2628-440b-b054-186d6f5a7708', @groupId1);
INSERT INTO rule_group_mapping VALUES (17, '88a77028-0e2a-4201-a713-ded3a94864f9', @groupId1);
INSERT INTO rule_group_mapping VALUES (18, '88a77028-0e2a-4201-a713-ded3a94864f9', @groupId2);
INSERT INTO rule_group_mapping VALUES (19, '7d323895-f07b-4845-8b3d-01c78180f270', @groupId1);
INSERT INTO rule_group_mapping VALUES (20, '339cf3fc-f9d9-457e-ac72-40d37c402bdf', @groupId1);
INSERT INTO rule_group_mapping VALUES (21, '339cf3fc-f9d9-457e-ac72-40d37c402bdf', @groupId2);
INSERT INTO rule_group_mapping VALUES (22, 'd826c85d-cb42-4824-ab13-6d7a8026d9ae', @groupId1);
INSERT INTO rule_group_mapping VALUES (23, 'd826c85d-cb42-4824-ab13-6d7a8026d9ae', @groupId3);
INSERT INTO rule_group_mapping VALUES (24, '429f8396-e04b-49d9-8b38-80647ac87e66', @groupId1);
INSERT INTO rule_group_mapping VALUES (25, '429f8396-e04b-49d9-8b38-80647ac87e66', @groupId3);
INSERT INTO rule_group_mapping VALUES (26, 'ba1edc8f-0944-4ebb-a953-f655aa710e84', @groupId1);
INSERT INTO rule_group_mapping VALUES (27, '1299a29b-e19d-4186-93fd-a18ed1b2584a', @groupId1);
INSERT INTO rule_group_mapping VALUES (28, 'ae65e90c-124c-4a81-8081-746d47f44e8f', @groupId1);
INSERT INTO rule_group_mapping VALUES (29, 'ae65e90c-124c-4a81-8081-746d47f44e8f', @groupId2);
INSERT INTO rule_group_mapping VALUES (30, '70c1e701-b87a-4e4d-8648-3db7ecc2c066', @groupId1);
INSERT INTO rule_group_mapping VALUES (31, '70c1e701-b87a-4e4d-8648-3db7ecc2c066', @groupId2);
INSERT INTO rule_group_mapping VALUES (32, '3e5d47ac-86b6-40d1-a191-1b2ff2496118', @groupId1);
INSERT INTO rule_group_mapping VALUES (33, '2533542d-5422-4bd5-8849-6a69ec05a874', @groupId1);
INSERT INTO rule_group_mapping VALUES (34, '083d24e2-881f-488b-b120-8f2cd961707f', @groupId1);
INSERT INTO rule_group_mapping VALUES (35, '9d94781e-922d-48c3-90a1-393dc79f2442', @groupId1);
INSERT INTO rule_group_mapping VALUES (36, '9d94781e-922d-48c3-90a1-393dc79f2442', @groupId2);
INSERT INTO rule_group_mapping VALUES (37, '9d94781e-922d-48c3-90a1-393dc79f2442', @groupId3);
INSERT INTO rule_group_mapping VALUES (38, '028b8362-08f2-404c-8e15-935426bb8545', @groupId1);
INSERT INTO rule_group_mapping VALUES (39, 'df4fb45c-f9bc-4c8e-996d-036c9d2f1800', @groupId1);
INSERT INTO rule_group_mapping VALUES (40, 'df4fb45c-f9bc-4c8e-996d-036c9d2f1800', @groupId2);
INSERT INTO rule_group_mapping VALUES (41, 'beda16d0-93fd-4366-9ebf-f5ce1360cd60', @groupId1);
INSERT INTO rule_group_mapping VALUES (42, '2adbae64-6403-4dfb-92ab-637354da49f8', @groupId1);


INSERT INTO rule_type VALUES ('245bc538-2c33-430a-a61c-37000d058263', 'ff153eea-2628-440b-b054-186d6f5a7708', 'aliyun.redis');
INSERT INTO rule_type VALUES ('26b965d0-a16e-4d4a-b727-1816f07ce49a', 'c57f055e-fd84-4af3-ba97-892a8fdc1fed', 'aliyun.polardb');
INSERT INTO rule_type VALUES ('4155c0ce-6f20-43a2-9a1f-678ed0e2ee0f', '083d24e2-881f-488b-b120-8f2cd961707f', 'aliyun.security-group');
INSERT INTO rule_type VALUES ('4b3dd93c-6f3a-4bdb-afd7-6039d826b541', '3e5d47ac-86b6-40d1-a191-1b2ff2496118', 'aliyun.ecs');
INSERT INTO rule_type VALUES ('50e1f0a2-2c98-4af3-b458-847771cdf80c', 'e054787c-5826-4242-8450-b0daa926ea40', 'aliyun.rds');
INSERT INTO rule_type VALUES ('545f8a99-b613-4d6e-85da-1f48cc076cbf', 'beda16d0-93fd-4366-9ebf-f5ce1360cd60', 'aliyun.rds');
INSERT INTO rule_type VALUES ('63d01a83-df8c-4b3c-a244-12031698568f', '339cf3fc-f9d9-457e-ac72-40d37c402bdf', 'aliyun.slb');
INSERT INTO rule_type VALUES ('66f5bfd5-48f4-4454-9df0-3e6d856aa36f', '44343a84-39e2-4fbc-b8c5-d3ac06186501', 'aliyun.mongodb');
INSERT INTO rule_type VALUES ('75bd5124-f247-4cdc-a0d5-fdd9a573b1f3', '6fd132c0-b4df-4685-b132-5441d1aef2f8', 'aliyun.ecs');
INSERT INTO rule_type VALUES ('883d6aa5-a59d-4844-98fa-5a53d1acb6ef', 'd690be79-2e8c-4054-bbe6-496bd29e91fe', 'aliyun.rds');
INSERT INTO rule_type VALUES ('922e73cd-a782-4238-9381-277c0e54f9d7', '2adbae64-6403-4dfb-92ab-637354da49f8', 'aliyun.polardb');
INSERT INTO rule_type VALUES ('a1bbf517-80f6-4a6d-b043-3e2654b9efbb', '0b2ece35-a17e-4584-ac2d-0b11483d04fb', 'aliyun.eip');
INSERT INTO rule_type VALUES ('a71f9bc1-6c08-4618-bad8-5da9d4452968', '70c1e701-b87a-4e4d-8648-3db7ecc2c066', 'aliyun.ram');
INSERT INTO rule_type VALUES ('b79afedc-1880-4aac-8bbf-f4d05eaea1d3', 'df4fb45c-f9bc-4c8e-996d-036c9d2f1800', 'aliyun.security-group');
INSERT INTO rule_type VALUES ('bbd20d15-6e27-4a53-b67b-0890c0d0c452', '594e7673-c0db-40a4-9a0c-f70f0e58cc62', 'aliyun.slb');
INSERT INTO rule_type VALUES ('bd0f8305-661a-40f3-a4e0-ee457e6da76e', 'fdef013f-ce14-468a-9af4-1c0fabc7e6e1', 'aliyun.oss');


INSERT INTO rule_type VALUES ('bfc2ce96-e36f-4280-bdeb-9b88204aff0a', '2533542d-5422-4bd5-8849-6a69ec05a874', 'aliyun.disk');
INSERT INTO rule_type VALUES ('c36099af-2a2f-496f-8bf7-4435e3edd64e', '88a77028-0e2a-4201-a713-ded3a94864f9', 'aliyun.slb');
INSERT INTO rule_type VALUES ('c9a126eb-41e8-4e8c-b2dd-57b9c4df9a34', '29763f5e-ef4c-431d-b44f-39cd1b5b5363', 'aliyun.redis');
INSERT INTO rule_type VALUES ('cd140b9d-0d2b-4535-a13e-3887f80dc56a', 'ae65e90c-124c-4a81-8081-746d47f44e8f', 'aliyun.rds');
INSERT INTO rule_type VALUES ('e39bb334-d7ae-4fc5-9caa-e91ac5e594e0', '429f8396-e04b-49d9-8b38-80647ac87e66', 'aliyun.oss');
INSERT INTO rule_type VALUES ('e4f77952-ef02-4d6d-8771-502ef1343811', '9d94781e-922d-48c3-90a1-393dc79f2442', 'aliyun.oss');
INSERT INTO rule_type VALUES ('eb725846-fe34-4d48-9e13-5c727c268dfd', '028b8362-08f2-404c-8e15-935426bb8545', 'aliyun.rds');
INSERT INTO rule_type VALUES ('ebcac89f-9f95-4bbf-8843-3591232150d6', 'ba1edc8f-0944-4ebb-a953-f655aa710e84', 'aliyun.cdn');
INSERT INTO rule_type VALUES ('f08d6448-024f-4d03-bf4d-705c8c973704', 'd826c85d-cb42-4824-ab13-6d7a8026d9ae', 'aliyun.oss');
INSERT INTO rule_type VALUES ('f3686b01-3bf7-4e5b-b55d-14b607323bf0', '7d323895-f07b-4845-8b3d-01c78180f270', 'aliyun.mongodb');
INSERT INTO rule_type VALUES ('f5d5b91f-1530-42d2-b007-603184247f24', '8c635fda-7f89-4d5c-b0f4-2116f1b65554', 'aliyun.oss');
INSERT INTO rule_type VALUES ('f6f58432-7418-49c6-bb79-f764694e53ce', '1299a29b-e19d-4186-93fd-a18ed1b2584a', 'aliyun.slb');
INSERT INTO rule_type VALUES ('fd0d0f27-ec6e-40ba-b30a-11043b209bbc', 'c95fde94-53a5-4658-98a4-56a0c6d951d4', 'aliyun.rds');
INSERT INTO rule_type VALUES ('fd8eeea3-cabf-491f-9411-6d11407f45b3', 'e2d51fc6-2ec2-4d17-bf87-13a3df90ea5d', 'aliyun.slb');

