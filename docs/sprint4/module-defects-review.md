# Верифікація та затвердження звітів про дефекти модуля (Module Defect Verification & Sign-off)

**Проєкт:** Автоматизована система дистанційного навчання «ЕдуПортал» (LMS)  
**Команда:** EduGuard (Команда F, 5 осіб, група ФБ-44)  
**Цільовий пакет:** `ua.kpi.tzyapz.eduportal` (версія коду v1)  
**Спринт 4:** «Автоматизація тестування: JUnit 5 та Selenium WebDriver»  
**Скрам-майстер / Рецензент (Reviewer):** Денис Бучинський (`Denchik`)  
**Керівник тестування (Test Lead):** Володимир Костенко (`vov4ik-qa`)  
**Аудитор-опонент:** Олександр Панайотов (`5ani0chek`)  
**Дата верифікації:** 13 листопада 2026 року  
**Статус верифікації:** **УСІ 6 ДЕФЕКТІВ ПІДТВЕРДЖЕНО, СИНХРОНІЗОВАНО З GITHUB ISSUES (#11–#16) ТА ЗАТВЕРДЖЕНО**  

---

## 1. Регламент обробки закладених дефектів модуля (§4.9 та §12 методичних вказівок)

Відповідно до пункту 5 алгоритму Спринту 4 та типової помилки №3 методички:
> «Тест, що впав, — не помилка тесту, поки ви не звірили очікуване значення з BL-вимогою. Якщо тест правий — це закладений дефект модуля: звіт про дефект за шаблоном Спринту 3 у трекері команди (GitHub Issues), з ID вимоги, ім'ям тест-класу і методу, фактичним і очікуваним значенням. Тест лишається у наборі і лишається «червоним» — у Спринті 5 він перевірить виправлення».

Скрам-майстер Денис Бучинський та Аудитор-опонент Олександр Панайотов провели верифікацію кожного падіння тесту, звірили очікувані формули з текстом вимог SRS 1.1 (розділ 5 BL), підтвердили, що тести не підганялися під помилкову поведінку коду `v1`, та затвердили реєстрацію у трекері GitHub Issues з обов'язковою міткою `module`.

---

## 2. Зведена таблиця верифікованих дефектів модуля «ЕдуПортал» (v1)

| ID дефекту | GitHub Issue | BL-вимога | Клас та метод модуля v1 | Вхідні дані (BVA) | Очікувано за SRS 1.1 | Фактично у v1 | Severity / Priority | Пов'язаний автотест JUnit 5 | Резолюція рецензента |
|:---:|:---:|:---:|:---|:---|:---|:---|:---:|:---|:---:|
| **MOD-BUG-001** | [#11](https://github.com/Sasha26062007/EDUPORTAL/issues/11) | **BL-20** | `GradeCalculator.ectsLetter(double total)` | `total = 95.0` (межа оцінки "A") | `"A"` (діапазон 95..100) | `"B"` | Major / P2 | `GradeCalculatorTest.ectsLetterAtBoundary95MustBeA()` | **VERIFIED (Approved)** |
| **MOD-BUG-002** | [#12](https://github.com/Sasha26062007/EDUPORTAL/issues/12) | **BL-30** | `GradeCalculator.latePenalty(double score, int daysLate)` | `score = 100.0, daysLate = 2` | `80.0` б. ($100 \times (1 - 0.2)$) | `81.0` б. ($100 \times 0.9^2$) | Major / P2 | `GradeCalculatorTest.latePenaltyTwoDaysLinearDiscountMustBeEighty()` | **VERIFIED (Approved)** |
| **MOD-BUG-003** | [#13](https://github.com/Sasha26062007/EDUPORTAL/issues/13) | **BL-50** | `QuizScorer.questionScore(QuizQuestion q, Set<String> a)` | `q: opt{A,B,C,D}, cor{A,B}; a: {C}` | `0.0` б. («але не менше 0») | `-0.5` б. | Major / P2 | `QuizScorerTest.questionScoreMultipleChoiceMustNotBeNegative()` | **VERIFIED (Approved)** |
| **MOD-BUG-004** | [#14](https://github.com/Sasha26062007/EDUPORTAL/issues/14) | **BL-60** | `QuizScorer.score(Map a, List<QuizQuestion> key)` | 1 правильне з 3 питань ($1/3 \times 100$) | `33.3%` (округлення до 1 знака HALF_UP) | `33.0%` (ціле число) | Normal / P3 | `QuizScorerTest.scoreAttemptRoundsToOneDecimalPlace()` | **VERIFIED (Approved)** |
| **MOD-BUG-005** | [#15](https://github.com/Sasha26062007/EDUPORTAL/issues/15) | **BL-70** | `EnrollmentValidator.withinPeriod(Course c, LocalDate d)` | `today = 2026-10-01`, `enrolEnd = 2026-09-30` | `false` (період закрито) | `true` (дозволено) | Critical / P1 | `EnrollmentValidatorTest.withinPeriodDayAfterEndMustReturnFalse()` | **VERIFIED (Approved)** |
| **MOD-BUG-006** | [#16](https://github.com/Sasha26062007/EDUPORTAL/issues/16) | **BL-70** | `EnrollmentValidator.hasCapacity(Course c)` | `capacity = 25`, `enrolled = 25` | `false` (вільних місць немає) | `true` (дозволено) | Major / P2 | `EnrollmentValidatorTest.hasCapacityWhenCourseIsFullMustReturnFalse()` | **VERIFIED (Approved)** |

---

## 3. Висновок щодо готовності до Спринту 5

Усі 6 закладених дефектів модуля `v1` (1 Critical, 4 Major, 1 Normal) повністю відповідають специфікації SRS та підтверджені незалежними автотестами JUnit 5. Жоден дефект не маскувався анотацією `@Disabled`, жодне очікуване значення не підганялося. 

У Спринті 5 ці ж самі 6 тестів слугуватимуть автоматизованим критерієм верифікації виправлень (TDD/Regression testing): після корекції коду в модулі `v2` всі 37 юніт-тестів мають стати зеленими (`BUILD SUCCESS`).
