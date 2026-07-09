package br.com.helper.designpartterns.facadefactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Serviço de produtos financeiros para ambiente de HOMOLOGAÇÃO.
 *
 * <p>Este serviço fornece produtos financeiros configurados especificamente
 * para testes em ambiente de homologação.</p>
 *
 * @author Seu Nome
 * @version 1.0
 */
public class ProdutoFinanceiroServiceHomologacao extends ProdutoFinanceiroService {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoFinanceiroServiceHomologacao.class);
    private final Map<String, ProdutoFinanceiro> produtosHomologacao = new HashMap<>();

    public ProdutoFinanceiroServiceHomologacao() {
        inicializarProdutosHomologacao();
        logger.info("ProdutoFinanceiroServiceHomologacao inicializado");
    }

    @Override
    public List<ProdutoFinanceiro> consultarProdutos(String numeroConta) {
        logger.info("[HOMOLOGAÇÃO] Consultando produtos para conta: {}", numeroConta);

        // Simula comportamento real com alguns produtos específicos
        return produtosHomologacao.values().stream()
            .filter(ProdutoFinanceiro::isAtivo)
            .filter(p -> {
                // Simula alguns produtos que não estão disponíveis para todas as contas
                if (p.getCodigo().equals("PREMIUM001")) {
                    return numeroConta.startsWith("9"); // Apenas contas com 9
                }
                if (p.getCodigo().equals("EXCLUSIVO001")) {
                    return numeroConta.length() >= 7; // Contas com 7+ dígitos
                }
                return true;
            })
            .collect(Collectors.toList());
    }

    @Override
    public ProdutoFinanceiro buscarPorCodigo(String codigo) {
        logger.debug("[HOMOLOGAÇÃO] Buscando produto por código: {}", codigo);
        return produtosHomologacao.get(codigo);
    }

    @Override
    public List<ProdutoFinanceiro> buscarPorTipo(TipoProduto tipo) {
        logger.debug("[HOMOLOGAÇÃO] Buscando produtos por tipo: {}", tipo);
        return produtosHomologacao.values().stream()
            .filter(p -> p.getTipo() == tipo)
            .filter(ProdutoFinanceiro::isAtivo)
            .collect(Collectors.toList());
    }

    private void inicializarProdutosHomologacao() {
        // Produtos padrão (similares ao da produção)
        produtosHomologacao.put("POUP001", new ProdutoFinanceiro(
            "Poupança Homologação", "POUP001", new BigDecimal("0.065"),
            TipoProduto.POUPANCA, new BigDecimal("10.00"), 0, true
        ));

        produtosHomologacao.put("CDB001", new ProdutoFinanceiro(
            "CDB 100% CDI HMG", "CDB001", new BigDecimal("0.12"),
            TipoProduto.CDB, new BigDecimal("1000.00"), 30, true
        ));

        // Produtos específicos de homologação
        produtosHomologacao.put("TESTE001", new ProdutoFinanceiro(
            "Produto Teste 1", "TESTE001", new BigDecimal("0.15"),
            TipoProduto.INVESTIMENTO, new BigDecimal("100.00"), 7, true
        ));

        produtosHomologacao.put("TESTE002", new ProdutoFinanceiro(
            "Produto Teste 2 (Inativo)", "TESTE002", new BigDecimal("0.20"),
            TipoProduto.INVESTIMENTO, new BigDecimal("50.00"), 1, false
        ));

        // Produto premium (apenas contas especiais)
        produtosHomologacao.put("PREMIUM001", new ProdutoFinanceiro(
            "Produto Premium", "PREMIUM001", new BigDecimal("0.25"),
            TipoProduto.FUNDO_INVESTIMENTO, new BigDecimal("50000.00"), 365, true
        ));

        // Produto exclusivo
        produtosHomologacao.put("EXCLUSIVO001", new ProdutoFinanceiro(
            "Produto Exclusivo", "EXCLUSIVO001", new BigDecimal("0.18"),
            TipoProduto.INVESTIMENTO, new BigDecimal("10000.00"), 180, true
        ));

        logger.info("[HOMOLOGAÇÃO] {} produtos inicializados", produtosHomologacao.size());
    }
}
