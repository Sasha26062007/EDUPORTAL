package ua.kpi.tzyapz.eduportal.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object для сторінки практичного завдання (Moodle Assignment).
 * Покриває сценарії TC-12, TC-15, TC-16 (перевірка дедлайну, статусу та форми здачі).
 *
 * Автор: Інженер з тестування / SDET Олександр Кираківський (Kyrakivskyi0)
 */
public class AssignmentPage extends BasePage {

    private final By assignmentTitle = By.cssSelector(".page-header-headings h1, h2");
    private final By submissionStatusTable = By.cssSelector(".submissionstatustable, table.generaltable");
    private final By addSubmissionBtn = By.cssSelector("button[type='submit'][class*='btn-primary'], a[href*='action=editsubmission']");
    private final By fileUploadContainer = By.cssSelector(".filemanager, .fitem_fdragdrop, .filepicker");
    private final By assignmentIntro = By.cssSelector("#intro, .activity-description");

    public AssignmentPage(WebDriver driver) {
        super(driver);
    }

    public AssignmentPage open(int assignmentId) {
        driver.get(BASE_URL + "/mod/assign/view.php?id=" + assignmentId);
        return this;
    }

    public String getTitleText() {
        return getText(assignmentTitle);
    }

    public boolean isSubmissionStatusTablePresent() {
        return isElementPresent(submissionStatusTable);
    }

    public boolean isAddSubmissionButtonPresent() {
        return isElementPresent(addSubmissionBtn);
    }

    public void clickAddSubmission() {
        if (isAddSubmissionButtonPresent()) {
            click(addSubmissionBtn);
        }
    }

    public boolean isFileUploadAreaPresent() {
        return isElementPresent(fileUploadContainer);
    }
}
