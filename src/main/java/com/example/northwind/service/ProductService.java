package com.example.northwind.service;

import com.example.northwind.entity.Product;
import com.example.northwind.entity.ProductReview;
import com.example.northwind.repository.ProductRepository;
import com.example.northwind.repository.ProductReviewRepository;
import com.example.northwind.repository.spec.ProductSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;
	private final ProductReviewRepository productReviewRepository;

	public Page<Product> list(String q, Integer categoryId, Integer supplierId,
							 java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice,
							 Boolean discontinued, Pageable pageable) {
		Specification<Product> spec = ProductSpecifications.buildSpecification(q, categoryId, supplierId, minPrice, maxPrice, discontinued);
		return productRepository.findAll(spec, pageable);
	}

	public Product get(Integer id) {
		return productRepository.findById(id).orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Product not found"));
	}

	public List<ProductReview> getReviews(Integer productId) {
		return productReviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
	}

	public Map<String, Object> getReviewStats(Integer productId) {
		Double averageRating = productReviewRepository.findAverageRatingByProductId(productId);
		Long reviewCount = productReviewRepository.countByProductId(productId);
		
		Map<String, Object> stats = new HashMap<>();
		stats.put("averageRating", averageRating != null ? averageRating : 0.0);
		stats.put("reviewCount", reviewCount);
		
		return stats;
	}
}
