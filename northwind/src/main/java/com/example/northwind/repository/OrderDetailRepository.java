package com.example.northwind.repository;

import com.example.northwind.entity.OrderDetail;
import com.example.northwind.entity.OrderDetailId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {
    List<OrderDetail> findByOrder_Id(Integer orderId);
    Page<OrderDetail> findByOrder_Id(Integer orderId, Pageable pageable);

    @Query("select coalesce(sum(od.unitPrice * od.quantity * (1 - od.discount)), 0) from OrderDetail od " +
            "where (:customerId is null or od.order.customer.id = :customerId) " +
            "and (:fromDate is null or od.order.orderDate >= :fromDate) " +
            "and (:toDate is null or od.order.orderDate <= :toDate)")
    BigDecimal sumTotalByCustomerAndDate(
            @Param("customerId") String customerId,
            @Param("fromDate") java.time.LocalDate fromDate,
            @Param("toDate") java.time.LocalDate toDate);
}
