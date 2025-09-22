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

public class ShoppingCartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Cart page container - based on actual HTML structure
    @FindBy(id = "cart-page")
    private WebElement cartPage;

    // Cart items container - matching actual HTML IDs
    @FindBy(id = "cart-items")
    private WebElement cartItemsContainer;

    @FindBy(css = ".cart-item")
    private List<WebElement> cartItems;

    // Cart item details - based on actual CSS classes from style.css
    @FindBy(css = ".cart-item-info")
    private List<WebElement> cartItemInfo;

    @FindBy(css = ".cart-item-title")
    private List<WebElement> itemTitles;

    @FindBy(css = ".cart-item-price")
    private List<WebElement> itemPrices;

    @FindBy(css = ".quantity-input")
    private List<WebElement> itemQuantities;

    @FindBy(css = ".cart-item-controls .btn-danger")
    private List<WebElement> removeItemButtons;

    @FindBy(css = ".cart-item-controls")
    private List<WebElement> cartItemControls;

    // Cart total section - matching actual HTML structure
    @FindBy(css = ".cart-total")
    private WebElement cartTotalSection;

    @FindBy(id = "cart-total")
    private WebElement cartTotal;

    @FindBy(id = "checkout-btn")
    private WebElement checkoutButton;

    // Empty cart state - based on CSS classes
    @FindBy(css = ".empty-state")
    private WebElement emptyCartMessage;

    // Navigation elements
    @FindBy(css = ".nav-links a[onclick*=\"showPage('home')\"]")
    private WebElement continueShoppingButton;

    public ShoppingCartPage() {
        this.driver = WebDriverConfig.getDriver();
        this.wait = WebDriverConfig.getWait(10);
        PageFactory.initElements(driver, this);
    }

    public boolean isCartPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(cartPage));
            return cartPage.isDisplayed() && cartPage.getAttribute("class").contains("active");
        } catch (Exception e) {
            return false;
        }
    }

    public List<WebElement> getCartItems() {
        try {
            wait.until(ExpectedConditions.visibilityOf(cartItemsContainer));
            return cartItems;
        } catch (Exception e) {
            return List.of(); // Return empty list if no items
        }
    }

    public void removeItem(int index) {
        if (index < removeItemButtons.size()) {
            wait.until(ExpectedConditions.elementToBeClickable(removeItemButtons.get(index)));
            removeItemButtons.get(index).click();
        }
    }

    public void updateQuantity(int index, String quantity) {
        if (index < itemQuantities.size()) {
            wait.until(ExpectedConditions.visibilityOf(itemQuantities.get(index)));
            itemQuantities.get(index).clear();
            itemQuantities.get(index).sendKeys(quantity);
        }
    }

    public String getCartTotal() {
        wait.until(ExpectedConditions.visibilityOf(cartTotal));
        return cartTotal.getText();
    }

    public void proceedToCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton));
        checkoutButton.click();
    }

    public void continueShopping() {
        wait.until(ExpectedConditions.elementToBeClickable(continueShoppingButton));
        continueShoppingButton.click();
    }

    public boolean isCartEmpty() {
        try {
            return emptyCartMessage.isDisplayed() || getCartItemCount() == 0;
        } catch (Exception e) {
            return getCartItemCount() == 0;
        }
    }

    public int getCartItemCount() {
        return getCartItems().size();
    }

    public String getItemTitle(int index) {
        if (index < itemTitles.size()) {
            wait.until(ExpectedConditions.visibilityOf(itemTitles.get(index)));
            return itemTitles.get(index).getText();
        }
        return "";
    }

    public String getItemPrice(int index) {
        if (index < itemPrices.size()) {
            wait.until(ExpectedConditions.visibilityOf(itemPrices.get(index)));
            return itemPrices.get(index).getText();
        }
        return "";
    }

    public String getItemQuantity(int index) {
        if (index < itemQuantities.size()) {
            wait.until(ExpectedConditions.visibilityOf(itemQuantities.get(index)));
            return itemQuantities.get(index).getAttribute("value");
        }
        return "";
    }
}
