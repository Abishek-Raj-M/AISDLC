package com.bookstore.config;

import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize sample books
        if (bookRepository.count() == 0) {
            bookRepository.save(new Book("The Great Gatsby", "F. Scott Fitzgerald", "978-0-7432-7356-5", 12.99, "A classic American novel about the Jazz Age.", 10));
            bookRepository.save(new Book("To Kill a Mockingbird", "Harper Lee", "978-0-06-112008-4", 14.99, "A gripping tale of racial injustice and childhood innocence.", 8));
            bookRepository.save(new Book("1984", "George Orwell", "978-0-452-28423-4", 13.99, "A dystopian social science fiction novel.", 15));
            bookRepository.save(new Book("Pride and Prejudice", "Jane Austen", "978-0-14-143951-8", 11.99, "A romantic novel of manners.", 12));
            bookRepository.save(new Book("The Catcher in the Rye", "J.D. Salinger", "978-0-316-76948-0", 13.49, "A controversial novel about teenage rebellion.", 6));
        }
    }
}
