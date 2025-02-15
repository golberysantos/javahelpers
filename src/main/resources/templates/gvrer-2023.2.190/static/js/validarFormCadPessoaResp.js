function validar() {

    // Toggle password visibility
    document.getElementById("togglePassword")?.addEventListener("click", function () {
        var passwordField = document.getElementById("senha");
        var type = passwordField.type === "password" ? "text" : "password";
        passwordField.type = type;
    });

    // Função para validar campos
    var usuario = document.getElementById("nome");
    var usuario = document.getElementById("nome");

    // Limpar as bordas de erro antes de validar novamente
    usuario.classList.remove("border-danger");



    var isValid = true;

    // Verifica se o campo de nome está vazio
    if (usuario.value.trim() === "") {
        usuario.classList.add("border-danger");  // Adiciona borda vermelha
        isValid = false;
    }


    // Retorna verdadeiro se os campos estiverem preenchidos, falso caso contrário
    return isValid;


}