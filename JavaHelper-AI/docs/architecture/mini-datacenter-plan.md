# Mini Datacenter Plan

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
✅ O que o diagrama mostra

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