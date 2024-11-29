 function añadirAlCarrito(button) {
    const clienteId = document.getElementById('cliente-id').value;
    const comidaId = button.getAttribute('data-id');
    const nombre = button.getAttribute('data-nombre');
    const precio = parseFloat(button.getAttribute('data-precio'));

    const carritos = JSON.parse(localStorage.getItem('carritos')) || {};
    const carrito = carritos[clienteId] || [];

    if (carrito.find(item => item.id === comidaId)) {
        alert('El producto ya está en el carrito.');
        return;
    }

    carrito.push({ id: comidaId, nombre, precio });
    carritos[clienteId] = carrito;

    localStorage.setItem('carritos', JSON.stringify(carritos));
    alert('Producto añadido al carrito.');
    renderCarrito(clienteId);
}


    // Renderizar el carrito para el cliente autenticado
    function renderCarrito(clienteId) {
    const carritos = JSON.parse(localStorage.getItem('carritos')) || {};
    const carrito = carritos[clienteId] || [];
    const carritoContainer = document.getElementById('carrito-container');
    const totalElement = document.getElementById('total');

    carritoContainer.innerHTML = '';
    let total = 0;

    carrito.forEach((producto, index) => {
        total += producto.precio;

        const card = document.createElement('div');
        card.className = 'col-md-4 mb-4';
        card.innerHTML = `
            <div class="card h-100 shadow-sm">
                <div class="card-body text-center">
                    <h5 class="card-title">${producto.nombre}</h5>
                    <p><strong>Precio:</strong> $${producto.precio.toFixed(2)}</p>
                    <button class="btn btn-danger btn-block" data-index="${index}">Eliminar</button>
                </div>
            </div>
        `;

        card.querySelector('button').addEventListener('click', () => {
            eliminarProducto(clienteId, index);
        });

        carritoContainer.appendChild(card);
    });

    totalElement.textContent = total.toFixed(2);
}


    function eliminarProducto(clienteId, index) {
        const carritos = JSON.parse(localStorage.getItem('carritos')) || {};
        const carrito = carritos[clienteId] || [];
        carrito.splice(index, 1);
        carritos[clienteId] = carrito;
        localStorage.setItem('carritos', JSON.stringify(carritos));
        renderCarrito(clienteId);
    }

    document.addEventListener('DOMContentLoaded', () => {
        const clienteId = document.getElementById('cliente-id').value; // ID del cliente en sesión
        renderCarrito(clienteId);
    });