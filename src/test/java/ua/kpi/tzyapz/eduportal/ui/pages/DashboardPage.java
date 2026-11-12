package ua.kpi.tzyapz.eduportal.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object для головного кабінету студента (Moodle Dashboard / My page).
 * Покриває перевірки успішного входу та навігації по системі.
 *
 * Автор: Інженер з тестування / SDET Олександр Кираківський (Kyrakivskyi0)
 */
public class DashboardPage extends BasePage {

    public static final String DASHBOARD_URL = BASE_URL + "/my/";

    private final By userMenuButton = By.cssSelector(".userbutton, .usermenu, #user-menu-toggle, a[href*='login/logout.php']");
    private final By userNameSpan = By.cssSelector(".userbutton .usertext, .usermenu .usertext, #user-menu-toggle .usertext, .usertext");
    private final By primaryNavbar = By.cssSelector(".primary-navigation, nav.navbar");
    private final By myCoursesLink = By.cssSelector("a[data-key='mycourses'], a[href*='/my/courses.php']");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        // Moodle 5.x після входу редіректить на "/" (панель сайту) або "/my/"; приймаємо обидва варіанти.
        wait.until(d -> !d.getCurrentUrl().contains("/login/"));
        return isElementPresent(userMenuButton);
    }

    public String getUserName() {
        return driver.findElements(userNameSpan).stream().findFirst().map(e -> e.getText().trim()).orElse("");
    }

    public boolean isPrimaryNavigationPresent() {
        return isElementPresent(primaryNavbar);
    }

    public void openMyCourses() {
        click(myCoursesLink);
    }
}
