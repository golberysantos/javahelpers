# Mini Datacenter Plan
Ubuntu 24.04 LTS (Noble) como sistema base para todas as VMs.
## Objetivo
Criar um ambiente de laboratório que simule um datacenter em nuvem, com separação clara entre aplicação e banco de dados, utilizando VMs, redes internas e containers Docker.

## Componentes

- **vm-app**
  - Função: Servidor de aplicação e bastion host.
  - IP externo: 192.168.0.39 (rede vmbr0).
  - IP interno: 192.168.100.10 (rede vmbr1).
  - Serviços: Docker Engine, Nginx, aplicação backend.
  - Papel: Ponte entre rede externa e interna.

- **vm-db**
  - Função: Servidor de banco de dados.
  - IP interno: 192.168.100.20 (rede vmbr1).
  - Serviços: Docker Engine, PostgreSQL.
  - Papel: Armazenamento seguro de dados, acessível apenas pela vm-app.

## Redes

- **vmbr0 (externa)**  
  - Conecta o host físico e permite acesso do PC à vm-app.  
  - IPs na faixa 192.168.0.x.  

- **vmbr1 (interna)**  
  - Rede privada entre vm-app e vm-db.  
  - IPs na faixa 192.168.100.x.  
  - Não acessível diretamente do PC.  

## Fluxo de acesso
PC → vm-app (externa) → vm-db (interna)

## Próximos passos
1. Instalar Docker na vm-db.  
2. Subir container PostgreSQL na vm-db.  
3. Configurar vm-app para se conectar ao PostgreSQL.  
4. Testar comunicação entre aplicação e banco.  
5. Documentar expansão futura (Redis, API Gateway, etc.).  


## Diagrama de arquitetura
https://copilot.microsoft.com/th/id/BCO.0e7b5e38-b030-498b-a876-ca0b05fb0b80.png

![Mini Datacenter Diagram](mini-datacenter-diagram.png)

✅ O que o diagrama mostra
O diagrama mostra a comunicação entre o PC, a vm-app e a vm-db:
- A vm-app atua como bastion host, conectando a rede externa (vmbr0) à interna (vmbr1).
- A vm-db é acessível apenas pela vm-app, garantindo isolamento e segurança.
- Cada VM roda Docker para hospedar seus serviços (Nginx, App Server, PostgreSQL).

    PC (seu host físico) → acessa a vm-app pela rede externa (vmbr0, faixa 192.168.0.x).

    vm-app (192.168.0.39 / 192.168.100.10)

        Tem Docker, Nginx e sua aplicação.

        Atua como bastion host para acessar a rede interna.

    vm-db (192.168.100.20)

        Só acessível pela rede interna (vmbr1, faixa 192.168.100.x).

        Vai rodar Docker + PostgreSQL.

    Fluxo: PC → vm-app → vm-db.

🚀 Próximos passos

    Instalar Docker na vm-db

        Para rodar o PostgreSQL em container.

    Subir PostgreSQL

        Banco de dados isolado na vm-db.

    Configurar conexão app ↔ DB

        A aplicação na vm-app acessa o PostgreSQL na vm-db.
        
        
---

# 📄 Pacotes Extras Pós-Boot — Ubuntu Server

### 🛠️ Ferramentas básicas
```bash
sudo apt install net-tools htop curl wget git unzip -y
```
- `net-tools` → comandos de rede (`ifconfig`, `netstat`).  
- `htop` → monitoramento de CPU/memória.  
- `curl` e `wget` → downloads e testes HTTP.  
- `git` → controle de versão.  
- `unzip` → manipulação de arquivos compactados.  

---

### 🐘 pgAdmin
```bash
sudo apt install pgadmin4 -y
sudo /usr/pgadmin4/bin/setup-web.sh
```
- Acesse via navegador: `http://192.168.0.25/pgadmin4`.  
- Conecte ao PostgreSQL da `vm-db` pela rede interna.  


---

### 📈 Monitoramento web com Netdata
O Netdata é uma ferramenta de código aberto feita para monitorar a saúde e o desempenho do seu servidor em tempo real. No Ubuntu Server 24, ele funciona coletando milhares de métricas a cada segundo e exibindo tudo em um painel web interativo e automático.

- Interface web em `http://192.168.0.25:19999`.  
- Monitoramento em tempo real de CPU, memória, disco e rede.  

---

### 🔒 Segurança extra com fail2ban
- Protege contra tentativas de login SSH.  
- Configuração padrão já cobre ataques de força bruta.  

---

### 🛡️ Firewall UFW

sudo ufw allow ssh
sudo ufw allow http
sudo ufw allow https
sudo ufw enable


---

### 🗄️ Backup e snapshots
- Configurar snapshots regulares no Proxmox.  
- Usar `backup-hdd` para armazenar dumps da VM.  

---

## ✅ Resultado
- Ferramentas essenciais instaladas.  
- pgAdmin acessível via navegador.  
- Monitoramento ativo com Netdata.  
- Segurança reforçada com UFW e fail2ban.  
- Ambiente pronto para operação contínua e manutenção.  


        