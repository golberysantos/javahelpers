
var n1;
var n2;
var operador;
var resultado;

function processar(operador) {
    console.log('Parametro: ', operador);

    if (operador != 'L') {
        n1 = document.getElementById('n1').valueAsNumber;
        n2 = document.getElementById('n2').valueAsNumber;
        operador = document.getElementById('operador').value;
    }

    switch (operador) {
        case '+':
            document.getElementById('resultado').textContent = soma();
            break;
        case '-':
            document.getElementById('resultado').textContent = sub();
            break;
        case '*':
            document.getElementById('resultado').textContent = mult();
            break;
        case '/':
            document.getElementById('resultado').textContent = div();
            break;
        case 'L':
            document.getElementById('resultado').textContent = limpar();
            break;
        default:
            alert("VOCÊ INOFORMOU UM OPERADOR ERRADO.");
            break;
    }
}

function soma() {
    resultado = n1 + n2;
    console.log("Soma = ", resultado);
    return resultado;
}

function sub() {
    resultado = n1 - n2;
    console.log("Sub = ", resultado);
    return resultado;
}

function mult() {
    resultado = n1 * n2;
    console.log("Mult = ", resultado);
    return resultado;
}

function div() {
    resultado = n1 / n2;
    console.log("Div = ", resultado);
    return resultado;
}

function limpar() {
    document.getElementById('resultado').textContent = '';
    document.getElementById('n1').value = '';
    document.getElementById('n2').value = '';
    document.getElementById('operador').value = '';
}

