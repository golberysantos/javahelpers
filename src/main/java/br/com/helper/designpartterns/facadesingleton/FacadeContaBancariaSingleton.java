package br.com.helper.designpartterns.facadesingleton;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.helper.designpartterns.facadeclassica.ContaInvalidaException;
import br.com.helper.designpartterns.facadeclassica.ExtratoBancario;
import br.com.helper.designpartterns.facadeclassica.subsistema.ConsultaSaldoService;
import br.com.helper.designpartterns.facadeclassica.subsistema.HistoricoTransacoesService;
import br.com.helper.designpartterns.facadeclassica.subsistema.Transacao;
import br.com.helper.designpartterns.facadeclassica.subsistema.ValidacaoContaService;

//===== FACADE SINGLETON =====

public class FacadeContaBancariaSingleton {
 private static final Logger logger = LoggerFactory.getLogger(FacadeContaBancariaSingleton.class);
 private static volatile FacadeContaBancariaSingleton instance;

 // Dependências (agora com estado compartilhado - cuidado!)
 private final ValidacaoContaService validador;
 private final ConsultaSaldoService saldoService;
 private final HistoricoTransacoesService historicoService;
 private final CacheService cacheService; // Serviço de cache adicional

 // Construtor privado com inicialização lazy das dependências
 private FacadeContaBancariaSingleton() {
     // Inicializa os serviços internamente (acoplamento mais forte)
     this.validador = new ValidacaoContaService();
     this.saldoService = new ConsultaSaldoService();
     this.historicoService = new HistoricoTransacoesService();
     this.cacheService = new CacheService(); // Serviço de cache em memória
     logger.info("Fachada Singleton inicializada");
 }

 // Método thread-safe para obter a instância
 public static FacadeContaBancariaSingleton getInstance() {
     if (instance == null) {
         synchronized (FacadeContaBancariaSingleton.class) {
             if (instance == null) {
                 instance = new FacadeContaBancariaSingleton();
             }
         }
     }
     return instance;
 }

 // Método com cache para otimizar consultas
 public ExtratoBancario obterExtratoCompleto(String numeroConta, int ultimosDias)
         throws ContaInvalidaException {

     logger.info("Obtendo extrato para conta: {} (com cache)", numeroConta);

     // Verificar cache (operações custosas)
     String cacheKey = numeroConta + "_" + ultimosDias;
     ExtratoBancario cacheExtrato = cacheService.obter(cacheKey);
     if (cacheExtrato != null) {
         logger.info("Retornando extrato do cache para conta: {}", numeroConta);
         return cacheExtrato;
     }

     // Executar operações (sem cache)
     if (!validador.validarConta(numeroConta)) {
         throw new ContaInvalidaException("Conta inválida: " + numeroConta);
     }

     BigDecimal saldo = saldoService.consultarSaldo(numeroConta);
     List<Transacao> transacoes = historicoService.consultarHistorico(numeroConta, ultimosDias);
     ExtratoBancario extrato = new ExtratoBancario(numeroConta, saldo, transacoes);

     // Armazenar no cache
     cacheService.armazenar(cacheKey, extrato);
     return extrato;
 }
}


