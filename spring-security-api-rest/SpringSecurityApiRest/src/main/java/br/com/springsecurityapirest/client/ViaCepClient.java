package br.com.springsecurityapirest.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.springsecurityapirest.dto.EnderecoResponse;

/**
 * Cliente declarativo Feign para consumo da API do ViaCEP.
 *
 * @author Golbery Santos
 */
@FeignClient(name = "viacep", url = "https://viacep.com.br/ws")
public interface ViaCepClient {

    @GetMapping("/{cep}/json")
    EnderecoResponse buscarPorCep(@PathVariable("cep") String cep);
}
