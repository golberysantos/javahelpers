package br.com.helper.designpartterns.facadefactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.helper.designpartterns.facadeclassica.TransferenciaException;


/**
 * Serviço de transferência para ambiente de HOMOLOGAÇÃO.
 * 
 * <p>
 * Este serviço simula transferências bancárias em ambiente de homologação, com
 * validações realísticas e registro de operações.
 * </p>
 * 
 * @author Golbery Santos
 * @version 1.0
 */
public class TransferenciaServiceHomologacao extends TransferenciaService {
    
    private static final Logger logger = LoggerFactory.getLogger(TransferenciaServiceHomologacao.class);
    
    private final ConsultaSaldoServiceHomologacao saldoService;
    private final HistoricoTransacoesServiceHomologacao historicoService;
    private final ConcurrentHashMap<String, LimiteTransferencia> limitesDiarios;
    private final AtomicLong contadorTransferencias = new AtomicLong(0);
    
    private static final BigDecimal LIMITE_DIARIO_PADRAO = new BigDecimal("5000.00");
    private static final BigDecimal LIMITE_MAXIMO = new BigDecimal("10000.00");
    
    public TransferenciaServiceHomologacao() {
        this.saldoService = new ConsultaSaldoServiceHomologacao();
        this.historicoService = new HistoricoTransacoesServiceHomologacao();
        this.limitesDiarios = new ConcurrentHashMap<>();
        logger.info("TransferenciaServiceHomologacao inicializado");
    }
    
    // ✅ AGORA COMPATÍVEL COM O PAI (mesma assinatura com throws)
    @Override
    public boolean executarTransferencia(String origem, String destino, BigDecimal valor) 
            throws TransferenciaException {  // ✅ OK! Pai também declara
        
        logger.info("[HOMOLOGAÇÃO] Iniciando transferência de {} para {} no valor R$ {}", 
                   origem, destino, valor);
        
        long idTransferencia = contadorTransferencias.incrementAndGet();
        LocalDateTime inicio = LocalDateTime.now();
        
        try {
            // Chama validações do pai (já inclui verificação de valor, contas, etc.)
            super.executarTransferencia(origem, destino, valor);
            
            // 1. Verificar saldo (específico de homologação)
            BigDecimal saldoOrigem = saldoService.consultarSaldo(origem);
            if (saldoOrigem.compareTo(valor) < 0) {
                logger.warn("[HOMOLOGAÇÃO] Saldo insuficiente na conta {}: R$ {} < R$ {}", 
                           origem, saldoOrigem, valor);
                throw new TransferenciaException(
                    String.format("Saldo insuficiente. Disponível: R$ %.2f, Solicitado: R$ %.2f",
                                 saldoOrigem, valor)
                );
            }
            
            // 2. Verificar limite diário (específico de homologação)
            verificarLimiteDiario(origem, valor);
            
            // 3. Processar transferência
            boolean sucesso = processarTransferencia(origem, destino, valor);
            
            if (sucesso) {
                // Registrar transações
                historicoService.registrarTransacao(origem, "TRANSFERENCIA_ENVIADA", valor.negate());
                historicoService.registrarTransacao(destino, "TRANSFERENCIA_RECEBIDA", valor);
                
                // Atualizar saldos
                BigDecimal novoSaldoOrigem = saldoOrigem.subtract(valor);
                saldoService.atualizarSaldoParaTeste(origem, novoSaldoOrigem);
                
                BigDecimal saldoDestino = saldoService.consultarSaldo(destino);
                BigDecimal novoSaldoDestino = saldoDestino.add(valor);
                saldoService.atualizarSaldoParaTeste(destino, novoSaldoDestino);
                
                logger.info("[HOMOLOGAÇÃO] Transferência #{} concluída com sucesso", idTransferencia);
                return true;
            } else {
                throw new TransferenciaException("Erro interno ao processar transferência");
            }
            
        } catch (TransferenciaException e) {
            logger.error("[HOMOLOGAÇÃO] Transferência #{} falhou: {}", idTransferencia, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("[HOMOLOGAÇÃO] Erro inesperado na transferência #{}", idTransferencia, e);
            throw new TransferenciaException("Erro inesperado: " + e.getMessage(), e);
        } finally {
            long duracao = java.time.Duration.between(inicio, LocalDateTime.now()).toMillis();
            logger.debug("[HOMOLOGAÇÃO] Transferência #{} durou {}ms", idTransferencia, duracao);
        }
    }
    
    // ===== MÉTODOS AUXILIARES =====
    
    private void verificarLimiteDiario(String conta, BigDecimal valor) 
            throws TransferenciaException {
        
        LimiteTransferencia limite = limitesDiarios.computeIfAbsent(
            conta, 
            k -> new LimiteTransferencia(LIMITE_DIARIO_PADRAO, LocalDateTime.now())
        );
        
        if (!limite.isMesmoDia(LocalDateTime.now())) {
            limite.resetar(LocalDateTime.now());
            limitesDiarios.put(conta, limite);
            logger.debug("[HOMOLOGAÇÃO] Limite diário resetado para conta {}", conta);
        }
        
        if (limite.getValorUtilizado().add(valor).compareTo(limite.getLimiteDiario()) > 0) {
            BigDecimal disponivel = limite.getLimiteDiario().subtract(limite.getValorUtilizado());
            throw new TransferenciaException(
                String.format("Limite diário excedido. Disponível hoje: R$ %.2f", disponivel)
            );
        }
        
        limite.adicionarValor(valor);
        logger.debug("[HOMOLOGAÇÃO] Limite utilizado hoje: R$ {}", limite.getValorUtilizado());
    }
    
    private boolean processarTransferencia(String origem, String destino, BigDecimal valor) {
        try {
            Thread.sleep(200 + (int)(Math.random() * 300));
            return Math.random() < 0.95;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
    
    // ===== CLASSE INTERNA =====
    
    private static class LimiteTransferencia {
        private final BigDecimal limiteDiario;
        private BigDecimal valorUtilizado;
        private LocalDateTime dataReferencia;
        
        public LimiteTransferencia(BigDecimal limiteDiario, LocalDateTime dataReferencia) {
            this.limiteDiario = limiteDiario;
            this.valorUtilizado = BigDecimal.ZERO;
            this.dataReferencia = dataReferencia;
        }
        
        public boolean isMesmoDia(LocalDateTime data) {
            return dataReferencia.toLocalDate().equals(data.toLocalDate());
        }
        
        public void resetar(LocalDateTime novaData) {
            this.valorUtilizado = BigDecimal.ZERO;
            this.dataReferencia = novaData;
        }
        
        public void adicionarValor(BigDecimal valor) {
            this.valorUtilizado = this.valorUtilizado.add(valor);
        }
        
        public BigDecimal getLimiteDiario() { return limiteDiario; }
        public BigDecimal getValorUtilizado() { return valorUtilizado; }
    }
}