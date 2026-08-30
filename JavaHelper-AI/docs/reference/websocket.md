
# WebSocket — Guia completo

Este documento fornece uma explicação detalhada sobre o protocolo WebSocket: sua motivação, funcionamento, uso em clientes e servidores, exemplos práticos, integração com proxies (como Nginx), considerações de segurança, performance, principais dúvidas (FAQ) e referências.

## Checklist de entrega

- [ ] Visão geral e motivação
- [ ] Conceitos do protocolo (handshake, frames, opcodes)
- [ ] APIs cliente (navegador) e servidor (exemplos em Node.js e Java)
- [ ] Proxying / integração com Nginx e balanceamento
- [ ] Segurança e TLS
- [ ] Escalabilidade, performance e boas práticas
- [ ] Principais dúvidas (FAQ)
- [ ] Citações e referências

## Visão geral e motivação

WebSocket é um protocolo de comunicação full-duplex sobre uma única conexão TCP. Padronizado como RFC 6455 (2011), ele permite que clientes (tipicamente navegadores) e servidores troquem mensagens bidirecionais em tempo real com baixa sobrecarga, superando limitações de soluções como polling e long-polling.

Motivação principal:

- Reduzir latência e overhead causados por requisições HTTP repetidas.
- Habilitar aplicações interativas em tempo real (chat, dashboards, jogos, colaboração, streaming de eventos).

Vantagens:

- Conexão persistente com comunicação bidirecional.
- Menor overhead por mensagem comparado a HTTP.
- Suporte em navegadores modernos através da API WebSocket.

Limitações:

- Requer conexão TCP persistente; consumo de recursos no servidor para conexões abertas.
- Requisição inicial ainda é feita via HTTP(S) para realizar o handshake.

## Como funciona (handshake e frames)

1) Handshake HTTP/Upgrade

- O cliente inicia uma requisição HTTP (GET) com cabeçalhos especiais para solicitar upgrade para WebSocket:

  - Upgrade: websocket
  - Connection: Upgrade
  - Sec-WebSocket-Key: <key_base64>
  - Sec-WebSocket-Version: 13

- O servidor responde com status 101 Switching Protocols e um cabeçalho Sec-WebSocket-Accept calculado (SHA-1 + base64) a partir da chave do cliente e o GUID do protocolo.

2) Comunicação por frames

- Após o handshake, a conexão muda de HTTP para o protocolo de frames do WebSocket. Cada frame tem um header (opcodes, comprimento, máscara) e payload.
- Tipos de frames (opcodes): continuação (0x0), texto (0x1), binário (0x2), close (0x8), ping (0x9), pong (0xA).
- Clientes devem mascarar (mask) os payloads enviados ao servidor; servidores não mascaram as mensagens para clientes.

3) Controle e fechamento

- Mensagens ping/pong permitem checar liveness.
- Close frame inicia encerramento ordenado (cliente ou servidor podem iniciar). Após o exchange de close frames, a conexão TCP é fechada.

## API do navegador (cliente)

Uso básico em JavaScript:

```javascript
const ws = new WebSocket('wss://exemplo.com/socket');

ws.addEventListener('open', () => {
  console.log('Conexão aberta');
  ws.send(JSON.stringify({ type: 'hello', payload: 'olá' }));
});

ws.addEventListener('message', (event) => {
  console.log('Mensagem recebida:', event.data);
});

ws.addEventListener('close', (event) => {
  console.log('Fechado:', event.code, event.reason);
});

ws.addEventListener('error', (err) => {
  console.error('Erro:', err);
});
```

Pontos importantes:

- Use `wss://` para conexões seguras (sobre TLS).
- Trate reconexões exponenciais e limites de retries para resiliência.
- Serialização/compressão podem ser aplicadas a payloads para otimizar tráfego.

## Servidores WebSocket (exemplos)

1) Node.js usando `ws` (módulo leve muito usado):

```javascript
const WebSocket = require('ws');
const wss = new WebSocket.Server({ port: 8080 });

wss.on('connection', (ws, req) => {
  console.log('Conectado:', req.socket.remoteAddress);

  ws.on('message', (msg) => {
	console.log('Recebido:', msg.toString());
	ws.send('eco: ' + msg);
  });

  ws.on('close', () => console.log('Conexão fechada'));
});
```

2) Java (Spring) — exemplo conceitual usando Spring WebSocket / STOMP:

- Spring fornece abstrações para WebSocket e STOMP por cima de WebSocket, com suporte a fallback (SockJS).
- Configurar endpoints e mensageria simples é comum em aplicações Spring Boot.

3) Outros stacks: Netty, Vert.x, Akka HTTP (Scala), Ratchet (PHP), e servidores que expõem APIs WebSocket nativamente.

## Protocolos relacionados e subprotocolos

- SockJS: biblioteca para fallback quando WebSocket não está disponível (usa polling, xhr-streaming etc.).
- STOMP: protocolo de mensageria simples frequentemente usado em aplicações WebSocket com Spring.
- WAMP: protocolo RPC/pubsub sobre WebSocket.

## Proxying e uso com Nginx

Considerações ao passar WebSocket por proxies:

