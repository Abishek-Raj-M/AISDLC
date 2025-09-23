Feature: Home Page Search and Cart Functionality

  Background:
    Given the bookstore application is running
    And I am on the home page

  Scenario Outline: Search for existing books and verify they are displayed
    When I search for the book "<bookTitle>"
    Then I should see the book title "<bookTitle>" displayed in search results

    Examples:
      | bookTitle              |
      | The Great Gatsby       |
      | To Kill a Mockingbird  |
      | 1984                   |
      | Pride and Prejudice    |
      | The Catcher in the Rye |

  Scenario: Add books to cart and verify they appear in cart page
    When I search for the book "The Great Gatsby"
    And I add the first book to cart
    When I search for the book "1984"
    And I add the first book to cart
    When I search for the book "Pride and Prejudice"
    And I add the first book to cart
    When I navigate to the cart page
    Then I should see "The Great Gatsby" in the cart
    And I should see "1984" in the cart
    And I should see "Pride and Prejudice" in the cart
