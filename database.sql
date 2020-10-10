-- Project M-Ton-Air-Server --
-- BENALI Myriam & NAAJI Dorian --
DROP DATABASE IF EXISTS `mtonairserver`;
CREATE DATABASE `mtonairserver` DEFAULT CHARACTER SET utf8 ;
USE `mtonairserver`;

CREATE TABLE `favorite_station` (
  `id_favorite_station` int(11) NOT NULL AUTO_INCREMENT,
  `station_name` varchar(300) NOT NULL,
  `url` varchar(300) NOT NULL,
  PRIMARY KEY (`id_favorite_station`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Table regroupant les stations mises en favorites par les utilisateurs';

CREATE TABLE `user` (
  `id_user` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `email` varchar(75) NOT NULL,
  `password` varchar(400) NOT NULL,
  `api_key` varchar(45) NOT NULL,
  PRIMARY KEY (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Table gérant les personnes créant un compte sur l''application M'' Ton Air';

CREATE TABLE `user_favorite_station` (
  `id_user_favorite_station` int(11) NOT NULL AUTO_INCREMENT,
  `id_user` int(11) NOT NULL,
  `id_favorite_station` int(11) NOT NULL,
  PRIMARY KEY (`id_user_favorite_station`),
  CONSTRAINT `id_favorite_station_fk` FOREIGN KEY (`id_favorite_station`) REFERENCES `favorite_station` (`id_favorite_station`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `id_user_fk` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Table regroupant les stations favorites en fonction des utilisateurs';
