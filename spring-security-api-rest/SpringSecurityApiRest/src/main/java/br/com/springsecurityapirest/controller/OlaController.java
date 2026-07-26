package br.com.springsecurityapirest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.springsecurityapirest.client.ViaCepClient;
import br.com.springsecurityapirest.dto.EnderecoResponse;

/**
 * Controller REST para testar o Spring Security e OpenFeign.
 *
 * @author Golbery Santos
 */
@RestController
@RequestMapping("/api")
public class OlaController {

    private final ViaCepClient viaCepClient;

    public OlaController(ViaCepClient viaCepClient) {
        this.viaCepClient = viaCepClient;
    }

    @GetMapping("/publico")
    public String publico() {
        return "Este endpoint e publico. Qualquer um pode ver!";
    }

    @GetMapping("/protegido")
    public String protegido() {
        return "Este endpoint exige login. Autenticado com sucesso!";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Area do Administrador. Acesso concedido!";
    }

    @GetMapping("/cep/{cep}")
    public EnderecoResponse consultarCep(@PathVariable String cep) {
        return viaCepClient.buscarPorCep(cep);
    }
}
