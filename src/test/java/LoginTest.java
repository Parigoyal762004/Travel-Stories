// File: src/test/java/LoginTest.java

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeMethod; // Import BeforeMethod
import org.testng.annotations.AfterMethod;  // Import AfterMethod
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import java.time.Duration;

public class LoginTest {

    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("http://localhost:5173/login");
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @DataProvider(name = "loginCredentials")
    public Object[][] provideLoginData() {
        return new Object[][]{
                {"goyalpari70@gmail.com", "Sanwara", true},
                {"invalid.email@test.com", "Sanwaragoyal", false},
                {"goyalpari70@gmail.com", "wrongpassword", false},
                {"", "Sanwaragoyal", false},
                {"goyalpari70@gmail.com", "", false},
                {"", "", false}
        };
    }

    @Test(dataProvider = "loginCredentials")
    public void testLoginFunctionality(String email, String password, boolean shouldBeSuccessful) {
        WebElement emailField = driver.findElement(By.xpath("//input[@placeholder='Email']")); // Line 46
        WebElement passwordField = driver.findElement(By.xpath("//input[@type='password']"));
        WebElement loginButton = driver.findElement(By.xpath("//button[text()='LOGIN']"));

        emailField.sendKeys(email);
        passwordField.sendKeys(password);
        loginButton.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (shouldBeSuccessful) {
            Assert.assertTrue(driver.getCurrentUrl().contains("/dashboard"), "Login should have been successful for: " + email + " / " + password);
            System.out.println("Login Test Passed for: " + email + " / " + password);
        } else {
            Assert.assertFalse(driver.getCurrentUrl().contains("/dashboard"), "Login should have failed for: " + email + " / " + password);
            System.out.println("Login Test Failed as expected for: " + email + " / " + password);
        }
    }
}