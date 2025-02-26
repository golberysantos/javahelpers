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