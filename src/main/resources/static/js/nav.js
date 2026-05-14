(function () {
    const nav = document.querySelector('header');
    const tienePrincipal = document.querySelector('.principal');
    let ultimoScroll = 0;

    if (!tienePrincipal) {
        nav.classList.add('nav-con-fondo');
    } else {
        function actualizar() {
            const scrollActual = window.scrollY;
            const alturaHero = tienePrincipal.offsetHeight;

            if (scrollActual <= 10) {
                /* Arriba del todo: transparente, siempre visible */
                nav.classList.remove('nav-oculto');
                nav.classList.remove('nav-con-fondo');
            } else if (scrollActual < alturaHero) {
                /* Dentro del hero: transparente, siempre visible */
                nav.classList.remove('nav-oculto');
                nav.classList.remove('nav-con-fondo');
            } else if (scrollActual > ultimoScroll) {
                /* Bajando fuera del hero: ocultar */
                nav.classList.add('nav-oculto');
            } else {
                /* Subiendo fuera del hero: mostrar con fondo */
                nav.classList.remove('nav-oculto');
                nav.classList.add('nav-con-fondo');
            }

            ultimoScroll = scrollActual;
        }

        window.addEventListener('scroll', actualizar, { passive: true });
        actualizar();
    }

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
