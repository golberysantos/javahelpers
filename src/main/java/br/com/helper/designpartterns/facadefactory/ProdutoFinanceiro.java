package br.com.helper.designpartterns.facadefactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa um produto financeiro oferecido pelo banco.
 * 
 * @author Golbery Santos
 * @version 1.0
 */
public class ProdutoFinanceiro {
    
    private static final long serialVersionUID = 1L;
    
    // ===== ATRIBUTOS =====
    private final String nome;
    private final String codigo;
    private final BigDecimal taxaRendimento;
    private final LocalDateTime dataCriacao;
    private final TipoProduto tipo;
    private final BigDecimal valorMinimoAplicacao;
    private final Integer prazoMinimoDias;
    private final boolean ativo;
    
    // ===== CONSTRUTOR PRINCIPAL =====
    public ProdutoFinanceiro(String nome, String codigo, BigDecimal taxaRendimento) {
        this(nome, codigo, taxaRendimento, TipoProduto.INVESTIMENTO, 
             new BigDecimal("100.00"), 30, true);
    }
    
    // ===== CONSTRUTOR COMPLETO =====
    public ProdutoFinanceiro(String nome, String codigo, BigDecimal taxaRendimento,
                             TipoProduto tipo, BigDecimal valorMinimoAplicacao,
                             Integer prazoMinimoDias, boolean ativo) {
        this.nome = validarString(nome, "Nome");
        this.codigo = validarString(codigo, "Código");
        this.taxaRendimento = validarTaxa(taxaRendimento);
        this.tipo = tipo != null ? tipo : TipoProduto.INVESTIMENTO;
        this.valorMinimoAplicacao = valorMinimoAplicacao != null ? valorMinimoAplicacao : BigDecimal.ZERO;
        this.prazoMinimoDias = prazoMinimoDias != null && prazoMinimoDias > 0 ? prazoMinimoDias : 0;
        this.ativo = ativo;
        this.dataCriacao = LocalDateTime.now();
    }
    
    // ===== MÉTODOS DE VALIDAÇÃO =====
    private String validarString(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " não pode ser nulo ou vazio");
        }
        return valor.trim();
    }
    
    private BigDecimal validarTaxa(BigDecimal taxa) {
        if (taxa == null) {
            return BigDecimal.ZERO;
        }
        if (taxa.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Taxa de rendimento não pode ser negativa");
        }
        return taxa;
    }
    
    // ===== GETTERS =====
    public String getNome() {
        return nome;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public BigDecimal getTaxaRendimento() {
        return taxaRendimento;
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    public TipoProduto getTipo() {
        return tipo;
    }
    
    public BigDecimal getValorMinimoAplicacao() {
        return valorMinimoAplicacao;
    }
    
    public Integer getPrazoMinimoDias() {
        return prazoMinimoDias;
    }
    
    public boolean isAtivo() {
        return ativo;
    }
    
    // ===== MÉTODOS DE NEGÓCIO =====
    public boolean isDisponivelParaAplicacao() {
        return ativo && valorMinimoAplicacao.compareTo(BigDecimal.ZERO) > 0;
    }
    
    public BigDecimal calcularRendimentoProjetado(BigDecimal valorAplicado, int dias) {
        if (valorAplicado == null || valorAplicado.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor de aplicação deve ser positivo");
        }
        if (dias <= 0) {
            throw new IllegalArgumentException("Dias deve ser maior que zero");
        }
        
        // Cálculo simples: Juros compostos diários
        // Fórmula: VF = VP * (1 + taxa)^(dias/365)
        BigDecimal taxaDiaria = taxaRendimento.divide(new BigDecimal("365"), 10, BigDecimal.ROUND_HALF_UP);
        BigDecimal fator = BigDecimal.ONE.add(taxaDiaria);
        BigDecimal fatorElevado = fator.pow(dias);
        return valorAplicado.multiply(fatorElevado).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    // ===== MÉTODOS SOBRESCRITOS =====
    @Override
    public String toString() {
        return String.format("ProdutoFinanceiro{nome='%s', codigo='%s', taxa=%.2f%%, tipo=%s, ativo=%s}",
                           nome, codigo, taxaRendimento.multiply(new BigDecimal("100")), tipo, ativo);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProdutoFinanceiro that = (ProdutoFinanceiro) o;
        return Objects.equals(codigo, that.codigo);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
