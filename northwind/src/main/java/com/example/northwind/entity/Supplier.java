package com.example.northwind.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "suppliers")
public class Supplier {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "supplier_id")
	private Integer id;

	@Column(name = "company_name", nullable = false, length = 40)
	private String companyName;

	@Column(name = "contact_name", length = 30)
	private String contactName;

	@Column(name = "contact_title", length = 30)
	private String contactTitle;

	@Column(name = "country", length = 15)
	private String country;

	@OneToMany(mappedBy = "supplier")
	@JsonIgnore
	private Set<Product> products = new LinkedHashSet<>();
}
