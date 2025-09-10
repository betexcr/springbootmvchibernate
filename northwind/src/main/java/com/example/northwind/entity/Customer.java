package com.example.northwind.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "customers")
public class Customer {
	@Id
	@Column(name = "customer_id", length = 5)
	private String id;

	@Column(name = "company_name", nullable = false, length = 40)
	private String companyName;

	@Column(name = "contact_name", length = 30)
	private String contactName;

	@Column(name = "contact_title", length = 30)
	private String contactTitle;

	@Column(name = "country", length = 15)
	private String country;

	@OneToMany(mappedBy = "customer")
	private Set<Order> orders = new LinkedHashSet<>();
}
