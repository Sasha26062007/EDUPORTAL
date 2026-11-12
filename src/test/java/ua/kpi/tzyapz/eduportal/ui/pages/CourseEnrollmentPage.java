package ua.kpi.tzyapz.eduportal.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object для сторінки самозарахування на курс (Moodle Enrolment Page).
 * Покриває сценарії TC-09, TC-10 (введення кодового слова, обробка помилок).
 *
 * Автор: Інженер з тестування / SDET Олександр Кираківський (Kyrakivskyi0)
 */
public class CourseEnrollmentPage extends BasePage {

    private final By enrolmentKeyInput = By.cssSelector("input[type='password'], input[name='enrolpassword'], #id_enrolpassword");
    private final By submitEnrollmentButton = By.cssSelector("input#id_submitbutton, button[type='submit'], input[type='submit'][value*='Enrol'], input[value*='Зарахувати']");
    private final By errorMessageContainer = By.cssSelector(".alert-danger, #id_error_enrolpassword, .error, .form-control-feedback");
    private final By enrolmentOptionsHeader = By.cssSelector(".page-header-headings h1, legend, h2");

    public CourseEnrollmentPage(WebDriver driver) {
        super(driver);
    }

    public CourseEnrollmentPage open(int courseId) {
        driver.get(BASE_URL + "/enrol/index.php?id=" + courseId);
        return this;
    }

    public boolean isEnrolmentFormDisplayed() {
        return isElementPresent(enrolmentKeyInput) || isElementPresent(submitEnrollmentButton);
    }

    public void enterKeyAndSubmit(String key) {
        if (isElementPresent(enrolmentKeyInput)) {
            type(enrolmentKeyInput, key);
        }
        click(submitEnrollmentButton);
    }

    public boolean isErrorMessageDisplayed() {
        return isElementPresent(errorMessageContainer);
    }

    public String getErrorMessage() {
        if (isElementPresent(errorMessageContainer)) {
            return getText(errorMessageContainer);
        }
        return "";
    }
}
