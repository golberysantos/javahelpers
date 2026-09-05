# Guia de Consulta: Comandos Essenciais de SSH e UFW

Este documento reúne os principais comandos úteis para gerenciar o servidor SSH e configurar regras de acesso com o firewall UFW, seguindo o padrão de explicações detalhadas por parâmetros.

---

## 1. Verificações do Serviço SSH

### Verificar o status do SSH
Este comando verifica se o serviço SSH está instalado, ativo e rodando no sistema.

* **sudo**: Executa o comando com privilégios de administrador.
* **systemctl**: Gerenciador de serviços do systemd.
* **status ssh**: Consulta o estado atual do serviço SSH.

**O que esperar no resultado:**
Se o SSH estiver instalado e rodando, o status exibido será `active (running)`. Se estiver parado, exibirá `inactive`. Caso o serviço não exista, informará que a unidade não foi encontrada.

```bash
sudo systemctl status ssh

```

---

### Iniciar o serviço SSH

Este comando coloca o servidor SSH em funcionamento caso ele esteja inativo.

* **sudo**: Executa o comando com privilégios de administrador.
* **systemctl**: Gerenciador de serviços do systemd.
* **start ssh**: Inicia imediatamente o serviço SSH.

**O que esperar no resultado:**
O comando executará sem erros e o serviço passará a aceitar conexões locais ou remotas (se o firewall permitir).

```bash
sudo systemctl start ssh

```

---

### Inspecionar portas e processos SSH ativos

Este comando exibe quais portas e processos de rede estão escutando conexões relacionadas ao SSH.

* **sudo**: Executa o comando com privilégios de administrador (necessário para ver quais processos pertencem a qual usuário).
* **ss**: Ferramenta moderna usada para investigar sockets de rede (substituta mais rápida do antigo netstat).
* **-t**: Mostra apenas conexões TCP.
* **-l**: Filtra apenas os sockets que estão no estado de escuta (listening), aguardando conexões de entrada.
* **-n**: Mostra os números das portas diretamente (em vez de tentar resolver nomes de serviços, como mostrar 22 em vez de ssh).
* **-p**: Mostra o processo (PID e nome) que está usando aquela porta.
* **| grep ssh**: Filtra a saída para exibir apenas as linhas que contêm a palavra "ssh".

**O que esperar no resultado:**
Se o servidor SSH estiver rodando corretamente, você verá uma linha indicando que o processo está escutando na porta padrão (22) ou em outra porta configurada, acompanhada pelo nome do processo (como sshd).

```bash
sudo ss -tlnp | grep ssh

```

---

## 2. Gerenciamento de Acesso e Firewall (UFW)

### Liberar acesso SSH para um IP específico

Este comando restringe o acesso ao serviço SSH exclusivamente para um endereço IP de origem determinado.

* **sudo**: Executa o comando com privilégios de administrador.
* **ufw**: Ferramenta padrão de gerenciamento de firewall no Ubuntu.
* **allow**: Regra para permitir o tráfego.
* **from SEU_IP**: Define o endereço IP específico que tem permissão de origem.
* **to any port ssh**: Aplica a regra de permissão para a porta do serviço SSH em qualquer interface de rede.

**O que esperar no resultado:**
O sistema exibirá a mensagem de que a regra foi adicionada com sucesso.

```bash
sudo ufw allow from SEU_IP to any port ssh

```

---

### Listar regras do UFW com números

Este comando exibe todas as regras cadastradas no firewall acompanhadas de seus respectivos índices numéricos.

* **sudo**: Executa o comando com privilégios de administrador.
* **ufw**: Ferramenta padrão de gerenciamento de firewall no Ubuntu.
* **status**: Exibe o estado e as regras atuais do firewall.
* **numbered**: Modificador que adiciona números de identificação a cada linha de regra listada.

**O que esperar no resultado:**
Uma lista detalhada contendo colunas com índices numéricos (`[ 1]`, `[ 2]`), a ação (ALLOW/DENY) e os IPs/portas envolvidos.

```bash
sudo ufw status numbered

```

---

### Remover uma regra do UFW pelo número

Este comando exclui permanentemente uma regra específica do firewall utilizando o índice numérico obtido na listagem.

* **sudo**: Executa o comando com privilégios de administrador.
* **ufw**: Ferramenta padrão de gerenciamento de firewall no Ubuntu.
* **delete**: Comando utilizado para apagar uma regra existente.
* **NUMERO**: O índice numérico correspondente à regra que deseja remover (ex: 1, 2).

**O que esperar no resultado:**
O terminal solicitará uma confirmação de exclusão. Ao confirmar, a regra será removida do painel do UFW.

```bash
sudo ufw delete NUMERO

```





