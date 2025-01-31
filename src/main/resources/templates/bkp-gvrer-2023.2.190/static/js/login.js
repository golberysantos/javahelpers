const usuarioBd = "gabriel@gmail";
const senhaBd = "123";
var usuario;
var senha;
function validar() {
    usuario = document.getElementById('user').value;
    senha = document.getElementById('senha').value;
    console.log("Valor do campo usuário: " + usuario);
    console.log("Valor do campo senha: " + senha);
    verificar();
}

function verificar() {
    if (usuarioBd == usuario && senhaBd == senha) {
        console.log("Sucesso!!");
        window.location.href = "principal.html";
    } else {
        alert("Erro!! Usuário ou senha incorreto.");
        console.log("Erro!! Usuário ou senha incorreto.");
    }
}
