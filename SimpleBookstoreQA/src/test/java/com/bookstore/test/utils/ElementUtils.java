package com.bookstore.test.utils;

import com.epam.healenium.annotation.DisableHealing;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.bookstore.test.config.WebDriverConfig;

/**
 * Utility class for element operations that may need healing disabled
 */
public class ElementUtils {

    /**
     * Check if an element is present without healing
     * Use this when you want to verify that element is NOT present
     */
    @DisableHealing
    public static boolean isElementPresent(By locator) {
        try {
            WebDriver driver = WebDriverConfig.getDriver();
            WebElement element = driver.findElement(locator);
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Check if an element is present by CSS selector without healing
     */
    @DisableHealing
    public static boolean isElementPresentByCss(String cssSelector) {
        return isElementPresent(By.cssSelector(cssSelector));
    }

    /**
     * Check if an element is present by XPath without healing
     */
    @DisableHealing
    public static boolean isElementPresentByXPath(String xpath) {
        return isElementPresent(By.xpath(xpath));
    }

    /**
     * Verify element is not displayed (healing disabled)
     * Useful for negative test cases
     */
    @DisableHealing
    public static boolean verifyElementNotDisplayed(By locator) {
        try {
            WebDriver driver = WebDriverConfig.getDriver();
            WebElement element = driver.findElement(locator);
            return !element.isDisplayed();
        } catch (NoSuchElementException e) {
            return true; // Element not found means it's not displayed
        }
    }
}
