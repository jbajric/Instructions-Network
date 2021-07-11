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
public class InvalidEmailTest {
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
  public void invalidEmail() {
    driver.get("http://localhost:3000/registration");
    driver.manage().window().setSize(new Dimension(1536, 824));
    driver.findElement(By.id("normal_login_firstname")).click();
    driver.findElement(By.id("normal_login_firstname")).sendKeys("Nejira");
    driver.findElement(By.id("normal_login_lastname")).click();
    driver.findElement(By.id("normal_login_lastname")).sendKeys("Music");
    driver.findElement(By.id("normal_login_email")).click();
    driver.findElement(By.id("normal_login_email")).sendKeys("nmusic");
    driver.findElement(By.id("normal_login_username")).click();
    driver.findElement(By.id("normal_login_username")).sendKeys("nmusic");
    driver.findElement(By.id("normal_login_password")).click();
    driver.findElement(By.id("normal_login_password")).sendKeys("password");
    driver.findElement(By.cssSelector(".ant-radio-wrapper:nth-child(2) .ant-radio-input")).click();
    driver.findElement(By.id("normal_login_firstname")).click();
    driver.findElement(By.id("normal_login_firstname")).click();
    {
      WebElement element = driver.findElement(By.id("normal_login_firstname"));
      Actions builder = new Actions(driver);
      builder.doubleClick(element).perform();
    }
    driver.findElement(By.id("normal_login_firstname")).click();
    {
      List<WebElement> elements = driver.findElements(By.id("normal_login_firstname"));
      assert(elements.size() > 0);
    }
    driver.findElement(By.cssSelector(".ant-input-affix-wrapper-focused")).click();
    driver.findElement(By.id("normal_login_lastname")).click();
    driver.findElement(By.id("normal_login_lastname")).click();
    {
      WebElement element = driver.findElement(By.id("normal_login_lastname"));
      Actions builder = new Actions(driver);
      builder.doubleClick(element).perform();
    }
    {
      List<WebElement> elements = driver.findElements(By.id("normal_login_lastname"));
      assert(elements.size() > 0);
    }
    driver.findElement(By.cssSelector(".ant-input-affix-wrapper-focused")).click();
    driver.findElement(By.id("normal_login_email")).click();
    driver.findElement(By.id("normal_login_email")).click();
    {
      WebElement element = driver.findElement(By.id("normal_login_email"));
      Actions builder = new Actions(driver);
      builder.doubleClick(element).perform();
    }
    {
      List<WebElement> elements = driver.findElements(By.id("normal_login_email"));
      assert(elements.size() > 0);
    }
    driver.findElement(By.cssSelector(".ant-input-affix-wrapper-focused")).click();
    driver.findElement(By.id("normal_login_username")).click();
    driver.findElement(By.id("normal_login_username")).click();
    {
      WebElement element = driver.findElement(By.id("normal_login_username"));
      Actions builder = new Actions(driver);
      builder.doubleClick(element).perform();
    }
    driver.findElement(By.id("normal_login_username")).click();
    {
      List<WebElement> elements = driver.findElements(By.id("normal_login_username"));
      assert(elements.size() > 0);
    }
    driver.findElement(By.cssSelector(".ant-input-password")).click();
    driver.findElement(By.id("normal_login_password")).click();
    driver.findElement(By.id("normal_login_password")).click();
    {
      WebElement element = driver.findElement(By.id("normal_login_password"));
      Actions builder = new Actions(driver);
      builder.doubleClick(element).perform();
    }
    {
      List<WebElement> elements = driver.findElements(By.id("normal_login_password"));
      assert(elements.size() > 0);
    }
    assertTrue(driver.findElement(By.cssSelector(".ant-radio-checked > .ant-radio-input")).isSelected());
    assertThat(driver.findElement(By.cssSelector(".ant-form-item-explain > div")).getText(), is("The input is not valid e-mail!"));
    driver.close();
  }
}
