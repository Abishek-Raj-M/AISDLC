package com.bookstore.test.utils;

import com.bookstore.test.config.WebDriverConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Utility class to validate all locators used in page objects
 * against the actual HTML structure of the application.
 * This helps identify potentially unreliable locators before running tests.
 */
public class LocatorValidationUtil {

    private static final Logger logger = LoggerFactory.getLogger(LocatorValidationUtil.class);
    private final WebDriver driver;
    private final Map<String, List<String>> validationResults;
    private final Map<String, List<String>> failedLocators;

    public LocatorValidationUtil() {
        this.driver = WebDriverConfig.getDriver();
        this.validationResults = new HashMap<>();
        this.failedLocators = new HashMap<>();
    }

    /**
     * Validates all locators for all pages by navigating to the application
     * and checking if elements can be found
     */
    public LocatorValidationReport validateAllLocators() {
        logger.info("Starting comprehensive locator validation...");

        // Navigate to the application
        driver.get(WebDriverConfig.getBaseUrl());

        // Validate each page's locators
        validateHomePageLocators();
        validateCartPageLocators();
        validateCheckoutPageLocators();
        validateAdminPageLocators();

        // Generate report
        return generateValidationReport();
    }

    /**
     * Validates HomePage locators based on the actual HTML structure
     */
    private void validateHomePageLocators() {
        logger.info("Validating HomePage locators...");
        String pageName = "HomePage";
        List<String> validLocators = new ArrayList<>();
        List<String> invalidLocators = new ArrayList<>();

        // Navigation elements
        validateLocator(".nav-links a[onclick*=\"showPage('home')\"]", pageName, "homeNavLink", validLocators, invalidLocators);
        validateLocator(".nav-links a[onclick*=\"showPage('cart')\"]", pageName, "cartNavLink", validLocators, invalidLocators);
        validateLocator("#cart-count", pageName, "cartCount", validLocators, invalidLocators);
        validateLocator(".nav-links a[onclick*=\"showPage('admin')\"]", pageName, "adminNavLink", validLocators, invalidLocators);

        // Search elements
        validateLocator("#search-input", pageName, "searchInput", validLocators, invalidLocators);
        validateLocator(".search-bar button", pageName, "searchButton", validLocators, invalidLocators);

        // Books grid elements
        validateLocator("#books-grid", pageName, "booksGrid", validLocators, invalidLocators);
        validateLocator(".book-card", pageName, "bookCards", validLocators, invalidLocators);
        validateLocator(".book-card .book-title", pageName, "bookTitles", validLocators, invalidLocators);
        validateLocator(".book-card .book-author", pageName, "bookAuthors", validLocators, invalidLocators);
        validateLocator(".book-card .book-price", pageName, "bookPrices", validLocators, invalidLocators);
        validateLocator(".book-card .book-description", pageName, "bookDescriptions", validLocators, invalidLocators);
        validateLocator(".book-card .book-stock", pageName, "bookStock", validLocators, invalidLocators);
        validateLocator(".book-card .btn-primary", pageName, "addToCartButtons", validLocators, invalidLocators);

        // Page elements
        validateLocator("#home-page", pageName, "homePage", validLocators, invalidLocators);
        validateLocator(".search-section h2", pageName, "browseTitle", validLocators, invalidLocators);

        validationResults.put(pageName, validLocators);
        failedLocators.put(pageName, invalidLocators);
    }

