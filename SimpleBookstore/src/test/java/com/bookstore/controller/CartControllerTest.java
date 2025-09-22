package com.bookstore.controller;

import com.bookstore.model.CartItem;
import com.bookstore.service.CartService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
@DisplayName("CartController Tests")
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    private CartItem testCartItem;
    private MockHttpSession mockSession;

    @BeforeEach
    void setUp() {
        testCartItem = new CartItem(1L, 2, "test-session");
        testCartItem.setId(1L);

        mockSession = new MockHttpSession();
        mockSession.setAttribute("test", "value"); // Ensure session has an ID
    }

    @Test
    @DisplayName("GET /api/cart should return cart items for session")
    void shouldReturnCartItemsForSession_whenGetCart() throws Exception {
        // Arrange
        List<CartItem> cartItems = Arrays.asList(testCartItem);
        when(cartService.getCartItems(any(String.class))).thenReturn(cartItems);

        // Act & Assert
        mockMvc.perform(get("/api/cart").session(mockSession))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].bookId", is(1)))
                .andExpect(jsonPath("$[0].quantity", is(2)));

        verify(cartService).getCartItems(any(String.class));
    }

    @Test
    @DisplayName("POST /api/cart should add item to cart")
    void shouldAddItemToCart_whenValidRequestProvided() throws Exception {
        // Arrange
        CartController.CartRequest request = new CartController.CartRequest();
        request.setBookId(1L);
        request.setQuantity(2);

        when(cartService.addToCart(any(String.class), eq(1L), eq(2))).thenReturn(testCartItem);

        // Act & Assert
        mockMvc.perform(post("/api/cart")
                .session(mockSession)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bookId", is(1)))
                .andExpect(jsonPath("$.quantity", is(2)));

        verify(cartService).addToCart(any(String.class), eq(1L), eq(2));
    }

    @Test
    @DisplayName("PUT /api/cart/{itemId} should update cart item quantity")
    void shouldUpdateCartItemQuantity_whenValidRequestProvided() throws Exception {
        // Arrange
        CartController.CartRequest request = new CartController.CartRequest();
        request.setQuantity(5);

        CartItem updatedItem = new CartItem(1L, 5, "test-session");
        updatedItem.setId(1L);

        when(cartService.updateCartItem(1L, 5)).thenReturn(updatedItem);

        // Act & Assert
        mockMvc.perform(put("/api/cart/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.quantity", is(5)));

        verify(cartService).updateCartItem(1L, 5);
    }

    @Test
    @DisplayName("PUT /api/cart/{itemId} should return 404 for non-existent item")
    void shouldReturn404_whenUpdatingNonExistentCartItem() throws Exception {
        // Arrange
        CartController.CartRequest request = new CartController.CartRequest();
        request.setQuantity(5);

        when(cartService.updateCartItem(999L, 5)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/api/cart/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(cartService).updateCartItem(999L, 5);
    }

    @Test
    @DisplayName("DELETE /api/cart/{itemId} should remove cart item")
    void shouldRemoveCartItem_whenValidIdProvided() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/cart/1"))
                .andExpect(status().isOk());

        verify(cartService).removeCartItem(1L);
    }

    @Test
    @DisplayName("DELETE /api/cart should clear entire cart")
    void shouldClearEntireCart_whenDeleteCart() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/cart").session(mockSession))
                .andExpect(status().isOk());

        verify(cartService).clearCart(any(String.class));
    }

    @Test
    @DisplayName("GET /api/cart/total should return cart total")
    void shouldReturnCartTotal_whenGetCartTotal() throws Exception {
        // Arrange
        when(cartService.calculateCartTotal(any(String.class))).thenReturn(39.98);

        // Act & Assert
        mockMvc.perform(get("/api/cart/total").session(mockSession))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(39.98)));

        verify(cartService).calculateCartTotal(any(String.class));
    }

    @Test
    @DisplayName("POST /api/cart should handle invalid JSON")
    void shouldHandleInvalidJson_whenAddingToCart() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/cart")
                .session(mockSession)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ invalid json }"))
                .andExpect(status().isBadRequest());

        verify(cartService, never()).addToCart(any(), any(), any());
    }

    @Test
    @DisplayName("POST /api/cart should handle missing required fields")
    void shouldHandleMissingFields_whenAddingToCart() throws Exception {
        // Arrange
        CartController.CartRequest request = new CartController.CartRequest();
        // Missing bookId and quantity

        // Act & Assert
        mockMvc.perform(post("/api/cart")
                .session(mockSession)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()); // Controller doesn't validate, service might handle nulls

        verify(cartService).addToCart(any(String.class), eq(null), eq(null));
    }

    @Test
    @DisplayName("GET /api/cart should return empty list for empty cart")
    void shouldReturnEmptyList_whenCartIsEmpty() throws Exception {
        // Arrange
        when(cartService.getCartItems(any(String.class))).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/cart").session(mockSession))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(cartService).getCartItems(any(String.class));
    }
}
