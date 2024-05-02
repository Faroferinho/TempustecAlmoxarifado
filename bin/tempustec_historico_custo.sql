-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: tempustec
-- ------------------------------------------------------
-- Server version	8.0.36

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
-- Table structure for table `historico_custo`
--

DROP TABLE IF EXISTS `historico_custo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `historico_custo` (
  `ID_History` int NOT NULL AUTO_INCREMENT,
  `Date` int NOT NULL,
  `Assembly` int NOT NULL,
  `Cost` decimal(16,2) NOT NULL,
  PRIMARY KEY (`ID_History`),
  KEY `Date` (`Date`),
  CONSTRAINT `historico_custo_ibfk_1` FOREIGN KEY (`Date`) REFERENCES `quinzena` (`ID_Fortnight`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historico_custo`
--

LOCK TABLES `historico_custo` WRITE;
/*!40000 ALTER TABLE `historico_custo` DISABLE KEYS */;
INSERT INTO `historico_custo` VALUES (1,1,1,5494.89),(2,1,2,4900.00),(3,1,3,0.00),(4,1,4,0.00),(5,1,5,6454.31),(6,1,6,0.00),(7,1,7,141.52),(8,1,8,0.00),(9,1,9,0.00),(10,1,10,0.00),(11,1,11,0.00),(12,1,12,0.00),(13,1,13,48.20),(14,1,14,0.00),(15,1,15,0.00),(16,1,19,124.22),(17,1,20,0.00),(18,1,21,0.00),(19,2,1,5900.59),(20,2,2,6800.00),(21,2,3,0.00),(22,2,4,0.00),(23,2,5,7023.80),(24,2,6,0.00),(25,2,7,141.52),(26,2,8,0.00),(27,2,9,0.00),(28,2,10,0.00),(29,2,11,0.00),(30,2,12,0.00),(31,2,13,48.20),(32,2,14,0.00),(33,2,15,0.00),(34,2,19,344.38),(35,2,20,2245.26),(36,2,21,0.00);
/*!40000 ALTER TABLE `historico_custo` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-04 10:08:20
