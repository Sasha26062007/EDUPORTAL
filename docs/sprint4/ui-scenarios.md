# Специфікація UI-сценаріїв для автоматизації Selenium WebDriver

**Дисципліна:** Технології забезпечення якості програмних засобів (ТЗЯПЗ)  
**Проєкт:** Автоматизована система дистанційного навчання «ЕдуПортал» (LMS)  
**Демо-стенд тестування:** `https://sandbox.moodledemo.net` (Moodle 4.4+)  
**Спринт 4:** «Автоматизація: JUnit 5 і Selenium WebDriver»  
**Тест-аналітик:** Олександр Пархоменко (`ParkhomenkoO`), група ФБ-44  
**Призначено для:** Олександра Кираківського (`KyrakivskyiO`), Інженер з тестування / SDET  
**Дата специфікації:** 10 листопада 2026 року  

---

## 1. Загальні вимоги до проектування WebDriver-тестів

Відповідно до розділів 4.10–4.15 методичних вказівок Спринту 4, усі UI-сценарії розробляються з дотриманням таких інженерних стандартів:
1. **Стійкі локатори (Robust Locators):** Використовуються виключно селектори за унікальними атрибутами (`id`, `name`), семантичними класами та data-атрибутами. **Категорично заборонено використання абсолютних XPath** (на зразок `/html/body/div[2]/...`), які ламаються при найменшій зміні верстки.
2. **Явні очікування (Explicit Waits):** Усі переходи, перемальовування та очікування асинхронних відповідей веб-сервера реалізуються через `WebDriverWait` та `ExpectedConditions` (`visibilityOfElementLocated`, `elementToBeClickable`, `urlContains`). **Використання `Thread.sleep()` та неявних очікувань `implicitlyWait()` суворо заборонено**.
3. **Ізоляція та незалежність тестів:** Кожен тест має незалежний життєвий цикл. Запуск браузера виконується у `@BeforeEach` з параметрами `--headless=new` та явно заданим розміром вікна `--window-size=1280,900`. Обов'язкове закриття браузера `driver.quit()` виконується у `@AfterEach`.
4. **Маркування:** Кожен UI тест-клас позначається анотацією `@Tag("ui")`, що унеможливлює його запуск під час швидкого модульного тестування `mvn test` і дозволяє окремий цільовий запуск:
   ```bash
   mvn test -DexcludedGroups=none -Dtest=*UiTest
   ```

---

## 2. Реєстр та специфікація відібраних UI-сценаріїв

---

### Сценарій UI-01: Автентифікація зареєстрованого студента з валідними даними
- **Пов'язаний тест-кейс Спринту 3:** TC-001
- **Пріоритет автоматизації:** P1 (Критичний, базовий димовий тест)
- **Обґрунтування автоматизації:** Автентифікація є точкою входу до всієї закритої функціональності системи LMS. Тест обов'язково має передувати будь-яким подальшим перевіркам студентського кабінету.
- **Передумови (Preconditions):** Браузер відкрито, тестовий користувач `student` зареєстрований із паролем `sandbox24`.
- **Покроковий сценарій дій:**

| Крок | Дія користувача | Локатор елемента (By) | Очікування WebDriverWait | Очікуваний результат / Перевірка (Assertion) |
|:---:|:---|:---|:---|:---|
| 1 | Відкрити сторінку входу | `driver.get("https://sandbox.moodledemo.net/login/index.php")` | `visibilityOfElementLocated(By.id("username"))` | Сторінка входу успішно завантажена, поле логіна видиме |
| 2 | Ввести ім'я користувача | `By.id("username")` | — | У поле внесено значення `"student"` |
| 3 | Ввести пароль | `By.id("password")` | — | У поле внесено значення `"sandbox24"` |
| 4 | Натиснути кнопку «Вхід» | `By.id("loginbtn")` | `elementToBeClickable(By.id("loginbtn"))` | Відправлено POST-запит форми автентифікації |
| 5 | Очікування завантаження кабінету | `wait.until(ExpectedConditions.urlContains("/my/"))` | URL містить `/my/` | Користувача перенаправлено до особистого кабінету |
| 6 | Верифікація профілю | `By.cssSelector(".userbutton .usertext")` | `visibilityOfElementLocated(...)` | Текст імені користувача відображає ім'я студента (`assertEquals`) |

- **Фрагмент реалізації для WebDriver:**
```java
@Test
@Tag("ui")
@DisplayName("UI-01 (TC-001): Успішний вхід студента відкриває особистий кабінет /my/")
void validLoginOpensDashboard() {
    driver.get(BASE_URL + "/login/index.php");
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys("student");
    driver.findElement(By.id("password")).sendKeys("sandbox24");
    driver.findElement(By.id("loginbtn")).click();

    wait.until(ExpectedConditions.urlContains("/my/"));
    WebElement userTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".userbutton .usertext")));
    assertAll("Параметри успішного входу",
        () -> assertTrue(driver.getCurrentUrl().contains("/my/"), "URL має бути /my/"),
        () -> assertTrue(userTitle.isDisplayed(), "Ім'я користувача має відображатися")
    );
}
```

