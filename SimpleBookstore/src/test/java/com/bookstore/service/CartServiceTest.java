package com.bookstore.service;

import com.bookstore.model.Book;
import com.bookstore.model.CartItem;
import com.bookstore.repository.CartItemRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CartService Tests")
class CartServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private BookService bookService;

    @InjectMocks
    private CartService cartService;

    private CartItem testCartItem;
    private Book testBook;
    private final String SESSION_ID = "test-session-123";

    @BeforeEach
    void setUp() {
        testCartItem = new CartItem(1L, 2, SESSION_ID);
        testCartItem.setId(1L);

        testBook = new Book("Test Book", "Test Author", "123-456-789", 19.99, "Test Description", 10);
        testBook.setId(1L);
    }

    @Test
    @DisplayName("Should return cart items for session")
    void shouldReturnCartItems_whenSessionIdProvided() {
        // Arrange
        List<CartItem> cartItems = Arrays.asList(testCartItem);
        when(cartItemRepository.findBySessionId(SESSION_ID)).thenReturn(cartItems);

        // Act
        List<CartItem> result = cartService.getCartItems(SESSION_ID);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSessionId()).isEqualTo(SESSION_ID);
        verify(cartItemRepository).findBySessionId(SESSION_ID);
    }

    @Test
    @DisplayName("Should create new cart item when item not exists")
    void shouldCreateNewCartItem_whenItemNotExists() {
        // Arrange
        when(cartItemRepository.findBySessionIdAndBookId(SESSION_ID, 1L)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(testCartItem);

        // Act
        CartItem result = cartService.addToCart(SESSION_ID, 1L, 2);

        // Assert
        assertThat(result).isNotNull();
        verify(cartItemRepository).findBySessionIdAndBookId(SESSION_ID, 1L);
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    @DisplayName("Should update existing cart item quantity")
    void shouldUpdateExistingCartItem_whenItemExists() {
        // Arrange
        when(cartItemRepository.findBySessionIdAndBookId(SESSION_ID, 1L)).thenReturn(Optional.of(testCartItem));
        when(cartItemRepository.save(testCartItem)).thenReturn(testCartItem);

        // Act
        CartItem result = cartService.addToCart(SESSION_ID, 1L, 3);

        // Assert
        assertThat(result.getQuantity()).isEqualTo(5); // 2 + 3
        verify(cartItemRepository).findBySessionIdAndBookId(SESSION_ID, 1L);
        verify(cartItemRepository).save(testCartItem);
    }

    @Test
    @DisplayName("Should update cart item quantity when item exists")
    void shouldUpdateCartItemQuantity_whenItemExists() {
        // Arrange
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(testCartItem));
        when(cartItemRepository.save(testCartItem)).thenReturn(testCartItem);

        // Act
        CartItem result = cartService.updateCartItem(1L, 5);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getQuantity()).isEqualTo(5);
        verify(cartItemRepository).findById(1L);
        verify(cartItemRepository).save(testCartItem);
    }

    @Test
    @DisplayName("Should return null when cart item not found")
    void shouldReturnNull_whenCartItemNotFound() {
        // Arrange
        when(cartItemRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        CartItem result = cartService.updateCartItem(999L, 5);

        // Assert
        assertThat(result).isNull();
        verify(cartItemRepository).findById(999L);
        verify(cartItemRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should call repository delete when remove cart item")
    void shouldCallRepositoryDelete_whenRemoveCartItem() {
        // Act
        cartService.removeCartItem(1L);

        // Assert
        verify(cartItemRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should remove all items for session when clear cart")
    void shouldRemoveAllItemsForSession_whenClearCart() {
        // Act
        cartService.clearCart(SESSION_ID);

        // Assert
        verify(cartItemRepository).deleteBySessionId(SESSION_ID);
    }

    @Test
    @DisplayName("Should calculate correct total for cart items")
    void shouldCalculateCorrectTotal_whenCalculateCartTotal() {
        // Arrange
        CartItem item1 = new CartItem(1L, 2, SESSION_ID);
        CartItem item2 = new CartItem(2L, 1, SESSION_ID);
        List<CartItem> cartItems = Arrays.asList(item1, item2);

        Book book1 = new Book("Book 1", "Author 1", "111", 10.00, "Desc 1", 5);
        book1.setId(1L);
        Book book2 = new Book("Book 2", "Author 2", "222", 20.00, "Desc 2", 3);
        book2.setId(2L);

        when(cartItemRepository.findBySessionId(SESSION_ID)).thenReturn(cartItems);
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book1));
        when(bookService.getBookById(2L)).thenReturn(Optional.of(book2));

        // Act
        Double result = cartService.calculateCartTotal(SESSION_ID);

        // Assert
        assertThat(result).isEqualTo(40.00); // (2 * 10.00) + (1 * 20.00)
        verify(cartItemRepository).findBySessionId(SESSION_ID);
        verify(bookService).getBookById(1L);
        verify(bookService).getBookById(2L);
    }

    @Test
    @DisplayName("Should handle non-existent books when calculating total")
    void shouldHandleNonExistentBooks_whenCalculatingTotal() {
        // Arrange
        List<CartItem> cartItems = Arrays.asList(testCartItem);
        when(cartItemRepository.findBySessionId(SESSION_ID)).thenReturn(cartItems);
        when(bookService.getBookById(1L)).thenReturn(Optional.empty());

        // Act
        Double result = cartService.calculateCartTotal(SESSION_ID);

        // Assert
        assertThat(result).isEqualTo(0.0);
        verify(cartItemRepository).findBySessionId(SESSION_ID);
        verify(bookService).getBookById(1L);
    }

    @Test
    @DisplayName("Should return zero for empty cart")
    void shouldReturnZero_whenCartIsEmpty() {
        // Arrange
        when(cartItemRepository.findBySessionId(SESSION_ID)).thenReturn(Arrays.asList());

        // Act
        Double result = cartService.calculateCartTotal(SESSION_ID);

        // Assert
        assertThat(result).isEqualTo(0.0);
        verify(cartItemRepository).findBySessionId(SESSION_ID);
        verify(bookService, never()).getBookById(any());
    }
}
