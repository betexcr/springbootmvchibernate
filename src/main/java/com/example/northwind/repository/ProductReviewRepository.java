package com.example.northwind.repository;

import com.example.northwind.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    
    List<ProductReview> findByProductIdOrderByCreatedAtDesc(Integer productId);
    
    @Query("SELECT AVG(r.rating) FROM ProductReview r WHERE r.productId = :productId")
    Double findAverageRatingByProductId(@Param("productId") Integer productId);
    
    @Query("SELECT COUNT(r) FROM ProductReview r WHERE r.productId = :productId")
    Long countByProductId(@Param("productId") Integer productId);
}
