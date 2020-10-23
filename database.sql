-- Project M-Ton-Air-Server --
-- BENALI Myriam & NAAJI Dorian --

CREATE DATABASE  IF NOT EXISTS `mtonairserver` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `mtonairserver`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: mtonairserver
-- ------------------------------------------------------
-- Server version	5.5.5-10.1.37-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `daily_aqicn_data`
--

DROP TABLE IF EXISTS `daily_aqicn_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `daily_aqicn_data` (
  `id_daily_aqicn_data` int(11) NOT NULL AUTO_INCREMENT,
  `id_station` int(11) NOT NULL,
  `datetime_data` datetime NOT NULL,
  `air_quality` float NOT NULL,
  `pm2_5` float NOT NULL,
  `o3` float NOT NULL,
  `pressure` float NOT NULL,
  `humidity` float NOT NULL,
  `wind` float NOT NULL,
  PRIMARY KEY (`id_station`,`datetime_data`,`id_daily_aqicn_data`),
  UNIQUE KEY `id_daily_aqicn_data_UNIQUE` (`id_daily_aqicn_data`),
  KEY `fk_id_station_idx` (`id_station`),
  CONSTRAINT `fk_id_station` FOREIGN KEY (`id_station`) REFERENCES `station` (`id_station`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Table gathering the information of a station for a given datetime';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `daily_aqicn_data`
--

LOCK TABLES `daily_aqicn_data` WRITE;
/*!40000 ALTER TABLE `daily_aqicn_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `daily_aqicn_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `forecast`
--

DROP TABLE IF EXISTS `forecast`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `forecast` (
  `id_forecast` int(11) NOT NULL AUTO_INCREMENT,
  `id_station` int(11) NOT NULL,
  `id_measure` int(11) NOT NULL,
  `id_date_forecast` date NOT NULL,
  `date_forecasted` datetime NOT NULL,
  `measure_average` float NOT NULL,
  `measure_min` float NOT NULL,
  `measure_max` float NOT NULL,
  PRIMARY KEY (`id_forecast`),
  KEY `fk_id_station_idx` (`id_station`),
  KEY `fk_id_measure_idx` (`id_measure`),
  CONSTRAINT `fk_id_measure` FOREIGN KEY (`id_measure`) REFERENCES `measure` (`id_measure`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_id_station_bis` FOREIGN KEY (`id_station`) REFERENCES `station` (`id_station`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Table gathering forecasts according to station name, date and particulates';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forecast`
--

LOCK TABLES `forecast` WRITE;
/*!40000 ALTER TABLE `forecast` DISABLE KEYS */;
/*!40000 ALTER TABLE `forecast` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `measure`
--

DROP TABLE IF EXISTS `measure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `measure` (
  `id_measure` int(11) NOT NULL AUTO_INCREMENT,
  `measure_name` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id_measure`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 COMMENT='Table gathering useful measures and particulates for forcasting';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `measure`
--

LOCK TABLES `measure` WRITE;
/*!40000 ALTER TABLE `measure` DISABLE KEYS */;
INSERT INTO `measure` VALUES (1,'o3'),(2,'pm10'),(3,'pm25'),(4,'uvi');
/*!40000 ALTER TABLE `measure` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `station`
--

DROP TABLE IF EXISTS `station`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `station` (
  `id_station` int(11) NOT NULL AUTO_INCREMENT,
  `station_name` varchar(300) NOT NULL,
  `country` varchar(100) NOT NULL,
  `region` varchar(100) DEFAULT NULL,
  `city` varchar(100) NOT NULL,
  `url` varchar(300) NOT NULL,
  PRIMARY KEY (`id_station`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Table regroupant les stations';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `station`
--

LOCK TABLES `station` WRITE;
/*!40000 ALTER TABLE `station` DISABLE KEYS */;
/*!40000 ALTER TABLE `station` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id_user` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `email` varchar(75) NOT NULL,
  `password` varchar(400) NOT NULL,
  `api_key` varchar(45) NOT NULL,
  PRIMARY KEY (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Table gérant les personnes créant un compte sur l''application M'' Ton Air';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_favorite_station`
--

DROP TABLE IF EXISTS `user_favorite_station`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_favorite_station` (
  `id_user_favorite_station` int(11) NOT NULL AUTO_INCREMENT,
  `id_user` int(11) NOT NULL,
  `id_station` int(11) NOT NULL,
  PRIMARY KEY (`id_user_favorite_station`),
  KEY `id_station_fk` (`id_station`),
  KEY `id_user_fk` (`id_user`),
  CONSTRAINT `id_station_fk` FOREIGN KEY (`id_station`) REFERENCES `station` (`id_station`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `id_user_fk` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Table regroupant les stations favorites en fonction des utilisateurs';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_favorite_station`
--

LOCK TABLES `user_favorite_station` WRITE;
/*!40000 ALTER TABLE `user_favorite_station` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_favorite_station` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-10-23 12:06:35