- O handshake é HTTP, portanto proxies que suportam Upgrade/Connection corretamente podem encaminhar WebSocket.
- Nginx e HAProxy suportam proxy de WebSocket quando configurados para passar headers `Upgrade` e `Connection` e manter timeout/keepalive apropriados.

Exemplo Nginx básico para WebSocket:

```nginx
map $http_upgrade $connection_upgrade {
  default upgrade;
  ''      close;
}

upstream ws_backends {
  server 127.0.0.1:8080;
}

server {
  listen 80;
  server_name ws.exemplo.com;

  location /socket/ {
	proxy_pass http://ws_backends;
	proxy_http_version 1.1;
	proxy_set_header Upgrade $http_upgrade;
	proxy_set_header Connection $connection_upgrade;
	proxy_set_header Host $host;
	proxy_set_header X-Real-IP $remote_addr;
	proxy_read_timeout 86400;
  }
}
```

Pontos extras:

- `proxy_read_timeout` deve ser maior para conexões longas.
- Assegure que balanceadores TCP/Layer4 tratem corretamente health checks e não terminem a conexão.

## Segurança

- Use TLS (`wss://`) para proteger tráfego e evitar ataques de eavesdropping e manipulação.
- Valide e saneie mensagens recebidas; nunca confie em conteúdo vindo do cliente.
- Implemente limites de taxa (`rate limiting`) e limites de conexões por IP para mitigar abuso.
- Proteja contra mensagens grandes ajustando limites de tamanho no servidor (por exemplo `maxPayload` em libs Node).
- CORS: o handshake WebSocket não segue exatamente as mesmas regras CORS de uma simples requisição XHR, mas o header `Origin` é enviado; servidores devem checar e validar `Origin` quando apropriado.

## Escalabilidade e arquitetura

Problema central: conexões persistentes significam que a escalabilidade horizontal exige coordenação.

Padrões comuns:

- Sticky sessions (persistence) no nível do load balancer para enviar a conexão inicial sempre ao mesmo backend.
- Uso de um enfileirador/mediador (Redis pub/sub, Kafka) para distribuição de mensagens entre instâncias.
- Offload de conexões longas para um serviço dedicado (ex: gateway WebSocket) que encaminha eventos para processamento assíncrono.
- Escala por número de conexões depende de: uso de I/O não-bloqueante, limites de file descriptors e memória por conexão.

## Performance e boas práticas

- Prefira servidores que usam I/O assíncrono (Node.js, Netty, libuv-based servers) para grandes quantidades de conexões.
- Mantenha payloads pequenos. Use compactação se for eficiente (per-message deflate, negociado no handshake como permessage-deflate).
- Use heartbeat (ping/pong) para detectar conexões mortas e liberar recursos.
- Monitore métricas: conexões ativas, pings, latência de mensagens, throughput.

## Principais dúvidas (FAQ)

1. Quando devo usar WebSocket ao invés de HTTP polling?
   - Use WebSocket quando precisar de comunicação bidirecional em tempo real com latência baixa e quando o volume de mensagens torna o polling ineficiente.

2. O WebSocket funciona por trás de proxies/NAT?
   - Sim, desde que o proxy suporte o mecanismo de Upgrade e não interrompa conexões longas. Alguns proxies corporativos podem bloquear WebSocket.

3. Devo usar `wss://` em produção?
   - Sim. Sempre proteja o tráfego e autenticações via TLS.

4. Como escalar uma aplicação WebSocket?
   - Use balanceamento (com sticky sessions ou redis pub/sub para broadcast), dimensione o número de instâncias e separe responsabilidades (gateway vs processamento).

5. WebSocket é seguro contra CSRF?
   - O handshake envia o header `Origin`, que o servidor deve validar. Além disso, autenticações via cookies exigem cuidado (CSRF) — prefira tokens Bearer enviados no subprotocol ou query string (com cautela) ou no header durante handshake.

6. WebSocket pode funcionar com HTTP/2?
   - WebSocket sobre HTTP/2 não é definido no RFC 6455; existe um esforço (RFCs experimentais) e implementações específicas (p.ex. WebTransport para casos mais modernos). Em prática, WebSocket tradicionalmente roda sobre HTTP/1.1 Upgrade.

7. Quando usar STOMP/SockJS?
   - Use STOMP quando precisar de um protocolo de mensageria padronizado; SockJS como fallback se precisar suportar navegadores/proxies que não aceitam WebSocket.

## Referências e leituras recomendadas

- RFC 6455 — The WebSocket Protocol: https://datatracker.ietf.org/doc/html/rfc6455
- MDN Web Docs — WebSockets: https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API
- IETF WebSocket resources e drafts relacionados
- Bibliotecas e documentos: `ws` (Node.js), `socket.io` (abstração com features extras e fallback), Spring WebSocket (Java), Netty (Java/Netty based implementations)
- Artigos sobre escala e arquitetura: blogs de engenharia de empresas que lidam com tempo real (ex: Slack, Discord, etc.)

---

Se quiser, posso:

- adicionar exemplos práticos completos (server + client) com instruções para executar localmente;
- gerar um guia de configuração de Nginx para produção com TLS e sticky sessions;
- criar um playbook de segurança e teste de carga para conexões WebSocket.

Arquivo atualizado: `docs/reference/websocket.md`.

