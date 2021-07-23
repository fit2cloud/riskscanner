
ALTER TABLE plugin ADD scan_type varchar(32) DEFAULT NULL COMMENT '支持的扫描引擎类型';

UPDATE plugin set scan_type = 'custodian';

INSERT INTO plugin ( id, name, icon, update_time, scan_type) VALUES ('fit2cloud-nuclei-plugin', 'Nuclei', 'nuclei.png', concat(unix_timestamp(now()), '010'), 'nuclei');

INSERT INTO rule_group (`name`, `description`, `level`, `plugin_id`, `flag`) VALUES ('Nuclei 漏洞扫描', '漏洞扫描，为您提供发现可利用漏洞的一种安全检测（渗透攻击）。', '漏洞扫描', 'fit2cloud-nuclei-plugin', 1);

INSERT INTO `cloud_account` (`id`, `name`, `credential`, `plugin_id`, `plugin_name`, `plugin_icon`, `status`, `create_time`, `update_time`, `creator`, `regions`, `proxy_id`) VALUES ('rs124d9d-f108-3198-8198-798k9971f814', 'Nuclei 漏洞扫描', '{\"description\":\"基于模板的 nuclei 被用来发送请求给目标，有着实现零误报的优点，并且可以对已知的路径进行有效的扫描。nuclei 的主要用于在初期的探测阶段快速地对已知的且易于检测的漏洞或者 CVE 进行扫描。如果存在 WAF 的话，nuclei 使用 retryablehttp-go 库来处理各种错误，并且重新尝试攻击。\", \"targetAddress\":\"https://baidu.com\"}', 'fit2cloud-nuclei-plugin', 'Nuclei', 'nuclei.png', 'VALID', concat(unix_timestamp(now()), '010'), concat(unix_timestamp(now()), '010'), 'admin', '[{\"regionId\":\"ALL\",\"regionName\":\"Nuclei 漏洞扫描\"}]', 0);
