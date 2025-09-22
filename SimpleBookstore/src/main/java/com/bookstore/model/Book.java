package com.bookstore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Title is required")
    private String title;

    @Column(nullable = false)
    @NotBlank(message = "Author is required")
    private String author;

    @Column(unique = true)
    private String isbn;

    @Column(nullable = false)
    @NotNull(message = "Price is required")
    private Double price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "stock_quantity")
    @PositiveOrZero(message = "Stock quantity must be zero or positive")
    private Integer stockQuantity = 0;

    // Constructors
    public Book() {}

    public Book(String title, String author, String isbn, Double price, String description, Integer stockQuantity) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
        this.description = description;
        this.stockQuantity = stockQuantity;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
}
