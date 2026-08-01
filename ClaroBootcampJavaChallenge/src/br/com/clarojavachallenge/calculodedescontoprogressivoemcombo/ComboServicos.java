package br.com.clarojavachallenge.calculodedescontoprogressivoemcombo;

import java.util.Arrays;

public class ComboServicos {
	private Servico[] servicos;
    private double desconto1, desconto2, desconto3;
    private double descontoAdicional = 20.00;

    public ComboServicos(Servico[] servicos, double[] descontos) {
        this.servicos = servicos;
        this.desconto1 = descontos[0];
        this.desconto2 = descontos[1];
        this.desconto3 = descontos[2];
    }

    // Método para calcular o valor total do combo com descontos
    public double calcularValorTotal() {
        // Conta quantos serviços foram contratados (com valor maior que 0)
        int servicosContratados = (int) Arrays.stream(servicos)
                .filter(servico -> servico.getValor() > 0)
                .count();

        // Determina o percentual de desconto com base na quantidade de serviços contratados
        double desconto = 0;
        if (servicosContratados == 1) {
            desconto = desconto1;
        } else if (servicosContratados == 2) {
            desconto = desconto2;
        } else if (servicosContratados == 3) {
            desconto = desconto3;
        }

        // Calcula o valor total bruto (soma dos serviços ativos)
        double valorBruto = Arrays.stream(servicos)
                .filter(servico -> servico.getValor() > 0)
                .mapToDouble(Servico::getValor)
                .sum();

        // Aplica o desconto percentual
        double valorComDesconto = valorBruto * (1 - desconto / 100.0);

        // Aplica desconto adicional de R$ 20,00 se todos os 3 serviços forem contratados
        if (servicosContratados == 3) {
            valorComDesconto -= descontoAdicional;
        }

        return valorComDesconto;
    }
}
