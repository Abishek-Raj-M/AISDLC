# QA Test Case Generator - BDD Gherkin Format

## Test Cases for Simple Digital Bookstore

### Epic 1: Book Management

#### Feature: View Available Books (US001)
**Story:** As a customer, I want to view a list of available books so that I can browse and select books to purchase.

```gherkin
Feature: View Available Books
  As a customer
  I want to view a list of available books
  So that I can browse and select books to purchase

  Background:
    Given the bookstore application is running
    And there are books available in the inventory

  Scenario: Display book list with required information
    Given I am on the homepage
    When the page loads
    Then I should see a list of available books
    And each book should display title, author, price, and availability status
    And book cover images should be displayed if available

  Scenario: Pagination functionality for large catalogs
    Given there are more than 10 books in the catalog
    When I am on the homepage
    Then I should see pagination controls
    And I should be able to navigate between pages
    And each page should display a maximum of 10 books
```

#### Feature: Search Books (US002)
**Story:** As a customer, I want to search for books by title or author so that I can quickly find specific books.

```gherkin
Feature: Search Books
  As a customer
  I want to search for books by title or author
  So that I can quickly find specific books

  Background:
    Given the bookstore application is running
    And there are multiple books in the catalog

  Scenario: Search books by title
    Given I am on the homepage
    When I enter "Harry Potter" in the search field
    And I click the search button
    Then I should see books with "Harry Potter" in the title
    And the search results should be relevant to my query

  Scenario: Search books by author
    Given I am on the homepage
    When I enter "J.K. Rowling" in the search field
    And I click the search button
    Then I should see all books by "J.K. Rowling"
    And the search results should display author information

  Scenario: Search with partial matches
    Given I am on the homepage
    When I enter "Harry" in the search field
    And I click the search button
    Then I should see books containing "Harry" in the title

  Scenario: Search with no results
    Given I am on the homepage
    When I enter "NonExistentBook123" in the search field
    And I click the search button
    Then I should see a "No results found" message
    And no books should be displayed
```

#### Feature: View Book Details (US003)
**Story:** As a customer, I want to view detailed information about a book so that I can make an informed purchase decision.

```gherkin
Feature: View Book Details
  As a customer
  I want to view detailed information about a book
  So that I can make an informed purchase decision

  Background:
    Given the bookstore application is running
    And there are books available in the catalog

  Scenario: View comprehensive book details
    Given I am on the homepage
    When I click on a book from the catalog
    Then I should see the book details page
    And I should see the book description
    And I should see the ISBN number
    And I should see the publication date
    And I should see customer reviews and ratings section
    And I should see the current stock availability

  Scenario: View book details with reviews
    Given I am on a book details page
    And the book has customer reviews
    Then I should see customer ratings
    And I should see individual review comments
    And I should see the average rating score
```

### Epic 2: Shopping Cart Management

#### Feature: Add Books to Cart (US004)
**Story:** As a customer, I want to add books to my shopping cart so that I can purchase multiple books together.

```gherkin
Feature: Add Books to Cart
  As a customer
  I want to add books to my shopping cart
  So that I can purchase multiple books together

  Background:
    Given the bookstore application is running
    And I am on the homepage
    And my cart is empty

  Scenario: Add a book to empty cart
    Given I see a book I want to purchase
    When I click the "Add to Cart" button
    Then the book should be added to my cart
    And the cart count should show "1"
    And I should see a confirmation message

  Scenario: Add same book multiple times
    Given I have added a book to my cart
    When I click "Add to Cart" for the same book again
    Then the quantity of that book should increase to 2
    And the cart count should update accordingly

  Scenario: Add different books to cart
    Given I have one book in my cart
    When I add a different book to my cart
    Then I should have 2 different books in my cart
    And the cart count should show "2"
```

#### Feature: View Shopping Cart (US005)
**Story:** As a customer, I want to view my shopping cart so that I can review my selected books before checkout.

