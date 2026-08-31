
## 📄 Guia atualizado para a vm-ubuntu-workstation com IP fixo

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

👉 Quer que eu já prepare um **diagrama Mermaid atualizado** (diagramar vm-ubuntu-workstation com IP fixo) mostrando os IPs estáticos (`.25` e `.100.25`) junto da `vm-app` e `vm-db`?