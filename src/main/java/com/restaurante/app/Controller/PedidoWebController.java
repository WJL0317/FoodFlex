package com.restaurante.app.Controller;

import com.restaurante.app.Entity.Carrito;
import com.restaurante.app.Entity.Comida;
import com.restaurante.app.Entity.Pedido;
import com.restaurante.app.Entity.Restaurante;
import com.restaurante.app.Entity.Usuario;
import com.restaurante.app.Repository.CarritoRepository;
import com.restaurante.app.Repository.PedidoRepository;
import com.restaurante.app.Repository.RestauranteRepository;
import com.restaurante.app.Repository.ComidaRepository;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/pedidos")
public class PedidoWebController {

	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private CarritoRepository carritoRepository;
	@Autowired
	private RestauranteRepository restauranteRepository;
	@Autowired
	private ComidaRepository comidaRepository;

	// Página de pago del cliente
	@GetMapping("/pagoCliente")
	public ModelAndView mostrarPaginaPago(@RequestParam(required = false) String clienteId,
			@RequestParam(required = false) String nombreCliente,
			@RequestParam(required = false, defaultValue = "0") double total) {
		if (clienteId == null || nombreCliente == null || total <= 0) {
			return new ModelAndView("error").addObject("message", "Datos inválidos para procesar el pago.");
		}

		ModelAndView mav = new ModelAndView("pagoCliente");
		mav.addObject("clienteId", clienteId);
		mav.addObject("nombreCliente", nombreCliente);
		mav.addObject("total", total);
		return mav;
	}

	// Procesar pago y asignar pedidos a restaurantes
	@PostMapping("/procesar")
	public ModelAndView procesarPago(@RequestParam String clienteId, @RequestParam String nombreCliente,
			@RequestParam double total) {
		Optional<Carrito> carritoOptional = carritoRepository.findByClienteId(clienteId);
		if (carritoOptional.isEmpty()) {
			return new ModelAndView("error").addObject("message", "El carrito está vacío o no existe.");
		}

		Carrito carrito = carritoOptional.get();
		List<String> comidasIds = carrito.getPedido();
		if (comidasIds == null || comidasIds.isEmpty()) {
			return new ModelAndView("error").addObject("message", "El carrito no contiene comidas.");
		}

		Map<String, Pedido> pedidosPorRestaurante = new HashMap<>();

		for (String comidaId : comidasIds) {
			Optional<Comida> comidaOptional = comidaRepository.findById(comidaId);
			if (comidaOptional.isPresent()) {
				Comida comida = comidaOptional.get();
				String restauranteNombre = comida.getRestauranteNombre();
				String restauranteId = obtenerIdRestaurantePorNombre(restauranteNombre);

				Pedido pedido = pedidosPorRestaurante.getOrDefault(restauranteNombre, new Pedido());
				pedido.setClienteId(clienteId);
				pedido.setNombreCliente(nombreCliente);
				pedido.setRestauranteId(restauranteId);
				pedido.setNombreRestaurante(restauranteNombre);
				pedido.setComidaId(pedido.getComidaId() == null ? comidaId : pedido.getComidaId() + ", " + comidaId);
				pedido.setNombreComida(pedido.getNombreComida() == null ? comida.getNombre()
						: pedido.getNombreComida() + ", " + comida.getNombre());
				pedido.setTotal(pedido.getTotal() + comida.getPrecio());
				pedido.setEstado("PENDIENTE");
				pedido.setFechaPedido(new Date());

				pedidosPorRestaurante.put(restauranteNombre, pedido);
			}
		}

		// Guardar los pedidos en la base de datos
		for (Pedido pedido : pedidosPorRestaurante.values()) {
			pedidoRepository.save(pedido);
		}

		carrito.setPedido(new ArrayList<>());
		carritoRepository.save(carrito);

		ModelAndView mav = new ModelAndView("pagoExitoso");
		mav.addObject("pedidos", pedidosPorRestaurante.values());
		return mav;
	}

	// Método auxiliar para obtener el ID del restaurante por nombre
	private String obtenerIdRestaurantePorNombre(String restauranteNombre) {
		Optional<Restaurante> restauranteOptional = restauranteRepository.findByNombre(restauranteNombre);
		return restauranteOptional.map(Restaurante::getId).orElse(null);
	}

	// Gestión de pedidos para administradores
	@GetMapping("/pedidosAdmin")
	public String gestionarPedidosAdmin(Model model) {
		List<Pedido> pedidos = pedidoRepository.findAll();
		model.addAttribute("pedidos", pedidos);
		return "pedidosAdmin";
	}

	// Actualizar estado de pedidos para administradores
	@PostMapping("/pedidosAdmin/actualizarEstado")
	public String actualizarEstadoAdmin(@RequestParam String pedidoId, @RequestParam String nuevoEstado,
			RedirectAttributes redirectAttributes) {
		Optional<Pedido> pedidoOptional = pedidoRepository.findById(pedidoId);
		if (pedidoOptional.isEmpty()) {
			redirectAttributes.addFlashAttribute("mensaje", "Pedido no encontrado.");
			return "redirect:/pedidos/pedidosAdmin";
		}

		Pedido pedido = pedidoOptional.get();
		pedido.setEstado(nuevoEstado);
		pedidoRepository.save(pedido);

		redirectAttributes.addFlashAttribute("mensaje", "Estado del pedido actualizado correctamente.");
		return "redirect:/pedidos/pedidosAdmin";
	}

	// Gestión de pedidos para administradores
		@GetMapping("/pedidosRestaurante")
		public String gestionarPedidosRestaurante(Model model) {
			List<Pedido> pedidos = pedidoRepository.findAll();
			model.addAttribute("pedidos", pedidos);
			return "pedidosRestaurante";
		}
	
		// Actualizar estado de pedidos para administradores
		@PostMapping("/pedidosRestaurante/actualizarEstado")
		public String actualizarEstadoRestaurante(@RequestParam String pedidoId, @RequestParam String nuevoEstado,
				RedirectAttributes redirectAttributes) {
			Optional<Pedido> pedidoOptional = pedidoRepository.findById(pedidoId);
			if (pedidoOptional.isEmpty()) {
				redirectAttributes.addFlashAttribute("mensaje", "Pedido no encontrado.");
				return "redirect:/pedidos/pedidosRestaurante";
			}

			Pedido pedido = pedidoOptional.get();
			pedido.setEstado(nuevoEstado);
			pedidoRepository.save(pedido);

			redirectAttributes.addFlashAttribute("mensaje", "Estado del pedido actualizado correctamente.");
			return "redirect:/pedidos/pedidosAdmin";
		}
	

	// Listado de pedidos para clientes
	@GetMapping("/pedidosClienteListado")
	public ModelAndView listarPedidosCliente(HttpSession session) {
		Object usuario = session.getAttribute("usuario");
		if (usuario == null) {
			return new ModelAndView("redirect:/login");
		}

		String clienteId = ((Usuario) usuario).getId();
		List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteId);

		ModelAndView mav = new ModelAndView("pedidosClienteListado");
		mav.addObject("pedidos", pedidos);
		return mav;
	}

	// Actualizar estado de pedidos genérico
	@PostMapping("/actualizarEstado/{id}")
	public ResponseEntity<String> actualizarEstado(@PathVariable String id, @RequestParam String estado) {
		Pedido pedido = pedidoRepository.findById(id).orElse(null);
		if (pedido == null) {
			return ResponseEntity.badRequest().body("Pedido no encontrado");
		}

		pedido.setEstado(estado);
		pedidoRepository.save(pedido);
		return ResponseEntity.ok("Estado actualizado a: " + estado);
	}
}
