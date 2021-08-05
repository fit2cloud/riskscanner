
UPDATE `rule` SET `script` = 'policies:\n  # 检测您账号下的Rds实例指定属于哪些VPC, 属于则合规，不属于则\"不合规\"。\n  - name: aliyun-rds-vpc-type\n    resource: aliyun.rds\n    filters:\n      - type: vpc-type\n        vpcIds: [${{value1}}, ${{value2}}]' WHERE id = 'f3675431-730d-446f-b062-7301c5c40f23';
UPDATE `rule` SET `script` = 'policies:\n  # 检测您账号下SLB负载均衡实例指定属于哪些VPC, 属于则合规，不属于则\"不合规\"。\n  - name: aliyun-slb-vpc-type\n    resource: aliyun.slb\n    filters:\n      - type: vpc-type\n        vpcIds: [${{value1}}, ${{value2}}]' WHERE id = '2274a926-ea5e-4cdc-915e-09fa6d803bff';
UPDATE `rule` SET `script` = 'policies:\n  # 检测您账号下的Polardb实例指定属于哪些VPC, 属于则合规，不属于则\"不合规\"。\n  - name: aliyun-polardb-vpc-type\n    resource: aliyun.polardb\n    filters:\n      - type: vpc-type\n        vpcIds: [${{value1}}, ${{value2}}]' WHERE id = '4f8fa101-171a-4491-9485-e5aa091a88a4';
UPDATE `rule` SET `script` = 'policies:\n  # 检测您账号下的Ecs实例指定属于哪些VPC, 属于则合规，不属于则\"不合规\"。\n  - name: aliyun-ecs-vpc-type\n    resource: aliyun.ecs\n    filters:\n      - type: vpc-type\n        vpcIds: [${{value1}}, ${{value2}}]' WHERE id = 'f4ebb59b-c93a-4f34-9e66-660b03943d7d';
UPDATE `rule_group_mapping` SET `group_id` = 5 WHERE id = 43;
