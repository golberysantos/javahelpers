//Atividade para verificar se um número é positivo, negativo ou zero

function verificaNumero(numero){
    if(numero > 0){
        console.log("O número é positivo!");
    }else if(numero < 0){
        console.log("O número é negativo!");
    } else {
        console.log("O número é zero!");
    }
}

//Testando o código com diferentes números
verificaNumero(10) // Positivo
verificaNumero(-5) // Negativo
verificaNumero(0) // Zero