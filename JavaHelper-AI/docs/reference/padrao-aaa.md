# Padrão AAA (Arrange, Act, Assert)

### Definição
O **Padrão AAA** é uma diretriz de design para estruturação de testes automatizados. Ele preconiza que cada caso de teste deve ser dividido visualmente e logicamente em três seções sequenciais e bem definidas: **Arrange** (Organizar), **Act** (Agir) e **Assert** (Garantir).

### As Três Fases
1. **Arrange (Organizar/Preparar):** Configuração de todo o cenário necessário para o teste. Criação de dados de teste, inicialização de classes, injeção de *mocks* ou definição de estados iniciais.
2. **Act (Agir/Executar):** Execução da ação principal ou do método que está sendo testado. Idealmente, deve conter apenas **uma única linha** de código.
3. **Assert (Garantir/Verificar):** Verificação de que o resultado da ação executada condiz perfeitamente com o resultado esperado.

### Exemplo de Código (Java com JUnit)
```java
@Test
void deveAplicarDescontoDeDezPorCentoNoCarrinho() {
    // 1. Arrange (Preparação)
    CalculadoraDeDesconto calculadora = new CalculadoraDeDesconto();
    double valorOriginal = 100.00;

    // 2. Act (Execução)
    double valorComDesconto = calculadora.aplicarDescontoEspecial(valorOriginal);

    // 3. Assert (Verificação)
    assertEquals(90.00, valorComDesconto, "O valor final deveria conter 10% de desconto");
}