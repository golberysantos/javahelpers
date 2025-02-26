var login = new Array();
var senha = new Array();

function inserirUsuario(l, s) {
    login.push(l);
    senha.push(s);
}

function listar() {
    for (let index = 0; index < login.length; index++) {
        const l = login[index];
        const s = senha[index];
        console.log(l);
        console.log(s);
    }
}
