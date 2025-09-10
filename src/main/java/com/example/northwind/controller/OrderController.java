package com.example.northwind.controller;

import com.example.northwind.dto.OrderCreateDto;
import com.example.northwind.dto.OrderDetailItemDto;
import com.example.northwind.dto.OrderSummaryDto;
import com.example.northwind.entity.Order;
import com.example.northwind.entity.OrderDetail;
import com.example.northwind.service.OrderDetailService;
import com.example.northwind.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
	private final OrderService service;
	private final OrderDetailService detailService;
	@GetMapping public Page<Order> list(@RequestParam(name = "customerId", required = false) String customerId,
										@RequestParam(name = "employeeId", required = false) Integer employeeId,
										@RequestParam(name = "from", required = false) java.time.LocalDate from,
										@RequestParam(name = "to", required = false) java.time.LocalDate to,
										Pageable p){return service.list(customerId, employeeId, from, to, p);}    
	@GetMapping("/{id}") public Order get(@PathVariable("id") Integer id){return service.get(id);}    
	@PostMapping @ResponseStatus(HttpStatus.CREATED) public Order create(@RequestBody OrderCreateDto dto){
		Order o = new Order();
		if (dto.customerId != null) { var c = new com.example.northwind.entity.Customer(); c.setId(dto.customerId); o.setCustomer(c);}    
		if (dto.employeeId != null) { var e = new com.example.northwind.entity.Employee(); e.setId(dto.employeeId); o.setEmployee(e);}    
		o.setOrderDate(dto.orderDate); o.setRequiredDate(dto.requiredDate); o.setShippedDate(dto.shippedDate);
		return service.create(o);
	}    
	@PutMapping("/{id}") public Order update(@PathVariable Integer id,@RequestBody OrderCreateDto dto){
		Order o = new Order();
		if (dto.customerId != null) { var c = new com.example.northwind.entity.Customer(); c.setId(dto.customerId); o.setCustomer(c);}    
		if (dto.employeeId != null) { var e = new com.example.northwind.entity.Employee(); e.setId(dto.employeeId); o.setEmployee(e);}    
		o.setOrderDate(dto.orderDate); o.setRequiredDate(dto.requiredDate); o.setShippedDate(dto.shippedDate);
		return service.update(id,o);
	}    

	@GetMapping("/{id}/summary")
	public OrderSummaryDto summary(@PathVariable("id") Integer id) {
		Order order = service.get(id);
		var details = detailService.list(id);
		OrderSummaryDto dto = new OrderSummaryDto();
		dto.orderId = order.getId();
		dto.customerId = order.getCustomer() != null ? order.getCustomer().getId() : null;
		dto.employeeId = order.getEmployee() != null ? order.getEmployee().getId() : null;
		dto.orderDate = order.getOrderDate();
		dto.items = details.stream().map(this::mapItem).collect(Collectors.toList());
		dto.total = service.computeTotal(id);
		return dto;
	}

	private OrderDetailItemDto mapItem(OrderDetail d) {
		OrderDetailItemDto item = new OrderDetailItemDto();
		item.productId = d.getProduct() != null ? d.getProduct().getId() : null;
		item.productName = d.getProduct() != null ? d.getProduct().getName() : null;
		item.unitPrice = d.getUnitPrice();
		item.quantity = d.getQuantity();
		item.discount = d.getDiscount();
		return item;
	}

	@GetMapping("/summary/total")
	public BigDecimal totalByFilters(@RequestParam(name = "customerId", required = false) String customerId,
	                                 @RequestParam(name = "from", required = false) java.time.LocalDate from,
	                                 @RequestParam(name = "to", required = false) java.time.LocalDate to) {
		return detailService.sumTotalByCustomerAndDate(customerId, from, to);
	}
}
