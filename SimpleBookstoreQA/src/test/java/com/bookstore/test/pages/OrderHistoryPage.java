package com.bookstore.test.pages;

import com.bookstore.test.config.WebDriverConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

public class OrderHistoryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Order history page would be a separate page/section (not in current HTML)
    // Using anticipated structure based on common patterns
    @FindBy(id = "order-history-page")
    private WebElement orderHistoryPage;

    @FindBy(css = ".order-history-container")
    private WebElement orderHistoryContainer;

    @FindBy(css = ".order-item, .order-record")
    private List<WebElement> orderItems;

    @FindBy(css = ".order-date")
    private List<WebElement> orderDates;

    @FindBy(css = ".order-number, .order-id")
    private List<WebElement> orderNumbers;

    @FindBy(css = ".order-total, .order-amount")
    private List<WebElement> orderTotals;

    @FindBy(css = ".order-status")
    private List<WebElement> orderStatuses;

    @FindBy(css = ".order-details-btn, .view-details")
    private List<WebElement> orderDetailsButtons;

    @FindBy(css = ".no-orders-message, .empty-state")
    private WebElement noOrdersMessage;

    @FindBy(css = ".order-filter, .filter-dropdown")
    private WebElement orderFilter;

    // Navigation back to home
    @FindBy(css = ".nav-links a[onclick*=\"showPage('home')\"]")
    private WebElement backToHomeButton;

    // Order details modal/section
    @FindBy(css = ".order-details-modal, .order-details")
    private WebElement orderDetailsModal;

    @FindBy(css = ".order-items-list")
    private WebElement orderItemsList;

    public OrderHistoryPage() {
        this.driver = WebDriverConfig.getDriver();
        this.wait = WebDriverConfig.getWait(); // Use configurable wait instead of hardcoded value
        PageFactory.initElements(driver, this);
    }

    public boolean isOrderHistoryPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(orderHistoryContainer));
            return orderHistoryContainer.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public List<WebElement> getOrderItems() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(orderItems));
            return orderItems;
        } catch (Exception e) {
            return List.of(); // Return empty list if no orders
        }
    }

    public void viewOrderDetails(int index) {
        if (index < orderDetailsButtons.size()) {
            wait.until(ExpectedConditions.elementToBeClickable(orderDetailsButtons.get(index)));
            orderDetailsButtons.get(index).click();
        }
    }

    public String getOrderDate(int index) {
        if (index < orderDates.size()) {
            wait.until(ExpectedConditions.visibilityOf(orderDates.get(index)));
            return orderDates.get(index).getText();
        }
        return "";
    }

    public String getOrderNumber(int index) {
        if (index < orderNumbers.size()) {
            wait.until(ExpectedConditions.visibilityOf(orderNumbers.get(index)));
            return orderNumbers.get(index).getText();
        }
        return "";
    }

    public String getOrderTotal(int index) {
        if (index < orderTotals.size()) {
            wait.until(ExpectedConditions.visibilityOf(orderTotals.get(index)));
            return orderTotals.get(index).getText();
        }
        return "";
    }

    public String getOrderStatus(int index) {
        if (index < orderStatuses.size()) {
            wait.until(ExpectedConditions.visibilityOf(orderStatuses.get(index)));
            return orderStatuses.get(index).getText();
        }
        return "";
    }

    public boolean isNoOrdersMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(noOrdersMessage));
            return noOrdersMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public int getOrderCount() {
        return getOrderItems().size();
    }

    public void goBackToHome() {
        wait.until(ExpectedConditions.elementToBeClickable(backToHomeButton));
        backToHomeButton.click();
    }

    public boolean isOrderDetailsModalDisplayed() {
        try {
            return orderDetailsModal.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
