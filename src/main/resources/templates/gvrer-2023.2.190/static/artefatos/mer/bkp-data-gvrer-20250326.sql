-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: gvrer
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
-- Dumping data for table `destino`
--

LOCK TABLES `destino` WRITE;
/*!40000 ALTER TABLE `destino` DISABLE KEYS */;
INSERT INTO `destino` VALUES (1,'SENAC','PONTO DE ENCONTRO'),(2,'BARREIRA',NULL),(3,'KM 21','parada para o lanche'),(4,'PRESIDENTE FIGUEIREDO','destino final'),(5,'TROPICAL HOTEL','PONTO DE ENCONTRO'),(6,'PRAIA DA LUA','BANHO'),(7,'CONTRO DAS ÁGUAS','DESTINO FINAL'),(8,'Aeroporto Internacional de Manaus','O início da viagem na capital amazonense, se dará com o receptivo no aeroporto de Manaus e o traslado para o hotel, com o pernoite em Manaus.'),(13,'Pernoite em Manaus','Em hotel 3 a 5 estrelas'),(14,'Museu da Amazônia (MUSA)','(Manhã) Início da viagem na capital do Amazonas'),(15,'centro histórico de Manaus','(Tarde) atrações que datam do século XIV, quando Manaus era chamada de “Paris dos trópicos”, como o famoso Teatro Amazonas, além de uma pausa no mercado municipal para compras de produtos regionais.'),(16,'Povos Indígenas + Boto-Cor-de-Rosa','Dia 5 (Pernoite em Manacapuru) visitar comunidades indígenas como Dessanas, Tukana, Tuyuka, Tatuia, Kubeua e Makuna para experiências culturais com danças, histórias, comida e artesanatos. '),(17,'Manacapuru / Novo Airão','Dia 6 (Pernoite em Novo Airão). (Manhã) Começando a manhã na pousada em Manacapuru, iniciaremos a observação de aves em uma trilha para avistar belas espécies que ocorrem na região, como o Beija- flor-brilho-de-fogo, que sempre aparece nas manhãs. Após a trilha, faremos o check- out na pousada.'),(18,'Novo Airão','Dia 7 - (Pernoite Manaus). (Manhã) Amanhecendo em Novo Airão, tomamos café às 5h30 da manhã e em seguida vamos até o porto onde pegaremos um barco (tipo voadeira) para o Parque Nacional de Anavilhanas, onde vamos explorar o arquipélago em busca de espécies incríveis como o Rabo-de-arame.');
/*!40000 ALTER TABLE `destino` ENABLE KEYS */;
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
INSERT INTO `itinerario_destino` VALUES (1,1,5,1),(2,2,5,2),(3,3,5,3),(4,4,5,4),(5,1,0,5),(6,2,0,6),(7,3,0,7),(17,1,1,8),(18,2,1,13),(19,3,1,14),(20,4,1,15),(21,5,1,16),(22,6,1,17),(23,7,1,18),(24,8,1,4),(25,9,1,7);
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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-26 21:31:29
