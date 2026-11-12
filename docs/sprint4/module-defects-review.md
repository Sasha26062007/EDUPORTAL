# Верифікація та затвердження звітів про дефекти модуля (Module Defect Verification & Sign-off)

**Проєкт:** Автоматизована система дистанційного навчання «ЕдуПортал» (LMS)  
**Команда:** EduGuard (Команда F, 5 осіб, група ФБ-44)  
**Цільовий пакет:** `ua.kpi.tzyapz.eduportal` (версія коду v1)  
**Спринт 4:** «Автоматизація тестування: JUnit 5 та Selenium WebDriver»  
**Скрам-майстер / Рецензент (Reviewer):** Денис Бучинський (`Denchik`)  
**Керівник тестування (Reporter / Lead):** Володимир Костенко (`vov4ik-qa`)  
**Дата верифікації:** 10 листопада 2026 року  
**Статус верифікації:** **УСІ 6 ДЕФЕКТІВ ПІДТВЕРДЖЕНО ТА ЗАТВЕРДЖЕНО (VERIFIED & LOCKED)**  

---

## 1. Регламент обробки закладених дефектів модуля (§4.9 та §12 методичних вказівок)

Відповідно до пункту 5 алгоритму Спринту 4 та типової помилки №3:
> «Тест, що впав, — не помилка тесту, поки ви не звірили очікуване значення з BL-вимогою. Якщо тест правий — це закладений дефект модуля: звіт про дефект за шаблоном Спринту 3 у трекері команди (GitHub Issues), з ID вимоги, ім'ям тест-класу і методу, фактичним і очікуваним значенням. Тест лишається у наборі і лишається «червоним» — у Спринті 5 він перевірить виправлення».

Скрам-майстер Денис Бучинський провів особисту верифікацію кожного падіння тесту, звірив очікувані формули з текстом вимог SRS (розділ 5 BL), підтвердив, що тести не підганялися під помилкову поведінку коду `v1`, та затвердив присвоєння обов'язкових міток `module`, `sprint:4`.

---

## 2. Зведена таблиця верифікованих дефектів модуля «ЕдуПортал» (v1)

| ID дефекту | BL-вимога | Клас та метод модуля v1 | Вхідні дані (BVA) | Очікувано за SRS | Фактично у v1 | Severity | Priority | Пов'язаний автотест JUnit 5 | Резолюція Скрам-майстра |
|:---:|:---:|:---|:---|:---|:---|:---:|:---:|:---|:---:|
| **MOD-BUG-001** | **BL-20** | `GradeCalculator.ectsLetter(double total)` | `total = 95.0` (межа оцінки "A") | `"A"` (діапазон 95..100) | `"B"` | **Major** | **P2** | `GradeCalculatorTest.ectsLetterGradeABoundary()` | **VERIFIED (Approved)** |
| **MOD-BUG-002** | **BL-30** | `GradeCalculator.latePenalty(double score, int daysLate)` | `score = 100.0, daysLate = 3` | `70.0` б. ($100 \times (1 - 0.3)$) | `72.9` б. ($100 \times 0.9^3$) | **Major** | **P2** | `GradeCalculatorTest.latePenaltyLinearCalculation()` | **VERIFIED (Approved)** |
| **MOD-BUG-003** | **BL-50** | `QuizScorer.questionScore(QuizQuestion q, Set<String> a)` | `q: opt{A,B,C,D}, cor{A,B}; a: {C}` | `0.0` б. («але не менше 0») | `-0.5` б. | **Major** | **P2** | `QuizScorerTest.questionScoreMultipleChoiceMustNotBeNegative()` | **VERIFIED (Approved)** |
| **MOD-BUG-004** | **BL-60** | `QuizScorer.score(Map a, List<QuizQuestion> key)` | 1 правильне з 3 питань ($1/3 \times 100$) | `33.3%` (округлення до 1 знака HALF_UP) | `33.0%` (ціле число) | **Normal** | **P3** | `QuizScorerTest.scoreAttemptRoundsToOneDecimalPlace()` | **VERIFIED (Approved)** |
| **MOD-BUG-005** | **BL-70** | `EnrollmentValidator.canEnroll(Course c, Student s, LocalDate d, String k)` | `today = 2026-10-01`, `enrolEnd = 2026-09-30` | `false` (період закрито) | `true` (дозволено) | **Critical** | **P1** | `EnrollmentValidatorTest.withinPeriodRespectsEndDate()` | **VERIFIED (Approved)** |
| **MOD-BUG-006** | **BL-70** | `EnrollmentValidator.canEnroll(Course c, Student s, LocalDate d, String k)` | `capacity = 25`, `enrolled = 25` | `false` (вільних місць немає) | `true` (дозволено) | **Major** | **P2** | `EnrollmentValidatorTest.hasCapacityStrictlyLessThan()` | **VERIFIED (Approved)** |

