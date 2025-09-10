package com.example.northwind.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "employees")
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "employee_id")
	private Integer id;

	@Column(name = "last_name", length = 20, nullable = false)
	private String lastName;

	@Column(name = "first_name", length = 10, nullable = false)
	private String firstName;

	@Column(name = "title", length = 30)
	private String title;

	@Column(name = "birth_date")
	private LocalDate birthDate;

	@OneToMany(mappedBy = "employee")
	private Set<Order> orders = new LinkedHashSet<>();
}
