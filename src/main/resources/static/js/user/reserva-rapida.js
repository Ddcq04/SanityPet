// Inicialización automática al cargar la página en caso de EDICIÓN
window.addEventListener('DOMContentLoaded', () => {
	const idCitaInput = document.querySelector('input[name="id"]');
	if (idCitaInput && idCitaInput.value) {
		const fechaInput = document.getElementById('fechaInput');
		if (fechaInput.value && !fechaInput.disabled) {
			fechaInput.dispatchEvent(new Event('change'));
		}
	}
});

// Slider de imágenes de fondo
(function () {
	const slides = document.querySelectorAll('.rc-slide');
	let actual = 0;
	setInterval(function () {
		slides[actual].classList.remove('activa');
		actual = (actual + 1) % slides.length;
		slides[actual].classList.add('activa');
	}, 8000);
})();

// Custom selects
function construirOpciones(wrapper) {
	const realSelect = wrapper.querySelector('select');
	const trigger = wrapper.querySelector('.rc-select-trigger');
	const triggerText = trigger.querySelector('span');
	const lista = wrapper.querySelector('.rc-select-lista');
	lista.innerHTML = '';
	Array.from(realSelect.options).forEach(function (opt) {
		const div = document.createElement('div');
		div.className = 'rc-option' + (opt.selected ? ' seleccionado' : '');
		div.textContent = opt.text;
		div.dataset.value = opt.value;
		div.addEventListener('click', function () {
			realSelect.value = opt.value;
			triggerText.textContent = opt.text;
			lista.querySelectorAll('.rc-option').forEach(o => o.classList.remove('seleccionado'));
			div.classList.add('seleccionado');
			trigger.classList.remove('abierto');
			lista.classList.remove('abierto');
		});
		lista.appendChild(div);
	});
	const sel = realSelect.options[realSelect.selectedIndex];
	if (sel) triggerText.textContent = sel.text;
}

document.querySelectorAll('.rc-custom-select').forEach(function (wrapper) {
	const trigger = wrapper.querySelector('.rc-select-trigger');
	const lista = wrapper.querySelector('.rc-select-lista');
	construirOpciones(wrapper);
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

// Horas disponibles + validación fin de semana
document.getElementById('fechaInput').addEventListener('change', function () {
	const fecha = this.value;
	const select = document.getElementById('horaSelect');
	const horaWrapper = document.getElementById('horaWrapper');

	if (!fecha) return;

	const fechaSeleccionada = new Date(fecha);
	const diaSemana = fechaSeleccionada.getUTCDay();
	if (diaSemana === 0 || diaSemana === 6) {
		alert("La clínica solo atiende de lunes a viernes. Por favor, selecciona otro día.");
		this.value = "";
		select.innerHTML = '<option value="">— Elige día primero —</option>';
		construirOpciones(horaWrapper);
		return;
	}

	const horaPreseleccionada = select.value;

	select.innerHTML = '<option value="">Cargando...</option>';
	construirOpciones(horaWrapper);

	fetch('/citas/horas-disponibles?fecha=' + fecha)
		.then(res => res.json())
		.then(horas => {
			select.innerHTML = '<option value="">— Selecciona hora —</option>';
			horas.forEach(h => {
				const valorOption = fecha + 'T' + h + ':00';
				select.add(new Option(h, valorOption));
			});
			if (horaPreseleccionada && horaPreseleccionada.startsWith(fecha)) {
				const existe = Array.from(select.options).some(opt => opt.value === horaPreseleccionada);
				if (!existe) {
					const textoHora = horaPreseleccionada.split('T')[1].substring(0, 5);
					select.add(new Option(textoHora, horaPreseleccionada));
				}
				select.value = horaPreseleccionada;
			}
			construirOpciones(horaWrapper);
		})
		.catch(() => {
			select.innerHTML = '<option value="">— Error al cargar horas —</option>';
			construirOpciones(horaWrapper);
		});
});

// Envío por fetch
document.getElementById('citaForm').addEventListener('submit', function (e) {
	e.preventDefault();
	fetch(this.action, { method: 'POST', body: new FormData(this) })
		.then(async res => {
			const data = await res.json();
			if (res.ok) {
				alert(data.mensaje);
				window.location.href = '/citas';
			} else {
				alert('⚠️ Error: ' + data.error);
			}
		})
		.catch(() => alert('❌ Error de conexión'));
});
