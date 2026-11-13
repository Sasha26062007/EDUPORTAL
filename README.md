# Навчальна платформа «ЕдуПортал» (LMS)

**Дисципліна:** Технології забезпечення якості програмних засобів (ТЗЯПЗ)  
**Продукт:** «ЕдуПортал» (система дистанційного навчання, LMS, Продукт №6)  
**Команда:** EduGuard (Команда F, група ФБ-44)  

---

### Склад команди та розподіл ролей за спринтами (5 осіб)

| Учасник команди | Псевдонім | Роль у Спринті 1 | Роль у Спринті 2 | Роль у Спринті 3 | Роль у Спринті 4 |
|:---|:---|:---|:---|:---|:---|
| **Олександр Панайотов** | `5ani0chek` (Sasha26062007) | Інженер з тестування (ІТ) | **Керівник тестування (Test Lead)** | **Скрам-майстер / Рецензент (Scrum Master)** | **Аудитор-опонент (Quality Auditor / Opponent)** |
| **Володимир Костенко** | `Vov4ik` (`vov4ik-qa`) | Тест-аналітик (ТА) | **Інженер з тестування (Test Engineer)** | **Тест-аналітик (Test Analyst)** | **Керівник тестування (Test Lead)** |
| **Денис Бучинський** | `Denchik` | Керівник тестування + Скрам-майстер (КТ+СМ) | **Тест-аналітик (Test Analyst)** | **Керівник тестування (Test Lead)** | **Скрам-майстер / Рецензент (Scrum Master / Reviewer)** |
| **Олександр Пархоменко** | `Parkhomenko0` | *(приєднався зі Спринту 2)* | **Аудитор-опонент (Quality Auditor / Opponent)** | **Інженер з тестування (Test Engineer)** | **Тест-аналітик (Test Analyst)** |
| **Олександр Кираківський** | `Kyrakivskyi0` | *(приєднався зі Спринту 2)* | **Скрам-майстер / Рецензент (Scrum Master / Reviewer)** | **Аудитор-опонент (Quality Auditor)** | **Інженер з тестування / SDET (SDET / Test Engineer)** |

---

## ⚡ Швидкий старт та інструкції викладачу (§8, DoD п.1, п.2)

### 1. Вимоги до тестового середовища
- **ОС:** Windows 10/11, Linux або macOS.
- **Java:** JDK 17 або вище (рекомендовано Eclipse Temurin OpenJDK 17). Змінна `JAVA_HOME` має вказувати на JDK 17.
- **Maven:** Apache Maven 3.9.0 або новіший.
- **Браузер:** Google Chrome 125+ (для UI-сценаріїв Selenium WebDriver, драйвер управляється автоматично).

### 2. Команди запуску автотестів
- **Швидкий запуск юніт-тестів бізнес-логіки (DoD п.2):**
  ```bash
  mvn test
  ```
  *(Властивість `<excludedGroups>ui</excludedGroups>` автоматично виключає браузерні тести: запускаються 64 модульні тести, з яких рівно 6 падають через закладені дефекти модуля v1: MOD-BUG-001..006).*

- **Повний запуск комплексу разом із UI-тестами WebDriver:**
  ```bash
  mvn test -DexcludedGroups=none
  ```
  *(Виконує 75 тестів: 64 модульні + 11 сценаріїв Selenium WebDriver у реальному браузері. Усі 11 UI-тестів мають статус **PASSED (100% Green)**).*

- **Запуск конкретного тестового класу:**
  ```bash
  mvn test -Dtest=GradeCalculatorTest
  mvn test -Dtest=MoodleLoginUiTest -DexcludedGroups=none
  ```

---

## 📑 Загальнокомандна документація
- **[Статут команди (docs/team/charter.md)](docs/team/charter.md)** — регламенти Scrum, правило «двох аудиторів», Definition of Done та матриця ротації.

---

## 🚀 Спринт 4: «Автоматизація тестування: JUnit 5 та Selenium WebDriver» (Поточний реліз)

**Інкремент якості v4:** Повний автоматизований тестовий комплекс для перевірки бізнес-логіки та веб-інтерфейсу навчальної платформи «ЕдуПортал».

### 📊 Презентація до захисту Спринту 4:
- **[Презентація PowerPoint (docs/sprint4/EDUPORTAL_Sprint4_Presentation.pptx)](docs/sprint4/EDUPORTAL_Sprint4_Presentation.pptx)** — повний 11-слайдовий звіт команди до захисту з архітектурою POM, пірамідою тестів, аналізом закладених дефектів та нотатками доповідачів.

