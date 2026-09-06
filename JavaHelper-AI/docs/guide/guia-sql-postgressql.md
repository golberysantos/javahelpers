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


Para remover um banco de dados no PostgreSQL, você deve utilizar o comando `DROP DATABASE`.


```sql
DROP DATABASE nome_do_banco;

```

**Comando seguro (evita erros caso o banco não exista):**

```sql
DROP DATABASE IF EXISTS nome_do_banco;

```

**⚠️ Observações importantes:**

* **Conexão:** Você não pode estar conectado ao banco de dados que deseja excluir. Conecte-se a outro banco (como o `postgres`) antes de executar o comando.
* **Conexões ativas (Versão 13 ou superior):** Se houver usuários conectados ao banco, você pode forçar a desconexão e exclusão utilizando:
```sql
DROP DATABASE nome_do_banco WITH (FORCE);

```


* **Alternativa via terminal (fora do SQL):** Você também pode usar o utilitário do sistema operacional no terminal do seu computador:
```bash
dropdb nome_do_banco

```
---

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


Para alterar a senha de um usuário no PostgreSQL, você pode utilizar o comando `ALTER ROLE` ou `ALTER USER` (ambos funcionam da mesma forma).


## **Comando SQL administativos:**

```sql
ALTER USER nome_do_usuario WITH PASSWORD 'nova_senha';

```

**Alternativa com ROLE:**

```sql
ALTER ROLE nome_do_usuario WITH PASSWORD 'nova_senha';

```

**⚠️ Observações importantes:**

* **Permissões:** Para alterar a senha de outro usuário, você precisa ter a permissão de `CREATEROLE` ou ser um superusuário (`postgres`). Qualquer usuário pode alterar a sua própria senha sem privilégios especiais.
* **Criptografia padrão:** Por segurança, o PostgreSQL armazena a senha criptografada (geralmente usando SCRAM-SHA-256 por padrão nas versões mais recentes). O comando acima cuida disso automaticamente, desde que a senha seja passada entre apóstrofos (`'`).
* **Alternativa via terminal (fora do SQL):** Você também pode alterar a senha diretamente pelo terminal do sistema operacional utilizando o comando `psql`:
```bash
psql -U postgres -c "\password nome_do_usuario"

```


*(O terminal solicitará a nova senha de forma segura, sem exibi-la na tela).*