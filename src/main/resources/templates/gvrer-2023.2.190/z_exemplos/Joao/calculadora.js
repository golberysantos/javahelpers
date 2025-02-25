class cac

let num1;
let num2;
 let resultado = 0.0;
 cac() {

 }

function Calculadora(Double num1, Double num2) {
        this.num1 = num1;
        this.num2 = num2;
    }

    public double calcular(char operador) {

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

    private double dividir() {
        return this.num1 / this.num2;
    }

    private double multiplicar() {
        return this.num1 * this.num2;
    }

    private Double somar() {
        return this.num1 + this.num2;
    }

    private Double subtrair() {
        return this.num1 - this.num2;
    }
}
