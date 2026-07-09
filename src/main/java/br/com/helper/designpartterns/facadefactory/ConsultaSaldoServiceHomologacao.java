package br.com.helper.designpartterns.facadefactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.helper.designpartterns.facadeclassica.subsistema.ConsultaSaldoService;

/**
 * Serviço de consulta de saldo para ambiente de HOMOLOGAÇÃO.
 *
 * <p>Este serviço simula dados reais mas com valores controlados para testes
 * em ambiente de homologação. Utiliza um banco de dados de teste ou
 * dados mockados com comportamento realístico.</p>
 *
 * @author Seu Nome
 * @version 1.0
 */
public class ConsultaSaldoServiceHomologacao extends ConsultaSaldoService {

    private static final Logger logger = LoggerFactory.getLogger(ConsultaSaldoServiceHomologacao.class);

    // Simula um banco de dados de homologação
    private final Map<String, ContaHomologacao> contasHomologacao = new HashMap<>();
    private final Random random = new Random();

    public ConsultaSaldoServiceHomologacao() {
        inicializarContasHomologacao();
        logger.info("ConsultaSaldoServiceHomologacao inicializado com {} contas",
                   contasHomologacao.size());
    }

    /**
     * Consulta saldo da conta no ambiente de homologação.
     *
     * @param numeroConta Número da conta
     * @return Saldo da conta (simulado com valores realísticos)
     */
    @Override
    public BigDecimal consultarSaldo(String numeroConta) {
        logger.info("[HOMOLOGAÇÃO] Consultando saldo para conta: {}", numeroConta);

        // Validação básica
        if (numeroConta == null || numeroConta.trim().isEmpty()) {
            logger.warn("[HOMOLOGAÇÃO] Tentativa de consulta com conta nula/vazia");
            return BigDecimal.ZERO;
        }

        // Simula latência de rede (para testes de performance)
        simularLatencia();

        // Busca no "banco de dados" de homologação
        ContaHomologacao conta = contasHomologacao.get(numeroConta);

        if (conta == null) {
            logger.warn("[HOMOLOGAÇÃO] Conta não encontrada: {}", numeroConta);
            // Em homologação, cria uma conta automática se não existir
            conta = criarContaHomologacao(numeroConta);
            contasHomologacao.put(numeroConta, conta);
            logger.info("[HOMOLOGAÇÃO] Conta criada automaticamente: {}", numeroConta);
        }

        // Simula pequenas variações no saldo (como se houvesse movimentações)
        BigDecimal saldoComVariacao = conta.getSaldo().add(
            new BigDecimal(random.nextInt(100) - 50) // Variação de -50 a +50
        );

        // Mantém saldo positivo
        if (saldoComVariacao.compareTo(BigDecimal.ZERO) < 0) {
            saldoComVariacao = BigDecimal.ZERO;
        }

        logger.debug("[HOMOLOGAÇÃO] Saldo retornado: R$ {}", saldoComVariacao);
        return saldoComVariacao.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Inicializa as contas de homologação com dados pré-definidos.
     */
    private void inicializarContasHomologacao() {
        // Contas de teste (padrão para homologação)
        contasHomologacao.put("12345-6", new ContaHomologacao(
            "12345-6",
            new BigDecimal("1500.00"),
            "João Silva",
            LocalDateTime.now().minusMonths(2)
        ));

        contasHomologacao.put("98765-4", new ContaHomologacao(
            "98765-4",
            new BigDecimal("2500.00"),
            "Maria Santos",
            LocalDateTime.now().minusMonths(6)
        ));

        contasHomologacao.put("11111-1", new ContaHomologacao(
            "11111-1",
            new BigDecimal("100.00"),
            "Cliente Pobre",
            LocalDateTime.now().minusDays(1)
        ));

        contasHomologacao.put("99999-9", new ContaHomologacao(
            "99999-9",
            new BigDecimal("10000.00"),
            "Cliente Rico",
            LocalDateTime.now().minusYears(3)
        ));

        contasHomologacao.put("55555-5", new ContaHomologacao(
            "55555-5",
            new BigDecimal("0.00"),
            "Cliente Zerado",
            LocalDateTime.now().minusMonths(1)
        ));

        logger.debug("[HOMOLOGAÇÃO] Contas de teste inicializadas: {}", contasHomologacao.keySet());
    }

    /**
     * Cria uma nova conta de homologação automaticamente.
     */
    private ContaHomologacao criarContaHomologacao(String numeroConta) {
        return new ContaHomologacao(
            numeroConta,
            new BigDecimal(random.nextInt(10000)), // Saldo aleatório
            "Cliente_" + numeroConta,
            LocalDateTime.now()
        );
    }

    /**
     * Simula latência de rede para testes de performance.
     */
    private void simularLatencia() {
        try {
            // Latência entre 100ms e 500ms (realístico)
            int latencia = 100 + random.nextInt(400);
            Thread.sleep(latencia);
            logger.trace("[HOMOLOGAÇÃO] Latência simulada: {}ms", latencia);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("[HOMOLOGAÇÃO] Thread interrompida durante simulação de latência");
        }
    }

    /**
     * Método específico de homologação para atualizar saldo manualmente.
     */
    public void atualizarSaldoParaTeste(String numeroConta, BigDecimal novoSaldo) {
        logger.info("[HOMOLOGAÇÃO] Atualizando saldo da conta {} para R$ {}",
                   numeroConta, novoSaldo);

        ContaHomologacao conta = contasHomologacao.get(numeroConta);
        if (conta != null) {
            conta.setSaldo(novoSaldo);
        } else {
            logger.warn("[HOMOLOGAÇÃO] Conta não encontrada para atualização: {}", numeroConta);
        }
    }

    // ===== CLASSE INTERNA =====

    private static class ContaHomologacao {
        private final String numero;
        private BigDecimal saldo;
        private final String titular;
        private final LocalDateTime dataAbertura;

        public ContaHomologacao(String numero, BigDecimal saldo, String titular,
                               LocalDateTime dataAbertura) {
            this.numero = numero;
            this.saldo = saldo;
            this.titular = titular;
            this.dataAbertura = dataAbertura;
        }

        public String getNumero() { return numero; }
        public BigDecimal getSaldo() { return saldo; }
        public void setSaldo(BigDecimal saldo) {
            this.saldo = saldo != null ? saldo : BigDecimal.ZERO;
        }
        public String getTitular() { return titular; }
        public LocalDateTime getDataAbertura() { return dataAbertura; }
    }
}