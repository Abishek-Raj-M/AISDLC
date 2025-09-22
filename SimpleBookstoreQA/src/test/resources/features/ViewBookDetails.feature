Feature: View Book Details
  As a customer
  I want to view detailed information about a book
  So that I can make an informed purchase decision

  Background:
    Given the bookstore application is running
    And there are books available in the catalog

  @smoke @regression
  Scenario: View comprehensive book details
    Given I am on the homepage
    When I click on a book from the catalog
    Then I should see the book details page
    And I should see the book description
    And I should see the ISBN number
    And I should see the publication date
    And I should see customer reviews and ratings section
    And I should see the current stock availability

  @regression
  Scenario: View "The Great Gatsby" book details
    Given I am on the homepage
    When I click on "The Great Gatsby"
    Then I should see the book details page
    And I should see the title "The Great Gatsby"
    And I should see the author "F. Scott Fitzgerald"
    And I should see the price "$12.99"
    And I should see the description "A classic American novel about the Jazz Age."
    And I should see "Stock: 9 available"

  @regression
  Scenario: View "1984" book details
    Given I am on the homepage
    When I click on "1984"
    Then I should see the book details page
    And I should see the title "1984"
    And I should see the author "George Orwell"
    And I should see the price "$13.99"
    And I should see the description "A dystopian social science fiction novel."
    And I should see "Stock: 15 available"

  @regression
  Scenario: View book details with reviews
    Given I am on a book details page
    And the book has customer reviews
    Then I should see customer ratings
    And I should see individual review comments
    And I should see the average rating score

  @regression
  Scenario: Navigate back from book details
    Given I am viewing book details for "To Kill a Mockingbird"
    When I click the back button or homepage link
    Then I should return to the homepage
    And I should see the full list of available books

  @regression
  Scenario: Add to cart from book details page
    Given I am viewing book details for "Pride and Prejudice"
    When I click "Add to Cart" on the details page
    Then the book should be added to my cart
    And I should see a confirmation message
    And the cart count should update

  @regression
  Scenario: View stock status on details page
    Given I am viewing book details for "The Catcher in the Rye"
    Then I should see "Stock: 6 available"
    And the "Add to Cart" button should be enabled

  @edge-case @regression
  Scenario: View details for low stock book
    Given I am viewing book details for "The Catcher in the Rye"
    And the book has low stock (6 available)
    Then I should see a low stock warning or indication
    And the stock information should be clearly displayed
