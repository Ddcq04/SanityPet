// Slider
(function () {
	const slides = document.querySelectorAll('.fm-slide');
	let actual = 0;
	setInterval(function () {
		slides[actual].classList.remove('activa');
		actual = (actual + 1) % slides.length;
		slides[actual].classList.add('activa');
	}, 8000);
})();

// Custom selects
document.querySelectorAll('.fm-custom-select').forEach(function (wrapper) {
	const realSelect = wrapper.querySelector('select');
	const trigger = wrapper.querySelector('.fm-select-trigger');
	const triggerText = trigger.querySelector('span');
	const lista = wrapper.querySelector('.fm-select-lista');

	// Construir opciones
	Array.from(realSelect.options).forEach(function (opt) {
		const div = document.createElement('div');
		div.className = 'fm-option' + (opt.selected ? ' seleccionado' : '');
		div.textContent = opt.text;
		div.dataset.value = opt.value;
		div.addEventListener('click', function () {
			realSelect.value = opt.value;
			triggerText.textContent = opt.text;
			lista.querySelectorAll('.fm-option').forEach(o => o.classList.remove('seleccionado'));
			div.classList.add('seleccionado');
			trigger.classList.remove('abierto');
			lista.classList.remove('abierto');
		});
		lista.appendChild(div);
	});

	// Texto inicial
	const seleccionado = realSelect.options[realSelect.selectedIndex];
	if (seleccionado) triggerText.textContent = seleccionado.text;

	// Abrir / cerrar
	trigger.addEventListener('click', function (e) {
		e.stopPropagation();
		trigger.classList.toggle('abierto');
		lista.classList.toggle('abierto');
	});

	// Cerrar al hacer clic fuera
	document.addEventListener('click', function () {
		trigger.classList.remove('abierto');
		lista.classList.remove('abierto');
	});
});
