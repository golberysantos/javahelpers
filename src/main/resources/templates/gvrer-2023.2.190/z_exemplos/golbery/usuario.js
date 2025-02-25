class User {
    constructor(login, senha) {
        this.login = login;
        this.senha = senha;
    }
}

var arrayUsuario = new Array();

function inserirUsuario(l, s) {
    novoUsuario = new User(l, s);    
    arrayUsuario.push(novoUsuario);
    console.log(arrayUsuario[0].login+ "| "+arrayUsuario[0].senha);    
    
}

function listar() {
    for (let index = 0; index < arrayUsuario.length; index++) {
        const l = arrayUsuario[index].login;
        const s = arrayUsuario[index].senha;
        console.log(l);
        console.log(s);
    }
}