### 📌 Основні артефакти Спринту 4:
1. **[Підсумковий звіт Керівника тестування (docs/sprint4/README.md)](docs/sprint4/README.md)** — паспорт ролі, зведена статистика виконання нормативів, структура проєкту та інструкції запуску.
2. **[Акт аудиту якості автотестів (docs/sprint4/audit.md)](docs/sprint4/audit.md)** — незалежний сліпий аудит закладених дефектів, відтворюваності та дотримання правила *No Assert Tampering* (автор: Олександр Панайотов).
3. **[Матриця покриття вимог BL-10..BL-80 (docs/sprint4/coverage-matrix.md)](docs/sprint4/coverage-matrix.md)** та **[Рецензія матриці (docs/sprint4/coverage-matrix-review.md)](docs/sprint4/coverage-matrix-review.md)** — 37 тест-методів (64 прогони).
4. **[Специфікація автоматизованих сценаріїв UI (docs/sprint4/ui-scenarios.md)](docs/sprint4/ui-scenarios.md)** — опис 11 сценаріїв Selenium WebDriver з локаторами, діями та перевірками до кейсів Спринту 3.
5. **[Протокол фактичного запуску Surefire (docs/sprint4/surefire-summary.md)](docs/sprint4/surefire-summary.md)** — точний вивід Maven Surefire для модульних та UI-прогонів.
6. **[Реєстр дефектів модуля (docs/sprint4/module-defects.md)](docs/sprint4/module-defects.md)** — закладені дефекти `MOD-BUG-001`–`MOD-BUG-006` у GitHub Issues [#11–#16](https://github.com/Sasha26062007/EDUPORTAL/issues) з міткою `module`.
7. **[Звіт SDET про реалізацію автотестів (docs/sprint4/sdet-report.md)](docs/sprint4/sdet-report.md)** та **[Чек-лист DoD верифікації (docs/sprint4/dod-verification-sdet.md)](docs/sprint4/dod-verification-sdet.md)**.
8. **[Протокол Code Review тестів (docs/sprint4/code-review-protocol.md)](docs/sprint4/code-review-protocol.md)** — проходження 8 обов'язкових критеріїв якості коду.
9. **[Дошка задач Спринту 4 (docs/sprint4/task-board.md)](docs/sprint4/task-board.md)** — розподіл та статус 28 інженерних задач.
10. **[Сценарій живого Демо (docs/sprint4/demo-scenario.md)](docs/sprint4/demo-scenario.md)** та **[Підготовка до бліц-опитування (docs/sprint4/blitz-prep-sprint4.md)](docs/sprint4/blitz-prep-sprint4.md)**.
11. **[Протокол ретроспективи Спринту 4 (docs/sprint4/retro.md)](docs/sprint4/retro.md)** — аналіз Start / Stop / Continue.

### 💻 Автоматизований тестовий код:
- **Модульні тести (JUnit 5):** [`src/test/java/ua/kpi/tzyapz/eduportal/`](src/test/java/ua/kpi/tzyapz/eduportal/) — `EnrollmentValidatorTest` (17 методів), `GradeCalculatorTest` (12 методів), `QuizScorerTest` (8 методів). 64 запуски, 6 обґрунтованих падінь на дефектах v1 за правилом *No Assert Tampering*.
- **UI тести (Selenium WebDriver 4 + Page Object Model):** [`src/test/java/ua/kpi/tzyapz/eduportal/ui/`](src/test/java/ua/kpi/tzyapz/eduportal/ui/) — `MoodleLoginUiTest` (3), `MoodleCourseEnrollmentUiTest` (3), `MoodleAssignmentUiTest` (3), `MoodleGradebookUiTest` (2). Усі 11 сценаріїв 100% Green.
- **Selenium IDE:** [`selenium-ide/eduportal.side`](selenium-ide/eduportal.side) та експортовані чернетки у [`selenium-ide/export/`](selenium-ide/export/).

---

## 📦 Спринт 3: «Тест-кейси та дефекти» (Архів інкременту)

**Досьє якості v3:** 30 тест-кейсів, 10 звітів про дефекти (GitHub Issues `#1`–`#10`), повний журнал прогону, сліпий аудит і підписаний DoD.

### 📌 Основні артефакти Спринту 3:
1. **[Досьє якості v3 (docs/sprint3/README.md)](docs/sprint3/README.md)** — зведені метрики, покриття SRS 1.1, обмеження та DoD.
2. **[Комплект тест-кейсів (docs/sprint3/test-cases.xlsx)](docs/sprint3/test-cases.xlsx)** — 30 кейсів, аркуші `Test cases`, `Coverage`, `Review`.
3. **[Журнал прогону (docs/sprint3/execution-log.xlsx)](docs/sprint3/execution-log.xlsx)** — `Run 1` (30 рядків) та `Environment`.
4. **[Звіти про дефекти (docs/sprint3/bug-reports.md)](docs/sprint3/bug-reports.md)** — `BUG-001..BUG-010` із вкладеннями в [attachments](docs/sprint3/attachments).
5. **[Акт сліпого аудиту (docs/sprint3/audit.md)](docs/sprint3/audit.md)** — відтворення дефектів, DoD.
6. **[Дошка задач Спринту 3 (docs/sprint3/task-board.md)](docs/sprint3/task-board.md)** — 25 задач, 5 ролей.
7. **[Протокол ретроспективи (docs/sprint3/retro-sprint3.md)](docs/sprint3/retro-sprint3.md)**.
8. **[Сценарій Демо (docs/sprint3/demo-script-sprint3.md)](docs/sprint3/demo-script-sprint3.md)**.
9. **[Презентація до захисту Спринту 3 (docs/sprint3/EDUPORTAL_Sprint3_Presentation.pptx)](docs/sprint3/EDUPORTAL_Sprint3_Presentation.pptx)**.

---

## 📦 Спринт 2: «Аудит вимог і стратегія тестування» (Архів інкременту)

### 📊 Презентація до захисту Спринту 2:
- **[Презентація PowerPoint (10 хв)](docs/sprint2/EDUPORTAL_Sprint2_Presentation.pptx)** — повний звіт за Спринтом 2 з нотатками доповідачів до кожного слайда.

### 📌 Основні інженерні артефакти Спринту 2:
1. **[Реєстр дефектів вимог (docs/sprint2/requirements-defects.xlsx)](docs/sprint2/requirements-defects.xlsx)** — 24 виявлені дефекти (по 4 на кожен із 6 критеріїв якості), розподіл розділів, аркуш «Спірні» та індивідуальні аркуші 5 аудиторів.
2. **[Виправлена специфікація вимог (docs/sprint2/SRS-1.1.md)](docs/sprint2/SRS-1.1.md)** — оновлена версія вимог із детальним Журналом змін (Change Log) на початку.
3. **[Вихідна специфікація вимог (docs/sprint2/SRS-1.0.md)](docs/sprint2/SRS-1.0.md)** — первинний документ «для аудиту».
4. **[План тестування продукту (docs/sprint2/test-plan.md)](docs/sprint2/test-plan.md)** — комплексна стратегія: 7 рівнів піраміди тестування, 16 застосовних типів тестування з Цілями та Описами, обґрунтування 2 незастосовних типів.
5. **[Таблиця задач спринту (docs/sprint2/tasks.md)](docs/sprint2/tasks.md)** — дошка виконання 21 задачі з перевірками Скрам-майстра.
6. **[Протокол підсумкової ретроспективи (docs/sprint2/retro-sprint2.md)](docs/sprint2/retro-sprint2.md)** — аналіз у форматі *Start / Stop / Continue*.
7. **[Повний звіт та навігація Спринту 2 (docs/sprint2/README.md)](docs/sprint2/README.md)**.

---

## 📦 Спринт 1: «Продукт і вимоги» (Архів інкременту)

- **[Презентація захисту Спринту 1](docs/sprint1/EDUPORTAL_Sprint1_Presentation.pptx)**
- **[Характеристика продукту (docs/sprint1/product.md)](docs/sprint1/product.md)**
- **[Беклог User Stories (docs/sprint1/backlog.md)](docs/sprint1/backlog.md)**
- **[Таблиця задач та тайм-трекінг (docs/sprint1/tasks.md)](docs/sprint1/tasks.md)**
- **[Протокол ретроспективи (docs/sprint1/retro.md)](docs/sprint1/retro.md)**
- **Моделі BPMN 2.0:** [01_testing.bpmn](docs/sprint1/bpmn/01_testing.bpmn) ([SVG](docs/sprint1/bpmn/01_testing.svg)) | [02_course_creation.bpmn](docs/sprint1/bpmn/02_course_creation.bpmn) ([SVG](docs/sprint1/bpmn/02_course_creation.svg))