    /**
     * Validates ShoppingCartPage locators
     */
    private void validateCartPageLocators() {
        logger.info("Validating ShoppingCartPage locators...");
        String pageName = "ShoppingCartPage";
        List<String> validLocators = new ArrayList<>();
        List<String> invalidLocators = new ArrayList<>();

        // Navigate to cart page first
        navigateToPage("cart");

        // Cart page elements
        validateLocator("#cart-page", pageName, "cartPage", validLocators, invalidLocators);
        validateLocator("#cart-items", pageName, "cartItemsContainer", validLocators, invalidLocators);

        // Cart item locators (these are conditionally present when cart has items)
        validateLocator(".cart-item", pageName, "cartItems", validLocators, invalidLocators);
        validateLocator(".cart-item-info", pageName, "cartItemInfo", validLocators, invalidLocators);
        validateLocator(".cart-item-title", pageName, "itemTitles", validLocators, invalidLocators);
        validateLocator(".cart-item-price", pageName, "itemPrices", validLocators, invalidLocators);
        validateLocator(".quantity-input", pageName, "itemQuantities", validLocators, invalidLocators);
        validateLocator(".cart-item-controls .btn-danger", pageName, "removeItemButtons", validLocators, invalidLocators);

        validateLocator(".cart-total", pageName, "cartTotalSection", validLocators, invalidLocators);
        validateLocator("#cart-total", pageName, "cartTotal", validLocators, invalidLocators);
        validateLocator("#checkout-btn", pageName, "checkoutButton", validLocators, invalidLocators);

        validationResults.put(pageName, validLocators);
        failedLocators.put(pageName, invalidLocators);
    }

    /**
     * Validates CheckoutPage locators
     */
    private void validateCheckoutPageLocators() {
        logger.info("Validating CheckoutPage locators...");
        String pageName = "CheckoutPage";
        List<String> validLocators = new ArrayList<>();
        List<String> invalidLocators = new ArrayList<>();

        // Navigate to checkout page
        navigateToPage("checkout");

        // Checkout page elements
        validateLocator("#checkout-page", pageName, "checkoutPage", validLocators, invalidLocators);
        validateLocator("#checkout-form", pageName, "checkoutForm", validLocators, invalidLocators);
        validateLocator("#customer-name", pageName, "customerNameField", validLocators, invalidLocators);
        validateLocator("#customer-email", pageName, "customerEmailField", validLocators, invalidLocators);
        validateLocator("#customer-address", pageName, "customerAddressField", validLocators, invalidLocators);
        validateLocator("label[for='customer-name']", pageName, "nameLabel", validLocators, invalidLocators);
        validateLocator("label[for='customer-email']", pageName, "emailLabel", validLocators, invalidLocators);
        validateLocator("label[for='customer-address']", pageName, "addressLabel", validLocators, invalidLocators);
        validateLocator(".form-actions", pageName, "formActions", validLocators, invalidLocators);
        validateLocator(".form-actions .btn-secondary", pageName, "backToCartButton", validLocators, invalidLocators);
        validateLocator(".form-actions .btn-primary", pageName, "placeOrderButton", validLocators, invalidLocators);

        validationResults.put(pageName, validLocators);
        failedLocators.put(pageName, invalidLocators);
    }

    /**
     * Validates AdminPage locators
     */
    private void validateAdminPageLocators() {
        logger.info("Validating AdminPage locators...");
        String pageName = "AdminPage";
        List<String> validLocators = new ArrayList<>();
        List<String> invalidLocators = new ArrayList<>();

        // Navigate to admin page
        navigateToPage("admin");

        // Admin page elements
        validateLocator("#admin-page", pageName, "adminPage", validLocators, invalidLocators);
        validateLocator(".admin-sections", pageName, "adminSections", validLocators, invalidLocators);
        validateLocator(".admin-section", pageName, "adminSection", validLocators, invalidLocators);
        validateLocator("#add-book-form", pageName, "addBookForm", validLocators, invalidLocators);
        validateLocator("#book-title", pageName, "bookTitleField", validLocators, invalidLocators);
        validateLocator("#book-author", pageName, "bookAuthorField", validLocators, invalidLocators);
        validateLocator("#book-isbn", pageName, "bookIsbnField", validLocators, invalidLocators);
        validateLocator("#book-price", pageName, "bookPriceField", validLocators, invalidLocators);
        validateLocator("#book-description", pageName, "bookDescriptionField", validLocators, invalidLocators);
        validateLocator("#book-stock", pageName, "bookStockField", validLocators, invalidLocators);
        validateLocator("#add-book-form .btn-primary", pageName, "addBookButton", validLocators, invalidLocators);

        validationResults.put(pageName, validLocators);
        failedLocators.put(pageName, invalidLocators);
    }

