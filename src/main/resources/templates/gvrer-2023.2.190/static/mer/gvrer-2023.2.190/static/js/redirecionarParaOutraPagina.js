
function abrirEmOutraAba(url) {
    let e = document.querySelector("#button");
    e.addEventListener(
        "click",
        function (e) {
            e.preventDefault();
            abrirAba(url)
        }
    );
}

function abrirAba(url) {
    window.open(url); // abre link em outra janela
}

function abrirNaAbaAtual(url) {
    let e = document.querySelector("#button");
    e.addEventListener(
        "click",
        function (e) {
            e.preventDefault();
            carregarNaAbaAtual(url)
        }
    );
}

function carregarNaAbaAtual(url) {
    setTimeout(function () {
        window.location.assign(url); // carrega link na janela atual
    }, 500)
}

function exemplo(params) {
    setTimeout(function () {
        window.location.href = "http://www.google.com";
        //ou
        window.location.assign(url); // carrega link na janela atual
        //ou
        window.open(url); // abre link em outra janela
    }, 500);
}


function onClickAbaAtual(url) {
    setTimeout(function () {
        window.location.assign(url); // carrega link na janela atual
    }, 500)
}




