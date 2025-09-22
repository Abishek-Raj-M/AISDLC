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

    @FindBy(id = "book-title")
    private WebElement bookTitleField;

    @FindBy(id = "book-author")
    private WebElement bookAuthorField;

    @FindBy(id = "book-isbn")
    private WebElement bookIsbnField;

    @FindBy(id = "book-price")
    private WebElement bookPriceField;

    @FindBy(id = "book-description")
    private WebElement bookDescriptionField;

    @FindBy(id = "book-stock")
    private WebElement bookStockField;

    // Form labels
    @FindBy(css = "label[for='book-title']")
    private WebElement titleLabel;

    @FindBy(css = "label[for='book-author']")
    private WebElement authorLabel;

    @FindBy(css = "label[for='book-isbn']")
    private WebElement isbnLabel;

    @FindBy(css = "label[for='book-price']")
    private WebElement priceLabel;

    @FindBy(css = "label[for='book-description']")
    private WebElement descriptionLabel;

    @FindBy(css = "label[for='book-stock']")
    private WebElement stockLabel;

    // Submit button
    @FindBy(css = "#add-book-form .btn-primary")
    private WebElement addBookButton;

    // Success/Error messages
    @FindBy(css = ".message.success")
    private WebElement successMessage;

    @FindBy(css = ".message.error")
    private WebElement errorMessage;

    // Navigation
    @FindBy(css = ".nav-links a[onclick*=\"showPage('admin')\"]")
    private WebElement adminNavLink;

    public AdminPage() {
        this.driver = WebDriverConfig.getDriver();
        this.wait = WebDriverConfig.getWait(10);
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

    public boolean isSuccessMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(successMessage));
            return successMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(errorMessage));
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getSuccessMessage() {
        try {
            return successMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getErrorMessage() {
        try {
            return errorMessage.getText();
        } catch (Exception e) {
            return "";
        }
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
