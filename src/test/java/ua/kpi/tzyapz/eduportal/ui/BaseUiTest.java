package ua.kpi.tzyapz.eduportal.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ua.kpi.tzyapz.eduportal.ui.pages.LoginPage;

import java.time.Duration;

/**
 * Базовий клас для всіх UI-тестів Selenium WebDriver 4.
 * Відповідає за життєвий цикл браузера: створення чистого екземпляра в режимі --headless=new
 * та гарантоване закриття через driver.quit() у @AfterEach без зомбі-процесів chromedriver.exe.
 *
 * Автор: Інженер з тестування / SDET Олександр Кираківський (Kyrakivskyi0)
 */
public abstract class BaseUiTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static final String BASE_URL = "https://sandbox.moodledemo.net";

    @BeforeEach
    void setUpDriver() {
        ChromeOptions options = new ChromeOptions();
        // Обов'язковий сучасний headless-режим та фіксований розмір екрана згідно з методичкою
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1280,900");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");

        // Драйвер автоматично завантажується та налаштовується Selenium Manager
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(12));
    }

    @AfterEach
    void tearDownDriver() {
        // Гарантоване закриття браузера та завершення процесу chromedriver
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception ignored) {
            } finally {
                driver = null;
            }
        }
    }

    /**
     * Допоміжний метод швидкої авторизації студентом (student / sandbox24)
     */
    protected void loginAsStudent() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        loginPage.loginAs("student", "sandbox24");
    }

    /**
     * Допоміжний метод швидкої авторизації викладачем (teacher / sandbox24)
     */
    protected void loginAsTeacher() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        loginPage.loginAs("teacher", "sandbox24");
    }
}
