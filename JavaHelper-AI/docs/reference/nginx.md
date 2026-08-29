
# Nginx — Guia completo

Este documento oferece uma explicação abrangente sobre o servidor/ reverso proxy Nginx, cobrindo sua arquitetura, usos comuns, configuração básica, exemplos práticos, tuning, segurança, perguntas frequentes (principais dúvidas) e referências para aprofundamento.

> Observação: este texto está em português e foi preparado para uso como referência técnica no projeto.

## Sumário

- Visão geral
- História e versões
- Arquitetura e componentes principais
- Casos de uso comuns
- Estrutura de configuração e diretivas importantes
- Exemplos práticos de configuração
- Desempenho, tuning e cache
- Segurança e TLS
- Monitoramento e troubleshooting
- Principais dúvidas (FAQ)
- Citações e referências

## Visão geral

Nginx (pronuncia-se "engine x") é um servidor web de alto desempenho, também usado como proxy reverso, balanceador de carga, terminador TLS, cache HTTP e servidor de proxy para WebSocket e gRPC. É projetado para lidar com muitas conexões concorrentes com baixo uso de recursos, usando um modelo de eventos assíncrono e não-bloqueante.

Pontos-chave:

- Desempenho e escalabilidade: projetado para servir conteúdo estático muito rapidamente e para atuar como gateway reverso para aplicações dinâmicas.
- Modelo de evento: utiliza um loop de eventos (event-driven) por worker process, evitando o modelo clássico de criar uma thread por conexão.
- Modularidade: suporta módulos core e módulos de terceiros (alguns pagos/empresariais no Nginx Plus).

TLS (Transport Layer Security) é o protocolo de criptografia usado para proteger os dados trocados entre o servidor web e os usuários. Ele transforma conexões comuns (HTTP) em conexões seguras (HTTPS), garantindo privacidade e integridade.

O que o TLS faz no Nginx?:
- Criptografia: Esconde senhas e dados sensíveis de quem tenta roubar informações na rede.
- Autenticação: Prova que o usuário está conectado ao site verdadeiro e não a um clone falso.
- Configuração: É ativado nos arquivos de configuração do Nginx com comandos para escolher versões seguras e certificados digitais.

## História e versões

Nginx foi criado por Igor Sysoev em 2002–2004 para resolver problemas de C10k (manter 10.000 conexões concorrentes). Existem duas linhas principais:

- Nginx Open Source (comunitário) — licenciado sob BSD-like; altamente popular.
- Nginx Plus — versão comercial mantida pela F5 (oferece recursos adicionais como dashboard, monitoramento avançado, balanceamento de sessão, suporte comercial).

Sempre consulte a documentação oficial para diferenças entre recursos e compatibilidade de versão.

## Arquitetura e componentes principais

Principais conceitos e blocos arquiteturais:

- Master process: processo principal que lê a configuração e gerencia worker processes (abrir/fechar, recarregar configuração, privilégios).
- Worker processes: um ou mais processos que atendem conexões. Cada worker executa um loop de eventos e serve muitas conexões simultâneas.
- Eventos: uso de mecanismos de I/O do SO (epoll em Linux, kqueue em BSD/macOS, IOCP no Windows) para multiplexação de eventos.
- Contextos de configuração: main, events, http, server, location (cada um com diretivas válidas específicas).
- Módulos: divididos entre core (empacotados) e opcionais; extensíveis via módulos dinâmicos.

Fluxo básico de requisição:

1. Pacote TCP chega na porta escutada.
2. Worker process aceita a conexão.
3. Nginx processa requisição conforme diretivas (server_name, location, proxy_pass, root, try_files etc.).
4. Nginx responde diretamente (arquivo estático) ou encaminha para um upstream (proxy_pass / fastcgi_pass / uwsgi_pass / grpc_pass / proxy for WebSocket).

## Casos de uso comuns

- Servidor de arquivos estáticos (HTML, CSS, JS, imagens)
- Reverse proxy para aplicações (Node.js, Java, Python, PHP-FPM)
- Balanceamento de carga: round-robin, least_conn, ip_hash, hash (hash de sessão)
- TLS termination (SSL offload) — terminar TLS em Nginx e encaminhar HTTP para backend
- Cache de conteúdo (proxy_cache) — reduzir carga nos backends
- WebSocket proxying e suporte a HTTP/2 e gRPC
- Autenticação básica/externa, limitação de taxa (rate limiting), controle de acesso

## Estrutura de configuração e diretivas importantes

Arquivo principal: `nginx.conf` (local padrão /etc/nginx/nginx.conf em Linux). A configuração é dividida em blocos e diretivas.

Contextos principais:

- main (fora de qualquer bloco): diretivas globais (user, worker_processes, error_log, pid)
- events: define parâmetros do loop de eventos (worker_connections, multi_accept)
- http: contexto que contém configurações HTTP (include, server, upstream, proxy settings, gzip, log_format)
- server: define blocos virtuais (virtual hosts) identificados por `listen` e `server_name`
- location: regras de roteamento dentro de um `server` para mapear URIs a handlers (root, proxy_pass, try_files)

