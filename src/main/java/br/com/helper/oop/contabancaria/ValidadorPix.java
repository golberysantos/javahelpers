package br.com.helper.oop.contabancaria;

@FunctionalInterface
public interface ValidadorPix {
	boolean validar(double valor);
}
