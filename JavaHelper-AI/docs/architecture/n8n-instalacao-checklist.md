# ✅ Checklist - Instalação e Configuração do n8n

## 📋 Fase 1: Preparação do Ambiente

### vm-app (Servidor de Aplicação)
- [x] Atualizar pacotes do sistema
  ```bash
  sudo apt update && sudo apt upgrade -y
  ```
- [x] Instalar Docker
  ```bash
  sudo apt install docker.io -y
  ```
- [x] Instalar Docker Compose
  ```bash
  sudo apt install docker-compose -y
  ```
- [ ] Habilitar Docker no boot
  ```bash
  sudo systemctl enable docker
  ```
- [x] Criar diretório `/opt/n8n`
  ```bash
  mkdir -p /opt/n8n/{data,config}
  ```
- [ ] Verificar permissões dos diretórios

---

## 🗄️ Fase 2: Configuração do Banco de Dados

### vm-db (Servidor de PostgreSQL)
- [ ] Verificar se PostgreSQL está instalado e rodando
- [ ] Criar banco de dados
  ```sql
  CREATE DATABASE n8n;
  ```
- [ ] Criar usuário dedicado
  ```sql
  CREATE USER n8n_user WITH PASSWORD 'senha_forte_aqui';
  ```
- [ ] Conceder permissões
  ```sql
  GRANT ALL PRIVILEGES ON DATABASE n8n TO n8n_user;
  ```
- [ ] Testar conexão de `vm-app` para `vm-db`
  ```bash
  psql -U n8n_user -h 192.168.100.20 -d n8n
  ```
- [ ] Documentar credenciais em local seguro (Vault/Bitwarden)

---

## 🐳 Fase 3: Configuração do Docker Compose

### vm-app
- [ ] Criar arquivo `/opt/n8n/docker-compose.yml`
- [ ] Configurar parâmetros:
  - [ ] `DB_POSTGRESDB_HOST=192.168.100.20`
  - [ ] `DB_POSTGRESDB_PORT=5432`
  - [ ] `DB_POSTGRESDB_DATABASE=n8n`
  - [ ] `DB_POSTGRESDB_USER=n8n_user`
  - [ ] `DB_POSTGRESDB_PASSWORD=senha_forte_aqui`
  - [ ] `N8N_ENCRYPTION_KEY=chave_super_secreta` (gerar chave forte)
  - [ ] `N8N_HOST=192.168.0.39`
  - [ ] `N8N_PORT=5678`
  - [ ] `N8N_PROTOCOL=http`
  - [ ] `WEBHOOK_URL=http://192.168.0.39:5678/`
- [ ] Configurar volumes para persistência
  - [ ] `/opt/n8n/data:/home/node/.n8n`
- [ ] Subir container
  ```bash
  cd /opt/n8n && docker-compose up -d
  ```
- [ ] Verificar status do container
  ```bash
  docker-compose ps
  ```
- [ ] Verificar logs
  ```bash
  docker logs n8n
  ```

---

## 🌐 Fase 4: Configuração do Nginx (Proxy Reverso)

### vm-app
- [ ] Instalar Nginx (se não instalado)
- [ ] Criar arquivo `/etc/nginx/sites-available/n8n.conf`
- [ ] Configurar proxy reverso:
  - [ ] `proxy_pass http://localhost:5678`
  - [ ] Headers: `Host`, `X-Real-IP`, `X-Forwarded-For`, `X-Forwarded-Proto`
- [ ] Ativar site
  ```bash
  ln -s /etc/nginx/sites-available/n8n.conf /etc/nginx/sites-enabled/
  ```
- [ ] Testar configuração
  ```bash
  nginx -t
  ```
- [ ] Recarregar Nginx
  ```bash
  systemctl reload nginx
  ```
- [ ] Testar acesso via navegador
  - [ ] `http://192.168.0.39`
  - [ ] `http://n8n.lab.local`

---

## 🔐 Fase 5: Segurança

- [ ] Configurar HTTPS com Certbot
  ```bash
  sudo apt install certbot python3-certbot-nginx -y
  sudo certbot certonly --nginx -d n8n.lab.local
  ```
- [ ] Atualizar Nginx com certificados SSL
- [ ] Verificar se `N8N_ENCRYPTION_KEY` está definida (chave forte)
- [ ] Restringir acesso do PostgreSQL apenas à rede interna
  - [ ] Verificar `pg_hba.conf` em vm-db
  - [ ] Permitir acesso apenas de `192.168.100.0/24`
