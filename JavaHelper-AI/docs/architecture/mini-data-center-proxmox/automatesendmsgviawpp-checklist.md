# ✅ Checklist - Deploy e Execução do automatesendmsgviawpp no Servidor de Aplicações

**Servidor:** `vm-app` (Ubuntu 24.04 LTS - Noble)  
**IP Externo:** `192.168.0.12`  
**Abordagem:** Docker (Selenium Standalone Chrome + noVNC) + Script Python

---

## 📋 Fase 1: Infraestrutura e Container noVNC (`vm-app`)
- [x] Conectar via SSH na `vm-app`:
  ```bash
  ssh devops@192.168.0.12
  ```
- [x] Criar diretório de persistência do perfil do Chrome:
  ```bash
  mkdir -p ~/selenium-chrome-profile
  ```
- [x] Resolução de problema (Troubleshooting): Expansão do volume lógico LVM para corrigir erro `no space left on device`:
  ```bash
  sudo lvextend -l +100%FREE /dev/mapper/ubuntu--vg-ubuntu--lv
  sudo resize2fs /dev/mapper/ubuntu--vg-ubuntu--lv
  ```
- [x] Iniciar o container Docker `selenium/standalone-chrome`:
  ```bash
  docker run -d --name selenium-chrome \
    --restart always \
    -p 4444:4444 \
    -p 7900:7900 \
    --shm-size="2g" \
    -v ~/selenium-chrome-profile:/home/seluser/.config/google-chrome \
    selenium/standalone-chrome:latest
  ```
- [x] Validar status do container (`docker ps` - container `selenium-chrome` ativo)
- [x] Validar acesso à interface noVNC no navegador do PC:
  - URL: `http://192.168.0.12:7900`
  - Senha: `secret`
  - Status: Conectado com sucesso (área de trabalho exibida).

---

## 💻 Fase 2: Adaptação e Versionamento do Código (Repositório Local)
- [x] Criar arquivo `requirements.txt` com as dependências necessárias (`selenium`).
- [x] Atualizar script Python para usar `webdriver.Remote` apontando para o Selenium:
  - Host padrão: `http://localhost:4444/wd/hub` (quando rodar dentro da `vm-app`)
  - Suporte a variável de ambiente `SELENIUM_REMOTE_URL`
  - Opções do Chrome adequadas para ambiente servidor (`--no-sandbox`, `--disable-dev-shm-usage`).
- [x] Adicionar sanitização da lista de contatos (remoção de parênteses, traços, espaços e inclusão do DDI 55).
- [x] Configurar usuário do Git e testar integridade.
- [x] Fazer commit e push para o GitHub (`origin/main`):
  - Commit: `9ffcb6e` ("feat: suporte a selenium remote, requirements.txt e sanitizacao de telefones")
  - Enviado com sucesso para `https://github.com/golberysantos/automatesendmsgviawpp.git`.

---

## 🚀 Fase 3: Deploy e Construção da Imagem Docker na `vm-app`
- [x] Clonar o repositório no `vm-app` via `git clone`:
  ```bash
  git clone https://github.com/golberysantos/automatesendmsgviawpp.git
  cd automatesendmsgviawpp
  ```
- [x] Substituir o container isolado pelo ecossistema do `docker-compose`:
  ```bash
  docker stop selenium-chrome && docker rm selenium-chrome
  docker compose up -d selenium-chrome
  ```
- [x] Construir a imagem da aplicação Python:
  ```bash
  docker compose build
  ```

---

## 📱 Fase 4: Autenticação e Execução do WhatsApp Web
- [x] Abrir a tela do noVNC no navegador do PC: `http://192.168.0.12:7900`
- [x] Executar o container da aplicação em modo interativo:
  ```bash
  docker compose run --rm wpp-app
  ```
- [x] Acompanhar pelo noVNC o carregamento do WhatsApp Web (sessão iniciada).
- [x] Desacoplamento da Mensagem: carregamento de `data/mensagem.txt` com política Fail-Safe (sem texto chumbado).
- [ ] Escanear o QR Code exibido na tela com o aplicativo do WhatsApp no celular.
- [ ] Pressionar `Enter` no terminal da `vm-app` após confirmação do login.
- [ ] Realizar teste de envio com um número de teste.
- [ ] Validar a persistência de login (interromper e reexecutar sem exigir novo QR Code).
