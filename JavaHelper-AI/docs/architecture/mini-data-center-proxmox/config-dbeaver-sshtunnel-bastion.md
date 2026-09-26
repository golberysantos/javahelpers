Guia Operacional: DBeaver + SSH Tunnel (Bastion Host)

Este documento descreve a arquitetura, configuração e operação do acesso ao PostgreSQL privado utilizando DBeaver + SSH Tunnel, mantendo o banco isolado na rede interna.

Objetivo

Permitir que um usuário no PC local (192.168.0.34) acesse com segurança o PostgreSQL localizado na VM vm-db (192.168.100.20), utilizando a vm-ubuntu-workstation (192.168.0.25 / 192.168.100.25) como Bastion Host.

Arquitetura
┌─────────────────────┐
│ PC 192.168.0.34     │
│ DBeaver             │
└──────────┬──────────┘
           │ SSH Tunnel
           ▼
┌─────────────────────┐
│ vm-ubuntu-workstation
│ 192.168.0.25        │
│ 192.168.100.25      │
│ Bastion Host        │
└──────────┬──────────┘
           │ PostgreSQL
           ▼
┌─────────────────────┐
│ vm-db               │
│ 192.168.100.20      │
│ PostgreSQL          │
└─────────────────────┘

Visão da Infraestrutura
Rede Externa (vmbr0)
192.168.0.0/24


Equipamentos conectados:

PC Local                  192.168.0.34
vm-app                    192.168.0.12
vm-ubuntu-workstation     192.168.0.25

Rede Interna (vmbr1)
192.168.100.0/24


Equipamentos conectados:

vm-app                    192.168.100.12
vm-db                     192.168.100.20
vm-ubuntu-workstation     192.168.100.25

Fluxo de Conexão

O tráfego segue exatamente este caminho:

PC 192.168.0.34
      │
      ▼
DBeaver
      │
      ▼
SSH Tunnel
      │
      ▼
vm-ubuntu-workstation
(192.168.0.25)
      │
      ▼
192.168.100.20:5432
(PostgreSQL)
      │
      ▼
vm-db


O PC nunca acessa diretamente o banco.

O PostgreSQL permanece acessível apenas pela rede privada.

Vantagens da Arquitetura
Segurança

O PostgreSQL não fica exposto na rede externa.

Não existe acesso:

192.168.0.34  ---> 192.168.100.20


O acesso ocorre somente através do SSH.

Bastion Host

A workstation atua como ponto único de entrada administrativa.

PC
 ↓
Bastion
 ↓
Banco


Benefícios:

Centralização dos acessos;
Auditoria simplificada;
Menor superfície de ataque;
Controle de usuários SSH.
Isolamento de Rede

O banco permanece exclusivamente na rede interna:

192.168.100.0/24


Sem necessidade de:

NAT;
Port Forward;
Exposição de porta PostgreSQL;
VPN para acesso administrativo.
Confiabilidade Operacional

Caso seja necessário alterar o banco de dados futuramente:

vm-db antigo
    ↓
vm-db novo


Somente o bastion precisa conhecer o novo endereço.

As estações continuam conectando via SSH Tunnel.

Pré-Requisitos
No PC

Instalar:

DBeaver Community ou Enterprise
Cliente SSH nativo

Teste:

ssh usuario@192.168.0.25


Se conectar normalmente, o túnel funcionará.

Na vm-ubuntu-workstation

Verificar:

sudo systemctl status ssh


Deve mostrar:

active (running)

Testar acesso ao PostgreSQL

Logado na workstation:

nc -zv 192.168.100.20 5432


Resultado esperado:

Connection to 192.168.100.20 5432 succeeded

Configuração do DBeaver
Nova Conexão PostgreSQL

Menu:

Database
↓
New Database Connection
↓
PostgreSQL

Aba Main

Preencher:

Host
192.168.100.20

Port
5432

Database
postgres


ou o banco desejado.

Username
postgres

Password
********

Aba SSH

Marcar:

Use SSH Tunnel

SSH Host
192.168.0.25

SSH Port
22

User Name

Exemplo:

ubuntu


ou o usuário administrativo definido na workstation.

Authentication Method
Opção A - Senha
Password


Informar:

senha_do_usuario

Opção B - Chave SSH (Recomendado)
Public Key


Selecionar:

id_rsa
ou
id_ed25519

Resumo dos Campos
PostgreSQL
Host........: 192.168.100.20
Port........: 5432
Database....: postgres
User........: postgres
Password....: ********

SSH Tunnel
Use SSH Tunnel : ✔

Host..........: 192.168.0.25
Port..........: 22
User..........: ubuntu

