# Підсумковий звіт Керівника тестування: Спринт 4 «Автоматизація: JUnit 5 і Selenium WebDriver»

**Дисципліна:** Технології забезпечення якості програмних засобів (ТЗЯПЗ)  
**Проєкт:** Автоматизована система дистанційного навчання «ЕдуПортал» (LMS)  
**Команда:** Команда F («EduGuard»), 5 осіб, група ФБ-44  
**Виконавець ролі:** Керівник тестування (Test Lead) — **Володимир Костенко** (`vov4ik-qa`, `Vov4ik`)  
**Рецензент:** Скрам-майстер / Рецензент — **Денис Бучинський** (`Denchik`)  
**Аудитор-опонент:** **Олександр Панайотов** (`5ani0chek`)  
**Дата актуалізації:** 13 листопада 2026 року  

---

## 1. Паспорт ролі та виконання нормативів Спринту 4

У Спринті 4 наша команда створила комплексний автоматизований регресійний комплекс для платформи дистанційного навчання «ЕдуПортал»:
- Модульні тести на базі **JUnit 5.10.2** для бізнес-логіки модуля `ua.kpi.tzyapz.eduportal` (еталонна версія v1).
- Наскрізні автоматизовані UI-сценарії на базі **Selenium WebDriver 4.21.0** з використанням **Page Object Model (POM)** на тестовому стенді Moodle LMS (https://sandbox.moodledemo.net).
- Документування 6 закладених дефектів модуля у трекері **GitHub Issues** з міткою `module`.

### Зведена статистика виконання нормативів (команда з 5 осіб):

| Показник нормативу | Норматив методички (§6, 5 осіб) | Фактичний результат у репозиторії | Статус виконання |
|:---|:---:|:---:|:---:|
| **Юніт-тестів JUnit 5** | $\ge 20$ тестів | **37 методів (64 прогони)** | **Перевиконано (+85%)** |
| **UI-сценаріїв WebDriver** | $\ge 8$ сценаріїв | **11 сценаріїв (4 класи POM)** | **Перевиконано (+37%)** |
| **Знайдених дефектів модуля v1** | $\ge 5$ із 6 закладених | **6 дефектів (MOD-BUG-001..006)** | **Виконано на 100% (6/6)** |
| **Реєстрація дефектів у трекері** | GitHub Issues з міткою `module` | **Створено Issues #11–#16** | **Виконано на 100%** |
| **Юніт-тестів на кожного учасника** | $\ge 3$ на особу | **$\ge 5$ на кожного члена команди** | **Виконано** |
| **Сценаріїв у Selenium IDE** | $1..2$ сценарії | **3 сценарії** (`eduportal.side` + `export/`) | **Виконано** |
| **Чек-лист Code Review (§5.3)** | 8 обов'язкових пунктів | **Пройдено за всіма 8 пунктами** | **Виконано** |
| **Матеріали Демо та бліцу (§11)** | 10 хв демо + 12 питань | **Сценарій Демо + 12 відповідей готові** | **Виконано** |

---

## 2. Структура створених файлів та артефактів Спринту 4

```text
c:\TZIPZ2\
├── pom.xml                                    <- Конфігурація збірки Maven (JDK 17, JUnit 5, Selenium, Surefire)
├── .gitignore                                 <- Правила ігнорування (target/, .idea/, *.iml, .DS_Store)
├── README.md                                  <- Головна навігація, інструкція запуску та ролі 5 спринтів
├── selenium-ide/
│   ├── eduportal.side                         <- Проєкт запису дій у Selenium IDE
│   └── export/                                <- Експортовані чернетки тестів Java
│       ├── MoodleCourseEnrollScenarioTest.java
│       └── MoodleLoginScenarioTest.java
├── src/
│   ├── main/java/ua/kpi/tzyapz/eduportal/     <- Незмінений еталонний модуль бізнес-логіки v1
│   │   ├── Course.java
│   │   ├── EnrollmentValidator.java
│   │   ├── GradeCalculator.java
│   │   ├── GradeItem.java
│   │   ├── QuizQuestion.java
│   │   ├── QuizScorer.java
│   │   └── Student.java
│   └── test/java/ua/kpi/tzyapz/eduportal/
│       ├── EnrollmentValidatorTest.java       <- 17 тест-методів (BL-70, BL-80)
│       ├── GradeCalculatorTest.java           <- 12 тест-методів (BL-10, BL-20, BL-30)
│       ├── QuizScorerTest.java                <- 8 тест-методів (BL-40, BL-50, BL-60)
│       └── ui/                                <- Наскрізні сценарії Selenium WebDriver
│           ├── BaseUiTest.java                <- Базовий клас запуску Chrome WebDriver
│           ├── MoodleAssignmentUiTest.java    <- 3 сценарії (TC-12, TC-15, TC-16)
│           ├── MoodleCourseEnrollmentUiTest.java <- 3 сценарії (TC-07, TC-08, TC-09)
│           ├── MoodleGradebookUiTest.java     <- 2 сценарії (TC-25, TC-26)
│           ├── MoodleLoginUiTest.java         <- 3 сценарії (TC-01, TC-02, TC-03)
│           └── pages/                         <- Реалізація патерну Page Object Model
│               ├── AssignmentPage.java
│               ├── BasePage.java
│               ├── CourseEnrollmentPage.java
│               ├── CoursePage.java
│               ├── DashboardPage.java
│               ├── GradebookPage.java
│               └── LoginPage.java
└── docs/sprint4/
    ├── README.md                              <- Цей підсумковий звіт Керівника тестування
    ├── EDUPORTAL_Sprint4_Presentation.pptx    <- Презентація команди до захисту
    ├── audit.md                               <- Акт Аудитора-опонента (О. Панайотов)
    ├── coverage-matrix.md                     <- Наскрізна матриця покриття BL-10..BL-80 (37 методів)
    ├── coverage-matrix-review.md              <- Протокол рецензування матриці
    ├── ui-scenarios.md                        <- Специфікація 11 сценаріїв UI з локаторами
    ├── surefire-summary.md                    <- Протокол фактичного запуску Surefire
    ├── module-defects.md                      <- Реєстр 6 дефектів модуля v1 (MOD-BUG-001..006)
    ├── module-defects-review.md               <- Акт верифікації дефектів Скрам-майстром
    ├── task-board.md                          <- Таблиця розподілу та виконання 28 задач
    ├── code-review-protocol.md                <- Протокол Code Review за чек-листом §5.3
    ├── dod-verification-sdet.md               <- Верифікація критеріїв Definition of Done
    ├── demo-scenario.md                       <- Похвилинний сценарій 10-хвилинного Демо
    ├── blitz-prep-sprint4.md                  <- Відповіді на 12 питань бліц-опитування (§11)
    ├── retro.md                               <- Протокол ретроспективи Спринту 4
    ├── sdet-report.md                         <- Звіт інженера з тестування / SDET
    └── defects/                               <- Картки дефектів (BUG-MOD-01..BUG-MOD-06)
```

---

## 3. Інструкція зі збірки та виконання автотестів (README для викладача)

### 3.1. Вимоги до середовища (Environment Prerequisites)
- **ОС:** Windows 10/11, Linux або macOS.
- **Java Development Kit:** JDK 17 або новіша (рекомендовано Eclipse Temurin OpenJDK 17). Змінна `JAVA_HOME` повинна вказувати на каталог JDK.
- **Apache Maven:** версія 3.9.0 або вище.
- **Google Chrome:** версія 125+ (для UI-тестів). Драйвер завантажується автоматично через Selenium Manager.

### 3.2. Команди запуску
1. **Швидкий запуск юніт-тестів бізнес-логіки (DoD п.2):**
   ```bash
   mvn test
   ```
   *Результат:* 64 прогони, 6 очікуваних падінь на закладених дефектах модуля v1 (`MOD-BUG-001`–`MOD-BUG-006`).

2. **Повний регресійний прогін включно з UI-тестами Selenium:**
   ```bash
   mvn test -DexcludedGroups=none
   ```
   *Результат:* 75 запусків (64 модульні + 11 UI-тестів у реальному браузері, всі 11 UI-тестів мають статус **PASSED**).

3. **Запуск окремого тестового класу:**
   ```bash
   mvn test -Dtest=GradeCalculatorTest
   mvn test -Dtest=MoodleLoginUiTest -DexcludedGroups=none
   ```
