// File: src/test/java/AddTofavourite.java

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
import java.util.List;

public class AddTofavourite {

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
        System.out.println("Login successful, navigated to home page for adding to favorite.");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testAddToFavouriteFromHomePage() {
        // Step 2: Wait for the travel story cards to load on the home page
        WebDriverWait waitStoryCards = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> storyCards = waitStoryCards.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='grid grid-cols-2 gap-4']/div"))
        );

        // Assert that at least one story card is present
        Assert.assertTrue(storyCards.size() > 0, "No travel story cards found on the home page.");
        System.out.println("Found " + storyCards.size() + " travel story cards on the home page.");

        // Step 3: Locate the favorite button within the first story card and click it
        if (!storyCards.isEmpty()) {
            WebElement firstStoryCard = storyCards.get(0);
            WebDriverWait waitFavoriteButton = new WebDriverWait(driver, Duration.ofSeconds(15));
            WebElement favoriteButton = waitFavoriteButton.until(
                    ExpectedConditions.elementToBeClickable(firstStoryCard.findElement(By.xpath(".//button[contains(@class, 'absolute')]")))
            );
            favoriteButton.click();
            System.out.println("Clicked the favorite button on the first story card.");

            try {
                Thread.sleep(5000); // Pause for visual inspection (remove for automated runs)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else {
            Assert.fail("Could not find any story cards to favorite.");
        }
    }
}