// Slider
(function () {
	const slides = document.querySelectorAll('.fp-slide');
	let actual = 0;
	setInterval(function () {
		slides[actual].classList.remove('activa');
		actual = (actual + 1) % slides.length;
		slides[actual].classList.add('activa');
	}, 8000);
})();

// Custom select categoría
document.querySelectorAll('.fp-custom-select').forEach(function (wrapper) {
	const realSelect = wrapper.querySelector('select');
	const trigger = wrapper.querySelector('.fp-select-trigger');
	const triggerText = trigger.querySelector('span');
	const lista = wrapper.querySelector('.fp-select-lista');

	Array.from(realSelect.options).forEach(function (opt) {
		const div = document.createElement('div');
		div.className = 'fp-option' + (opt.selected ? ' seleccionado' : '');
		div.textContent = opt.text;
		div.dataset.value = opt.value;
		div.addEventListener('click', function () {
			realSelect.value = opt.value;
			triggerText.textContent = opt.text;
			lista.querySelectorAll('.fp-option').forEach(o => o.classList.remove('seleccionado'));
			div.classList.add('seleccionado');
			trigger.classList.remove('abierto');
			lista.classList.remove('abierto');
		});
		lista.appendChild(div);
	});

	const seleccionado = realSelect.options[realSelect.selectedIndex];
	if (seleccionado) triggerText.textContent = seleccionado.text;

	trigger.addEventListener('click', function (e) {
		e.stopPropagation();
		trigger.classList.toggle('abierto');
		lista.classList.toggle('abierto');
	});

	document.addEventListener('click', function () {
		trigger.classList.remove('abierto');
		lista.classList.remove('abierto');
	});
});
