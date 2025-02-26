document.getElementById('telefone').addEventListener('input', function (e) {
    let telefone = e.target.value;

    // Remove todos os caracteres não numéricos
    telefone = telefone.replace(/\D/g, '');

    // Aplica a máscara
    if (telefone.length <= 2) {
        telefone = telefone.replace(/^(\d{0,2})/, '($1');
    } else if (telefone.length <= 7) {
        telefone = telefone.replace(/^(\d{2})(\d{0,5})/, '($1) $2');
    } else {
        telefone = telefone.replace(/^(\d{2})(\d{5})(\d{0,4})/, '($1) $2-$3');
    }

    // Limita o número de caracteres a 15 (DDD + 9 dígitos + hífen)
    e.target.value = telefone.substring(0, 15);
});