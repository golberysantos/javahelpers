package br.com.helper.designpartterns.facadeclassica.subsistema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transacao {
    private final LocalDateTime data;
    private final String tipo;
    private final BigDecimal valor;
    
    public Transacao(LocalDateTime data, String tipo, BigDecimal valor) {
        this.data = data;
        this.tipo = tipo;
        this.valor = valor;
    }
    // Getters...

	public LocalDateTime getData() {
		return data;
	}

	public String getTipo() {
		return tipo;
	}

	public BigDecimal getValor() {
		return valor;
	}
}

