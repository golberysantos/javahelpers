# Bastion Host (Jump Server)

## Introdução

Um **Bastion Host** (também conhecido como **Jump Server** ou **Jump Host**) é um servidor especialmente projetado para fornecer acesso seguro a recursos localizados em redes privadas. Ele atua como um ponto controlado de entrada e saída entre usuários autorizados e sistemas internos, reduzindo a superfície de ataque da infraestrutura.

O termo "bastion" tem origem na arquitetura militar, onde um bastião era uma estrutura fortificada usada para proteger áreas sensíveis. Da mesma forma, na segurança da informação, o Bastion Host é um sistema fortificado que protege o acesso aos ativos críticos da organização.

---

## Objetivo Principal

O principal objetivo de um Bastion Host é centralizar e controlar o acesso administrativo a servidores, bancos de dados, equipamentos de rede e demais recursos internos.

Sem um Bastion Host:

- Diversos servidores ficam expostos à Internet.
- Existem múltiplos pontos de entrada para invasores.
- O controle de auditoria é mais complexo.
- A gestão de credenciais se torna mais difícil.

Com um Bastion Host:

- Apenas um ponto de acesso é exposto.
- O tráfego administrativo é centralizado.
- Logs e auditorias são simplificados.
- O risco de comprometimento é reduzido.

---

## Como Funciona

### Fluxo Tradicional

```text
Administrador
      |
      v
 Internet
      |
      v
 Bastion Host
      |
      +------> Servidor Web
      |
      +------> Banco de Dados
      |
      +------> Servidor de Aplicação
```

O administrador conecta-se inicialmente ao Bastion Host utilizando protocolos seguros como:

- SSH (Linux/Unix)
- RDP (Windows)
- VPN
- HTTPS

Após a autenticação, o usuário pode acessar os recursos internos autorizados.

---

## Características de Segurança

### 1. Sistema Fortemente Endurecido (Hardening)

O Bastion Host normalmente possui:

- Serviços desnecessários removidos.
- Portas não utilizadas desabilitadas.
- Atualizações constantes.
- Monitoramento contínuo.
- Políticas rígidas de segurança.

### 2. Autenticação Multifator (MFA)

É comum exigir:

- Senha.
- Token.
- Aplicativo autenticador.
- Certificado digital.

### 3. Registro de Logs

Toda tentativa de acesso pode ser registrada:

- Usuário.
- Endereço IP.
- Horário.
- Comandos executados.
- Sessões remotas.

### 4. Controle de Acesso

Permissões podem ser concedidas seguindo o princípio do menor privilégio:

- Administradores de banco acessam apenas bancos.
- Equipes de infraestrutura acessam apenas servidores.
- Desenvolvedores acessam apenas ambientes permitidos.

---

## Benefícios

### Segurança Aprimorada

Reduz significativamente a exposição direta dos ativos internos.

### Auditoria Centralizada

Facilita investigações e análises de incidentes.

### Gestão Simplificada

Centraliza autenticação e autorização.

### Conformidade

Ajuda no atendimento de requisitos regulatórios como:

- ISO 27001
- PCI DSS
- LGPD
- NIST Cybersecurity Framework

---

## Desvantagens

Apesar das vantagens, existem alguns desafios:

### Ponto Único de Falha

Caso não haja redundância, a indisponibilidade do Bastion Host pode impedir acessos administrativos.

### Alvo Prioritário

Por ser um ponto estratégico, torna-se um alvo frequente para ataques.

### Necessidade de Monitoramento

Exige monitoramento contínuo e manutenção rigorosa.

---

## Boas Práticas

### Implementar MFA

Nunca confiar apenas em usuário e senha.

### Restringir Endereços IP

Permitir acesso somente de redes confiáveis.

### Atualizar Regularmente

Aplicar correções de segurança rapidamente.

### Segmentar Redes

Manter os recursos internos isolados.

### Registrar Sessões

Capturar atividades administrativas para auditoria.

### Utilizar Chaves SSH

Evitar autenticação baseada somente em senhas.

---

## Bastion Host em Ambientes Cloud

Provedores de nuvem oferecem soluções específicas:

### Microsoft Azure

- Azure Bastion
- Acesso seguro via navegador
- Conexões RDP e SSH sem exposição pública dos servidores

### AWS

- EC2 configurada como Bastion Host
- Acesso seguro a instâncias privadas

### Google Cloud

- Bastion VM
- Integração com controles de identidade

---

## Exemplo de Cenário Corporativo

Uma empresa possui:

- 50 servidores Linux
- 20 servidores Windows
- Bancos de dados internos

Em vez de expor cada servidor à Internet:

1. Apenas o Bastion Host recebe IP público.
2. Administradores conectam-se ao Bastion Host.
3. O Bastion Host encaminha as conexões para a rede privada.
4. Todas as ações ficam registradas.

Resultado:

- Menor superfície de ataque.
- Melhor rastreabilidade.
- Maior controle operacional.

---

## Comparação: Bastion Host vs VPN

| Característica | Bastion Host | VPN |
|---------------|-------------|-----|
| Acesso Centralizado | Sim | Parcial |
| Auditoria | Excelente | Limitada |
| Controle Granular | Alto | Médio |
| Complexidade | Média | Baixa |
| Exposição da Rede | Menor | Maior |

Muitas organizações utilizam Bastion Host e VPN em conjunto para aumentar a segurança.

---

## Conclusão

O Bastion Host é uma camada fundamental de segurança para ambientes corporativos modernos. Sua função é proteger o acesso administrativo a sistemas críticos por meio de centralização, autenticação forte, auditoria e controle de acesso. Quando corretamente implementado, reduz riscos operacionais, melhora a conformidade regulatória e fortalece significativamente a postura de segurança da organização.
