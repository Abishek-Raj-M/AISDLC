package com.bookstore.test.pages;

import com.bookstore.test.config.WebDriverConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Checkout page container - based on actual HTML structure
    @FindBy(id = "checkout-page")
    private WebElement checkoutPage;

    // Checkout form - matching actual HTML IDs from index.html
    @FindBy(id = "checkout-form")
    private WebElement checkoutForm;

    // Customer information fields - using exact IDs from HTML
    @FindBy(id = "customer-name")
    private WebElement customerNameField;

    @FindBy(id = "customer-email")
    private WebElement customerEmailField;

    @FindBy(id = "customer-address")
    private WebElement customerAddressField;

    // Form labels - using actual CSS structure
    @FindBy(css = "label[for='customer-name']")
    private WebElement nameLabel;

    @FindBy(css = "label[for='customer-email']")
    private WebElement emailLabel;

    @FindBy(css = "label[for='customer-address']")
    private WebElement addressLabel;

    // Form action buttons - based on actual HTML structure
    @FindBy(css = ".form-actions")
    private WebElement formActions;

    @FindBy(css = ".form-actions .btn-secondary")
    private WebElement backToCartButton;

    @FindBy(css = ".form-actions .btn-primary")
    private WebElement placeOrderButton;

    // Order confirmation elements (would be added dynamically)
    @FindBy(css = ".message.success")
    private WebElement orderConfirmation;

    @FindBy(css = ".order-number")
    private WebElement orderNumber;

    @FindBy(css = ".order-total-display")
    private WebElement orderTotalDisplay;

    public CheckoutPage() {
        this.driver = WebDriverConfig.getDriver();
        this.wait = WebDriverConfig.getWait(); // Use configurable wait instead of hardcoded value
        PageFactory.initElements(driver, this);
    }

    public boolean isCheckoutPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(checkoutPage));
            return checkoutPage.isDisplayed() && checkoutPage.getAttribute("class").contains("active");
        } catch (Exception e) {
            return false;
        }
    }

    public void fillCustomerInformation(String name, String email, String address) {
        wait.until(ExpectedConditions.visibilityOf(checkoutForm));

        // Fill customer name
        wait.until(ExpectedConditions.visibilityOf(customerNameField));
        customerNameField.clear();
        customerNameField.sendKeys(name);

        // Fill customer email
        customerEmailField.clear();
        customerEmailField.sendKeys(email);

        // Fill customer address
        customerAddressField.clear();
        customerAddressField.sendKeys(address);
    }

    public void fillCustomerInformation(String name, String email, String address, String phone) {
        // For backward compatibility - phone field not in current HTML
        fillCustomerInformation(name, email, address);
    }

    public void placeOrder() {
        wait.until(ExpectedConditions.elementToBeClickable(placeOrderButton));
        placeOrderButton.click();
    }

    public void goBackToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(backToCartButton));
        backToCartButton.click();
    }

    public boolean isOrderConfirmationDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(orderConfirmation));
            return orderConfirmation.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getOrderNumber() {
        try {
            wait.until(ExpectedConditions.visibilityOf(orderNumber));
            return orderNumber.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getOrderTotal() {
        try {
            wait.until(ExpectedConditions.visibilityOf(orderTotalDisplay));
            return orderTotalDisplay.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getOrderSummary() {
        try {
            return checkoutForm.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean areRequiredFieldsVisible() {
        try {
            return customerNameField.isDisplayed() &&
                   customerEmailField.isDisplayed() &&
                   customerAddressField.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
