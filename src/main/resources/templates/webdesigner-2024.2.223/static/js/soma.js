var n1 = 10;
var n2 = 10;
var resultado = n1 + n2;

function fSoma() {
    console.log("BOTÃO 'OK' FOI CLICADO");
    this.n1 = document.getElementById("inputN1").value;
    this.n2 = document.getElementById("inputN2").value;
    this.resultado = (parseInt(n1) + parseInt(n2));
    document.getElementById("resultado").value = this.resultado;
    fResultado();
}

function fResultado() {
    console.log("operação realizada!!");
    console.log("N1: " + this.n1);
    console.log("N2: " + this.n2);
    console.log("RESULTADO DA SOMA: " + this.resultado);
    document.getElementById("idSpanResultado")
        .textContent = "RESULTADO DA SOMA = " + this.resultado;
}

function limpar() {
    console.log("o botão limpar foi clicado.");
    document.getElementById('inputN1').value = "";
    document.getElementById('inputN2').value = "";
    document.getElementById('resultado').value = "";
    document.getElementById('idSpanResultado').textContent = "";
}
