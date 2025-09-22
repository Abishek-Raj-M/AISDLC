package com.bookstore.test.hooks;

import com.bookstore.test.config.WebDriverConfig;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestHooks {

    private static final Logger logger = LoggerFactory.getLogger(TestHooks.class);

    @Before
    public void setUp(Scenario scenario) {
        try {
            logger.info("Starting scenario: {}", scenario.getName());
            WebDriverConfig.setDriver();

            // Add a small delay to ensure driver is fully initialized
            Thread.sleep(1000);

            WebDriver driver = WebDriverConfig.getDriver();
            if (driver != null) {
                // Navigate to the application to ensure it's accessible
                driver.get(WebDriverConfig.getBaseUrl());
                logger.info("Browser initialized and navigated to application for scenario: {}", scenario.getName());
            } else {
                throw new RuntimeException("Failed to initialize WebDriver");
            }
        } catch (Exception e) {
            logger.error("Failed to setup test environment for scenario: {}", scenario.getName(), e);
            throw new RuntimeException("Test setup failed", e);
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        try {
            WebDriver driver = WebDriverConfig.getDriver();

            if (scenario.isFailed() && driver != null) {
                logger.error("Scenario failed: {}", scenario.getName());

                // Take screenshot for failed scenarios
                try {
                    byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    scenario.attach(screenshot, "image/png", "Screenshot");
                    logger.info("Screenshot attached for failed scenario");
                } catch (Exception e) {
                    logger.error("Failed to take screenshot: {}", e.getMessage());
                }
            }

            // Quit the driver with proper error handling
            WebDriverConfig.quitDriver();
            logger.info("Test completed and browser closed for scenario: {}", scenario.getName());

        } catch (Exception e) {
            logger.error("Error during test teardown for scenario: {}", scenario.getName(), e);
        }
    }
}