Authentication:
Password ou Public Key

Como Funciona Internamente

O DBeaver cria automaticamente um túnel semelhante a:

ssh -L 5432:192.168.100.20:5432 ubuntu@192.168.0.25


Significado:

PC Local
    │
localhost:5432
    │
SSH
    ▼
192.168.0.25
    │
    ▼
192.168.100.20:5432


O PostgreSQL acredita que a conexão veio da workstation.

Validação

Ao clicar em:

Test Connection


O fluxo esperado é:

1. DBeaver abre SSH
2. SSH conecta na workstation
3. Workstation acessa 192.168.100.20:5432
4. PostgreSQL responde
5. Conexão validada


Mensagem esperada:

Connected
Connection established

Troubleshooting
Erro SSH
Cannot establish SSH connection


Verificar:

ssh usuario@192.168.0.25

Erro PostgreSQL
Connection refused


Na workstation:

nc -zv 192.168.100.20 5432

PostgreSQL parado

No vm-db:

sudo systemctl status postgresql


ou container:

docker ps

Firewall

Verificar no vm-db:

sudo ufw status


Liberar somente para rede interna:

sudo ufw allow from 192.168.100.0/24 to any port 5432

Boas Práticas Recomendadas
✔ Utilizar chave SSH (ED25519)
Mais segura
Mais rápida
Menor risco

✔ Desabilitar login root via SSH
PermitRootLogin no

✔ Restringir acesso ao Bastion
AllowUsers admin dba suporte

✔ Banco somente na rede privada
listen_addresses = '192.168.100.20'


ou

listen_addresses = '*'


com firewall restrito à rede interna.

✔ Não publicar PostgreSQL na rede externa

Evitar:

192.168.0.x:5432


Expor apenas:

192.168.100.20:5432

Conclusão

A arquitetura recomendada é:

PC 192.168.0.34
    ↓
DBeaver
    ↓
SSH Tunnel
    ↓
vm-ubuntu-workstation (192.168.0.25)
    ↓
Rede privada 192.168.100.0/24
    ↓
vm-db (192.168.100.20)
    ↓
PostgreSQL


Essa abordagem preserva o isolamento da rede privada, utiliza um Bastion Host para acesso administrativo e mantém o PostgreSQL protegido sem exposição direta à rede externa.


## Topologia das Redes
flowchart TB

    subgraph EXTERNA["vmbr0 - Rede Externa (192.168.0.0/24)"]

        PC["PC<br/>192.168.0.34"]

        APP0["vm-app<br/>192.168.0.12"]

        WS0["vm-ubuntu-workstation<br/>192.168.0.25"]

    end

    subgraph INTERNA["vmbr1 - Rede Privada (192.168.100.0/24)"]

        APP1["vm-app<br/>192.168.100.12"]

        WS1["vm-ubuntu-workstation<br/>192.168.100.25"]

        DB["vm-db<br/>192.168.100.20"]

    end

    PC --> WS0

    WS0 --- WS1

    APP0 --- APP1


## Fluxo Operacional

sequenceDiagram

    participant PC as PC 192.168.0.34
    participant DBeaver
    participant SSH as vm-ubuntu-workstation
    participant PG as PostgreSQL 16

    PC->>DBeaver: Abrir Conexão
    DBeaver->>SSH: SSH Tunnel (ED25519)
    SSH->>PG: TCP 5432
    PG-->>SSH: Connection Accepted
    SSH-->>DBeaver: Tunnel Ready
    DBeaver-->>PC: Connected

Bastion Host
vm-ubuntu-workstation
Item	ValorIP Externo	192.168.0.25
IP Interno	192.168.100.25
Serviço	OpenSSH Server
Função	Bastion Host
Banco de Dados
vm-db
Item	ValorIP	192.168.100.20
Banco	PostgreSQL 16
Execução	Docker
Porta	5432
Configuração DBeaver
PostgreSQL
Plain Text
Host........: 192.168.100.20
Port........: 5432
Database....: postgres
User........: postgres
Password....: ********
Show more lines
SSH Tunnel
Plain Text
Use SSH Tunnel.....: Sim
 
Host/IP............: 192.168.0.25
Port...............: 22
Username...........: golbery
 
Authentication.....: Public Key
Private Key........: C:\Users\User\.ssh\id_ed25519
Show more lines
Configuração SSH
Geração da Chave

Windows PowerShell:

PowerShell
ssh-keygen -t ed25519 -C "golbery@dbeaver"
Show more lines
Instalação da Chave

Servidor:

Shell
mkdir -p ~/.ssh
chmod 700 ~/.ssh
Show more lines

