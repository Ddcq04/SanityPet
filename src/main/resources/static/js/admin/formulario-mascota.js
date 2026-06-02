// Buscador de clientes (autocomplete)

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
            sinResultado.className = 'opcion';
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
            item.className = 'opcion';
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

// Slider
(function () {
	const slides = document.querySelectorAll('.fondo-imagen');
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
document.querySelectorAll('.selector-custom').forEach(function (wrapper) {
	const realSelect = wrapper.querySelector('select');
	const trigger = wrapper.querySelector('.selector-boton');
	const triggerText = trigger ? trigger.querySelector('span') : null;
	const lista = wrapper.querySelector('.selector-lista');

	if (!realSelect || !trigger || !lista) return;

	// Construir opciones
	Array.from(realSelect.options).forEach(function (opt) {
		const div = document.createElement('div');
		div.className = 'opcion' + (opt.selected ? ' seleccionado' : '');
		div.textContent = opt.text;
		div.dataset.value = opt.value;
		div.addEventListener('click', function () {
			realSelect.value = opt.value;
			if (triggerText) triggerText.textContent = opt.text;
			lista.querySelectorAll('.opcion').forEach(o => o.classList.remove('seleccionado'));
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
	document.querySelectorAll('.selector-boton, .selector-lista').forEach(el => {
		el.classList.remove('abierto');
	});
});

// Sincronización buscador de clientes (admin)
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
		        e.target.select(); 
		    }
		});
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', conectarBuscadorCliente);
    } else {
        conectarBuscadorCliente();
    }
})();
