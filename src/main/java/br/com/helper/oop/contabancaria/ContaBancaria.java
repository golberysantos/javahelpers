package br.com.helper.oop.contabancaria;

public class ContaBancaria {

	private int saldo;

	public ContaBancaria(int saldoInicial) {
	        this.saldo = saldoInicial;
	    }

	public void depositar(int valor) {
		if (valor > 0) {
			saldo += valor;
		}
	}

	public void sacar(int valor) {
		if (valor > 0 && valor <= saldo) {
			saldo -= valor;
		}
	}

	public int getSaldo() {
		return saldo;
	}
}
