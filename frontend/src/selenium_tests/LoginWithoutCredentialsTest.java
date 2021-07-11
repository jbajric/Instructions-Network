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
public class LoginWithoutCredentialsTest {
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
  public void loginWithoutCredentials() {
    driver.get("http://localhost:3000/login");
    driver.manage().window().setSize(new Dimension(1536, 824));
    driver.findElement(By.id("normal_login_username")).click();
    {
      List<WebElement> elements = driver.findElements(By.id("normal_login_username"));
      assert(elements.size() > 0);
    }
    driver.findElement(By.cssSelector(".ant-input-password")).click();
    {
      List<WebElement> elements = driver.findElements(By.cssSelector(".ant-input-password"));
      assert(elements.size() > 0);
    }
    driver.findElement(By.cssSelector(".ant-btn > span")).click();
    driver.findElement(By.cssSelector(".ant-row:nth-child(1) .ant-form-item-explain > div")).click();
    driver.findElement(By.cssSelector(".ant-row:nth-child(1) .ant-form-item-explain > div")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".ant-row:nth-child(1) .ant-form-item-explain > div"));
      Actions builder = new Actions(driver);
      builder.doubleClick(element).perform();
    }
    driver.findElement(By.cssSelector(".ant-row:nth-child(1) .ant-form-item-explain > div")).click();
    driver.findElement(By.cssSelector(".ant-row:nth-child(1) .ant-form-item-explain > div")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".ant-row:nth-child(1) .ant-form-item-explain > div"));
      Actions builder = new Actions(driver);
      builder.doubleClick(element).perform();
    }
    driver.findElement(By.cssSelector(".ant-row:nth-child(1) .ant-form-item-explain > div")).click();
    assertThat(driver.findElement(By.cssSelector(".ant-row:nth-child(1) .ant-form-item-explain > div")).getText(), is("Please input your Username!"));
    driver.findElement(By.cssSelector(".ant-row:nth-child(2) .ant-form-item-explain > div")).click();
    driver.findElement(By.cssSelector(".ant-row:nth-child(2) .ant-form-item-explain > div")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".ant-row:nth-child(2) .ant-form-item-explain > div"));
      Actions builder = new Actions(driver);
      builder.doubleClick(element).perform();
    }
    driver.findElement(By.cssSelector(".ant-row:nth-child(2) .ant-form-item-explain > div")).click();
    assertThat(driver.findElement(By.cssSelector(".ant-row:nth-child(2) .ant-form-item-explain > div")).getText(), is("Please input your Password!"));
    driver.close();
  }
}
