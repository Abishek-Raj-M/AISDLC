package com.bookstore.controller;

import com.bookstore.model.CartItem;
import com.bookstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public List<CartItem> getCartItems(HttpSession session) {
        return cartService.getCartItems(session.getId());
    }

    @PostMapping
    public CartItem addToCart(@RequestBody CartRequest request, HttpSession session) {
        return cartService.addToCart(session.getId(), request.getBookId(), request.getQuantity());
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<CartItem> updateCartItem(@PathVariable Long itemId, @RequestBody CartRequest request) {
        CartItem updatedItem = cartService.updateCartItem(itemId, request.getQuantity());
        return updatedItem != null ? ResponseEntity.ok(updatedItem) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long itemId) {
        cartService.removeCartItem(itemId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(HttpSession session) {
        cartService.clearCart(session.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/total")
    public ResponseEntity<Double> getCartTotal(HttpSession session) {
        Double total = cartService.calculateCartTotal(session.getId());
        return ResponseEntity.ok(total);
    }

    // Inner class for request body
    public static class CartRequest {
        private Long bookId;
        private Integer quantity;

        public Long getBookId() { return bookId; }
        public void setBookId(Long bookId) { this.bookId = bookId; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
}
