package com.example.northwind.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class OrderDetailId implements Serializable {
	@Column(name = "order_id")
	private Integer orderId;
	@Column(name = "product_id")
	private Integer productId;
}
