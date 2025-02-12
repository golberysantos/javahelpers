function validar() {

    if (document.getElementsByClassName("validar")[0].required) {
        if (document.getElementsByClassName("validar")[0].value == "") {
            return false;
        }
        return true;
    }

}