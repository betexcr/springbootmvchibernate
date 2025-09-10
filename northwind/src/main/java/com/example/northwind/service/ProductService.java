package com.example.northwind.service;

import com.example.northwind.entity.Product;
import com.example.northwind.repository.ProductRepository;
import com.example.northwind.repository.spec.ProductSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

	public Page<Product> list(String q, Integer categoryId, Integer supplierId,
							 java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice,
							 Boolean discontinued, Pageable pageable) {
		Specification<Product> spec = Specification.where(ProductSpecifications.nameContains(q))
			.and(ProductSpecifications.categoryId(categoryId))
			.and(ProductSpecifications.supplierId(supplierId))
			.and(ProductSpecifications.priceGte(minPrice))
			.and(ProductSpecifications.priceLte(maxPrice))
			.and(ProductSpecifications.discontinued(discontinued));
		return productRepository.findAll(spec, pageable);
	}

	public Product get(Integer id) {
		return productRepository.findById(id).orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Product not found"));
	}
}
