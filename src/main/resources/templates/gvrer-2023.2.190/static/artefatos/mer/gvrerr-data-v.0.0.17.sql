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
-- Dumping data for table `bairro`
--

LOCK TABLES `bairro` WRITE;
/*!40000 ALTER TABLE `bairro` DISABLE KEYS */;
/*!40000 ALTER TABLE `bairro` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `categoriaitem`
--

LOCK TABLES `categoriaitem` WRITE;
/*!40000 ALTER TABLE `categoriaitem` DISABLE KEYS */;
/*!40000 ALTER TABLE `categoriaitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cidade`
--

LOCK TABLES `cidade` WRITE;
/*!40000 ALTER TABLE `cidade` DISABLE KEYS */;
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
/*!40000 ALTER TABLE `endereco` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `especialidade`
--

LOCK TABLES `especialidade` WRITE;
/*!40000 ALTER TABLE `especialidade` DISABLE KEYS */;
/*!40000 ALTER TABLE `especialidade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `especialidadedoitem`
--

LOCK TABLES `especialidadedoitem` WRITE;
/*!40000 ALTER TABLE `especialidadedoitem` DISABLE KEYS */;
INSERT INTO `especialidadedoitem` VALUES (1,'COMIDA PORTUGUESA'),(2,'BACALHAO DO PORTO'),(3,'BATATA RUSTICA'),(4,'BACALHAO A PORTUGUESA');
/*!40000 ALTER TABLE `especialidadedoitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `estado`
--

LOCK TABLES `estado` WRITE;
/*!40000 ALTER TABLE `estado` DISABLE KEYS */;
/*!40000 ALTER TABLE `estado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fisica`
--

LOCK TABLES `fisica` WRITE;
/*!40000 ALTER TABLE `fisica` DISABLE KEYS */;
INSERT INTO `fisica` VALUES (1,'Juan','Cabral','90765432100','456789123','123456789012',1,3,0),(2,'Teodor ','Sales','27483956130',NULL,NULL,2,1,0);
/*!40000 ALTER TABLE `fisica` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `funcaogrupo`
--

LOCK TABLES `funcaogrupo` WRITE;
/*!40000 ALTER TABLE `funcaogrupo` DISABLE KEYS */;
INSERT INTO `funcaogrupo` VALUES (1,'Administrador');
/*!40000 ALTER TABLE `funcaogrupo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `genero`
--

LOCK TABLES `genero` WRITE;
/*!40000 ALTER TABLE `genero` DISABLE KEYS */;
INSERT INTO `genero` VALUES (1,'Agênero'),(2,'Aliagênero'),(3,'Andrógine'),(4,'Autigênero'),(5,'Bigênero'),(6,'Cisgênero'),(7,'Demiboy'),(8,'Demigirl'),(9,'Gênero-fluido'),(10,'Gênero queer'),(11,'Homem trans'),(12,'Intergênero'),(13,'Maverique'),(14,'Mulher trans'),(15,'Neurogênero'),(16,'Não-binárie'),(17,'Novigênero'),(18,'Pangênero'),(19,'Transfeminina'),(20,'Transgênero'),(21,'Transmasculino'),(22,'Trigênero'),(23,'Two-Spirit'),(24,'Xenogênero');
/*!40000 ALTER TABLE `genero` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `grupo`
--

LOCK TABLES `grupo` WRITE;
/*!40000 ALTER TABLE `grupo` DISABLE KEYS */;
INSERT INTO `grupo` VALUES (1,'Viagem para Curitiba','viagem-para-curitiba/KSHS58R646/grupo/gvrer.com.br','2025-04-01 00:00:00','2025-04-08 00:00:00','jhswdygfuakywefgausydgfuasgydfausdgyfiusagydfsuydgfusgydfiusygdfiusygdfusdfgyusyfgsuydfgsuydfgiwauygdfasiugfiuasdgfyiusagfiusdfgy');
/*!40000 ALTER TABLE `grupo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `horariodisponivel`
--

LOCK TABLES `horariodisponivel` WRITE;
/*!40000 ALTER TABLE `horariodisponivel` DISABLE KEYS */;
INSERT INTO `horariodisponivel` VALUES (1,'18:00:00'),(2,'19:00:00'),(3,'19:45:00'),(4,'20:15:00');
/*!40000 ALTER TABLE `horariodisponivel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `itemrestaurante`
--

LOCK TABLES `itemrestaurante` WRITE;
/*!40000 ALTER TABLE `itemrestaurante` DISABLE KEYS */;
/*!40000 ALTER TABLE `itemrestaurante` ENABLE KEYS */;
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
/*!40000 ALTER TABLE `itinerario_destino` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `logdogrupo`
--

LOCK TABLES `logdogrupo` WRITE;
/*!40000 ALTER TABLE `logdogrupo` DISABLE KEYS */;
INSERT INTO `logdogrupo` VALUES (1,1,'2025-04-01 00:00:00',1);
/*!40000 ALTER TABLE `logdogrupo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `membrogrupo`
--

LOCK TABLES `membrogrupo` WRITE;
/*!40000 ALTER TABLE `membrogrupo` DISABLE KEYS */;
INSERT INTO `membrogrupo` VALUES (1,'2025-04-01 00:00:00',NULL,1,1,1,1);
/*!40000 ALTER TABLE `membrogrupo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `mesa`
--

LOCK TABLES `mesa` WRITE;
/*!40000 ALTER TABLE `mesa` DISABLE KEYS */;
INSERT INTO `mesa` VALUES (1,'1','2'),(2,'2','4'),(3,'3','6');
/*!40000 ALTER TABLE `mesa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `motivodaviagem`
--

LOCK TABLES `motivodaviagem` WRITE;
/*!40000 ALTER TABLE `motivodaviagem` DISABLE KEYS */;
/*!40000 ALTER TABLE `motivodaviagem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `origemitemrestaurante`
--

LOCK TABLES `origemitemrestaurante` WRITE;
/*!40000 ALTER TABLE `origemitemrestaurante` DISABLE KEYS */;
INSERT INTO `origemitemrestaurante` VALUES (1,'AZEITE'),(2,'PORTUGUES'),(3,'GALO'),(4,'BACALHAO'),(5,'PORTO');
/*!40000 ALTER TABLE `origemitemrestaurante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `pais`
--

LOCK TABLES `pais` WRITE;
/*!40000 ALTER TABLE `pais` DISABLE KEYS */;
/*!40000 ALTER TABLE `pais` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `pessoa`
--

LOCK TABLES `pessoa` WRITE;
/*!40000 ALTER TABLE `pessoa` DISABLE KEYS */;
INSERT INTO `pessoa` VALUES (1,'2000-04-11 04:00:00'),(2,'2000-04-12 04:00:00');
/*!40000 ALTER TABLE `pessoa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `reserva`
--

LOCK TABLES `reserva` WRITE;
/*!40000 ALTER TABLE `reserva` DISABLE KEYS */;
INSERT INTO `reserva` VALUES (1,'2025-02-21 04:00:00','19:00:00',2,2);
/*!40000 ALTER TABLE `reserva` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `reserva_item`
--

LOCK TABLES `reserva_item` WRITE;
/*!40000 ALTER TABLE `reserva_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `reserva_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `rua`
--

LOCK TABLES `rua` WRITE;
/*!40000 ALTER TABLE `rua` DISABLE KEYS */;
/*!40000 ALTER TABLE `rua` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `segmento`
--

LOCK TABLES `segmento` WRITE;
/*!40000 ALTER TABLE `segmento` DISABLE KEYS */;
/*!40000 ALTER TABLE `segmento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `statusfisica`
--

LOCK TABLES `statusfisica` WRITE;
/*!40000 ALTER TABLE `statusfisica` DISABLE KEYS */;
INSERT INTO `statusfisica` VALUES (1,'Ativo'),(2,'Pendente'),(3,'Inativo');
/*!40000 ALTER TABLE `statusfisica` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `statususuario`
--

LOCK TABLES `statususuario` WRITE;
/*!40000 ALTER TABLE `statususuario` DISABLE KEYS */;
INSERT INTO `statususuario` VALUES (1,'Ativo'),(2,'Pendente'),(3,'Inativo');
/*!40000 ALTER TABLE `statususuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `tiposegmento`
--

LOCK TABLES `tiposegmento` WRITE;
/*!40000 ALTER TABLE `tiposegmento` DISABLE KEYS */;
INSERT INTO `tiposegmento` VALUES (1,'Restaurante italiano'),(2,'Restaurante japonês'),(3,'Restaurante chinês'),(4,'Restaurante mexicano'),(5,'Restaurante brasileiro'),(6,'Restaurante árabe'),(7,'Restaurante francês'),(8,'Restaurante indiano'),(9,'Restaurante tailandês'),(10,'Restaurante grego'),(11,'Restaurante fast food'),(12,'Restaurante self-service (buffet)'),(13,'Restaurante à la carte'),(14,'Restaurante por quilo'),(15,'Rodízio'),(16,'Restaurante delivery/takeaway'),(17,'Restaurante vegano'),(18,'Restaurante vegetariano'),(19,'Restaurante orgânico'),(20,'Restaurante gourmet'),(21,'Restaurante temático'),(22,'Food truck'),(23,'Restaurante de alta gastronomia'),(24,'Restaurante popular'),(25,'Bistrô'),(26,'Churrascaria'),(27,'Lanchonete'),(28,'Cafeteria'),(29,'Padaria com refeições'),(30,'Cantina');
/*!40000 ALTER TABLE `tiposegmento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'Jhon','123',1,0),(2,'Nestor','321',2,0);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-22 20:29:00
