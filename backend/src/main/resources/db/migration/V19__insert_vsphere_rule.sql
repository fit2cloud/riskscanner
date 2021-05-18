
insert into plugin ( id, name, icon, update_time) values ('fit2cloud-vsphere-plugin', 'VMware vSphere', 'vmware.png', concat(unix_timestamp(now()), '007'));

INSERT INTO rule_group (`name`, `description`, `level`, `plugin_id`, `flag`) VALUES ('VMware 安全检查', '安全检查，为您提供通信网络、计算环境和管理中心的网络安全检查。', '等保三级', 'fit2cloud-vsphere-plugin', 1);
