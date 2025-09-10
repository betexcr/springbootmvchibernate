package com.example.northwind.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Integer id;

	@Column(name = "product_name", nullable = false, length = 40)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "supplier_id")
	@JsonIgnore
	private Supplier supplier;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	@JsonIgnore
	private Category category;

	@Column(name = "quantity_per_unit", length = 20)
	private String quantityPerUnit;

	@Column(name = "unit_price")
	private java.math.BigDecimal unitPrice;

	@Column(name = "units_in_stock")
	private Short unitsInStock;

	@Column(name = "units_on_order")
	private Short unitsOnOrder;

	@Column(name = "reorder_level")
	private Short reorderLevel;

	@Column(name = "discontinued", nullable = false)
	private Boolean discontinued;
}
