package br.com.helper.designpartterns.facadefactory;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Serviço responsável por operações relacionadas a produtos financeiros.
 *
 * <p>Esta classe contém a lógica de negócio para gerenciar produtos financeiros,
 * incluindo consulta, filtragem, e cálculos.</p>
 *
 * @author Seu Nome
 * @version 1.0
 */
public class ProdutoFinanceiroService {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoFinanceiroService.class);

    // Simulação de um repositório de produtos (em produção seria um banco de dados)
    private final Map<String, ProdutoFinanceiro> produtos = new HashMap<>();

    // ===== CONSTRUTOR =====
    public ProdutoFinanceiroService() {
        inicializarProdutos();
    }

    // ===== MÉTODOS DE NEGÓCIO =====

    /**
     * Consulta todos os produtos disponíveis para uma conta específica.
     *
     * @param numeroConta Número da conta do cliente
     * @return Lista de produtos disponíveis
     */
    public List<ProdutoFinanceiro> consultarProdutos(String numeroConta) {
        logger.info("Consultando produtos para conta: {}", numeroConta);

        // Validação da conta
        if (numeroConta == null || numeroConta.trim().isEmpty()) {
            logger.warn("Tentativa de consulta com conta nula/vazia");
            return Collections.emptyList();
        }

        // Em produção: consultaria o perfil do cliente para produtos adequados
        // Aqui simulamos com base no tipo de conta

        if (numeroConta.startsWith("1")) {
            // Conta PF - produtos para pessoa física
            return produtos.values().stream()
                .filter(p -> p.isAtivo())
                .filter(p -> p.getTipo() != TipoProduto.INVESTIMENTO ||
                             p.getValorMinimoAplicacao().compareTo(new BigDecimal("100.00")) <= 0)
                .collect(Collectors.toList());
        } else {
            // Conta PJ - produtos para pessoa jurídica
            return produtos.values().stream()
                .filter(ProdutoFinanceiro::isAtivo)
                .collect(Collectors.toList());
        }
    }

    /**
     * Busca um produto pelo código.
     *
     * @param codigo Código único do produto
     * @return Produto encontrado ou null
     */
    public ProdutoFinanceiro buscarPorCodigo(String codigo) {
        logger.debug("Buscando produto por código: {}", codigo);

        if (codigo == null || codigo.trim().isEmpty()) {
            return null;
        }

        return produtos.get(codigo.trim().toUpperCase());
    }

    /**
     * Busca produtos por tipo.
     *
     * @param tipo Tipo do produto
     * @return Lista de produtos do tipo especificado
     */
    public List<ProdutoFinanceiro> buscarPorTipo(TipoProduto tipo) {
        logger.debug("Buscando produtos por tipo: {}", tipo);

        if (tipo == null) {
            return Collections.emptyList();
        }

        return produtos.values().stream()
            .filter(p -> p.getTipo() == tipo)
            .filter(ProdutoFinanceiro::isAtivo)
            .collect(Collectors.toList());
    }

    /**
     * Busca produtos com rendimento acima de um valor mínimo.
     *
     * @param taxaMinima Taxa mínima de rendimento (ex: 0.10 = 10%)
     * @return Lista de produtos que atendem ao critério
     */
    public List<ProdutoFinanceiro> buscarPorRendimentoMinimo(BigDecimal taxaMinima) {
        logger.debug("Buscando produtos com taxa >= {}", taxaMinima);

        if (taxaMinima == null || taxaMinima.compareTo(BigDecimal.ZERO) < 0) {
            return Collections.emptyList();
        }

        return produtos.values().stream()
            .filter(p -> p.isAtivo())
            .filter(p -> p.getTaxaRendimento().compareTo(taxaMinima) >= 0)
            .sorted((p1, p2) -> p2.getTaxaRendimento().compareTo(p1.getTaxaRendimento()))
            .collect(Collectors.toList());
    }

    /**
     * Calcula o rendimento de um produto para um período específico.
     *
     * @param codigoProduto Código do produto
     * @param valorAplicado Valor que será aplicado
     * @param dias Número de dias para cálculo
     * @return Valor total após o período
     */
    public BigDecimal calcularRendimentoProjetado(String codigoProduto,
                                                  BigDecimal valorAplicado,
                                                  int dias) {
        logger.info("Calculando rendimento para produto: {}, valor: {}, dias: {}",
                   codigoProduto, valorAplicado, dias);

        // Validações
        if (codigoProduto == null || codigoProduto.trim().isEmpty()) {
            throw new IllegalArgumentException("Código do produto não pode ser vazio");
        }

        if (valorAplicado == null || valorAplicado.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }

        if (dias <= 0) {
            throw new IllegalArgumentException("Dias deve ser maior que zero");
        }

        // Buscar produto
        ProdutoFinanceiro produto = buscarPorCodigo(codigoProduto);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado: " + codigoProduto);
        }

        // Validar se produto está ativo
        if (!produto.isAtivo()) {
            throw new IllegalStateException("Produto inativo: " + codigoProduto);
        }

        // Validar valor mínimo de aplicação
        if (valorAplicado.compareTo(produto.getValorMinimoAplicacao()) < 0) {
            throw new IllegalArgumentException(String.format(
                "Valor mínimo para este produto é R$ %.2f",
                produto.getValorMinimoAplicacao()
            ));
        }

        // Calcular rendimento
        return produto.calcularRendimentoProjetado(valorAplicado, dias);
    }

    /**
     * Verifica se um produto está disponível para um determinado cliente.
     *
     * @param codigoProduto Código do produto
     * @param numeroConta Número da conta do cliente
     * @return true se disponível, false caso contrário
     */
    public boolean isProdutoDisponivelParaCliente(String codigoProduto, String numeroConta) {
        logger.debug("Verificando disponibilidade do produto {} para conta {}",
                    codigoProduto, numeroConta);

        // Buscar produtos disponíveis para a conta
        List<ProdutoFinanceiro> produtosDisponiveis = consultarProdutos(numeroConta);

        // Verificar se o produto está na lista
        return produtosDisponiveis.stream()
            .anyMatch(p -> p.getCodigo().equals(codigoProduto));
    }

    /**
     * Recomenda produtos baseados no perfil do cliente.
     *
     * @param numeroConta Número da conta
     * @param perfilCliente Perfil de investimento (CONSERVADOR, MODERADO, AGRESSIVO)
     * @return Lista de produtos recomendados
     */
    public List<ProdutoFinanceiro> recomendarProdutos(String numeroConta,
                                                     PerfilCliente perfilCliente) {
        logger.info("Recomendando produtos para conta: {}, perfil: {}",
                   numeroConta, perfilCliente);

        List<ProdutoFinanceiro> disponiveis = consultarProdutos(numeroConta);

        if (perfilCliente == PerfilCliente.CONSERVADOR) {
            return disponiveis.stream()
                .filter(p -> p.getTipo() == TipoProduto.POUPANCA ||
                             p.getTipo() == TipoProduto.CDB ||
                             p.getTaxaRendimento().compareTo(new BigDecimal("0.08")) <= 0)
                .limit(5)
                .collect(Collectors.toList());
        } else if (perfilCliente == PerfilCliente.MODERADO) {
            return disponiveis.stream()
                .filter(p -> p.getTipo() != TipoProduto.POUPANCA)
                .filter(p -> p.getTaxaRendimento().compareTo(new BigDecimal("0.05")) >= 0)
                .limit(5)
                .collect(Collectors.toList());
        } else { // AGRESSIVO
            return disponiveis.stream()
                .filter(p -> p.getTipo() == TipoProduto.FUNDO_INVESTIMENTO ||
                             p.getTipo() == TipoProduto.PREVIDENCIA)
                .filter(p -> p.getTaxaRendimento().compareTo(new BigDecimal("0.12")) >= 0)
                .sorted((p1, p2) -> p2.getTaxaRendimento().compareTo(p1.getTaxaRendimento()))
                .limit(5)
                .collect(Collectors.toList());
        }
    }

    // ===== MÉTODOS PRIVADOS DE INICIALIZAÇÃO =====

    private void inicializarProdutos() {
        logger.info("Inicializando catálogo de produtos financeiros...");

        // Produtos - Poupança
        ProdutoFinanceiro poupanca = new ProdutoFinanceiro(
            "Poupança", "POUP001", new BigDecimal("0.065"),
            TipoProduto.POUPANCA, new BigDecimal("10.00"), 0, true
        );
        produtos.put(poupanca.getCodigo(), poupanca);

        // Produtos - CDB
        ProdutoFinanceiro cdb1 = new ProdutoFinanceiro(
            "CDB Pós-Fixado 100% CDI", "CDB001", new BigDecimal("0.12"),
            TipoProduto.CDB, new BigDecimal("1000.00"), 30, true
        );
        produtos.put(cdb1.getCodigo(), cdb1);

        ProdutoFinanceiro cdb2 = new ProdutoFinanceiro(
            "CDB Pós-Fixado 120% CDI", "CDB002", new BigDecimal("0.145"),
            TipoProduto.CDB, new BigDecimal("5000.00"), 90, true
        );
        produtos.put(cdb2.getCodigo(), cdb2);

        // Produtos - LCI/LCA
        ProdutoFinanceiro lci = new ProdutoFinanceiro(
            "LCI - Isento IR", "LCI001", new BigDecimal("0.09"),
            TipoProduto.LCI, new BigDecimal("10000.00"), 180, true
        );
        produtos.put(lci.getCodigo(), lci);

        ProdutoFinanceiro lca = new ProdutoFinanceiro(
            "LCA - Isento IR", "LCA001", new BigDecimal("0.095"),
            TipoProduto.LCA, new BigDecimal("10000.00"), 180, true
        );
        produtos.put(lca.getCodigo(), lca);

        // Produtos - Fundos
        ProdutoFinanceiro fundo1 = new ProdutoFinanceiro(
            "Fundo Multimercado", "FUND001", new BigDecimal("0.15"),
            TipoProduto.FUNDO_INVESTIMENTO, new BigDecimal("500.00"), 30, true
        );
        produtos.put(fundo1.getCodigo(), fundo1);

        ProdutoFinanceiro fundo2 = new ProdutoFinanceiro(
            "Fundo de Ações", "FUND002", new BigDecimal("0.20"),
            TipoProduto.FUNDO_INVESTIMENTO, new BigDecimal("1000.00"), 30, true
        );
        produtos.put(fundo2.getCodigo(), fundo2);

        // Produto - Previdência
        ProdutoFinanceiro previdencia = new ProdutoFinanceiro(
            "Previdência Privada VGBL", "PREV001", new BigDecimal("0.10"),
            TipoProduto.PREVIDENCIA, new BigDecimal("100.00"), 365, true
        );
        produtos.put(previdencia.getCodigo(), previdencia);

        logger.info("Catálogo inicializado com {} produtos", produtos.size());
    }

    // ===== ENUM PERFIL CLIENTE =====

    public enum PerfilCliente {
        CONSERVADOR, MODERADO, AGRESSIVO
    }
}
