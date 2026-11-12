package ua.kpi.tzyapz.eduportal.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ua.kpi.tzyapz.eduportal.ui.pages.DashboardPage;
import ua.kpi.tzyapz.eduportal.ui.pages.LoginPage;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI-автотести для модуля автентифікації «ЕдуПортал» (Moodle Sandbox).
 * Покривають тест-кейси Спринту 3: TC-01, TC-02, TC-03.
 *
 * Автор: Інженер з тестування / SDET Олександр Кираківський (Kyrakivskyi0)
 * Запуск: mvn test -DexcludedGroups=none -Dtest=MoodleLoginUiTest
 */
@Tag("ui")
@DisplayName("UI-01: Автентифікація користувача та форма входу (TC-01, TC-02, TC-03)")
public class MoodleLoginUiTest extends BaseUiTest {

    @Test
    @DisplayName("TC-01: успішний вхід студента за валідними даними відкриває Dashboard (/my/)")
    void validStudentLoginOpensDashboard() {
        // Arrange
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();

        // Act: введення облікових даних demo-студента Moodle (student / sandbox24)
        DashboardPage dashboardPage = loginPage.loginAs("student", "sandbox24");

        // Assert: перевірка переходу на панель та відображення профілю
        assertAll("Перевірка стану після успішного входу",
                () -> assertTrue(dashboardPage.isLoaded(), "Після входу користувач потрапляє на панель (/ або /my/), меню користувача присутнє"),
                () -> assertTrue(dashboardPage.isPrimaryNavigationPresent(), "Головна панель навігації повинна бути присутня")
        );
    }

    @Test
    @DisplayName("TC-02: невірний пароль призводить до відображення повідомлення про помилку")
    void invalidPasswordShowsErrorMessage() {
        // Arrange
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();

        // Act: введення невірного пароля
        loginPage.loginExpectingFailure("student", "incorrect_password_123");

        // Assert: перевірка наявності повідомлення про помилку та стану форми
        assertAll("Перевірка валідації невірного пароля",
                () -> assertTrue(loginPage.isErrorMessageDisplayed(), "Має відображатися блок повідомлення про помилку"),
                () -> assertTrue(driver.getCurrentUrl().contains("/login/"),
                        "Користувач повинен залишатися на сторінці входу"),
                () -> assertTrue(loginPage.isLoginFormLoaded(), "Форма входу залишається доступною для повторної спроби")
        );
    }

    @Test
    @DisplayName("TC-03: спроба входу з порожніми полями блокується валідацією форми")
    void emptyCredentialsValidationBlocksSubmission() {
        // Arrange
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();

        // Act: натискання кнопки «Увійти» без заповнення полів
        loginPage.loginExpectingFailure("", "");

        // Assert: перевірка, що користувач залишився на сторінці входу
        assertAll("Перевірка валідації порожніх полів",
                () -> assertTrue(driver.getCurrentUrl().contains("/login/"),
                        "При порожніх полях перехід до кабінету не повинен відбуватися"),
                () -> assertTrue(loginPage.isLoginFormLoaded(), "Форма входу повинна залишатися доступною")
        );
    }
}
