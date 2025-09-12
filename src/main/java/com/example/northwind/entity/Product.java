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

	@Column(name = "category_id", insertable = false, updatable = false)
	private Integer categoryId;

	@Column(name = "quantity_per_unit", length = 20)
	private String quantityPerUnit;

	@Column(name = "unit_price")
	private Float unitPrice;

	@Column(name = "units_in_stock")
	private Short unitsInStock;

	@Column(name = "units_on_order")
	private Short unitsOnOrder;

	@Column(name = "reorder_level")
	private Short reorderLevel;

	@Column(name = "discontinued", nullable = false)
	private Boolean discontinued;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "variants", columnDefinition = "TEXT")
	private String variants; // JSON string for color options, sizes, etc.

	@Column(name = "care_instructions", columnDefinition = "TEXT")
	private String careInstructions;

	@Column(name = "detailed_description", columnDefinition = "TEXT")
	private String detailedDescription;

	@Column(name = "image_url", length = 500)
	private String imageUrl;
}