---

## 3. Детальні картки звітів про дефекти модуля

### Звіт #1: MOD-BUG-001 (Некоректне визначення оцінки ECTS на нижній межі діапазону "A")
- **Мітки (Labels):** `module`, `sprint:4`, `severity:major`, `priority:p2`, `component:grade-calc`
- **ID вимоги SRS:** **BL-20** («Шкала оцінювання ECTS: A: 95..100, B: 85..94, C: 75..84, D: 65..74, E: 60..64, FX: 35..59, F: 0..34»).
- **Цільовий клас та метод:** `ua.kpi.tzyapz.eduportal.GradeCalculator.ectsLetter(double total)`
- **Вхідні тестові дані:** `total = 95.0`
- **Очікуваний результат:** Повертається літера `"A"`, оскільки межа 95.0 включно належить інтервалу [95, 100].
- **Фактичний результат у v1:** Метод повертає `"B"`.
- **Локалізація помилки в коді:**  
  У `GradeCalculator.java` (рядок 45) реалізовано строгу нерівність:
  ```java
  if (total > 95) return "A"; // ПОМИЛКА: має бути total >= 95
  ```
- **Падаючий тест:** `GradeCalculatorTest.ectsLetterGradeABoundary()`
- **Рішення Скрам-майстра:** Звіт затверджено. Тест зафіксовано як «червоний». Заборонено підганяти очікуване значення під `"B"`.

---

### Звіт #2: MOD-BUG-002 (Нелінійне нарахування штрафу за прострочену здачу завдання)
- **Мітки (Labels):** `module`, `sprint:4`, `severity:major`, `priority:p2`, `component:grade-calc`
- **ID вимоги SRS:** **BL-30** («Штраф за прострочення: 10% від набраного бала за кожен день запізнення, але не більше 50% загалом: $\text{результат} = \text{бал} \times (1 - \text{дні} \times 0.1)$»).
- **Цільовий клас та метод:** `ua.kpi.tzyapz.eduportal.GradeCalculator.latePenalty(double score, int daysLate)`
- **Вхідні тестові дані:** `score = 100.0`, `daysLate = 3`
- **Очікуваний результат:** Лінійне списання 30% ($3 \times 10\%$): $100.0 \times (1 - 0.30) = 70.0$ балів.
- **Фактичний результат у v1:** Метод повертає `72.9` балів.
- **Локалізація помилки в коді:**  
  У `GradeCalculator.java` (рядок 58) застосовано експоненційне складне зменшення замість лінійного:
  ```java
  return score * Math.pow(0.9, daysLate); // ПОМИЛКА: обчислює 100 * (0.9)^3 = 72.9
  ```
- **Падаючий тест:** `GradeCalculatorTest.latePenaltyLinearCalculation()`
- **Рішення Скрам-майстра:** Звіт затверджено. Формулу лінійного штрафу підтверджено.

---

### Звіт #3: MOD-BUG-003 (Від'ємний бал за питання тесту з кількома правильними відповідями)
- **Мітки (Labels):** `module`, `sprint:4`, `severity:major`, `priority:p2`, `component:quiz-scorer`
- **ID вимоги SRS:** **BL-50** («Питання з кількома правильними відповідями: частка обраних правильних мінус частка обраних неправильних, але не менше 0»).
- **Цільовий клас та метод:** `ua.kpi.tzyapz.eduportal.QuizScorer.questionScore(QuizQuestion question, Set<String> studentAnswers)`
- **Вхідні тестові дані:** Питання `q2` з варіантами `{A, B, C, D}`, правильні `{A, B}`. Студент відповів `{C}` (0 правильних, 1 неправильний з 2 можливих).
- **Очікуваний результат:** $\text{correctShare} = 0/2 = 0.0$, $\text{wrongShare} = 1/2 = 0.5$. Різниця $-0.5$, але за правилом «не менше 0» очікується рівно `0.0` балів.
- **Фактичний результат у v1:** Метод повертає `-0.5` балів.
- **Локалізація помилки в коді:**  
  У `QuizScorer.java` (рядок 42) відсутнє обмеження нижнього порогу:
  ```java
  return correctShare - wrongShare; // ПОМИЛКА: має бути Math.max(0.0, correctShare - wrongShare)
  ```
- **Падаючий тест:** `QuizScorerTest.questionScoreMultipleChoiceMustNotBeNegative()`
- **Рішення Скрам-майстра:** Звіт затверджено. Авторський тест Дениса Бучинського правильно ізолює дефект.

---

