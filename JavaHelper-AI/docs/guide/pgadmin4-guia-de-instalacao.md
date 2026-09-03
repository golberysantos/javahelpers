
```markdown
# 📘 Guia de Instalação e Configuração do pgAdmin no Mini Datacenter

Este documento registra o passo a passo definitivo para instalação do repositório oficial e do ambiente web do pgAdmin 4, contornando falhas comuns de download de chaves GPG por bloqueios de rede.


## 🎯 Objetivo
Permitir o gerenciamento do banco de dados PostgreSQL (rodando em container Docker na **vm-db**) através do **pgAdmin** instalado na **vm-ubuntu-workstation**, seguindo o fluxo seguro:

**PC (192.168.0.34) → vm-workstation (192.168.0.25) → vm-db (192.168.100.20)**

---

## 🖥️ Infraestrutura
- **Proxmox VE**: versão 9.2.2  
- **VMs**: Ubuntu 24.04 LTS (Noble)  
- **vm-db**: PostgreSQL em container Docker (`postgres:16`)  
- **vm-workstation**: pgAdmin + ferramentas administrativas  
- **Redes**:
  - `vmbr0` (externa): 192.168.0.x  
  - `vmbr1` (interna): 192.168.100.x  

---

## Passo 1: Importar a Chave Pública GPG Oficial
Como o download direto via `curl` pode sofrer bloqueios de conexão, utilizamos o servidor de chaves alternativo do Ubuntu para registrar a assinatura oficial do pgAdmin (`8881B2A8210976F2`).

```bash
# Importa a chave pública para o chaveiro confiável do sistema
sudo apt-key adv --keyserver hkps://://ubuntu.com --recv-keys 8881B2A8210976F2

# Exporta a chave importada para o formato binário exigido pelo Ubuntu 24.04
sudo gpg --no-default-keyring --ring /etc/apt/trusted.gpg --export 8881B2A8210976F2 | sudo tee /etc/apt/keyrings/packages-pgadmin-org.gpg > /dev/null
```

## Passo 2: Adicionar o Repositório do pgAdmin 4
Com a chave armazenada de forma segura em `/etc/apt/keyrings/`, criamos o arquivo de configuração de fontes apontando especificamente para a versão `noble` (Ubuntu 24.04).

```bash
sudo sh -c 'echo "deb [signed-by=/etc/apt/keyrings/packages-pgadmin-org.gpg] https://postgresql.org pgadmin4 main" > /etc/apt/sources.list.d/pgadmin4.list'
```

## Passo 3: Atualizar o Gerenciador de Pacotes (APT)
Atualize as listas do sistema para validar e sincronizar os novos pacotes assinados.

```bash
sudo apt update
```
*Nota: Certifique-se de que a linha do repositório do pgAdmin retorne o status `Hit` ou `Obter` sem erros de assinatura.*

Aqui está um texto organizado em formato **Markdown (.md)** que resume todo o processo de instalação e configuração do **pgAdmin** na sua arquitetura de laboratório. Assim você terá um guia de referência para futuras consultas:

---


## 🔧 Configuração de Rede na vm-workstation

Arquivo `/etc/netplan/50-cloud-init.yaml`:

```yaml
network:
  version: 2
  ethernets:
    ens18:  # interface ligada ao vmbr0 (rede externa)
      dhcp4: false
      mtu: 1400
      addresses:
        - 192.168.0.25/24
      nameservers:
        addresses:
          - 8.8.8.8
          - 1.1.1.1
      routes:
        - to: default
          via: 192.168.0.1

    ens19:  # interface ligada ao vmbr1 (rede interna)
      dhcp4: false
      addresses:
        - 192.168.100.25/24
