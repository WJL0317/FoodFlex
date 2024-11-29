document.addEventListener("DOMContentLoaded", () => {
	inicializarCantidades();
	calcularTotal();
});

function inicializarCantidades() {
	const cantidades = JSON.parse(localStorage.getItem("cantidadesCarrito")) || {};

	document.querySelectorAll(".cantidad").forEach((input) => {
		const comidaId = input.id.split("-")[1]; // Obtener el ID específico del producto
		if (cantidades[comidaId]) {
			input.value = cantidades[comidaId]; // Establecer la cantidad específica del producto
		}
	});
}

function guardarCantidades(comidaId, cantidad) {
	const cantidades = JSON.parse(localStorage.getItem("cantidadesCarrito")) || {};
	cantidades[comidaId] = cantidad; // Guardar solo la cantidad asociada al producto
	localStorage.setItem("cantidadesCarrito", JSON.stringify(cantidades));
}

function calcularTotal() {
	let total = 0;

	// Iterar sobre todas las tarjetas de productos y sumar sus precios
	document.querySelectorAll(".card").forEach((item) => {
		const precioElement = item.querySelector(".precio");
		if (precioElement) {
			const precio = parseFloat(precioElement.textContent.replace(',', '.').trim()) || 0;
			total += precio;
		}
	});

	// Asegurarse de que el valor total sea válido y tenga solo un punto decimal
	const totalElement = document.getElementById("total");
	if (totalElement) {
		totalElement.textContent = total.toFixed(2);
	}
}

function onCantidadChange(input, comidaId) {
	const cantidad = parseInt(input.value);
	if (cantidad > 0) {
		guardarCantidades(comidaId, cantidad); // Guardar la cantidad para este producto

		// Enviar actualización al servidor
		fetch("/carrito/actualizar-cantidad", {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
			},
			body: JSON.stringify({ comidaId, cantidad }),
		})
			.then((response) => response.json())
			.then((data) => {
				if (data.success) {
					calcularTotal(); // Recalcular el total después de actualizar
				} else {
					alert("Error al actualizar la cantidad: " + data.message);
				}
			})
			.catch((error) => {
				console.error("Error:", error);
				alert("Hubo un error al actualizar la cantidad.");
			});
	} else {
		alert("La cantidad debe ser mayor a 0");
		input.value = 1;
	}
}

function eliminarProducto(button) {
    const comidaId = button.getAttribute("data-id");

    console.log("Comida ID enviado:", comidaId); // Debug para verificar qué se está enviando

    fetch("/carrito/eliminar", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ comidaId }), // Envía el objeto esperado
    })
        .then((response) => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error("No se pudo eliminar el producto del carrito.");
            }
        })
        .then((data) => {
            if (data.success) {
                const productoCard = button.closest(".col-md-4");
                if (productoCard) {
                    productoCard.remove();
                }
                calcularTotal(); // Recalcular el total después de eliminar
                verificarCarritoVacio(); // Verificar si el carrito quedó vacío
            } else {
                alert(data.message || "Error al eliminar el producto.");
            }
        })
        .catch((error) => {
            console.error("Error al eliminar el producto:", error);
            alert("Hubo un error al procesar la solicitud.");
        });
}


function eliminarCantidadLocal(comidaId) {
	const cantidades = JSON.parse(localStorage.getItem("cantidadesCarrito")) || {};
	delete cantidades[comidaId]; // Eliminar solo la cantidad específica del producto
	localStorage.setItem("cantidadesCarrito", JSON.stringify(cantidades));
}

function verificarCarritoVacio() {
	const productos = document.querySelectorAll(".col-md-4");
	if (productos.length === 0) {
		document.querySelector(".text-center").innerHTML = `
		            <h4>Tu carrito está vacío</h4>
		            <a href="/catalogo" class="btn btn-outline-primary mt-3">Ver Catálogo</a>
		        `;
	}
}
document.addEventListener("DOMContentLoaded", () => {
    const totalElement = document.getElementById("total");
    const totalInput = document.getElementById("totalPedido");

    if (totalElement && totalInput) {
        totalInput.value = totalElement.textContent.trim();
    }
});


