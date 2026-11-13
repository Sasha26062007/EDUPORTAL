# Специфікація автоматизованих UI-сценаріїв (Selenium WebDriver 4)

**Дисципліна:** Технології забезпечення якості програмних засобів (ТЗЯПЗ)  
**Проєкт:** Автоматизована система дистанційного навчання «ЕдуПортал» (LMS)  
**Спринт 4:** «Автоматизація тестування: JUnit 5 та Selenium WebDriver»  
**Тестовий стенд:** Демонстраційний екземпляр Moodle LMS (https://sandbox.moodledemo.net)  
**Автор специфікації:** Олександр Кираківський (`Kyrakivskyi0`), Інженер з тестування / SDET  
**Рецензент:** Олександр Панайотов (`5ani0chek`), Аудитор-опонент  
**Дата актуалізації:** 13 листопада 2026 року  

---

## 1. Загальний огляд UI-комплексу

Для наскрізного функціонального регресійного тестування веб-інтерфейсу реалізовано **11 автоматизованих сценаріїв** на базі Java, JUnit 5 та **Selenium WebDriver 4.21.0** з використанням архітектурного патерну **Page Object Model (POM)**.

Кожен тестовий метод має анотацію `@Tag("ui")`, пряму прив'язку до затвердженого тест-кейсу Спринту 3 у `@DisplayName`, явні динамічні очікування `WebDriverWait` (без антипатерну `Thread.sleep`) та строгі валідації стану DOM-елементів.

---

## 2. Матриця простежуваності UI-сценаріїв до тест-кейсів Спринту 3

| ID тест-кейсу С3 | Назва сценарію у Спринті 3 | Тестовий клас JUnit 5 | Тест-метод у коді Java | Цільовий Page Object | Результат у стенді |
|:---:|:---|:---|:---|:---|:---:|
| **TC-01** | Успішний вхід студента з валідними даними | `MoodleLoginUiTest` | `validStudentLoginOpensDashboard()` | `LoginPage`, `DashboardPage` | **PASSED** |
| **TC-02** | Помилка входу з невірним паролем, збереження логіна | `MoodleLoginUiTest` | `invalidPasswordShowsErrorMessage()` | `LoginPage` | **PASSED** |
| **TC-03** | Спроба входу з порожніми полями блокується | `MoodleLoginUiTest` | `emptyCredentialsValidationBlocksSubmission()` | `LoginPage` | **PASSED** |
| **TC-07** | Пошук неіснуючого курсу повертає порожній результат | `MoodleCourseEnrollmentUiTest` | `searchNonExistentCourseShowsNoResults()` | `CourseEnrollmentPage` | **PASSED** |
| **TC-08** | Сторінка курсу: секції, матеріали та заголовок | `MoodleCourseEnrollmentUiTest` | `openCoursePageDisplaysSectionsAndContent()` | `CoursePage` | **PASSED** |
| **TC-09** | Статус зарахування та доступність форми самореєстрації | `MoodleCourseEnrollmentUiTest` | `courseEnrollmentStatusVerification()` | `CourseEnrollmentPage` | **PASSED** |
| **TC-12** | Форма завдання: перевірка заголовка та області подання | `MoodleAssignmentUiTest` | `assignmentPageDisplaysTitleAndStatus()` | `AssignmentPage` | **PASSED** |
| **TC-15** | Таблиця статусу здачі містить стан оцінювання та дедлайн | `MoodleAssignmentUiTest` | `submissionStatusContainsGradingAndTimingInfo()` | `AssignmentPage` | **PASSED** |
| **TC-16** | Доступ викладача до перегляду робіт та панелі керування | `MoodleAssignmentUiTest` | `teacherCanAccessSubmissionsOverview()` | `AssignmentPage`, `CoursePage` | **PASSED** |
| **TC-25** | Персональний журнал оцінок студента (User Report) | `MoodleGradebookUiTest` | `studentCanViewPersonalGradesReport()` | `GradebookPage` | **PASSED** |
| **TC-26** | Журнал викладача (Grader Report) зі зведеною відомістю | `MoodleGradebookUiTest` | `teacherCanAccessCourseGraderReport()` | `GradebookPage` | **PASSED** |

---

## 3. Детальний покроковий опис сценаріїв та локаторів

### 3.1. Набір `MoodleLoginUiTest` (Автентифікація: TC-01, TC-02, TC-03)

#### Сценарій 1: TC-01 — Успішний вхід студента за валідними даними
* **Тест-метод:** `MoodleLoginUiTest.validStudentLoginOpensDashboard()`
* **Пов'язана вимога:** F-10, GUI-10
* **Кроки та локатори дій:**
  1. `driver.get("https://sandbox.moodledemo.net/login/index.php")` — відкриття сторінки входу.
  2. Знайти `By.id("username")`, очистити та ввести `"student"`.
  3. Знайти `By.id("password")`, ввести `"sandbox24"`.
  4. Натиснути кнопку `By.id("loginbtn")`.
  5. Очікування `WebDriverWait`: `ExpectedConditions.not(urlContains("/login/index.php"))`.
* **Перевірки (Asserts):**
  - URL перенаправлення відповідає головній панелі (`/` або `/my/`);
  - Наявне меню користувача `By.cssSelector(".usermenu, .userbutton")`;
  - Наявна панель первинної навігації `By.cssSelector(".primary-navigation, nav.navbar")`.

#### Сценарій 2: TC-02 — Невірний пароль призводить до відображення повідомлення про помилку
* **Тест-метод:** `MoodleLoginUiTest.invalidPasswordShowsErrorMessage()`
* **Пов'язана вимога:** F-10, GUI-20
* **Кроки та локатори дій:**
  1. Відкрити сторінку авторизації.
  2. Ввести логін `"student"` та некоректний пароль `"incorrect_password_123"`.
  3. Натиснути `loginbtn`.
  4. Очікування `wait.until(d -> isElementPresent(errorMessageDiv) || d.getCurrentUrl().contains("/login/"))`.
* **Перевірки (Asserts):**
  - Блок помилки `By.cssSelector("#loginerrormessage, .alert-danger, .login-form-error, [role='alert'], .loginerrors")` відображається (`isDisplayed() == true`);
  - Користувач залишається на формі входу (`driver.getCurrentUrl().contains("/login/")`);
  - Поля входу доступні для повторної спроби.

#### Сценарій 3: TC-03 — Спроба входу з порожніми полями блокується валідацією форми
* **Тест-метод:** `MoodleLoginUiTest.emptyCredentialsValidationBlocksSubmission()`
* **Пов'язана вимога:** F-10, S-20
* **Кроки та локатори дій:**
  1. Відкрити форму входу.
  2. Не заповнюючи поля, викликати натискання кнопки `loginbtn`.
* **Перевірки (Asserts):**
  - Форма не виконує перехід до захищеного кабінету: URL залишається на `/login/`;
  - Елементи форми (`#username`, `#loginbtn`) залишаються активними в DOM-дереві.

---

### 3.2. Набір `MoodleCourseEnrollmentUiTest` (Каталог та самозарахування: TC-07, TC-08, TC-09)

#### Сценарій 4: TC-07 — Пошук неіснуючого курсу повертає сповіщення про порожній результат
* **Тест-метод:** `MoodleCourseEnrollmentUiTest.searchNonExistentCourseShowsNoResults()`
* **Пов'язана вимога:** F-30
* **Кроки та локатори дій:**
  1. Авторизуватися студентом (`student` / `sandbox24`).
  2. Виконати навігацію за адресою `/course/search.php?search=NonExistentCourse_AutoTest_2026`.
  3. Очікування: `wait.until(ExpectedConditions.urlContains("/course/search.php"))`.
* **Перевірки (Asserts):**
  - Сторінка результатів містить системне повідомлення про відсутність збігів: «No courses were found», «Нічого не знайдено» або «No results».

#### Сценарій 5: TC-08 — Відкриття сторінки курсу відображає заголовок та навчальні секції
* **Тест-метод:** `MoodleCourseEnrollmentUiTest.openCoursePageDisplaysSectionsAndContent()`
* **Пов'язана вимога:** F-50, GUI-75
* **Кроки та локатори дій:**
  1. Авторизуватися студентом.
  2. Відкрити курс за ID: `/course/view.php?id=2`.
  3. Очікування появи контенту: `By.cssSelector(".course-content, .course-header, h1")`.
* **Перевірки (Asserts):**
  - Заголовок сторінки курсу не є порожнім (`getCourseTitle().isEmpty() == false`);
  - Кількість виявлених навчальних секцій `By.cssSelector("li.section, div.course-section, .sectionname")` $\ge 1$.

#### Сценарій 6: TC-09 — Статус самозарахування та перенаправлення
* **Тест-метод:** `MoodleCourseEnrollmentUiTest.courseEnrollmentStatusVerification()`
* **Пов'язана вимога:** F-40, GUI-60, BL-70
* **Кроки та локатори дій:**
  1. Авторизуватися студентом.
  2. Звернутися до ендпоінту зарахування курсу: `/enrol/index.php?id=2`.
* **Перевірки (Asserts):**
  - Для вже зарахованого студента відбувається безпечний 303/302 редірект на сторінку матеріалів курсу `/course/view.php?id=2`;
  - Для незарахованого студента відкривається інтерфейс самореєстрації з формою кодового слова.

---

### 3.3. Набір `MoodleAssignmentUiTest` (Здача робіт: TC-12, TC-15, TC-16)

#### Сценарій 7: TC-12 — Відкриття сторінки завдання відображає заголовок та область подання
* **Тест-метод:** `MoodleAssignmentUiTest.assignmentPageDisplaysTitleAndStatus()`
* **Пов'язана вимога:** GUI-30, F-60
* **Кроки та локатори дій:**
  1. Авторизуватися студентом.
  2. Відкрити сторінку завдання `/mod/assign/view.php?id=1`.
* **Перевірки (Asserts):**
  - URL переходу підтверджує відкриття завдання модуля `assign` або сторінки матеріалів курсу;
  - Заголовок та контейнер завдання завантажуються успішно.

#### Сценарій 8: TC-15 — Таблиця статусу здачі містить дані про стан оцінювання та терміни
* **Тест-метод:** `MoodleAssignmentUiTest.submissionStatusContainsGradingAndTimingInfo()`
* **Пов'язана вимога:** F-60, BL-30
* **Кроки та локатори дій:**
  1. Авторизуватися студентом.
  2. Відкрити сторінку завдання `/mod/assign/view.php?id=1`.
  3. Знайти таблицю статусу `By.cssSelector(".submissionstatustable, table.generaltable")`.
* **Перевірки (Asserts):**
  - При наявності таблиці перевіряється обов'язкова наявність полів статусу («Status» / «Статус») та інформації щодо термінів / оцінювання («Due» / «Grading» / «Оцінювання»);
  - Сторінка підтверджує валідне відображення активності Moodle LMS.

#### Сценарій 9: TC-16 — Доступ викладача до панелі керування та перегляду робіт
* **Тест-метод:** `MoodleAssignmentUiTest.teacherCanAccessSubmissionsOverview()`
* **Пов'язана вимога:** F-70, BL-30
* **Кроки та локатори дій:**
  1. Авторизуватися викладачем (`teacher` / `sandbox24`).
  2. Відкрити курс `/course/view.php?id=2`.
  3. Очікування: `wait.until(ExpectedConditions.urlContains("/course/view.php"))`.
* **Перевірки (Asserts):**
  - Сторінка підтверджує наявність адміністративних прав викладача: елементи перемикача режиму редагування («Edit mode», «Режим редагування») або панелі викладача («Teacher»).

---

### 3.4. Набір `MoodleGradebookUiTest` (Журнал оцінок: TC-25, TC-26)

#### Сценарій 10: TC-25 — Персональний журнал оцінок студента відображає звіт успішності
* **Тест-метод:** `MoodleGradebookUiTest.studentCanViewPersonalGradesReport()`
* **Пов'язана вимога:** F-100, GUI-45
* **Кроки та локатори дій:**
  1. Авторизуватися студентом.
  2. Викликати перехід за посиланням журналу користувача: `/grade/report/user/index.php?id=2`.
  3. Очікування завантаження: `wait.until(ExpectedConditions.urlContains("/grade/report/"))`.
* **Перевірки (Asserts):**
  - Завантажений вміст сторінки містить структуру звіту оцінок («Grade», «User report», «Оцінк» або «Звіт»).

#### Сценарій 11: TC-26 — Доступ викладача до зведеного журналу (Grader Report)
* **Тест-метод:** `MoodleGradebookUiTest.teacherCanAccessCourseGraderReport()`
* **Пов'язана вимога:** F-100, BL-20
* **Кроки та локатори дій:**
  1. Авторизуватися викладачем (`teacher` / `sandbox24`).
  2. Відкрити зведений журнал курсу: `/grade/report/grader/index.php?id=2`.
  3. Очікування завантаження: `wait.until(ExpectedConditions.urlContains("/grade/report/"))`.
* **Перевірки (Asserts):**
  - Наявність зведеної таблиці відомості: `By.cssSelector("table.gradestable, table.generaltable, .gradetreebox, .grade-report-grader, #grade-report")`;
  - Наявність контексту журналу викладача («Grader», «Оцінки», «Grade»).
