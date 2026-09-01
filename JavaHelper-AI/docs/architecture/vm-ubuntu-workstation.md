
## 📄 Guia para a vm-ubuntu-workstation com IP fixo

###
graph TD
    subgraph Rede Externa (vmbr0 - 192.168.0.x)
        PC[pvc-lab\n192.168.0.5]
        Workstation[vm-ubuntu-workstation\n192.168.0.25]
        App[vm-app\n192.168.0.10]
    end

    subgraph Rede Interna (vmbr1 - 192.168.100.x)
        WorkstationInt[vm-ubuntu-workstation\n192.168.100.25]
        DB[vm-db\n192.168.100.20]
        AppInt[vm-app\n192.168.100.10]
    end

    PC -->|HTTP/SSH| Workstation
    PC -->|HTTP/SSH| App
    WorkstationInt -->|pgAdmin| DB
    AppInt -->|API / Nginx| DB

📄 Explicação do diagrama

    Rede externa (vmbr0): conecta o PC físico (192.168.0.5), a workstation (192.168.0.25) e a vm-app (192.168.0.10).

    Rede interna (vmbr1): conecta a workstation (192.168.100.25), a vm-db (192.168.100.20) e a vm-app (192.168.100.10).

    O pgAdmin na workstation acessa o PostgreSQL da vm-db pela rede interna.

    O PC físico acessa a workstation e a vm-app pela rede externa.
### 1. Escolha do IP
- Rede externa (`vmbr0`): sugiro `192.168.0.25` (abaixo de `.30` e diferente do `.5` do seu PC).  
- Rede interna (`vmbr1`): pode ser `192.168.100.25` (mantendo consistência).  

### 2. Configuração Netplan
Edite o arquivo:
```bash
sudo nano /etc/netplan/01-netcfg.yaml
```

Exemplo:
```yaml
network:
  version: 2
  ethernets:
    ens18:   # interface ligada ao vmbr0
      addresses:
        - 192.168.0.25/24
      gateway4: 192.168.0.1
      nameservers:
        addresses: [8.8.8.8, 1.1.1.1]
    ens19:   # interface ligada ao vmbr1
      addresses:
        - 192.168.100.25/24
```

Aplicar:
```bash
sudo netplan apply
```

### 3. Instalar pgAdmin
```bash
sudo apt update && sudo apt upgrade -y
sudo apt install pgadmin4 -y
sudo /usr/pgadmin4/bin/setup-web.sh
```

### 4. Acesso
- Via navegador: `http://192.168.0.25/pgadmin4`  
- Conexão ao banco:  
  - Host: `192.168.100.20`  
  - Porta: `5432`  
  - Usuário: `postgres`  
  - Senha: definida no container da `vm-db`.  

---

## ✅ Resultado
- A `vm-ubuntu-workstation` terá IP fixo fora do range DHCP.  
- O pgAdmin ficará acessível via navegador do seu PC físico (`192.168.0.5`).  
- Comunicação segura com a `vm-db` pela rede interna (`vmbr1`).  

---

Golbery, perfeito 👌 — aqui está o **guia consolidado da instalação da vm-ubuntu-workstation** com todas as configurações que definimos juntos. Você pode incluir esse documento dentro da própria VM (por exemplo em `/docs/vm-ubuntu-workstation.md`) para manter o histórico técnico.

---

# 📄 Guia de Instalação — vm-ubuntu-workstation

## 1. Criação da VM no Proxmox
- **VM ID**: 102 (ou outro livre)  
- **Name**: `vm-ubuntu-workstation`  
- **ISO**: `ubuntu-22.04-server.iso`  
- **Firmware**: OVMF (UEFI) com EFI Disk em `local-lvm`  
- **SCSI Controller**: VirtIO SCSI single  
- **Disk**: `local-lvm`, 40 GB, formato qcow2, IO thread habilitado  
- **CPU**: 1 socket × 2 cores, tipo `x86-64-v2-AES`  
- **Memory**: 4096 MiB (4 GB)  
- **Network**:  
  - net0 → VirtIO, bridge `vmbr0`, firewall ativo  
  - net1 → VirtIO, bridge `vmbr1` (adicionar após criação)  

