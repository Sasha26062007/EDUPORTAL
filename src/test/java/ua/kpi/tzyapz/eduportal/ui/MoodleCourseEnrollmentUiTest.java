package ua.kpi.tzyapz.eduportal.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ua.kpi.tzyapz.eduportal.ui.pages.CourseEnrollmentPage;
import ua.kpi.tzyapz.eduportal.ui.pages.CoursePage;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI-автотести для каталогу курсів та самозарахування (Moodle Sandbox).
 * Покривають тест-кейси Спринту 3: TC-07, TC-08, TC-09.
 *
 * Автор: Інженер з тестування / SDET Олександр Кираківський (Kyrakivskyi0)
 * Запуск: mvn test -DexcludedGroups=none -Dtest=MoodleCourseEnrollmentUiTest
 */
@Tag("ui")
@DisplayName("UI-02: Каталог курсів та самозарахування (TC-07, TC-08, TC-09)")
public class MoodleCourseEnrollmentUiTest extends BaseUiTest {

    @Test
    @DisplayName("TC-07: пошук неіснуючого курсу повертає порожній результат (сповіщення про відсутність)")
    void searchNonExistentCourseShowsNoResults() {
        // Arrange
        loginAsStudent();
        String searchQuery = "NonExistentCourse_AutoTest_2026";

        // Act: перехід до пошуку курсів (Moodle: /course/search.php?search=)
        driver.get(BASE_URL + "/course/search.php?search=" + searchQuery);

        // Assert: очікування завантаження сторінки результатів та перевірка вмісту (укр/рос/англ)
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/course/search.php"),
                ExpectedConditions.urlContains("search=")));
        String pageSource = driver.getPageSource().toLowerCase();

        assertTrue(pageSource.contains("no courses") ||
                        pageSource.contains("no results") ||
                        pageSource.contains("no matching") ||
                        pageSource.contains("нічого не знайдено") ||
                        pageSource.contains("немає курсів") ||
                        pageSource.contains("ничего не найдено") ||
                        pageSource.contains("нет курсов"),
                "Сторінка повинна містити повідомлення про відсутність результатів пошуку за неіснуючим запитом");
    }

    @Test
    @DisplayName("TC-08: відкриття сторінки курсу відображає заголовок та навчальні секції")
    void openCoursePageDisplaysSectionsAndContent() {
        // Arrange
        loginAsStudent();
        CoursePage coursePage = new CoursePage(driver);

        // Act: перехід на навчальний курс ID 2 («My first course»)
        coursePage.openCourse(2);

        // Assert: перевірка елементів курсу за допомогою assertAll
        assertAll("Перевірка сторінки навчального курсу",
                () -> assertTrue(coursePage.isLoaded(), "Сторінка курсу має успішно завантажитися"),
                () -> assertFalse(coursePage.getCourseTitle().isEmpty(), "Заголовок курсу не повинен бути порожнім"),
                () -> assertTrue(coursePage.getSectionsCount() >= 1, "Курс повинен містити щонайменше одну секцію матеріалів")
        );
    }

    @Test
    @DisplayName("TC-09: форма самозарахування доступна або перенаправляє зарахованого студента до курсу")
    void courseEnrollmentStatusVerification() {
        // Arrange
        loginAsStudent();
        CourseEnrollmentPage enrolPage = new CourseEnrollmentPage(driver);

        // Act: перехід на сторінку самозарахування курсу ID 2
        enrolPage.open(2);

        // Assert: якщо студент уже зарахований, система Moodle перенаправляє його на /course/view.php?id=2,
        // якщо ні — відображає форму із параметрами зарахування
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/course/view.php?id=2") || currentUrl.contains("/enrol/index.php?id=2"),
                "URL повинен вести або на перегляд курсу (для зарахованих), або на форму самозарахування");
    }
}
