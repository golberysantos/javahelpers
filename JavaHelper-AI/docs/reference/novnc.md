# noVNC

O noVNC é uma solução de acesso remoto que permite controlar um computador ou servidor através do navegador web, sem precisar instalar um cliente VNC tradicional.

## O que é o noVNC?

O noVNC é um cliente VNC escrito em HTML5, JavaScript e WebSockets que funciona diretamente em navegadores modernos.

Ele atua como uma ponte entre:

Seu navegador (Chrome, Firefox, Edge, Safari)
Um servidor VNC remoto (TigerVNC, TightVNC, RealVNC, x11vnc, etc.)

Arquitetura simplificada:

Navegador
     │
     ▼
   noVNC
     │
(WebSocket)
     │
     ▼
 websockify
     │
(VNC Protocol)
     │
     ▼
Servidor VNC
     │
     ▼
Ambiente gráfico remoto

Principais componentes
1. noVNC

É a interface web.

Responsável por:

Exibir a tela remota
Capturar mouse e teclado
Enviar comandos ao servidor VNC
Funcionar no navegador

Arquivos principais:

index.html
app/
core/
vnc.html

2. Websockify

O protocolo VNC usa TCP tradicional.

Os navegadores trabalham com:

HTTP
HTTPS
WebSocket


Por isso o noVNC precisa do Websockify, que converte:

WebSocket <-> TCP VNC


Exemplo:

Browser
ws://servidor:6080
        │
        ▼
Websockify
        │
        ▼
127.0.0.1:5901

Como funciona uma conexão

Exemplo:

Servidor Linux:

Ubuntu
VNC Server
Porta 5901


Websockify:

Porta 6080


Usuário acessa:

http://servidor:6080


Fluxo:

Browser abre noVNC
noVNC cria conexão WebSocket
Websockify recebe
Websockify conecta ao VNC
Tela aparece no navegador
Portas utilizadas
VNC
5900 + display


Exemplos:

:0 -> 5900
:1 -> 5901
:2 -> 5902

noVNC/Websockify

Geralmente:

6080


ou

6081
8080
443

Instalação no Linux
Ubuntu/Debian

Instalar VNC:

sudo apt update
sudo apt install tigervnc-standalone-server


Instalar noVNC:

sudo apt install novnc


ou:

git clone https://github.com/novnc/noVNC.git


Instalar Websockify:

sudo apt install websockify

Exemplo de inicialização

Servidor VNC:

vncserver :1


Irá escutar:

5901


Iniciar Websockify:

websockify 6080 localhost:5901


Acessar:

http://ip-servidor:6080/vnc.html

Uso com Docker

Um caso muito comum.

Exemplo:

services:
  desktop:
    image: consol/ubuntu-xfce-vnc
    ports:
      - "6080:80"


Após iniciar:

docker compose up -d


Acesso:

http://localhost:6080


Muitos containers já vêm com:

XFCE
Firefox
noVNC
Websockify
VNC Server

integrados.

Recursos do noVNC
Redimensionamento automático

A tela adapta ao navegador:

Scale to Window


Ideal para:

Tablets
Smartphones
Monitores diferentes
Área de transferência

Permite copiar e colar texto entre:

Cliente ↔ Servidor

Tela cheia

Modo Fullscreen:

F11


ou botão da interface.

Teclas especiais

Envia combinações que o navegador normalmente interceptaria:

Ctrl+Alt+Del
Ctrl+Alt+Backspace
Alt+Tab

Múltiplas sessões

Vários usuários podem acessar diferentes displays:

5901
5902
5903

Segurança
Sem HTTPS

Não recomendado:

http://


Os dados transitam sem criptografia.

Com HTTPS

Recomendado:

https://


Exemplo:

websockify \
 --cert certificado.pem \
 6080 localhost:5901

Firewall

Liberar apenas as portas necessárias:

ufw allow 6080/tcp

VPN

Melhor prática:

Internet
   ↓
 VPN
   ↓
 noVNC


Assim o serviço não fica exposto diretamente.

Casos de uso
Administração de servidores Linux

Gerenciar:

XFCE
GNOME
KDE

via navegador.

Laboratórios virtuais

Universidades usam noVNC para:

Treinamentos
Ambientes de prova
Laboratórios remotos
Containers gráficos

Executar aplicações com interface gráfica em Docker:

Chrome
Firefox
LibreOffice
Android Studio


e acessá-las pelo navegador.

Máquinas virtuais

Integração comum com:

Proxmox
OpenStack
Apache CloudStack
KVM
QEMU
Vantagens do noVNC

✅ Sem instalar software cliente

✅ Funciona em qualquer navegador moderno

✅ Multiplataforma

✅ Open Source

✅ Fácil integração com Docker

✅ Funciona em dispositivos móveis

✅ Interface simples

Limitações

❌ Mais lento que RDP em algumas situações

❌ Consome mais banda em telas com muitas mudanças

❌ Depende do Websockify na maioria dos cenários

❌ Pode apresentar latência em conexões ruins

❌ A qualidade depende do servidor VNC

noVNC vs VNC tradicional
Recurso	noVNC	Cliente VNCNavegador	✅	❌
Instalação local	❌	✅
HTTPS	✅	Limitado
Mobilidade	✅	Parcial
Performance	Boa	Melhor
Facilidade de acesso	Excelente	Média
noVNC vs RDP
Característica	noVNC	RDPLinux	Excelente	Limitado
Windows	Bom	Excelente
Navegador	Sim	Normalmente não
Compressão	Menor	Maior
Desempenho	Médio	Alto
Estrutura típica em produção
Internet
    │
    ▼
Nginx Reverse Proxy
    │
HTTPS
    │
    ▼
noVNC
    │
WebSocket
    │
    ▼
Websockify
    │
TCP
    │
    ▼
TigerVNC
    │
    ▼
Ubuntu XFCE


Essa arquitetura é muito utilizada em ambientes Docker, VPSs, painéis de hospedagem, laboratórios remotos e plataformas de desktops virtuais acessíveis diretamente pelo navegador.