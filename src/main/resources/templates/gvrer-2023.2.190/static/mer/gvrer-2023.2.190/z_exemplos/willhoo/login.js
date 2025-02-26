let loginOK = false;
let passwordOK = false;
let found = false;

function find(l, p) {
    for (let index = 0; index < login.length; index++) {
        const ldb = login[index]; //ldb (Login Database)
        const pdb = password[index]; //pdb (Password Database)

        if (l == ldb) {
            loginOK = true;
        }

        if (p == pdb) {
            passwordOK = true;
        }

        if (loginOK && senhaOK) {
            found = true;
        }

        return found;

    }
}