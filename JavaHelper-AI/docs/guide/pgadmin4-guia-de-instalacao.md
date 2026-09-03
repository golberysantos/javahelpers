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
