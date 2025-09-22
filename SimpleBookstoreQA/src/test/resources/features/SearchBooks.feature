Feature: Search Books
  As a customer
  I want to search for books by title or author
  So that I can quickly find specific books

  Background:
    Given the bookstore application is running
    And there are multiple books in the catalog

  @smoke @regression
  Scenario: Search books by title
    Given I am on the homepage
    When I enter "Gatsby" in the search field
    And I click the search button
    Then I should see books with "Gatsby" in the title
    And I should see "The Great Gatsby" by "F. Scott Fitzgerald"
    And the search results should be relevant to my query

  @smoke @regression
  Scenario: Search books by author
    Given I am on the homepage
    When I enter "George Orwell" in the search field
    And I click the search button
    Then I should see all books by "George Orwell"
    And I should see "1984" in the search results
    And the search results should display author information

  @regression
  Scenario: Search with partial matches
    Given I am on the homepage
    When I enter "Pride" in the search field
    And I click the search button
    Then I should see books containing "Pride" in the title
    And I should see "Pride and Prejudice" by "Jane Austen"

  @regression
  Scenario: Search books by partial author name
    Given I am on the homepage
    When I enter "Harper" in the search field
    And I click the search button
    Then I should see books by authors containing "Harper"
    And I should see "To Kill a Mockingbird" by "Harper Lee"

  @negative @regression
  Scenario: Search with no results
    Given I am on the homepage
    When I enter "NonExistentBook123" in the search field
    And I click the search button
    Then I should see a "No results found" message
    And no books should be displayed

  @edge-case @regression
  Scenario: Search with empty field
    Given I am on the homepage
    When I leave the search field empty
    And I click the search button
    Then I should see all available books

  @regression
  Scenario: Case insensitive search
    Given I am on the homepage
    When I enter "CATCHER" in the search field
    And I click the search button
    Then I should see "The Catcher in the Rye" by "J.D. Salinger"
    And the search should be case insensitive
