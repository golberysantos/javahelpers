
## 🚀 Passo a passo para instalar n8n

### 1. Preparar ambiente na vm-app
- **Atualizar pacotes**:
  ```bash
  sudo apt update && sudo apt upgrade -y
  ```
- **Instalar Docker e Docker Compose** (se já não tiver):
  ```bash
  sudo apt install docker.io docker-compose -y
  sudo systemctl enable docker
  ```

---

### 2. Criar diretórios de persistência
- Criar pasta para configuração e dados do n8n:
  ```bash
  mkdir -p /opt/n8n/{data,config}
  ```
  * mkdir -p: A flag -p (de parents) instrui o comando a criar todas as pastas intermediárias caso elas não existam e a não retornar erro caso os diretórios já estejam criados.  
  * /opt/n8n/{data,config}: O shell expande essa linha antes de executá-la, transformando-a em dois caminhos distintos passados para o comando: /opt/n8n/data e /opt/n8n/config.
  
  O que é gerado na prática:
  1. A pasta principal /opt/n8n/ (caso não exista).
  2. A subpasta /opt/n8n/data/ (geralmente usada pelo n8n para armazenar bancos de dados SQLite, arquivos locais ou credenciais).
  3. A subpasta /opt/n8n/config/ (utilizada para armazenar arquivos de configuração).
  
  Essa estrutura é amplamente recomendada ao implantar o n8n via Docker, garantindo que os dados da aplicação não sejam perdidos ao recriar o container.

---

### 3. Configurar Docker Compose
Crie o arquivo `/opt/n8n/docker-compose.yml`:

```yaml
version: "3.8"

services:
  n8n:
    image: n8nio/n8n:latest
    restart: always
    ports:
      - "5678:5678"
    environment:
      - DB_TYPE=postgresdb
      - DB_POSTGRESDB_HOST=192.168.100.20
      - DB_POSTGRESDB_PORT=5432
      - DB_POSTGRESDB_DATABASE=n8n
      - DB_POSTGRESDB_USER=n8n_user
      - DB_POSTGRESDB_PASSWORD=senha_forte_aqui
      - N8N_ENCRYPTION_KEY=chave_super_secreta
      - N8N_HOST=192.168.0.39
      - N8N_PORT=5678
      - N8N_PROTOCOL=http
      - WEBHOOK_URL=http://192.168.0.39:5678/
    volumes:
      - /opt/n8n/data:/home/node/.n8n
```

---

### 4. Configurar banco de dados na vm-db
No PostgreSQL da vm-db:
```sql
CREATE DATABASE n8n;
CREATE USER n8n_user WITH PASSWORD 'senha_forte_aqui';
GRANT ALL PRIVILEGES ON DATABASE n8n TO n8n_user;
```

---

### 5. Subir o container
Na vm-app:
```bash
cd /opt/n8n
docker-compose up -d
```
O comando docker-compose up -d (ou docker compose up -d) lê o arquivo de configuração docker-compose.yml do diretório atual, baixa as imagens necessárias, cria os containers, as redes e os volumes, e inicia os serviços.

    * -d (Detached / Desacoplado): Faz com que os containers rodem em segundo plano (background). O terminal é liberado imediatamente para que você possa continuar digitando outros comandos, em vez de ficar preso exibindo os logs em tempo real da aplicação.

---

### 6. Configurar proxy reverso com Nginx
No Nginx da vm-app, crie `/etc/nginx/sites-available/n8n.conf`:

```nginx
server {
    listen 80;
    server_name n8n.lab.local;

    location / {
        proxy_pass http://localhost:5678;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

Ative o site:
```bash
ln -s /etc/nginx/sites-available/n8n.conf /etc/nginx/sites-enabled/
nginx -t && systemctl reload nginx
```

Agora você acessa via navegador em `http://192.168.0.39` ou `http://n8n.lab.local`.

---

