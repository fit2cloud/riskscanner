
UPDATE rule_inspection_report SET improvement='查看您账号下的资源是否启用了加密，若未加密，会导致该规则不合规。' WHERE id=96;
UPDATE rule_inspection_report SET improvement='查看您账号下的RDS实例是否对外开放，若已开放，会导致该规则不合规。' WHERE id=97;