Diretivas comuns:

- listen: porta e ip (p.ex. `listen 80;`, `listen 443 ssl;`)
- server_name: nomes de host para o virtual host
- root / alias: caminho de arquivos estáticos
- index: arquivos index default (index.html, index.php)
- proxy_pass: encaminhar para upstream/backend
- upstream: definir grupo de servidores backend e método de balanceamento
- error_page: customizar páginas de erro
- access_log / error_log: logs
- include: incluir arquivos externos (prática comum para sites-available/sites-enabled)

Exemplo mínimo:

server {
	listen 80;
	server_name exemplo.com www.exemplo.com;
	root /var/www/exemplo;
	index index.html;

	location / {
		try_files $uri $uri/ =404;
	}
}

## Exemplos práticos de configuração

1) Reverse proxy simples para uma app Node.js:

upstream app_nodes {
	server 127.0.0.1:3000;
	# server 127.0.0.1:3001; # múltiplos backends
}

server {
	listen 80;
	server_name app.exemplo.com;

	location / {
		proxy_set_header Host $host;
		proxy_set_header X-Real-IP $remote_addr;
		proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
		proxy_pass http://app_nodes;
	}
}

2) TLS básico com Let's Encrypt (exemplo simplificado):

server {
	listen 80;
	server_name exemplo.com www.exemplo.com;
	# Redirecionar HTTP para HTTPS
	return 301 https://$host$request_uri;
}

server {
	listen 443 ssl http2;
	server_name exemplo.com www.exemplo.com;

	ssl_certificate /etc/letsencrypt/live/exemplo.com/fullchain.pem;
	ssl_certificate_key /etc/letsencrypt/live/exemplo.com/privkey.pem;
	include /etc/letsencrypt/options-ssl-nginx.conf;
	ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem;

	location / {
		proxy_pass http://127.0.0.1:8080;
		proxy_set_header Host $host;
	}
}

3) Cache de proxy (proxy_cache básico):

http {
	proxy_cache_path /var/cache/nginx levels=1:2 keys_zone=my_cache:10m max_size=1g inactive=60m use_temp_path=off;

	server {
		listen 80;
		location / {
			proxy_cache my_cache;
			proxy_cache_valid 200 302 10m;
			proxy_cache_valid 404 1m;
			proxy_pass http://backend;
		}
	}
}

## Desempenho, tuning e cache

Boas práticas para alta performance:

- worker_processes: normalmente configurado para número de núcleos (ou auto com `auto`);
- worker_connections: define quantas conexões cada worker pode manter; conexões totais ~ worker_processes * worker_connections;
- use epoll (Linux) para máxima eficiência (configurado automaticamente em distribuições recentes);
- gzip/deflate: comprimir respostas quando apropriado;
- keepalive: configurar `keepalive_timeout` e `keepalive_requests` para reuso de conexões com backends/clients;
- proxy_buffering: controle de buffering para proxied responses (pode afetar latência/throughput);
- proxy_cache: reduzir carga nos backends para conteúdo cacheável;
- sendfile, tcp_nopush, tcp_nodelay: otimizações de I/O em sistemas Unix;
- ajustar limites do SO (ulimit, file descriptors) e parâmetros de kernel (somaxconn, tcp_tw_reuse) para cargas muito altas.

Medição e profiling: use ferramentas como `ab`, `wrk`, `nginx status` (stub_status ou ngx_http_stub_status_module), Prometheus + nginx-exporter, e logs para identificar gargalos.

## Segurança e TLS

- Use versões modernas de TLS (TLS 1.2/1.3) e desative SSLv3/TLS1.0/TLS1.1.
- Configure cipher suites seguras e HSTS (HTTP Strict Transport Security) quando aplicável.
- Proteja diretórios, desabilite listagem de diretório (`autoindex off`) quando não necessário.
- Use `limit_req` e `limit_conn` para mitigação básica de DoS/abuso.
- Evite expor informações sensíveis em headers e mensagens de erro.
- Mantenha Nginx atualizado (corrige vulnerabilidades). Para cargas críticas considere Nginx Plus ou suporte comercial.

Exemplo de headers de segurança básicos:

add_header X-Frame-Options "SAMEORIGIN" always;
add_header X-Content-Type-Options "nosniff" always;
add_header X-XSS-Protection "1; mode=block" always;
add_header Strict-Transport-Security "max-age=31536000; includeSubDomains; preload" always;

## Monitoramento e troubleshooting

