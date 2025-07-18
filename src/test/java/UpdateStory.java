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

public class UpdateStory {

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
        String searchTerm = "what";

        // Step 1: Search for a story
        WebElement searchInputField = driver.findElement(By.xpath("//input[@placeholder='Search Notes']"));
        searchInputField.sendKeys(searchTerm);

        WebElement searchIcon = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector(".text-slate-400.cursor-pointer.hover\\:text-black")));
        searchIcon.click();
        System.out.println("Clicked the search icon with term: " + searchTerm);

        // Step 2: Wait and click the searched story
        WebElement searchResultsGrid = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='grid grid-cols-2 gap-4']"))
        );
        WebElement searchResultTitle = searchResultsGrid.findElement(By.xpath(".//div//h6[contains(text(), '" + searchTerm + "')]"));
        Assert.assertTrue(searchResultTitle.isDisplayed(), "Story with title containing '" + searchTerm + "' not found.");
        searchResultTitle.click();
        System.out.println("Clicked the story titled '" + searchTerm + "'.");

        // Step 3: Click on "UPDATE STORY" button
        WebElement updateStoryBtn = wait.until(
            ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'UPDATE STORY')]")));
        updateStoryBtn.click();
        System.out.println("Clicked UPDATE STORY button.");

        // Step 4: Update the title field
        WebElement titleInput = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@type='text' and contains(@class, 'text-2xl')]")
            )
        );
        titleInput.clear();
        String newTitle = "This is the updated title - submitted by test";
        titleInput.sendKeys(newTitle);
        System.out.println("Entered new title: " + newTitle);

        // Step 5: Click final "Update Story" button in the form
        WebElement finalUpdateBtn = wait.until(
            ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'UPDATE STORY')]")));
        finalUpdateBtn.click();
        System.out.println("Clicked final Update Story button.");

        // Step 6: Optional — wait for toast confirmation or visual pause
        Thread.sleep(4000); // for visual confirmation or toast
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
