// File: SignUpTest.java
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
// import java.util.UUID;

public class SignUpTest {

    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("http://localhost:5173/signUp");
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @DataProvider(name = "signUpData")
    public Object[][] provideSignUpData() {
        return new Object[][]{
                {"Valid User", "valid6S.email@test.com", "StrongPass123", true},
                {"User With Space", "invalid_email", "StrongPass123", false},
                {"", "empty@test.com", "StrongPass123", false},
                {"Valid User", "", "StrongPass123", false},
                {"Valid User", "valid@test.com", "", false},
        };
    }

    @Test(dataProvider = "signUpData")
    public void testSignUpFunctionality(String fullName, String email, String password, boolean shouldBeSuccessful) {
        WebElement nameField = driver.findElement(By.xpath("//input[@placeholder='Full Name']"));
        WebElement emailField = driver.findElement(By.xpath("//input[@placeholder='Email']")); // Line 50
        WebElement passwordField = driver.findElement(By.xpath("//input[@type='password']"));
        WebElement signUpButton = driver.findElement(By.xpath("//button[text()='CREATE ACCOUNT']"));

        nameField.sendKeys(fullName);
        emailField.sendKeys(email);
        passwordField.sendKeys(password);
        signUpButton.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (shouldBeSuccessful) {
            Assert.assertTrue(driver.getCurrentUrl().contains("/dashboard"), "SignUp should have been successful for: " + fullName + " / " + email);
            System.out.println("SignUp Test Passed for: " + fullName + " / " + email);
        } else {
            Assert.assertFalse(driver.getCurrentUrl().contains("/dashboard"), "SignUp should have failed for: " + fullName + " / " + email);
            System.out.println("SignUp Test Failed as expected for: " + fullName + " / " + email);
        }
    }
}