## 🔒 Boas práticas de segurança
- **Usar HTTPS** com Certbot.
- Definir uma **chave de criptografia** forte para proteger credenciais.
- Criar usuário dedicado no PostgreSQL com permissões mínimas.
- Restringir acesso ao banco apenas pela rede interna (192.168.100.0/24).

---

## 💾 Persistência e Backup
- Os dados do n8n ficam em `/opt/n8n/data`.
- Configure **backup automático** do banco de dados:
  ```bash
  pg_dump -U n8n_user -h 192.168.100.20 n8n > /backup/n8n_$(date +%F).sql
  ```
- Faça snapshot das VMs no Proxmox para recuperação rápida.

---

👉 Com isso, você terá o n8n rodando em container Docker na vm-app, usando PostgreSQL da vm-db, acessível pelo navegador do seu PC, com segurança e persistência garantidas.  


Ótimo, vamos montar um **script de backup automatizado** para o PostgreSQL da vm-db e para os dados do n8n na vm-app. Assim você terá cópias diárias seguras e organizadas.

---

## 📂 Estrutura de backup

- **Banco de dados (vm-db)**: dump diário via `pg_dump`.
- **Dados do n8n (vm-app)**: cópia da pasta `/opt/n8n/data`.
- **Destino dos backups**: `/opt/backups/n8n` na vm-app (pode ser sincronizado depois para outro storage).

---

## 🛠️ Script de backup

Crie o arquivo `/opt/n8n/backup.sh` na **vm-app**:

```bash
#!/bin/bash

# Diretórios
BACKUP_DIR="/opt/backups/n8n"
DATE=$(date +%F_%H-%M)

# Banco de dados
DB_HOST="192.168.100.20"
DB_NAME="n8n"
DB_USER="n8n_user"
DB_PASS="senha_forte_aqui"

# Exportar senha para pg_dump
export PGPASSWORD=$DB_PASS

# Criar diretório se não existir
mkdir -p $BACKUP_DIR

# Backup do banco de dados
pg_dump -U $DB_USER -h $DB_HOST $DB_NAME > $BACKUP_DIR/db_$DATE.sql

# Backup dos dados do n8n
tar -czf $BACKUP_DIR/n8n_data_$DATE.tar.gz /opt/n8n/data

# Remover backups antigos (mantém últimos 7 dias)
find $BACKUP_DIR -type f -mtime +7 -delete
```

Dar permissão de execução:
```bash
chmod +x /opt/n8n/backup.sh
```

---

## ⏰ Automatizar com cron

Edite o cron:
```bash
crontab -e
```

Adicione a linha para rodar todos os dias às 2h da manhã:
```
0 2 * * * /opt/n8n/backup.sh >> /opt/backups/n8n/backup.log 2>&1
```

---

## 🔒 Boas práticas adicionais
- **Criptografar backups** antes de enviar para storage externo.
- **Sincronizar backups** para outro servidor ou NAS.
- Testar restauração periodicamente para garantir que os dumps estão íntegros.

---

👉 Com esse script, você terá backups automáticos diários do banco e dos dados do n8n, com retenção de 7 dias.  


---

Excelente, vamos montar um **script de restauração rápida** para que você consiga recuperar o n8n em caso de falha, usando os backups que configuramos.

---

## 🔄 Script de restauração

Crie o arquivo `/opt/n8n/restore.sh` na **vm-app**:

```bash
#!/bin/bash

# Diretórios
BACKUP_DIR="/opt/backups/n8n"
LATEST_DB_BACKUP=$(ls -t $BACKUP_DIR/db_*.sql | head -n 1)
LATEST_DATA_BACKUP=$(ls -t $BACKUP_DIR/n8n_data_*.tar.gz | head -n 1)

# Banco de dados
DB_HOST="192.168.100.20"
DB_NAME="n8n"
DB_USER="n8n_user"
DB_PASS="senha_forte_aqui"

export PGPASSWORD=$DB_PASS

echo "Restaurando banco de dados a partir de $LATEST_DB_BACKUP..."
psql -U $DB_USER -h $DB_HOST -d $DB_NAME < $LATEST_DB_BACKUP

echo "Restaurando dados do n8n a partir de $LATEST_DATA_BACKUP..."
rm -rf /opt/n8n/data/*
tar -xzf $LATEST_DATA_BACKUP -C /

echo "Restauração concluída!"
```

