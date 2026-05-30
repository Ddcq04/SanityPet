const pass1 = document.getElementById('usuario.passwordPlana');
const pass2 = document.getElementById('usuario.passwordRepeat');
if (pass1 && pass2) {
	pass2.addEventListener('input', () => {
		pass2.setCustomValidity(pass1.value !== pass2.value ? 'Las contraseñas no coinciden' : '');
	});
}
