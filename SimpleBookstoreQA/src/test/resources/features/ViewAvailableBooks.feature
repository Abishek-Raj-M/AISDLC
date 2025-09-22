Feature: View Available Books
  As a customer
  I want to view a list of available books
  So that I can browse and select books to purchase

  Background:
    Given the bookstore application is running
    And there are books available in the inventory

  @smoke @regression
  Scenario: Display book list with required information
    Given I am on the homepage
    When the page loads
    Then I should see a list of available books
    And each book should display title, author, price, and availability status
    And book cover images should be displayed if available
    And I should see "The Great Gatsby" by "F. Scott Fitzgerald" priced at "$12.99"
    And I should see "To Kill a Mockingbird" by "Harper Lee" priced at "$14.99"
    And I should see "1984" by "George Orwell" priced at "$13.99"
    And I should see "Pride and Prejudice" by "Jane Austen" priced at "$11.99"
    And I should see "The Catcher in the Rye" by "J.D. Salinger" priced at "$13.49"

  @regression
  Scenario: Verify stock availability display
    Given I am on the homepage
    When the page loads
    Then "The Great Gatsby" should show "9 available"
    And "To Kill a Mockingbird" should show "7 available"
    And "1984" should show "15 available"
    And "Pride and Prejudice" should show "12 available"
    And "The Catcher in the Rye" should show "6 available"

  @regression
  Scenario: Pagination functionality for large catalogs
    Given there are more than 10 books in the catalog
    When I am on the homepage
    Then I should see pagination controls
    And I should be able to navigate between pages
    And each page should display a maximum of 10 books
