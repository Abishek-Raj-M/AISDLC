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

    public boolean isBookTitleVisible(String title) {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(bookTitles));
            for (WebElement bookTitle : bookTitles) {
                if (bookTitle.getText().equals(title)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void clickHomeNavigation() {
        wait.until(ExpectedConditions.elementToBeClickable(homeNavLink));
        homeNavLink.click();
        wait.until(ExpectedConditions.visibilityOf(homePage));
    }

    public void clickAdminNavigation() {
        wait.until(ExpectedConditions.elementToBeClickable(adminNavLink));
        adminNavLink.click();
    }

    public boolean isHomePageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(homePage));
            return homePage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isBookTitleVisibleInSearchResults(String title) {
        try {
            // Wait a bit for search results to load
            Thread.sleep(1000);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("books-grid")));

            for (WebElement bookTitle : bookTitles) {
                if (bookTitle.getText().trim().equalsIgnoreCase(title.trim())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void addFirstBookToCart() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(addToCartButtons));
            if (!addToCartButtons.isEmpty()) {
                wait.until(ExpectedConditions.elementToBeClickable(addToCartButtons.get(0)));
                addToCartButtons.get(0).click();
                // Wait for cart update
                Thread.sleep(500);
            }
        } catch (Exception e) {
            System.err.println("Failed to add book to cart: " + e.getMessage());
        }
    }

    public boolean hasBookCards() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("books-grid")));
            return !bookCards.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickCartNavigation() {
        wait.until(ExpectedConditions.elementToBeClickable(cartNavLink));
        cartNavLink.click();
    }

    public boolean isEmptyStateDisplayed() {
        try {
            return emptyState.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
