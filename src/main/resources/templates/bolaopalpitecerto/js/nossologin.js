
function exibirNome() {
    document.getElementById("demo").innerHTML = "Hello Golbery"
}
function limparNome() {
    document.getElementById("demo").innerHTML = ""
}
function pegarNomeDoUsuario() {
    var usuario;
    usuario = document.getElementById("usuario").value;
    document.getElementById("eu").innerHTML = usuario;
    return usuario;
}
function pegarSenhaDoUsuario() {
    var senha;
    senha = document.getElementById("senha").value;
    document.getElementById("es").innerHTML = senha;            
    return senha;
}
function validar() {
    let u = pegarNomeDoUsuario();
    let s = pegarSenhaDoUsuario();
    //alert("Usuário: " + u + ", Senha: "+ s);
    var usuarioDoBD = "A@A";
    var senhaDoBD = "123";
    if (u == usuarioDoBD  && s == senhaDoBD) {
        alert("Usuário Ok!!")
    } else {
        alert("Usuário ou senha incorreto!!")
    }
}


