-- MySQL dump 10.13  Distrib 8.3.0, for Win64 (x86_64)
--
-- Host: localhost    Database: lavson
-- ------------------------------------------------------
-- Server version	8.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+08:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标题',
  `content` text COLLATE utf8mb4_unicode_ci COMMENT '内容',
  `tags` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签列表（json 数组）',
  `answer` text COLLATE utf8mb4_unicode_ci COMMENT '题目答案',
  `submitNum` int NOT NULL DEFAULT '0' COMMENT '题目提交数',
  `acceptedNum` int NOT NULL DEFAULT '0' COMMENT '题目通过数',
  `judgeCase` text COLLATE utf8mb4_unicode_ci COMMENT '判题用例（json 数组）',
  `judgeConfig` text COLLATE utf8mb4_unicode_ci COMMENT '判题配置（json 对象）',
  `thumbNum` int NOT NULL DEFAULT '0' COMMENT '点赞数',
  `favourNum` int NOT NULL DEFAULT '0' COMMENT '收藏数',
  `userId` bigint NOT NULL COMMENT '创建用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=1790346048124862466 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题目';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (1790020640590295042,'A+B','首行输入两个整数(x < 10^10)，用空格分隔开。计算并输出这两个数的和。','[\"Easy\",\"Beginner\"]','',0,0,'[{\"input\":\"-5 1000\",\"output\":\"995\"},{\"input\":\"11111 99999\",\"output\":\"111110\"},{\"input\":\"2147483647 1\",\"output\":\"2147483648\"},{\"input\":\"-100000 -100000\",\"output\":\"-200000\"},{\"input\":\"-2147483648 -1\",\"output\":\"-2147483649\"}]','{\"timeLimit\":2000,\"memoryLimit\":32768,\"stackLimit\":0}',0,0,1788167247869554690,'2024-05-13 22:05:52','2024-05-13 22:05:52',0),(1790346048124862465,'A+B(demo)','首行输入两个整数(x < 10^10)，用空格分隔开。计算并输出这两个数的和。','[\"Easy\",\"Beginner\"]','',0,0,'[{\"input\":\"1 2\",\"output\":\"3\"}]','{\"timeLimit\":2000,\"memoryLimit\":32768,\"stackLimit\":0}',0,0,1788167247869554690,'2024-05-14 19:38:55','2024-05-27 15:16:39',1);
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question_submit`
--

DROP TABLE IF EXISTS `question_submit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question_submit` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `language` varchar(128) NOT NULL COMMENT '编程语言',
  `code` text NOT NULL COMMENT '用户代码',
  `judgeInfo` text COMMENT '判题信息（json 对象）',
  `status` int NOT NULL DEFAULT '0' COMMENT '判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）',
  `questionId` bigint NOT NULL COMMENT '题目 id',
  `userId` bigint NOT NULL COMMENT '创建用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_questionId` (`questionId`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='题目提交';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question_submit`
--

LOCK TABLES `question_submit` WRITE;
/*!40000 ALTER TABLE `question_submit` DISABLE KEYS */;
/*!40000 ALTER TABLE `question_submit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userAccount` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
  `userPassword` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `unionId` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信开放平台id',
  `mpOpenId` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '公众号openId',
  `userName` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户昵称',
  `userAvatar` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户头像',
  `userProfile` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户简介',
  `userRole` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user' COMMENT '用户角色：user/admin/ban',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_unionId` (`unionId`)
) ENGINE=InnoDB AUTO_INCREMENT=1788171459244449794 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1788167247869554690,'jison','7576e2dc584ebf320f1adea885e3fc98',NULL,NULL,'','','eden','admin','2024-05-08 19:21:09','2024-05-08 19:25:15',0),(1788171459244449793,'lavson','21b003e82f468c8aa7fe8add8f58d065',NULL,NULL,'','',NULL,'user','2024-05-08 19:37:53','2024-05-08 19:37:53',0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-27 15:17:57
