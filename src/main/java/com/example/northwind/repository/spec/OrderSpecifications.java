package com.example.northwind.repository.spec;

import com.example.northwind.entity.Order;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class OrderSpecifications {
	public static Specification<Order> customerId(String id){
		return (root,cq,cb) -> id==null? cb.conjunction() : cb.equal(root.get("customer").get("id"), id);
	}
	public static Specification<Order> employeeId(Integer id){
		return (root,cq,cb) -> id==null? cb.conjunction() : cb.equal(root.get("employee").get("id"), id);
	}
	public static Specification<Order> dateFrom(LocalDate from){
		return (root,cq,cb) -> from==null? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("orderDate"), from);
	}
	public static Specification<Order> dateTo(LocalDate to){
		return (root,cq,cb) -> to==null? cb.conjunction() : cb.lessThanOrEqualTo(root.get("orderDate"), to);
	}
}
