// Atividade número divisivel por 3
function numeroDivisivelPorTres (numero){
    if(numero %3 === 0){
        console.log(`${numero} é divisível por três!`);
    } else{
        console.log(`${numero} não é divisível por três`);
    }
}

// Teste da função
numeroDivisivelPorTres(9); // Verdadeiro
numeroDivisivelPorTres(10); // Falso
numeroDivisivelPorTres(15); // Verdadeiro
numeroDivisivelPorTres(7); // Falso
