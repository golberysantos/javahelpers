package br.com.helper.designpartterns.facadeclassica;


/**
 * Exceção lançada quando ocorre um erro durante uma operação de transferência bancária.
 *
 * @author Seu Nome
 * @version 1.0
 */
public class TransferenciaException extends Exception {

    private static final long serialVersionUID = 1L;  // ← ADICIONADO

    /**
     * Construtor com mensagem de erro.
     *
     * @param mensagem Descrição do erro ocorrido
     */
    public TransferenciaException(String mensagem) {
        super(mensagem);
    }

    /**
     * Construtor com mensagem e causa (para encadeamento de exceções).
     *
     * @param mensagem Descrição do erro ocorrido
     * @param causa Exceção original que causou este erro
     */
    public TransferenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
