package br.com.helper.designpartterns.facadefactory;

import java.util.EnumMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.helper.designpartterns.facadeclassica.subsistema.ConsultaSaldoService;
import br.com.helper.designpartterns.facadeclassica.subsistema.HistoricoTransacoesService;
import br.com.helper.designpartterns.facadeclassica.subsistema.ValidacaoContaService;

public class FacadeBancariaFactory {
    private static final Logger logger = LoggerFactory.getLogger(FacadeBancariaFactory.class);
    private static final Map<Ambiente, FacadeBancaria> factories = new EnumMap<>(Ambiente.class);

    // Configuração carregada de arquivo de propriedades
    private static String ambienteConfig = System.getProperty("app.ambiente", "PRODUCAO");
    private static final Ambiente ambienteAtual = Ambiente.valueOf(ambienteConfig);

    static {
        // Pré-inicializar as fachadas para cada ambiente
        factories.put(Ambiente.PRODUCAO, criarFacadeProducao());
        factories.put(Ambiente.HOMOLOGACAO, criarFacadeHomologacao());
        factories.put(Ambiente.DESENVOLVIMENTO, criarFacadeDesenvolvimento());
        factories.put(Ambiente.TESTE, criarFacadeTeste());
    }

    private static FacadeBancaria criarFacadeProducao() {
        logger.info("Criando Facade em PRODUÇÃO");
        // Serviços reais
        ValidacaoContaService validador = new ValidacaoContaService();
        ConsultaSaldoService saldoService = new ConsultaSaldoService();
        HistoricoTransacoesService historicoService = new HistoricoTransacoesService();
        TransferenciaService transferenciaService = new TransferenciaService();
        ProdutoFinanceiroService produtoService = new ProdutoFinanceiroService();

        return new FacadeBancariaPadrao(validador, saldoService, historicoService,
                                       transferenciaService, produtoService);
    }

    private static FacadeBancaria criarFacadeHomologacao() {
        logger.info("Criando Facade em HOMOLOGAÇÃO");
        // Serviços reais com configurações específicas de homologação
        // Pode ter acesso a bancos de dados de teste
        ValidacaoContaService validador = new ValidacaoContaService();
        ConsultaSaldoService saldoService = new ConsultaSaldoServiceHomologacao();
        HistoricoTransacoesService historicoService = new HistoricoTransacoesServiceHomologacao();
        TransferenciaService transferenciaService = new TransferenciaServiceHomologacao();
        ProdutoFinanceiroService produtoService = new ProdutoFinanceiroServiceHomologacao();

        return new FacadeBancariaPadrao(validador, saldoService, historicoService,
                                       transferenciaService, produtoService);
    }

    private static FacadeBancaria criarFacadeDesenvolvimento() {
        logger.info("Criando Facade em DESENVOLVIMENTO");
        // Mocks e serviços simulados
        return new FacadeBancariaMock();
    }

    private static FacadeBancaria criarFacadeTeste() {
        logger.info("Criando Facade em TESTE");
        // Configuração específica para testes unitários
        return new FacadeBancariaMock();
    }

    public static FacadeBancaria getFacade() {
        return getFacade(ambienteAtual);
    }

    public static FacadeBancaria getFacade(Ambiente ambiente) {
        FacadeBancaria facade = factories.get(ambiente);
        if (facade == null) {
            throw new IllegalArgumentException("Ambiente não suportado: " + ambiente);
        }
        logger.info("Retornando fachada para ambiente: {}", ambiente);
        return facade;
    }

    public enum Ambiente {
        PRODUCAO, HOMOLOGACAO, DESENVOLVIMENTO, TESTE
    }
}
