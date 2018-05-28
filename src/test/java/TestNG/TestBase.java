package TestNG;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;

public class TestBase {
    private ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();

    /**
     * Выполняется перед выполнением тестового класса.
     * Устанаваливаем путь к драйверу Mozilla (geckodriver)
     */
    @BeforeClass
    public void setUp() {
        String driverPath = "/opt/geckodriver";
        System.setProperty("webdriver.gecko.driver", driverPath);
        webDriver.set(new FirefoxDriver());
    }

    public WebDriver getWebDriver() {
        return webDriver.get();
    }

    /**
     * Выполняется после всех тестов. Закрываем браузер
     */
    @AfterTest
    public void tearDown() {
        webDriver.get().quit();
    }

    /**
     * Закрываем всплывающий баннер, мешающих работе webDriver
     */
    public void closeBanner() throws InterruptedException {
        Boolean isPresent = getWebDriver().findElements(By.cssSelector(".close-icon")).size() > 0;
        if (isPresent)
            getWebDriver().findElement(By.cssSelector(".close-icon")).click();

    }
}
