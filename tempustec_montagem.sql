-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: tempustec
-- ------------------------------------------------------
-- Server version	8.0.35

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
-- Table structure for table `montagem`
--

DROP TABLE IF EXISTS `montagem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `montagem` (
  `ID_Montagem` int NOT NULL AUTO_INCREMENT,
  `ISO` varchar(15) NOT NULL,
  `Description` varchar(300) DEFAULT NULL,
  `Company` varchar(30) DEFAULT NULL,
  `Image` varchar(30) DEFAULT NULL,
  `Cost` decimal(16,2) DEFAULT NULL,
  PRIMARY KEY (`ID_Montagem`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `montagem`
--

LOCK TABLES `montagem` WRITE;
/*!40000 ALTER TABLE `montagem` DISABLE KEYS */;
INSERT INTO `montagem` VALUES (1,'Uso Interno','Produtos Internos da Tempustec','Tempustec',NULL,3122.71),(2,'Combustivel','Valor Gasto em Combustivel','Tempustec',NULL,4900.00),(3,'OS 2720','Router CNC','Tempustec',NULL,0.00),(4,'OS 2846','Desenho do Gradil NR12 embaladora L1 e L2','Electrolux',NULL,0.00),(5,'OS 2858','Montadora de filtros d\'agua','G. Marinheiro',NULL,3957.05),(6,'OS 2848','Equipamentos de Teste de vácuo Motosserra','Husqvarna',NULL,0.00),(7,'OS 2860','Montadora manual distribuidor Nazaré','Electrolux',NULL,141.52),(8,'OS 2827','Cabine de teste de funcionamento motosserras','Husqvarna',NULL,0.00),(9,'OS 2838','Sistema de controle leitura de Qr Code fogões','Electrolux',NULL,0.00),(10,'OS 2855','50 suportes para compressores VHG','Tecumseh',NULL,0.00),(11,'OS 2850','Posto de Retrabalho Limpeza L1','Electrolux',NULL,0.00),(12,'OS 2861','Celula de Pesagem de Oleo da linha 6','Tecumseh',NULL,0.00),(13,'OS 2835','Transportador de Tanques linha 1','Electrolux',NULL,48.20),(14,'OS 2857','Posto de retrabalho Limpeza L1 (Elétrica)','Electrolux',NULL,0.00),(15,'OS 2771','9 Painéis do teste funcional L1','Electrolux',NULL,0.00),(19,'OS 2901','Maquina Semi-Automatica de Montagem de Pipas','W. J. L. M.',NULL,121.82),(20,'OS 2900','2 (duas) Linhas de Montagem Semi-Automatica','Husqvarna',NULL,0.00),(21,'OS 2887','Equipamento de Colagem Cinta - Freio','Colormaq',NULL,0.00);
/*!40000 ALTER TABLE `montagem` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-06  9:36:39
