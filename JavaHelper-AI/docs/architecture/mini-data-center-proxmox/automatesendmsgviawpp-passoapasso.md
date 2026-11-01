

### 🗺️ Roteiro Geral das Etapas

1. **Passo 1:** Subir o container Docker do **Selenium Chrome com noVNC** na `vm-app` e validar o acesso no navegador do seu PC.
2. **Passo 2:** Ajustar o código do repositório local [automatesendmsgviawpp](file:///d:/TOSHIBA320GB/wamp/www/gos/automatesendmsgviawpp) (adaptar para `Remote WebDriver`, adicionar `requirements.txt` e persistência) e enviar para o GitHub (`git push`).
3. **Passo 3:** Conectar na `vm-app`, clonar o repositório (`git clone`) e preparar o ambiente virtual Python (`venv`).
4. **Passo 4:** Executar o script na `vm-app`, visualizar o WhatsApp Web pelo noVNC no navegador do PC, autenticar o QR Code e validar o envio.

---

## 🚀 Passo 1: Subir o Selenium Standalone Chrome no Servidor (`vm-app`)

O container do Selenium já traz o Google Chrome, ChromeDriver, servidor VNC e o **noVNC** (acesso web) pré-configurados.

### 1.1. Conectar via SSH na `vm-app`
No terminal do seu PC, acesse o servidor de aplicações:
```bash
ssh golbery@192.168.0.12
```

---

### 1.2. Criar a pasta de persistência e iniciar o container
No terminal da `vm-app`, execute:

```bash
mkdir -p ~/selenium-chrome-profile

docker run -d --name selenium-chrome \
  --restart always \
  -p 4444:4444 \
  -p 7900:7900 \
  --shm-size="2g" \
  -v ~/selenium-chrome-profile:/home/seluser/.config/google-chrome \
  selenium/standalone-chrome:latest
```

> **Por que esses parâmetros?**
> * `-p 4444:4444`: Porta onde o script Python enviará os comandos do Selenium.
> * `-p 7900:7900`: Porta da interface gráfica noVNC (para visualizar o navegador pelo seu browser).
> * `--shm-size="2g"`: Evita travamentos de memória compartilhada do Chrome.
> * `-v ~/selenium-chrome-profile:...`: Salva o perfil do Chrome no disco do servidor, garantindo que o login do WhatsApp permaneça salvo.

---

### 1.3. Validar a execução
1. No terminal da `vm-app`, verifique se o container está `Up`:
   ```bash
   docker ps
   ```
2. No navegador do seu computador físico, acesse:
   **`http://192.168.0.12:7900`**
3. Clique em **Connect** e digite a senha padrão: `secret`.
4. Uma tela com uma área de trabalho limpa deverá aparecer na janela do navegador.

✅ **Status:** Concluído com sucesso! Interface noVNC acessível e pronta.

---

### 1.4. Troubleshooting Registrado: Erro de Disco Cheio (`no space left on device`)
* **Problema:** Durante o download da imagem `selenium/standalone-chrome:latest`, o Docker retornou o erro `write ... no space left on device`.
* **Causa:** O instalador do Ubuntu Server alocou inicialmente apenas 9,8 GB para o volume raiz `/dev/mapper/ubuntu--vg-ubuntu--lv`, apesar de o disco no Proxmox possuir 20 GB alocados.
* **Solução aplicada (On-line sem reinicialização):**
  1. Limpeza de containers e imagens paradas:
     ```bash
     docker container prune -f
     docker image prune -a -f
     sudo apt clean && sudo apt autoremove -y
     sudo journalctl --vacuum-size=100M
     ```
  2. Expansão do volume lógico e sistema de arquivos LVM:
     ```bash
     sudo lvextend -l +100%FREE /dev/mapper/ubuntu--vg-ubuntu--lv
     sudo resize2fs /dev/mapper/ubuntu--vg-ubuntu--lv
     ```
  3. Resultado: Espaço expandido com sucesso de **9,8 GB** para **17,32 GB**, liberando espaço suficiente para baixar a imagem e subir o container.

---

## 💻 Passo 2: Adaptação e Versionamento do Código Local

1. Criado o arquivo [requirements.txt](file:///d:/TOSHIBA320GB/wamp/www/gos/automatesendmsgviawpp/requirements.txt) (`selenium>=4.20.0`).
2. Atualizado o script [msgwpp.py](file:///d:/TOSHIBA320GB/wamp/www/gos/automatesendmsgviawpp/msgwpp.py) com suporte a WebDriver Remoto, sanitização de números e resiliência de seletores.
3. Criado o arquivo [.dockerignore](file:///d:/TOSHIBA320GB/wamp/www/gos/automatesendmsgviawpp/.dockerignore) e o [Dockerfile](file:///d:/TOSHIBA320GB/wamp/www/gos/automatesendmsgviawpp/Dockerfile) baseado em `python:3.12-slim`.
4. Criado o arquivo [docker-compose.yml](file:///d:/TOSHIBA320GB/wamp/www/gos/automatesendmsgviawpp/docker-compose.yml) orquestrando o `selenium-chrome` e o `wpp-app` na mesma rede Docker (`wpp-network`).
5. Realizado commit (`d168792`) e push para o repositório remoto (`origin/main`).

✅ **Status:** Concluído com sucesso!

---

## 🚀 Passo 3: Deploy e Construção da Imagem Docker na `vm-app`

Agora o servidor de aplicações rodará 100% conteinerizado via Docker Compose, sem precisar instalar pacotes Python no sistema operacional:

### 3.1. Clonar o repositório atualizado:
No terminal da `vm-app` (`ssh devops@192.168.0.12`):
```bash
git clone https://github.com/golberysantos/automatesendmsgviawpp.git
cd automatesendmsgviawpp
```

### 3.2. Migrar o Selenium para a rede gerenciada do Compose:
Como já subimos o container no Passo 1, vamos substituí-lo para integrar na rede do Compose:
```bash
docker stop selenium-chrome && docker rm selenium-chrome
docker compose up -d selenium-chrome
```
*(O volume de persistência `~/selenium-chrome-profile` continua intacto)*.

### 3.3. Construir a imagem da aplicação Python:
```bash
docker compose build
```

✅ **Status:** Concluído com sucesso! Imagem `automatesendmsgviawpp-wpp-app:latest` construída em 16s.

---

## 📱 Passo 4: Autenticação e Execução

### 4.1. Abrir o noVNC no navegador do PC
Acesse: **`http://192.168.0.12:7900`** (senha: `secret`).
*(Mantenha essa aba aberta ao lado do seu terminal)*.

### 4.2. Iniciar a automação em modo interativo
No terminal da `vm-app` (`~/automatesendmsgviawpp`):
```bash
git pull
docker compose build
docker compose run --rm wpp-app
```

### 4.3. Novo Fluxo de Arquitetura & UX:
1. **Entrada de Contatos:** O script lê o arquivo [contatos.csv](file:///d:/TOSHIBA320GB/wamp/www/gos/automatesendmsgviawpp/data/contatos.csv) montado via volume `./data:/app/data`.
2. **Mensagem Externa Desacoplada & Fail-Safe:** O texto do disparo fica no arquivo [mensagem.txt](file:///d:/TOSHIBA320GB/wamp/www/gos/automatesendmsgviawpp/data/mensagem.txt).
   * Pode ser editado livremente sem tocar no código Python.
   * Suporta a tag `{nome}` para personalização dinâmica.
   * Exibe preview no terminal antes do envio.
   * **Política Fail-Safe:** Sem mensagem chumbada no código; se o arquivo não existir ou estiver vazio, a automação aborta a execução para evitar envios acidentais (*Silent Fallback Hazard*).
3. **Envio via URL Direta (Padrão Ouro):** 
   - A automação navega diretamente para `https://web.whatsapp.com/send?phone=...&text=...`.
   - Abre o chat instantaneamente com a mensagem pré-carregada e clica em Enviar.
   - Detecta números inexistentes/sem WhatsApp através do popup de erro nativo.
4. **Persistência em Tempo Real:** 
   - Conforme cada número é processado, o status no CSV é atualizado (`ENVIADO`, `INVALIDO`) com data e hora.
   - Se a execução for interrompida e reiniciada, ela pula quem já foi processado e continua apenas os `PENDENTES`.
5. **Intervalo Anti-Bloqueio:** Pausa randômica de 6 a 12 segundos entre disparos para segurança da conta.

---

### 4.4. Troubleshooting Registrado: Permissões de Volume e Kernel 6.8 (Ubuntu 24.04)
* **Sintoma:** Erro `SessionNotCreatedException: session not created: Chrome instance exited` e mensagem `chrome_crashpad_handler: --database is required`.
* **Causas e Soluções aplicadas:**
  1. **Seccomp / Syscalls do Kernel 6.8:** O Ubuntu 24.04 bloqueia chamadas de sistema não privilegiadas do Chrome por padrão.  
     *Solução:* Adicionado `security_opt: [seccomp:unconfined]` no serviço `selenium-chrome` do [docker-compose.yml](file:///d:/TOSHIBA320GB/wamp/www/gos/automatesendmsgviawpp/docker-compose.yml).
  2. **Aceleração Gráfica em Ambiente Server:** Chrome tentava carregar hardware de GPU inexistente na VM.  
     *Solução:* Adicionado `--disable-gpu` nas opções do Chrome no [msgwpp.py](file:///d:/TOSHIBA320GB/wamp/www/gos/automatesendmsgviawpp/msgwpp.py).
  3. **Permissão do Volume Montado (`seluser` vs `root`):** O volume nomeado foi criado com permissões `root:root`, impedindo o usuário `seluser` (UID 1200) de gravar o perfil e o Crashpad do Chrome.  
     *Solução:* Ajustado o dono da pasta com:
     ```bash
     docker exec -u 0 selenium-chrome chown -R seluser:seluser /home/seluser
     ```
* **Resultado:** Chrome inicializou com sucesso e a aplicação conectou no Selenium Grid.





