mysqldump: [Warning] Using a password on the command line interface can be insecure.
-- MySQL dump 10.13  Distrib 8.0.36, for Linux (x86_64)
--
-- Host: localhost    Database: urturn
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `history`
--

DROP TABLE IF EXISTS `history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `history` (
  `is_deleted` bit(1) DEFAULT NULL,
  `total_round` int NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `end_time` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `manager_id` bigint NOT NULL,
  `pair_id` bigint NOT NULL,
  `problem1_id` bigint NOT NULL,
  `problem2_id` bigint NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `retro1` varchar(500) DEFAULT NULL,
  `retro2` varchar(500) DEFAULT NULL,
  `code1` varchar(5000) DEFAULT NULL,
  `code2` varchar(5000) DEFAULT NULL,
  `language1` enum('JAVA','CPP','C','JAVASCRIPT','PYTHON') DEFAULT NULL,
  `language2` enum('JAVA','CPP','C','JAVASCRIPT','PYTHON') DEFAULT NULL,
  `result` enum('SUCCESS','FAILURE','SURRENDER') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmo9alkffivw6j9pwoxc3orisg` (`manager_id`),
  KEY `FK5j5ihu25jacsowtnbnqdd6ir1` (`pair_id`),
  KEY `FKo1dxpw4l0qy46lsll8o3tfse5` (`problem1_id`),
  KEY `FK214a0ibsv2dyut9xpe3b9ga0c` (`problem2_id`),
  CONSTRAINT `FK214a0ibsv2dyut9xpe3b9ga0c` FOREIGN KEY (`problem2_id`) REFERENCES `problem` (`id`),
  CONSTRAINT `FK5j5ihu25jacsowtnbnqdd6ir1` FOREIGN KEY (`pair_id`) REFERENCES `member` (`id`),
  CONSTRAINT `FKmo9alkffivw6j9pwoxc3orisg` FOREIGN KEY (`manager_id`) REFERENCES `member` (`id`),
  CONSTRAINT `FKo1dxpw4l0qy46lsll8o3tfse5` FOREIGN KEY (`problem1_id`) REFERENCES `problem` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=292 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `history`
--


--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `exp` int NOT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `github_access_token` varchar(255) DEFAULT NULL,
  `github_unique_id` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `profile_image` varchar(255) NOT NULL,
  `repository` varchar(255) DEFAULT NULL,
  `level` enum('LEVEL1','LEVEL2','LEVEL3','LEVEL4','LEVEL5') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_hh9kg6jti4n1eoiertn2k6qsc` (`nickname`)
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

--
-- Table structure for table `member_problem`
--

DROP TABLE IF EXISTS `member_problem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member_problem` (
  `is_deleted` bit(1) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `member_id` bigint DEFAULT NULL,
  `problem_id` bigint DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpa97xjg3nanp1qpeq9knkqsdk` (`member_id`),
  KEY `FKa5f0d4ghe617f07gmhow79bqv` (`problem_id`),
  CONSTRAINT `FKa5f0d4ghe617f07gmhow79bqv` FOREIGN KEY (`problem_id`) REFERENCES `problem` (`id`),
  CONSTRAINT `FKpa97xjg3nanp1qpeq9knkqsdk` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=777 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member_problem`
--


--
-- Table structure for table `member_roles`
--

DROP TABLE IF EXISTS `member_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member_roles` (
  `member_id` bigint NOT NULL,
  `roles` enum('USER','ADMIN') DEFAULT NULL,
  KEY `FKet63dfllh4o5qa9qwm7f5kx9x` (`member_id`),
  CONSTRAINT `FKet63dfllh4o5qa9qwm7f5kx9x` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member_roles`
--

--
-- Table structure for table `problem`
--

