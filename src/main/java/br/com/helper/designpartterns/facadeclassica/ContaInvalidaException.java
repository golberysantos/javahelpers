package br.com.helper.designpartterns.facadeclassica;

public class ContaInvalidaException extends Exception {
    public ContaInvalidaException(String mensagem) {
        super(mensagem);
    }
}