---

## 2. Instalação do Ubuntu Server
- Instalar **Ubuntu Server 22.04 LTS**.  
- Criar usuário administrador e senha.  
- Não instalar interface gráfica (somente server).  

---

## 3. Configuração de IP fixo
Como o DHCP inicia em `.30` e o PC físico está em `.5`, usamos IPs abaixo de `.30`:

- **vmbr0 (externa)** → `192.168.0.25`  
- **vmbr1 (interna)** → `192.168.100.25`  

Editar Netplan:
```yaml
network:
  version: 2
  ethernets:
    ens18:
      addresses:
        - 192.168.0.25/24
      gateway4: 192.168.0.1
      nameservers:
        addresses: [8.8.8.8, 1.1.1.1]
    ens19:
      addresses:
        - 192.168.100.25/24
```

Aplicar:
```bash
sudo netplan apply
```

---

## 4. Testes de conectividade
- Do PC físico (`192.168.0.5`):
```bash
ping 192.168.0.25
```
- Da VM workstation:
```bash
ping 192.168.100.20
```

---

## 5. Instalação do pgAdmin
```bash
sudo apt update && sudo apt upgrade -y
sudo apt install pgadmin4 -y
sudo /usr/pgadmin4/bin/setup-web.sh
```

- Acesso via navegador: `http://192.168.0.25/pgadmin4`  
- Conexão ao banco:  
  - Host: `192.168.100.20`  
  - Porta: `5432`  
  - Usuário: `postgres`  
  - Senha: definida no container da `vm-db`  

---

## ✅ Resultado
- VM criada com **Ubuntu Server**.  
- IP fixo fora do range DHCP.  
- pgAdmin acessível via navegador do PC físico.  
- Comunicação segura com `vm-db` pela rede interna.  

---

# 📄 Checklist de Monitoramento — Ubuntu Server

### 📊 Recursos do sistema
- Instalar ferramentas de monitoramento:
  ```bash
  sudo apt install htop sysstat -y
  ```
- Usar:
  - `htop` → monitorar CPU, memória e processos em tempo real.  
  - `iostat` → verificar desempenho de disco.  
  - `free -h` → checar uso de memória.  

---

### 📜 Logs do sistema
- Verificar logs críticos:
  ```bash
  sudo journalctl -xe
  sudo tail -f /var/log/syslog
  ```
- Configurar rotação de logs com `logrotate` (já vem instalado por padrão).  

---

### 🛡️ Segurança e acessos
- Monitorar tentativas de login:
  ```bash
  sudo tail -f /var/log/auth.log
  ```
- Usar **fail2ban** para bloquear IPs suspeitos.  
- Revisar regras do firewall (`sudo ufw status`).  

---

### 📈 Serviços e rede
- Testar conectividade com a `vm-db`:
  ```bash
  ping 192.168.100.20
  ```
- Verificar portas abertas:
  ```bash
  sudo ss -tulwn
  ```
- Monitorar status do pgAdmin:
  ```bash
  sudo systemctl status apache2
  ```

---

### 🔔 Alertas e notificações
- Instalar **netdata** para monitoramento web em tempo real:
  ```bash
  sudo apt install netdata -y
  ```
- Configurar envio de alertas por e-mail (via `postfix` ou `msmtp`).  
- Integrar com Proxmox para alertas de uso de CPU/RAM.  

---

## ✅ Resultado
- Monitoramento ativo de CPU, memória, disco e rede.  
- Logs e acessos sob controle.  
- Alertas configurados para incidentes.  
- Ambiente seguro e pronto para produção.  

---
