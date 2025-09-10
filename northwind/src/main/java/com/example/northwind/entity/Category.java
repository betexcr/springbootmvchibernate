package com.example.northwind.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Integer id;

	@Column(name = "category_name", nullable = false, length = 15)
	private String name;

	@Column(name = "description")
	private String description;

	@OneToMany(mappedBy = "category")
	private Set<Product> products = new LinkedHashSet<>();
}