Dar permissão de execução:
```bash
chmod +x /opt/n8n/restore.sh
```

---

## 🧪 Teste de restauração

1. **Parar containers**:
   ```bash
   cd /opt/n8n
   docker-compose down
   ```

2. **Rodar restauração**:
   ```bash
   /opt/n8n/restore.sh
   ```

3. **Subir containers novamente**:
   ```bash
   docker-compose up -d
   ```

---

## ⚙️ Boas práticas
- Sempre teste a restauração em ambiente de laboratório antes de precisar em produção.
- Combine com snapshots do **Proxmox** para recuperação rápida de toda VM.
- Considere armazenar backups em storage externo ou **sincronizar com rsync** para maior resiliência.

---

👉 Com esse script, você terá um processo simples e rápido para restaurar tanto o banco quanto os dados do n8n.  


---

Perfeito, vamos estruturar um **playbook de desastre** para o seu mini datacenter. A ideia é ter um guia claro e rápido para recuperar o ambiente n8n em caso de falha total da **vm-app** ou da **vm-db**.

---

## 📖 Playbook de Desastre – Recuperação do n8n

### 1. Falha da vm-app (servidor de aplicação)
- **Restaurar VM** a partir de snapshot ou backup do Proxmox.
- Caso não haja snapshot:
  1. Criar nova VM com Ubuntu 24.04.
  2. Instalar Docker e Docker Compose.
  3. Restaurar diretório `/opt/n8n/data` a partir do backup (`restore.sh`).
  4. Copiar `docker-compose.yml` e subir containers:
     ```bash
     cd /opt/n8n
     docker-compose up -d
     ```
  5. Restaurar configuração do Nginx e certificados TLS.

---

### 2. Falha da vm-db (servidor de banco de dados)
- **Restaurar VM** a partir de snapshot ou backup.
- Caso não haja snapshot:
  1. Criar nova VM com Ubuntu 24.04.
  2. Instalar Docker e PostgreSQL.
  3. Criar usuário e banco novamente:
     ```sql
     CREATE DATABASE n8n;
     CREATE USER n8n_user WITH PASSWORD 'senha_forte_aqui';
     GRANT ALL PRIVILEGES ON DATABASE n8n TO n8n_user;
     ```
  4. Restaurar dump mais recente:
     ```bash
     psql -U n8n_user -h 192.168.100.20 -d n8n < /opt/backups/n8n/db_YYYY-MM-DD.sql
     ```

---

### 3. Falha total (vm-app + vm-db)
1. Restaurar ambas as VMs a partir de snapshots do Proxmox.
2. Caso não haja snapshots:
   - Criar novamente vm-app e vm-db conforme descrito acima.
   - Restaurar banco de dados primeiro.
   - Depois restaurar dados do n8n.
   - Subir containers e validar conectividade.

---

### 4. Checklist pós-restauração
- **Testar acesso** ao n8n pelo navegador.
- Validar credenciais e workflows.
- Conferir logs de containers:
  ```bash
  docker logs n8n
  ```
- Confirmar que backups automáticos estão ativos (`crontab -l`).

---

### 5. Boas práticas adicionais
- **Snapshots regulares** no Proxmox (diários ou semanais).
- **Backups externos**: sincronizar `/opt/backups/n8n` para NAS ou cloud.
- **Testes de recuperação** trimestrais para garantir integridade.
- Documentar credenciais e chaves em cofre seguro (ex: Vault ou Bitwarden).

---

👉 Com esse playbook, você terá um guia rápido e organizado para restaurar o ambiente n8n em qualquer cenário de falha.  
