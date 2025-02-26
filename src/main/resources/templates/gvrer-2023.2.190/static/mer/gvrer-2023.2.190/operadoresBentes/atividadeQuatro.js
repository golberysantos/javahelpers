// Atividade divisível por cinco

function numeroDivisvelcinco(numero); {
    if(numero %5 === 0){
        console.log(`${numero} É divisível por cinco!`);
    } else{
        console.log(`${numero} Não é divisível por cinco!`);
    }
}

//Teste da função

numeroDivisivelcinco(15); // Verdadeiro
numeroDivisivelcinco(19); // Falso
numeroDivisivelcinco(5); // Verdadeiro
numeroDivisivelcinco(7); // Falso