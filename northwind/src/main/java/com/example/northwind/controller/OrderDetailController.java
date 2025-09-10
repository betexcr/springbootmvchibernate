package com.example.northwind.controller;

import com.example.northwind.dto.OrderDetailCreateDto;
import com.example.northwind.entity.OrderDetail;
import com.example.northwind.entity.Product;
import com.example.northwind.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/orders/{orderId}/details")
@RequiredArgsConstructor
public class OrderDetailController {
	private final OrderDetailService service;

	@GetMapping public Page<OrderDetail> list(@PathVariable(name = "orderId") Integer orderId, Pageable pageable){return service.list(orderId, pageable);}    
	@GetMapping("/{productId}") public OrderDetail get(@PathVariable(name = "orderId") Integer orderId,@PathVariable(name = "productId") Integer productId){return service.get(orderId,productId);}    
	@PostMapping @ResponseStatus(HttpStatus.CREATED) public OrderDetail create(@PathVariable(name = "orderId") Integer orderId,@RequestBody OrderDetailCreateDto dto){
		OrderDetail d = new OrderDetail();
		var p = new Product(); p.setId(dto.productId); d.setProduct(p);
		d.setUnitPrice(dto.unitPrice); d.setQuantity(dto.quantity); d.setDiscount(dto.discount);
		return service.create(orderId,d);
	}    
	@PutMapping("/{productId}") public OrderDetail update(@PathVariable(name = "orderId") Integer orderId,@PathVariable(name = "productId") Integer productId,@RequestBody OrderDetailCreateDto dto){
		OrderDetail d = new OrderDetail();
		var p = new Product(); p.setId(dto.productId); d.setProduct(p);
		d.setUnitPrice(dto.unitPrice); d.setQuantity(dto.quantity); d.setDiscount(dto.discount);
		return service.update(orderId,productId,d);
	}    
	@DeleteMapping("/{productId}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable(name = "orderId") Integer orderId,@PathVariable(name = "productId") Integer productId){service.delete(orderId,productId);}    

	@GetMapping("/summary/total") public BigDecimal total(@PathVariable(name = "orderId") Integer orderId){return service.summaryTotal(orderId);}    
}
