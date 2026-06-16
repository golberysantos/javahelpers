package br.com.helper.oop.contabancaria;

import java.math.BigDecimal;

public class ContaBancaria {

	private double saldo;

	public ContaBancaria(BigDecimal saldoInicial) {
	        this.saldo = saldoInicial.doubleValue();
	    }

	public void depositar(BigDecimal valor) {
		if (valor.floatValue() > 0) {
			saldo += valor.doubleValue();
		}
	}

	public double getSaldo() {
		return saldo;
	}

	public void sacar(BigDecimal valor) {
		if (valor.doubleValue() > 0 && valor.doubleValue() <= saldo) {
			saldo -= valor.doubleValue();
		}
	}
}
