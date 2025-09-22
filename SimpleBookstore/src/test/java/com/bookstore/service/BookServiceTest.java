package com.bookstore.service;

import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookService Tests")
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

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
    @DisplayName("Should return all books from repository")
    void shouldReturnAllBooks_whenGetAllBooksCalled() {
        // Arrange
        when(bookRepository.findAll()).thenReturn(testBooks);

        // Act
        List<Book> result = bookService.getAllBooks();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(testBooks);
        verify(bookRepository).findAll();
    }

    @Test
    @DisplayName("Should return book when valid ID provided")
    void shouldReturnBook_whenValidIdProvided() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        // Act
        Optional<Book> result = bookService.getBookById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Test Book");
        verify(bookRepository).findById(1L);
    }

    @Test
    @DisplayName("Should return empty when book not found")
    void shouldReturnEmpty_whenBookNotFound() {
        // Arrange
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Book> result = bookService.getBookById(999L);

        // Assert
        assertThat(result).isEmpty();
        verify(bookRepository).findById(999L);
    }

    @Test
    @DisplayName("Should return filtered results when search query provided")
    void shouldReturnFilteredResults_whenSearchQueryProvided() {
        // Arrange
        List<Book> filteredBooks = Arrays.asList(testBook);
        when(bookRepository.searchBooks("Test")).thenReturn(filteredBooks);

        // Act
        List<Book> result = bookService.searchBooks("Test");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Book");
        verify(bookRepository).searchBooks("Test");
    }

    @Test
    @DisplayName("Should return all books when empty search query provided")
    void shouldReturnAllBooks_whenEmptySearchQuery() {
        // Arrange
        when(bookRepository.findAll()).thenReturn(testBooks);

        // Act
        List<Book> result = bookService.searchBooks("");

        // Assert
        assertThat(result).hasSize(2);
        verify(bookRepository).findAll();
        verify(bookRepository, never()).searchBooks(any());
    }

    @Test
    @DisplayName("Should return all books when null search query provided")
    void shouldReturnAllBooks_whenNullSearchQuery() {
        // Arrange
        when(bookRepository.findAll()).thenReturn(testBooks);

        // Act
        List<Book> result = bookService.searchBooks(null);

        // Assert
        assertThat(result).hasSize(2);
        verify(bookRepository).findAll();
        verify(bookRepository, never()).searchBooks(any());
    }

    @Test
    @DisplayName("Should return books with stock greater than zero")
    void shouldReturnAvailableBooks_whenGetAvailableBooksCalled() {
        // Arrange
        when(bookRepository.findByStockQuantityGreaterThan(0)).thenReturn(testBooks);

        // Act
        List<Book> result = bookService.getAvailableBooks();

        // Assert
        assertThat(result).hasSize(2);
        verify(bookRepository).findByStockQuantityGreaterThan(0);
    }

    @Test
    @DisplayName("Should save and return book")
    void shouldSaveAndReturnBook_whenSaveBookCalled() {
        // Arrange
        when(bookRepository.save(testBook)).thenReturn(testBook);

        // Act
        Book result = bookService.saveBook(testBook);

        // Assert
        assertThat(result).isEqualTo(testBook);
        verify(bookRepository).save(testBook);
    }

    @Test
    @DisplayName("Should call repository delete when delete book called")
    void shouldCallRepositoryDelete_whenDeleteBookCalled() {
        // Act
        bookService.deleteBook(1L);

        // Assert
        verify(bookRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should reduce stock when sufficient stock available")
    void shouldReduceStock_whenSufficientStockAvailable() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        // Act
        boolean result = bookService.updateStock(1L, 3);

        // Assert
        assertThat(result).isTrue();
        assertThat(testBook.getStockQuantity()).isEqualTo(7);
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(testBook);
    }

    @Test
    @DisplayName("Should return false when insufficient stock")
    void shouldReturnFalse_whenInsufficientStock() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        // Act
        boolean result = bookService.updateStock(1L, 15);

        // Assert
        assertThat(result).isFalse();
        assertThat(testBook.getStockQuantity()).isEqualTo(10); // Stock unchanged
        verify(bookRepository).findById(1L);
        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return false when book not found")
    void shouldReturnFalse_whenBookNotFoundForStockUpdate() {
        // Arrange
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        boolean result = bookService.updateStock(999L, 5);

        // Assert
        assertThat(result).isFalse();
        verify(bookRepository).findById(999L);
        verify(bookRepository, never()).save(any());
    }
}
