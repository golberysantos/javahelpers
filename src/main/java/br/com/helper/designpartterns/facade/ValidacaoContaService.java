package br.com.helper.designpartterns.facade;

public class ValidacaoContaService {
    private static final Logger logger = LoggerFactory.getLogger(ValidacaoContaService.class);
    
    public boolean validarConta(String numeroConta) {
        logger.info("Validando conta: {}", numeroConta);
        // Simulação de validação
        if (numeroConta == null || numeroConta.trim().isEmpty()) {
            return false;
        }
        return numeroConta.matches("\\d{5}-\\d{1}"); // Ex: 12345-6
    }
}