---

### Сценарій UI-02: Повідомлення про помилку автентифікації при невірному паролі
- **Пов'язаний тест-кейс Спринту 3:** TC-003
- **Пріоритет автоматизації:** P1 (Критичний негативний сценарій безпеки)
- **Обґрунтування автоматизації:** Запобігання несанкціонованому доступу; перевірка наявності валідаційного повідомлення без скидання сесії сервера.
- **Передумови (Preconditions):** Браузер відкрито, користувач не авторизований.
- **Покроковий сценарій дій:**

| Крок | Дія користувача | Локатор елемента (By) | Очікування WebDriverWait | Очікуваний результат / Перевірка (Assertion) |
|:---:|:---|:---|:---|:---|
| 1 | Відкрити сторінку входу | `driver.get(BASE_URL + "/login/index.php")` | `visibilityOfElementLocated(By.id("username"))` | Сторінка входу відкрита |
| 2 | Ввести логін | `By.id("username")` | — | Введено `"student"` |
| 3 | Ввести некоректний пароль | `By.id("password")` | — | Введено `"invalid_password_2026"` |
| 4 | Натиснути кнопку «Вхід» | `By.id("loginbtn")` | `elementToBeClickable(...)` | Запит надіслано |
| 5 | Дочекатися повідомлення про помилку | `By.cssSelector(".alert-danger, [role='alert']")` | `visibilityOfElementLocated(...)` | З'явився банер попередження |
| 6 | Перевірка вмісту помилки | Читання `getText()` блоку помилки | — | URL залишається `/login/index.php`, виведено повідомлення про помилку входу |

- **Фрагмент реалізації для WebDriver:**
```java
@Test
@Tag("ui")
@DisplayName("UI-02 (TC-003): Невірний пароль блокує вхід та відображає повідомлення про помилку")
void invalidPasswordDisplaysErrorMessage() {
    driver.get(BASE_URL + "/login/index.php");
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys("student");
    driver.findElement(By.id("password")).sendKeys("invalid_password_2026");
    driver.findElement(By.id("loginbtn")).click();

    WebElement alertBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert.alert-danger, [role='alert']")));
    assertAll("Перевірка відхилення невалідного входу",
        () -> assertTrue(driver.getCurrentUrl().contains("/login/"), "Користувач має залишитися на сторінці логіна"),
        () -> assertTrue(alertBox.isDisplayed(), "Банер помилки має бути видимим"),
        () -> assertFalse(alertBox.getText().isBlank(), "Текст помилки не повинен бути порожнім")
    );
}
```

---

### Сценарій UI-03: Коректне завершення сесії користувача (Logout)
- **Пов'язаний тест-кейс Спринту 3:** TC-005
- **Пріоритет автоматизації:** P2 (Безпека сесій та запобігання сесійному перехопленню)
- **Обґрунтування автоматизації:** Регресійний контроль інвалідації токенів авторизації.
- **Передумови (Preconditions):** Користувач попередньо авторизований у системі під логіном `student`.
- **Покроковий сценарій дій:**

| Крок | Дія користувача | Локатор елемента (By) | Очікування WebDriverWait | Очікуваний результат / Перевірка (Assertion) |
|:---:|:---|:---|:---|:---|
| 1 | Клік на випадаюче меню профілю | `By.id("user-menu-toggle")` | `elementToBeClickable(...)` | Меню користувача розкрито |
| 2 | Клік на пункт «Вихід» | `By.cssSelector("a[data-title='logout,moodle'], a[href*='login/logout.php']")` | `elementToBeClickable(...)` | Викликано процедуру завершення сесії |
| 3 | Дочекатися перенаправлення | `wait.until(ExpectedConditions.or(urlContains("login"), urlToBe(BASE_URL + "/")))` | Завантажено публічну сторінку | Сесію завершено |
| 4 | Спроба прямого доступу до кабінету | `driver.get(BASE_URL + "/my/")` | `urlContains("/login/")` | Система вимагає повторної авторизації |

---

### Сценарій UI-04: Самостійний запис студента на відкритий курс
- **Пов'язаний тест-кейс Спринту 3:** TC-010
- **Пріоритет автоматизації:** P2 (Ключовий бізнес-сценарій самозарахування)
- **Обґрунтування автоматизації:** Верифікація можливості приєднання до навчального курсу без обов'язкового введення кодового слова.
- **Передумови (Preconditions):** Студент авторизований, курс `COURSE_OPEN_12` (ID 12) відкритий для реєстрації.
- **Покроковий сценарій дій:**

