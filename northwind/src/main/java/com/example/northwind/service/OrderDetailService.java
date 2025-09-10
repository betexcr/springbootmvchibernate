package com.example.northwind.service;

import com.example.northwind.entity.Order;
import com.example.northwind.entity.OrderDetail;
import com.example.northwind.entity.OrderDetailId;
import com.example.northwind.repository.OrderDetailRepository;
import com.example.northwind.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService {
	private final OrderDetailRepository detailRepo;
	private final OrderRepository orderRepo;

	public List<OrderDetail> list(Integer orderId) { return detailRepo.findByOrder_Id(orderId); }
	public Page<OrderDetail> list(Integer orderId, Pageable pageable) { return detailRepo.findByOrder_Id(orderId, pageable); }
	public OrderDetail get(Integer orderId, Integer productId) {
		OrderDetailId id = new OrderDetailId(); id.setOrderId(orderId); id.setProductId(productId);
		return detailRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Order detail not found"));
	}
	@PreAuthorize("hasAnyRole('STAFF','ADMIN')")
	public OrderDetail create(Integer orderId, OrderDetail d) {
		Order order = orderRepo.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));
		d.setOrder(order);
		OrderDetailId id = new OrderDetailId(); id.setOrderId(orderId); id.setProductId(d.getProduct().getId());
		d.setId(id);
		return detailRepo.save(d);
	}
	@PreAuthorize("hasAnyRole('STAFF','ADMIN')")
	public OrderDetail update(Integer orderId, Integer productId, OrderDetail d) {
		OrderDetail existing = get(orderId, productId);
		d.setOrder(existing.getOrder());
		d.setId(existing.getId());
		return detailRepo.save(d);
	}
	@PreAuthorize("hasRole('ADMIN')")
	public void delete(Integer orderId, Integer productId) {
		OrderDetailId id = new OrderDetailId(); id.setOrderId(orderId); id.setProductId(productId);
		detailRepo.deleteById(id);
	}

	public BigDecimal summaryTotal(Integer orderId) {
		return list(orderId).stream()
			.map(od -> od.getUnitPrice().multiply(BigDecimal.valueOf(od.getQuantity())).multiply(BigDecimal.valueOf(1 - od.getDiscount())))
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public BigDecimal sumTotalByCustomerAndDate(String customerId, java.time.LocalDate from, java.time.LocalDate to) {
		return detailRepo.sumTotalByCustomerAndDate(customerId, from, to);
	}
}