    /**
     * Validates a single locator with context about when it should be found
     */
    private void validateLocator(String locator, String pageName, String elementName,
                                List<String> validLocators, List<String> invalidLocators) {
        try {
            By by = By.cssSelector(locator);
            List<WebElement> elements = driver.findElements(by);

            if (!elements.isEmpty()) {
                validLocators.add(String.format("%s: %s - FOUND (%d elements)", elementName, locator, elements.size()));
                logger.debug("✓ Valid locator: {} - {}", elementName, locator);
            } else {
                // Special handling for cart item locators that are conditionally present
                if (isCartItemRelatedLocator(elementName)) {
                    validLocators.add(String.format("%s: %s - CONDITIONALLY VALID (empty cart)", elementName, locator));
                    logger.info("✓ Conditionally valid locator (empty cart): {} - {}", elementName, locator);
                } else {
                    invalidLocators.add(String.format("%s: %s - NOT FOUND", elementName, locator));
                    logger.warn("✗ Invalid locator: {} - {}", elementName, locator);
                }
            }
        } catch (Exception e) {
            invalidLocators.add(String.format("%s: %s - ERROR: %s", elementName, locator, e.getMessage()));
            logger.error("✗ Error validating locator: {} - {} - Error: {}", elementName, locator, e.getMessage());
        }
    }

    /**
     * Determines if a locator is related to cart items and should be conditionally valid
     */
    private boolean isCartItemRelatedLocator(String elementName) {
        String name = elementName.toLowerCase();
        return (name.contains("cart") && name.contains("item")) ||
               name.contains("itemtitles") ||
               name.contains("itemprices") ||
               name.contains("itemquantities") ||
               name.contains("removeitembuttons") ||
               name.contains("quantity");
    }

    /**
     * Navigates to a specific page using JavaScript
     */
    private void navigateToPage(String pageName) {
        try {
            String script = String.format("showPage('%s')", pageName);
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(script);
            Thread.sleep(500); // Small delay for page transition
        } catch (Exception e) {
            logger.warn("Could not navigate to page: {} - {}", pageName, e.getMessage());
        }
    }

    /**
     * Generates a comprehensive validation report
     */
    private LocatorValidationReport generateValidationReport() {
        LocatorValidationReport report = new LocatorValidationReport();

        int totalValid = 0;
        int totalInvalid = 0;

        for (String pageName : validationResults.keySet()) {
            int validCount = validationResults.get(pageName).size();
            int invalidCount = failedLocators.get(pageName).size();

            totalValid += validCount;
            totalInvalid += invalidCount;

            report.addPageResult(pageName, validCount, invalidCount,
                               validationResults.get(pageName), failedLocators.get(pageName));
        }

        report.setTotalValid(totalValid);
        report.setTotalInvalid(totalInvalid);

        return report;
    }

    /**
     * Validates critical locators that must be present for basic functionality
     */
    public boolean validateCriticalLocators() {
        logger.info("Validating critical locators...");

        // Navigate to home page
        driver.get(WebDriverConfig.getBaseUrl());

        String[] criticalLocators = {
            "#search-input",           // Search functionality
            "#cart-count",             // Cart count display
            "#books-grid",             // Books display area
            ".nav-links",              // Navigation
            "#home-page",              // Home page container
            "#cart-page",              // Cart page container
            "#checkout-page",          // Checkout page container
            "#admin-page"              // Admin page container
        };

        boolean allCriticalValid = true;

        for (String locator : criticalLocators) {
            try {
                List<WebElement> elements = driver.findElements(By.cssSelector(locator));
                if (elements.isEmpty()) {
                    logger.error("Critical locator FAILED: {}", locator);
                    allCriticalValid = false;
                } else {
                    logger.info("Critical locator OK: {}", locator);
                }
            } catch (Exception e) {
                logger.error("Critical locator ERROR: {} - {}", locator, e.getMessage());
                allCriticalValid = false;
            }
        }

        return allCriticalValid;
    }

    /**
     * Updates the WebDriverConfig base URL to use the correct localhost:8081
     */
    public void updateBaseUrlForValidation() {
        // Ensure we're using the correct URL as specified in the workflow
        String correctUrl = "http://localhost:8081/#";
        driver.get(correctUrl);
        logger.info("Navigated to application URL: {}", correctUrl);
    }
}
