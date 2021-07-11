// Generated by Selenium IDE
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;
public class OpenAboutUsPageInstructorTest {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;
  @Before
  public void setUp() {
    driver = new ChromeDriver();
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
  }
  @After
  public void tearDown() {
    driver.quit();
  }
  @Test
  public void openAboutUsPageInstructor() {
    driver.get("http://localhost:3000/");
    driver.manage().window().setSize(new Dimension(1536, 824));
    driver.findElement(By.id("normal_login_username")).click();
    driver.findElement(By.id("normal_login_username")).sendKeys("iprazina");
    driver.findElement(By.id("normal_login_password")).click();
    driver.findElement(By.id("normal_login_password")).sendKeys("password");
    driver.findElement(By.cssSelector(".ant-btn")).click();
    driver.findElement(By.cssSelector("div:nth-child(1) > div > h1")).click();
    {
      WebDriverWait wait = new WebDriverWait(driver, 6);
      wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div:nth-child(1) > div > h1")));
    }
    driver.findElement(By.linkText("About Us")).click();
    driver.findElement(By.cssSelector("h1")).click();
    {
      WebDriverWait wait = new WebDriverWait(driver, 6);
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1")));
    }
    driver.close();
  }
}
