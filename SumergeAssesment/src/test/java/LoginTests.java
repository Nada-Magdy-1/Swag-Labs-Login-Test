import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTests {
    private WebDriver driver;

    @BeforeMethod
    public void setup(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
    }

    @AfterMethod
    public void teardown() throws InterruptedException {
        Thread.sleep(5000);
        driver.quit();
    }

    @Test (priority = 0)
    public void testLoginElementsPresence(){
        WebElement usernameInput = driver.findElement(By.id("user-name"));
        Assert.assertTrue(usernameInput.isDisplayed());

        WebElement passwordInput = driver.findElement(By.id("password"));
        Assert.assertTrue(passwordInput.isDisplayed());

        WebElement loginButton = driver.findElement(By.id("login-button"));
        Assert.assertTrue(loginButton.isDisplayed());

    }

    @Test (priority = 1)
    public void testValidCredentials(){
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        WebElement swagLabsDiv = driver.findElement(By.xpath("//div[contains(text(),'Swag Labs')]"));
        Assert.assertTrue(swagLabsDiv.isDisplayed());
    }

    @Test (priority = 2)
    public void testWrongCredentials(){
        driver.findElement(By.id("user-name")).sendKeys("invalid_user");
        driver.findElement(By.id("password")).sendKeys("invalid_password");
        driver.findElement(By.id("login-button")).click();

        WebElement errorMessageDiv = driver.findElement(By.xpath("//h3[@data-test='error']"));
        Assert.assertTrue(errorMessageDiv.isDisplayed());
        Assert.assertTrue(errorMessageDiv.getText().contains("Epic sadface: Username and password do not match any user in this service"), "Error message text should be correct");
    }


    @Test (priority = 3)
    public void testEmptyCredentials(){
        //Without UserName
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        WebElement emptyUsernameErrorMessageDiv = driver.findElement(By.xpath("//h3[@data-test='error']"));
        Assert.assertTrue(emptyUsernameErrorMessageDiv.isDisplayed(), "Error message should be displayed for empty username");
        Assert.assertTrue(emptyUsernameErrorMessageDiv.getText().contains("Epic sadface: Username is required"), "Error message text should be correct for empty username");

        driver.navigate().refresh();


        //Without Password
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("login-button")).click();

        WebElement emptyPasswordErrorMessage = driver.findElement(By.xpath("//h3[@data-test='error']"));
        Assert.assertTrue(emptyPasswordErrorMessage.isDisplayed(), "Error message should be displayed for empty password");
        Assert.assertTrue(emptyPasswordErrorMessage.getText().contains("Epic sadface: Password is required"), "Error message text should be correct for empty password");
    }
}
