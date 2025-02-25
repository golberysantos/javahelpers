// Atividade palíndromo

function verificaPalindromo(str){
    const stringLimpa = str.replace("/\s+/g,").toLowerCase();
    const stringReversa = stringLimpa.split('').reverse().join('');

    return stringLimpa === stringReversa
}

//Teste 
console.log(verificaPalindromo("arara")); // Verdade
console.log(verificaPalindromo("A man a plan a canal panama")); // Verdade
console.log(verificaPalindromo("hello")); // Falso
console.log(verificaPalindromo("racecar'"); // Verdade)