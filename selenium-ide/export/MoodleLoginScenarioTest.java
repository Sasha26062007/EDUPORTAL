package ua.kpi.tzyapz.eduportal.ui;

// Згенеровано Selenium IDE (Export -> Java JUnit)
// Чернетка у стилі JUnit 4 для демонстрації адаптації у фінальний WebDriver-тест JUnit 5
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import java.util.*;

public class MoodleLoginScenarioTest {
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
    public void tc01ValidStudentLogin() {
        driver.get("https://sandbox.moodledemo.net/login/index.php");
        driver.manage().window().setSize(new Dimension(1280, 900));
        driver.findElement(By.id("username")).sendKeys("student");
        driver.findElement(By.id("password")).sendKeys("sandbox24");
        driver.findElement(By.id("loginbtn")).click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertThat(driver.findElement(By.cssSelector(".userbutton .usertext")).getText(), is("Student User"));
    }

    @Test
    public void tc02InvalidPassword() {
        driver.get("https://sandbox.moodledemo.net/login/index.php");
        driver.manage().window().setSize(new Dimension(1280, 900));
        driver.findElement(By.id("username")).sendKeys("student");
        driver.findElement(By.id("password")).sendKeys("wrong_pwd");
        driver.findElement(By.id("loginbtn")).click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertThat(driver.findElement(By.id("username")).getAttribute("value"), is("student"));
        assertTrue(driver.findElement(By.cssSelector(".alert-danger")).isDisplayed());
    }
}
