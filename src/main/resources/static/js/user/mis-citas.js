(function () {
	const slides = document.querySelectorAll('.hero-slide');
	const dots = document.querySelectorAll('.hero-dot');
	let actual = 0;

	function irA(i) {
		slides[actual].classList.remove('activa');
		dots[actual].classList.remove('activa');
		actual = i;
		slides[actual].classList.add('activa');
		dots[actual].classList.add('activa');
	}

	dots.forEach(function (dot, i) {
		dot.addEventListener('click', function () { irA(i); });
	});

	setInterval(function () {
		irA((actual + 1) % slides.length);
	}, 8000);
})();
