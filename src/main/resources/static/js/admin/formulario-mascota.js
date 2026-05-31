// Slider
(function () {
	const slides = document.querySelectorAll('.fm-slide');
	if (slides.length) {
		let actual = 0;
		setInterval(function () {
			slides[actual].classList.remove('activa');
			actual = (actual + 1) % slides.length;
			slides[actual].classList.add('activa');
		}, 8000);
	}
})();

// Custom selects (Especie, etc.)
document.querySelectorAll('.fm-custom-select').forEach(function (wrapper) {
	const realSelect = wrapper.querySelector('select');
	const trigger = wrapper.querySelector('.fm-select-trigger');
	const triggerText = trigger ? trigger.querySelector('span') : null;
	const lista = wrapper.querySelector('.fm-select-lista');

	if (!realSelect || !trigger || !lista) return;

	// Construir opciones
	Array.from(realSelect.options).forEach(function (opt) {
		const div = document.createElement('div');
		div.className = 'fm-option' + (opt.selected ? ' seleccionado' : '');
		div.textContent = opt.text;
		div.dataset.value = opt.value;
		div.addEventListener('click', function () {
			realSelect.value = opt.value;
			if (triggerText) triggerText.textContent = opt.text;
			lista.querySelectorAll('.fm-option').forEach(o => o.classList.remove('seleccionado'));
			div.classList.add('seleccionado');
			trigger.classList.remove('abierto');
			lista.classList.remove('abierto');
			
			realSelect.dispatchEvent(new Event('change'));
		});
		lista.appendChild(div);
	});

	// Texto inicial
	const seleccionado = realSelect.options[realSelect.selectedIndex];
	if (seleccionado && triggerText) triggerText.textContent = seleccionado.text;

	// Abrir / cerrar
	trigger.addEventListener('click', function (e) {
		e.stopPropagation();
		trigger.classList.toggle('abierto');
		lista.classList.toggle('abierto');
	});
});

// Cerrar al hacer clic fuera de los custom selects
document.addEventListener('click', function () {
	document.querySelectorAll('.fm-select-trigger, .fm-select-lista').forEach(el => {
		el.classList.remove('abierto');
	});
});

// ============================================================
// 🌟 NUEVO: SINCRONIZACIÓN DEL BUSCADOR DE CLIENTES (ADMIN)
// ============================================================
(function() {
    function conectarBuscadorCliente() {
        const buscador = document.getElementById('clienteBuscador');
        const datalist = document.getElementById('datalistClientes');
        const hiddenId = document.getElementById('clienteIdHidden');

        if (!buscador || !datalist || !hiddenId) return;

        // Si ya viene cargado con un cliente (Modo Edición), sincronizamos el ID oculto
        if (buscador.value !== "") {
            const opciones = datalist.options;
            for (let i = 0; i < opciones.length; i++) {
                if (opciones[i].value === buscador.value) {
                    hiddenId.value = opciones[i].getAttribute('data-id');
                    break;
                }
            }
        }

        // Detectar selección o escritura en el buscador inteligente
        buscador.addEventListener('input', function(e) {
            const valorActual = e.target.value;
            const opciones = datalist.options;
            
            hiddenId.value = ""; // Reseteamos por seguridad

            for (let i = 0; i < opciones.length; i++) {
                if (opciones[i].value === valorActual) {
                    hiddenId.value = opciones[i].getAttribute('data-id');
                    break;
                }
            }
        });

        // Limpiar el campo al hacer clic para facilitar que vuelvan a escribir
        buscador.addEventListener('focus', function(e) {
            if (!e.target.disabled) {
                e.target.value = '';
                hiddenId.value = ''; 
            }
        });
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', conectarBuscadorCliente);
    } else {
        conectarBuscadorCliente();
    }
})();
