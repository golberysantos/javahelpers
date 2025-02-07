/*const usuarioBd = "gabriel@gmail";
const senhaBd = "123";
var usuario;
var senha;
function validar() {
    usuario = document.getElementById('user').value;
    senha = document.getElementById('senha').value;
    console.log("Valor do campo usuário: " + usuario);
    console.log("Valor do campo senha: " + senha);
    verificar();
}

function verificar() {
    if (usuarioBd == usuario && senhaBd == senha) {
        console.log("Sucesso!!");
        window.location.href = "principal.html";
    } else {
        alert("Erro!! Usuário ou senha incorreto.");
        console.log("Erro!! Usuário ou senha incorreto.");
    }
}*/
// Código para alternar a visibilidade da senha
const passwordField = document.getElementById('senha');
const togglePasswordIcon = document.getElementById('togglePassword');

togglePasswordIcon.addEventListener('click', function () {
  // Alterna o tipo da senha entre 'password' e 'text'
  const type = passwordField.type === 'password' ? 'text' : 'password';
  passwordField.type = type;

  // Alterna o ícone entre 'fa-eye' (olho aberto) e 'fa-eye-slash' (olho fechado)
  this.classList.toggle('fa-eye');
  this.classList.toggle('fa-eye-slash');
});