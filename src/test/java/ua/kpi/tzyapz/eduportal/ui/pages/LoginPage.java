package ua.kpi.tzyapz.eduportal.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page Object для форми авторизації платформи «ЕдуПортал» (Moodle Login).
 * Покриває сценарії TC-01, TC-02, TC-03.
 *
 * Автор: Інженер з тестування / SDET Олександр Кираківський (Kyrakivskyi0)
 */
public class LoginPage extends BasePage {

    public static final String LOGIN_URL = BASE_URL + "/login/index.php";

    // Стійкі By-локатори на базі унікальних ID та CSS-класів форми входу Moodle
    private final By usernameInput = By.id("username");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("loginbtn");
    private final By guestLoginButton = By.id("loginguestbtn");
    private final By errorMessageDiv = By.cssSelector("#loginerrormessage, .alert-danger, .login-form-error, [role='alert'], .loginerrors");
    private final By pageHeading = By.cssSelector("h1.h2, .loginform h1");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get(LOGIN_URL);
        waitForVisibility(usernameInput);
        return this;
    }

    public DashboardPage loginAs(String username, String password) {
        type(usernameInput, username);
        type(passwordInput, password);
        click(loginButton);
        wait.until(d -> !d.getCurrentUrl().contains("/login/index.php") || isElementPresent(By.cssSelector(".usermenu, .userbutton, .primary-navigation")));
        return new DashboardPage(driver);
    }

    public LoginPage loginExpectingFailure(String username, String password) {
        type(usernameInput, username);
        type(passwordInput, password);
        click(loginButton);
        wait.until(d -> isElementPresent(errorMessageDiv) || d.getCurrentUrl().contains("/login/"));
        return this;
    }

    public String getUsernameFieldValue() {
        return getAttribute(usernameInput, "value");
    }

    public String getPasswordFieldValue() {
        return getAttribute(passwordInput, "value");
    }

    public String getErrorMessageText() {
        return getText(errorMessageDiv);
    }

    public boolean isErrorMessageDisplayed() {
        return isElementPresent(errorMessageDiv) && waitForVisibility(errorMessageDiv).isDisplayed();
    }

    public boolean isLoginFormLoaded() {
        return isElementPresent(usernameInput) && isElementPresent(loginButton);
    }

    public void clickGuestLogin() {
        click(guestLoginButton);
    }
}
