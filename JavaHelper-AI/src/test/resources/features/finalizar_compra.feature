## Feature gerada a partir do Caso de Uso: Finalizar Compra (CU-002)
# CU-002
@CU-002
Feature: Finalizar Compra
  Como um Cliente
  Eu quero finalizar a compra dos itens no meu carrinho
  Para que o pedido seja criado e pago

  Background:
    Given que o cliente "joao@example.com" está autenticado
    And o carrinho contém:
      | produto    | quantidade |
      | Camiseta   | 2          |
      | Caneca     | 1          |

  Scenario: Finalizar compra com pagamento aprovado
    Given o cliente seleciona o endereço "Rua A, 123"
    And escolhe a forma de pagamento "Cartão"
    When o cliente confirma a compra
    And o sistema processa o pagamento com resultado "aprovado"
    Then o pedido deve ser criado com status "Confirmado"
    And o cliente deve receber a confirmação por email

  Scenario: Pagamento recusado
    Given o cliente seleciona o endereço "Rua A, 123"
    And escolhe a forma de pagamento "Cartão"
    When o cliente confirma a compra
    And o sistema processa o pagamento com resultado "recusado"
    Then o pedido não deve ser criado
    And o cliente deve ver a mensagem "Pagamento recusado"

  Scenario Outline: Finalizar compra com diferentes resultados de pagamento
    Given o cliente seleciona o endereço "Rua A, 123"
    And escolhe a forma de pagamento "<forma>"
    When o cliente confirma a compra
    And o sistema processa o pagamento com resultado "<resultado>"
    Then o pedido deve ter status "<status>"

    Examples:
      | forma  | resultado  | status     |
      | Cartão | aprovado   | Confirmado |
      | Boleto | pendente   | Pendente   |
      | Cartão | recusado   | Cancelado  |
