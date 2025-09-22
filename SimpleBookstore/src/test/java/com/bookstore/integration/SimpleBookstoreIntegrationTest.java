package com.bookstore.integration;

import com.bookstore.model.Book;
import com.bookstore.model.CartItem;
import com.bookstore.model.Order;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CartItemRepository;
import com.bookstore.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Simple Bookstore Integration Tests")
class SimpleBookstoreIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Book testBook;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        // Clean up repositories
        cartItemRepository.deleteAll();
        orderRepository.deleteAll();
        bookRepository.deleteAll();

        // Create test book
        testBook = new Book("Integration Test Book", "Test Author", "999-888-777", 25.99, "Test book for integration", 10);
        testBook = bookRepository.save(testBook);

        // Create mock session
        session = new MockHttpSession();
    }

    @Test
    @DisplayName("Complete book browsing workflow")
    @Transactional
    void shouldCompleteBookBrowsingWorkflow() throws Exception {
        // 1. Get all books
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Integration Test Book")));

        // 2. Search for books
        mockMvc.perform(get("/api/books/search").param("query", "Integration"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        // 3. Get book details
        mockMvc.perform(get("/api/books/" + testBook.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Integration Test Book")));

        // 4. Get available books
        mockMvc.perform(get("/api/books/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("Complete shopping cart workflow")
    @Transactional
    void shouldCompleteShoppingCartWorkflow() throws Exception {
        // 1. Add item to cart
        String addToCartRequest = """
            {
                "bookId": %d,
                "quantity": 2
            }
            """.formatted(testBook.getId());

        mockMvc.perform(post("/api/cart")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addToCartRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId", is(testBook.getId().intValue())))
                .andExpect(jsonPath("$.quantity", is(2)));

        // 2. Get cart items
        mockMvc.perform(get("/api/cart").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        // 3. Get cart total
        mockMvc.perform(get("/api/cart/total").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(51.98))); // 2 * 25.99

        // 4. Update cart item quantity
        List<CartItem> cartItems = cartItemRepository.findBySessionId(session.getId());
        assertThat(cartItems).hasSize(1);
        Long cartItemId = cartItems.get(0).getId();

        String updateCartRequest = """
            {
                "quantity": 3
            }
            """;

        mockMvc.perform(put("/api/cart/" + cartItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateCartRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity", is(3)));

        // 5. Verify updated total
        mockMvc.perform(get("/api/cart/total").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(77.97))); // 3 * 25.99
    }

    @Test
    @DisplayName("Complete order placement workflow")
    @Transactional
    void shouldCompleteOrderPlacementWorkflow() throws Exception {
        // 1. Add item to cart first
        String addToCartRequest = """
            {
                "bookId": %d,
                "quantity": 2
            }
            """.formatted(testBook.getId());

        mockMvc.perform(post("/api/cart")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addToCartRequest))
                .andExpect(status().isOk());

        // 2. Place order
        String orderRequest = """
            {
                "customerName": "Integration Test Customer",
                "customerEmail": "test@integration.com",
                "customerAddress": "123 Integration St"
            }
            """;

        mockMvc.perform(post("/api/orders")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName", is("Integration Test Customer")))
                .andExpect(jsonPath("$.status", is("CONFIRMED")))
                .andExpect(jsonPath("$.totalAmount", is(51.98)));

        // 3. Verify cart is cleared
        mockMvc.perform(get("/api/cart").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        // 4. Verify book stock is updated
        mockMvc.perform(get("/api/books/" + testBook.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockQuantity", is(8))); // 10 - 2

        // 5. Verify order was saved
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getCustomerEmail()).isEqualTo("test@integration.com");
    }

    @Test
    @DisplayName("Should handle empty cart order placement")
    @Transactional
    void shouldHandleEmptyCartOrderPlacement() throws Exception {
        // Attempt to place order with empty cart
        String orderRequest = """
            {
                "customerName": "Test Customer",
                "customerEmail": "test@example.com",
                "customerAddress": "123 Test St"
            }
            """;

        mockMvc.perform(post("/api/orders")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderRequest))
                .andExpect(status().isBadRequest());

        // Verify no order was created
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).isEmpty();
    }

    @Test
    @DisplayName("Should handle insufficient stock scenario")
    @Transactional
    void shouldHandleInsufficientStockScenario() throws Exception {
        // Try to add more items than available stock
        String addToCartRequest = """
            {
                "bookId": %d,
                "quantity": 15
            }
            """.formatted(testBook.getId());

        mockMvc.perform(post("/api/cart")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addToCartRequest))
                .andExpect(status().isOk());

        // Attempt to place order
        String orderRequest = """
            {
                "customerName": "Test Customer",
                "customerEmail": "test@example.com",
                "customerAddress": "123 Test St"
            }
            """;

        mockMvc.perform(post("/api/orders")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderRequest))
                .andExpect(status().isBadRequest());

        // Verify no order was created and stock unchanged
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).isEmpty();

        Book updatedBook = bookRepository.findById(testBook.getId()).orElseThrow();
        assertThat(updatedBook.getStockQuantity()).isEqualTo(10); // Stock unchanged
    }

    @Test
    @DisplayName("Admin book management workflow")
    @Transactional
    void shouldCompleteAdminBookManagementWorkflow() throws Exception {
        // 1. Create new book
        Book newBook = new Book("Admin Test Book", "Admin Author", "111-222-333", 35.99, "Admin created book", 5);

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Admin Test Book")));

        // 2. Verify book count increased
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        // 3. Update existing book
        testBook.setPrice(29.99);
        testBook.setStockQuantity(15);

        mockMvc.perform(put("/api/books/" + testBook.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(29.99)))
                .andExpect(jsonPath("$.stockQuantity", is(15)));

        // 4. Delete book
        mockMvc.perform(delete("/api/books/" + testBook.getId()))
                .andExpect(status().isOk());

        // 5. Verify book was deleted
        mockMvc.perform(get("/api/books/" + testBook.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should handle cart item removal")
    @Transactional
    void shouldHandleCartItemRemoval() throws Exception {
        // 1. Add item to cart
        String addToCartRequest = """
            {
                "bookId": %d,
                "quantity": 1
            }
            """.formatted(testBook.getId());

        mockMvc.perform(post("/api/cart")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addToCartRequest))
                .andExpect(status().isOk());

        // 2. Get cart item ID
        List<CartItem> cartItems = cartItemRepository.findBySessionId(session.getId());
        assertThat(cartItems).hasSize(1);
        Long cartItemId = cartItems.get(0).getId();

        // 3. Remove cart item
        mockMvc.perform(delete("/api/cart/" + cartItemId))
                .andExpect(status().isOk());

        // 4. Verify cart is empty
        mockMvc.perform(get("/api/cart").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Should handle cart clearing")
    @Transactional
    void shouldHandleCartClearing() throws Exception {
        // 1. Add multiple items to cart
        String addToCartRequest1 = """
            {
                "bookId": %d,
                "quantity": 2
            }
            """.formatted(testBook.getId());

        mockMvc.perform(post("/api/cart")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addToCartRequest1))
                .andExpect(status().isOk());

        // 2. Verify cart has items
        mockMvc.perform(get("/api/cart").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        // 3. Clear cart
        mockMvc.perform(delete("/api/cart").session(session))
                .andExpect(status().isOk());

        // 4. Verify cart is empty
        mockMvc.perform(get("/api/cart").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Should handle book search functionality")
    @Transactional
    void shouldHandleBookSearchFunctionality() throws Exception {
        // 1. Create additional test books
        Book book2 = new Book("Java Programming", "John Doe", "123-456-789", 45.99, "Learn Java", 5);
        Book book3 = new Book("Python Basics", "Jane Smith", "987-654-321", 35.99, "Learn Python", 8);
        bookRepository.save(book2);
        bookRepository.save(book3);

        // 2. Search by title
        mockMvc.perform(get("/api/books/search").param("query", "Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Java Programming")));

        // 3. Search by author
        mockMvc.perform(get("/api/books/search").param("query", "Jane"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].author", is("Jane Smith")));

        // 4. Search with no results
        mockMvc.perform(get("/api/books/search").param("query", "NonExistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Should handle order retrieval by customer email")
    @Transactional
    void shouldHandleOrderRetrievalByCustomerEmail() throws Exception {
        // 1. Create and place an order
        String addToCartRequest = """
            {
                "bookId": %d,
                "quantity": 1
            }
            """.formatted(testBook.getId());

        mockMvc.perform(post("/api/cart")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addToCartRequest))
                .andExpect(status().isOk());

        String orderRequest = """
            {
                "customerName": "Test Customer",
                "customerEmail": "customer@test.com",
                "customerAddress": "123 Test Street"
            }
            """;

        mockMvc.perform(post("/api/orders")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderRequest))
                .andExpect(status().isOk());

        // 2. Retrieve orders by customer email
        mockMvc.perform(get("/api/orders/customer/customer@test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].customerEmail", is("customer@test.com")));
    }
}
