let loginOK = false;
let senhaOK = false;
let encontrou = false;

function encontrar(l, s) {
    for (let index = 0; index < login.length; index++) {
        const lbd = login[index];
        const sbd = senha[index];
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