| Крок | Дія користувача | Локатор елемента (By) | Очікування WebDriverWait | Очікуваний результат / Перевірка (Assertion) |
|:---:|:---|:---|:---|:---|
| 1 | Перейти на сторінку запису курсу | `driver.get(BASE_URL + "/enrol/index.php?id=12")` | `visibilityOfElementLocated(By.cssSelector(".maincontent, .page-header-headings"))` | Сторінка курсу завантажена |
| 2 | Знайти кнопку самореєстрації | `By.cssSelector("input[type='submit'][value*='Enrol'], button[id*='enrol']")` | `elementToBeClickable(...)` | Кнопка самореєстрації доступна |
| 3 | Натиснути кнопку зарахування | Клік по знайденому елементу кнопки | `elementToBeClickable(...)` | Відправлено форму зарахування |
| 4 | Дочекатися переходу до матеріалів | `wait.until(ExpectedConditions.urlContains("/course/view.php?id=12"))` | URL курсу відкрито | Студент зарахований, матеріали курсу доступні |

---

### Сценарій UI-05: Зарахування на курс закритим кодовим словом
- **Пов'язаний тест-кейс Спринту 3:** TC-012
- **Пріоритет автоматизації:** P2 (Захист закритого освітнього контенту)
- **Обґрунтування автоматизації:** Перевірка бізнес-логіки валідації реєстраційного ключа у веб-інтерфейсі.
- **Передумови (Preconditions):** Студент авторизований, курс `COURSE_SEC_15` (ID 15) вимагає ключ `'EduPass2026!'`.
- **Покроковий сценарій дій:**

| Крок | Дія користувача | Локатор елемента (By) | Очікування WebDriverWait | Очікуваний результат / Перевірка (Assertion) |
|:---:|:---|:---|:---|:---|
| 1 | Відкрити сторінку курсу з ключем | `driver.get(BASE_URL + "/enrol/index.php?id=15")` | `visibilityOfElementLocated(By.id("id_enrolpassword"))` | Відображається поле вводу пароля курсу |
| 2 | Ввести діюче кодове слово | `By.id("id_enrolpassword")` | — | Введено `'EduPass2026!'` |
| 3 | Натиснути кнопку приєднання | `By.cssSelector("input[type='submit'][id*='submitbutton']")` | `elementToBeClickable(...)` | Форму відправлено |
| 4 | Перевірка успішного входу | `wait.until(ExpectedConditions.urlContains("/course/view.php?id=15"))` | Доступ до контенту курсу | Студент успішно записаний |

---

### Сценарій UI-06: Завантаження допустимої кількості файлів відповіді у завдання
- **Пов'язаний тест-кейс Спринту 3:** TC-007
- **Пріоритет автоматизації:** P1 (Основна навчальна діяльність студента)
- **Обґрунтування автоматизації:** Перевірка коректної взаємодії файлового пікера та збереження чернетки робіт.
- **Передумови (Preconditions):** Студент зарахований на курс, відкрито завдання `ASSIGN_LAB_1` (ID 42).
- **Покроковий сценарій дій:**

| Крок | Дія користувача | Локатор елемента (By) | Очікування WebDriverWait | Очікуваний результат / Перевірка (Assertion) |
|:---:|:---|:---|:---|:---|
| 1 | Відкрити сторінку завдання | `driver.get(BASE_URL + "/mod/assign/view.php?id=42")` | `visibilityOfElementLocated(By.cssSelector(".submissionaction"))` | Сторінка опису завдання відкрита |
| 2 | Натиснути «Додати відповідь на завдання» | `By.cssSelector("button[type='submit']:contains('submission'), a.btn-primary")` | `elementToBeClickable(...)` | Відкрито форму подання роботи |
| 3 | Завантажити файл чернетки через input | `By.cssSelector("input[type='file'].fp-file-input, input[name*='userfile']")` | `presenceOfElementLocated(...)` | Файл передано у форму через `sendKeys(filePath)` |
| 4 | Натиснути «Зберегти зміни» | `By.id("id_submitbutton")` | `elementToBeClickable(...)` | Чернетку збережено |
| 5 | Перевірка статусу відповіді | `By.cssSelector(".submissionstatustable .statusearlysubmission, .generaltable")` | `visibilityOfElementLocated(...)` | Статус містить «Чернетка (не надіслано)» або відображає доданий файл |

---

