
## 🚀 Passos para instalar o n8n no Docker

### 1. Criar rede Docker
Como você já tem duas interfaces (externa e interna), o ideal é usar uma rede Docker bridge para comunicação interna dos containers:

```bash
docker network create n8n-net
```

---

### 2. Configurar container do n8n
Crie um `docker-compose.yml` na `vm-app`:

```yaml
version: "3.7"

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
      - DB_POSTGRESDB_PASSWORD=senha_forte
      - N8N_HOST=n8n.lab.local
      - N8N_PORT=5678
      - N8N_PROTOCOL=http
      - WEBHOOK_URL=https://n8n.lab.local/
    networks:
      - n8n-net

networks:
  n8n-net:
    external: true
```

> ⚠️ Crie previamente o banco e usuário `n8n_user` no PostgreSQL da `vm-db`.

---

### 3. Configurar Nginx como proxy reverso
No `vm-app`, adicione um bloco de configuração em `/etc/nginx/sites-available/n8n.conf`:

```nginx
server {
    listen 80;
    server_name n8n.lab.local;

    location / {
        proxy_pass http://127.0.0.1:5678;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

Depois habilite:
```bash
ln -s /etc/nginx/sites-available/n8n.conf /etc/nginx/sites-enabled/
nginx -t
systemctl reload nginx
```

---

### 4. Testar acesso
- No seu PC, adicione `n8n.lab.local` ao `/etc/hosts` apontando para `192.168.0.39`.
- Acesse via navegador: `http://n8n.lab.local`.

---

## 🔒 Recomendações extras
- Configure **HTTPS** com Let's Encrypt ou certificado interno.
- Use variáveis de ambiente seguras (arquivo `.env`).
- Considere rodar o n8n em modo **detached** com `docker-compose up -d`.

---

Quer que eu detalhe também como criar o banco e usuário no **PostgreSQL** da `vm-db`, ou prefere que foquemos na parte de **segurança com HTTPS** via Nginx?  

Você pode escolher: PostgreSQL setup ou HTTPS com Nginx.