package br.com.helper.designpartterns.facadecache;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import br.com.helper.designpartterns.facadeclassica.ContaInvalidaException;
import br.com.helper.designpartterns.facadeclassica.ExtratoBancario;
import br.com.helper.designpartterns.facadeclassica.TransferenciaException;
import br.com.helper.designpartterns.facadeclassica.subsistema.ConsultaSaldoService;
import br.com.helper.designpartterns.facadeclassica.subsistema.HistoricoTransacoesService;
import br.com.helper.designpartterns.facadeclassica.subsistema.Transacao;
import br.com.helper.designpartterns.facadeclassica.subsistema.ValidacaoContaService;
import br.com.helper.designpartterns.facadefactory.ProdutoFinanceiro;
import br.com.helper.designpartterns.facadefactory.TransferenciaService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

/**
 * Fachada bancária com cache avançado para produção.
 * 
 * <p>Esta implementação utiliza Google Guava para caching e inclui
 * gerenciamento de ciclo de vida, métricas e invalidação seletiva.</p>
 * 
 * @author Seu Nome
 * @version 2.0
 */
public class FacadeContaBancariaCacheProfissional implements AutoCloseable {
    
    private static final Logger logger = LoggerFactory.getLogger(FacadeContaBancariaCacheProfissional.class);
    
    // ===== SERVIÇOS =====
    private final ValidacaoContaService validador;
    private final ConsultaSaldoService saldoService;
    private final HistoricoTransacoesService historicoService;
    private final TransferenciaService transferenciaService;
    
    // ===== CACHES (Guava) =====
    private final Cache<String, ExtratoBancario> cacheExtratos;
    private final Cache<String, BigDecimal> cacheSaldos;
    private final Cache<String, List<ProdutoFinanceiro>> cacheProdutos;
    
    // ===== METADADOS PARA INVALIDAÇÃO SELETIVA =====
    private final Cache<String, String> contaParaChavesExtrato; // Mapeia conta -> chaves de extrato
    private final AtomicLong totalCacheHits = new AtomicLong(0);
    private final AtomicLong totalCacheMisses = new AtomicLong(0);
    private final AtomicLong totalInvalidacoes = new AtomicLong(0);
    
    // ===== CONFIGURAÇÕES =====
    private static final long TEMPO_EXPIRACAO_EXTRATO = 300; // 5 minutos (em segundos)
    private static final long TEMPO_EXPIRACAO_SALDO = 60; // 1 minuto (em segundos)
    private static final long TEMPO_EXPIRACAO_PRODUTOS = 3600; // 1 hora (em segundos)
    private static final int MAXIMO_ITENS_CACHE = 10000;
    private static final int MAXIMO_ITENS_POR_CONTA = 100;
    
    // ===== CONSTRUTOR =====
    public FacadeContaBancariaCacheProfissional(ValidacaoContaService validador,
                                                ConsultaSaldoService saldoService,
                                                HistoricoTransacoesService historicoService,
                                                TransferenciaService transferenciaService) {
        this.validador = validador;
        this.saldoService = saldoService;
        this.historicoService = historicoService;
        this.transferenciaService = transferenciaService;
        
        // ===== CACHES COM GUAVA =====
        this.cacheExtratos = CacheBuilder.newBuilder()
            .expireAfterWrite(TEMPO_EXPIRACAO_EXTRATO, TimeUnit.SECONDS)
            .maximumSize(MAXIMO_ITENS_CACHE)
            .recordStats() // Habilita métricas
            .build();
        
        this.cacheSaldos = CacheBuilder.newBuilder()
            .expireAfterWrite(TEMPO_EXPIRACAO_SALDO, TimeUnit.SECONDS)
            .maximumSize(MAXIMO_ITENS_CACHE)
            .recordStats()
            .build();
        
        this.cacheProdutos = CacheBuilder.newBuilder()
            .expireAfterWrite(TEMPO_EXPIRACAO_PRODUTOS, TimeUnit.SECONDS)
            .maximumSize(MAXIMO_ITENS_CACHE)
            .recordStats()
            .build();
        
        // ===== MAPA PARA INVALIDAÇÃO SELETIVA =====
        this.contaParaChavesExtrato = CacheBuilder.newBuilder()
            .expireAfterWrite(TEMPO_EXPIRACAO_EXTRATO, TimeUnit.SECONDS)
            .maximumSize(MAXIMO_ITENS_CACHE)
            .build();
        
        logger.info("Fachada com cache profissional inicializada");
    }
    
    // ===== INICIALIZAÇÃO E FINALIZAÇÃO =====
    
    @PostConstruct
    public void iniciar() {
        logger.info("Iniciando fachada com cache - Monitoramento ativo");
        // Pode iniciar threads de monitoramento aqui
    }
    
