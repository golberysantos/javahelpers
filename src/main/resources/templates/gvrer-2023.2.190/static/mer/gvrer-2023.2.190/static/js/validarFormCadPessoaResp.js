function validar() {
    // Toggle password visibility
    document.getElementById("togglePassword")?.addEventListener("click", function () {
        var passwordField = document.getElementById("senha");
        var type = passwordField.type === "password" ? "text" : "password";
        passwordField.type = type;
    });

    // Função para validar campos
    var usuario = document.getElementById("nome");
    var registro = document.getElementById("cpf");
    var celular = document.getElementById("telefone");
    var sexo = document.querySelector('input[name="genero"]:checked'); // Verifica se algum rádio foi selecionado

    // Limpar as bordas de erro antes de validar novamente
    usuario.classList.remove("border-danger");
    registro.classList.remove("border-danger");
    celular.classList.remove("border-danger");
    sexo?.classList.remove("border-danger"); // Limpa a borda do sexo se houver

    // Limpar mensagens de erro
    limparMensagensDeErro();

    var isValid = true;

    // Verifica se o campo de nome está vazio
    if (usuario.value.trim() === "") {
        usuario.classList.add("border-danger");  // Adiciona borda vermelha
        isValid = false;
    }
    if (registro.value.trim() === "") {
        registro.classList.add("border-danger");  // Adiciona borda vermelha
        isValid = false;
    }
    if (celular.value.trim() === "") {
        celular.classList.add("border-danger");  // Adiciona borda vermelha
        isValid = false;
    }

    // Validação do sexo
    if (!sexo) { // Se não há nenhum sexo selecionado
        var sexoRadios = document.querySelectorAll('input[name="genero"]');
        sexoRadios.forEach(radio => radio.classList.add("border-danger"));  // Adiciona borda vermelha nos botões de sexo
        mostrarMensagemDeErro("sexo", "Por favor, selecione uma opção de sexo.");
        isValid = false;
    }

    // Preenche automaticamente CPF e celular
    preencherCPF(registro);
    preencherCelular(celular);

    // Validação do CPF (11 dígitos)
    if (!validarCPF(registro.value)) {
        registro.classList.add("border-danger");
        mostrarMensagemDeErro("cpf", "CPF inválido. Certifique-se de que está preenchido corretamente.");
        isValid = false;
    }

    // Validação do celular (11 dígitos)
    if (!validarCelular(celular.value)) {
        celular.classList.add("border-danger");
        mostrarMensagemDeErro("telefone", "Número de celular inválido. Certifique-se de que está preenchido corretamente.");
        isValid = false;
    }

    // Retorna verdadeiro se os campos estiverem preenchidos e válidos, falso caso contrário
    return isValid;
}

// Função para preencher o CPF automaticamente
function preencherCPF(campoCPF) {
    let valor = campoCPF.value.replace(/\D/g, ''); // Remove tudo que não for número
    if (valor.length <= 11) {
        valor = valor.replace(/(\d{3})(\d)/, "$1.$2");
        valor = valor.replace(/(\d{3})(\d)/, "$1.$2");
        valor = valor.replace(/(\d{3})(\d{1,2})$/, "$1-$2");
    }
    campoCPF.value = valor;
}

// Função para preencher o número de celular automaticamente
function preencherCelular(campoCelular) {
    let valor = campoCelular.value.replace(/\D/g, ''); // Remove tudo que não for número
    if (valor.length <= 11) {
        valor = valor.replace(/^(\d{2})(\d{5})(\d{4})$/, "($1) $2-$3");
    }
    campoCelular.value = valor;
}

// Função para validar o CPF (verifica se tem exatamente 11 números)
function validarCPF(cpf) {
    cpf = cpf.replace(/\D/g, ''); // Remove qualquer caractere não numérico
    return cpf.length === 11; // CPF deve ter 11 dígitos
}

// Função para validar o celular (verifica se tem exatamente 11 números)
function validarCelular(celular) {
    celular = celular.replace(/\D/g, ''); // Remove qualquer caractere não numérico
    return celular.length === 11; // Celular deve ter 11 dígitos
}

// Função para mostrar mensagens de erro abaixo dos campos
function mostrarMensagemDeErro(campo, mensagem) {
    var elemento = document.getElementById(campo);
    var mensagemErro = document.createElement("small");
    mensagemErro.style.color = "red";
    mensagemErro.textContent = mensagem;
    
    // Verifica se já existe uma mensagem de erro e remove antes de adicionar uma nova
    var mensagensExistentes = elemento.parentElement.querySelectorAll("small");
    mensagensExistentes.forEach(mensagem => mensagem.remove());

    // Adiciona a mensagem de erro
    elemento.parentElement.appendChild(mensagemErro);
}

// Função para limpar mensagens de erro
function limparMensagensDeErro() {
    var mensagens = document.querySelectorAll("small");
    mensagens.forEach(mensagem => mensagem.remove());
}
