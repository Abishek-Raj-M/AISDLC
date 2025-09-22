package com.bookstore.service;

import com.bookstore.model.Book;
import com.bookstore.model.CartItem;
import com.bookstore.model.Order;
import com.bookstore.model.OrderItem;
import com.bookstore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private BookService bookService;

    @Transactional
    public Order createOrder(String sessionId, String customerName, String customerEmail, String customerAddress) {
        List<CartItem> cartItems = cartService.getCartItems(sessionId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Cache books and validate stock availability FIRST before creating any order
        Map<Long, Book> bookCache = new HashMap<>();
        for (CartItem cartItem : cartItems) {
            Optional<Book> bookOpt = bookService.getBookById(cartItem.getBookId());
            if (bookOpt.isPresent()) {
                Book book = bookOpt.get();
                bookCache.put(book.getId(), book);

                if (book.getStockQuantity() < cartItem.getQuantity()) {
                    throw new RuntimeException("Insufficient stock for book: " + book.getTitle());
                }
            } else {
                throw new RuntimeException("Book not found with ID: " + cartItem.getBookId());
            }
        }

        double totalAmount = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        // Create order AFTER validation passes
        Order order = new Order(customerName, customerEmail, customerAddress, 0.0);
        order = orderRepository.save(order);

        // Process cart items and update stock using cached book data
        for (CartItem cartItem : cartItems) {
            Book book = bookCache.get(cartItem.getBookId());

            // Update stock
            bookService.updateStock(book.getId(), cartItem.getQuantity());

            // Create order item
            OrderItem orderItem = new OrderItem(order, book, cartItem.getQuantity(), book.getPrice());
            orderItems.add(orderItem);

            totalAmount += book.getPrice() * cartItem.getQuantity();
        }

        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);
        order.setStatus("CONFIRMED");

        // Clear cart
        cartService.clearCart(sessionId);

        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrdersByCustomerEmail(String email) {
        return orderRepository.findByCustomerEmailOrderByOrderDateDesc(email);
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatusOrderByOrderDateDesc(status);
    }
}
