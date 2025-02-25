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

function salvarArquivo() {
    // Preenchendo as arrays (exemplo)
    /*
    login.push('usuario1');
    senha.push('senha1');
    */

    // Criando um objeto com os dados
    const dados = {
        login: login,
        senha: senha
    };

    // Convertendo o objeto para uma string JSON
    const jsonData = JSON.stringify(dados, null, 2);

    // Salvando o JSON em um arquivo .txt
    fs.writeFile('dados.txt', jsonData, (err) => {
        if (err) {
            console.log('Erro ao salvar o arquivo:', err);
        } else {
            console.log('Arquivo salvo com sucesso!');
        }
    });
}
