package com.bookstore.controller;

import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@DisplayName("BookController Tests")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private Book testBook;
    private List<Book> testBooks;

    @BeforeEach
    void setUp() {
        testBook = new Book("Test Book", "Test Author", "123-456-789", 19.99, "Test Description", 10);
        testBook.setId(1L);

        Book testBook2 = new Book("Another Book", "Another Author", "987-654-321", 29.99, "Another Description", 5);
        testBook2.setId(2L);

        testBooks = Arrays.asList(testBook, testBook2);
    }

    @Test
    @DisplayName("GET /api/books should return all books")
    void shouldReturnAllBooks_whenGetAllBooks() throws Exception {
        // Arrange
        when(bookService.getAllBooks()).thenReturn(testBooks);

        // Act & Assert
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Test Book")))
                .andExpect(jsonPath("$[0].author", is("Test Author")))
                .andExpect(jsonPath("$[0].price", is(19.99)))
                .andExpect(jsonPath("$[1].title", is("Another Book")));

        verify(bookService).getAllBooks();
    }

    @Test
    @DisplayName("GET /api/books/{id} should return specific book")
    void shouldReturnSpecificBook_whenValidIdProvided() throws Exception {
        // Arrange
        when(bookService.getBookById(1L)).thenReturn(Optional.of(testBook));

        // Act & Assert
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Book")))
                .andExpect(jsonPath("$.author", is("Test Author")))
                .andExpect(jsonPath("$.isbn", is("123-456-789")))
                .andExpect(jsonPath("$.price", is(19.99)));

        verify(bookService).getBookById(1L);
    }

    @Test
    @DisplayName("GET /api/books/{id} should return 404 for non-existent book")
    void shouldReturn404_whenBookNotFound() throws Exception {
        // Arrange
        when(bookService.getBookById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/books/999"))
                .andExpect(status().isNotFound());

        verify(bookService).getBookById(999L);
    }

    @Test
    @DisplayName("GET /api/books/search should return filtered books")
    void shouldReturnFilteredBooks_whenSearchQueryProvided() throws Exception {
        // Arrange
        List<Book> filteredBooks = Arrays.asList(testBook);
        when(bookService.searchBooks("Test")).thenReturn(filteredBooks);

        // Act & Assert
        mockMvc.perform(get("/api/books/search").param("query", "Test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Test Book")));

        verify(bookService).searchBooks("Test");
    }

    @Test
    @DisplayName("GET /api/books/search should handle empty query")
    void shouldHandleEmptyQuery_whenSearchWithoutQuery() throws Exception {
        // Arrange
        when(bookService.searchBooks(null)).thenReturn(testBooks);

        // Act & Assert
        mockMvc.perform(get("/api/books/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));

        verify(bookService).searchBooks(null);
    }

    @Test
    @DisplayName("GET /api/books/available should return only available books")
    void shouldReturnOnlyAvailableBooks_whenGetAvailableBooks() throws Exception {
        // Arrange
        when(bookService.getAvailableBooks()).thenReturn(testBooks);

        // Act & Assert
        mockMvc.perform(get("/api/books/available"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));

        verify(bookService).getAvailableBooks();
    }

    @Test
    @DisplayName("POST /api/books should create new book")
    void shouldCreateNewBook_whenValidBookProvided() throws Exception {
        // Arrange
        Book newBook = new Book("New Book", "New Author", "999-888-777", 25.99, "New Description", 8);
        when(bookService.saveBook(any(Book.class))).thenReturn(newBook);

        // Act & Assert
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("New Book")))
                .andExpect(jsonPath("$.author", is("New Author")));

        verify(bookService).saveBook(any(Book.class));
    }

    @Test
    @DisplayName("PUT /api/books/{id} should update existing book")
    void shouldUpdateExistingBook_whenValidBookProvided() throws Exception {
        // Arrange
        Book updatedBook = new Book("Updated Book", "Updated Author", "123-456-789", 24.99, "Updated Description", 15);
        updatedBook.setId(1L);

        when(bookService.getBookById(1L)).thenReturn(Optional.of(testBook));
        when(bookService.saveBook(any(Book.class))).thenReturn(updatedBook);

        // Act & Assert
        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("Updated Book")))
                .andExpect(jsonPath("$.author", is("Updated Author")));

        verify(bookService).getBookById(1L);
        verify(bookService).saveBook(any(Book.class));
    }

    @Test
    @DisplayName("PUT /api/books/{id} should return 404 for non-existent book")
    void shouldReturn404_whenUpdatingNonExistentBook() throws Exception {
        // Arrange
        Book updatedBook = new Book("Updated Book", "Updated Author", "123-456-789", 24.99, "Updated Description", 15);
        when(bookService.getBookById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/api/books/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isNotFound());

        verify(bookService).getBookById(999L);
        verify(bookService, never()).saveBook(any());
    }

    @Test
    @DisplayName("DELETE /api/books/{id} should delete existing book")
    void shouldDeleteExistingBook_whenValidIdProvided() throws Exception {
        // Arrange
        when(bookService.getBookById(1L)).thenReturn(Optional.of(testBook));

        // Act & Assert
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isOk());

        verify(bookService).getBookById(1L);
        verify(bookService).deleteBook(1L);
    }

    @Test
    @DisplayName("DELETE /api/books/{id} should return 404 for non-existent book")
    void shouldReturn404_whenDeletingNonExistentBook() throws Exception {
        // Arrange
        when(bookService.getBookById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/books/999"))
                .andExpect(status().isNotFound());

        verify(bookService).getBookById(999L);
        verify(bookService, never()).deleteBook(anyLong());
    }

    @Test
    @DisplayName("POST /api/books should handle invalid JSON")
    void shouldHandleInvalidJson_whenCreatingBook() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ invalid json }"))
                .andExpect(status().isBadRequest());

        verify(bookService, never()).saveBook(any());
    }
}
