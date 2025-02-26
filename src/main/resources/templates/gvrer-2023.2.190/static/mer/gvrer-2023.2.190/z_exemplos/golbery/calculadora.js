
class Calculadora {
    constructor(num1, num2) {
        this.num1 = num1;
        this.num2 = num2;
    }

    calcular(operador) {
        console.log(operador);


        switch (operador) {
            case '+':
                console.log("SOMA: ");
                console.log(this.somar());
                resultado = this.somar();
                break;
            case "-":
                console.log("SUBTRAÇÃO: ");
                console.log(this.subtrair());
                resultado = this.subtrair();
                break;
            case "x":
                console.log("MULTIPLICAÇÃO: ");
                console.log(this.multiplicar());
                resultado = this.multiplicar();
                break;
            case "/":
                console.log("DIVISÃO: ");
                console.log(this.dividir());
                resultado = this.dividir();
                break;
            default:
                console.log("OPERADOR ERRADO");
                break;
        }
        return resultado;
    }

    dividir() {
        return this.num1 / this.num2;
    }

    multiplicar() {
        return this.num1 * this.num2;
    }

    somar() {
        return this.num1 + this.num2;
    }

    subtrair() {
        return this.num1 - this.num2;
    }


}








