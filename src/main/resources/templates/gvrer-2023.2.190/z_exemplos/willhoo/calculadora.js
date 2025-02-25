function processar() {
    const num1 = parseFloat(document.getElementById("num1").value);
    const num2 = parseFloat(document.getElementById("num2").value);
    const operador = document.getElementById("operador").value;
    let resultado;

    switch (operador) {
        case "+":
            resultado = somar(num1,num2);
            break;
        case "-":
            resultado = subtrair(num1,num2);
            break;
        case "x":
            resultado = multiplicar(num1,num2);
            break;
        case "/":
            resultado = dividir(num1,num2);
            break;
        default:
            resultado = "Operador inválido!";
    }

    document.getElementById("resultado").value = resultado;
}

function somar(num1,num2) {
    return num1+num2;
}

function subtrair(num1,num2) {
    return num1-num2;
}

function multiplicar(num1,num2) {
    return num1*num2;
}

function dividir(num1,num2) {
    return num1/num2;
}



/*let num1;
let num2;
let operador = 0;
let resultado = 0.0;

function Calculadora(num1, num2) {
    this.num1 = num1;
    this.num2 = num2;
}

function calcular(operador) {
    console.log("OPERADOR >>> " + operador);

    switch (operador) {
        case "+":
            console.log("SOMA: ");
            console.log(somar());
            resultado = somar();
            break;
        case "-":
            console.log("SUBTRAÇÃO: ");
            console.log(subtrair());
            resultado = subtrair();
            break;
        case "x":
            console.log("MULTIPLICAÇÃO: ");
            console.log(multiplicar());
            resultado = multiplicar();
            break;
        case "/":
            console.log("DIVISÃO: ");
            console.log(dividir());
            resultado = dividir();
            break;
        default:
            console.log("OPERADOR ERRADO");
            break;
    }
    return resultado;
}

function dividir() {
    return this.num1 / this.num2;
}

function multiplicar() {
    return this.num1 * this.num2;
}

function somar() {
    return this.num1 + this.num2;
}

function subtrair() {
    return this.num1 - this.num2;
}*/