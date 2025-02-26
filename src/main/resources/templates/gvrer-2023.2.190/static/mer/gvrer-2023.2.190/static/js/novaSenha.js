const passwordField = document.getElementById('senha');
const togglePasswordIcon = document.getElementById('togglePassword');
const emailRadio = document.getElementById('emailRadio');
const celularRadio = document.getElementById('celularRadio');
const inputEmail = document.getElementById('inputEmail');
const inputCelular = document.getElementById('inputCelular');
const button = document.getElementById('button');

// Função para aplicar a máscara no número do celular
document.getElementById('inputCelular').addEventListener('input', function (e) {
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

// Alterar visibilidade dos campos e texto do botão com base na escolha do radio
emailRadio.addEventListener('change', function () {
  if (this.checked) {
    inputEmail.style.display = 'block';
    inputCelular.style.display = 'none';
    button.textContent = 'Enviar por E-mail';
  }
});

celularRadio.addEventListener('change', function () {
  if (this.checked) {
    inputCelular.style.display = 'block';
    inputEmail.style.display = 'none';
    button.textContent = 'Enviar por Celular';
  }
});

// Função de envio (apenas exibe um alerta neste exemplo)
function enviar() {
  if (emailRadio.checked || celularRadio.checked) {
    Swal.fire({
      title: "Sucesso!",
      text: "Instruções enviadas com sucesso!",
      icon: "success",
      confirmButtonColor: "#FF385C",
    });
  } else {
    Swal.fire({
      title: "Erro!",
      text: "Por favor, selecione uma opção de redefinição.",
      icon: "error",
      confirmButtonColor: "#FF385C",
    });
  }
}
