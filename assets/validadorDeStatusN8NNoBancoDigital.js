// IMPORTANTE: As funções "gets" e "print" são acessíveis globalmente e têm as seguintes funcionalidades: 
// - "gets": lê UMA linha com dados de entrada (inputs) do usuário;
// - "print": imprime um texto de saída (output) e pula uma linha ("\n") automaticamente;

const statusRecebido = String(gets()).trim();
const proximaAcaoPorStatus = {
  START: "VALIDATE",
  PROCESS: "SAVE",
  ERROR: "RETRY",
  END: "FINISH"
};

// Se o status nao existir no mapeamento, a saida deve ser INVALID.
const resposta = proximaAcaoPorStatus[statusRecebido] || "INVALID";

print(resposta);