    @PreDestroy
    @Override
    public void close() {
        logger.info("Finalizando fachada com cache");
        // Estatísticas finais
        logger.info("Estatísticas finais - Hits: {}, Misses: {}, Hit Rate: {:.2f}%",
                   totalCacheHits.get(), totalCacheMisses.get(),
                   calcularHitRate());
        
        // Limpar caches
        cacheExtratos.invalidateAll();
        cacheSaldos.invalidateAll();
        cacheProdutos.invalidateAll();
        contaParaChavesExtrato.invalidateAll();
    }
    
    // ===== MÉTODOS DE NEGÓCIO COM CACHE =====
    
    /**
     * Obtém extrato completo com cache.
     */
    public ExtratoBancario obterExtratoCompleto(String numeroConta, int ultimosDias) 
            throws ContaInvalidaException {
        
        String cacheKey = criarChaveExtrato(numeroConta, ultimosDias);
        
        // 1. Tentar obter do cache
        ExtratoBancario cacheExtrato = cacheExtratos.getIfPresent(cacheKey);
        if (cacheExtrato != null) {
            totalCacheHits.incrementAndGet();
            logger.debug("Cache HIT para extrato: {}", cacheKey);
            return cacheExtrato;
        }
        
        totalCacheMisses.incrementAndGet();
        logger.debug("Cache MISS para extrato: {}", cacheKey);
        
        // 2. Validar e consultar subsistema
        if (!validador.validarConta(numeroConta)) {
            throw new ContaInvalidaException("Conta inválida: " + numeroConta);
        }
        
        // 3. Executar consultas
        BigDecimal saldo = saldoService.consultarSaldo(numeroConta);
        List<Transacao> transacoes = historicoService.consultarHistorico(numeroConta, ultimosDias);
        
        // 4. Atualizar cache
        cacheSaldos.put(numeroConta, saldo);
        
        ExtratoBancario extrato = new ExtratoBancario(numeroConta, saldo, transacoes);
        cacheExtratos.put(cacheKey, extrato);
        
        // 5. Registrar chave para invalidação seletiva
        registrarChaveExtrato(numeroConta, cacheKey);
        
        return extrato;
    }
    
    /**
     * Consulta saldo com cache.
     */
    public BigDecimal consultarSaldo(String numeroConta) throws ContaInvalidaException {
        // Tenta obter do cache de saldos
        BigDecimal cacheSaldo = cacheSaldos.getIfPresent(numeroConta);
        if (cacheSaldo != null) {
            totalCacheHits.incrementAndGet();
            logger.debug("Cache HIT para saldo: {}", numeroConta);
            return cacheSaldo;
        }
        
        totalCacheMisses.incrementAndGet();
        logger.debug("Cache MISS para saldo: {}", numeroConta);
        
        // Consulta real
        if (!validador.validarConta(numeroConta)) {
            throw new ContaInvalidaException("Conta inválida: " + numeroConta);
        }
        
        BigDecimal saldo = saldoService.consultarSaldo(numeroConta);
        cacheSaldos.put(numeroConta, saldo);
        return saldo;
    }
    
    /**
     * Realiza transferência com invalidação inteligente de cache.
     */
    public boolean realizarTransferencia(String contaOrigem, String contaDestino, 
                                         BigDecimal valor) throws TransferenciaException {
        
        logger.info("Transferência de {} para {} no valor R$ {}", 
                   contaOrigem, contaDestino, valor);
        
        // 1. Validações
        if (!validador.validarConta(contaOrigem) || !validador.validarConta(contaDestino)) {
            throw new TransferenciaException("Conta origem ou destino inválida");
        }
        
        // 2. Invalidar cache ANTES da operação (consistência)
        invalidarCacheConta(contaOrigem);
        invalidarCacheConta(contaDestino);
        
        // 3. Executar transferência
        boolean sucesso = false;
        try {
            sucesso = transferenciaService.executarTransferencia(contaOrigem, contaDestino, valor);
            
            if (sucesso) {
                logger.info("Transferência realizada com sucesso");
                // Recarregar caches com dados atualizados (assíncrono)
                recarregarCachesAssincrono(contaOrigem, contaDestino);
            } else {
                logger.warn("Transferência falhou. Revertendo invalidação...");
                // Recarregar caches com dados anteriores (não temos rollback real)
                recarregarCacheConta(contaOrigem);
                recarregarCacheConta(contaDestino);
            }
            
        } catch (Exception e) {
            logger.error("Erro na transferência", e);
            // Em caso de erro, recarregar caches
            recarregarCacheConta(contaOrigem);
            recarregarCacheConta(contaDestino);
            throw new TransferenciaException("Erro na transferência: " + e.getMessage(), e);
        }
        
        return sucesso;
    }
    
    // ===== GERENCIAMENTO DE CACHE =====
    
    /**
     * Invalida todas as entradas de cache relacionadas a uma conta.
     */
    private void invalidarCacheConta(String numeroConta) {
        totalInvalidacoes.incrementAndGet();
        logger.debug("Invalidando cache para conta: {}", numeroConta);
        
        // 1. Invalidar saldo
        cacheSaldos.invalidate(numeroConta);
        
        // 2. Invalidar extratos relacionados
        String chaveExtrato = contaParaChavesExtrato.getIfPresent(numeroConta);
        if (chaveExtrato != null) {
            cacheExtratos.invalidate(chaveExtrato);
            contaParaChavesExtrato.invalidate(numeroConta);
        }
        
        // 3. Invalidar produtos (se houver relação com conta)
        // cacheProdutos.invalidate("PRODUTOS:" + numeroConta);
    }
    
