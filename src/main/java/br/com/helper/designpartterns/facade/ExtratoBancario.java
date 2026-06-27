package br.com.helper.designpartterns.facade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExtratoBancario {
    private final String numeroConta;
    private final BigDecimal saldoAtual;
    private final List<Transacao> transacoes;
    
    public ExtratoBancario(String numeroConta, BigDecimal saldoAtual, List<Transacao> transacoes) {
        this.numeroConta = numeroConta;
        this.saldoAtual = saldoAtual;
        this.transacoes = new ArrayList<>(transacoes);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== EXTRATO BANCÁRIO =====\n");
        sb.append("Conta: ").append(numeroConta).append("\n");
        sb.append("Saldo: R$ ").append(saldoAtual).append("\n");
        sb.append("Histórico:\n");
        for (Transacao t : transacoes) {
            sb.append("  - ").append(t.getData()).append(": ")
              .append(t.getTipo()).append(" - R$ ")
              .append(t.getValor()).append("\n");
        }
        return sb.toString();
    }
}

