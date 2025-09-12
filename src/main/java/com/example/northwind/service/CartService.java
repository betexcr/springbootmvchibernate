package com.example.northwind.service;

import com.example.northwind.dto.CartDTO;
import com.example.northwind.dto.CartItemDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CartService {

    // In-memory storage for demo purposes
    private final Map<String, CartDTO> carts = new ConcurrentHashMap<>();

    public CartDTO createCart() {
        String cartId = UUID.randomUUID().toString();
        CartDTO cart = new CartDTO(cartId, new ArrayList<>(), BigDecimal.ZERO, 0, "/checkout");
        carts.put(cartId, cart);
        return cart;
    }

    public CartDTO getCart(String cartId) {
        return carts.getOrDefault(cartId, new CartDTO(cartId, new ArrayList<>(), BigDecimal.ZERO, 0, "/checkout"));
    }

    public CartDTO addItem(String cartId, CartItemDTO item) {
        CartDTO cart = getCart(cartId);
        
        // Check if item already exists
        Optional<CartItemDTO> existingItem = cart.getItems().stream()
            .filter(i -> i.getProductId().equals(item.getProductId()))
            .findFirst();

        if (existingItem.isPresent()) {
            // Update quantity
            existingItem.get().setQuantity(existingItem.get().getQuantity() + item.getQuantity());
        } else {
            // Add new item
            item.setId(UUID.randomUUID().toString());
            cart.getItems().add(item);
        }

        updateCartTotals(cart);
        carts.put(cartId, cart);
        return cart;
    }

    public CartDTO updateItem(String cartId, String itemId, CartItemDTO item) {
        CartDTO cart = getCart(cartId);
        
        cart.getItems().removeIf(i -> i.getId().equals(itemId));
        if (item.getQuantity() > 0) {
            item.setId(itemId);
            cart.getItems().add(item);
        }

        updateCartTotals(cart);
        carts.put(cartId, cart);
        return cart;
    }

    public CartDTO removeItem(String cartId, String itemId) {
        CartDTO cart = getCart(cartId);
        cart.getItems().removeIf(i -> i.getId().equals(itemId));
        updateCartTotals(cart);
        carts.put(cartId, cart);
        return cart;
    }

    private void updateCartTotals(CartDTO cart) {
        BigDecimal totalPrice = cart.getItems().stream()
            .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        int totalQuantity = cart.getItems().stream()
            .mapToInt(CartItemDTO::getQuantity)
            .sum();

        cart.setTotalPrice(totalPrice);
        cart.setTotalQuantity(totalQuantity);
    }
}
