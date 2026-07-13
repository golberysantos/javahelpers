


Como você provaria que uma calculadora soma corretamente dois números?

Provavelmente você responderia:

2 + 2 = 4

Perfeito.

Você acabou de imaginar um teste.

Percebe?

Você não pensou em JUnit.

Nem em Java.

Nem em Spring.

Você pensou em:
Entrada → Execução → Resultado Esperado: Essa é a essência de praticamente todo teste automatizado.

JUnit apenas nos fornece uma forma organizada de expressar essa ideia.


## Estrutura de um teste (AAA)

Todo teste unitário deve ser organizado em três etapas:

- Arrange: preparar o cenário.
- Act: executar a ação.
- Assert: verificar o resultado.

Esse padrão melhora a legibilidade e facilita a manutenção dos testes.