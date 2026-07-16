package br.com.padroes.facade;

import br.com.padroes.facade.subsistema.CepApi;
import br.com.padroes.facade.subsistema.CrmService;

public class Facade {

	public void migrarCliente(String nome, String cep) {
		String cidade = CepApi.getInstancia().recuperarCidade(cep);
		String estado = CepApi.getInstancia().recuperarEstado(cep);
		
		CrmService.gravarCliente(nome, cep, cidade, estado);
	}
}
