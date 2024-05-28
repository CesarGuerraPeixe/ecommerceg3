package br.org.serratec.ecommerce.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedido")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idPedido", scope = Pedido.class)
public class Pedido {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_pedido")
	private Long idPedido;

	@Column(name = "data_pedido")
	private Date dataPedido = new Date();

	@Column(name = "data_envio")
	private Date dataEnvio;

	@Column(name = "data_entrega")
	private Date dataEntrega;

	@Column(name = "status")
	private String status;

	@Column(name = "valor_total")
	private BigDecimal valorTotal;

	@OneToMany(mappedBy = "pedido")
	private List<ItemPedido> itensPedidos;

	@ManyToOne
	@JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente", nullable = false)
	private Cliente cliente;

	public Pedido() {
	}

	public void gerarValorTotal(Pedido pedido) {
		BigDecimal valorTotal = new BigDecimal(0);
		if (pedido.getItensPedidos() != null) {
			for (ItemPedido itemPedido : pedido.getItensPedidos()) {
				valorTotal = valorTotal.add(itemPedido.getValorLiquido());
			}
			System.out.println("\n\n\n\n" + valorTotal + "\n\n\n\n\n\n");
		} else {
			System.out.println("\n\n\n\nitem pedido e nulo\n\n\n\n\n");
		}
		pedido.setValorTotal(valorTotal);
	}

	public Long getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(Long idPedido) {
		this.idPedido = idPedido;
	}

	public Date getDataPedido() {
		return dataPedido;
	}

	public void setDataPedido(Date dataPedido) {
		this.dataPedido = dataPedido;
	}

	public Date getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	public Date getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(Date dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public List<ItemPedido> getItensPedidos() {
		return itensPedidos;
	}

	public void setItensPedidos(List<ItemPedido> itensPedidos) {
		this.itensPedidos = itensPedidos;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}
