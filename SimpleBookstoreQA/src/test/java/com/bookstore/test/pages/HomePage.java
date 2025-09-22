package com.bookstore.test.pages;

import com.bookstore.test.config.WebDriverConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

public class HomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Navigation elements - based on actual HTML structure
    @FindBy(css = ".nav-links a[onclick*=\"showPage('home')\"]")
    private WebElement homeNavLink;

    @FindBy(css = ".nav-links a[onclick*=\"showPage('cart')\"]")
    private WebElement cartNavLink;

    @FindBy(id = "cart-count")
    private WebElement cartCount;

    @FindBy(css = ".nav-links a[onclick*=\"showPage('admin')\"]")
    private WebElement adminNavLink;

    // Search elements - matching actual HTML IDs
    @FindBy(id = "search-input")
    private WebElement searchInput;

    @FindBy(css = ".search-bar button")
    private WebElement searchButton;

    // Books grid and book cards - based on actual CSS classes
    @FindBy(id = "books-grid")
    private WebElement booksGrid;

    @FindBy(css = ".book-card")
    private List<WebElement> bookCards;

    @FindBy(css = ".book-card .book-title")
    private List<WebElement> bookTitles;

    @FindBy(css = ".book-card .book-author")
    private List<WebElement> bookAuthors;

    @FindBy(css = ".book-card .book-price")
    private List<WebElement> bookPrices;

    @FindBy(css = ".book-card .book-description")
    private List<WebElement> bookDescriptions;

    @FindBy(css = ".book-card .book-stock")
    private List<WebElement> bookStock;

    @FindBy(css = ".book-card .btn-primary")
    private List<WebElement> addToCartButtons;

    // Page elements
    @FindBy(id = "home-page")
    private WebElement homePage;

    @FindBy(css = ".search-section h2")
    private WebElement browseTitle;

    // Empty state or loading messages
    @FindBy(css = ".empty-state")
    private WebElement emptyState;

    @FindBy(css = ".loading")
    private WebElement loadingMessage;

    public HomePage() {
        this.driver = WebDriverConfig.getDriver();
        this.wait = WebDriverConfig.getWait(10);
        PageFactory.initElements(driver, this);
    }

    public void navigateToHomePage() {
        driver.get(WebDriverConfig.getBaseUrl());
        wait.until(ExpectedConditions.visibilityOf(homePage));
    }

    public void searchForBook(String searchTerm) {
        wait.until(ExpectedConditions.visibilityOf(searchInput));
        searchInput.clear();
        searchInput.sendKeys(searchTerm);
        searchButton.click();
    }

    public List<WebElement> getBookCards() {
        wait.until(ExpectedConditions.visibilityOfAllElements(bookCards));
        return bookCards;
    }

    public List<WebElement> getBookItems() {
        return getBookCards(); // Alias for backward compatibility
    }

    public void clickOnBook(int index) {
        List<WebElement> books = getBookCards();
        if (index < books.size()) {
            wait.until(ExpectedConditions.elementToBeClickable(books.get(index)));
            books.get(index).click();
        }
    }

    public void addBookToCart(int index) {
        wait.until(ExpectedConditions.visibilityOfAllElements(addToCartButtons));
        if (index < addToCartButtons.size()) {
            wait.until(ExpectedConditions.elementToBeClickable(addToCartButtons.get(index)));
            addToCartButtons.get(index).click();
        }
    }

    public String getCartCount() {
        wait.until(ExpectedConditions.visibilityOf(cartCount));
        return cartCount.getText();
    }

    public void clickCartLink() {
        wait.until(ExpectedConditions.elementToBeClickable(cartNavLink));
        cartNavLink.click();
    }

    public boolean isBookDisplayed(String bookTitle) {
        for (WebElement title : bookTitles) {
            if (title.getText().contains(bookTitle)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPaginationDisplayed() {
        // Since pagination is not in the current HTML, this would be for future implementation
        try {
            WebElement pagination = driver.findElement(By.cssSelector(".pagination"));
            return pagination.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNoResultsMessageDisplayed() {
        try {
            return emptyState.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isHomePageDisplayed() {
        try {
            return homePage.isDisplayed() && homePage.getAttribute("class").contains("active");
        } catch (Exception e) {
            return false;
        }
    }

    public void clickHomeNavigation() {
        wait.until(ExpectedConditions.elementToBeClickable(homeNavLink));
        homeNavLink.click();
    }

    public void clickAdminNavigation() {
        wait.until(ExpectedConditions.elementToBeClickable(adminNavLink));
        adminNavLink.click();
    }
}
