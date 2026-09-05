Os comandos do PostgreSQL são divididos em categorias funcionais padrão SQL, acompanhadas de recursos específicos do SGBD para gerenciamento via terminal (`psql`).

O acesso a um banco de dados PostgreSQL pode ser feito de três formas principais: pela linha de comando, por ferramentas gráficas (GUIs) ou via string de conexão em aplicações.

## **Via Linha de Comando (psql)**
O utilitário padrão `psql` vem instalado com o PostgreSQL. No terminal do seu sistema operacional, utilize os seguintes comandos:

* **Conexão local simples:** `psql -U nome_usuario -d nome_banco`
* **Conexão remota:** `psql -h host_ou_ip -p 5432 -U nome_usuario -d nome_banco`
* *Nota:* O sistema solicitará a senha configurada para o usuário logo após a execução.

**Via Ferramentas Gráficas (GUIs)**
Para gerenciar o banco por meio de uma interface visual, utilize softwares dedicados informando os dados de conexão (`Host`, `Porta`, `Banco`, `Usuário` e `Senha`):

* **pgAdmin:** Ferramenta oficial e nativa de administração do PostgreSQL.
* **DBeaver:** Uma das opções gratuitas mais populares e completas para múltiplos bancos de dados.
* **Beekeeper Studio** ou **DataGrip:** Alternativas modernas com foco em usabilidade e design.

**Via String de Conexão (URI)**
Formato padrão utilizado em arquivos de configuração de sistemas e frameworks de desenvolvimento:
`postgresql://usuario:senha@localhost:5432/nome_banco`


**Consulta de Dados (DQL)**

* `SELECT`: Recupera dados de uma ou mais tabelas, permitindo filtragem, ordenação e agregações.

**Manipulação de Dados (DML)**

* `INSERT`: Adiciona novas linhas de dados a uma tabela.
* `UPDATE`: Modifica dados já existentes em uma tabela.
* `DELETE`: Remove linhas específicas de uma tabela com base em condições.

**Definição de Estrutura (DDL)**

* `CREATE`: Cria novos objetos no banco de dados (como tabelas, índices, visões e schemas).
* `ALTER`: Modifica a estrutura de um objeto existente (ex: adicionar colunas).
* `DROP`: Remove permanentemente um objeto do banco de dados.
* `TRUNCATE`: Esvazia rapidamente uma tabela, redefinindo contadores, sendo mais eficiente que o `DELETE`.

**Controle de Transações (TCL)**

* `BEGIN` (ou `START TRANSACTION`): Inicia uma nova transação.
* `COMMIT`: Efetiva permanentemente todas as operações da transação atual.
* `ROLLBACK`: Cancela todas as alterações feitas durante a transação atual em caso de erro.

**Controle de Acesso (DCL)**

* `GRANT`: Concede privilégios de acesso a usuários ou grupos.
* `REVOKE`: Remove privilégios previamente concedidos.

**Metacomandos do Terminal (psql)**

* `\l`: Lista todos os bancos de dados disponíveis no servidor.
* `\c nome_banco`: Conecta a um banco de dados específico.
* `\dt`: Lista todas as tabelas do schema atual.
* `\d nome_tabela`: Exibe a estrutura detalhada de uma tabela (colunas, tipos e restrições).
* `\q`: Sai da interface de linha de comando do PostgreSQL.

**Otimização**

* `EXPLAIN`: Exibe o plano de execução planejado pelo otimizador para uma consulta, útil para identificar gargalos de performance e uso de índices.