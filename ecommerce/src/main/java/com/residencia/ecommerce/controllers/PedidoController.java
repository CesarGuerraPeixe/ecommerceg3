package com.residencia.ecommerce.controllers;

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

import com.residencia.ecommerce.dto.RelatorioPedidoDTO;
import com.residencia.ecommerce.entities.Pedido;
import com.residencia.ecommerce.services.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
	
	@Autowired
	PedidoService pedidoService;
	
	@GetMapping
	public ResponseEntity<List<RelatorioPedidoDTO>> listarPedidos() {
		return new ResponseEntity<>(pedidoService.listarPedidos(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Pedido> buscarPedidoPorId(@PathVariable Long id) {
		Pedido pedido = pedidoService.getPedidoPorId(id);
		if(pedido == null) {
			return new ResponseEntity<>(pedido, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(pedido, HttpStatus.OK);
		}	
	}
	
	@GetMapping("/relatorio/{id}")
	public ResponseEntity<RelatorioPedidoDTO> gerarRelatorioPedidoDTO(@PathVariable Long id) {
		Pedido pedido = pedidoService.getPedidoPorId(id);
		return new ResponseEntity<>(pedidoService.gerarRelatorioDTO(pedido), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<RelatorioPedidoDTO> salvar(@RequestBody Pedido pedido) {
		return new ResponseEntity<>(pedidoService.salvarPedido(pedido), HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<Pedido> atualizar(@RequestBody Pedido pedido) {
		return new ResponseEntity<>(pedidoService.atualizarPedido(pedido), HttpStatus.OK);
	}
	
	@DeleteMapping
	public ResponseEntity<String> deletarPedido(@RequestBody Pedido pedido) {
		if (pedidoService.deletarPedido(pedido))
			return new ResponseEntity<>("Deletado com sucesso.", HttpStatus.OK);
			else
				return new ResponseEntity<>("Não foi possível deletar.", HttpStatus.BAD_REQUEST);
	}

}
