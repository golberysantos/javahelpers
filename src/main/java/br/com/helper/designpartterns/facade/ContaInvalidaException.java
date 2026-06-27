package br.com.helper.designpartterns.facade;

public class ContaInvalidaException extends Exception {
    public ContaInvalidaException(String mensagem) {
        super(mensagem);
    }
}