### Звіт #4: MOD-BUG-004 (Втрата точності та неправильне округлення підсумку спроби тесту)
- **Мітки (Labels):** `module`, `sprint:4`, `severity:normal`, `priority:p3`, `component:quiz-scorer`
- **ID вимоги SRS:** **BL-60** («Підсумок спроби тесту: сума балів / кількість питань $\times 100$, округлення до 1 десяткового знака за правилом HALF_UP»).
- **Цільовий клас та метод:** `ua.kpi.tzyapz.eduportal.QuizScorer.score(Map<String, Set<String>> answers, List<QuizQuestion> key)`
- **Вхідні тестові дані:** Тест із 3 питань. Студент дав 1 правильну відповідь ($1/3 \times 100 = 33.3333...\%$).
- **Очікуваний результат:** `33.3`% (округлення до одного десяткового знака).
- **Фактичний результат у v1:** Метод повертає `33.0`%.
- **Локалізація помилки в коді:**  
  У `QuizScorer.java` (рядок 63) використано округлення до найближчого цілого:
  ```java
  return (double) Math.round(percent); // ПОМИЛКА: округлює 33.3333 до 33.0 замість BigDecimal.setScale(1, HALF_UP)
  ```
- **Падаючий тест:** `QuizScorerTest.scoreAttemptRoundsToOneDecimalPlace()`
- **Рішення Скрам-майстра:** Звіт затверджено.

---

### Звіт #5: MOD-BUG-005 (Дозвіл на самозарахування після офіційного завершення періоду)
- **Мітки (Labels):** `module`, `sprint:4`, `severity:critical`, `priority:p1`, `component:enrollment`
- **ID вимоги SRS:** **BL-70** («Умови самозарахування на курс: реєстрація дозволена лише у межах періоду [enrolStart, enrolEnd] включно»).
- **Цільовий клас та метод:** `ua.kpi.tzyapz.eduportal.EnrollmentValidator.canEnroll(...)`
- **Вхідні тестові дані:** Період курсу: `2026-09-01` .. `2026-09-30`. Поточна дата запиту: `2026-10-01` (наступний день після завершення).
- **Очікуваний результат:** `false` (зарахування відхилено, оскільки період минув).
- **Фактичний результат у v1:** `true` (зарахування успішно дозволено).
- **Локалізація помилки в коді:**  
  У `EnrollmentValidator.java` (рядок 51) перевіряється лише дата початку, а дата завершення не перевіряється взагалі:
  ```java
  boolean withinPeriod = !today.isBefore(course.enrolStart()); // ПОМИЛКА: відсутня перевірка !today.isAfter(course.enrolEnd())
  ```
- **Падаючий тест:** `EnrollmentValidatorTest.withinPeriodRespectsEndDate()`
- **Рішення Скрам-майстра:** Звіт затверджено з найвищим пріоритетом P1/Critical.

---

### Звіт #6: MOD-BUG-006 (Дозвіл на реєстрацію за повної відсутності вільних місць у групі)
- **Мітки (Labels):** `module`, `sprint:4`, `severity:major`, `priority:p2`, `component:enrollment`
- **ID вимоги SRS:** **BL-70** («Умови самозарахування: на курсі є вільні місця: $\text{enrolled} < \text{capacity}$»).
- **Цільовий клас та метод:** `ua.kpi.tzyapz.eduportal.EnrollmentValidator.canEnroll(...)`
- **Вхідні тестові дані:** Місткість курсу `capacity = 25`, поточна кількість зарахованих `enrolled = 25` (місць немає).
- **Очікуваний результат:** `false` (курс заповнений на 100%, реєстрація заборонена).
- **Фактичний результат у v1:** `true` (дозволено реєстрацію 26-го студента).
- **Локалізація помилки в коді:**  
  У `EnrollmentValidator.java` (рядок 58) використано нестрогу нерівність:
  ```java
  boolean hasCapacity = course.enrolled() <= course.capacity(); // ПОМИЛКА: дозволяє enrolled == capacity
  ```
- **Падаючий тест:** `EnrollmentValidatorTest.hasCapacityStrictlyLessThan()`
- **Рішення Скрам-майстра:** Звіт затверджено.

---

## 4. Висновок щодо готовності до Спринту 5

Усі 6 закладених дефектів модуля `v1` (1 Critical, 4 Major, 1 Normal) повністю відповідають специфікації SRS та підтверджені незалежними автотестами JUnit 5. Жоден дефект не маскувався анотацією `@Disabled`, жодне очікуване значення не підганялося. 

У Спринті 5 ці ж самі 6 тестів слугуватимуть автоматизованим критерієм верифікації виправлень (TDD/Regression testing): після корекції коду в модулі `v2` всі 24 юніт-тести мають стати зеленими (`Build Success`).
