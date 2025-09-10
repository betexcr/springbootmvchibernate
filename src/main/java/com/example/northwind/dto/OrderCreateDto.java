package com.example.northwind.dto;

import java.time.LocalDate;

public class OrderCreateDto {
	public String customerId;
	public Integer employeeId;
	public LocalDate orderDate;
	public LocalDate requiredDate;
	public LocalDate shippedDate;
}
