package ua.kpi.tzyapz.eduportal.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object для сторінки навчального курсу «ЕдуПортал» (Moodle Course View).
 * Покриває перегляд матеріалів, секцій та перехід до практичних завдань.
 *
 * Автор: Інженер з тестування / SDET Олександр Кираківський (Kyrakivskyi0)
 */
public class CoursePage extends BasePage {

    private final By pageHeaderTitle = By.cssSelector(".page-header-headings h1, .page-header-content h1");
    private final By courseSections = By.cssSelector(".course-section, li.section");
    private final By activityItems = By.cssSelector(".activity-item, .modtype_assign, .modtype_quiz, .activityname");
    private final By gradesNavLink = By.cssSelector("a[data-key='grades'], a[href*='/grade/report/']");

    public CoursePage(WebDriver driver) {
        super(driver);
    }

    public CoursePage openCourse(int courseId) {
        driver.get(BASE_URL + "/course/view.php?id=" + courseId);
        wait.until(d -> isElementPresent(pageHeaderTitle) || d.getCurrentUrl().contains("/course/"));
        return this;
    }

    public String getCourseTitle() {
        return isElementPresent(pageHeaderTitle) ? getText(pageHeaderTitle) : driver.getTitle();
    }

    public boolean isLoaded() {
        return driver.getCurrentUrl().contains("/course/") || isElementPresent(pageHeaderTitle);
    }

    public int getSectionsCount() {
        return driver.findElements(courseSections).size();
    }

    public int getActivitiesCount() {
        return driver.findElements(activityItems).size();
    }

    public GradebookPage openGrades() {
        click(gradesNavLink);
        return new GradebookPage(driver);
    }
}
