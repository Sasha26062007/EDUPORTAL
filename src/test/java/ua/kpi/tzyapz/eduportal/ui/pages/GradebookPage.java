package ua.kpi.tzyapz.eduportal.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object для персонального журналу успішності (Moodle Gradebook User Report).
 * Покриває сценарії TC-25, TC-26 (перегляд оцінок студента, шкала ECTS).
 *
 * Автор: Інженер з тестування / SDET Олександр Кираківський (Kyrakivskyi0)
 */
public class GradebookPage extends BasePage {

    private final By userGradeTable = By.cssSelector(".user-grade, table.generaltable, .grade-report-user");
    private final By pageHeading = By.cssSelector(".page-header-headings h1, h2");
    private final By gradeItemRows = By.cssSelector("tbody tr");

    public GradebookPage(WebDriver driver) {
        super(driver);
    }

    public GradebookPage open(int courseId) {
        driver.get(BASE_URL + "/grade/report/user/index.php?id=" + courseId);
        return this;
    }

    public boolean isGradeTableVisible() {
        return isElementPresent(userGradeTable) && waitForVisibility(userGradeTable).isDisplayed();
    }

    public int getGradeItemsCount() {
        return driver.findElements(gradeItemRows).size();
    }

    public String getPageHeading() {
        return getText(pageHeading);
    }
}
