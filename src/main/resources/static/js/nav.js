(function () {
    var nav = document.querySelector('header');
    var ultimoScroll = 0;

    // Si la página pide nav siempre sólida
    var navSolida = document.body.dataset.navSolid === 'true';

    if (navSolida) {
        nav.classList.add('nav-solida');
    } else {
        // Actualiza la nav según el scroll
        function actualizar() {
            var scrollActual = window.scrollY;

            if (scrollActual <= 10) {
                // Arriba del todo: nav transparente
                nav.classList.remove('nav-oculta');
                nav.classList.remove('nav-solida');
            } else if (scrollActual > ultimoScroll) {
                // Bajando: ocultar nav
                nav.classList.add('nav-oculta');
            } else {
                // Subiendo: mostrar nav con fondo
                nav.classList.remove('nav-oculta');
                nav.classList.add('nav-solida');
            }

            ultimoScroll = scrollActual;
        }

        window.addEventListener('scroll', actualizar, { passive: true });
        actualizar();
    }

    // Marca el enlace activo según la URL
    (function marcarActivo() {
        var ruta = window.location.pathname;

        // Si estamos en el carrito, marcamos el icono del carrito
        if (ruta === '/tienda/carrito') {
            var enlaceCarrito = document.querySelector('.enlace-carrito');
            if (enlaceCarrito) enlaceCarrito.classList.add('activo');
            return;
        }

        function activarEnlaces(selector) {
            var enlaces = Array.from(document.querySelectorAll(selector));
            var mejorRuta = '';

            enlaces.forEach(function (a) {
                try {
                    var path = new URL(a.href, window.location.origin).pathname;
                    var coincide = path === '/home'
                        ? ruta === '/home'
                        : ruta === path || ruta.startsWith(path + '/');
                    if (coincide && path.length > mejorRuta.length) {
                        mejorRuta = path;
                    }
                } catch (e) {}
            });

            if (mejorRuta) {
                enlaces.forEach(function (a) {
                    try {
                        var path = new URL(a.href, window.location.origin).pathname;
                        if (path === mejorRuta) a.classList.add('activo');
                    } catch (e) {}
                });
            }
        }

        activarEnlaces('.nav-enlace');
        activarEnlaces('.enlace-menu');
    })();

    // Menú hamburguesa
    var botonMenu = document.getElementById('navHamburguesa');
    var menuMovil = document.getElementById('navMovil');

    if (botonMenu && menuMovil) {
        // Abrir o cerrar el menú al hacer clic
        botonMenu.addEventListener('click', function () {
            var abierto = menuMovil.classList.toggle('abierto');
            botonMenu.classList.toggle('abierto', abierto);
        });

        // Cerrar el menú al hacer clic en un enlace
        menuMovil.querySelectorAll('.enlace-menu').forEach(function (enlace) {
            enlace.addEventListener('click', function () {
                menuMovil.classList.remove('abierto');
                botonMenu.classList.remove('abierto');
            });
        });

        // Cerrar el menú al hacer clic fuera
        document.addEventListener('click', function (e) {
            if (!nav.contains(e.target)) {
                menuMovil.classList.remove('abierto');
                botonMenu.classList.remove('abierto');
            }
        });
    }
})();
