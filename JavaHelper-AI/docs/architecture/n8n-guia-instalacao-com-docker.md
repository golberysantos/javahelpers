
## 🚀 Passo a passo para instalar n8n

### 1. Preparar ambiente na vm-app
- **Atualizar pacotes**:
  ```bash
  sudo apt update && sudo apt upgrade -y
  ```
- **Instalar Docker e Docker Compose**:
  ```bash
  sudo apt install docker.io docker-compose -y
  sudo systemctl enable docker
  ```

---

### 2. Criar diretórios de persistência
```bash
mkdir -p /opt/n8n/{data,config}
```
- `mkdir -p`: cria todas as pastas intermediárias se não existirem e não retorna erro se já existirem.  
- Estrutura gerada:
  - `/opt/n8n/` (principal)  
  - `/opt/n8n/data/` (dados do n8n: credenciais, SQLite, etc.)  
  - `/opt/n8n/config/` (configurações adicionais)  

Essa organização garante persistência mesmo após recriar containers.

---

### 3. Configurar Docker Compose
Arquivo `/opt/n8n/docker-compose.yml`:

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

- **N8N_HOST**: IP ou hostname usado pelo n8n para se identificar internamente.  
- **WEBHOOK_URL**: URL pública usada para gerar endpoints de Webhook.  
  → Teste criando um nó Webhook no n8n e verifique se a URL gerada começa com `http://192.168.0.39:5678/`.

---

### 4. Configurar banco de dados na vm-db
No PostgreSQL:
```sql
CREATE DATABASE n8n;
CREATE USER n8n_user WITH PASSWORD 'senha_forte_aqui';
GRANT ALL PRIVILEGES ON DATABASE n8n TO n8n_user;
```

---

### 5. Subir o container
```bash
cd /opt/n8n
docker-compose up -d
```
- `-d` (detached): roda em segundo plano, liberando o terminal.

---

### 6. Configurar proxy reverso com Nginx
Arquivo `/etc/nginx/sites-available/n8n.conf`:

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

Ativar:
```bash
ln -s /etc/nginx/sites-available/n8n.conf /etc/nginx/sites-enabled/
nginx -t && systemctl reload nginx
```

Acesso:  
- **http://192.168.0.39**  
- **http://n8n.lab.local**

---

## 🔒 Boas práticas de segurança
- **Usar HTTPS** com Certbot.  
- Definir **chave de criptografia** forte.  
- Usuário PostgreSQL dedicado com permissões mínimas.  
- Restringir acesso ao banco à rede interna (192.168.100.0/24).  

---

## 💾 Persistência e Backup

### Estrutura
- Banco: dump diário via `pg_dump`.  
- Dados do n8n: cópia de `/opt/n8n/data`.  
- Destino: `/opt/backups/n8n` na vm-app.  

### Script de backup `/opt/n8n/backup.sh`
```bash
#!/bin/bash
BACKUP_DIR="/opt/backups/n8n"
DATE=$(date +%F_%H-%M)
DB_HOST="192.168.100.20"
DB_NAME="n8n"
DB_USER="n8n_user"
DB_PASS="senha_forte_aqui"
export PGPASSWORD=$DB_PASS
mkdir -p $BACKUP_DIR
pg_dump -U $DB_USER -h $DB_HOST $DB_NAME > $BACKUP_DIR/db_$DATE.sql
tar -czf $BACKUP_DIR/n8n_data_$DATE.tar.gz /opt/n8n/data
find $BACKUP_DIR -type f -mtime +7 -delete
```

Agendar no cron (diário às 2h):
```
0 2 * * * /opt/n8n/backup.sh >> /opt/backups/n8n/backup.log 2>&1
```

### Script de restauração `/opt/n8n/restore.sh`
```bash
#!/bin/bash
BACKUP_DIR="/opt/backups/n8n"
LATEST_DB_BACKUP=$(ls -t $BACKUP_DIR/db_*.sql | head -n 1)
LATEST_DATA_BACKUP=$(ls -t $BACKUP_DIR/n8n_data_*.tar.gz | head -n 1)
DB_HOST="192.168.100.20"
DB_NAME="n8n"
DB_USER="n8n_user"
DB_PASS="senha_forte_aqui"
export PGPASSWORD=$DB_PASS
psql -U $DB_USER -h $DB_HOST -d $DB_NAME < $LATEST_DB_BACKUP
rm -rf /opt/n8n/data/*
tar -xzf $LATEST_DATA_BACKUP -C /
```

---

## 📖 Playbook de Desastre

### Falha vm-app
- Restaurar snapshot Proxmox.  
- Se não houver: recriar VM, instalar Docker, restaurar `/opt/n8n/data`, subir containers, restaurar Nginx/TLS.  

### Falha vm-db
- Restaurar snapshot Proxmox.  
- Se não houver: recriar VM, instalar PostgreSQL, recriar usuário/banco, restaurar dump.  

### Falha total
- Restaurar ambas as VMs.  
- Se não houver: recriar vm-app e vm-db, restaurar banco primeiro, depois dados do n8n.  

### Checklist pós-restauração
- Testar acesso via navegador.  
- Validar credenciais/workflows.  
- Conferir logs (`docker logs n8n`).  
- Confirmar cron ativo (`crontab -l`).  

---

