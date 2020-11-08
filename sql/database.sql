-- Project M-Ton-Air-Server --
-- BENALI Myriam & NAAJI Dorian --

DROP DATABASE IF EXISTS `mtonairserver`;
CREATE DATABASE `mtonairserver` DEFAULT CHARACTER SET utf8 ;
USE `mtonairserver`;

CREATE TABLE `station` (
  `id_station` int(11) NOT NULL AUTO_INCREMENT,
  `station_name` varchar(300) NOT NULL,
  `country` varchar(100) NOT NULL,
  `iso2` varchar(3) NOT NULL,
  -- subdivisions are handy cause they allow us to manage region/cities the same way but we lose some information. --
  `subdivision1` varchar(100),
  `subdivision2` varchar(100),
  `subdivision3` varchar(100),
  `url` varchar(300) NOT NULL,
  `latitude` DOUBLE,
  `longitude` DOUBLE,
  PRIMARY KEY (`id_station`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='Table gathering stations';

CREATE TABLE `user` (
  `id_user` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `email` varchar(75) NOT NULL,
  `password` varchar(400) NOT NULL,
  `api_key` varchar(45) NOT NULL,
  PRIMARY KEY (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='Table managing people creating an account on the M'' Ton Air application';

CREATE TABLE `user_favorite_station` (
  `id_user_favorite_station` int(11) NOT NULL AUTO_INCREMENT,
  `id_user` int(11) NOT NULL,
  `id_station` int(11) NOT NULL,
  PRIMARY KEY (`id_user_favorite_station`),
  CONSTRAINT `id_station_fk` FOREIGN KEY (`id_station`) REFERENCES `station` (`id_station`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `id_user_fk` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='Table gathering favorite stations according to users';


CREATE TABLE `daily_aqicn_data` (
  `id_daily_aqicn_data` int(11) NOT NULL AUTO_INCREMENT,
  `id_station` int(11) NOT NULL,
  `datetime_data` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `air_quality` float NOT NULL,
  `no2` float,
  `pm2_5` float,
  `pm10` float,
  `o3` float,
  `pressure` float,
  `humidity` float,
  `wind` float,
  `temperature` float,
  PRIMARY KEY (`id_station`,`datetime_data`,`id_daily_aqicn_data`),
  UNIQUE KEY `id_daily_aqicn_data_UNIQUE` (`id_daily_aqicn_data`),
  CONSTRAINT `fk_id_station` FOREIGN KEY (`id_station`) REFERENCES `station` (`id_station`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='Table gathering the information of a station for a given datetime';

CREATE TABLE `measure` (
  `id_measure` int(11) NOT NULL AUTO_INCREMENT,
  `measure_name` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id_measure`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='Table gathering useful measures and particulates for forcasting';

CREATE TABLE `forecast` (
  `id_forecast` int(11) NOT NULL AUTO_INCREMENT,
  `id_station` int(11) NOT NULL,
  `id_measure` int(11) NOT NULL,
  `id_date_forecast` date NOT NULL,
  `date_forecasted` datetime NOT NULL,
  `measure_average` float,
  `measure_min` float,
  `measure_max` float,
  PRIMARY KEY (`id_forecast`,`id_station`,`id_date_forecast`,`id_measure`),
  CONSTRAINT `fk_id_measure` FOREIGN KEY (`id_measure`) REFERENCES `measure` (`id_measure`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_id_station_bis` FOREIGN KEY (`id_station`) REFERENCES `station` (`id_station`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='Table gathering forecasts according to station name, date and particulates';
