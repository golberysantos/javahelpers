# Guia de Consulta Rápida - Comandos Docker

## Gerenciamento de Containers
* docker run: Cria e inicia um novo container a partir de uma imagem.
* docker ps: Lista os containers em execução no momento.
* docker ps -a: Lista todos os containers, incluindo os que estão parados.
* docker start: Inicia um ou mais containers parados.
* docker stop: Interrompe um ou mais containers em execução de forma amigável.
* docker kill: Interrompe um container imediatamente forçando a parada.
* docker restart: Reinicia um container em execução ou parado.
* docker exec: Executa um comando dentro de um container que já está ativo.
* docker logs: Exibe os logs de saída de um container específico.
* docker rm: Remove um ou mais containers que não estão em execução.
* docker inspect: Retorna informações detalhadas e estruturadas sobre um container.
* docker stats: Exibe o fluxo contínuo de estatísticas de uso de recursos dos containers.

## Gerenciamento de Imagens
* docker images: Lista todas as imagens armazenadas localmente no sistema.
* docker pull: Baixa uma imagem de um repositório, como o Docker Hub.
* docker push: Envia uma imagem local para um repositório remoto.
* docker build: Constrói uma nova imagem a partir de um arquivo Dockerfile.
* docker rmi: Remove uma ou mais imagens armazenadas localmente.
* docker tag: Cria uma tag associando um nome ou versão a uma imagem existente.
* docker history: Mostra o histórico de camadas e comandos de uma imagem.

## Gerenciamento de Volumes e Redes
* docker volume ls: Lista todos os volumes de dados criados.
* docker volume create: Cria um novo volume para persistência de dados.
* docker volume rm: Remove um ou mais volumes de dados não utilizados.
* docker network ls: Lista todas as redes configuradas no Docker.
* docker network create: Cria uma nova rede para comunicação entre containers.
* docker network connect: Conecta um container ativo a uma rede específica.

## Limpeza do Sistema
* docker system prune: Remove containers parados, redes não utilizadas e imagens suspensas.
* docker system prune -a: Remove todos os containers parados e imagens que não estão em uso.
