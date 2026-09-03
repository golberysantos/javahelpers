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
* docker network disconnect: Desconecta um container de uma rede específica.
* docker network rm: Remove uma ou mais redes que não estão em uso.
* docker network inspect: Exibe detalhes sobre uma rede específica, incluindo containers conectados.
* docker volume inspect: Exibe detalhes sobre um volume específico, incluindo containers que o utilizam.
* docker volume prune: Remove todos os volumes não utilizados para liberar espaço.


## Limpeza do Sistema
* docker system prune: Remove containers parados, redes não utilizadas e imagens suspensas.
* docker system prune -a: Remove todos os containers parados e imagens que não estão em uso.

## Informações e Diagnóstico
* docker info: Exibe informações detalhadas sobre a instalação do Docker e o estado do sistema.
* docker version: Mostra a versão do Docker instalada no sistema.

## Dicas e Boas Práticas
* Sempre utilize tags específicas ao puxar imagens para evitar problemas de compatibilidade.
* Mantenha seus containers e imagens atualizados para garantir segurança e desempenho.
* Use volumes para persistência de dados, evitando perda de informações ao remover containers.

## Comandos Avançados
* docker-compose up: Inicia serviços definidos em um arquivo docker-compose.yml.
* docker-compose down: Para e remove os serviços definidos no docker-compose.yml.

## Comandos de Segurança
* docker login: Autentica o usuário em um repositório Docker.

## Comandos de Monitoramento
* docker stats: Exibe estatísticas em tempo real de containers em execução.

## Comandos de Depuração
* docker inspect: Fornece informações detalhadas sobre containers, imagens, volumes e redes.

## Comandos de Backup e Restauração
* docker export: Exporta o sistema de arquivos de um container para um arquivo tar.
* docker import: Cria uma imagem a partir de um arquivo tar exportado.

## Comandos de Atualização
* docker pull: Atualiza uma imagem local com a versão mais recente do repositório.

## Comandos de Configuração
* docker config create: Cria uma nova configuração para uso em serviços Docker.

## Comandos de Secret Management
* docker secret create: Cria um novo segredo para uso em serviços Docker.

## Comandos de Orquestração
* docker swarm init: Inicializa um novo cluster Docker Swarm.

## Comandos de Escalonamento
* docker service scale: Ajusta o número de réplicas de um serviço em execução.

## Comandos de Rollback
* docker service rollback: Reverte um serviço para a versão anterior.

## Comandos de Atualização de Serviço
* docker service update: Atualiza a configuração de um serviço em execução.

## Comandos de Rede Avançados
* docker network inspect: Exibe detalhes sobre uma rede específica.

## Comandos de Volume Avançados
* docker volume inspect: Exibe detalhes sobre um volume específico.

## Comandos de Imagem Avançados
* docker image inspect: Exibe detalhes sobre uma imagem específica.

## Comandos de Container Avançados
* docker container inspect: Exibe detalhes sobre um container específico.

## Comandos de Log Avançados
* docker logs --tail: Exibe apenas as últimas linhas dos logs de um container.

## Comandos de Execução Avançados de interação com Explicação
* psql: O comando psql é o cliente de linha de comando do PostgreSQL, utilizado para interagir com bancos de dados PostgreSQL. Ele permite executar consultas SQL, gerenciar bancos de dados e realizar tarefas administrativas.  
  - Exemplo: `psql -U postgres -d mydatabase` conecta ao banco de dados "mydatabase" usando o usuário "postgres".
* docker exec -it: Executa um comando interativo dentro de um container em execução.
* psql -U <usuario>: O comando -U <usuario> utilizado em conjunto com o psql dentro do Docker serve para especificar o nome de usuário (role) com o qual você deseja se conectar ao banco de dados PostgreSQL. No contexto do Docker, ele faz parte do utilitário de terminal do Postgres (psql) e não do Docker em si. 
	Detalhes do comportamento:
    - Autenticação: Informa ao PostgreSQL qual usuário está tentando abrir a sessão. Se o usuário criado ao subir o container foi o padrão (postgres) ou um customizado ( meu_usuario), você deve passá-lo obrigatoriamente após a flag -U.
    - Erro comum sem ele: Se você rodar apenas docker exec -it <container> psql sem especificar o -U, o psql tentará se conectar usando o nome do seu usuário atual do sistema operacional de dentro do container (geralmente root), o que costuma resultar no erro FATAL: role "root" does not exist.
* docker exec -it postgres psql -U postgres: 'exec -it' Executa um comando interativo dentro de um container em execução; 'psql -U <usuario>' Especifica o usuário do PostgreSQL para autenticação.  
  - Resumindo, a divisão detalhada de cada parte do comando:
  	- docker exec -it postgres: Executa um comando interativo (-it) dentro do container em execução chamado postgres.
    - psql: Abre a ferramenta de linha de comando oficial do PostgreSQL.
    - '-U devops': Conecta ao banco utilizando o usuário (role) chamado devops.
    - '-d appdb': Conecta diretamente ao banco de dados específico chamado appdb (em vez de usar o banco padrão que tem o mesmo nome do usuário).
  - Exemplo: `docker exec -it postgres psql -U postgres` conecta ao banco de dados PostgreSQL dentro do container chamado "postgres" usando o usuário "postgres".
* docker exec -it postgres psql -U devops -d appdb: 'exec -it' Executa um comando interativo dentro de um container em execução; 'psql -U <usuario> -d <database>' Especifica o usuário do PostgreSQL e o banco de dados para autenticação.  
  - Exemplo: `docker exec -it postgres psql -U devops -d appdb` conecta ao banco de dados "appdb" dentro do container chamado "postgres" usando o usuário "devops".
*  docker inspect postgres | grep IPAddress: Exibe informações detalhadas sobre o container "postgres" e filtra a saída para mostrar apenas o endereço IP do container.  
  - Exemplo: `docker inspect postgres | grep IPAddress` retorna o endereço IP do container "postgres".

* docker exec -it <nomeDoConteiner> bash: Executa um shell bash interativo dentro do container "postgres".  
  - Exemplo: `docker exec -it postgres bash` abre um terminal dentro do container "postgres", permitindo executar comandos diretamente no ambiente do container.

## Referências
* [Documentação oficial do Docker](https://docs.docker.com/)
