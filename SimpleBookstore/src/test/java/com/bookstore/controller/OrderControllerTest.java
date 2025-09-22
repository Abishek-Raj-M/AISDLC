package com.bookstore.controller;

import com.bookstore.model.Order;
import com.bookstore.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@DisplayName("OrderController Tests")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private Order testOrder;
    private MockHttpSession mockSession;

    @BeforeEach
    void setUp() {
        testOrder = new Order("John Doe", "john@example.com", "123 Test Street", 39.98);
        testOrder.setId(1L);
        testOrder.setStatus("CONFIRMED");

        mockSession = new MockHttpSession();
    }

    @Test
    @DisplayName("POST /api/orders should create order successfully")
    void shouldCreateOrderSuccessfully_whenValidRequestProvided() throws Exception {
        // Arrange
        OrderController.OrderRequest request = new OrderController.OrderRequest();
        request.setCustomerName("John Doe");
        request.setCustomerEmail("john@example.com");
        request.setCustomerAddress("123 Test Street");

        when(orderService.createOrder(any(String.class), eq("John Doe"), eq("john@example.com"), eq("123 Test Street")))
                .thenReturn(testOrder);

        // Act & Assert
        mockMvc.perform(post("/api/orders")
                .session(mockSession)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.customerName", is("John Doe")))
                .andExpect(jsonPath("$.customerEmail", is("john@example.com")))
                .andExpect(jsonPath("$.status", is("CONFIRMED")));

        verify(orderService).createOrder(any(String.class), eq("John Doe"), eq("john@example.com"), eq("123 Test Street"));
    }

    @Test
    @DisplayName("POST /api/orders should handle empty cart error")
    void shouldHandleEmptyCartError_whenCreatingOrder() throws Exception {
        // Arrange
        OrderController.OrderRequest request = new OrderController.OrderRequest();
        request.setCustomerName("John Doe");
        request.setCustomerEmail("john@example.com");
        request.setCustomerAddress("123 Test Street");

        when(orderService.createOrder(any(String.class), eq("John Doe"), eq("john@example.com"), eq("123 Test Street")))
                .thenThrow(new RuntimeException("Cart is empty"));

        // Act & Assert
        mockMvc.perform(post("/api/orders")
                .session(mockSession)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(orderService).createOrder(any(String.class), eq("John Doe"), eq("john@example.com"), eq("123 Test Street"));
    }

    @Test
    @DisplayName("POST /api/orders should handle insufficient stock error")
    void shouldHandleInsufficientStockError_whenCreatingOrder() throws Exception {
        // Arrange
        OrderController.OrderRequest request = new OrderController.OrderRequest();
        request.setCustomerName("Jane Doe");
        request.setCustomerEmail("jane@example.com");
        request.setCustomerAddress("456 Test Avenue");

        when(orderService.createOrder(any(String.class), eq("Jane Doe"), eq("jane@example.com"), eq("456 Test Avenue")))
                .thenThrow(new RuntimeException("Insufficient stock for book: Test Book"));

        // Act & Assert
        mockMvc.perform(post("/api/orders")
                .session(mockSession)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(orderService).createOrder(any(String.class), eq("Jane Doe"), eq("jane@example.com"), eq("456 Test Avenue"));
    }

    @Test
    @DisplayName("GET /api/orders should return all orders")
    void shouldReturnAllOrders_whenGetAllOrders() throws Exception {
        // Arrange
        List<Order> orders = Arrays.asList(testOrder);
        when(orderService.getAllOrders()).thenReturn(orders);

        // Act & Assert
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].customerName", is("John Doe")));

        verify(orderService).getAllOrders();
    }

    @Test
    @DisplayName("GET /api/orders/{id} should return specific order")
    void shouldReturnSpecificOrder_whenValidIdProvided() throws Exception {
        // Arrange
        when(orderService.getOrderById(1L)).thenReturn(Optional.of(testOrder));

        // Act & Assert
        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.customerName", is("John Doe")))
                .andExpect(jsonPath("$.totalAmount", is(39.98)));

        verify(orderService).getOrderById(1L);
    }

    @Test
    @DisplayName("GET /api/orders/{id} should return 404 for non-existent order")
    void shouldReturn404_whenOrderNotFound() throws Exception {
        // Arrange
        when(orderService.getOrderById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/orders/999"))
                .andExpect(status().isNotFound());

        verify(orderService).getOrderById(999L);
    }

    @Test
    @DisplayName("GET /api/orders/customer/{email} should return customer orders")
    void shouldReturnCustomerOrders_whenEmailProvided() throws Exception {
        // Arrange
        List<Order> customerOrders = Arrays.asList(testOrder);
        when(orderService.getOrdersByCustomerEmail("john@example.com")).thenReturn(customerOrders);

        // Act & Assert
        mockMvc.perform(get("/api/orders/customer/john@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].customerEmail", is("john@example.com")));

        verify(orderService).getOrdersByCustomerEmail("john@example.com");
    }

    @Test
    @DisplayName("GET /api/orders/status/{status} should return orders by status")
    void shouldReturnOrdersByStatus_whenStatusProvided() throws Exception {
        // Arrange
        List<Order> statusOrders = Arrays.asList(testOrder);
        when(orderService.getOrdersByStatus("CONFIRMED")).thenReturn(statusOrders);

        // Act & Assert
        mockMvc.perform(get("/api/orders/status/CONFIRMED"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status", is("CONFIRMED")));

        verify(orderService).getOrdersByStatus("CONFIRMED");
    }

    @Test
    @DisplayName("POST /api/orders should handle invalid JSON")
    void shouldHandleInvalidJson_whenCreatingOrder() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/orders")
                .session(mockSession)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ invalid json }"))
                .andExpect(status().isBadRequest());

        verify(orderService, never()).createOrder(any(), any(), any(), any());
    }

    @Test
    @DisplayName("POST /api/orders should handle missing required fields")
    void shouldHandleMissingFields_whenCreatingOrder() throws Exception {
        // Arrange
        OrderController.OrderRequest request = new OrderController.OrderRequest();
        // Missing required fields

        // Act & Assert
        mockMvc.perform(post("/api/orders")
                .session(mockSession)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()); // Controller doesn't validate, service might handle nulls

        verify(orderService).createOrder(any(String.class), eq(null), eq(null), eq(null));
    }
}
