package com.bookstore.test.hooks;

import com.bookstore.test.config.WebDriverConfig;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class TestHooks {

    @Before
    public void setUp() {
        WebDriverConfig.setDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = WebDriverConfig.getDriver();
        if (driver != null) {
            if (scenario.isFailed()) {
                // Take screenshot on failure
                try {
                    byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    scenario.attach(screenshot, "image/png", "Screenshot");
                } catch (Exception e) {
                    System.err.println("Failed to take screenshot: " + e.getMessage());
                }
            }
            WebDriverConfig.quitDriver();
        }
    }
}
