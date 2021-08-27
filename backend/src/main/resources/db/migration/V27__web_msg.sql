-- ----------------------------
-- Table structure for msg
-- ----------------------------
DROP TABLE IF EXISTS `msg`;
CREATE TABLE `msg` (
  `msg_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息主键',
  `user_id` varchar(50) NOT NULL COMMENT '用户ID',
  `type_id` bigint(20) NOT NULL COMMENT '类型',
  `status` tinyint(1) NOT NULL COMMENT '状态',
  `param` varchar(255) DEFAULT NULL COMMENT '路由参数',
  `create_time` bigint(13) NOT NULL COMMENT '发送时间',
  `read_time` bigint(13) DEFAULT NULL COMMENT '读取时间',
  `content` varchar(255) DEFAULT NULL COMMENT '消息内容',
  PRIMARY KEY (`msg_id`) USING BTREE,
  KEY `inx_msg_userid` (`user_id`) USING BTREE,
  KEY `inx_msg_type` (`type_id`) USING BTREE,
  KEY `inx_msg_status` (`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='消息通知表';

-- ----------------------------
-- Table structure for msg_channel
-- ----------------------------
DROP TABLE IF EXISTS `msg_channel`;
CREATE TABLE `msg_channel` (
  `msg_channel_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `channel_name` varchar(255) DEFAULT NULL COMMENT '渠道名称',
  PRIMARY KEY (`msg_channel_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='消息渠道表';

-- ----------------------------
-- Records of msg_channel
-- ----------------------------
BEGIN;
INSERT INTO `msg_channel` VALUES (1, 'webmsg.channel_inner_msg');
COMMIT;

-- ----------------------------
-- Table structure for msg_setting
-- ----------------------------
DROP TABLE IF EXISTS `msg_setting`;
CREATE TABLE `msg_setting` (
  `msg_setting_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(50) NOT NULL COMMENT '用户ID',
  `type_id` bigint(20) NOT NULL COMMENT '类型ID',
  `channel_id` bigint(20) NOT NULL COMMENT '渠道ID',
  `enable` tinyint(1) DEFAULT NULL COMMENT '是否启用',
  PRIMARY KEY (`msg_setting_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='消息设置表';


