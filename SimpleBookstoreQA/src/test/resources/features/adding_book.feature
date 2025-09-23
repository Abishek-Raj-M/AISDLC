Feature: Adding a book via admin page

  Background:
    Given the bookstore application is running

  Scenario: Admin adds a new book and verifies it appears on the home page
    Given I am on the admin page
    When I add a new book with the following details:
      | title       | Test Book Title     |
      | author      | Test Author         |
      | isbn        | 1234567890123      |
      | price       | 29.99              |
      | description | A test book description |
      | stock       | 10                 |
    And I navigate to the home page
    Then I should see the book title "Test Book Title" displayed on the home page

  Scenario: Admin adds multiple books and verifies they appear on home page
    Given I am on the admin page
    When I add a new book with title "First Book" and author "First Author" and isbn "1111111111111" and price "19.99" and description "First test book" and stock "5"
    And I add a new book with title "Second Book" and author "Second Author" and isbn "2222222222222" and price "24.99" and description "Second test book" and stock "8"
    And I navigate to the home page
    Then I should see the book title "First Book" displayed on the home page
    And I should see the book title "Second Book" displayed on the home page