```gherkin
Feature: View Shopping Cart
  As a customer
  I want to view my shopping cart
  So that I can review my selected books before checkout

  Background:
    Given the bookstore application is running
    And I have books in my shopping cart

  Scenario: View cart with multiple items
    Given I have 3 different books in my cart
    When I click on the cart icon
    Then I should see the shopping cart page
    And I should see all 3 books listed
    And each book should show title, price, and quantity
    And I should see the total cart value

  Scenario: Update quantity from cart view
    Given I am viewing my shopping cart
    And I have a book with quantity 1
    When I change the quantity to 3
    Then the quantity should update to 3
    And the total price should recalculate automatically

  Scenario: View empty cart
    Given my cart is empty
    When I click on the cart icon
    Then I should see an "empty cart" message
    And the total should show $0.00
```

#### Feature: Remove Items from Cart (US006)
**Story:** As a customer, I want to remove items from my cart so that I can manage my purchases.

```gherkin
Feature: Remove Items from Cart
  As a customer
  I want to remove items from my cart
  So that I can manage my purchases

  Background:
    Given the bookstore application is running
    And I have multiple books in my shopping cart

  Scenario: Remove single item from cart
    Given I am viewing my shopping cart
    And I have 3 books in my cart
    When I click "Remove" for one book
    Then that book should be removed from my cart
    And I should have 2 books remaining
    And the total price should update

  Scenario: Remove item with confirmation
    Given I am viewing my shopping cart
    When I click "Remove" for a book
    Then I should see a confirmation dialog
    When I confirm the removal
    Then the book should be removed from my cart

  Scenario: Remove all items from cart
    Given I have books in my cart
    When I remove all items one by one
    Then my cart should be empty
    And I should see an "empty cart" message
```

### Epic 3: Order Processing

#### Feature: Place Order (US007)
**Story:** As a customer, I want to place an order for books in my cart so that I can complete my purchase.

```gherkin
Feature: Place Order
  As a customer
  I want to place an order for books in my cart
  So that I can complete my purchase

  Background:
    Given the bookstore application is running
    And I have books in my shopping cart

  Scenario: Complete order with valid information
    Given I am viewing my shopping cart
    When I click "Proceed to Checkout"
    Then I should see the checkout form
    When I fill in my name as "John Doe"
    And I fill in my email as "john@example.com"
    And I fill in my address as "123 Main St, City, State"
    And I click "Place Order"
    Then I should see an order confirmation page
    And I should receive an order confirmation number
    And my cart should be cleared

  Scenario: Order validation with missing information
    Given I am on the checkout page
    When I leave the name field empty
    And I click "Place Order"
    Then I should see a validation error message
    And the order should not be processed

  Scenario: Order confirmation details
    Given I have successfully placed an order
    Then the confirmation page should show my order details
    And it should display the books I purchased
    And it should show the total amount paid
    And it should provide an estimated delivery date
```

#### Feature: View Order History (US008)
**Story:** As a customer, I want to view my order history so that I can track my past purchases.

```gherkin
Feature: View Order History
  As a customer
  I want to view my order history
  So that I can track my past purchases

  Background:
    Given the bookstore application is running
    And I have placed orders in the past

  Scenario: View list of previous orders
    Given I am logged in as a customer
    When I navigate to "Order History"
    Then I should see a list of my previous orders
    And each order should show the order date
    And each order should show the order status
    And each order should show the total amount

  Scenario: View specific order details
    Given I am viewing my order history
    When I click on a specific order
    Then I should see the detailed order information
    And I should see all books purchased in that order
    And I should see the delivery address
    And I should see the order status progression
```

### Epic 4: Admin Functions

#### Feature: Manage Book Inventory (US009)
**Story:** As an admin, I want to manage book inventory so that I can add, update, or remove books from the catalog.

