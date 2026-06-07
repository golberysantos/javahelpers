package br.com.helper.oop.contabancaria;

public class ServicoTransferencia {
	public void realizarPix(double valor, ValidadorPix validador) {
		if (validador.validar(valor)) {
			System.out.println("Pix de R$ " + valor + " autorizado.");
		} else {
			System.out.println("Transação bloqueada pelo validador.");
		}
	}
	
	public void transferir(double valor, CalculadoraTarifa estrategia) {
        double tarifa = estrategia.calcular(valor);
        System.out.println("Valor: R$ " + valor);
        System.out.println("Tarifa: R$ " + tarifa);
        System.out.println("Total: R$ " + (valor + tarifa));
    }
}
