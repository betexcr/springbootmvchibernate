package com.example.northwind.repository.spec;

import com.example.northwind.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecifications {
	public static Specification<Product> nameContains(String q) {
		return (root, cq, cb) -> q == null ? cb.conjunction() : cb.like(cb.lower(root.get("name")), "%" + q.toLowerCase() + "%");
	}
	public static Specification<Product> categoryId(Integer id) {
		return (root, cq, cb) -> id == null ? cb.conjunction() : cb.equal(root.get("category").get("id"), id);
	}
	public static Specification<Product> supplierId(Integer id) {
		return (root, cq, cb) -> id == null ? cb.conjunction() : cb.equal(root.get("supplier").get("id"), id);
	}
	public static Specification<Product> priceGte(BigDecimal min) {
		return (root, cq, cb) -> min == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("unitPrice"), min);
	}
	public static Specification<Product> priceLte(BigDecimal max) {
		return (root, cq, cb) -> max == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("unitPrice"), max);
	}
	public static Specification<Product> discontinued(Boolean d) {
		return (root, cq, cb) -> d == null ? cb.conjunction() : cb.equal(root.get("discontinued"), d);
	}
}
