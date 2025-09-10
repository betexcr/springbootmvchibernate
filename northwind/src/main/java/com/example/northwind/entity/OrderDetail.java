package com.example.northwind.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "order_details")
public class OrderDetail {
	@EmbeddedId
	private OrderDetailId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("orderId")
	@JoinColumn(name = "order_id")
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("productId")
	@JoinColumn(name = "product_id")
	private Product product;

	@Column(name = "unit_price", nullable = false)
	private BigDecimal unitPrice;

	@Column(name = "quantity", nullable = false)
	private Short quantity;

	@Column(name = "discount", nullable = false)
	private Float discount;
}
