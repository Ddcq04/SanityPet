(function () {
    const nav = document.querySelector('header');
    let ultimoScroll = 0;

    /* Si la página pide nav siempre sólido (fondo blanco fijo) */
    const navSolido = document.body.dataset.navSolid === 'true';

    if (navSolido) {
        nav.classList.add('nav-con-fondo');
    } else {
        function actualizar() {
            const scrollActual = window.scrollY;

            if (scrollActual <= 10) {
                nav.classList.remove('nav-oculto');
                nav.classList.remove('nav-con-fondo');
            } else if (scrollActual > ultimoScroll) {
                nav.classList.add('nav-oculto');
            } else {
                nav.classList.remove('nav-oculto');
                nav.classList.add('nav-con-fondo');
            }

            ultimoScroll = scrollActual;
        }

        window.addEventListener('scroll', actualizar, { passive: true });
        actualizar();
    }

    /* Enlace activo según la URL actual — gana el match más largo */
    (function marcarActivo() {
        const ruta = window.location.pathname;

        /* Si estamos en el carrito, solo pintar el icono del carrito */
        if (ruta === '/tienda/carrito') {
            const cartLink = document.querySelector('.nav-cart-link');
            if (cartLink) cartLink.classList.add('activo');
            return;
        }

        function activarEnlaces(selector) {
            const enlaces = Array.from(document.querySelectorAll(selector));
            let mejorPath = '';

            enlaces.forEach(function (a) {
                try {
                    const path = new URL(a.href, window.location.origin).pathname;
                    const coincide = path === '/home'
                        ? ruta === '/home'
                        : ruta === path || ruta.startsWith(path + '/');
                    if (coincide && path.length > mejorPath.length) {
                        mejorPath = path;
                    }
                } catch (e) {}
            });

            if (mejorPath) {
                enlaces.forEach(function (a) {
                    try {
                        const path = new URL(a.href, window.location.origin).pathname;
                        if (path === mejorPath) a.classList.add('activo');
                    } catch (e) {}
                });
            }
        }

        activarEnlaces('.nav-enlace');
        activarEnlaces('.nav-movil-enlace');
    })();

    /* Menú hamburguesa */
    const hamburguesa = document.getElementById('navHamburguesa');
    const menuMovil = document.getElementById('navMovil');

    if (hamburguesa && menuMovil) {
        hamburguesa.addEventListener('click', function () {
            const abierto = menuMovil.classList.toggle('abierto');
            hamburguesa.classList.toggle('abierto', abierto);
        });

        menuMovil.querySelectorAll('.nav-movil-enlace').forEach(function (enlace) {
            enlace.addEventListener('click', function () {
                menuMovil.classList.remove('abierto');
                hamburguesa.classList.remove('abierto');
            });
        });

        document.addEventListener('click', function (e) {
            if (!nav.contains(e.target)) {
                menuMovil.classList.remove('abierto');
                hamburguesa.classList.remove('abierto');
            }
        });
    }
})();
