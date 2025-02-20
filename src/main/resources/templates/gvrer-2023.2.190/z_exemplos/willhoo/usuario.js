//Declara uma nova variável e cria um novo objeto do tipo Array
var login = new Array();
var password = new Array();

function addUser(l, p) {
    login.push(l);
    password.push(p);
}

function listar() {
    console.log(login[0]);
    console.log(password[0]);
}