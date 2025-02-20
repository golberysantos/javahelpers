var login = new Array();
var senha = new Array();

function inserirUsuario(l, s) {
    login.push(l);
    senha.push(s);
}

function listar() {
    console.log(login[0]);
    console.log(senha[0]);
    console.log(login[1]);
    console.log(senha[1]);
}