Adicionar ao arquivo:

Shell
~/.ssh/authorized_keys
Show more lines

Permissões:

Shell
chmod 600 ~/.ssh/authorized_keys
Show more lines
Configuração do SSHD

Arquivo:

Shell
/etc/ssh/sshd_config
Show more lines

Configuração mínima:

Plain Text
PubkeyAuthentication yes
AuthorizedKeysFile .ssh/authorized_keys
Show more lines

Validar:

Shell
sudo sshd -t
Show more lines

Aplicar:

Shell
sudo systemctl reload ssh
Show more lines
Firewall
vm-db

Liberar PostgreSQL apenas para a workstation:

Shell
sudo ufw allow from 192.168.100.25 to any port 5432
Show more lines

Verificar:

Shell
sudo ufw status
Show more lines

Resultado esperado:

Plain Text
5432 ALLOW IN 192.168.100.25
Show more lines
Testes de Validação
Teste SSH
PowerShell
ssh golbery@192.168.0.25
Show more lines
Teste de Chave
PowerShell
ssh -i $env:USERPROFILE\.ssh\id_ed25519 golbery@192.168.0.25
Show more lines
Teste PostgreSQL

Na workstation:

Shell
nc -zv 192.168.100.20 5432
Show more lines

Resultado esperado:

Plain Text
Connection succeeded
Show more lines
Teste DBeaver
Plain Text
Server: PostgreSQL 16.x
Status: Connected
Show more lines
Troubleshooting
Erro: SSH Public Key Authentication Failed

Mensagem:

Plain Text
SSH public key authentication failed
Exhausted available authentication methods
Show more lines

Verificar:

Shell
grep PubkeyAuthentication /etc/ssh/sshd_config
Show more lines

Resultado esperado:

Plain Text
PubkeyAuthentication yes
Show more lines

Validar:

Shell
sudo sshd -T | grep pubkey
Show more lines
Erro: Connection Refused na Porta 5432

Teste:

Shell
nc -zv 192.168.100.20 5432
Show more lines

Se retornar:

Plain Text
Connection refused
Show more lines

Verificar:

Shell
docker ps
Show more lines

e

Shell
sudo ss -tulpn | grep 5432
Show more lines

Resultado esperado:

Plain Text
0.0.0.0:5432
Show more lines
Erro: PostgreSQL Container Parado

Verificar:

Shell
docker ps -a
Show more lines

Iniciar:

Shell
docker start postgres
Show more lines

Configurar reinício automático:

Shell
docker update --restart unless-stopped postgres
Show more lines
Erro: Chave Não Funciona

Verificar conteúdo:

Windows:

PowerShell
Get-Content $env:USERPROFILE\.ssh\id_ed25519.pub
Show more lines

Linux:

Shell
cat ~/.ssh/authorized_keys
Show more lines

Os conteúdos devem ser idênticos.

Erro: Permission Denied (Publickey)

Validar permissões:

Shell
ls -la ~/.ssh
Show more lines

Esperado:

Plain Text
drwx------ .ssh
-rw------- authorized_keys
Show more lines

Corrigir:

Shell
chmod 700 ~/.ssh
chmod 600 ~/.ssh/authorized_keys
Show more lines
Erro: DBeaver Não Conecta via Chave

Verificar:

Plain Text
Authentication Method:
Public Key
Show more lines

Arquivo correto:

Plain Text
id_ed25519
Show more lines

Arquivo incorreto:

Plain Text
id_ed25519.pub
Show more lines
Hardening SSH Checklist
Chaves
 Utilizar ED25519
 Utilizar authorized_keys
 Diretório .ssh protegido
 Arquivo authorized_keys protegido
SSHD
 PubkeyAuthentication yes
 PasswordAuthentication no
 PermitRootLogin no
 AllowUsers golbery
 MaxAuthTries 3
 LoginGraceTime 30
Firewall
 PostgreSQL acessível apenas pelo Bastion
 SSH limitado aos IPs administrativos
 Fail2ban instalado
Operação
 PostgreSQL privado
 Bastion Host operacional
 SSH Tunnel validado
 DBeaver conectado
 Segmentação de rede implementada
Resultado Final
Plain Text
PC 192.168.0.34
│
▼
DBeaver
│
▼
SSH Tunnel (ED25519)
│
▼
vm-ubuntu-workstation
192.168.0.25
│
▼
192.168.100.25
│
▼
vm-db
192.168.100.20
│
▼
PostgreSQL 16
Show more lines

O PostgreSQL permanece isolado na rede privada e só pode ser acessado através do Bastion Host utilizando autenticação SSH por chave pública ED25519.