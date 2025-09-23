package com.bookstore.test.hooks;

import com.bookstore.test.config.WebDriverConfig;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import com.epam.healenium.SelfHealingDriver;

public class TestHooks {

    @Before
    public void setUp() {
        WebDriverConfig.setDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = WebDriverConfig.getDriver();
        if (driver != null) {
            // Always take screenshot for better debugging (you can change this to only on failure)
            if (scenario.isFailed()) {
                takeScreenshot(scenario, driver, "FAILED");
            }
            // Uncomment the line below to take screenshots for all scenarios (passed/failed)
            // takeScreenshot(scenario, driver, scenario.getStatus().toString());

            WebDriverConfig.quitDriver();
        }
    }

    private void takeScreenshot(Scenario scenario, WebDriver driver, String status) {
        try {
            // Handle both regular WebDriver and SelfHealingDriver
            WebDriver actualDriver = driver;
            if (driver instanceof SelfHealingDriver) {
                // Get the underlying WebDriver from SelfHealingDriver
                actualDriver = ((SelfHealingDriver) driver).getDelegate();
            }

            if (actualDriver instanceof TakesScreenshot) {
                byte[] screenshot = ((TakesScreenshot) actualDriver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Screenshot - " + status);
                System.out.println("Screenshot attached to Cucumber report for scenario: " + scenario.getName());
            } else {
                System.err.println("Driver does not support taking screenshots");
            }
        } catch (Exception e) {
            System.err.println("Failed to take screenshot for scenario '" + scenario.getName() + "': " + e.getMessage());
            e.printStackTrace();
        }
    }
}
