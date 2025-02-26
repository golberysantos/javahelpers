let loginOK = false;
let senhaOK = false;
let encontrou = false;

function encontrar(l, s) {
    for (let index = 0; index < arrayUsuario.length; index++) {
        const lbd = arrayUsuario[index].login;
        const sbd = arrayUsuario[index].senha;
        if (l == lbd) {
            loginOK = true;
        }
        if (s == sbd) {
            senhaOK = true
        }
        if (loginOK && senhaOK) {
            encontrou = true;
        }
        return encontrou;
    }
}