-- MySQL dump 10.13  Distrib 8.0.36, for macos14 (x86_64)
--
-- Host: 127.0.0.1    Database: bankbox
-- ------------------------------------------------------
-- Server version	5.7.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bank_account`
--

DROP TABLE IF EXISTS `bank_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bank_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account` varchar(255) NOT NULL,
  `agency` varchar(255) NOT NULL,
  `balance` decimal(19,2) NOT NULL,
  `bank_name` varchar(255) NOT NULL,
  `pix_key` varchar(255) DEFAULT NULL,
  `type` varchar(255) NOT NULL,
  `owner_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_rv68xsgun33h74ykf8ryn1vxu` (`pix_key`),
  KEY `FKog0hm7djp4sggwl9054534wer` (`owner_id`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bank_account`
--

LOCK TABLES `bank_account` WRITE;
/*!40000 ALTER TABLE `bank_account` DISABLE KEYS */;
INSERT INTO `bank_account` VALUES (1,'71961-2','1754',91.47,'ITAU',NULL,'CHECKING',1),(2,'74251-5','8103',323.50,'NUBANK',NULL,'CHECKING',1),(3,'11603-7','9059',41.94,'ITAU',NULL,'CHECKING',2),(4,'33134-4','8357',653.00,'NUBANK',NULL,'CHECKING',2),(5,'25514-5','9087',785.91,'ITAU',NULL,'CHECKING',3),(6,'36456-4','4926',842.17,'NUBANK',NULL,'CHECKING',3),(7,'22744-7','9402',920.66,'ITAU',NULL,'CHECKING',4),(8,'58371-7','4131',675.91,'NUBANK',NULL,'CHECKING',4),(9,'21735-1','5526',926.35,'ITAU',NULL,'CHECKING',5),(10,'81147-0','8790',332.30,'NUBANK',NULL,'CHECKING',5),(11,'56155-5','5657',185.12,'ITAU',NULL,'CHECKING',6),(12,'26108-1','6463',275.02,'NUBANK',NULL,'CHECKING',6);
/*!40000 ALTER TABLE `bank_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `credit_card`
--

DROP TABLE IF EXISTS `credit_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `credit_card` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `brand` varchar(255) NOT NULL,
  `expiration` varchar(255) NOT NULL,
  `limit` decimal(19,2) NOT NULL,
  `number` varchar(255) NOT NULL,
  `owner_name` varchar(255) NOT NULL,
  `security_number` int(11) NOT NULL,
  `type` varchar(255) NOT NULL,
  `customer_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsuvn58jmqe7oucg43j8g020ss` (`customer_id`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `credit_card`
--

LOCK TABLES `credit_card` WRITE;
/*!40000 ALTER TABLE `credit_card` DISABLE KEYS */;
INSERT INTO `credit_card` VALUES (1,'VISA','2031-01',1000.00,'1234567891231112','Richard Nascimento',933,'VIRTUAL',1),(2,'VISA','2031-06',1374.00,'9749221449541603','Ana Damásio Santana',316,'VIRTUAL',3),(3,'MASTERCARD','2031-06',916.00,'3656827053761783','Ana Damásio Santana',146,'PHYSICAL',3),(4,'VISA','2031-06',703.00,'4268832785017550','Bruno Aranha Duarte',193,'VIRTUAL',4),(5,'MASTERCARD','2031-06',754.00,'7400634089369702','Bruno Aranha Duarte',834,'PHYSICAL',4),(6,'VISA','2031-06',801.00,'1543200112718622','Ana Murta Camargo',961,'VIRTUAL',5),(7,'MASTERCARD','2031-06',952.00,'6958706161741394','Ana Murta Camargo',175,'PHYSICAL',5),(8,'VISA','2031-06',941.00,'7047989772757290','Bruno Damásio Alves',702,'VIRTUAL',6),(9,'MASTERCARD','2031-06',1335.00,'3132958811028650','Bruno Damásio Alves',270,'PHYSICAL',6);
/*!40000 ALTER TABLE `credit_card` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cpf` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_cwtgtb16nebxu54elskdjei66` (`cpf`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'53368907507','Ana Estanislau Floriano','$2a$10$vGxBqaQnJGh6MAM.lJIEueBMyPGeROPLb5cd7hnrHsmuUYqkJMsNi'),(2,'30126075024','Bruno Damásio Queiroz','$2a$10$o7dl3/Txpzr0Rd.D7Jy5P.JzbFOJjDc2ClnRF2E.A3PyxNi4oNova'),(3,'68314469632','Ana Damásio Santana','$2a$10$lcJdjFJiN2KHTemX.5ytg.yzNpK1QIjfq6fH1aZwTtF0c6HBPV0Ae'),(4,'11205690541','Bruno Aranha Duarte','$2a$10$TUCYTaZJNB4LG6la6bgW3eueJEHsEe9R2sLUDslP.FgAgBcQPKlPS'),(5,'28040092949','Ana Murta Camargo','$2a$10$NPlwVM6ZhBwF8yUOznY6sOH44jJfoLzuVm/0M0NIjtIeri8FXUS82'),(6,'75452205820','Bruno Damásio Alves','$2a$10$ua1tF3nbulzZLzE9fC68R.nTxtp3fUPautkRIHXfkvUi9N7l5GyVK');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `performed_at` datetime NOT NULL,
  `type` varchar(255) NOT NULL,
  `value` decimal(19,2) NOT NULL,
  `beneficiary_id` bigint(20) NOT NULL,
  `source_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6csems8ny6n6dxdmdrqsy72dd` (`beneficiary_id`),
  KEY `FKdku22j338n5ehcps7n629kyg0` (`source_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction`
--

LOCK TABLES `transaction` WRITE;
/*!40000 ALTER TABLE `transaction` DISABLE KEYS */;
INSERT INTO `transaction` VALUES (1,'2025-05-18 20:21:04','TRANSFERENCE',36.00,2,1);
/*!40000 ALTER TABLE `transaction` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-18 23:50:00
