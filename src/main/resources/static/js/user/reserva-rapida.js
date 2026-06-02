
// Buscador de mascotas (autocomplete)

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
            sinResultado.className = 'opcion';
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
            item.className = 'opcion';
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


// Inicialización al cargar la página

window.addEventListener('DOMContentLoaded', () => {
    const idCitaInput = document.querySelector('input[name="id"]');
    const fechaInput = document.getElementById('fechaInput');
    
	var selectMascota = document.querySelector('[name="mascota"]');
	var inputBuscarMasc = document.getElementById('buscadorMascota');
	    if (selectMascota && inputBuscarMasc && selectMascota.selectedIndex !== -1) {
	        var opcionSeleccionada = selectMascota.options[selectMascota.selectedIndex];
	        if (opcionSeleccionada && opcionSeleccionada.value !== "") {
	            inputBuscarMasc.value = opcionSeleccionada.text; // Rellena el input visual con "Toby (Carlos Mendoza)"
	        }
	    }
    // Si estamos en edición y el campo de fecha no está deshabilitado (cita futura), cargamos horas
    if (idCitaInput && idCitaInput.value && fechaInput && fechaInput.value) {
        if (!fechaInput.disabled) {
            fechaInput.dispatchEvent(new Event('change'));
        }
    }
});

// Slider de imágenes de fondo
(function () {
    const slides = document.querySelectorAll('.fondo-imagen');
    if (!slides.length) return;
    let actual = 0;
    setInterval(function () {
        slides[actual].classList.remove('activa');
        actual = (actual + 1) % slides.length;
        slides[actual].classList.add('activa');
    }, 8000);
})();

// Reconstrucción de opciones para Custom Selects (Servicio y Horas)
function construirOpciones(wrapper) {
    const realSelect = wrapper.querySelector('select');
    const trigger = wrapper.querySelector('.selector-boton');
    const triggerText = trigger ? trigger.querySelector('span') : null;
    const lista = wrapper.querySelector('.selector-lista');
    
    if (!realSelect || !lista || !trigger) return; 
    
    lista.style.maxHeight = "240px";
    lista.style.overflowY = "auto";
    lista.innerHTML = '';
    
    Array.from(realSelect.options).forEach(function (opt) {
        const div = document.createElement('div');
        div.className = 'opcion' + (opt.selected ? ' seleccionado' : '');
        div.textContent = opt.text;
        div.dataset.value = opt.value;
        
        div.addEventListener('click', function (e) {
            e.stopPropagation();
            // Si el select real está deshabilitado (Cita pasada), no hacemos nada
            if (realSelect.disabled) return;
            
            realSelect.value = opt.value;
            if (triggerText) triggerText.textContent = opt.text;
            
            lista.querySelectorAll('.opcion').forEach(o => o.classList.remove('seleccionado'));
            div.classList.add('seleccionado');
            
            trigger.classList.remove('abierto');
            lista.classList.remove('abierto');
            
            // Disparar evento change manual por si otros componentes escuchan este select
            realSelect.dispatchEvent(new Event('change'));
        });
        lista.appendChild(div);
    });
    
    const sel = realSelect.options[realSelect.selectedIndex];
    if (sel && triggerText) triggerText.textContent = sel.text;
}

// Inicialización global de los selectores customizados
document.querySelectorAll('.selector-custom').forEach(function (wrapper) {
    if (wrapper.id === "mascotaWrapper") return; // El buscador de mascota va aparte
    
    const realSelect = wrapper.querySelector('select');
    const trigger = wrapper.querySelector('.selector-boton');
    const lista = wrapper.querySelector('.selector-lista');
    
    construirOpciones(wrapper);
    
    if (trigger && lista) {
        trigger.addEventListener('click', function (e) {
            e.stopPropagation();
            // 🌟 CLAVE: Si el campo está deshabilitado por Thymeleaf, impedimos abrir el desplegable
            if (realSelect && realSelect.disabled) return;
            
            // Cerrar cualquier otro select abierto primero
            document.querySelectorAll('.selector-boton, .selector-lista').forEach(el => {
                if (el !== trigger && el !== lista) el.classList.remove('abierto');
            });
            
            trigger.classList.toggle('abierto');
            lista.classList.toggle('abierto');
        });
    }
});