DROP TABLE IF EXISTS `problem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `problem` (
  `is_deleted` bit(1) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `level` enum('LEVEL1','LEVEL2','LEVEL3','LEVEL4','LEVEL5') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `problem`
--

LOCK TABLES `problem` WRITE;
/*!40000 ALTER TABLE `problem` DISABLE KEYS */;
INSERT INTO `problem` VALUES (_binary '\0','2024-05-15 12:37:01.715551',1,'2024-05-15 12:37:01.715551','https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/1012%EB%B2%88+%EC%9C%A0%EA%B8%B0%EB%86%8D+%EC%96%91%EC%83%81%EC%B6%94.md','유기농 양상추','LEVEL1'),(_binary '\0','2024-05-15 12:37:01.860638',2,'2024-05-15 12:37:01.860638','https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/1697%EB%B2%88+%EC%88%A0%EB%9E%98%EC%9E%A1%EA%B8%B0.md','술래잡기','LEVEL1'),(_binary '\0','2024-05-15 12:37:01.996819',3,'2024-05-15 12:37:01.996819','https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/2573%EB%B2%88+%EB%B9%99%ED%95%98.md','빙하','LEVEL2'),(_binary '\0','2024-05-15 12:37:02.057630',4,'2024-05-15 12:37:02.057630','https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/1759%EB%B2%88+%EC%95%94%ED%98%B8%ED%99%94.md','암호화','LEVEL2'),(_binary '\0','2024-05-15 12:37:02.090208',5,'2024-05-15 12:37:02.090208','https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/14499%EB%B2%88+%EC%A0%95%EC%9C%A1%EB%A9%B4%EC%B2%B4+%EB%8D%98%EC%A7%80%EA%B8%B0.md','정육면체 던지기','LEVEL2'),(_binary '\0','2024-05-15 12:37:02.168093',6,'2024-05-15 12:37:02.168093','https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/1238%EB%B2%88+%EC%B6%95%EC%A0%9C.md','축제','LEVEL2'),(_binary '\0','2024-05-15 12:37:02.237033',7,'2024-05-15 12:37:02.237033','https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/1937%EB%B2%88+%EC%9A%95%EC%8B%AC%EC%9F%81%EC%9D%B4+%ED%86%A0%EB%81%BC.md','욕심쟁이 토끼','LEVEL3'),(_binary '\0','2024-05-15 12:37:02.268315',8,'2024-05-15 12:37:02.268315','https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/9370%EB%B2%88+%EB%AF%B8%ED%99%95%EC%9D%B8+%EB%8F%84%EC%B0%A9%EC%A7%80.md','미확인 도착지','LEVEL3'),(_binary '\0','2024-05-15 12:37:02.375564',9,'2024-05-15 12:37:02.375564','https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/2169%EB%B2%88+%EB%A1%9C%EB%B4%87+%EC%A1%B0%EC%A2%85%ED%95%98%EA%B8%B0.md','로봇 조종하기','LEVEL4'),(_binary '\0','2024-05-15 12:37:02.484102',10,'2024-05-15 12:37:02.484102','https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/16985%EB%B2%88+AMAAAAZZZZEEEE.md','AMAAAAZZZZEEEE','LEVEL4'),(_binary '\0','2024-05-15 12:37:02.621899',11,'2024-05-15 12:37:02.621899','https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/2325%EB%B2%88+%EB%B2%A0%EB%8A%91%EC%A0%84%EC%9F%81.md','베늑전쟁','LEVEL5'),(_binary '\0','2024-05-15 12:37:02.687079',12,'2024-05-15 12:37:02.687079','https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/3780%EB%B2%88+%EB%84%A4%ED%8A%B8%EC%9B%8C%ED%81%AC+%EC%97%B0%EA%B2%B0.md','네트워크 연결','LEVEL5');
/*!40000 ALTER TABLE `problem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `testcase`
--

DROP TABLE IF EXISTS `testcase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `testcase` (
  `is_deleted` bit(1) DEFAULT NULL,
  `is_public` bit(1) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `problem_id` bigint DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `stdin` varchar(2000) DEFAULT NULL,
  `expected_output` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK75jw84bkakgr5i4lq7yqavho9` (`problem_id`),
  CONSTRAINT `FK75jw84bkakgr5i4lq7yqavho9` FOREIGN KEY (`problem_id`) REFERENCES `problem` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `testcase`
--

