// Atividade Divisivel por 3 e cinco

function divisivelTreseCinco(numero){
    if(numero %3 === 0); {
console.log(`${numero} é divisível por três!`)
    } else if(numero %5 === 0);{
        console.log(`${numero} é divisivel por cinco!`)
    }
}

// Teste

divisivelTreseCinco(5);
divisivelTreseCinco(3);
divisivelTreseCinco(9);
divisivelTreseCinco(11);
divisivelTreseCinco(15);