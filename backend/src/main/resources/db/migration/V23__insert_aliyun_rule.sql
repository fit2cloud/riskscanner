
INSERT INTO `rule` (`id`, `name`, `status`, `severity`, `description`, `script`, `parameter`, `plugin_id`, `plugin_name`, `plugin_icon`, `last_modified`, `flag`, `scan_type`) VALUES ('2274a926-ea5e-4cdc-915e-09fa6d803bff', 'Aliyun SLB VPC扫描', 1, 'HighRisk', 'Aliyun  账号下SLB负载均衡实例指定属于哪些VPC, 属于则合规，不属于则\"不合规\"', 'policies:\n  # 检测您账号下SLB负载均衡实例指定属于哪些VPC, 属于则合规，不属于则\"不合规\"。\n  - name: aliyun-slb-vpc-type\n    resource: aliyun.slb\n    filters:\n      - type: vpc-type\n        value: [${{value1}}, ${{value2}}]', '[{\"key\":\"value1\",\"name\":\"vpcId1\",\"defaultValue\":\"\\\"vpc-1\\\"\",\"required\":true},{\"key\":\"value2\",\"name\":\"vpcId2\",\"defaultValue\":\"\\\"vpc-2\\\"\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '009'), 1, 'custodian');
INSERT INTO `rule` (`id`, `name`, `status`, `severity`, `description`, `script`, `parameter`, `plugin_id`, `plugin_name`, `plugin_icon`, `last_modified`, `flag`, `scan_type`) VALUES ('f3675431-730d-446f-b062-7301c5c40f23', 'Aliyun Rds VPC扫描', 1, 'HighRisk', 'Aliyun  账号下的Rds实例指定属于哪些VPC, 属于则合规，不属于则\"不合规\"', 'policies:\n  # 检测您账号下的Rds实例指定属于哪些VPC, 属于则合规，不属于则\"不合规\"。\n  - name: aliyun-rds-vpc-type\n    resource: aliyun.rds\n    filters:\n      - type: vpc-type\n        value: [${{value1}}, ${{value2}}]', '[{\"key\":\"value1\",\"name\":\"vpcId1\",\"defaultValue\":\"\\\"vpc-1\\\"\",\"required\":true},{\"key\":\"value2\",\"name\":\"vpcId2\",\"defaultValue\":\"\\\"vpc-2\\\"\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '009'), 1, 'custodian');
INSERT INTO `rule` (`id`, `name`, `status`, `severity`, `description`, `script`, `parameter`, `plugin_id`, `plugin_name`, `plugin_icon`, `last_modified`, `flag`, `scan_type`) VALUES ('4f8fa101-171a-4491-9485-e5aa091a88a4', 'Aliyun Polardb VPC扫描', 1, 'HighRisk', 'Aliyun  账号下的Polardb实例指定属于哪些VPC, 属于则合规，不属于则\"不合规\"', 'policies:\n  # 检测您账号下的Polardb实例指定属于哪些VPC, 属于则合规，不属于则\"不合规\"。\n  - name: aliyun-polardb-vpc-type\n    resource: aliyun.polardb\n    filters:\n      - type: vpc-type\n        value: [${{value1}}, ${{value2}}]', '[{\"key\":\"value1\",\"name\":\"vpcId1\",\"defaultValue\":\"\\\"vpc-1\\\"\",\"required\":true},{\"key\":\"value2\",\"name\":\"vpcId2\",\"defaultValue\":\"\\\"vpc-2\\\"\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '009'), 1, 'custodian');
INSERT INTO `rule` (`id`, `name`, `status`, `severity`, `description`, `script`, `parameter`, `plugin_id`, `plugin_name`, `plugin_icon`, `last_modified`, `flag`, `scan_type`) VALUES ('f4ebb59b-c93a-4f34-9e66-660b03943d7d', 'Aliyun ECS VPC扫描', 1, 'HighRisk', 'Aliyun  账号下的Ecs实例指定属于哪些VPC, 属于则合规，不属于则\"不合规\"', 'policies:\n  # 检测您账号下的Ecs实例指定属于哪些VPC, 属于则合规，不属于则\"不合规\"。\n  - name: aliyun-ecs-vpc-type\n    resource: aliyun.ecs\n    filters:\n      - type: vpc-type\n        value: [${{value1}}, ${{value2}}]', '[{\"key\":\"value1\",\"name\":\"vpcId1\",\"defaultValue\":\"\\\"vpc-1\\\"\",\"required\":true},{\"key\":\"value2\",\"name\":\"vpcId2\",\"defaultValue\":\"\\\"vpc-2\\\"\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '009'), 1, 'custodian');
INSERT INTO `rule` (`id`, `name`, `status`, `severity`, `description`, `script`, `parameter`, `plugin_id`, `plugin_name`, `plugin_icon`, `last_modified`, `flag`, `scan_type`) VALUES ('e28786b3-f2b1-46de-b4b6-7835b42ae58b', 'Aliyun SLB负载均衡监听扫描', 1, 'HighRisk', 'Aliyun  账号下的SLB负载均衡是否开启HTTPS或HTTP监听，开启视为合规，否则不合规', 'policies:\n  # 检测您账号下的SLB负载均衡是否开启HTTPS或HTTP监听，开启视为合规，否则不合规。\n  - name: aliyun-slb-listener-type\n    resource: aliyun.slb\n    filters:\n      - type: listener-type\n        value: [${{value1}}, ${{value2}}]', '[{\"key\":\"value1\",\"name\":\"监听类型1\",\"defaultValue\":\"\\\"https\\\"\",\"required\":true},{\"key\":\"value2\",\"name\":\"监听类型2\",\"defaultValue\":\"\\\"http\\\"\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '009'), 1, 'custodian');
INSERT INTO `rule` (`id`, `name`, `status`, `severity`, `description`, `script`, `parameter`, `plugin_id`, `plugin_name`, `plugin_icon`, `last_modified`, `flag`, `scan_type`) VALUES ('4d427c93-5645-445a-93a3-0c536d6865ab', 'Aliyun SecurityGroup安全组端口访问扫描', 1, 'HighRisk', 'Aliyun  账号下ECS安全组配置允许所有端口访问视为”不合规“，否则为”合规“', 'policies:\n  # 账号下ECS安全组配置允许所有端口访问视为”不合规“，否则为”合规“\n  - name: aliyun-sg-ports\n    resource: aliyun.security-group\n    filters:\n      - type: source-ports\n        SourceCidrIp: ${{SourceCidrIp}}\n        PortRange: ${{PortRange}}', '[{\"key\":\"SourceCidrIp\",\"name\":\"目标IP地址段\",\"defaultValue\":\"\\\"0.0.0.0/0\\\"\",\"required\":true},{\"key\":\"PortRange\",\"name\":\"端口号\",\"defaultValue\":\"”-1/-1“\",\"required\":true}]', 'fit2cloud-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '009'), 1, 'custodian');

INSERT INTO `rule_tag_mapping` (`rule_id`, `tag_key`) VALUES ('2274a926-ea5e-4cdc-915e-09fa6d803bff', 'safety');
INSERT INTO `rule_tag_mapping` (`rule_id`, `tag_key`) VALUES ('4d427c93-5645-445a-93a3-0c536d6865ab', 'safety');
INSERT INTO `rule_tag_mapping` (`rule_id`, `tag_key`) VALUES ('4f8fa101-171a-4491-9485-e5aa091a88a4', 'safety');
INSERT INTO `rule_tag_mapping` (`rule_id`, `tag_key`) VALUES ('e28786b3-f2b1-46de-b4b6-7835b42ae58b', 'safety');
INSERT INTO `rule_tag_mapping` (`rule_id`, `tag_key`) VALUES ('f3675431-730d-446f-b062-7301c5c40f23', 'safety');
INSERT INTO `rule_tag_mapping` (`rule_id`, `tag_key`) VALUES ('f4ebb59b-c93a-4f34-9e66-660b03943d7d', 'safety');

INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('2274a926-ea5e-4cdc-915e-09fa6d803bff', '10');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('2274a926-ea5e-4cdc-915e-09fa6d803bff', '13');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('2274a926-ea5e-4cdc-915e-09fa6d803bff', '92');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('2274a926-ea5e-4cdc-915e-09fa6d803bff', '93');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('2274a926-ea5e-4cdc-915e-09fa6d803bff', '95');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('4d427c93-5645-445a-93a3-0c536d6865ab', '10');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('4d427c93-5645-445a-93a3-0c536d6865ab', '13');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('4d427c93-5645-445a-93a3-0c536d6865ab', '92');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('4d427c93-5645-445a-93a3-0c536d6865ab', '93');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('4d427c93-5645-445a-93a3-0c536d6865ab', '95');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('4f8fa101-171a-4491-9485-e5aa091a88a4', '10');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('4f8fa101-171a-4491-9485-e5aa091a88a4', '13');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('4f8fa101-171a-4491-9485-e5aa091a88a4', '92');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('4f8fa101-171a-4491-9485-e5aa091a88a4', '93');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('4f8fa101-171a-4491-9485-e5aa091a88a4', '95');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('e28786b3-f2b1-46de-b4b6-7835b42ae58b', '10');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('e28786b3-f2b1-46de-b4b6-7835b42ae58b', '13');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('e28786b3-f2b1-46de-b4b6-7835b42ae58b', '92');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('e28786b3-f2b1-46de-b4b6-7835b42ae58b', '93');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('e28786b3-f2b1-46de-b4b6-7835b42ae58b', '95');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('f3675431-730d-446f-b062-7301c5c40f23', '10');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('f3675431-730d-446f-b062-7301c5c40f23', '13');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('f3675431-730d-446f-b062-7301c5c40f23', '92');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('f3675431-730d-446f-b062-7301c5c40f23', '93');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('f3675431-730d-446f-b062-7301c5c40f23', '95');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('f4ebb59b-c93a-4f34-9e66-660b03943d7d', '10');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('f4ebb59b-c93a-4f34-9e66-660b03943d7d', '13');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('f4ebb59b-c93a-4f34-9e66-660b03943d7d', '92');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('f4ebb59b-c93a-4f34-9e66-660b03943d7d', '93');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('f4ebb59b-c93a-4f34-9e66-660b03943d7d', '95');

INSERT INTO `rule_group_mapping` (`rule_id`, `group_id`) VALUES ('2274a926-ea5e-4cdc-915e-09fa6d803bff', '2');
INSERT INTO `rule_group_mapping` (`rule_id`, `group_id`) VALUES ('4d427c93-5645-445a-93a3-0c536d6865ab', '1');
INSERT INTO `rule_group_mapping` (`rule_id`, `group_id`) VALUES ('4f8fa101-171a-4491-9485-e5aa091a88a4', '2');
INSERT INTO `rule_group_mapping` (`rule_id`, `group_id`) VALUES ('e28786b3-f2b1-46de-b4b6-7835b42ae58b', '1');
INSERT INTO `rule_group_mapping` (`rule_id`, `group_id`) VALUES ('f3675431-730d-446f-b062-7301c5c40f23', '2');
INSERT INTO `rule_group_mapping` (`rule_id`, `group_id`) VALUES ('f4ebb59b-c93a-4f34-9e66-660b03943d7d', '2');

INSERT INTO `rule_type` (`id`, `rule_id`, `resource_type`) VALUES ('0281bea0-ac3f-443c-980d-068200bf4483', '4d427c93-5645-445a-93a3-0c536d6865ab', 'aliyun.security-group');
INSERT INTO `rule_type` (`id`, `rule_id`, `resource_type`) VALUES ('abb87eba-a9bc-4176-be91-e0df46468b25', 'f3675431-730d-446f-b062-7301c5c40f23', 'aliyun.rds');
INSERT INTO `rule_type` (`id`, `rule_id`, `resource_type`) VALUES ('cd24cf12-4a17-4900-be38-88458b98bc8a', 'e28786b3-f2b1-46de-b4b6-7835b42ae58b', 'aliyun.slb');
INSERT INTO `rule_type` (`id`, `rule_id`, `resource_type`) VALUES ('cfaaa915-eb25-4089-9c60-559095d201df', 'f4ebb59b-c93a-4f34-9e66-660b03943d7d', 'aliyun.ecs');
INSERT INTO `rule_type` (`id`, `rule_id`, `resource_type`) VALUES ('da05c56e-3c4c-4ca8-8ea1-c7668e3c2a78', '2274a926-ea5e-4cdc-915e-09fa6d803bff', 'aliyun.slb');
INSERT INTO `rule_type` (`id`, `rule_id`, `resource_type`) VALUES ('f03e4c89-5f6d-402b-a4af-38c28281568f', '4f8fa101-171a-4491-9485-e5aa091a88a4', 'aliyun.polardb');
