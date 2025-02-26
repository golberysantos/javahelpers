function validar() {
    // Toggle password visibility
    document.getElementById("togglePassword")?.addEventListener("click", function () {
        var passwordField = document.getElementById("senha");
        var type = passwordField.type === "password" ? "text" : "password";
        passwordField.type = type;
    });

    // Função para validar campos
    var usuario = document.getElementById("usuario");
    var senha = document.getElementById("senha");
    var termosDeUso = document.getElementById("termosDeUso");

    // Limpar as bordas de erro antes de validar novamente
    usuario.classList.remove("border-danger");
    senha.classList.remove("border-danger");
    termosDeUso.classList.remove("border-danger");  // Limpa a borda do checkbox

    var isValid = true;

    // Verifica se o campo de usuário está vazio
    if (usuario.value.trim() === "") {
        usuario.classList.add("border-danger");  // Adiciona borda vermelha
        isValid = false;
    }

    // Verifica se o campo de senha está vazio
    if (senha.value.trim() === "") {
        senha.classList.add("border-danger");  // Adiciona borda vermelha
        isValid = false;
    }

    // Verifica se o checkbox de termos de uso está marcado
    if (!termosDeUso.checked) {
        termosDeUso.classList.add("border-danger");  // Adiciona borda vermelha no checkbox
        isValid = false;
    }

    // Retorna verdadeiro se os campos estiverem preenchidos, falso caso contrário
    return isValid;
}
