package com.bookstore.repository;

import com.bookstore.model.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("CartItemRepository Tests")
class CartItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CartItemRepository cartItemRepository;

    private CartItem cartItem1;
    private CartItem cartItem2;
    private CartItem cartItem3;

    @BeforeEach
    void setUp() {
        cartItem1 = new CartItem(1L, 2, "session-123");
        cartItem2 = new CartItem(2L, 1, "session-123");
        cartItem3 = new CartItem(1L, 3, "session-456");

        entityManager.persistAndFlush(cartItem1);
        entityManager.persistAndFlush(cartItem2);
        entityManager.persistAndFlush(cartItem3);
    }

    @Test
    @DisplayName("Should find cart items by session ID")
    void shouldFindCartItemsBySessionId_whenSessionExists() {
        // Act
        List<CartItem> result = cartItemRepository.findBySessionId("session-123");

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting(CartItem::getSessionId)
                .containsOnly("session-123");
        assertThat(result).extracting(CartItem::getBookId)
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    @DisplayName("Should return empty list for non-existent session")
    void shouldReturnEmptyList_whenSessionNotExists() {
        // Act
        List<CartItem> result = cartItemRepository.findBySessionId("non-existent-session");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should find specific cart item by session and book ID")
    void shouldFindSpecificCartItem_whenSessionAndBookIdMatch() {
        // Act
        Optional<CartItem> result = cartItemRepository.findBySessionIdAndBookId("session-123", 1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getQuantity()).isEqualTo(2);
        assertThat(result.get().getBookId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should return empty when cart item not found")
    void shouldReturnEmpty_whenCartItemNotFound() {
        // Act
        Optional<CartItem> result = cartItemRepository.findBySessionIdAndBookId("session-123", 999L);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should delete all items for specific session")
    void shouldDeleteAllItemsForSession_whenDeleteBySessionId() {
        // Arrange
        List<CartItem> beforeDelete = cartItemRepository.findBySessionId("session-123");
        assertThat(beforeDelete).hasSize(2);

        // Act
        cartItemRepository.deleteBySessionId("session-123");
        entityManager.flush();

        // Assert
        List<CartItem> afterDelete = cartItemRepository.findBySessionId("session-123");
        assertThat(afterDelete).isEmpty();

        // Verify other session items are not affected
        List<CartItem> otherSessionItems = cartItemRepository.findBySessionId("session-456");
        assertThat(otherSessionItems).hasSize(1);
    }

    @Test
    @DisplayName("Should save new cart item")
    void shouldSaveNewCartItem_whenSaving() {
        // Arrange
        CartItem newItem = new CartItem(3L, 5, "new-session");

        // Act
        CartItem savedItem = cartItemRepository.save(newItem);

        // Assert
        assertThat(savedItem.getId()).isNotNull();
        assertThat(savedItem.getBookId()).isEqualTo(3L);
        assertThat(savedItem.getQuantity()).isEqualTo(5);
        assertThat(savedItem.getSessionId()).isEqualTo("new-session");
    }

    @Test
    @DisplayName("Should update existing cart item")
    void shouldUpdateExistingCartItem_whenSaving() {
        // Arrange
        cartItem1.setQuantity(10);

        // Act
        CartItem updatedItem = cartItemRepository.save(cartItem1);

        // Assert
        assertThat(updatedItem.getQuantity()).isEqualTo(10);
        assertThat(updatedItem.getId()).isEqualTo(cartItem1.getId());
    }

    @Test
    @DisplayName("Should delete cart item by ID")
    void shouldDeleteCartItem_whenDeleteById() {
        // Arrange
        Long itemId = cartItem1.getId();

        // Act
        cartItemRepository.deleteById(itemId);
        entityManager.flush();

        // Assert
        Optional<CartItem> result = cartItemRepository.findById(itemId);
        assertThat(result).isEmpty();
    }
}
