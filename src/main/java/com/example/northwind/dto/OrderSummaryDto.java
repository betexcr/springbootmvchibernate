package com.example.northwind.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OrderSummaryDto {
	public Integer orderId;
	public String customerId;
	public Integer employeeId;
	public LocalDate orderDate;
	public List<OrderDetailItemDto> items;
	public BigDecimal total;
}
