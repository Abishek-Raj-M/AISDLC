package com.bookstore.repository;

import com.bookstore.model.Book;
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
@DisplayName("BookRepository Tests")
class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    private Book testBook1;
    private Book testBook2;
    private Book testBook3;

    @BeforeEach
    void setUp() {
        testBook1 = new Book("Java Programming", "John Doe", "123-456-789", 29.99, "Learn Java programming", 10);
        testBook2 = new Book("Spring Boot Guide", "Jane Smith", "987-654-321", 39.99, "Master Spring Boot", 5);
        testBook3 = new Book("Python Basics", "Bob Johnson", "111-222-333", 19.99, "Python for beginners", 0);

        entityManager.persistAndFlush(testBook1);
        entityManager.persistAndFlush(testBook2);
        entityManager.persistAndFlush(testBook3);
    }

    @Test
    @DisplayName("Should find books by title case insensitive")
    void shouldFindBooksByTitle_whenSearchingCaseInsensitive() {
        // Act
        List<Book> result = bookRepository.searchBooks("java");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Java Programming");
    }

    @Test
    @DisplayName("Should find books by author case insensitive")
    void shouldFindBooksByAuthor_whenSearchingCaseInsensitive() {
        // Act
        List<Book> result = bookRepository.searchBooks("jane");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAuthor()).isEqualTo("Jane Smith");
    }

    @Test
    @DisplayName("Should find multiple books with partial match")
    void shouldFindMultipleBooks_whenPartialMatch() {
        // Act
        List<Book> result = bookRepository.searchBooks("john"); // Should match "John Doe" and "Bob Johnson"

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Book::getAuthor)
                .containsExactlyInAnyOrder("John Doe", "Bob Johnson");
    }

    @Test
    @DisplayName("Should return empty list when no matches found")
    void shouldReturnEmptyList_whenNoMatchesFound() {
        // Act
        List<Book> result = bookRepository.searchBooks("NonExistentBook");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return books with stock greater than specified quantity")
    void shouldReturnBooksWithStock_whenStockGreaterThanQuantity() {
        // Act
        List<Book> result = bookRepository.findByStockQuantityGreaterThan(0);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Book::getTitle)
                .containsExactlyInAnyOrder("Java Programming", "Spring Boot Guide");
    }

    @Test
    @DisplayName("Should return books with stock greater than 5")
    void shouldReturnBooksWithHighStock_whenStockGreaterThanFive() {
        // Act
        List<Book> result = bookRepository.findByStockQuantityGreaterThan(5);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Java Programming");
    }

    @Test
    @DisplayName("Should save book and generate ID")
    void shouldSaveBookAndGenerateId_whenSavingNewBook() {
        // Arrange
        Book newBook = new Book("New Book", "New Author", "444-555-666", 25.99, "New Description", 8);

        // Act
        Book savedBook = bookRepository.save(newBook);

        // Assert
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("New Book");
        assertThat(savedBook.getAuthor()).isEqualTo("New Author");
    }

    @Test
    @DisplayName("Should retrieve saved book by ID")
    void shouldRetrieveSavedBook_whenFindingById() {
        // Act
        Optional<Book> result = bookRepository.findById(testBook1.getId());

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Java Programming");
        assertThat(result.get().getAuthor()).isEqualTo("John Doe");
    }

    @Test
    @DisplayName("Should return empty when book not found by ID")
    void shouldReturnEmpty_whenBookNotFoundById() {
        // Act
        Optional<Book> result = bookRepository.findById(999L);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should delete book from database")
    void shouldDeleteBook_whenDeleteCalled() {
        // Arrange
        Long bookId = testBook1.getId();

        // Act
        bookRepository.deleteById(bookId);

        // Assert
        Optional<Book> result = bookRepository.findById(bookId);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should update book when saving existing book")
    void shouldUpdateBook_whenSavingExistingBook() {
        // Arrange
        testBook1.setPrice(35.99);
        testBook1.setStockQuantity(15);

        // Act
        Book updatedBook = bookRepository.save(testBook1);

        // Assert
        assertThat(updatedBook.getPrice()).isEqualTo(35.99);
        assertThat(updatedBook.getStockQuantity()).isEqualTo(15);
    }

    @Test
    @DisplayName("Should enforce unique ISBN constraint")
    void shouldEnforceUniqueIsbn_whenSavingDuplicateIsbn() {
        // Arrange
        Book duplicateIsbnBook = new Book("Duplicate Book", "Another Author", "123-456-789", 45.99, "Duplicate ISBN", 3);

        // Act & Assert
        try {
            bookRepository.saveAndFlush(duplicateIsbnBook);
            // If we reach here, the test should fail
            assertThat(false).as("Expected constraint violation").isTrue();
        } catch (Exception e) {
            // Expected exception due to unique constraint
            assertThat(e).isNotNull();
        }
    }
}
