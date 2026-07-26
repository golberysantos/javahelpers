package br.com.springsecurityapirest.dto;

/**
 * DTO para mapear o retorno da API ViaCEP.
 *
 * @author Golbery Santos
 */
public record EnderecoResponse(
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        String localidade,
        String uf,
        String estado,
        String regiao
) {}
