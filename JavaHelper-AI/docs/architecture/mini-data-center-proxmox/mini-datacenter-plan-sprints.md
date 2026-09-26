Sprint 04 — Administração profissional do PostgreSQL

Objetivo:

Plain Text
PC (DBeaver)
│
▼
vm-ubuntu-workstation (Bastion)
│
▼
vm-db (PostgreSQL)
Show more lines

Entregas:

Instalar DBeaver no seu PC.
Criar túnel SSH.
Conectar PostgreSQL.
Documentar arquitetura Bastion.
Criar guia operacional.

Após isso, eu abriria a próxima sprint:

Plain Text
Sprint 05
Instalação do n8n


Perfeito, Software Engineer. 🚀

Agora vamos fazer isso do jeito que um DBA/Cloud Engineer faria em um ambiente corporativo.

✅ Primeiro: vamos validar a arquitetura

Hoje temos:

PC
192.168.0.34
│
├── DBeaver
│
▼
vm-ubuntu-workstation
192.168.0.25
192.168.100.25
│
▼
vm-db
192.168.100.20
PostgreSQL


A ideia NÃO é permitir:

PC
↓
192.168.100.20


Porque isso quebraria a proposta da rede privada.

A ideia é:

PC
↓ SSH Tunnel
vm-ubuntu-workstation
↓
vm-db

Sprint 04.1
Validar SSH da workstation

No Windows:

ssh golbery@192.168.0.25


Se entrar:

golbery@vm-ubuntu-workstation:~$


✅ aprovado

Sprint 04.2
Testar se a workstation alcança o PostgreSQL

Na vm-workstation:

nc -zv 192.168.100.20 5432


ou

telnet 192.168.100.20 5432


Resultado esperado:

Connected


✅ aprovado

Sprint 04.3
Configurar Tunnel no DBeaver

Na conexão PostgreSQL:

Main

Host:

192.168.100.20


Port:

5432


Database:

appdb


User:

devops


Password:

********

SSH

Clique:

SSH, Proxy


Marque:

Use SSH Tunnel


Preencher:

SSH Host
192.168.0.25

Port
22

User
golbery

Authentication Method
Password


Senha:

senha_da_workstation

Estrutura final no DBeaver
SSH Host:
192.168.0.25

SSH Port:
22

SSH User:
golbery

Database Host:
192.168.100.20

Database Port:
5432

Database:
appdb

User:
devops

O que vai acontecer

Quando clicar:

Test Connection


o fluxo será:

DBeaver
 ↓
SSH
 ↓
192.168.0.25
 ↓
PostgreSQL
 ↓
192.168.100.20


exatamente como numa infraestrutura corporativa.


Antes de avançarmos para:

UFW
Fail2Ban
Netdata
Backup


eu fecharia primeiro:

✅ DBeaver
✅ SSH Tunnel
✅ Documentação Bastion Host


Porque isso completa a camada de administração do banco.

Depois disso considero a Sprint 04 encerrada e passamos para:

Sprint 05
Operação e Segurança

• UFW
• Fail2Ban
• PostgreSQL Backup
• Netdata

