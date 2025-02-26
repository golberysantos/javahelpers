
 let num1;
 let num2;
 let resultado = 0.0;

    function Calculadora (num1, num2) {
        this.num1 = num1;
        this.num2 = num2;
    }

    function calcular(operador) {

        switch (operador) {
            case '+':
                System.out.println("SOMA: ");
                System.out.println(somar());
                resultado = somar();
                break;
            case '-':
                System.out.println("SUBTRAÇÃO: ");
                System.out.println(subtrair());
                resultado = subtrair();
                break;
            case 'x':
                System.out.println("MULTIPLICAÇÃO: ");
                System.out.println(multiplicar());
                resultado = multiplicar();
                break;
            case '/':
                System.out.println("DIVISÃO: ");
                System.out.println(dividir());
                resultado = dividir();
                break;
            default:
                System.out.println("OPERADOR ERRADO");
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

    function  subtrair() {
        return this.num1 - this.num2;
    }

