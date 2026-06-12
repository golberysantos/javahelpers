package br.com.helper.oop.contabancaria;

public class ServicoTransferenciaController {
	public void servicotransferenca() {
		
		ServicoTransferencia servico = new ServicoTransferencia();
		
		// Usando classes concretas
		servico.transferir(1000.0, new TarifaPix());
		servico.transferir(1000.0, new TarifaTed());

		// Ou com classe anônima inline, sem criar um arquivo separado
		servico.transferir(500.0, new CalculadoraTarifa() {
		    @Override
		    public double calcular(double valor) {
		        return valor * 0.01; // tarifa de 1% para casos especiais
		    }
		});


	}
}