// Cerrar desplegables si se pincha fuera de ellos
document.addEventListener('click', function () {
    document.querySelectorAll('.selector-boton, .selector-lista').forEach(el => {
        el.classList.remove('abierto');
    });
});


// Horas disponibles

document.getElementById('fechaInput').addEventListener('change', function () {
    const fecha = this.value;
    const select = document.getElementById('horaSelect');
    const horaWrapper = document.getElementById('horaWrapper');

    if (!fecha || !select || !horaWrapper) return;

    // Validación de fines de semana
    const fechaSeleccionada = new Date(fecha);
    const diaSemana = fechaSeleccionada.getUTCDay();
    if (diaSemana === 0 || diaSemana === 6) {
        alert("La clínica solo atiende de lunes a viernes. Por favor, selecciona otro día.");
        this.value = "";
        select.innerHTML = '<option value="">— Elige día primero —</option>';
        construirOpciones(horaWrapper);
        return;
    }

    // Guardamos la hora que ya tenía la cita (por si es una edición en el mismo día)
    const horaPreseleccionada = select.getAttribute('data-hora-inicial') || select.value;

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
            
            // 🌟 SOLUCIÓN HORA EN EDICIÓN: Si estamos editando y el día coincide, volvemos a meter la hora actual de la cita
            if (horaPreseleccionada && horaPreseleccionada.startsWith(fecha)) {
                const existe = Array.from(select.options).some(opt => opt.value === horaPreseleccionada);
                if (!existe) {
                    const textoHora = horaPreseleccionada.split('T')[1].substring(0, 5);
                    // Insertamos la opción actual reservada al principio para que no desaparezca
                    const optActual = new Option(textoHora, horaPreseleccionada);
                    select.add(optActual, 1);
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


// Envío del formulario

document.getElementById('citaForm').addEventListener('submit', function (e) {
    e.preventDefault();
    
    const esEdicion = this.getAttribute('data-es-edicion') === 'true';

    fetch(this.action, { method: 'POST', body: new FormData(this) })
        .then(async res => {
            const data = await res.json();
            if (res.ok) {
                if (esEdicion) {
                    alert("¡Cita modificada correctamente!");
                } else {
                    alert("¡Cita reservada con éxito!");
                }
                window.location.href = '/citas';
            } else {
                alert('⚠️ Error: ' + data.error);
            }
        })
        .catch(() => alert('❌ Error de conexión'));
});


// Sincronización buscador de mascotas
(function() {
    function conectarBuscadorMascota() {
        const buscador = document.getElementById('mascotaBuscador');
        const datalist = document.getElementById('datalistMascotas');
        const hiddenId = document.getElementById('mascotaIdHidden');

        if (!buscador || !datalist || !hiddenId) return;

        if (buscador.value !== "") {
            const opciones = datalist.options;
            for (let i = 0; i < opciones.length; i++) {
                if (opciones[i].value === buscador.value) {
                    hiddenId.value = opciones[i].getAttribute('data-id');
                    break;
                }
            }
        }

        buscador.addEventListener('input', function(e) {
            const valorActual = e.target.value;
            const opciones = datalist.options;
            
            hiddenId.value = ""; 

            for (let i = 0; i < opciones.length; i++) {
                if (opciones[i].value === valorActual) {
                    hiddenId.value = opciones[i].getAttribute('data-id');
                    break;
                }
            }
        });

		// Seleccionar el texto al hacer foco en lugar de destruirlo al instante
		buscador.addEventListener('focus', function(e) {
		    if (!e.target.disabled) {
		        e.target.select(); 
		    }
		});
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', conectarBuscadorMascota);
    } else {
        conectarBuscadorMascota();
    }
})();