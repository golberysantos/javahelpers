const status = String(gets()).trim();
let resultado = "ERRO_STATUS";

if (status === "APROVADO") {
  resultado = "PAGAMENTO";
} else if (status === "PENDENTE") {
  resultado = "ANALISE_MANUAL";
} else if (status === "NEGADO") {
  resultado = "ENCERRAMENTO";
}

print(resultado);