- Testar configuração: `nginx -t` (verifica sintaxe e arquivos incluídos)
- Reiniciar/recarregar: `systemctl restart nginx` / `systemctl reload nginx` ou `nginx -s reload` para recarregar sem downtime
- Ver logs: `error_log` e `access_log` (localizados em /var/log/nginx/ por padrão)
- Status básico: habilitar `stub_status` e consultar `/nginx_status` para métricas: active connections, accepts, handled, requests, reading, writing, waiting
- Métricas mais avançadas: usar `nginx-module-vts` ou exportadores para Prometheus

Problemas comuns:

- 502 Bad Gateway: backend inacessível, erro de upstream, mismatch de protocolo (ex: proxy_pass http:// vs https://)
- 504 Gateway Timeout: tempo de resposta do backend excedido (ajustar proxy_read_timeout / backend)
- 413 Payload Too Large: limite client_max_body_size muito baixo
- Erros TLS: certificados inválidos, permissions nas chaves, mismatch de cipher

## Principais dúvidas (FAQ)

1. O Nginx é um servidor web ou proxy?
   - Ambos. Nginx é um servidor web que também é amplamente usado como proxy reverso, balanceador de carga e terminador TLS.

2. Quando usar Nginx vs Apache?
   - Nginx é preferido para servir conteúdo estático, proxy reverso e alta concorrência por seu modelo event-driven. Apache tem força em ambientes que dependem fortemente de módulos baseados em processo/thread e configurações .htaccess por diretório. Muitas infraestruturas usam ambos em conjunto.

3. O Nginx roda bem no Windows?
   - Há uma versão do Nginx para Windows, mas historicamente o desempenho e as capacidades são melhores em sistemas Unix-like (Linux/BSD). Em produção, o Linux é a escolha mais comum.

4. Como faço zero-downtime deploy com Nginx?
   - Use `nginx -s reload` (recarrega workers com nova configuração) e estratégias de deploy para backends (blue/green, rolling updates). Combine com health checks e balanceamento apropriado.

5. Como habilitar HTTP/2?
   - Habilite `http2` no `listen` do bloco `server` junto com TLS (ex: `listen 443 ssl http2;`) e use uma build de Nginx com suporte a HTTP/2 (builds modernas padrão).

6. O que é `proxy_pass` vs `fastcgi_pass`?
   - `proxy_pass` encaminha requisições HTTP para um backend HTTP. `fastcgi_pass` é usado para comunicar com servidores FastCGI (ex: PHP-FPM).

7. Como configurar balanceamento de carga com sticky sessions?
   - Use `ip_hash` (simples) ou módulos terceiros / Nginx Plus para session persistence avançada. Outra opção é delegar sessões para armazenamento compartilhado (Redis) e usar round-robin.

8. Como lidar com WebSocket?
   - Nginx suporta proxy de WebSocket via `proxy_pass` (necessário encaminhar headers e manter conexões): `proxy_set_header Upgrade $http_upgrade; proxy_set_header Connection "upgrade";`

9. Como reduzir latência para clientes?
   - Ative HTTP/2, compressão (gzip), cache, use CDN, ajuste buffers e keepalive, e sirva ativos estáticos diretamente via Nginx.

10. Quais são limites de conexões?
	- Em teoria, Nginx pode atender muitas conexões (ordens de magnitude maiores do que modelos por-thread) — limites práticos dependem de `worker_connections`, file descriptors e recursos do SO.

## Citações e referências

As referências abaixo são fontes oficiais e materiais de referência comumente usados para Nginx:

1. Nginx Official Documentation — https://nginx.org/en/docs/ (documentação oficial do projeto open source)
2. NGINX, Inc. Documentation — https://docs.nginx.com/ (documentação do Nginx Plus / recursos comerciais)
3. Official Nginx Blog — https://www.nginx.com/blog/
4. Let's Encrypt Documentation — https://letsencrypt.org/docs/
5. Mozilla SSL Configuration Generator — recomendações de TLS: https://ssl-config.mozilla.org/
6. "The Architecture of Nginx" — Igor Sysoev (apresentações e posts) e diversos artigos técnicos históricos sobre o design event-driven de Nginx.
7. Digita artigos e guias: "High Performance Browser Networking" (Ilya Grigorik) para conceitos de rede, e tutoriais práticos sobre tuning.

Citações curtas:

"Nginx is a high performance HTTP server and reverse proxy." — nginx.org

"Designed for maximum performance and stability." — Nginx project materials

## Onde aprender mais

- Documentação oficial do Nginx (nginx.org e docs.nginx.com)
- Blogs e guias práticos (DigitalOcean, nginx.com, GitHub gists)
- Cursos e livros sobre administração de sistemas e operações (DevOps) cobrindo TLS, redes e tuning de servidores web

---

Se desejar, posso:

- adicionar exemplos mais detalhados (ex: configurações de PHP-FPM, gRPC, WebSocket completo),
- gerar snippets prontos para `systemd`/Windows service e instruções de instalação por distro,
- incluir um checklist de segurança ou um playbook de deploy com Nginx e Let's Encrypt.

---

Arquivo atualizado: `docs/reference/nginx.md`.

