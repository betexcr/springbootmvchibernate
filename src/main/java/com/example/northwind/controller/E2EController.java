package com.example.northwind.controller;

import com.example.northwind.entity.Product;
import com.example.northwind.repository.ProductRepository;
import com.example.northwind.repository.spec.ProductSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/e2e")
public class E2EController {
    private final ProductRepository productRepository;

    public E2EController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Simple search endpoint to validate search flow end-to-end
    @GetMapping("/search")
    public Page<Product> search(
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "categoryId", required = false) Integer categoryId,
            @RequestParam(name = "supplierId", required = false) Integer supplierId,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(name = "discontinued", required = false) Boolean discontinued,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Specification<Product> spec = Specification
                .where(ProductSpecifications.nameContains(q))
                .and(ProductSpecifications.categoryId(categoryId))
                .and(ProductSpecifications.supplierId(supplierId))
                .and(ProductSpecifications.priceGte(minPrice))
                .and(ProductSpecifications.priceLte(maxPrice))
                .and(ProductSpecifications.discontinued(discontinued));
        return productRepository.findAll(spec, PageRequest.of(page, size));
    }

    // Select nth item from a search result page (for e2e click-through)
    @GetMapping("/select")
    public Product select(
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "index", defaultValue = "0") int index
    ) {
        Page<Product> page = productRepository.findAll(
                ProductSpecifications.nameContains(q),
                PageRequest.of(0, Math.max(1, index + 1))
        );
        if (index < 0 || index >= page.getNumberOfElements()) {
            throw new jakarta.persistence.EntityNotFoundException("Index out of range");
        }
        return page.getContent().get(index);
    }

    // Seed a single product (id,name,price) for deterministic detail page tests
    @PostMapping("/seedProduct")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> seedProduct(@RequestBody Map<String, Object> body) {
        Integer id = (Integer) body.get("id");
        String name = (String) body.get("name");
        Number priceNum = (Number) body.get("unitPrice");
        Float unitPrice = priceNum == null ? null : priceNum.floatValue();

        if (id == null || name == null || unitPrice == null) {
            throw new IllegalArgumentException("id, name, unitPrice are required");
        }

        Product p = productRepository.findById(id).orElseGet(() -> {
            Product np = new Product();
            np.setId(id);
            return np;
        });
        p.setName(name);
        p.setUnitPrice(unitPrice);
        p.setQuantityPerUnit(p.getQuantityPerUnit() == null ? "1 unit" : p.getQuantityPerUnit());
        p.setUnitsInStock(p.getUnitsInStock() == null ? (short)0 : p.getUnitsInStock());
        p.setUnitsOnOrder(p.getUnitsOnOrder() == null ? (short)0 : p.getUnitsOnOrder());
        p.setReorderLevel(p.getReorderLevel() == null ? (short)0 : p.getReorderLevel());
        p.setDiscontinued(p.getDiscontinued() == null ? Boolean.FALSE : p.getDiscontinued());
        productRepository.save(p);
        return Map.of("status", "ok", "id", p.getId());
    }
}

