import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class LogoutTest {

    WebDriver driver;
    String loginUrl = "http://localhost:5173/login";
    String homeUrl = "http://localhost:5173/";

    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get(loginUrl);

        // Perform Login
        WebElement emailField = driver.findElement(By.xpath("//input[@placeholder='Email']"));
        WebElement passwordField = driver.findElement(By.xpath("//input[@type='password']"));
        WebElement loginButton = driver.findElement(By.xpath("//button[text()='LOGIN']"));

        emailField.sendKeys("goyalpari70@gmail.com");
        passwordField.sendKeys("Sanwara");
        loginButton.click();

        // Wait for home page to load
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement logo = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@alt='travel story']")));
        Assert.assertTrue(logo.isDisplayed(), "Login failed or home page not loaded.");
        System.out.println("Login successful.");
    }

    @Test
    public void testLogoutFunctionality() throws InterruptedException {
        // Wait for the logout button and click it
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Logout']")));
        logoutButton.click();
    
        // Add a brief wait after logout click
        Thread.sleep(2000); // 2 seconds
    
        // Wait until redirected back to login page or login button is visible
        WebElement loginButtonAfterLogout = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[text()='LOGIN']")));
        Assert.assertTrue(loginButtonAfterLogout.isDisplayed(), "Logout failed - LOGIN button not visible.");
        System.out.println("Logout successful - redirected to login page.");
    }
    

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
