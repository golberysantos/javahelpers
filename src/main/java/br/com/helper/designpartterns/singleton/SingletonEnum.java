package br.com.helper.designpartterns.singleton;

public enum SingletonEnum {
    INSTANCIA;
    
    // Campos (podem ser finais ou não)
    private String configuracao;
    private int contador = 0;
    
    // Construtor do enum (sempre private)
    SingletonEnum() {
        this.configuracao = "configuracao_padrao";
        System.out.println("SingletonEnum inicializado!");
    }
    
    // Métodos de negócio
    public void fazerAlgo() {
        System.out.println("Executando ação no SingletonEnum...");
        contador++;
    }
    
    public String getConfiguracao() {
        return configuracao;
    }
    
    public void setConfiguracao(String configuracao) {
        this.configuracao = configuracao;
    }
    
    public int getContador() {
        return contador;
    }
    
    // Método para reset (exemplo)
    public void reset() {
        contador = 0;
        configuracao = "configuracao_padrao";
    }
}
