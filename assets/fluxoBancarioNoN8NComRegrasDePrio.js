const linhas = String(gets()).split("<br>").map(l => l.trim());

const tipoEvento = linhas[0];
const statusEvento = linhas[1];
const etapaFluxo = linhas[2];

let resposta = "IGNORAR";

if (statusEvento === "ERRO") {
  resposta = "FALHA";
} else if (tipoEvento === "PIX" && statusEvento === "OK" && etapaFluxo === "validar") {
  resposta = "PROCESSAR";
} else if (tipoEvento === "TED" && statusEvento === "OK" && etapaFluxo === "validar") {
  resposta = "AGENDAR";
} else if (etapaFluxo === "revisar") {
  resposta = "ANALISAR";
}

print(resposta);