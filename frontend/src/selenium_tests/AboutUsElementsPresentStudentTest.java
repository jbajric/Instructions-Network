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
public class AboutUsElementsPresentStudentTest {
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
  public void aboutUsElementsPresentStudent() {
    driver.get("http://localhost:3000/");
    driver.manage().window().setSize(new Dimension(1536, 824));
    driver.findElement(By.id("normal_login_username")).click();
    driver.findElement(By.id("normal_login_username")).sendKeys("nmusic");
    driver.findElement(By.id("normal_login_password")).click();
    driver.findElement(By.id("normal_login_password")).sendKeys("password");
    driver.findElement(By.cssSelector(".ant-btn > span")).click();
    driver.findElement(By.cssSelector("div:nth-child(1) > div > h1")).click();
    {
      WebDriverWait wait = new WebDriverWait(driver, 6);
      wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div:nth-child(1) > div > h1")));
    }
    driver.findElement(By.linkText("About Us")).click();
    {
      WebDriverWait wait = new WebDriverWait(driver, 6);
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1")));
    }
    driver.findElement(By.cssSelector("h1")).click();
    {
      List<WebElement> elements = driver.findElements(By.cssSelector("h1"));
      assert(elements.size() > 0);
    }
    driver.findElement(By.cssSelector("p:nth-child(2)")).click();
    {
      List<WebElement> elements = driver.findElements(By.cssSelector("p:nth-child(2)"));
      assert(elements.size() > 0);
    }
    driver.findElement(By.cssSelector("#aboutus > div:nth-child(2) > p")).click();
    {
      List<WebElement> elements = driver.findElements(By.cssSelector("#aboutus > div:nth-child(2) > p"));
      assert(elements.size() > 0);
    }
    {
      List<WebElement> elements = driver.findElements(By.cssSelector(".col-sm-4:nth-child(1) .card-img-top"));
      assert(elements.size() > 0);
    }
    driver.findElement(By.cssSelector(".col-sm-4:nth-child(1) .card-text")).click();
    {
      List<WebElement> elements = driver.findElements(By.cssSelector(".col-sm-4:nth-child(1) .card-text"));
      assert(elements.size() > 0);
    }
    {
      List<WebElement> elements = driver.findElements(By.cssSelector(".col-sm-4:nth-child(2) .card-img-top"));
      assert(elements.size() > 0);
    }
    driver.findElement(By.cssSelector(".col-sm-4:nth-child(2) .card-text")).click();
    driver.findElement(By.cssSelector(".col-sm-4:nth-child(2) .card-text")).click();
    {
      List<WebElement> elements = driver.findElements(By.cssSelector(".col-sm-4:nth-child(2) .card-text"));
      assert(elements.size() > 0);
    }
    {
      List<WebElement> elements = driver.findElements(By.cssSelector(".col-sm-4:nth-child(3) .card-img-top"));
      assert(elements.size() > 0);
    }
    driver.findElement(By.cssSelector(".col-sm-4:nth-child(3) .card-text")).click();
    {
      List<WebElement> elements = driver.findElements(By.cssSelector(".col-sm-4:nth-child(3) .card-img-top"));
      assert(elements.size() > 0);
    }
    driver.close();
  }
}
