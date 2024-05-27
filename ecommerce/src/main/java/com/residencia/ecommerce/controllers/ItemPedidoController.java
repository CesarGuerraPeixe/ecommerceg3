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

import com.residencia.ecommerce.entities.ItemPedido;
import com.residencia.ecommerce.services.ItemPedidoService;

@RestController
@RequestMapping("/itens")
public class ItemPedidoController {

	@Autowired
	ItemPedidoService itemPedidoService;

	@GetMapping
	public ResponseEntity<List<ItemPedido>> listarItemPedidos() {
		return new ResponseEntity<>(itemPedidoService.listarItemPedidos(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ItemPedido> buscarItemPedidoPorId(@PathVariable Long id) {
		ItemPedido itemPedido = itemPedidoService.getItemPedidoPorId(id);
		return new ResponseEntity<>(itemPedido, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ItemPedido> salvar(@RequestBody ItemPedido itemPedido) {
		return new ResponseEntity<>(itemPedidoService.salvarItemPedido(itemPedido), HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<ItemPedido> atualizar(@RequestBody ItemPedido itemPedido) {
		return new ResponseEntity<>(itemPedidoService.atualizarItemPedido(itemPedido), HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<String> deletarItemPedido(@RequestBody ItemPedido itemPedido) {
		if (itemPedidoService.deletarItemPedido(itemPedido))
			return new ResponseEntity<>("Deletado com sucesso.", HttpStatus.OK);
		else
			return new ResponseEntity<>("Não foi possível deletar.", HttpStatus.BAD_REQUEST);
	}

}
