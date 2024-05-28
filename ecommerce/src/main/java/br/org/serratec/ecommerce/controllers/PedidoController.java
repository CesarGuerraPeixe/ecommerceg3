package br.org.serratec.ecommerce.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.serratec.ecommerce.dtos.RelatorioPedidoDTO;
import br.org.serratec.ecommerce.entities.Pedido;
import br.org.serratec.ecommerce.services.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

	@Autowired
	PedidoService pedidoService;

	@GetMapping
	public ResponseEntity<List<RelatorioPedidoDTO>> findAll() {
		return new ResponseEntity<>(pedidoService.findAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Pedido> findById(@PathVariable Long id) {
		Pedido pedido = pedidoService.findById(id);
		if (pedido == null) {
			return new ResponseEntity<>(pedido, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(pedido, HttpStatus.OK);
		}
	}

	@GetMapping("/relatorio/{id}")
	public ResponseEntity<RelatorioPedidoDTO> gerarRelatorioPedidoDTO(@PathVariable Long id) {
		Pedido pedido = pedidoService.findById(id);
		return new ResponseEntity<>(pedidoService.gerarRelatorioDTO(pedido), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<RelatorioPedidoDTO> save(@RequestBody Pedido pedido) {
		return new ResponseEntity<>(pedidoService.save(pedido), HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<Pedido> update(@RequestBody Pedido pedido) {
		return new ResponseEntity<>(pedidoService.update(pedido), HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<String> delete(@RequestBody Pedido pedido) {
		if (pedidoService.delete(pedido))
			return new ResponseEntity<>("Deletado com sucesso.", HttpStatus.OK);
		else
			return new ResponseEntity<>("Não foi possível deletar.", HttpStatus.BAD_REQUEST);
	}

}