/**
 * Crie um script que analisa o preço de um determinado produto.
 * Se o valor for maior que R$2000,00, será aplicado um desconto
 * de 15%, caso contrário será aplicado um desconto somente de 5%.
 * Ao final do processamento o script deverá exibir o valor original
 * e o valor com o desconto.,
 * 
 */
function desconto(){
let precoOriginal = document.getElementById("precoOriginal").value;
let valorComDesconto = 0;


if (precoOriginal >= 2000) {
    valorComDesconto = precoOriginal - (precoOriginal * 0.15);
} else {
    valorComDesconto = precoOriginal - (precoOriginal * 0.05);
}

document.getElementById("resultado").innerHTML = valorComDesconto;

}