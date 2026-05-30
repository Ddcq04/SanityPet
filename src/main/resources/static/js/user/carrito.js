const STOCK_KEY = 'tienda_stock_decrementos';

function getDecrementos() {
	return JSON.parse(sessionStorage.getItem(STOCK_KEY) || '{}');
}
function guardarDecrementos(d) {
	sessionStorage.setItem(STOCK_KEY, JSON.stringify(d));
}

// Quitar (-1 unidad → restaurar 1 al stock visual)
document.querySelectorAll('form[action*="/quitar/"]').forEach(form => {
	form.addEventListener('submit', () => {
		const id = form.action.split('/quitar/')[1].split('?')[0];
		const d = getDecrementos();
		if (d[id]) { d[id] -= 1; if (d[id] <= 0) delete d[id]; guardarDecrementos(d); }
	});
});

// Eliminar (todas las unidades → restaurar cantidad al stock visual)
document.querySelectorAll('form[action*="/eliminar/"]').forEach(form => {
	form.addEventListener('submit', () => {
		const id = form.action.split('/eliminar/')[1].split('?')[0];
		const row = form.closest('tr');
		const cantidadSpan = row ? row.querySelector('.cantidad-controles span') : null;
		const cantidad = cantidadSpan ? parseInt(cantidadSpan.innerText) : 1;
		const d = getDecrementos();
		if (d[id]) { d[id] -= cantidad; if (d[id] <= 0) delete d[id]; guardarDecrementos(d); }
	});
});

// Agregar desde carrito (+1 → decrementar 1 más en stock visual)
document.querySelectorAll('form[action*="/agregar/"]').forEach(form => {
	form.addEventListener('submit', () => {
		const id = form.action.split('/agregar/')[1].split('?')[0];
		const d = getDecrementos();
		d[id] = (d[id] || 0) + 1;
		guardarDecrementos(d);
	});
});
