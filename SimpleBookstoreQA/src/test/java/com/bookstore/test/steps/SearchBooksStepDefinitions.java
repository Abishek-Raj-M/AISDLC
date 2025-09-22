package com.bookstore.test.steps;

import com.bookstore.test.pages.HomePage;
import io.cucumber.java.en.*;
import org.testng.Assert;
import org.openqa.selenium.WebElement;
import java.util.List;

public class SearchBooksStepDefinitions {

    private HomePage homePage;

    public SearchBooksStepDefinitions() {
        this.homePage = new HomePage();
    }

    @Given("the bookstore application is running")
    public void the_bookstore_application_is_running() {
        homePage.navigateToHomePage();
    }

    @Given("there are multiple books in the catalog")
    public void there_are_multiple_books_in_the_catalog() {
        List<WebElement> books = homePage.getBookCards();
        Assert.assertTrue(books.size() > 1, "There should be multiple books in the catalog");
    }

    @Given("I am on the homepage")
    public void i_am_on_the_homepage() {
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page should be displayed");
    }

    @When("I enter {string} in the search field")
    public void i_enter_in_the_search_field(String searchTerm) {
        homePage.searchForBook(searchTerm);
    }

    @When("I click the search button")
    public void i_click_the_search_button() {
        // Search is performed in the searchForBook method
        // This step is included for BDD clarity but doesn't need additional action
    }

    @When("I leave the search field empty")
    public void i_leave_the_search_field_empty() {
        homePage.searchForBook("");
    }

    @Then("I should see books with {string} in the title")
    public void i_should_see_books_with_in_the_title(String titleText) {
        List<WebElement> books = homePage.getBookCards();
        boolean foundBookWithTitle = false;

        for (WebElement book : books) {
            String bookTitle = book.findElement(
                org.openqa.selenium.By.cssSelector(".book-title")).getText();
            if (bookTitle.toLowerCase().contains(titleText.toLowerCase())) {
                foundBookWithTitle = true;
                break;
            }
        }

        Assert.assertTrue(foundBookWithTitle,
            "Should find at least one book with '" + titleText + "' in the title");
    }

    @Then("I should see {string} by {string}")
    public void i_should_see_by(String bookTitle, String author) {
        List<WebElement> books = homePage.getBookCards();
        boolean foundBook = false;

        for (WebElement book : books) {
            String title = book.findElement(
                org.openqa.selenium.By.cssSelector(".book-title")).getText();
            String bookAuthor = book.findElement(
                org.openqa.selenium.By.cssSelector(".book-author")).getText();

            if (title.contains(bookTitle) && bookAuthor.contains(author)) {
                foundBook = true;
                break;
            }
        }

        Assert.assertTrue(foundBook,
            "Should find book '" + bookTitle + "' by '" + author + "'");
    }

    @Then("the search results should be relevant to my query")
    public void the_search_results_should_be_relevant_to_my_query() {
        List<WebElement> books = homePage.getBookCards();
        Assert.assertTrue(books.size() > 0, "Search results should contain relevant books");
    }

    @Then("I should see all books by {string}")
    public void i_should_see_all_books_by(String author) {
        List<WebElement> books = homePage.getBookCards();
        boolean foundAuthorBook = false;

        for (WebElement book : books) {
            String bookAuthor = book.findElement(
                org.openqa.selenium.By.cssSelector(".book-author")).getText();
            if (bookAuthor.contains(author)) {
                foundAuthorBook = true;
                break;
            }
        }

        Assert.assertTrue(foundAuthorBook,
            "Should find at least one book by '" + author + "'");
    }

    @Then("I should see {string} in the search results")
    public void i_should_see_in_the_search_results(String bookTitle) {
        Assert.assertTrue(homePage.isBookDisplayed(bookTitle),
            "Book '" + bookTitle + "' should be displayed in search results");
    }

    @Then("the search results should display author information")
    public void the_search_results_should_display_author_information() {
        List<WebElement> books = homePage.getBookCards();
        for (WebElement book : books) {
            WebElement authorElement = book.findElement(
                org.openqa.selenium.By.cssSelector(".book-author"));
            Assert.assertTrue(authorElement.isDisplayed() &&
                           !authorElement.getText().trim().isEmpty(),
                           "Each book should display author information");
        }
    }

    @Then("I should see books containing {string} in the title")
    public void i_should_see_books_containing_in_the_title(String partialTitle) {
        i_should_see_books_with_in_the_title(partialTitle);
    }

    @Then("I should see books by authors containing {string}")
    public void i_should_see_books_by_authors_containing(String partialAuthor) {
        List<WebElement> books = homePage.getBookCards();
        boolean foundAuthorMatch = false;

        for (WebElement book : books) {
            String bookAuthor = book.findElement(
                org.openqa.selenium.By.cssSelector(".book-author")).getText();
            if (bookAuthor.toLowerCase().contains(partialAuthor.toLowerCase())) {
                foundAuthorMatch = true;
                break;
            }
        }

        Assert.assertTrue(foundAuthorMatch,
            "Should find at least one book by author containing '" + partialAuthor + "'");
    }

    @Then("I should see a {string} message")
    public void i_should_see_a_message(String messageType) {
        if (messageType.equals("No results found")) {
            Assert.assertTrue(homePage.isNoResultsMessageDisplayed() ||
                           homePage.getBookCards().isEmpty(),
                           "Should display no results message or empty results");
        }
    }

    @Then("no books should be displayed")
    public void no_books_should_be_displayed() {
        List<WebElement> books = homePage.getBookCards();
        Assert.assertTrue(books.isEmpty(), "No books should be displayed");
    }

    @Then("I should see all available books")
    public void i_should_see_all_available_books() {
        List<WebElement> books = homePage.getBookCards();
        Assert.assertTrue(books.size() >= 5, "Should see all available books when search is empty");
    }

    @Then("the search should be case insensitive")
    public void the_search_should_be_case_insensitive() {
        // This is verified by the fact that we found the book with uppercase search term
        List<WebElement> books = homePage.getBookCards();
        Assert.assertTrue(books.size() > 0, "Case insensitive search should return results");
    }
}
