-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: gvrer
-- ------------------------------------------------------
-- Server version	8.3.0

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
-- Dumping data for table `bairro`
--

LOCK TABLES `bairro` WRITE;
/*!40000 ALTER TABLE `bairro` DISABLE KEYS */;
INSERT INTO `bairro` VALUES (1,'CHAPADA',1),(2,'CENTRO',2),(3,'São Francisco',1);
/*!40000 ALTER TABLE `bairro` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cidade`
--

LOCK TABLES `cidade` WRITE;
/*!40000 ALTER TABLE `cidade` DISABLE KEYS */;
INSERT INTO `cidade` VALUES (1,'MANAUS',NULL,NULL,1),(2,'RIO DE JANEIRO',NULL,NULL,2);
/*!40000 ALTER TABLE `cidade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `destino`
--

LOCK TABLES `destino` WRITE;
/*!40000 ALTER TABLE `destino` DISABLE KEYS */;
INSERT INTO `destino` VALUES (1,'SENAC','PONTO DE ENCONTRO'),(2,'BARREIRA',NULL),(3,'KM 21','parada para o lanche'),(4,'PRESIDENTE FIGUEIREDO','destino final'),(5,'TROPICAL HOTEL','PONTO DE ENCONTRO'),(6,'PRAIA DA LUA','BANHO'),(7,'CONTRO DAS ÁGUAS','DESTINO FINAL'),(8,'Aeroporto Internacional de Manaus','O início da viagem na capital amazonense, se dará com o receptivo no aeroporto de Manaus e o traslado para o hotel, com o pernoite em Manaus.'),(13,'Pernoite em Manaus','Em hotel 3 a 5 estrelas'),(14,'Museu da Amazônia (MUSA)','(Manhã) Início da viagem na capital do Amazonas'),(15,'centro histórico de Manaus','(Tarde) atrações que datam do século XIV, quando Manaus era chamada de “Paris dos trópicos”, como o famoso Teatro Amazonas, além de uma pausa no mercado municipal para compras de produtos regionais.'),(16,'Povos Indígenas + Boto-Cor-de-Rosa','Dia 5 (Pernoite em Manacapuru) visitar comunidades indígenas como Dessanas, Tukana, Tuyuka, Tatuia, Kubeua e Makuna para experiências culturais com danças, histórias, comida e artesanatos. '),(17,'Manacapuru / Novo Airão','Dia 6 (Pernoite em Novo Airão). (Manhã) Começando a manhã na pousada em Manacapuru, iniciaremos a observação de aves em uma trilha para avistar belas espécies que ocorrem na região, como o Beija- flor-brilho-de-fogo, que sempre aparece nas manhãs. Após a trilha, faremos o check- out na pousada.'),(18,'Novo Airão','Dia 7 - (Pernoite Manaus). (Manhã) Amanhecendo em Novo Airão, tomamos café às 5h30 da manhã e em seguida vamos até o porto onde pegaremos um barco (tipo voadeira) para o Parque Nacional de Anavilhanas, onde vamos explorar o arquipélago em busca de espécies incríveis como o Rabo-de-arame.');
/*!40000 ALTER TABLE `destino` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `endereco`
--

LOCK TABLES `endereco` WRITE;
/*!40000 ALTER TABLE `endereco` DISABLE KEYS */;
INSERT INTO `endereco` VALUES (1,'288','Ponto comercial','Px Amazonas Shopping',1,1),(2,'285','Estacionamento','Px CEI SENAC Chapada',1,1),(3,NULL,'até 99998 - lado par',NULL,2,3),(4,NULL,'até 164/165',NULL,3,2);
/*!40000 ALTER TABLE `endereco` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `estado`
--

LOCK TABLES `estado` WRITE;
/*!40000 ALTER TABLE `estado` DISABLE KEYS */;
INSERT INTO `estado` VALUES (1,'AMAZONAS','AM',1),(2,'RIO DE JANEIRO','RJ',1);
/*!40000 ALTER TABLE `estado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fisica`
--

LOCK TABLES `fisica` WRITE;
/*!40000 ALTER TABLE `fisica` DISABLE KEYS */;
INSERT INTO `fisica` VALUES (1,'RENNAN GODOI','DOS ÍTALOS E MARLISONS',NULL,NULL,NULL,3),(2,'JOAO DERY',NULL,NULL,NULL,NULL,2),(3,'PF 1',NULL,NULL,NULL,NULL,4),(4,'PF 2','SOBRENOME 2',NULL,NULL,NULL,6);
/*!40000 ALTER TABLE `fisica` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `itinerario`
--

LOCK TABLES `itinerario` WRITE;
/*!40000 ALTER TABLE `itinerario` DISABLE KEYS */;
INSERT INTO `itinerario` VALUES (0,'Encontro das Águas',0,'parada para comprar gelo',NULL,'2025-04-15 04:00:00',NULL,NULL,NULL,3),(1,'TOUR IMERSÃO AMAZÔNICA',0,'Cultura amazônica e apreciar a floresta amazônica e sua biodiversidade',NULL,NULL,NULL,NULL,NULL,4),(2,'parada pusa',NULL,'pusa de viajem km 200',NULL,NULL,NULL,NULL,NULL,0),(3,'KM12',0,'PAUSA CAFE DA MANHA',NULL,'2025-05-26 04:00:00','08:00:00','2025-05-26 04:00:00','264:00:00',0),(4,'KM15',0,'NANANA',NULL,NULL,NULL,NULL,NULL,0),(5,'FESTA DO CUPUACÚ',NULL,NULL,NULL,NULL,NULL,NULL,NULL,3);
/*!40000 ALTER TABLE `itinerario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `itinerario_destino`
--

LOCK TABLES `itinerario_destino` WRITE;
/*!40000 ALTER TABLE `itinerario_destino` DISABLE KEYS */;
INSERT INTO `itinerario_destino` VALUES (1,1,5,1,NULL),(2,2,5,2,NULL),(3,3,5,3,NULL),(4,4,5,4,NULL),(5,1,0,5,NULL),(6,2,0,6,NULL),(7,3,0,7,NULL),(17,1,1,8,NULL),(18,2,1,13,NULL),(19,3,1,14,NULL),(20,4,1,15,NULL),(21,7,1,16,NULL),(22,8,1,17,NULL),(23,9,1,18,NULL),(24,5,1,4,NULL),(25,6,1,7,NULL);
/*!40000 ALTER TABLE `itinerario_destino` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `motivodaviagem`
--

LOCK TABLES `motivodaviagem` WRITE;
/*!40000 ALTER TABLE `motivodaviagem` DISABLE KEYS */;
INSERT INTO `motivodaviagem` VALUES (1,'INTERCÂMBIO'),(2,'EVENTO'),(3,'LAZER'),(4,'PASSEIO');
/*!40000 ALTER TABLE `motivodaviagem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `pais`
--

LOCK TABLES `pais` WRITE;
/*!40000 ALTER TABLE `pais` DISABLE KEYS */;
INSERT INTO `pais` VALUES (1,'BRASIL','BRA');
/*!40000 ALTER TABLE `pais` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `pessoa`
--

LOCK TABLES `pessoa` WRITE;
/*!40000 ALTER TABLE `pessoa` DISABLE KEYS */;
INSERT INTO `pessoa` VALUES (1,'1999-10-24 04:00:00'),(2,'1998-07-05 04:00:00'),(3,'2000-05-19 04:00:00'),(4,'2000-01-01 04:00:00'),(5,'2000-01-02 04:00:00'),(6,'2000-01-02 04:00:00');
/*!40000 ALTER TABLE `pessoa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `rua`
--

LOCK TABLES `rua` WRITE;
/*!40000 ALTER TABLE `rua` DISABLE KEYS */;
INSERT INTO `rua` VALUES (1,'69088051','darcy vargas',1),(2,'20030020','Avenida Presidente Wilson',2),(3,'69079265','Avenida Paraíba',3);
/*!40000 ALTER TABLE `rua` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-03 20:58:56
