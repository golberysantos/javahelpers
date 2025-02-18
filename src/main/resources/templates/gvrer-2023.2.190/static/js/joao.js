
function pegarItemSelecionado() {
    let idItemSelect;
    let txtSelect;

    idItemSelect = document.getElementById("idEscolhaAtividade")
        .selectedIndex

    txtSelect = document.getElementById("idEscolhaAtividade")[idItemSelect]
        .textContent

    console.log(txtSelect);
}