    /**
     * Recarrega o cache para uma conta.
     */
    private void recarregarCacheConta(String numeroConta) {
        try {
            if (validador.validarConta(numeroConta)) {
                // Recarregar saldo
                BigDecimal saldo = saldoService.consultarSaldo(numeroConta);
                cacheSaldos.put(numeroConta, saldo);
                logger.debug("Cache recarregado para conta: {}", numeroConta);
            }
        } catch (Exception e) {
            logger.error("Erro ao recarregar cache para conta: {}", numeroConta, e);
        }
    }
    
    /**
     * Recarrega caches de forma assíncrona para não bloquear a resposta.
     */
    private void recarregarCachesAssincrono(String contaOrigem, String contaDestino) {
        // Em produção, usar um executor para tarefas assíncronas
        // Exemplo: CompletableFuture.runAsync(() -> { ... });
        
        // Executar em background
        new Thread(() -> {
            try {
                Thread.sleep(100); // Pequeno delay para não conflitar
                recarregarCacheConta(contaOrigem);
                recarregarCacheConta(contaDestino);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
    
    /**
     * Registra a relação entre conta e chave de extrato para invalidação.
     */
    private void registrarChaveExtrato(String numeroConta, String chaveExtrato) {
        // Guardamos apenas a última chave (mais recente)
        // Em produção, usaríamos uma lista de chaves
        contaParaChavesExtrato.put(numeroConta, chaveExtrato);
    }
    
    // ===== MÉTRICAS E MONITORAMENTO =====
    
    /**
     * Calcula a taxa de acerto do cache.
     */
    public double calcularHitRate() {
        long hits = totalCacheHits.get();
        long misses = totalCacheMisses.get();
        long total = hits + misses;
        return total == 0 ? 0.0 : (double) hits / total * 100;
    }
    
    /**
     * Retorna estatísticas do cache.
     */
    public CacheStats getCacheStats() {
        return new CacheStats(
            totalCacheHits.get(),
            totalCacheMisses.get(),
            totalInvalidacoes.get(),
            cacheExtratos.size(),
            cacheSaldos.size(),
            cacheProdutos.size(),
            calcularHitRate()
        );
    }
    
    /**
     * Limpa todo o cache.
     */
    public void limparCache() {
        logger.info("Limpando todo o cache");
        cacheExtratos.invalidateAll();
        cacheSaldos.invalidateAll();
        cacheProdutos.invalidateAll();
        contaParaChavesExtrato.invalidateAll();
    }
    
    /**
     * Força a recarga de uma conta.
     */
    public void recarregarConta(String numeroConta) {
        logger.info("Recarregando conta: {}", numeroConta);
        invalidarCacheConta(numeroConta);
        recarregarCacheConta(numeroConta);
    }
    
    // ===== MÉTODOS AUXILIARES =====
    
    private String criarChaveExtrato(String numeroConta, int ultimosDias) {
        return String.format("EXTRATO:%s:%d", numeroConta, ultimosDias);
    }
    
    // ===== CLASSE DE ESTATÍSTICAS =====
    
    public static class CacheStats {
        private final long hits;
        private final long misses;
        private final long invalidacoes;
        private final long tamanhoExtratos;
        private final long tamanhoSaldos;
        private final long tamanhoProdutos;
        private final double hitRate;
        
        public CacheStats(long hits, long misses, long invalidacoes, 
                         long tamanhoExtratos, long tamanhoSaldos, 
                         long tamanhoProdutos, double hitRate) {
            this.hits = hits;
            this.misses = misses;
            this.invalidacoes = invalidacoes;
            this.tamanhoExtratos = tamanhoExtratos;
            this.tamanhoSaldos = tamanhoSaldos;
            this.tamanhoProdutos = tamanhoProdutos;
            this.hitRate = hitRate;
        }
        
        // Getters
        public long getHits() { return hits; }
        public long getMisses() { return misses; }
        public long getInvalidacoes() { return invalidacoes; }
        public long getTamanhoExtratos() { return tamanhoExtratos; }
        public long getTamanhoSaldos() { return tamanhoSaldos; }
        public long getTamanhoProdutos() { return tamanhoProdutos; }
        public double getHitRate() { return hitRate; }
        
        @Override
        public String toString() {
            return String.format("CacheStats{hits=%d, misses=%d, invalidacoes=%d, " +
                               "tamanhoExtratos=%d, tamanhoSaldos=%d, tamanhoProdutos=%d, hitRate=%.2f%%}",
                               hits, misses, invalidacoes, tamanhoExtratos, 
                               tamanhoSaldos, tamanhoProdutos, hitRate);
        }
    }
}