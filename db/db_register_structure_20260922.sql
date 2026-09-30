-- MySQL dump 10.13  Distrib 8.0.46, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: db_register
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `Afiliados`
--

DROP TABLE IF EXISTS `Afiliados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Afiliados` (
  `Afil_id` int NOT NULL AUTO_INCREMENT,
  `Institucion` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci NOT NULL,
  `Nombres` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci NOT NULL,
  `Apellidos` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci NOT NULL,
  `fecha_nac` datetime DEFAULT NULL,
  `Estado` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci NOT NULL COMMENT 'estado abreviado',
  `Referencia` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci NOT NULL,
  `Fecha_ingreso` date DEFAULT NULL,
  `Comentarios` text CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci,
  `correo1` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL COMMENT 'correo electronico',
  `correo2` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `SK` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `Ejecuto` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `capacitacion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci,
  PRIMARY KEY (`Afil_id`),
  UNIQUE KEY `nombre` (`Institucion`,`Nombres`,`Apellidos`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1573 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `C_ESTADO`
--

DROP TABLE IF EXISTS `C_ESTADO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `C_ESTADO` (
  `IDESTADO` int NOT NULL,
  `ABREVIADO` varchar(5) NOT NULL,
  `NOMBRE` varchar(45) NOT NULL,
  PRIMARY KEY (`IDESTADO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `C_TIPOAFILIACION`
--

DROP TABLE IF EXISTS `C_TIPOAFILIACION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `C_TIPOAFILIACION` (
  `IDTIPOAFILIACION` int NOT NULL,
  `TIPO` varchar(15) NOT NULL,
  `DESCRIPCION` varchar(45) NOT NULL,
  PRIMARY KEY (`IDTIPOAFILIACION`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `C_TIPODATOCONTACTO`
--

DROP TABLE IF EXISTS `C_TIPODATOCONTACTO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `C_TIPODATOCONTACTO` (
  `IDTIPODATOCONTACTO` int NOT NULL,
  `TIPOCONTACTO` varchar(15) NOT NULL,
  `DESCRIPCION` varchar(45) NOT NULL,
  PRIMARY KEY (`IDTIPODATOCONTACTO`),
  UNIQUE KEY `IDTIPOCONTACTO_UNIQUE` (`IDTIPODATOCONTACTO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `C_TIPOIMAGENDOCUMENTO`
--

DROP TABLE IF EXISTS `C_TIPOIMAGENDOCUMENTO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `C_TIPOIMAGENDOCUMENTO` (
  `IDTIPOIMAGENDOCUMENTO` int NOT NULL,
  `TIPO` varchar(15) NOT NULL,
  `DESCRIPCION` varchar(50) NOT NULL,
  `FECHAINICIO` date NOT NULL,
  `FECHAFIN` date DEFAULT NULL,
  PRIMARY KEY (`IDTIPOIMAGENDOCUMENTO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Registros`
--

DROP TABLE IF EXISTS `Registros`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Registros` (
  `reg_id` int NOT NULL AUTO_INCREMENT,
  `fk_afiliados` int NOT NULL,
  `Institucion` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `categoria` varchar(90) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `Distintivo` varchar(30) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `estado` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `Ref_legal` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `Fecha_ini` date DEFAULT NULL,
  `Fecha_fin` date DEFAULT NULL,
  `Fecha_alta` date DEFAULT NULL,
  `Fotografia` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `fk_credencial` int DEFAULT NULL,
  `Ejecuto` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`reg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2839 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `T_AFICIONADO`
--

DROP TABLE IF EXISTS `T_AFICIONADO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `T_AFICIONADO` (
  `IDAFICIONADO` int NOT NULL AUTO_INCREMENT,
  `IDPERSONA` int NOT NULL,
  `INDICATIVO` varchar(10) DEFAULT NULL,
  `FECHAINICIO` date DEFAULT NULL,
  `FECHAFIN` date DEFAULT NULL,
  `IDIMAGEN` int DEFAULT NULL,
  `MODIFIED_AT` datetime NOT NULL,
  PRIMARY KEY (`IDAFICIONADO`),
  KEY `fk_T_LICENCIA_T_PERSONA1_idx` (`IDPERSONA`),
  KEY `fk_T_LICENCIA_T_IMAGEN1_idx` (`IDIMAGEN`),
  CONSTRAINT `fk_T_LICENCIA_T_IMAGEN1` FOREIGN KEY (`IDIMAGEN`) REFERENCES `T_IMAGEN` (`IDIMAGEN`),
  CONSTRAINT `fk_T_LICENCIA_T_PERSONA1` FOREIGN KEY (`IDPERSONA`) REFERENCES `T_PERSONA` (`IDPERSONA`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `T_AFILIACION`
--

DROP TABLE IF EXISTS `T_AFILIACION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `T_AFILIACION` (
  `IDAFILIACION` int NOT NULL AUTO_INCREMENT,
  `IDPERSONA` int NOT NULL,
  `IDESTADO` int NOT NULL,
  `IDTIPOAFILIACION` int NOT NULL,
  `FECHAINICIO` date NOT NULL,
  `FECHAFIN` date DEFAULT NULL,
  `VITALICIA` tinyint NOT NULL DEFAULT '0',
  `DELETED` tinyint DEFAULT NULL,
  `MODIFIED_AT` datetime NOT NULL,
  PRIMARY KEY (`IDAFILIACION`),
  KEY `fk_T_AFILIACION_T_PERSONA1_idx` (`IDPERSONA`),
  KEY `fk_T_AFILIACION_C_ESTADO1_idx` (`IDESTADO`),
  KEY `fk_T_AFILIACION_C_TIPOAFILIACION1_idx` (`IDTIPOAFILIACION`),
  CONSTRAINT `fk_T_AFILIACION_C_ESTADO1` FOREIGN KEY (`IDESTADO`) REFERENCES `C_ESTADO` (`IDESTADO`),
  CONSTRAINT `fk_T_AFILIACION_C_TIPOAFILIACION1` FOREIGN KEY (`IDTIPOAFILIACION`) REFERENCES `C_TIPOAFILIACION` (`IDTIPOAFILIACION`),
  CONSTRAINT `fk_T_AFILIACION_T_PERSONA1` FOREIGN KEY (`IDPERSONA`) REFERENCES `T_PERSONA` (`IDPERSONA`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `T_ASPIRANTE`
--

DROP TABLE IF EXISTS `T_ASPIRANTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `T_ASPIRANTE` (
  `IDASPIRANTE` int NOT NULL AUTO_INCREMENT,
  `IDPERSONA` int NOT NULL,
  `IDESTADO` int NOT NULL,
  `CONTADORESTADO` int NOT NULL,
  `FECHAINICIO` date NOT NULL,
  `FECHAFIN` date DEFAULT NULL,
  PRIMARY KEY (`IDASPIRANTE`),
  KEY `fk_T_ASPIRANTE_T_PERSONA1_idx` (`IDPERSONA`),
  KEY `fk_T_ASPIRANTE_C_ESTADO1_idx` (`IDESTADO`),
  CONSTRAINT `fk_T_ASPIRANTE_C_ESTADO1` FOREIGN KEY (`IDESTADO`) REFERENCES `C_ESTADO` (`IDESTADO`),
  CONSTRAINT `fk_T_ASPIRANTE_T_PERSONA1` FOREIGN KEY (`IDPERSONA`) REFERENCES `T_PERSONA` (`IDPERSONA`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `T_DATOCONTACTO`
--

DROP TABLE IF EXISTS `T_DATOCONTACTO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `T_DATOCONTACTO` (
  `IDDATOCONTACTO` int NOT NULL AUTO_INCREMENT,
  `IDPERSONA` int NOT NULL,
  `IDTIPODATOCONTACTO` int NOT NULL,
  `DATO` varchar(100) NOT NULL,
  `INICIO` date DEFAULT NULL,
  `FIN` date DEFAULT NULL,
  PRIMARY KEY (`IDDATOCONTACTO`),
  KEY `fk_T_DATOCONTACTO_T_PERSONA1_idx` (`IDPERSONA`),
  KEY `fk_T_DATOCONTACTO_C_TIPODATOCONTACTO1_idx` (`IDTIPODATOCONTACTO`),
  CONSTRAINT `fk_T_DATOCONTACTO_C_TIPODATOCONTACTO1` FOREIGN KEY (`IDTIPODATOCONTACTO`) REFERENCES `C_TIPODATOCONTACTO` (`IDTIPODATOCONTACTO`),
  CONSTRAINT `fk_T_DATOCONTACTO_T_PERSONA1` FOREIGN KEY (`IDPERSONA`) REFERENCES `T_PERSONA` (`IDPERSONA`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `T_DOMICILIO`
--

DROP TABLE IF EXISTS `T_DOMICILIO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `T_DOMICILIO` (
  `IDDOMICILIO` int NOT NULL AUTO_INCREMENT,
  `IDPERSONA` int NOT NULL,
  `CP` varchar(5) DEFAULT NULL,
  `DOMICILIO` varchar(45) DEFAULT NULL,
  `ENTIDADFED` varchar(45) DEFAULT NULL,
  `MUNICIPIO` varchar(45) DEFAULT NULL,
  `LOCALIDAD` varchar(45) DEFAULT NULL,
  `PAIS` varchar(45) NOT NULL,
  PRIMARY KEY (`IDDOMICILIO`),
  KEY `fk_T_DOMICILIO_T_PERSONA1_idx` (`IDPERSONA`),
  CONSTRAINT `fk_T_DOMICILIO_T_PERSONA1` FOREIGN KEY (`IDPERSONA`) REFERENCES `T_PERSONA` (`IDPERSONA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `T_IMAGEN`
--

DROP TABLE IF EXISTS `T_IMAGEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `T_IMAGEN` (
  `IDIMAGEN` int NOT NULL AUTO_INCREMENT,
  `IDPERSONA` int DEFAULT NULL,
  `IDAFILIACION` int DEFAULT NULL,
  `UUID` varchar(45) NOT NULL,
  `IDTIPOIMAGENDOCUMENTO` int NOT NULL,
  PRIMARY KEY (`IDIMAGEN`),
  KEY `fk_T_IMAGEN_T_PERSONA1_idx` (`IDPERSONA`),
  KEY `fk_T_IMAGEN_C_TIPOIMAGENDOCUMENTO1_idx` (`IDTIPOIMAGENDOCUMENTO`),
  KEY `fk_T_IMAGEN_T_AFILIACION1_idx` (`IDAFILIACION`),
  CONSTRAINT `fk_T_IMAGEN_C_TIPOIMAGENDOCUMENTO1` FOREIGN KEY (`IDTIPOIMAGENDOCUMENTO`) REFERENCES `C_TIPOIMAGENDOCUMENTO` (`IDTIPOIMAGENDOCUMENTO`),
  CONSTRAINT `fk_T_IMAGEN_T_AFILIACION1` FOREIGN KEY (`IDAFILIACION`) REFERENCES `T_AFILIACION` (`IDAFILIACION`),
  CONSTRAINT `fk_T_IMAGEN_T_PERSONA1` FOREIGN KEY (`IDPERSONA`) REFERENCES `T_PERSONA` (`IDPERSONA`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `T_PERSONA`
--

DROP TABLE IF EXISTS `T_PERSONA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `T_PERSONA` (
  `IDPERSONA` int NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(250) NOT NULL,
  `PRIMERAPELLIDO` varchar(250) NOT NULL,
  `SEGUNDOAPELLIDO` varchar(250) DEFAULT NULL,
  `FECNAC` date DEFAULT NULL,
  PRIMARY KEY (`IDPERSONA`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `V_ELEGIBLE_IDBADGE`
--

DROP TABLE IF EXISTS `V_ELEGIBLE_IDBADGE`;
/*!50001 DROP VIEW IF EXISTS `V_ELEGIBLE_IDBADGE`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `V_ELEGIBLE_IDBADGE` AS SELECT 
 1 AS `IDPERSONA`,
 1 AS `IDAFILIACION`,
 1 AS `IDAFICIONADO`,
 1 AS `IDASPIRANTE`,
 1 AS `IDIMAGEN`*/;
SET character_set_client = @saved_cs_client;

--
-- Dumping routines for database 'db_register'
--

--
-- Final view structure for view `V_ELEGIBLE_IDBADGE`
--

/*!50001 DROP VIEW IF EXISTS `V_ELEGIBLE_IDBADGE`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`db_register_user`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `V_ELEGIBLE_IDBADGE` AS select `persona`.`IDPERSONA` AS `IDPERSONA`,`afiliacion`.`IDAFILIACION` AS `IDAFILIACION`,`aficionado`.`IDAFICIONADO` AS `IDAFICIONADO`,`aspirante`.`IDASPIRANTE` AS `IDASPIRANTE`,`imagen`.`IDIMAGEN` AS `IDIMAGEN` from ((((`T_PERSONA` `persona` left join `T_AFILIACION` `afiliacion` on((`afiliacion`.`IDPERSONA` = `persona`.`IDPERSONA`))) left join `T_AFICIONADO` `aficionado` on((`aficionado`.`IDPERSONA` = `persona`.`IDPERSONA`))) left join `T_ASPIRANTE` `aspirante` on((`aspirante`.`IDPERSONA` = `persona`.`IDPERSONA`))) left join `T_IMAGEN` `imagen` on(((`imagen`.`IDPERSONA` = `persona`.`IDPERSONA`) and (`imagen`.`IDTIPOIMAGENDOCUMENTO` = 1)))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-22 10:10:13
