let lcu;
let pcu;
lcu = document.getElementById("usuario").value;
pcu = document.getElementById("senha").value;

function addUser(l, p) {
    login.push(l);
    password.push(p);
}

function listar() {

    for (let index = 0; index < login.length; index++) {
        const l = login[index];
        const p = password[index];
        console.log(l);
        console.log(p);
    }

}