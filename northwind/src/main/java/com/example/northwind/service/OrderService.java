package com.example.northwind.service;

import com.example.northwind.entity.Order;
import com.example.northwind.entity.OrderDetail;
import com.example.northwind.repository.OrderRepository;
import com.example.northwind.repository.OrderDetailRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;
import com.example.northwind.repository.spec.OrderSpecifications;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository repository;
	private final OrderDetailRepository detailRepository;

	public Page<Order> list(String customerId, Integer employeeId, java.time.LocalDate from, java.time.LocalDate to, Pageable pageable) {
		Specification<Order> spec = Specification.where(OrderSpecifications.customerId(customerId))
			.and(OrderSpecifications.employeeId(employeeId))
			.and(OrderSpecifications.dateFrom(from))
			.and(OrderSpecifications.dateTo(to));
		return repository.findAll(spec, pageable);
	}
	public Order get(Integer id) { return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found")); }

	@PreAuthorize("hasAnyRole('STAFF','ADMIN')")
	public Order create(Order o) { return repository.save(o); }
	@PreAuthorize("hasAnyRole('STAFF','ADMIN')")
	public Order update(Integer id, Order o) { Order existing = get(id); o.setId(existing.getId()); return repository.save(o); }
	@PreAuthorize("hasRole('ADMIN')")
	public void delete(Integer id) { repository.deleteById(id); }

	public BigDecimal computeTotal(Integer orderId) {
		List<OrderDetail> details = detailRepository.findByOrder_Id(orderId);
		return details.stream()
			.map(od -> od.getUnitPrice().multiply(BigDecimal.valueOf(od.getQuantity())).multiply(BigDecimal.valueOf(1 - od.getDiscount())))
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
