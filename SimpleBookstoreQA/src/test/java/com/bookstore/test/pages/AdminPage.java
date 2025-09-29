package com.bookstore.test.pages;

import com.bookstore.test.config.WebDriverConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AdminPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Admin page container - based on actual HTML structure
    @FindBy(id = "admin-page")
    private WebElement adminPage;

    // Admin sections container
    @FindBy(css = ".admin-sections")
    private WebElement adminSections;

    @FindBy(css = ".admin-section")
    private WebElement adminSection;

    // Add book form - matching exact HTML IDs from index.html
    @FindBy(id = "add-book-form")
    private WebElement addBookForm;

    @FindBy(xpath = "//*[@id='book-title']")
    private WebElement bookTitleField;

    @FindBy(xpath = "//*[@id='book-author']")
    private WebElement bookAuthorField;

    @FindBy(xpath = "//*[@id='book-isbn']")
    private WebElement bookIsbnField;

    @FindBy(xpath = "//*[@id='book-price']")
    private WebElement bookPriceField;

    @FindBy(xpath = "//*[@id='book-description']")
    private WebElement bookDescriptionField;

    @FindBy(xpath = "//*[@id='book-stock']")
    private WebElement bookStockField;

    // Submit button
    @FindBy(css = "#add-book-form .btn-primary")
    private WebElement addBookButton;

    // Navigation
    @FindBy(css = ".nav-links a[onclick*=\"showPage('admin')\"]")
    private WebElement adminNavLink;

    public AdminPage() {
        this.driver = WebDriverConfig.getDriver();
        this.wait = WebDriverConfig.getWait(); // Use configurable wait instead of hardcoded value
        PageFactory.initElements(driver, this);
    }

    public void navigateToAdminPage() {
        wait.until(ExpectedConditions.elementToBeClickable(adminNavLink));
        adminNavLink.click();
        wait.until(ExpectedConditions.visibilityOf(adminPage));
    }

    public boolean isAdminPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(adminPage));
            return adminPage.isDisplayed() && adminPage.getAttribute("class").contains("active");
        } catch (Exception e) {
            return false;
        }
    }

    public void addNewBook(String title, String author, String isbn, String price, String description, String stock) {
        wait.until(ExpectedConditions.visibilityOf(addBookForm));

        // Fill book title
        bookTitleField.clear();
        bookTitleField.sendKeys(title);

        // Fill book author
        bookAuthorField.clear();
        bookAuthorField.sendKeys(author);

        // Fill ISBN (optional field)
        if (isbn != null && !isbn.isEmpty()) {
            bookIsbnField.clear();
            bookIsbnField.sendKeys(isbn);
        }

        // Fill price
        bookPriceField.clear();
        bookPriceField.sendKeys(price);

        // Fill description (optional field)
        if (description != null && !description.isEmpty()) {
            bookDescriptionField.clear();
            bookDescriptionField.sendKeys(description);
        }

        // Fill stock quantity
        bookStockField.clear();
        bookStockField.sendKeys(stock);

        // Submit the form
        wait.until(ExpectedConditions.elementToBeClickable(addBookButton));
        addBookButton.click();
    }

    public void clearForm() {
        bookTitleField.clear();
        bookAuthorField.clear();
        bookIsbnField.clear();
        bookPriceField.clear();
        bookDescriptionField.clear();
        bookStockField.clear();
    }

    public boolean areRequiredFieldsVisible() {
        try {
            return bookTitleField.isDisplayed() &&
                   bookAuthorField.isDisplayed() &&
                   bookPriceField.isDisplayed() &&
                   bookStockField.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
