(function () {
    const nav = document.querySelector('header');
    const tienePrincipal = document.querySelector('.principal');

    function actualizarNav() {
        if (tienePrincipal) {
            nav.classList.toggle('nav-con-fondo', window.scrollY > 20);
        } else {
            nav.classList.add('nav-con-fondo');
        }
    }

    window.addEventListener('scroll', actualizarNav, { passive: true });
    actualizarNav();
})();
