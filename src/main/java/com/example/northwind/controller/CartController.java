package com.example.northwind.controller;

import com.example.northwind.dto.CartItemDTO;
import com.example.northwind.dto.CartDTO;
import com.example.northwind.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable String cartId) {
        CartDTO cart = cartService.getCart(cartId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartDTO> addItem(@PathVariable String cartId, @RequestBody CartItemDTO item) {
        CartDTO cart = cartService.addItem(cartId, item);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<CartDTO> updateItem(@PathVariable String cartId, @PathVariable String itemId, @RequestBody CartItemDTO item) {
        CartDTO cart = cartService.updateItem(cartId, itemId, item);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<CartDTO> removeItem(@PathVariable String cartId, @PathVariable String itemId) {
        CartDTO cart = cartService.removeItem(cartId, itemId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping
    public ResponseEntity<CartDTO> createCart() {
        CartDTO cart = cartService.createCart();
        return ResponseEntity.ok(cart);
    }
}