```

Aplicar configuração:
```bash
sudo netplan apply
```

Testes:
```bash
ping 192.168.0.1       # gateway externo
ping 192.168.0.34      # PC
ping 192.168.100.20    # vm-db
```

---

## 🛠️ Configuração do PostgreSQL no Container

### Entrar no container:

Para ficar **dentro do container PostgreSQL** e conseguir editar arquivos de configuração ou acessar o banco diretamente, você precisa usar o comando `docker exec`. Isso abre um shell dentro do container em execução.  

Aqui está o passo a passo:

#### 01 Listar containers ativos

Confirme que o container PostgreSQL está rodando.

No host da vm-db, execute:

    docker ps

    Verifique o container com IMAGE postgres:16 e nome postgres

#### 02 Entrar no container

Abra um shell dentro do container PostgreSQL.

No host da vm-db, execute:

    docker exec -it postgres bash

    Isso abre um terminal dentro do container, como se fosse uma máquina Linux separada

#### 03 Acessar o PostgreSQL

Use o cliente psql para interagir com o banco.

Dentro do container, execute:

    psql -U postgres

    Agora você está no prompt do PostgreSQL e pode rodar comandos SQL

#### 04 🛠️ Editar arquivos de configuração

Se precisar ajustar permissões de rede ou parâmetros, edite os arquivos do PostgreSQL.

##### Editar `postgresql.conf`:

    Dentro do container:
    bash
    	nano /var/lib/postgresql/data/postgresql.conf

    Procure a linha listen_addresses e ajuste para:
    Code

    listen_addresses = '*'
    
##### Editar `pg_hba.conf`:

    Dentro do container:
    bash
	    nano /var/lib/postgresql/data/pg_hba.conf

    Adicione no final:
    Code

    	host    all    all    192.168.100.25/32    md5


#### 🛠️ Reiniciar o container

Saia do container (exit) e reinicie:
bash

docker restart postgres

#### 🛠️ Criar usuário para pgAdmin

De volta ao host da vm-db, entre no psql:
bash

docker exec -it postgres psql -U postgres

Crie um usuário administrativo:
sql

CREATE USER admin WITH PASSWORD 'admin123';
GRANT ALL PRIVILEGES ON DATABASE labdb TO admin;

✅ Teste de conexão

Na vm-workstation, abra o pgAdmin e configure o servidor:

    Host: 192.168.100.20

    Port: 5432

    Username: admin

    Password: admin123

Assim você consegue:

    Entrar no container (docker exec -it postgres bash)

    Usar o cliente psql para criar tabelas e dados de teste

    Editar arquivos de configuração (postgresql.conf e pg_hba.conf) para permitir conexões da vm-workstation
    

---

### Nota: se falhar com o nano

🛠️ Opção 1 – Usar vi ou vim

Normalmente o container PostgreSQL vem com vi. Tente:
bash

vi /var/lib/postgresql/data/pg_hba.conf

No vi:

    Pressione i para entrar em modo de edição.

    Adicione a linha:
    Code

    host    all    all    192.168.100.25/32    md5

    Pressione Esc, depois digite :wq e Enter para salvar e sair.

🛠️ Opção 2 – Usar cat com redirecionamento

Se não quiser usar editor, você pode adicionar a linha diretamente com:
bash

echo "host    all    all    192.168.100.25/32    md5" >> /var/lib/postgresql/data/pg_hba.conf

🛠️ Opção 3 – Instalar nano dentro do container

Se preferir continuar com nano:
bash

apt-get update && apt-get install nano -y

Depois edite normalmente:
bash

nano /var/lib/postgresql/data/pg_hba.conf

🔄 Reiniciar o container

Após editar, saia (exit) e reinicie o container no host:
bash

docker restart postgres

---




### Criar usuário administrativo:
```bash
docker exec -it postgres psql -U postgres
```

```sql
CREATE USER admin WITH PASSWORD 'admin123';
CREATE DATABASE labdb;
GRANT ALL PRIVILEGES ON DATABASE labdb TO admin;
```

---

## 🌐 Instalação e Configuração do pgAdmin Web

Adicionar repositório oficial:
```bash
curl https://www.pgadmin.org/static/packages_pgadmin_org.pub | sudo apt-key add
sudo sh -c 'echo "deb https://ftp.postgresql.org/pub/pgadmin/pgadmin4/apt/$(lsb_release -cs) pgadmin4 main" > /etc/apt/sources.list.d/pgadmin4.list'
sudo apt update
```

Instalar pgAdmin Web:
```bash
sudo apt install pgadmin4-web -y
```

Configurar pgAdmin Web:
```bash
sudo /usr/pgadmin4/bin/setup-web.sh
```
- Definir e-mail e senha para login.  
- Apache será configurado automaticamente.  

Reiniciar Apache:
```bash
sudo systemctl restart apache2
```


---

## ✅ Acesso ao pgAdmin

Para abrir e usar o pgAdmin na sua vm-ubuntu-workstation, você tem duas opções principais: modo desktop ou modo web. Como você quer acessar do seu PC → vm-workstation → vm-db, o ideal é usar o pgAdmin Web rodando na vm-workstation e abrir pelo navegador do seu PC.

### 01 Verificar instalação do pgAdmin

Confirme que o pgAdmin já está instalado na vm-workstation.

No terminal da vm-workstation

    Rode pgadmin4 para modo desktop

    Ou rode pgadmin4-web para modo web

### 02 Configurar pgAdmin Web

Se optar pelo acesso via navegador, configure o pgAdmin como serviço web.

No terminal da vm-workstation

    Execute sudo /usr/pgadmin4/bin/setup-web.sh

    Defina email e senha de administrador

    O serviço será configurado para rodar em http://127.0.0.1:5050

### 03 Acessar pgAdmin do PC

Abra o navegador no seu PC e acesse o pgAdmin rodando na vm-workstation.

    No PC, abra http://192.168.0.25:5050

    Use o email e senha definidos no setup

    Adicione um novo servidor apontando para 192.168.100.20 (vm-db)

### 04 Testar conexão com PostgreSQL

Confirme que o pgAdmin consegue se conectar ao banco na vm-db.

    Host: 192.168.100.20

    Port: 5432

    Username: admin

    Password: senha definida

    Se conectar, você verá o banco labdb
    

No **PC (192.168.0.34)** abra no navegador:
```
http://192.168.0.25/pgadmin4
```

Login com e-mail e senha definidos.  
Adicionar novo servidor:
- **Name:** vm-db  
- **Host:** 192.168.100.20  
- **Port:** 5432  
- **Username:** admin  
- **Password:** admin123  

---

## 🔒 Segurança Recomendada
- Configurar **UFW** na vm-db para permitir apenas conexões da vm-workstation:
```bash
sudo ufw allow from 192.168.100.25 to any port 5432
sudo ufw enable
```
- Documentar credenciais e variáveis de ambiente para a aplicação na vm-app.  
- Configurar backups automáticos e monitoramento (Netdata).  

---

## 📌 Fluxo Final
1. **PC (192.168.0.34)** acessa pgAdmin via navegador.  
2. **vm-workstation (192.168.0.25)** roda pgAdmin Web.  
3. **vm-db (192.168.100.20)** recebe conexões internas seguras.  
4. Banco de dados gerenciado sem exposição externa.  

```

---
*Para descobrir o endereço IP do seu servidor Ubuntu via terminal, execute:*
```bash
ip a s | grep global
```


---

