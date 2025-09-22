package com.bookstore.test.pages;

import com.bookstore.test.config.WebDriverConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

public class BookDetailsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Book details would typically be displayed in a modal or separate page
    // Since not explicitly defined in HTML, using common patterns with book-card context
    @FindBy(css = ".book-card.selected, .book-details-modal")
    private WebElement bookDetailsContainer;

    @FindBy(css = ".book-card .book-title, .book-details .book-title")
    private WebElement bookTitle;

    @FindBy(css = ".book-card .book-author, .book-details .book-author")
    private WebElement bookAuthor;

    @FindBy(css = ".book-card .book-description, .book-details .book-description")
    private WebElement bookDescription;

    @FindBy(css = ".book-isbn, .book-details .book-isbn")
    private WebElement bookIsbn;

    @FindBy(css = ".book-publication-date, .book-details .publication-date")
    private WebElement bookPublicationDate;

    @FindBy(css = ".book-card .book-price, .book-details .book-price")
    private WebElement bookPrice;

    @FindBy(css = ".book-card .book-stock, .book-details .book-availability")
    private WebElement bookAvailability;

    @FindBy(css = ".book-card .btn-primary, .book-details .add-to-cart-btn")
    private WebElement addToCartButton;

    // Customer reviews would be dynamically loaded
    @FindBy(css = ".customer-review, .reviews .review-item")
    private List<WebElement> customerReviews;

    @FindBy(css = ".book-rating, .rating-display")
    private WebElement bookRating;

    // Navigation back to books
    @FindBy(css = ".back-to-books, .btn-secondary")
    private WebElement backToBooksButton;

    // Close modal if implemented as modal
    @FindBy(css = ".modal-close, .close-btn")
    private WebElement closeButton;

    public BookDetailsPage() {
        this.driver = WebDriverConfig.getDriver();
        this.wait = WebDriverConfig.getWait(10);
        PageFactory.initElements(driver, this);
    }

    public String getBookTitle() {
        wait.until(ExpectedConditions.visibilityOf(bookTitle));
        return bookTitle.getText();
    }

    public String getBookAuthor() {
        wait.until(ExpectedConditions.visibilityOf(bookAuthor));
        return bookAuthor.getText();
    }

    public String getBookDescription() {
        wait.until(ExpectedConditions.visibilityOf(bookDescription));
        return bookDescription.getText();
    }

    public String getBookIsbn() {
        try {
            wait.until(ExpectedConditions.visibilityOf(bookIsbn));
            return bookIsbn.getText();
        } catch (Exception e) {
            return ""; // ISBN might not be displayed in current implementation
        }
    }

    public String getBookPublicationDate() {
        try {
            wait.until(ExpectedConditions.visibilityOf(bookPublicationDate));
            return bookPublicationDate.getText();
        } catch (Exception e) {
            return ""; // Publication date might not be displayed
        }
    }

    public String getBookPrice() {
        wait.until(ExpectedConditions.visibilityOf(bookPrice));
        return bookPrice.getText();
    }

    public String getBookAvailability() {
        wait.until(ExpectedConditions.visibilityOf(bookAvailability));
        return bookAvailability.getText();
    }

    public void addToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
        addToCartButton.click();
    }

    public List<WebElement> getCustomerReviews() {
        try {
            return customerReviews;
        } catch (Exception e) {
            return List.of(); // Return empty list if no reviews
        }
    }

    public String getBookRating() {
        try {
            wait.until(ExpectedConditions.visibilityOf(bookRating));
            return bookRating.getText();
        } catch (Exception e) {
            return ""; // Rating might not be available
        }
    }

    public void goBackToBooks() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(backToBooksButton));
            backToBooksButton.click();
        } catch (Exception e) {
            // Fallback to navigation
            driver.navigate().back();
        }
    }

    public boolean isBookDetailsDisplayed() {
        try {
            return bookTitle.isDisplayed() && bookPrice.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void closeBookDetails() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(closeButton));
            closeButton.click();
        } catch (Exception e) {
            // Fallback to navigation back
            driver.navigate().back();
        }
    }
}
