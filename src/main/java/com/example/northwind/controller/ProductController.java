package com.example.northwind.controller;

import com.example.northwind.entity.Product;
import com.example.northwind.entity.ProductReview;
import com.example.northwind.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
	private final ProductService productService;

	@GetMapping
	public Page<Product> list(@RequestParam(name = "q", required = false) String q,
							  @RequestParam(name = "categoryId", required = false) Integer categoryId,
							  @RequestParam(name = "supplierId", required = false) Integer supplierId,
							  @RequestParam(name = "minPrice", required = false) java.math.BigDecimal minPrice,
							  @RequestParam(name = "maxPrice", required = false) java.math.BigDecimal maxPrice,
							  @RequestParam(name = "discontinued", required = false) Boolean discontinued,
							  Pageable pageable) {
		return productService.list(q, categoryId, supplierId, minPrice, maxPrice, discontinued, pageable);
	}

	@GetMapping("/{id}")
	public Product get(@PathVariable Integer id) {
		return productService.get(id);
	}

	@GetMapping("/{id}/reviews")
	public List<ProductReview> getReviews(@PathVariable Integer id) {
		return productService.getReviews(id);
	}

	@GetMapping("/{id}/reviews/stats")
	public Map<String, Object> getReviewStats(@PathVariable Integer id) {
		return productService.getReviewStats(id);
	}
}
