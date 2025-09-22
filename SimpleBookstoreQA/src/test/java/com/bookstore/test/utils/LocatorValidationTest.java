package com.bookstore.test.utils;

import com.bookstore.test.config.WebDriverConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;

/**
 * Test class to validate all locators used in the Simple Digital Bookstore application
 * This should be run before executing the main test suite to ensure all locators are reliable
 */
public class LocatorValidationTest {

    private static final Logger logger = LoggerFactory.getLogger(LocatorValidationTest.class);
    private LocatorValidationUtil validationUtil;

    @BeforeClass
    public void setUp() {
        logger.info("Setting up locator validation test...");
        WebDriverConfig.setDriver();
        validationUtil = new LocatorValidationUtil();
    }

    @Test(priority = 1, description = "Validate critical locators that are essential for basic functionality")
    public void testCriticalLocators() {
        logger.info("Testing critical locators...");

        // Update base URL to use the correct localhost:8081
        validationUtil.updateBaseUrlForValidation();

        boolean criticalLocatorsValid = validationUtil.validateCriticalLocators();

        Assert.assertTrue(criticalLocatorsValid,
            "Critical locators validation failed! Some essential elements cannot be found.");

        logger.info("✅ All critical locators are valid and functional");
    }

    @Test(priority = 2, description = "Comprehensive validation of all page object locators")
    public void testAllLocators() {
        logger.info("Running comprehensive locator validation...");

        LocatorValidationReport report = validationUtil.validateAllLocators();

        // Log the summary
        logger.info("\n" + report.getSummary());

        // Log detailed results if there are failures
        if (report.hasFailures()) {
            logger.warn("\n" + report.getDetailedReport());
        }

        // Assert that we have at least 80% success rate
        double successRate = report.getSuccessRate();
        Assert.assertTrue(successRate >= 80.0,
            String.format("Locator success rate is %.2f%%, which is below the 80%% threshold. " +
                         "Total: %d, Valid: %d, Invalid: %d",
                         successRate, report.getTotalLocators(), report.getTotalValid(), report.getTotalInvalid()));

        logger.info("✅ Locator validation completed with {:.2f}% success rate", successRate);
    }

    @Test(priority = 3, description = "Test navigation between different pages")
    public void testPageNavigation() {
        logger.info("Testing page navigation...");

        // Test navigation to each page and verify page elements are accessible
        String[] pages = {"home", "cart", "checkout", "admin"};

        for (String page : pages) {
            logger.info("Testing navigation to {} page", page);

            try {
                // Navigate using JavaScript (simulating the actual application navigation)
                String script = String.format("showPage('%s')", page);
                ((org.openqa.selenium.JavascriptExecutor) WebDriverConfig.getDriver()).executeScript(script);

                // Wait a moment for page transition
                Thread.sleep(500);

                // Verify the page container is visible and active
                String pageSelector = String.format("#%s-page", page);
                boolean pageVisible = !WebDriverConfig.getDriver().findElements(org.openqa.selenium.By.cssSelector(pageSelector)).isEmpty();

                Assert.assertTrue(pageVisible, String.format("Navigation to %s page failed - page container not found", page));

                logger.info("✅ Successfully navigated to {} page", page);

            } catch (Exception e) {
                logger.error("❌ Failed to navigate to {} page: {}", page, e.getMessage());
                Assert.fail(String.format("Navigation to %s page failed: %s", page, e.getMessage()));
            }
        }
    }

    @Test(priority = 4, description = "Test form field accessibility on different pages")
    public void testFormFieldAccessibility() {
        logger.info("Testing form field accessibility...");

        // Test checkout form fields
        try {
            String script = "showPage('checkout')";
            ((org.openqa.selenium.JavascriptExecutor) WebDriverConfig.getDriver()).executeScript(script);
            Thread.sleep(500);

            String[] checkoutFields = {"#customer-name", "#customer-email", "#customer-address"};
            for (String field : checkoutFields) {
                boolean fieldExists = !WebDriverConfig.getDriver().findElements(org.openqa.selenium.By.cssSelector(field)).isEmpty();
                Assert.assertTrue(fieldExists, String.format("Checkout field %s not found", field));
            }

            logger.info("✅ Checkout form fields are accessible");

        } catch (Exception e) {
            logger.error("❌ Checkout form field test failed: {}", e.getMessage());
            Assert.fail("Checkout form field accessibility test failed: " + e.getMessage());
        }

        // Test admin form fields
        try {
            String script = "showPage('admin')";
            ((org.openqa.selenium.JavascriptExecutor) WebDriverConfig.getDriver()).executeScript(script);
            Thread.sleep(500);

            String[] adminFields = {"#book-title", "#book-author", "#book-price", "#book-stock"};
            for (String field : adminFields) {
                boolean fieldExists = !WebDriverConfig.getDriver().findElements(org.openqa.selenium.By.cssSelector(field)).isEmpty();
                Assert.assertTrue(fieldExists, String.format("Admin field %s not found", field));
            }

            logger.info("✅ Admin form fields are accessible");

        } catch (Exception e) {
            logger.error("❌ Admin form field test failed: {}", e.getMessage());
            Assert.fail("Admin form field accessibility test failed: " + e.getMessage());
        }
    }

    @Test(priority = 5, description = "Generate comprehensive locator validation report")
    public void generateValidationReport() {
        logger.info("Generating final validation report...");

        LocatorValidationReport report = validationUtil.validateAllLocators();

        // Create a detailed report for documentation
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("# Locator Validation Test Results\n\n");
        reportBuilder.append("**Test Execution Date:** ").append(java.time.LocalDateTime.now()).append("\n\n");
        reportBuilder.append("**Application URL:** ").append(WebDriverConfig.getBaseUrl()).append("\n\n");

        reportBuilder.append(report.getDetailedReport());

        // Add recommendations based on results
        reportBuilder.append("\n## Recommendations\n\n");

        if (report.getSuccessRate() >= 95.0) {
            reportBuilder.append("✅ **Excellent** - Locator reliability is very high. Ready for test execution.\n\n");
        } else if (report.getSuccessRate() >= 80.0) {
            reportBuilder.append("⚠️ **Good** - Most locators are reliable. Review failed locators before test execution.\n\n");
        } else {
            reportBuilder.append("❌ **Needs Attention** - Low locator reliability. Fix failed locators before proceeding.\n\n");
        }

        if (report.hasFailures()) {
            reportBuilder.append("### Failed Locators to Address:\n");
            for (LocatorValidationReport.PageValidationResult pageResult : report.getPageResults().values()) {
                if (!pageResult.getInvalidLocators().isEmpty()) {
                    reportBuilder.append(String.format("- **%s**: %d failed locators\n",
                                       pageResult.getPageName(), pageResult.getInvalidCount()));
                }
            }
        }

        String finalReport = reportBuilder.toString();
        logger.info("\n" + finalReport);

        // Assert overall success
        Assert.assertTrue(report.getSuccessRate() >= 70.0,
            "Overall locator validation failed with success rate: " + report.getSuccessRate() + "%");
    }

    @AfterClass
    public void tearDown() {
        logger.info("Cleaning up locator validation test...");
        WebDriverConfig.quitDriver();
    }
}
