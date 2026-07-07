package br.com.helper.designpartterns.facadefactory;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.helper.designpartterns.facadeclassica.TransferenciaException;

public class TransferenciaService {
    private static final Logger logger = LoggerFactory.getLogger(TransferenciaService.class);
    
    // ✅ AGORA DECLARA throws TransferenciaException
    public boolean executarTransferencia(String origem, String destino, BigDecimal valor) 
            throws TransferenciaException {
        
        logger.info("Executando transferência de {} para {}", origem, destino);
        
        try {
            // Validações
            if (origem == null || origem.trim().isEmpty()) {
                throw new TransferenciaException("Conta de origem não pode ser vazia");
            }
            
            if (destino == null || destino.trim().isEmpty()) {
                throw new TransferenciaException("Conta de destino não pode ser vazia");
            }
            
            if (origem.equals(destino)) {
                throw new TransferenciaException("Conta de origem e destino não podem ser iguais");
            }
            
            if (valor == null) {
                throw new TransferenciaException("Valor não pode ser nulo");
            }
            
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                throw new TransferenciaException("Valor deve ser positivo");
            }
            
            if (valor.compareTo(new BigDecimal("0.01")) < 0) {
                throw new TransferenciaException("Valor mínimo para transferência é R$ 0,01");
            }
            
            // Limite de segurança
            if (valor.compareTo(new BigDecimal("10000")) > 0) {
                logger.warn("Transferência acima de 10k requer autorização");
                throw new TransferenciaException("Transferência acima de R$ 10.000,00 requer autorização");
            }
            
            // Simular processamento
            Thread.sleep(100);
            logger.info("Transferência executada com sucesso");
            return true;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Transferência interrompida", e);
            throw new TransferenciaException("Transferência interrompida", e);
        } catch (TransferenciaException e) {
            // Relançar exceções de negócio
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado na transferência", e);
            throw new TransferenciaException("Erro inesperado: " + e.getMessage(), e);
        }
    }
}