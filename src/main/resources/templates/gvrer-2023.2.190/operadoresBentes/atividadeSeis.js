// Atividade numero primo

function verificaNprimo(numero){
    if(numero <=1){
        return `${numero} não é um número primo`;
    }
    for(let i= 2, i <= Math.sqrt(numero); i ++){
        if(numero %i === 0){
            return `${numero} não é um número primo`;
        } 
    }
    return`${numero} é um número primo`;
}

// Teste

console.log(verificaNprimo(2)); //É um número primo
console.log(verificaNprimo(4)); //Não é um número primo
console.log(verificaNprimo(17)); //É um número primo
console.log(verificaNprimo(20)); //Não é um número primo