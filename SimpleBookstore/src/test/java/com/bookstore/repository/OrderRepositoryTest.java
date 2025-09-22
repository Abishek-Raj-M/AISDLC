package com.bookstore.repository;

import com.bookstore.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("OrderRepository Tests")
class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    private Order order1;
    private Order order2;
    private Order order3;

    @BeforeEach
    void setUp() {
        order1 = new Order("John Doe", "john@example.com", "123 Main St", 39.98);
        order1.setOrderDate(LocalDateTime.now().minusDays(2));
        order1.setStatus("CONFIRMED");

        order2 = new Order("Jane Smith", "jane@example.com", "456 Oak Ave", 59.97);
        order2.setOrderDate(LocalDateTime.now().minusDays(1));
        order2.setStatus("PENDING");

        order3 = new Order("John Doe", "john@example.com", "123 Main St", 29.99);
        order3.setOrderDate(LocalDateTime.now());
        order3.setStatus("CONFIRMED");

        entityManager.persistAndFlush(order1);
        entityManager.persistAndFlush(order2);
        entityManager.persistAndFlush(order3);
    }

    @Test
    @DisplayName("Should find orders by customer email ordered by date desc")
    void shouldFindOrdersByCustomerEmail_whenEmailExists() {
        // Act
        List<Order> result = orderRepository.findByCustomerEmailOrderByOrderDateDesc("john@example.com");

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo(order3); // Most recent first
        assertThat(result.get(1)).isEqualTo(order1);
        assertThat(result).extracting(Order::getCustomerEmail)
                .containsOnly("john@example.com");
    }

    @Test
    @DisplayName("Should return empty list for non-existent customer email")
    void shouldReturnEmptyList_whenCustomerEmailNotExists() {
        // Act
        List<Order> result = orderRepository.findByCustomerEmailOrderByOrderDateDesc("nonexistent@example.com");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should find orders by status ordered by date desc")
    void shouldFindOrdersByStatus_whenStatusExists() {
        // Act
        List<Order> result = orderRepository.findByStatusOrderByOrderDateDesc("CONFIRMED");

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo(order3); // Most recent first
        assertThat(result.get(1)).isEqualTo(order1);
        assertThat(result).extracting(Order::getStatus)
                .containsOnly("CONFIRMED");
    }

    @Test
    @DisplayName("Should find single order by unique status")
    void shouldFindSingleOrder_whenUniqueStatus() {
        // Act
        List<Order> result = orderRepository.findByStatusOrderByOrderDateDesc("PENDING");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(order2);
    }

    @Test
    @DisplayName("Should return empty list for non-existent status")
    void shouldReturnEmptyList_whenStatusNotExists() {
        // Act
        List<Order> result = orderRepository.findByStatusOrderByOrderDateDesc("CANCELLED");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should save order and generate ID")
    void shouldSaveOrderAndGenerateId_whenSavingNewOrder() {
        // Arrange
        Order newOrder = new Order("Bob Johnson", "bob@example.com", "789 Pine St", 49.99);

        // Act
        Order savedOrder = orderRepository.save(newOrder);

        // Assert
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getCustomerName()).isEqualTo("Bob Johnson");
        assertThat(savedOrder.getOrderDate()).isNotNull();
        assertThat(savedOrder.getStatus()).isEqualTo("PENDING");
    }

    @Test
    @DisplayName("Should retrieve saved order by ID")
    void shouldRetrieveSavedOrder_whenFindingById() {
        // Act
        Optional<Order> result = orderRepository.findById(order1.getId());

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getCustomerName()).isEqualTo("John Doe");
        assertThat(result.get().getTotalAmount()).isEqualTo(39.98);
    }

    @Test
    @DisplayName("Should return empty when order not found by ID")
    void shouldReturnEmpty_whenOrderNotFoundById() {
        // Act
        Optional<Order> result = orderRepository.findById(999L);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should update order when saving existing order")
    void shouldUpdateOrder_whenSavingExistingOrder() {
        // Arrange
        order1.setStatus("SHIPPED");
        order1.setTotalAmount(45.99);

        // Act
        Order updatedOrder = orderRepository.save(order1);

        // Assert
        assertThat(updatedOrder.getStatus()).isEqualTo("SHIPPED");
        assertThat(updatedOrder.getTotalAmount()).isEqualTo(45.99);
        assertThat(updatedOrder.getId()).isEqualTo(order1.getId());
    }

    @Test
    @DisplayName("Should delete order from database")
    void shouldDeleteOrder_whenDeleteCalled() {
        // Arrange
        Long orderId = order1.getId();

        // Act
        orderRepository.deleteById(orderId);

        // Assert
        Optional<Order> result = orderRepository.findById(orderId);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should set default order date when saving")
    void shouldSetDefaultOrderDate_whenSavingOrder() {
        // Arrange
        Order orderWithoutDate = new Order("Test User", "test@example.com", "Test Address", 19.99);
        LocalDateTime beforeSave = LocalDateTime.now();

        // Act
        Order savedOrder = orderRepository.save(orderWithoutDate);

        // Assert
        assertThat(savedOrder.getOrderDate()).isNotNull();
        assertThat(savedOrder.getOrderDate()).isAfterOrEqualTo(beforeSave);
    }
}
