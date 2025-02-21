
let loginOk = false;
let senhaOk = false;
let encontrou = false;
function encontrar(l, s) {
    for (let index = 0; index < login.length; index++) {
        const lbd = login[index];
        const sbd = senha[index];
        if (l == lbd) {
            loginOk = true;
        }
        if (s == sbd) {
            senhaOk = true; 
        }
        if (loginOk && senhaOk) {
            encontrou = true;
        }
        return encontrou;
    }
    
    
}