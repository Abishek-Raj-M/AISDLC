package com.bookstore.service;

import com.bookstore.model.Book;
import com.bookstore.model.CartItem;
import com.bookstore.model.Order;
import com.bookstore.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService Tests")
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private OrderService orderService;

    private Order testOrder;
    private CartItem testCartItem;
    private Book testBook;
    private final String SESSION_ID = "test-session-123";
    private final String CUSTOMER_NAME = "John Doe";
    private final String CUSTOMER_EMAIL = "john@example.com";
    private final String CUSTOMER_ADDRESS = "123 Test Street";

    @BeforeEach
    void setUp() {
        testOrder = new Order(CUSTOMER_NAME, CUSTOMER_EMAIL, CUSTOMER_ADDRESS, 39.98);
        testOrder.setId(1L);

        testCartItem = new CartItem(1L, 2, SESSION_ID);
        testCartItem.setId(1L);

        testBook = new Book("Test Book", "Test Author", "123-456-789", 19.99, "Test Description", 10);
        testBook.setId(1L);
    }

    @Test
    @DisplayName("Should create order with valid cart")
    void shouldCreateOrder_whenValidCartProvided() {
        // Arrange
        List<CartItem> cartItems = Arrays.asList(testCartItem);
        when(cartService.getCartItems(SESSION_ID)).thenReturn(cartItems);
        when(bookService.getBookById(1L)).thenReturn(Optional.of(testBook));
        when(bookService.updateStock(1L, 2)).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // Act
        Order result = orderService.createOrder(SESSION_ID, CUSTOMER_NAME, CUSTOMER_EMAIL, CUSTOMER_ADDRESS);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getCustomerName()).isEqualTo(CUSTOMER_NAME);
        assertThat(result.getStatus()).isEqualTo("CONFIRMED");
        verify(cartService).getCartItems(SESSION_ID);
        verify(bookService).getBookById(1L);
        verify(bookService).updateStock(1L, 2);
        verify(cartService).clearCart(SESSION_ID);
        verify(orderRepository, times(2)).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw exception when cart is empty")
    void shouldThrowException_whenCartIsEmpty() {
        // Arrange
        when(cartService.getCartItems(SESSION_ID)).thenReturn(Arrays.asList());

        // Act & Assert
        assertThatThrownBy(() -> orderService.createOrder(SESSION_ID, CUSTOMER_NAME, CUSTOMER_EMAIL, CUSTOMER_ADDRESS))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cart is empty");

        verify(cartService).getCartItems(SESSION_ID);
        verify(orderRepository, never()).save(any());
        verify(cartService, never()).clearCart(any());
    }

    @Test
    @DisplayName("Should throw exception when insufficient stock")
    void shouldThrowException_whenInsufficientStock() {
        // Arrange
        Book lowStockBook = new Book("Low Stock Book", "Author", "456", 15.99, "Description", 1);
        lowStockBook.setId(1L);
        CartItem highQuantityItem = new CartItem(1L, 5, SESSION_ID);

        List<CartItem> cartItems = Arrays.asList(highQuantityItem);
        when(cartService.getCartItems(SESSION_ID)).thenReturn(cartItems);
        when(bookService.getBookById(1L)).thenReturn(Optional.of(lowStockBook));

        // Act & Assert
        assertThatThrownBy(() -> orderService.createOrder(SESSION_ID, CUSTOMER_NAME, CUSTOMER_EMAIL, CUSTOMER_ADDRESS))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Insufficient stock for book: Low Stock Book");

        verify(cartService).getCartItems(SESSION_ID);
        verify(bookService).getBookById(1L);
        verify(bookService, never()).updateStock(anyLong(), anyInt());
        verify(cartService, never()).clearCart(any());
    }

    @Test
    @DisplayName("Should update book stock correctly during order creation")
    void shouldUpdateBookStockCorrectly_whenCreatingOrder() {
        // Arrange
        List<CartItem> cartItems = Arrays.asList(testCartItem);
        when(cartService.getCartItems(SESSION_ID)).thenReturn(cartItems);
        when(bookService.getBookById(1L)).thenReturn(Optional.of(testBook));
        when(bookService.updateStock(1L, 2)).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // Act
        orderService.createOrder(SESSION_ID, CUSTOMER_NAME, CUSTOMER_EMAIL, CUSTOMER_ADDRESS);

        // Assert
        verify(bookService).updateStock(1L, 2);
    }

    @Test
    @DisplayName("Should clear cart after successful order")
    void shouldClearCart_afterSuccessfulOrder() {
        // Arrange
        List<CartItem> cartItems = Arrays.asList(testCartItem);
        when(cartService.getCartItems(SESSION_ID)).thenReturn(cartItems);
        when(bookService.getBookById(1L)).thenReturn(Optional.of(testBook));
        when(bookService.updateStock(1L, 2)).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // Act
        orderService.createOrder(SESSION_ID, CUSTOMER_NAME, CUSTOMER_EMAIL, CUSTOMER_ADDRESS);

        // Assert
        verify(cartService).clearCart(SESSION_ID);
    }

    @Test
    @DisplayName("Should return all orders")
    void shouldReturnAllOrders_whenGetAllOrdersCalled() {
        // Arrange
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findAll()).thenReturn(orders);

        // Act
        List<Order> result = orderService.getAllOrders();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testOrder);
        verify(orderRepository).findAll();
    }

    @Test
    @DisplayName("Should return order when valid ID provided")
    void shouldReturnOrder_whenValidIdProvided() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        // Act
        Optional<Order> result = orderService.getOrderById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testOrder);
        verify(orderRepository).findById(1L);
    }

    @Test
    @DisplayName("Should return empty when order not found")
    void shouldReturnEmpty_whenOrderNotFound() {
        // Arrange
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Order> result = orderService.getOrderById(999L);

        // Assert
        assertThat(result).isEmpty();
        verify(orderRepository).findById(999L);
    }

    @Test
    @DisplayName("Should return orders by customer email")
    void shouldReturnOrdersByCustomerEmail_whenEmailProvided() {
        // Arrange
        List<Order> customerOrders = Arrays.asList(testOrder);
        when(orderRepository.findByCustomerEmailOrderByOrderDateDesc(CUSTOMER_EMAIL)).thenReturn(customerOrders);

        // Act
        List<Order> result = orderService.getOrdersByCustomerEmail(CUSTOMER_EMAIL);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testOrder);
        verify(orderRepository).findByCustomerEmailOrderByOrderDateDesc(CUSTOMER_EMAIL);
    }

    @Test
    @DisplayName("Should return orders by status")
    void shouldReturnOrdersByStatus_whenStatusProvided() {
        // Arrange
        String status = "CONFIRMED";
        List<Order> statusOrders = Arrays.asList(testOrder);
        when(orderRepository.findByStatusOrderByOrderDateDesc(status)).thenReturn(statusOrders);

        // Act
        List<Order> result = orderService.getOrdersByStatus(status);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testOrder);
        verify(orderRepository).findByStatusOrderByOrderDateDesc(status);
    }

    @Test
    @DisplayName("Should calculate correct total amount for order")
    void shouldCalculateCorrectTotalAmount_whenCreatingOrder() {
        // Arrange
        CartItem item1 = new CartItem(1L, 2, SESSION_ID);
        CartItem item2 = new CartItem(2L, 1, SESSION_ID);
        List<CartItem> cartItems = Arrays.asList(item1, item2);

        Book book1 = new Book("Book 1", "Author 1", "111", 10.00, "Description 1", 5);
        book1.setId(1L);
        Book book2 = new Book("Book 2", "Author 2", "222", 20.00, "Description 2", 3);
        book2.setId(2L);

        when(cartService.getCartItems(SESSION_ID)).thenReturn(cartItems);
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book1));
        when(bookService.getBookById(2L)).thenReturn(Optional.of(book2));
        when(bookService.updateStock(1L, 2)).thenReturn(true);
        when(bookService.updateStock(2L, 1)).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L);
            return order;
        });

        // Act
        Order result = orderService.createOrder(SESSION_ID, CUSTOMER_NAME, CUSTOMER_EMAIL, CUSTOMER_ADDRESS);

        // Assert
        assertThat(result.getTotalAmount()).isEqualTo(40.00); // (2 * 10.00) + (1 * 20.00)
    }
}