```gherkin
Feature: Manage Book Inventory
  As an admin
  I want to manage book inventory
  So that I can add, update, or remove books from the catalog

  Background:
    Given the bookstore application is running
    And I am logged in as an admin

  Scenario: Add new book to inventory
    Given I am on the admin dashboard
    When I click "Add New Book"
    And I fill in the book title as "New Adventure"
    And I fill in the author as "Jane Author"
    And I fill in the price as "19.99"
    And I fill in the stock quantity as "50"
    And I click "Save Book"
    Then the book should be added to the catalog
    And customers should be able to see the new book

  Scenario: Update existing book information
    Given I am on the admin dashboard
    And there is an existing book in the catalog
    When I click "Edit" for that book
    And I change the price to "24.99"
    And I click "Update Book"
    Then the book price should be updated
    And customers should see the new price

  Scenario: Remove book from catalog
    Given I am on the admin dashboard
    And there is a book I want to remove
    When I click "Delete" for that book
    And I confirm the deletion
    Then the book should be removed from the catalog
    And customers should no longer see that book

  Scenario: Update stock quantity
    Given I am editing a book's information
    When I change the stock quantity to "0"
    And I save the changes
    Then the book should show as "Out of Stock" to customers
    And customers should not be able to add it to cart
```

#### Feature: View Sales Reports (US010)
**Story:** As an admin, I want to view sales reports so that I can track business performance.

```gherkin
Feature: View Sales Reports
  As an admin
  I want to view sales reports
  So that I can track business performance

  Background:
    Given the bookstore application is running
    And I am logged in as an admin
    And there is historical sales data available

  Scenario: Generate sales report by date range
    Given I am on the admin dashboard
    When I click "Sales Reports"
    And I select start date as "2025-01-01"
    And I select end date as "2025-01-31"
    And I click "Generate Report"
    Then I should see total sales for January 2025
    And I should see the number of orders processed
    And I should see revenue breakdown by day

  Scenario: View best-selling books report
    Given I am viewing the sales reports
    When I click "Best Sellers"
    Then I should see a list of top-selling books
    And each book should show total copies sold
    And books should be ranked by sales volume

  Scenario: View basic analytics dashboard
    Given I am on the sales reports page
    Then I should see key performance indicators
    And I should see total revenue
    And I should see total number of customers
    And I should see average order value
    And I should see sales trends in graphical format
```

### Cross-Functional Test Scenarios

#### Feature: Performance Testing
```gherkin
Feature: Application Performance
  As a user
  I want the application to perform well
  So that I have a smooth browsing experience

  Scenario: Page load performance
    Given the bookstore application is running
    When I navigate to the homepage
    Then the page should load within 3 seconds
    And all images should load within 5 seconds

  Scenario: Search performance
    Given I am on the homepage
    When I perform a search
    Then search results should appear within 2 seconds
    And the page should remain responsive
```

#### Feature: Browser Compatibility
```gherkin
Feature: Cross-Browser Compatibility
  As a user
  I want the application to work on different browsers
  So that I can use my preferred browser

  Scenario Outline: Test functionality across browsers
    Given I am using <browser>
    When I perform basic bookstore operations
    Then all features should work correctly
    And the UI should display properly

    Examples:
      | browser |
      | Chrome  |
      | Firefox |
      | Safari  |
      | Edge    |
```

#### Feature: Mobile Responsiveness
```gherkin
Feature: Mobile Responsiveness
  As a mobile user
  I want the application to work well on my mobile device
  So that I can shop for books on the go

  Scenario: Mobile navigation
    Given I am accessing the site on a mobile device
    When I navigate through different pages
    Then the layout should be mobile-friendly
    And all buttons should be easily clickable
    And text should be readable without zooming

  Scenario: Mobile cart functionality
    Given I am on a mobile device
    When I add books to cart and proceed to checkout
    Then the cart functionality should work smoothly
    And the checkout process should be mobile-optimized
```
