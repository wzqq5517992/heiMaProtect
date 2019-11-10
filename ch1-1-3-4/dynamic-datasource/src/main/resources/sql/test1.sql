/*
Navicat MySQL Data Transfer

Source Server         : 本地数据库
Source Server Version : 50624
Source Host           : 127.0.0.1:3306
Source Database       : test1

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2018-06-25 17:56:09
*/

# Create Database
# ------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS test1 DEFAULT CHARACTER SET = utf8mb4;

Use test1;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'test1数据库user表内容');
