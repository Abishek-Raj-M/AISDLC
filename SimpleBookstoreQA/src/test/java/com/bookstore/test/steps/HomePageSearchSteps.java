package com.bookstore.test.steps;

import com.bookstore.test.pages.HomePage;
import com.bookstore.test.pages.ShoppingCartPage;
import io.cucumber.java.en.*;
import org.testng.Assert;

public class HomePageSearchSteps {

    private HomePage homePage;
    private ShoppingCartPage shoppingCartPage;

    public HomePageSearchSteps() {
        this.homePage = new HomePage();
        this.shoppingCartPage = new ShoppingCartPage();
    }

    @Given("I am on the home page")
    public void i_am_on_the_home_page() {
        homePage.navigateToHomePage();
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page is not displayed");
    }

    @When("I search for the book {string}")
    public void i_search_for_the_book(String bookTitle) {
        homePage.searchForBook(bookTitle);
        // Wait a moment for search results to load
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Then("I should see the book title {string} displayed in search results")
    public void i_should_see_the_book_title_displayed_in_search_results(String bookTitle) {
        Assert.assertTrue(homePage.isBookTitleVisibleInSearchResults(bookTitle),
            "Book title '" + bookTitle + "' is not visible in search results");
    }

    @When("I add the first book to cart")
    public void i_add_the_first_book_to_cart() {
        homePage.addFirstBookToCart();
    }

    @Then("I should see the cart count is {string}")
    public void i_should_see_the_cart_count_is(String expectedCount) {
        String actualCount = homePage.getCartCount();
        Assert.assertEquals(actualCount, expectedCount,
            "Cart count does not match. Expected: " + expectedCount + ", Actual: " + actualCount);
    }

    @When("I navigate to the cart page")
    public void i_navigate_to_the_cart_page() {
        homePage.clickCartNavigation();
        Assert.assertTrue(shoppingCartPage.isCartPageDisplayed(), "Cart page is not displayed");
    }

    @Then("I should see {string} in the cart")
    public void i_should_see_in_the_cart(String bookTitle) {
        Assert.assertTrue(shoppingCartPage.isItemInCart(bookTitle),
            "Book '" + bookTitle + "' is not found in the cart");
    }

    @Then("I should see no book cards displayed")
    public void i_should_see_no_book_cards_displayed() {
        Assert.assertFalse(homePage.hasBookCards(),
            "Book cards are displayed when they should not be");
    }

    @Then("I should not see the book title {string} displayed in search results")
    public void i_should_not_see_the_book_title_displayed_in_search_results(String bookTitle) {
        Assert.assertFalse(homePage.isBookTitleVisibleInSearchResults(bookTitle),
            "Book title '" + bookTitle + "' is visible in search results when it should not be");
    }

    @Then("the search should show no results message or empty state")
    public void the_search_should_show_no_results_message_or_empty_state() {
        // Check for either empty state or no book cards
        boolean noBookCards = !homePage.hasBookCards();
        boolean emptyStateDisplayed = homePage.isEmptyStateDisplayed();

        Assert.assertTrue(noBookCards || emptyStateDisplayed,
            "Expected either no book cards or empty state to be displayed for no search results");
    }
}
