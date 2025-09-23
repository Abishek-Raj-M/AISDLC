package com.bookstore.test.steps;

import com.bookstore.test.pages.AdminPage;
import com.bookstore.test.pages.HomePage;
import com.bookstore.test.config.WebDriverConfig;
import io.cucumber.java.en.*;
import io.cucumber.datatable.DataTable;
import org.testng.Assert;

import java.util.Map;

public class AddingBookSteps {

    private AdminPage adminPage;
    private HomePage homePage;

    public AddingBookSteps() {
        this.adminPage = new AdminPage();
        this.homePage = new HomePage();
    }

    @Given("the bookstore application is running")
    public void the_bookstore_application_is_running() {
        // Navigate to the base URL to ensure application is running
        homePage.navigateToHomePage();
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Bookstore application is not running");
    }

    @Given("I am on the admin page")
    public void i_am_on_the_admin_page() {
        adminPage.navigateToAdminPage();
        Assert.assertTrue(adminPage.isAdminPageDisplayed(), "Admin page is not displayed");
    }

    @When("I add a new book with the following details:")
    public void i_add_a_new_book_with_the_following_details(DataTable dataTable) {
        Map<String, String> bookData = dataTable.asMap(String.class, String.class);

        String title = bookData.get("title");
        String author = bookData.get("author");
        String isbn = bookData.get("isbn");
        String price = bookData.get("price");
        String description = bookData.get("description");
        String stock = bookData.get("stock");

        adminPage.addNewBook(title, author, isbn, price, description, stock);
    }

    @When("I add a new book with title {string} and author {string} and isbn {string} and price {string} and description {string} and stock {string}")
    public void i_add_a_new_book_with_details(String title, String author, String isbn, String price, String description, String stock) {
        adminPage.addNewBook(title, author, isbn, price, description, stock);
    }

    @When("I navigate to the home page")
    public void i_navigate_to_the_home_page() {
        homePage.clickHomeNavigation();
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page is not displayed");
    }

    @Then("I should see the book title {string} displayed on the home page")
    public void i_should_see_the_book_title_displayed_on_the_home_page(String title) {
        // Wait a moment for the book to appear after adding
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Assert.assertTrue(homePage.isBookTitleVisible(title),
            "Book title '" + title + "' is not visible on the home page");
    }
}
