// ─────────────────────────────────────────────────────────
//  BUSCADOR DE CLIENTES (autocomplete)
// ─────────────────────────────────────────────────────────

// 1) Agarramos los elementos que ya existen en el HTML
var selectReal    = document.querySelector('[name="cliente"]');   // el <select> oculto del backend
var inputBuscar   = document.getElementById('buscadorCliente');   // el input donde el admin escribe
var listaSugeren  = document.getElementById('buscadorLista');     // el div donde aparecen las sugerencias

// Solo ejecutamos esto si los tres elementos existen en la página
if (selectReal && inputBuscar && listaSugeren) {

    // 2) Convertimos las opciones del <select> en un array normal para trabajar con ellas
    //    Cada opción tiene: .value (el id del cliente) y .text (el nombre que se muestra)
    var todosLosClientes = Array.from(selectReal.options).filter(function(opcion) {
        return opcion.value !== '';  // quitamos la opción vacía "— Seleccionar cliente —"
    });

    // 3) Función que muestra las sugerencias según lo que escribió el admin
    function mostrarSugerencias(textoBuscado) {

        // Limpiamos la lista antes de volver a pintar
        listaSugeren.innerHTML = '';

        // Si el input está vacío, no mostramos nada
        if (textoBuscado.trim() === '') {
            listaSugeren.classList.remove('abierto');
            return;
        }

        // Separamos el texto de cada opción en dos partes: el DNI y el nombre
        // El formato que viene del backend es: "12345678A - Erik García"
        // Entonces partimos por " - " y nos quedamos con cada trozo
        var coincidencias = todosLosClientes.filter(function(opcion) {
            var partes     = opcion.text.split(' - ');   // ["12345678A", "Erik García"]
            var dni        = partes[0] || '';             // "12345678A"
            var nombre     = partes[1] || '';             // "Erik García"
            var busqueda   = textoBuscado.toLowerCase();

            // Comprobamos si el DNI o el nombre EMPIEZA por lo que escribió el admin
            // startsWith() devuelve true solo si el texto comienza con esa cadena
            var coincideDni    = dni.toLowerCase().startsWith(busqueda);
            var coincideNombre = nombre.toLowerCase().startsWith(busqueda);

            return coincideDni || coincideNombre;
        });

        // Si no hay ninguna coincidencia, mostramos un mensaje
        if (coincidencias.length === 0) {
            var sinResultado = document.createElement('div');
            sinResultado.className = 'fm-option';
            sinResultado.textContent = 'No se encontró ningún cliente';
            sinResultado.style.opacity = '0.5';
            sinResultado.style.cursor = 'default';
            listaSugeren.appendChild(sinResultado);
            listaSugeren.classList.add('abierto');
            return;
        }

        // Por cada coincidencia creamos un elemento div clicable
        coincidencias.forEach(function(opcion) {
            var item = document.createElement('div');
            item.className = 'fm-option';
            item.textContent = opcion.text;

            // Cuando el admin hace clic en una sugerencia:
            item.addEventListener('click', function() {
                // Ponemos el valor en el <select> real (esto es lo que se envía al backend)
                selectReal.value = opcion.value;

                // Mostramos el nombre elegido en el input
                inputBuscar.value = opcion.text;

                // Cerramos la lista de sugerencias
                listaSugeren.innerHTML = '';
                listaSugeren.classList.remove('abierto');
            });

            listaSugeren.appendChild(item);
        });

        // Abrimos la lista para que sea visible
        listaSugeren.classList.add('abierto');
    }

    // 4) Cada vez que el admin escribe algo, llamamos a mostrarSugerencias
    inputBuscar.addEventListener('input', function() {
        mostrarSugerencias(inputBuscar.value);
    });

    // 5) Si el admin hace clic fuera del buscador, cerramos la lista
    document.addEventListener('click', function(evento) {
        var clickFuera = !inputBuscar.contains(evento.target) && !listaSugeren.contains(evento.target);
        if (clickFuera) {
            listaSugeren.innerHTML = '';
            listaSugeren.classList.remove('abierto');
        }
    });
}

// ─────────────────────────────────────────────────────────

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
// SINCRONIZACIÓN DEL BUSCADOR DE CLIENTES 
// ============================================================
document.addEventListener('DOMContentLoaded', function() {
    var selectReal  = document.querySelector('[name="cliente"]');   // el select oculto
    var inputBuscar = document.getElementById('buscadorCliente');   // el input visual

    // Si ambos existen y el select real tiene un cliente ya preseleccionado por el backend
    if (selectReal && inputBuscar && selectReal.selectedIndex !== -1) {
        var opcionSeleccionada = selectReal.options[selectReal.selectedIndex];
        
        // Verificamos que no sea la opción vacía por defecto
        if (opcionSeleccionada && opcionSeleccionada.value !== "") {
            // Rellenamos el input visual con el formato: "12345678A - Nombre Dueño"
            inputBuscar.value = opcionSeleccionada.text;
        }
    }

    // Seleccionar todo el texto al hacer clic en el buscador para facilitar la edición
    if (inputBuscar) {
        inputBuscar.addEventListener('focus', function(e) {
            e.target.select();
        });
    }
});