### Сценарій UI-07: Фінальне надсилання відповіді на завдання на оцінювання
- **Пов'язаний тест-кейс Спринту 3:** TC-013
- **Пріоритет автоматизації:** P1 (Завершення бізнес-процесу подання роботи)
- **Обґрунтування автоматизації:** Фіксація статусу та часової мітки здачі для подальшого розрахунку штрафів (BL-30).
- **Передумови (Preconditions):** У завданні збережено попередню чернетку відповіді.
- **Покроковий сценарій дій:**

| Крок | Дія користувача | Локатор елемента (By) | Очікування WebDriverWait | Очікуваний результат / Перевірка (Assertion) |
|:---:|:---|:---|:---|:---|
| 1 | Відкрити сторінку завдання | `driver.get(BASE_URL + "/mod/assign/view.php?id=42")` | `visibilityOfElementLocated(By.cssSelector("form.submitpanel button, a.btn-secondary"))` | Сторінка завантажена |
| 2 | Натиснути «Здати на оцінювання» | `By.cssSelector("button[type='submit'][value*='submit'], a[href*='action=submit']")` | `elementToBeClickable(...)` | Відкрито модальне діалогове вікно підтвердження |
| 3 | Підтвердити фінальну відправку | `By.cssSelector(".modal-footer button.btn-primary, input[id='id_submitbutton']")` | `elementToBeClickable(...)` | Запит підтверджено |
| 4 | Перевірка фінального статусу | `By.cssSelector(".submissionstatus, .statusconfirm")` | `visibilityOfElementLocated(...)` | Статус змінився на «Здано на оцінювання» (Submitted for grading) |

---

### Сценарій UI-08: Перегляд студентом електронного журналу оцінок (User Grade Report)
- **Пов'язаний тест-кейс Спринту 3:** TC-022
- **Пріоритет автоматизації:** P2 (Звітність та прозорість оцінювання)
- **Обґрунтування автоматизації:** Регресійний контроль доступності підсумкових балів, розрахованих бізнес-модулем (BL-10/BL-20).
- **Передумови (Preconditions):** Студент авторизований та має оцінені завдання на курсі.
- **Покроковий сценарій дій:**

| Крок | Дія користувача | Локатор елемента (By) | Очікування WebDriverWait | Очікуваний результат / Перевірка (Assertion) |
|:---:|:---|:---|:---|:---|
| 1 | Перейти до журналу оцінок курсу | `driver.get(BASE_URL + "/grade/report/user/index.php?id=12")` | `visibilityOfElementLocated(By.cssSelector("table.user-grade"))` | Завантажено сторінку табеля успішності |
| 2 | Перевірка таблиці оцінок | `By.cssSelector("table.user-grade tbody tr")` | `presenceOfElementLocated(...)` | Рядки завдань присутні в таблиці |
| 3 | Перевірка підсумкового рядка | `By.cssSelector("tr.coursegrade, tr.total")` | `visibilityOfElementLocated(...)` | Відображається підсумковий бал курсу |
| 4 | Валідація відображення | `assertAll` для перевірки наявності балів та відсотків | — | Таблиця заповнена коректними числовими даними |

---

### Сценарій UI-09: Перегляд академічного календаря та подій курсу
- **Пов'язаний тест-кейс Спринту 3:** TC-026
- **Пріоритет автоматизації:** P3 (Інформаційні сервіси розкладу)
- **Обґрунтування автоматизації:** Перевірка наявності дедлайнів завдань у загальносистемному календарі.
- **Передумови (Preconditions):** Студент авторизований.
- **Покроковий сценарій дій:**

| Крок | Дія користувача | Локатор елемента (By) | Очікування WebDriverWait | Очікуваний результат / Перевірка (Assertion) |
|:---:|:---|:---|:---|:---|
| 1 | Відкрити сторінку календаря | `driver.get(BASE_URL + "/calendar/view.php?view=month")` | `visibilityOfElementLocated(By.cssSelector("table.calendarmonth"))` | Сітка календаря місяця видима |
| 2 | Перевірка навігаційного заголовка | `By.cssSelector(".calendar-controls .current")` | `visibilityOfElementLocated(...)` | Відображається поточний місяць (наприклад, Жовтень 2026) |
| 3 | Перевірка наявності блоку майбутніх подій | `By.cssSelector(".block_calendar_upcoming, .event")` | `presenceOfElementLocated(...)` | Події дедлайнів відображаються у списку |

---

## 3. Рекомендації для SDET (Кираківського О.) щодо збірки тестів

1. Використовувати клас-помічник `BaseUiTest` для спільної ініціалізації:
```java
public abstract class BaseUiTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static final String BASE_URL = "https://sandbox.moodledemo.net";

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--window-size=1280,900", "--disable-gpu");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(12));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
```
2. Усі UI класи помістити у пакет `ua.kpi.tzyapz.eduportal.ui` та позначити `@Tag("ui")`.
