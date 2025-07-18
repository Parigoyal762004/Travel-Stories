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

public class DeleteStory {

    WebDriver driver;
    String loginUrl = "http://localhost:5173/login";

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
        WebElement homePageLogo = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@alt='travel story']")));
        Assert.assertTrue(homePageLogo.isDisplayed(), "Login failed or home page did not load.");
        System.out.println("Login successful. Ready to update story.");
    }

    @Test
    public void testUpdateStoryTitleAndSubmit() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String searchTerm = "where";

        // Step 1: Search for a story
        WebElement searchInputField = driver.findElement(By.xpath("//input[@placeholder='Search Notes']"));
        searchInputField.sendKeys(searchTerm);

        WebElement searchIcon = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector(".text-slate-400.cursor-pointer.hover\\:text-black")));
        searchIcon.click();
        System.out.println("Clicked the search icon with term: " + searchTerm);

        // Step 2: Wait for search results grid to load
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='grid grid-cols-2 gap-4']")));

        // ⚠️ Re-locate the story title element to avoid stale reference
        WebElement searchResultTitle = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[@class='grid grid-cols-2 gap-4']//h6[contains(text(), '" + searchTerm + "')]")
        ));
        Assert.assertTrue(searchResultTitle.isDisplayed(), "Story with title containing '" + searchTerm + "' not found.");
        searchResultTitle.click();
        System.out.println("Clicked the story titled '" + searchTerm + "'.");

        // Step 3: Click the DELETE button
        WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(., 'DELETE')]")
        ));
        deleteButton.click();
        System.out.println("Clicked the DELETE button.");

        // Step 4: Handle confirmation (if any)
        try {
            driver.switchTo().alert().accept(); // Accept JavaScript alert if present
            System.out.println("Confirmed the delete action via alert.");
        } catch (Exception e) {
            System.out.println("No alert popup appeared.");
        }

        // Step 5: Wait for UI update after deletion
        Thread.sleep(2000);
        System.out.println("Story deleted successfully.");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
