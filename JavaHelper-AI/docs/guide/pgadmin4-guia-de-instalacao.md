# Guia de Instalação do pgAdmin 4 (Web) no Ubuntu 24.04 LTS (Noble Numbat)

Este documento registra o passo a passo definitivo para instalação do repositório oficial e do ambiente web do pgAdmin 4, contornando falhas comuns de download de chaves GPG por bloqueios de rede.

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

## Passo 4: Instalar o pgAdmin 4 (Versão Web)
Para ambientes Ubuntu Server (sem interface gráfica), instalamos o pacote web integrado ao servidor HTTP Apache.

```bash
sudo apt install pgadmin4-web -y
```

## Passo 5: Configurar o Usuário Administrador e o Apache
Execute o script oficial de pós-instalação para definir as credenciais de login e mapear o endereço no servidor web.

```bash
sudo /usr/pgadmin4/bin/setup-web.sh
```

**Durante a execução do script:**
1. **Email address**: Defina o e-mail que servirá como nome de usuário (ex: `admin@local.com`).
2. **Password**: Crie uma senha segura (os caracteres não aparecerão na tela enquanto digita).
3. **Configure Apache**: Confirme com `Y` (ou pressione Enter) para que o script crie os caminhos de rede automaticamente.

---

## Como Acessar a Interface
Após a conclusão, abra o navegador de qualquer máquina na mesma rede e acesse:

```text
http://<IP_DO_SEU_SERVIDOR>/pgadmin4
```

*Para descobrir o endereço IP do seu servidor Ubuntu via terminal, execute:*
```bash
ip a s | grep global
```


---

# Conectar com a vm-db (PostgreSQL)

## 🔍 Diagnóstico provável
1. **Interface da vm-workstation**:  
   - Pelo documento, a vm-workstation tem apenas IP externo (`192.168.0.25` em `vmbr0`).  
   - Para falar com a vm-db, ela precisa também estar ligada à rede interna (`vmbr1`).  

2. **Configuração de rede no Proxmox**:  
   - A vm-db está em `vmbr1` (rede interna).  
   - A vm-workstation precisa de uma segunda interface de rede conectada ao mesmo `vmbr1`.  

3. **Rotas**:  
   - Se a vm-workstation não tem IP na rede `192.168.100.x`, ela nunca vai alcançar `192.168.100.20`.  

---

## 🛠️ Solução passo a passo

### 1. Adicionar interface interna na vm-workstation
No Proxmox:
- Abra a configuração da **vm-ubuntu-workstation**.  
- Adicione uma segunda NIC conectada ao **bridge vmbr1**.  

### 2. Configurar IP interno na vm-workstation
Dentro da vm-workstation, configure a nova interface (exemplo `ens19`):

```bash
sudo ip addr add 192.168.100.25/24 dev ens19
sudo ip link set ens19 up
```

Ou configure permanentemente em `/etc/netplan/*.yaml`:

```yaml
network:
  version: 2
  ethernets:
    ens18:
      addresses: [192.168.0.25/24]
      gateway4: 192.168.0.1
    ens19:
      addresses: [192.168.100.25/24]
```

Depois aplique:
```bash
sudo netplan apply
```

### 3. Testar conectividade
Agora da vm-workstation:
```bash
ping 192.168.100.20
```
Se responder, a rede interna está funcionando.

### 4. Configurar pgAdmin
No pgAdmin da vm-workstation:
- **Host:** `192.168.100.20`  
- **Port:** `5432`  
- **User/Password:** conforme configurado no PostgreSQL  

---

## 📌 Resultado esperado
Fluxo completo:
- Seu **PC (192.168.0.34)** acessa a **vm-workstation (192.168.0.25)** via browser.  
- A **vm-workstation** tem acesso tanto à rede externa quanto à interna.  
- O **pgAdmin** na vm-workstation conecta ao PostgreSQL na **vm-db (192.168.100.20)**.  

---
