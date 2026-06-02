// ─────────────────────────────────────────────────────────
//  BUSCADOR DE MASCOTAS (autocomplete)
// ─────────────────────────────────────────────────────────

// 1) Agarramos los tres elementos que ya existen en el HTML
var selectMascota    = document.querySelector('[name="mascota"]');   // el <select> oculto del backend
var inputBuscarMasc  = document.getElementById('buscadorMascota');   // el input donde se escribe
var listasugerMasc   = document.getElementById('buscadorListaMascota'); // el div donde aparecen las sugerencias

// Solo ejecutamos si los tres elementos existen en la página
if (selectMascota && inputBuscarMasc && listasugerMasc) {

    // 2) Sacamos todas las opciones del <select> en un array
    //    Quitamos la opción vacía "— Selecciona —"
    var todasLasMascotas = Array.from(selectMascota.options).filter(function(opcion) {
        return opcion.value !== '';
    });

    // 3) Función que filtra y muestra las sugerencias
    function mostrarSugerenciasMascota(textoBuscado) {

        // Limpiamos la lista antes de volver a pintar
        listasugerMasc.innerHTML = '';

        // Si el input está vacío, cerramos la lista
        if (textoBuscado.trim() === '') {
            listasugerMasc.classList.remove('abierto');
            return;
        }

        // El texto de cada opción tiene el formato: "Toby (Carlos Mendoza)"
        // Buscamos por el nombre de la mascota que es la parte antes del paréntesis
        var busqueda = textoBuscado.toLowerCase();

        var coincidencias = todasLasMascotas.filter(function(opcion) {
            var nombreMascota = opcion.text.split(' (')[0]; // "Toby"
            // Comprobamos si el nombre EMPIEZA por lo que escribió el usuario
            return nombreMascota.toLowerCase().startsWith(busqueda);
        });

        // Si no hay coincidencias mostramos un mensaje
        if (coincidencias.length === 0) {
            var sinResultado = document.createElement('div');
            sinResultado.className = 'rc-option';
            sinResultado.textContent = 'No se encontró ninguna mascota';
            sinResultado.style.opacity = '0.5';
            sinResultado.style.cursor = 'default';
            listasugerMasc.appendChild(sinResultado);
            listasugerMasc.classList.add('abierto');
            return;
        }

        // Por cada coincidencia creamos un div clicable
        coincidencias.forEach(function(opcion) {
            var item = document.createElement('div');
            item.className = 'rc-option';
            item.textContent = opcion.text;

            // Al hacer clic seleccionamos esa mascota
            item.addEventListener('click', function() {
                selectMascota.value = opcion.value;   // guardamos el id en el select oculto
                inputBuscarMasc.value = opcion.text;  // mostramos el nombre en el input
                listasugerMasc.innerHTML = '';
                listasugerMasc.classList.remove('abierto');
            });

            listasugerMasc.appendChild(item);
        });

        listasugerMasc.classList.add('abierto');
    }

    // 4) Cada vez que el usuario escribe, filtramos
    inputBuscarMasc.addEventListener('input', function() {
        mostrarSugerenciasMascota(inputBuscarMasc.value);
    });

    // 5) Al hacer clic fuera, cerramos la lista
    document.addEventListener('click', function(evento) {
        var clickFuera = !inputBuscarMasc.contains(evento.target) && !listasugerMasc.contains(evento.target);
        if (clickFuera) {
            listasugerMasc.innerHTML = '';
            listasugerMasc.classList.remove('abierto');
        }
    });
}

// ─────────────────────────────────────────────────────────

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
