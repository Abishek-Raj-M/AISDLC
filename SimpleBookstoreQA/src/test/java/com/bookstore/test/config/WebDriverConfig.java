package com.bookstore.test.config;

import com.epam.healenium.SelfHealingDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class WebDriverConfig {

    private static final ThreadLocal<SelfHealingDriver> driverThread = new ThreadLocal<>();
    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream input = WebDriverConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            System.err.println("Could not load application.properties: " + e.getMessage());
        }
    }

    public static SelfHealingDriver getDriver() {
        return driverThread.get();
    }

    public static void setDriver() {
        String browser = properties.getProperty("browser", "chrome");
        boolean headless = Boolean.parseBoolean(properties.getProperty("headless", "false"));

        WebDriver delegate;

        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless");
                }
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--window-size=1920,1080");
                delegate = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("--headless");
                }
                delegate = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless");
                }
                delegate = new EdgeDriver(edgeOptions);
                break;

            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }

        delegate.manage().window().maximize();
        delegate.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        delegate.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        // Create Self-healing driver
        SelfHealingDriver selfHealingDriver = SelfHealingDriver.create(delegate);
        driverThread.set(selfHealingDriver);
    }

    public static void quitDriver() {
        SelfHealingDriver driver = driverThread.get();
        if (driver != null) {
            driver.quit();
            driverThread.remove();
        }
    }

    public static WebDriverWait getWait(int timeoutInSeconds) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutInSeconds));
    }

    public static String getBaseUrl() {
        return properties.getProperty("base.url", "http://localhost:8081");
    }
}
