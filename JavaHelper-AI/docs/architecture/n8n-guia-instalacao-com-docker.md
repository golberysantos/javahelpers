Ótimo, vamos montar juntos um **passo a passo guiado** para instalar o **n8n** no Docker dentro da sua `vm-app`, já incluindo o stack de **observabilidade (Prometheus + Grafana + exporters)** e preparando o ambiente para **backup e recuperação**.  

---

## 🛠️ Etapa 1 — Preparar ambiente
- **Rede Docker**:  
  ```bash
  docker network create n8n-net
  ```
- **Volumes persistentes**:  
  Crie diretórios para dados e logs:
  ```bash
  mkdir -p /opt/n8n/data /opt/n8n/logs
  ```

---

## 🛠️ Etapa 2 — Criar arquivo `.env`
No `/opt/n8n/.env`:
```env
DB_TYPE=postgresdb
DB_POSTGRESDB_HOST=192.168.100.20
DB_POSTGRESDB_PORT=5432
DB_POSTGRESDB_DATABASE=n8n
DB_POSTGRESDB_USER=n8n_user
DB_POSTGRESDB_PASSWORD=senha_forte
N8N_HOST=n8n.lab.local
N8N_PORT=5678
N8N_PROTOCOL=http
WEBHOOK_URL=https://n8n.lab.local/
```

---

## 🛠️ Etapa 3 — docker-compose.yml
No `/opt/n8n/docker-compose.yml`:

```yaml
version: "3.7"

services:
  n8n:
    image: n8nio/n8n:latest
    restart: always
    env_file: .env
    ports:
      - "5678:5678"
    volumes:
      - /opt/n8n/data:/home/node/.n8n
      - /opt/n8n/logs:/var/log/n8n
    networks:
      - n8n-net

  prometheus:
    image: prom/prometheus
    volumes:
      - /opt/prometheus:/etc/prometheus
    ports:
      - "9090:9090"
    networks:
      - n8n-net

  grafana:
    image: grafana/grafana
    ports:
      - "3000:3000"
    volumes:
      - /opt/grafana:/var/lib/grafana
    networks:
      - n8n-net

  cadvisor:
    image: gcr.io/cadvisor/cadvisor:latest
    ports:
      - "8080:8080"
    volumes:
      - /:/rootfs:ro
      - /var/run:/var/run:ro
      - /sys:/sys:ro
      - /var/lib/docker/:/var/lib/docker:ro
    networks:
      - n8n-net

  node-exporter:
    image: prom/node-exporter
    ports:
      - "9100:9100"
    networks:
      - n8n-net

networks:
  n8n-net:
    external: true
```

---

## 🛠️ Etapa 4 — Configurar Prometheus
No `/opt/prometheus/prometheus.yml`:

```yaml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'n8n'
    static_configs:
      - targets: ['n8n:5678']

  - job_name: 'cadvisor'
    static_configs:
      - targets: ['cadvisor:8080']

  - job_name: 'node'
    static_configs:
      - targets: ['node-exporter:9100']
```

---

## 🛠️ Etapa 5 — Subir stack
```bash
docker-compose up -d
```

---

## 🛠️ Etapa 6 — Configurar Grafana
- Acesse `http://vm-app:3000` (usuário admin / senha admin).
- Adicione Prometheus como **Data Source** (`http://prometheus:9090`).
- Importe dashboards prontos:
  - Docker / cAdvisor.
  - Node Exporter.
  - PostgreSQL (se adicionar exporter).
  - Crie dashboard custom para **n8n workflows**.

---

## 🛠️ Etapa 7 — Backup e Recuperação
- **Banco PostgreSQL (vm-db)**:
  - Backup diário com `pg_dump`:
    ```bash
    pg_dump -U n8n_user -h 192.168.100.20 n8n > /backups/n8n_$(date +%F).sql
    ```
  - Automatizar com cron.
- **Volumes n8n**:
  - Backup de `/opt/n8n/data` e `/opt/n8n/logs`.
  - Sincronizar para NAS ou cloud.
- **Testes de restauração**:
  - Restaurar banco em ambiente separado.
  - Subir container n8n com volumes restaurados.
  - Validar workflows.

---

## 🛠️ Etapa 8 — Segurança
- Restringir acesso externo à porta 5678 (apenas via Nginx).
- Configurar HTTPS no proxy reverso.
- Usar autenticação forte no painel Grafana e n8n.

---

👉 Agora você tem o **guia completo** para instalar o n8n com observabilidade e backup.  

Quer que eu prepare também um **script cron automatizado** para rodar os backups diariamente e semanalmente, já pronto para colar na `vm-db`?