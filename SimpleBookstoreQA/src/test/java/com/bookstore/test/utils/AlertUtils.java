package com.bookstore.test.utils;

import com.bookstore.test.config.WebDriverConfig;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AlertUtils {

    /**
     * Waits for alert and dismisses it if present. Returns true if alert was handled.
     * Uses configurable alert wait timeout from application.properties
     */
    public static boolean handleAlertIfPresent() {
        return handleAlertIfPresent(true); // dismiss by default
    }

    /**
     * Waits for alert and handles it if present.
     * @param dismiss - true to dismiss, false to accept
     * @return true if alert was handled, false if no alert appeared
     */
    public static boolean handleAlertIfPresent(boolean dismiss) {
        WebDriver driver = WebDriverConfig.getDriver();
        int alertWaitTimeout = WebDriverConfig.getAlertWaitTimeout();

        try {
            WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(alertWaitTimeout));
            Alert alert = alertWait.until(ExpectedConditions.alertIsPresent());

            String alertText = alert.getText();
            System.out.println("Alert detected: " + alertText);

            if (dismiss) {
                alert.dismiss();
                System.out.println("Alert dismissed");
            } else {
                alert.accept();
                System.out.println("Alert accepted");
            }
            return true;
        } catch (TimeoutException | NoAlertPresentException e) {
            // No alert appeared within timeout - this is normal
            return false;
        }
    }

    /**
     * Waits for alert with custom timeout and handles it if present
     */
    public static boolean handleAlertIfPresent(int timeoutSeconds, boolean dismiss) {
        WebDriver driver = WebDriverConfig.getDriver();

        try {
            WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            Alert alert = alertWait.until(ExpectedConditions.alertIsPresent());

            String alertText = alert.getText();
            System.out.println("Alert detected: " + alertText);

            if (dismiss) {
                alert.dismiss();
            } else {
                alert.accept();
            }
            return true;
        } catch (TimeoutException | NoAlertPresentException e) {
            return false;
        }
    }
}
