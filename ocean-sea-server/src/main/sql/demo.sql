
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for wd_demo_info
-- ----------------------------
DROP TABLE IF EXISTS `wd_demo_info`;
CREATE TABLE `wd_demo_info`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `GOODS_CODE` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `GOODS_SN` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `STATUS` tinyint(4) NOT NULL DEFAULT 0,
  `CREATE_TIME` datetime(0) NOT NULL,
  `UPDATE_TIME` timestamp(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `VERSION` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wd_demo_info
-- ----------------------------
INSERT INTO `wd_demo_info` VALUES (1, 'CODE123456', 'SN22222212', 0, '2016-07-20 14:58:50', '2016-07-20 14:58:50', 0);
INSERT INTO `wd_demo_info` VALUES (2, 'CODE123456', 'SN22222212', 0, '2016-07-20 14:59:30', '2016-07-20 14:59:30', 0);
INSERT INTO `wd_demo_info` VALUES (3, 'CODE123456', 'SN22222212', 0, '2016-07-20 15:06:02', '2016-07-20 15:06:02', 0);
INSERT INTO `wd_demo_info` VALUES (4, 'CODE123456', 'SN22222212', 0, '2016-07-20 15:08:30', '2016-07-20 15:08:30', 0);

SET FOREIGN_KEY_CHECKS = 1;
