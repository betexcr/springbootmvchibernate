package com.example.northwind.dto;

import java.math.BigDecimal;
import java.util.List;

public class CartDTO {
    private String id;
    private List<CartItemDTO> items;
    private BigDecimal totalPrice;
    private int totalQuantity;
    private String checkoutUrl;

    public CartDTO() {}

    public CartDTO(String id, List<CartItemDTO> items, BigDecimal totalPrice, int totalQuantity, String checkoutUrl) {
        this.id = id;
        this.items = items;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.checkoutUrl = checkoutUrl;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    public void setCheckoutUrl(String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
    }
}
