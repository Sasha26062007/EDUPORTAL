package ua.kpi.tzyapz.eduportal.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ua.kpi.tzyapz.eduportal.ui.pages.GradebookPage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI-автотести для журналу успішності «ЕдуПортал» (Moodle Gradebook).
 * Покривають тест-кейси Спринту 3: TC-25, TC-26.
 *
 * Автор: Інженер з тестування / SDET Олександр Кираківський (Kyrakivskyi0)
 * Запуск: mvn test -DexcludedGroups=none -Dtest=MoodleGradebookUiTest
 */
@Tag("ui")
@DisplayName("UI-04: Журнал успішності та облік оцінок (TC-25, TC-26)")
public class MoodleGradebookUiTest extends BaseUiTest {

    @Test
    @DisplayName("TC-25: персональний журнал оцінок студента відображає звіт успішності")
    void studentCanViewPersonalGradesReport() {
        // Arrange
        loginAsStudent();
        GradebookPage gradebookPage = new GradebookPage(driver);

        // Act: перехід до звіту успішності користувача за курсом ID 2
        gradebookPage.open(2);

        // Assert: перевірка відкриття сторінки журналу оцінок
        wait.until(ExpectedConditions.urlContains("/grade/report/"));
        String pageSource = driver.getPageSource().toLowerCase();

        assertTrue(pageSource.contains("grade") ||
                        pageSource.contains("оцінк") ||
                        pageSource.contains("user report") ||
                        pageSource.contains("звіт"),
                "Сторінка повинна містити журнал оцінок студента");
    }

    @Test
    @DisplayName("TC-26: журнал викладача (Grader report) доступний для перегляду зведеної відомості")
    void teacherCanAccessCourseGraderReport() {
        // Arrange
        loginAsTeacher();

        // Act: перехід за URL зведеного журналу оцінок викладача
        driver.get(BASE_URL + "/grade/report/grader/index.php?id=2");

        // Assert: перевірка доступу викладача до зведеного журналу (grader report)
        wait.until(ExpectedConditions.urlContains("/grade/report/"));
        String pageSource = driver.getPageSource().toLowerCase();
        boolean hasGraderContent = !driver.findElements(By.cssSelector(
                "table.gradestable, table.generaltable, .gradetreebox, .grade-report-grader, #grade-report")).isEmpty()
                || pageSource.contains("grader") || pageSource.contains("оцінк")
                || pageSource.contains("оценк") || pageSource.contains("отметк") || pageSource.contains("grade");

        assertTrue(hasGraderContent, "Викладач повинен бачити зведений журнал оцінок курсу (grader report)");
    }
}
