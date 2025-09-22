package com.bookstore.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@DisplayName("Model Validation Tests")
class ModelValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Book Entity Tests
    @Test
    @DisplayName("Should validate Book with all required fields")
    void shouldValidateBook_whenAllRequiredFieldsProvided() {
        // Arrange
        Book book = new Book("Valid Title", "Valid Author", "123-456-789", 19.99, "Valid Description", 10);

        // Act
        Set<ConstraintViolation<Book>> violations = validator.validate(book);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should fail validation when Book title is null")
    void shouldFailValidation_whenBookTitleIsNull() {
        // Arrange
        Book book = new Book(null, "Valid Author", "123-456-789", 19.99, "Valid Description", 10);

        // Act
        Set<ConstraintViolation<Book>> violations = validator.validate(book);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("title");
    }

    @Test
    @DisplayName("Should fail validation when Book author is null")
    void shouldFailValidation_whenBookAuthorIsNull() {
        // Arrange
        Book book = new Book("Valid Title", null, "123-456-789", 19.99, "Valid Description", 10);

        // Act
        Set<ConstraintViolation<Book>> violations = validator.validate(book);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("author");
    }

    @Test
    @DisplayName("Should fail validation when Book price is null")
    void shouldFailValidation_whenBookPriceIsNull() {
        // Arrange
        Book book = new Book("Valid Title", "Valid Author", "123-456-789", null, "Valid Description", 10);

        // Act
        Set<ConstraintViolation<Book>> violations = validator.validate(book);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("price");
    }

    @Test
    @DisplayName("Should validate Book with positive stock quantity")
    void shouldValidateBook_whenStockQuantityIsPositive() {
        // Arrange
        Book book = new Book("Valid Title", "Valid Author", "123-456-789", 19.99, "Valid Description", 5);

        // Act
        Set<ConstraintViolation<Book>> violations = validator.validate(book);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should validate Book with zero stock quantity")
    void shouldValidateBook_whenStockQuantityIsZero() {
        // Arrange
        Book book = new Book("Valid Title", "Valid Author", "123-456-789", 19.99, "Valid Description", 0);

        // Act
        Set<ConstraintViolation<Book>> violations = validator.validate(book);

        // Assert
        assertThat(violations).isEmpty();
    }

    // CartItem Entity Tests
    @Test
    @DisplayName("Should validate CartItem with positive quantity")
    void shouldValidateCartItem_whenQuantityIsPositive() {
        // Arrange
        CartItem cartItem = new CartItem(1L, 2, "test-session");

        // Act
        Set<ConstraintViolation<CartItem>> violations = validator.validate(cartItem);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should fail validation when CartItem quantity is zero")
    void shouldFailValidation_whenCartItemQuantityIsZero() {
        // Arrange
        CartItem cartItem = new CartItem(1L, 0, "test-session");

        // Act
        Set<ConstraintViolation<CartItem>> violations = validator.validate(cartItem);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("quantity");
    }

    @Test
    @DisplayName("Should fail validation when CartItem quantity is negative")
    void shouldFailValidation_whenCartItemQuantityIsNegative() {
        // Arrange
        CartItem cartItem = new CartItem(1L, -1, "test-session");

        // Act
        Set<ConstraintViolation<CartItem>> violations = validator.validate(cartItem);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("quantity");
    }

    @Test
    @DisplayName("Should fail validation when CartItem bookId is null")
    void shouldFailValidation_whenCartItemBookIdIsNull() {
        // Arrange
        CartItem cartItem = new CartItem(null, 2, "test-session");

        // Act
        Set<ConstraintViolation<CartItem>> violations = validator.validate(cartItem);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("bookId");
    }

    @Test
    @DisplayName("Should fail validation when CartItem sessionId is null")
    void shouldFailValidation_whenCartItemSessionIdIsNull() {
        // Arrange
        CartItem cartItem = new CartItem(1L, 2, null);

        // Act
        Set<ConstraintViolation<CartItem>> violations = validator.validate(cartItem);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("sessionId");
    }

    // Order Entity Tests
    @Test
    @DisplayName("Should validate Order with all required customer information")
    void shouldValidateOrder_whenAllCustomerInformationProvided() {
        // Arrange
        Order order = new Order("John Doe", "john@example.com", "123 Test Street", 39.98);

        // Act
        Set<ConstraintViolation<Order>> violations = validator.validate(order);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should fail validation when Order customer name is null")
    void shouldFailValidation_whenOrderCustomerNameIsNull() {
        // Arrange
        Order order = new Order(null, "john@example.com", "123 Test Street", 39.98);

        // Act
        Set<ConstraintViolation<Order>> violations = validator.validate(order);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("customerName");
    }

    @Test
    @DisplayName("Should fail validation when Order customer email is null")
    void shouldFailValidation_whenOrderCustomerEmailIsNull() {
        // Arrange
        Order order = new Order("John Doe", null, "123 Test Street", 39.98);

        // Act
        Set<ConstraintViolation<Order>> violations = validator.validate(order);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("customerEmail");
    }

    @Test
    @DisplayName("Should fail validation when Order customer address is null")
    void shouldFailValidation_whenOrderCustomerAddressIsNull() {
        // Arrange
        Order order = new Order("John Doe", "john@example.com", null, 39.98);

        // Act
        Set<ConstraintViolation<Order>> violations = validator.validate(order);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("customerAddress");
    }

    @Test
    @DisplayName("Should fail validation when Order total amount is null")
    void shouldFailValidation_whenOrderTotalAmountIsNull() {
        // Arrange
        Order order = new Order("John Doe", "john@example.com", "123 Test Street", null);

        // Act
        Set<ConstraintViolation<Order>> violations = validator.validate(order);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("totalAmount");
    }

    // OrderItem Entity Tests
    @Test
    @DisplayName("Should validate OrderItem with positive quantity and price")
    void shouldValidateOrderItem_whenQuantityAndPriceArePositive() {
        // Arrange
        Book book = new Book("Test Book", "Test Author", "123-456-789", 19.99, "Test Description", 10);
        book.setId(1L);
        Order order = new Order("John Doe", "john@example.com", "123 Test Street", 39.98);
        order.setId(1L);
        OrderItem orderItem = new OrderItem(order, book, 2, 19.99);

        // Act
        Set<ConstraintViolation<OrderItem>> violations = validator.validate(orderItem);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should fail validation when OrderItem quantity is null")
    void shouldFailValidation_whenOrderItemQuantityIsNull() {
        // Arrange
        Book book = new Book("Test Book", "Test Author", "123-456-789", 19.99, "Test Description", 10);
        book.setId(1L);
        Order order = new Order("John Doe", "john@example.com", "123 Test Street", 39.98);
        order.setId(1L);
        OrderItem orderItem = new OrderItem(order, book, null, 19.99);

        // Act
        Set<ConstraintViolation<OrderItem>> violations = validator.validate(orderItem);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("quantity");
    }

    @Test
    @DisplayName("Should fail validation when OrderItem price is null")
    void shouldFailValidation_whenOrderItemPriceIsNull() {
        // Arrange
        Book book = new Book("Test Book", "Test Author", "123-456-789", 19.99, "Test Description", 10);
        book.setId(1L);
        Order order = new Order("John Doe", "john@example.com", "123 Test Street", 39.98);
        order.setId(1L);
        OrderItem orderItem = new OrderItem(order, book, 2, null);

        // Act
        Set<ConstraintViolation<OrderItem>> violations = validator.validate(orderItem);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("price");
    }

    // Entity Relationship Tests
    @Test
    @DisplayName("Should maintain Order-OrderItem relationship integrity")
    void shouldMaintainOrderOrderItemRelationship() {
        // Arrange
        Order order = new Order("John Doe", "john@example.com", "123 Test Street", 39.98);
        order.setId(1L);

        Book book = new Book("Test Book", "Test Author", "123-456-789", 19.99, "Test Description", 10);
        book.setId(1L);

        OrderItem orderItem = new OrderItem(order, book, 2, 19.99);

        // Act & Assert
        assertThat(orderItem.getOrder()).isEqualTo(order);
        assertThat(orderItem.getBook()).isEqualTo(book);
        assertThat(orderItem.getQuantity()).isEqualTo(2);
        assertThat(orderItem.getPrice()).isEqualTo(19.99);
    }

    @Test
    @DisplayName("Should validate entity field constraints")
    void shouldValidateEntityFieldConstraints() {
        // Test Book entity constraints
        Book book = new Book();
        book.setTitle("Valid Title");
        book.setAuthor("Valid Author");
        book.setPrice(19.99);
        book.setStockQuantity(10);

        Set<ConstraintViolation<Book>> bookViolations = validator.validate(book);
        assertThat(bookViolations).isEmpty();

        // Test that Book can be created with minimum required fields
        Book minimalBook = new Book("Title", "Author", null, 0.01, null, 0);
        Set<ConstraintViolation<Book>> minimalBookViolations = validator.validate(minimalBook);
        assertThat(minimalBookViolations).isEmpty();
    }
}
