
insert into plugin ( id, name, icon, update_time) values ('fit2cloud-aws-plugin', 'AWS 亚马逊云', 'aws.png', concat(unix_timestamp(now()), '001'));
insert into plugin ( id, name, icon, update_time) values ('fit2cloud-azure-plugin', 'Azure 微软云', 'azure.png', concat(unix_timestamp(now()), '001'));

INSERT INTO rule_group (`name`, `description`, `level`, `plugin_id`, `flag`) VALUES ('AWS 等保预检', '等保合规检查（全称为等级保护合规检查）为您提供了全面覆盖通信网络、区域边界、计算环境和管理中心的网络安全检查。', '等保三级', 'fit2cloud-aws-plugin', 1);
INSERT INTO rule_group (`name`, `description`, `level`, `plugin_id`, `flag`) VALUES ('AWS CIS合规检查', 'CIS（Center for Internet Security）合规检查能力，为您动态且持续地监控您保有在云上的资源是否符合 CIS Control 网络安全架构要求。', '高风险', 'fit2cloud-aws-plugin', 1);
INSERT INTO rule_group (`name`, `description`, `level`, `plugin_id`, `flag`) VALUES ('AWS S3合规基线', 'S3 合规检查为您提供全方位的对象存储资源检查功能。', '高风险', 'fit2cloud-aws-plugin', 1);

INSERT INTO rule_group (`name`, `description`, `level`, `plugin_id`, `flag`) VALUES ('Azure 等保预检', '等保合规检查（全称为等级保护合规检查）为您提供了全面覆盖通信网络、区域边界、计算环境和管理中心的网络安全检查。', '等保三级', 'fit2cloud-azure-plugin', 1);
INSERT INTO rule_group (`name`, `description`, `level`, `plugin_id`, `flag`) VALUES ('Azure CIS合规检查', 'CIS（Center for Internet Security）合规检查能力，为您动态且持续地监控您保有在云上的资源是否符合 CIS Control 网络安全架构要求。', '高风险', 'fit2cloud-azure-plugin', 1);
INSERT INTO rule_group (`name`, `description`, `level`, `plugin_id`, `flag`) VALUES ('Azure S3合规基线', 'S3 合规检查为您提供全方位的对象存储资源检查功能。', '高风险', 'fit2cloud-aws-plugin', 1);