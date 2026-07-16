# Padrões de Projeto — Java Puro

Módulo do desafio **Padrões de Projeto com Java e Spring** — Bootcamp Santander 2026.

---

## Descrição

Implementação dos padrões **Singleton**, **Strategy** e **Facade** com Java puro, sem dependência de frameworks. O objetivo é entender a essência de cada padrão antes de vê-los aplicados no contexto do Spring.

---

## Estrutura do Projeto

```
lab-padroes-projeto-java/
└── src/main/java/br/com/padroes/
    ├── singleton/
    │   ├── SingletonLazy.java
    │   ├── SingletonEager.java
    │   └── SingletonLazyHolder.java
    ├── strategy/
    │   ├── Comportamento.java
    │   ├── ComportamentoNormal.java
    │   ├── ComportamentoDefensivo.java
    │   ├── ComportamentoAgressivo.java
    │   └── Robo.java
    ├── facade/
    │   ├── Facade.java
    │   └── subsistema/
    │       ├── cep/CepApi.java
    │       └── crm/CrmService.java
    └── Test.java
```

---

## Padrões

### Singleton
Garante que uma classe tenha apenas uma instância durante todo o ciclo de vida da aplicação.

| Variação | Quando cria a instância | Thread-safe |
|---|---|---|
| `SingletonLazy` | Na primeira chamada | Não |
| `SingletonEager` | No carregamento da classe | Sim |
| `SingletonLazyHolder` | Na primeira chamada via classe interna | Sim |

`SingletonLazyHolder` é a variação recomendada: combina inicialização lazy com thread-safety sem sincronização explícita.

```java
// As três chamadas retornam a mesma instância
SingletonLazyHolder a = SingletonLazyHolder.getInstancia();
SingletonLazyHolder b = SingletonLazyHolder.getInstancia();
System.out.println(a == b); // true
```

---

### Strategy
Define uma família de algoritmos encapsulados e intercambiáveis em tempo de execução, sem alterar o código do contexto que os usa.

```
«interface» Comportamento
    ├── ComportamentoNormal      → "Movendo-se normalmente..."
    ├── ComportamentoDefensivo   → "Movendo-se defensivamente..."
    └── ComportamentoAgressivo   → "Movendo-se agressivamente..."

Robo → delega mover() para o Comportamento configurado
```

```java
Robo robo = new Robo();
robo.setComportamento(new ComportamentoNormal());
robo.mover(); // Movendo-se normalmente...

robo.setComportamento(new ComportamentoAgressivo());
robo.mover(); // Movendo-se agressivamente...
```

---

### Facade
Fornece uma interface simplificada para um conjunto de subsistemas complexos. O chamador não precisa conhecer os detalhes internos.

```
Facade.migrarCliente(nome, cep)
    ├── CepApi.recuperarCidade(cep)   ← subsistema 1
    ├── CepApi.recuperarEstado(cep)   ← subsistema 1
    └── CrmService.gravarCliente(...) ← subsistema 2
```

```java
Facade facade = new Facade();
facade.migrarCliente("Venilton", "01310-100");
// Saída:
// === Cliente salvo no CRM ===
// Nome   : Venilton
// CEP    : 01310-100
// Cidade : São Paulo
// Estado : SP
```

---

## Como Executar

**1. Compilar** (a partir da raiz do módulo):
```bash
javac -d bin $(find src -name "*.java")
```

**2. Executar:**
```bash
java -cp bin br.com.padroes.Test
```

**Saída esperada:**
```
=== Singleton Lazy ===
Mesma instância? true

=== Singleton Eager ===
Mesma instância? true

=== Singleton LazyHolder ===
Mesma instância? true

=== Strategy — Robô ===
Movendo-se normalmente...
Movendo-se normalmente...
Movendo-se defensivamente...
Movendo-se agressivamente...
Movendo-se agressivamente...
Movendo-se agressivamente...

=== Facade — Migração de Cliente ===
=== Cliente salvo no CRM ===
Nome   : Venilton
CEP    : 01310-100
Cidade : São Paulo
Estado : SP
```

---

## Tecnologias

- Java 21
- Sem dependências externas


