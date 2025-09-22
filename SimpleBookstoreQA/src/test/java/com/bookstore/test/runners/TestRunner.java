package com.bookstore.test.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = {
        "com.bookstore.test.steps",
        "com.bookstore.test.hooks"
    },
    plugin = {
        "pretty",
        "html:target/cucumber-html-report",
        "json:target/cucumber.json",
        "testng:target/cucumber-testng.xml"
    },
    tags = "@smoke",
    monochrome = true,
    publish = false
)
public class TestRunner extends AbstractTestNGCucumberTests {

    private static String testType = "all";

    @Parameters({"testType"})
    @BeforeClass(alwaysRun = true)
    public void setTestType(@Optional("all") String type) {
        if (type != null && !type.isEmpty()) {
            testType = type.toLowerCase();
            updateTagsBasedOnTestType();
        }
    }

    /**
     * Updates the Cucumber tags based on the test type parameter
     */
    private void updateTagsBasedOnTestType() {
        String tags;
        switch (testType) {
            case "smoke":
                tags = "@smoke";
                break;
            case "regression":
                tags = "@regression";
                break;
            case "negative":
                tags = "@negative";
                break;
            case "edge-case":
                tags = "@edge-case";
                break;
            case "critical":
                tags = "@smoke or @critical";
                break;
            default:
                tags = "@smoke or @regression";
                break;
        }

        // Update system property for Cucumber to pick up
        System.setProperty("cucumber.filter.tags", tags);
    }

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    /**
     * Entry point for different test execution modes
     * Usage examples:
     *
     * Run all tests (default):
     * mvn test -Dtest=TestRunner
     *
     * Run only smoke tests:
     * mvn test -Dtest=TestRunner -DtestType=smoke
     *
     * Run regression tests:
     * mvn test -Dtest=TestRunner -DtestType=regression
     *
     * Run negative tests:
     * mvn test -Dtest=TestRunner -DtestType=negative
     *
     * Run edge case tests:
     * mvn test -Dtest=TestRunner -DtestType=edge-case
     *
     * Run critical tests:
     * mvn test -Dtest=TestRunner -DtestType=critical
     */
}
