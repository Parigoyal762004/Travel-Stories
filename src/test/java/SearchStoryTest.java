// File: src/test/java/SearchStoryTest.java

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
// import org.openqa.selenium.JavascriptExecutor; // Import for JavaScriptExecutor

public class SearchStoryTest {

    WebDriver driver;
    String loginUrl = "http://localhost:5173/login";
    String homeUrlAfterLogin = "http://localhost:5173/";

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

        // Wait for successful login and home page load
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement homePageLogo = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@alt='travel story']")));
        Assert.assertTrue(homePageLogo.isDisplayed(), "Login failed or home page did not load.");
        System.out.println("Login successful, navigated to home page for search test.");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testSearchStoryFunctionality() {
        // Step 2: Search for a story
        String searchTerm = "what";
        WebElement searchInputField = driver.findElement(By.xpath("//input[@placeholder='Search Notes']"));
        searchInputField.sendKeys(searchTerm);
        System.out.println("Entered search term: " + searchTerm);

        // Step 3: Explicitly click the search icon
        WebDriverWait waitSearchIcon = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement searchIcon = waitSearchIcon.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".text-slate-400.cursor-pointer.hover\\:text-black")));
        searchIcon.click();
        System.out.println("Clicked the search icon.");

        // Step 4: Wait for the search results to update and a story with the search term to be visible
        WebDriverWait waitResultsContainer = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement searchResultsGrid = waitResultsContainer.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='grid grid-cols-2 gap-4']"))
        );
        WebElement searchResultTitle = searchResultsGrid.findElement(By.xpath(".//div//h6[contains(text(), '" + searchTerm + "')]"));

        // Assert that the matching story title is displayed
        Assert.assertTrue(searchResultTitle.isDisplayed(), "A story with the title containing '" + searchTerm + "' is visible in the search results.");
        System.out.println("Search Test Passed: A story with the title containing '" + searchTerm + "' is displayed after search.");

        try {
            Thread.sleep(7000); // Increased pause for visual inspection
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}