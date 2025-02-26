document.getElementById('cpf').addEventListener('input', function (e) {
    let cpf = e.target.value;

    // Remove todos os caracteres não numéricos
    cpf = cpf.replace(/\D/g, '');

    // Aplica a máscara
    if (cpf.length <= 3) {
        cpf = cpf.replace(/^(\d{0,3})/, '$1');
    } else if (cpf.length <= 6) {
        cpf = cpf.replace(/^(\d{3})(\d{0,3})/, '$1.$2');
    } else if (cpf.length <= 9) {
        cpf = cpf.replace(/^(\d{3})(\d{3})(\d{0,3})/, '$1.$2.$3');
    } else {
        cpf = cpf.replace(/^(\d{3})(\d{3})(\d{3})(\d{0,2})/, '$1.$2.$3-$4');
    }

    // Limita o número de caracteres a 14 (11 dígitos + 3 caracteres de formatação)
    e.target.value = cpf.substring(0, 14);
});