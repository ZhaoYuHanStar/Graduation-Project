/*
Navicat MySQL Data Transfer

Source Server         : yxd
Source Server Version : 50506
Source Host           : localhost:3306
Source Database       : toy_online

Target Server Type    : MYSQL
Target Server Version : 50506
File Encoding         : 65001

Date: 2021-01-04 20:48:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `attention`
-- ----------------------------
DROP TABLE IF EXISTS `attention`;
CREATE TABLE `attention` (
  `user_name_from` varchar(16) NOT NULL,
  `user_name_to` varchar(16) NOT NULL,
  `time` varchar(32) NOT NULL,
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `start` varchar(32) NOT NULL,
  `update` varchar(32) NOT NULL,
  `delete` int(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_name_from` (`user_name_from`),
  KEY `user_name_to` (`user_name_to`),
  CONSTRAINT `user_name_from` FOREIGN KEY (`user_name_from`) REFERENCES `user` (`name`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_name_to` FOREIGN KEY (`user_name_to`) REFERENCES `user` (`name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of attention
-- ----------------------------

-- ----------------------------
-- Table structure for `comments`
-- ----------------------------
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments` (
  `user_name_coms` varchar(16) NOT NULL,
  `post_id_coms` int(16) NOT NULL,
  `content` varchar(128) NOT NULL,
  `floor` int(16) NOT NULL,
  `time` varchar(64) NOT NULL,
  `id` int(16) NOT NULL,
  `start` varchar(32) NOT NULL,
  `update` varchar(32) NOT NULL,
  `delete` int(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_name_coms` (`user_name_coms`),
  KEY `post_id_coms` (`post_id_coms`),
  CONSTRAINT `post_id_coms` FOREIGN KEY (`post_id_coms`) REFERENCES `post` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_name_coms` FOREIGN KEY (`user_name_coms`) REFERENCES `user` (`name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of comments
-- ----------------------------

-- ----------------------------
-- Table structure for `label`
-- ----------------------------
DROP TABLE IF EXISTS `label`;
CREATE TABLE `label` (
  `id` int(16) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  `start` varchar(32) NOT NULL,
  `update` varchar(32) NOT NULL,
  `delete` int(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of label
-- ----------------------------

-- ----------------------------
-- Table structure for `like`
-- ----------------------------
DROP TABLE IF EXISTS `like`;
CREATE TABLE `like` (
  `user_name` varchar(16) NOT NULL,
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `post_id` int(16) NOT NULL,
  `time` varchar(128) NOT NULL,
  `start` varchar(32) NOT NULL,
  `update` varchar(32) NOT NULL,
  `delete` int(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_name_like` (`user_name`),
  KEY `post_id` (`post_id`),
  CONSTRAINT `post_id` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_name_like` FOREIGN KEY (`user_name`) REFERENCES `user` (`name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of like
-- ----------------------------

-- ----------------------------
-- Table structure for `post`
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
  `title` varchar(16) NOT NULL,
  `content` varchar(128) NOT NULL,
  `time` char(32) NOT NULL,
  `user_name` varchar(16) NOT NULL,
  `imgs` varchar(128) NOT NULL,
  `viewed` int(16) NOT NULL,
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `labels` varchar(32) NOT NULL,
  `start` varchar(32) NOT NULL,
  `update` varchar(32) NOT NULL,
  `delete` int(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_name` (`user_name`),
  CONSTRAINT `user_name` FOREIGN KEY (`user_name`) REFERENCES `user` (`name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of post
-- ----------------------------

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `name` varchar(16) NOT NULL COMMENT '用户名',
  `sex` int(11) NOT NULL,
  `img` varchar(128) DEFAULT NULL,
  `introduction` varchar(128) DEFAULT NULL,
  `phone` char(16) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `start` varchar(32) NOT NULL,
  `update` varchar(32) DEFAULT NULL,
  `delete` int(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