LOCK TABLES `testcase` WRITE;
/*!40000 ALTER TABLE `testcase` DISABLE KEYS */;
INSERT INTO `testcase` VALUES (_binary '\0',_binary '','2024-05-15 12:37:02.012539',13,3,'2024-05-15 12:37:02.012539','5 7\n0 0 0 0 0 0 0\n0 2 4 5 3 0 0\n0 3 0 2 5 2 0\n0 7 6 2 4 0 0\n0 0 0 0 0 0 0','2'),(_binary '\0',_binary '','2024-05-15 12:37:02.029326',14,3,'2024-05-15 12:37:02.029326','5 5 \n0 0 0 0 0 \n0 1 1 1 0 \n0 1 0 1 0 \n0 1 1 1 0 \n0 0 0 0 0','0'),(_binary '\0',_binary '\0','2024-05-15 12:37:02.044715',15,3,'2024-05-15 12:37:02.044715','4 4 \n0 0 0 0 \n0 3 1 0 \n0 1 3 0 \n0 0 0 0','1'),(_binary '\0',_binary '','2024-05-15 12:37:02.077733',16,4,'2024-05-15 12:37:02.077733','4 6\na t c i s w','acis\nacit\naciw\nacst\nacsw\nactw\naist\naisw\naitw\nastw\ncist\ncisw\ncitw\nistw'),(_binary '\0',_binary '','2024-05-15 12:37:02.105274',17,5,'2024-05-15 12:37:02.105274','4 2 0 0 8\n0 2\n3 4\n5 6\n7 8\n4 4 4 1 3 3 3 2','0\n0\n3\n0\n0\n8\n6\n3'),(_binary '\0',_binary '','2024-05-15 12:37:02.121208',18,5,'2024-05-15 12:37:02.121208','3 3 1 1 9\n1 2 3\n4 0 5\n6 7 8\n1 3 2 2 4 4 1 1 3','0\n0\n0\n3\n0\n1\n0\n6\n0'),(_binary '\0',_binary '','2024-05-15 12:37:02.136326',19,5,'2024-05-15 12:37:02.136326','2 2 0 0 16\n0 2\n3 4\n4 4 4 4 1 1 1 1 3 3 3 3 2 2 2 2','0\n0\n0\n0'),(_binary '\0',_binary '','2024-05-15 12:37:02.155652',20,5,'2024-05-15 12:37:02.155652','3 3 0 0 16\n0 1 2\n3 4 5\n6 7 8\n4 4 1 1 3 3 2 2 4 4 1 1 3 3 2 2','0\n0\n0\n6\n0\n8\n0\n2\n0\n8\n0\n2\n0\n8\n0\n2'),(_binary '\0',_binary '','2024-05-15 12:37:02.185289',21,6,'2024-05-15 12:37:02.185289','4 8 2\n1 2 4\n1 3 2\n1 4 7\n2 1 1\n2 3 5\n3 1 2\n3 4 4\n4 2 3','10'),(_binary '\0',_binary '\0','2024-05-15 12:37:02.203642',22,6,'2024-05-15 12:37:02.203642','10 24 2\n9 5 9 \n7 9 2 \n8 1 7 \n7 10 2 \n6 1 6 \n10 6 5 \n5 1 6 \n5 7 9 \n3 8 9 \n4 10 7 \n8 10 6 \n7 2 6 \n4 3 1 \n10 9 9 \n8 5 9 \n2 1 4 \n3 9 6 \n1 2 4 \n8 3 3 \n1 8 4 \n8 7 8 \n6 8 5 \n1 4 2 \n2 3 4','29'),(_binary '\0',_binary '\0','2024-05-15 12:37:02.221082',23,6,'2024-05-15 12:37:02.221082','7 21 4\n1 2 7 \n7 3 7 \n6 5 1 \n6 7 9 \n3 6 9 \n2 3 4 \n3 5 8 \n2 7 5 \n4 1 3 \n1 3 3 \n6 2 8 \n2 5 1 \n4 3 1 \n5 4 2 \n5 1 1 \n3 2 6 \n6 3 3 \n5 7 8 \n2 6 6 \n1 6 3 \n3 1 9','28'),(_binary '\0',_binary '','2024-05-15 12:37:02.254892',24,7,'2024-05-15 12:37:02.254892','4\n14 9 12 10\n1 11 5 4\n7 15 2 13\n6 3 16 8','4'),(_binary '\0',_binary '','2024-05-15 12:37:02.284428',25,8,'2024-05-15 12:37:02.284428','5 4 2\n1 2 3\n1 2 6\n2 3 2\n3 4 4\n3 5 3\n5\n4\n6 9 2\n2 3 1\n1 2 1\n1 3 3\n2 4 4\n2 5 5\n3 4 3\n3 6 2\n4 5 4\n4 6 3\n5 6 7\n5\n6','4 5\n6'),(_binary '\0',_binary '\0','2024-05-15 12:37:02.299634',26,8,'2024-05-15 12:37:02.299634','1\n6 7 3\n1 4 5\n1 2 1\n2 4 2\n2 3 2\n3 5 3\n4 5 3\n5 6 4\n2 6 9\n5\n3\n6','5 6'),(_binary '\0',_binary '\0','2024-05-15 12:37:02.316446',27,8,'2024-05-15 12:37:02.316446','1\n5 5 2\n1 2 3\n1 4 3\n4 5 3\n1 2 2\n2 3 2\n3 5 2\n3\n5','3 5'),(_binary '\0',_binary '\0','2024-05-15 12:37:02.331701',28,8,'2024-05-15 12:37:02.331701','5 4 2\n1 2 3\n1 2 1\n2 3 1\n3 4 1\n2 5 1\n4 5','4'),(_binary '\0',_binary '\0','2024-05-15 12:37:02.346146',29,8,'2024-05-15 12:37:02.346146','3\n5 5 1\n1 3 5\n1 2 1\n2 4 2\n2 3 2\n3 5 3\n4 5 3\n5\n5 5 1\n1 4 5\n1 2 1\n2 4 2\n2 3 2\n3 5 3\n4 5 3\n5\n6 7 3\n1 4 5\n1 2 1\n2 4 2\n2 3 2\n3 5 3\n4 5 3\n5 6 4\n2 6 9\n5\n3\n6','5\n5\n5 6'),(_binary '\0',_binary '\0','2024-05-15 12:37:02.363295',30,8,'2024-05-15 12:37:02.363295','1\n4 4 1\n1 1 4\n1 2 1\n1 4 3\n4 3 1\n2 3 1\n4','4'),(_binary '\0',_binary '','2024-05-15 12:37:02.391185',31,9,'2024-05-15 12:37:02.391185','5 5\n10 25 7 8 13\n68 24 -78 63 32\n12 -69 100 -29 -25\n-16 -22 -57 -33 99\n7 -76 -11 77 15','319'),(_binary '\0',_binary '\0','2024-05-15 12:37:02.405866',32,9,'2024-05-15 12:37:02.405866','3 5\n0 0 0 0 5\n-10000 0 5 5 5\n1000 0 0 0 1000','1020'),(_binary '\0',_binary '\0','2024-05-15 12:37:02.425489',33,9,'2024-05-15 12:37:02.425489','5 5\n-1 -1 -1 -1 -1\n-1 -1 -1 -1 -1\n-1 -1 -1 -1 -1\n-1 -1 -1 -1 -1\n-1 -1 -1 -1 -1','-9'),(_binary '\0',_binary '\0','2024-05-15 12:37:02.442958',34,9,'2024-05-15 12:37:02.442958','9 12\n0 0 0 0 0 0 0 0 0 0 0 1\n0 1 1 1 1 0 0 1 1 1 1 0\n0 0 0 0 0 0 0 1 1 1 1 0\n0 1 1 1 1 0 0 1 1 1 1 0\n0 0 0 0 0 0 0 0 0 0 0 0\n0 1 1 1 1 0 0 1 1 1 1 0\n0 1 1 1 1 0 0 0 0 0 0 0\n0 1 1 1 1 0 0 1 1 1 1 0\n1 0 0 0 0 0 0 0 0 0 0 0','10'),(_binary '\0',_binary '\0','2024-05-15 12:37:02.457989',35,9,'2024-05-15 12:37:02.457989','4 2\n0 0\n0 0\n1 0\n0 0','7'),(_binary '\0',_binary '\0','2024-05-15 12:37:02.473728',36,9,'2024-05-15 12:37:02.473728',' 5 6\n1 1 1 1 1 1\n1 0 0 0 0 1\n1 0 1 1 0 1\n1 0 0 0 0 1\n1 1 1 1 1 1','3'),(_binary '\0',_binary '','2024-05-15 12:37:02.498728',37,10,'2024-05-15 12:37:02.498728','1 1 1 1 1\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0\n1 1 1 1 1\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0\n1 1 1 1 1\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0\n1 1 1 1 1\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0\n1 1 1 1 1\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0','12'),(_binary '\0',_binary '','2024-05-15 12:37:02.512398',38,10,'2024-05-15 12:37:02.512398','1 1 1 1 1\n1 0 0 0 1\n1 0 0 0 1\n1 0 0 0 1\n1 1 1 1 1\n0 0 0 0 0\n0 1 1 1 0\n0 1 0 1 0\n0 1 1 1 0\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0\n0 0 1 0 0\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0\n0 1 1 1 0\n0 1 0 1 0\n0 1 1 1 0\n0 0 0 0 0\n1 1 1 1 1\n1 0 0 0 1\n1 0 0 0 1\n1 0 0 0 1\n1 1 1 1 1','-1'),(_binary '\0',_binary '','2024-05-15 12:37:02.537505',39,10,'2024-05-15 12:37:02.537505','1 1 1 1 1\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0\n1 1 1 1 1\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0\n1 1 1 1 1\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0\n1 1 1 1 1\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0\n1 1 1 1 1','12'),(_binary '\0',_binary '','2024-05-15 12:37:02.551858',40,10,'2024-05-15 12:37:02.551858','1 1 1 1 1\n1 1 1 1 1\n1 1 1 1 1\n1 1 1 1 1\n1 1 1 1 1\n1 1 1 1 1\n1 1 1 1 1\n1 1 1 1 1\n1 1 1 1 1\n1 1 1 1 1\n1 1 1 1 1\n1 1 1 1 1\n1 1 1 1 1\n1 1 1 1 1\n1 1 1 1 1\n1 1 1 1 1\n1 1 1 1 1\n1 1 1 1 1\n1 1 1 1 1\n1 1 1 1 1\n1 1 1 1 1\n1 1 1 1 1\n1 1 1 1 1\n1 1 1 1 1\n1 1 1 1 1','12'),(_binary '\0',_binary '','2024-05-15 12:37:02.566330',41,10,'2024-05-15 12:37:02.566330','0 0 0 1 0\n0 0 0 0 0\n1 0 1 1 1\n0 0 0 1 0\n0 0 1 0 0\n0 1 0 0 0\n1 1 0 0 0\n1 0 0 1 0\n0 1 1 1 0\n0 1 0 1 0\n0 0 1 0 0\n1 0 0 0 0\n0 1 0 0 0\n0 0 1 0 0\n1 1 1 0 0\n1 0 0 0 1\n1 0 0 0 0\n0 0 1 0 1\n0 1 1 0 0\n0 1 0 0 0\n0 0 0 1 0\n1 0 0 0 0\n0 0 1 0 0\n0 1 0 0 1\n0 1 0 0 0','22'),(_binary '\0',_binary '','2024-05-15 12:37:02.582370',42,10,'2024-05-15 12:37:02.582370','0 0 0 0 0\n0 0 0 0 0\n1 0 0 0 1\n0 0 1 0 0\n0 0 1 1 1\n0 1 0 0 1\n0 0 0 0 1\n0 0 0 0 0\n0 0 0 0 0\n0 1 0 0 0\n0 1 0 0 1\n1 0 0 1 0\n0 0 0 1 0\n0 1 1 0 0\n0 1 0 0 0\n1 0 1 0 0\n0 0 0 0 0\n1 0 0 0 0\n0 0 0 1 0\n1 0 0 0 0\n0 0 0 1 0\n0 0 0 0 1\n1 1 0 0 0\n1 0 0 1 1\n1 0 0 0 0','-1'),(_binary '\0',_binary '','2024-05-15 12:37:02.595755',43,10,'2024-05-15 12:37:02.595755','1 1 0 0 0\n0 0 0 0 1\n0 0 1 0 0\n0 0 0 0 0\n0 0 0 0 0\n0 0 1 1 1\n1 0 0 0 0\n0 0 1 0 0\n0 0 1 1 1\n0 0 1 0 0\n0 0 0 0 0\n0 0 1 0 1\n0 0 0 0 0\n0 0 0 1 0\n0 0 1 0 1\n0 0 1 0 0\n1 0 0 0 0\n0 0 1 1 0\n1 0 1 0 0\n0 0 1 0 1\n0 0 1 1 0\n1 1 0 1 1\n0 0 0 0 1\n0 1 0 1 0\n0 1 0 0 0','16'),(_binary '\0',_binary '','2024-05-15 12:37:02.609105',44,10,'2024-05-15 12:37:02.609105','0 0 1 0 0\n0 0 0 0 0\n1 1 0 0 0\n0 0 1 0 0\n1 1 1 0 0\n0 0 0 0 1\n1 0 0 0 0\n0 1 0 0 1\n0 0 0 0 0\n0 1 0 1 0\n1 0 0 0 1\n1 1 1 1 1\n1 1 0 0 0\n0 0 0 1 0\n0 0 0 1 0\n0 0 0 1 1\n0 0 1 0 0\n0 1 1 1 0\n1 0 0 0 0\n0 1 1 0 1\n0 1 0 0 0\n0 0 0 1 0\n1 0 0 0 0\n0 0 0 1 0\n0 0 0 1 0','18'),(_binary '\0',_binary '','2024-05-15 12:37:02.647079',45,11,'2024-05-15 12:37:02.647079','5 6\n1 2 4\n1 3 3\n2 3 1\n2 4 4\n2 5 7\n4 5 1','11'),(_binary '\0',_binary '','2024-05-15 12:37:02.662020',46,11,'2024-05-15 12:37:02.662020','6 7\n1 2 1\n2 3 4\n3 4 4\n4 6 4\n1 5 5\n2 5 2\n5 6 5','13'),(_binary '\0',_binary '','2024-05-15 12:37:02.674824',47,11,'2024-05-15 12:37:02.674824','5 7\n1 2 8\n1 4 10\n2 3 9\n2 4 10\n2 5 1\n3 4 7\n3 5 10','27'),(_binary '\0',_binary '','2024-05-15 12:37:02.699780',48,12,'2024-05-15 12:37:02.699780','1\n4\nE 3\nI 3 1\nE 3\nI 1 2\nE 3\nI 2 4\nE 3\nO','0\n2\n3\n5'),(_binary '\0',_binary '\0','2024-05-15 12:37:02.713276',49,12,'2024-05-15 12:37:02.713276','1\n4\nI 4 2\nI 2 3\nI 1 4\nE 1\nO','6'),(_binary '\0',_binary '\0','2024-05-15 12:37:02.727251',50,12,'2024-05-15 12:37:02.727251','3\n20\nI 16 1\nI 15 9\nI 8 6\nI 5 19\nI 1 17\nI 14 13\nI 9 20\nI 13 19\nI 10 13\nI 20 17\nE 20\nI 19 6\nI 18 12\nI 17 2\nI 4 2\nI 6 17\nE 16\nE 2\nE 4\nE 17\nI 2 18\nE 13\nE 2\nE 6\nE 20\nE 4\nI 3 14\nI 7 10\nE 5\nE 12\nE 12\nI 11 10\nE 3\nE 19\nE 2\nE 11\nE 8\nE 1\nE 4\nE 13\nE 15\nE 15\nE 8\nE 6\nE 19\nE 14\nE 18\nE 11\nE 15\nE 12\nE 8\nE 1\nE 5\nE 5\nE 14\nE 12\nE 11\nE 6\nE 14\nE 15\nE 4\nE 17\nE 16\nE 5\nE 11\nE 13\nE 6\nE 15\nE 15\nE 13\nE 20\nE 20\nE 3\nE 18\nE 8\nE 7\nE 10\nE 19\nE 11\nE 10\nE 7\nE 15\nE 20\nE 14\nE 9\nE 2\nE 15\nE 12\nE 11\nE 14\nE 7\nE 16\nE 8\nE 2\nE 13\nE 16\nE 11\nE 5\nE 7\nE 13\nO','3\n46\n0\n2\n15\n67\n22\n48\n40\n24\n75\n0\n0\n79\n61\n22\n71\n50\n53\n24\n67\n57\n57\n50\n48\n61\n68\n6\n71\n57\n0\n50\n53\n75\n75\n68\n0\n71\n48\n68\n57\n24\n37\n68\n75\n71\n67\n48\n57\n57\n67\n40\n40\n79\n6\n50\n73\n70\n61\n71\n70\n73\n57\n40\n68\n51\n22\n57\n0\n71\n68\n73\n68\n50\n22\n67\n68\n71\n75\n73\n67'),(_binary '\0',_binary '','2024-05-15 15:28:23.173651',52,2,'2024-05-15 15:28:23.173651','5 17','4'),(_binary '\0',_binary '\0','2024-05-15 15:28:23.191430',53,2,'2024-05-15 15:28:23.191430','0 1','1'),(_binary '\0',_binary '\0','2024-05-15 15:28:23.211189',54,2,'2024-05-15 15:28:23.211189','1 15','5'),(_binary '\0',_binary '\0','2024-05-15 15:28:23.228018',55,2,'2024-05-15 15:28:23.228018','1 100000','21'),(_binary '\0',_binary '\0','2024-05-15 15:28:23.244458',56,2,'2024-05-15 15:28:23.244458','3482 45592','637'),(_binary '\0',_binary '\0','2024-05-15 15:28:23.262830',57,2,'2024-05-15 15:28:23.262830','5 35','5'),(_binary '\0',_binary '\0','2024-05-15 15:28:23.279277',58,2,'2024-05-15 15:28:23.279277','10007 98767','2343'),(_binary '\0',_binary '','2024-05-16 00:37:03.280283',59,1,'2024-05-16 00:37:03.280283','2\n10 8 17\n0 0\n1 0\n1 1\n4 2\n4 3\n4 5\n2 4\n3 4\n7 4\n8 4\n9 4\n7 5\n8 5\n9 5\n7 6\n8 6\n9 6\n10 10 1\n5 5 ','5\n1'),(_binary '\0',_binary '','2024-05-16 00:37:03.344467',60,1,'2024-05-16 00:37:03.344467','1\n5 3 6\n0 2\n1 2\n2 2\n3 2\n4 2\n4 0','2'),(_binary '\0',_binary '\0','2024-05-16 00:37:03.360851',61,1,'2024-05-16 00:37:03.360851','1\n5 5 5\n0 1\n1 0\n1 2\n2 1\n1 1','1'),(_binary '\0',_binary '\0','2024-05-16 00:37:03.376936',62,1,'2024-05-16 00:37:03.376936','1\n3 3 6\n0 0\n1 1\n2 2\n0 2\n2 0\n1 2','3'),(_binary '\0',_binary '\0','2024-05-16 00:37:03.396690',63,1,'2024-05-16 00:37:03.396690','3\n1 1 1\n0 0\n5 5 6\n0 0\n0 3\n1 4\n2 3\n3 3\n4 4\n8 8 15\n0 0\n0 1\n0 2\n1 2\n1 3\n1 6\n1 7\n2 3\n3 5\n4 5\n5 7\n6 2\n6 4\n7 5\n7 7','1\n5\n8');
/*!40000 ALTER TABLE `testcase` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-19  6:30:28