- [ ] Criar backup da senha do usuário n8n_user em cofre seguro
- [ ] Configurar firewall na vm-app
  - [ ] Permitir porta 80 (HTTP)
  - [ ] Permitir porta 443 (HTTPS)
  - [ ] Permitir porta 5678 (n8n direto, se necessário)

---

## 💾 Fase 6: Backup e Persistência

### vm-app
- [ ] Criar diretório de backups
  ```bash
  mkdir -p /opt/backups/n8n
  ```
- [ ] Criar script `/opt/n8n/backup.sh`
  - [ ] Incluir backup do banco de dados (`pg_dump`)
  - [ ] Incluir backup dos dados do n8n (tar.gz)
  - [ ] Limpeza de backups antigos (manter últimos 7 dias)
- [ ] Dar permissão de execução
  ```bash
  chmod +x /opt/n8n/backup.sh
  ```
- [ ] Configurar cron para backup automático
  ```bash
  crontab -e
  # Adicionar: 0 2 * * * /opt/n8n/backup.sh >> /opt/backups/n8n/backup.log 2>&1
  ```
- [ ] Testar script de backup manualmente
- [ ] Verificar se backups estão sendo gerados em `/opt/backups/n8n`

---

## 🔄 Fase 7: Restauração

### vm-app
- [ ] Criar script `/opt/n8n/restore.sh`
  - [ ] Incluir restauração do banco de dados
  - [ ] Incluir restauração dos dados do n8n
  - [ ] Validar caminhos e variáveis
- [ ] Dar permissão de execução
  ```bash
  chmod +x /opt/n8n/restore.sh
  ```
- [ ] **TESTAR restauração em ambiente de laboratório**
  - [ ] Parar containers
  - [ ] Executar restore.sh
  - [ ] Subir containers novamente
  - [ ] Validar integridade dos dados

---

## 🚨 Fase 8: Snapshots e Plano de Desastre

### Proxmox
- [ ] Criar snapshot de vm-app
  - [ ] Documentar data e descrição
- [ ] Criar snapshot de vm-db
  - [ ] Documentar data e descrição
- [ ] Agendar snapshots semanais/diários

### Documentação
- [ ] Revisar playbook de desastre
  - [ ] Procedimento para falha de vm-app
  - [ ] Procedimento para falha de vm-db
  - [ ] Procedimento para falha total
  - [ ] Checklist pós-restauração

---

## 🧪 Fase 9: Testes e Validação

- [ ] Acessar interface web do n8n
  - [ ] URL: `http://n8n.lab.local` ou `http://192.168.0.39`
  - [ ] Porta: 5678
- [ ] Criar workflow de teste
  - [ ] Validar execução
  - [ ] Verificar logs
- [ ] Testar persistência
  - [ ] Parar container
  - [ ] Subir novamente
  - [ ] Validar se dados estão preservados
- [ ] Verificar logs de aplicação
  ```bash
  docker logs n8n
  ```
- [ ] Validar conectividade com banco de dados
  ```bash
  docker exec n8n psql -U n8n_user -h 192.168.100.20 -d n8n -c "SELECT 1;"
  ```
- [ ] Verificar permissões de arquivo em `/opt/n8n/data`

---

## 📊 Fase 10: Monitoramento e Manutenção

- [ ] Configurar monitoramento de logs
  - [ ] Verificar rotação de logs do n8n
  - [ ] Arquivar logs antigos
- [ ] Monitorar espaço em disco
  - [ ] `/opt/n8n/data`
  - [ ] `/opt/backups/n8n`
- [ ] Testar restauração **trimestralmente**
  - [ ] Validar integridade de backups
  - [ ] Atualizar documentação
- [ ] Revisar credenciais e chaves de criptografia
  - [ ] Rotação de senhas periodicamente
  - [ ] Atualização de chaves de segurança

---

## 📝 Notas Importantes

- **IP vm-app**: `192.168.0.39`
- **IP vm-db**: `192.168.100.20`
- **Porta n8n**: `5678`
- **Banco de dados**: PostgreSQL em container ou bare metal em vm-db
- **Backup automático**: 2h da manhã via cron
- **Retenção de backups**: últimos 7 dias
- **Sincronização de backups**: configurar com NAS ou storage externo

---

## ✨ Recursos Adicionais

- 🔒 Guia de Boas Práticas: Consultar seção "Boas práticas de segurança"
- 💾 Scripts: `/opt/n8n/backup.sh`, `/opt/n8n/restore.sh`
- 📖 Documentação: Playbook de desastre em `n8n-guia-instalacao-com-docker.md`

