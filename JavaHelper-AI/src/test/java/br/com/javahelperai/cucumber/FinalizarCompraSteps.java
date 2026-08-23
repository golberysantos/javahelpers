package br.com.javahelperai.cucumber;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FinalizarCompraSteps {

    @Given("que o cliente {string} está autenticado")
    public void cliente_autenticado(String email) {
        // TODO: preparar contexto de autenticação (mock ou teste de integração)
        System.out.println("Autenticando cliente: " + email);
    }

    @Given("o carrinho contém:")
    public void carrinho_contem(DataTable table) {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        // TODO: popular um carrinho de teste com os dados da tabela
        rows.forEach(r -> System.out.println("Produto: " + r.get("produto") + " qnt: " + r.get("quantidade")));
    }

    @Given("o cliente seleciona o endereço {string}")
    public void seleciona_endereco(String endereco) {
        // TODO: armazenar endereço no contexto de teste
        System.out.println("Endereço selecionado: " + endereco);
    }

    @Given("escolhe a forma de pagamento {string}")
    public void escolhe_forma_pagamento(String forma) {
        // TODO: armazenar forma de pagamento
        System.out.println("Forma de pagamento: " + forma);
    }

    @When("o cliente confirma a compra")
    public void cliente_confirma_compra() {
        // TODO: acionar fluxo de finalização de compra no teste
        System.out.println("Cliente confirma a compra");
    }

    @When("o sistema processa o pagamento com resultado {string}")
    public void sistema_processa_pagamento(String resultado) {
        // TODO: simular resultado do gateway de pagamento (aprovado / recusado / pendente)
        System.out.println("Resultado do pagamento: " + resultado);
    }

    @Then("o pedido deve ser criado com status {string}")
    public void pedido_criado_status(String status) {
        // TODO: validar criação de pedido e status
        System.out.println("Validando pedido com status: " + status);
        // Exemplo de assert (ajuste para validação real)
        assertNotNull(status);
    }

    @Then("o cliente deve receber a confirmação por email")
    public void cliente_recebe_confirmacao() {
        // TODO: verificar envio de email (mock/verificação)
        System.out.println("Verificando envio de confirmação por email");
    }

    @Then("o pedido não deve ser criado")
    public void pedido_nao_criado() {
        // TODO: verificar que nenhum pedido foi persistido
        System.out.println("Verificando que o pedido não foi criado");
    }

    @Then("o cliente deve ver a mensagem {string}")
    public void cliente_ver_mensagem(String mensagem) {
        // TODO: validar mensagem exibida ao usuário
        System.out.println("Mensagem exibida: " + mensagem);
    }

    @Then("o pedido deve ter status {string}")
    public void pedido_status_generico(String status) {
        pedido_criado_status(status);
    }
}
