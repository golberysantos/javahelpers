package br.com.helper.designpartterns.facadefactory;



/**
 * Enumeração dos tipos de produtos financeiros.
 */
public enum TipoProduto {
    POUPANCA("Poupança"),
    CDB("Certificado de Depósito Bancário"),
    LCI("Letra de Crédito Imobiliário"),
    LCA("Letra de Crédito do Agronegócio"),
    FUNDO_INVESTIMENTO("Fundo de Investimento"),
    PREVIDENCIA("Previdência Privada"),
    INVESTIMENTO("Investimento Geral");

    private final String descricao;

    TipoProduto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
