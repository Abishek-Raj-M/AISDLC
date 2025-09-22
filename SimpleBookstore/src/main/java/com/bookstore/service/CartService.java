package com.bookstore.service;

import com.bookstore.model.Book;
import com.bookstore.model.CartItem;
import com.bookstore.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private BookService bookService;

    public List<CartItem> getCartItems(String sessionId) {
        return cartItemRepository.findBySessionId(sessionId);
    }

    public CartItem addToCart(String sessionId, Long bookId, Integer quantity) {
        Optional<CartItem> existingItem = cartItemRepository.findBySessionIdAndBookId(sessionId, bookId);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            return cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem(bookId, quantity, sessionId);
            return cartItemRepository.save(newItem);
        }
    }

    public CartItem updateCartItem(Long itemId, Integer quantity) {
        Optional<CartItem> itemOpt = cartItemRepository.findById(itemId);
        if (itemOpt.isPresent()) {
            CartItem item = itemOpt.get();
            item.setQuantity(quantity);
            return cartItemRepository.save(item);
        }
        return null;
    }

    public void removeCartItem(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }

    @Transactional
    public void clearCart(String sessionId) {
        cartItemRepository.deleteBySessionId(sessionId);
    }

    public Double calculateCartTotal(String sessionId) {
        List<CartItem> cartItems = getCartItems(sessionId);
        double total = 0.0;

        for (CartItem item : cartItems) {
            Optional<Book> book = bookService.getBookById(item.getBookId());
            if (book.isPresent()) {
                total += book.get().getPrice() * item.getQuantity();
            }
        }

        return total;
    }
}
