# Протокол виконання тестів Surefire (Surefire Execution Summary)

**Дисципліна:** Технології забезпечення якості програмних засобів (ТЗЯПЗ)  
**Спринт №:** 4 («Автоматизація: JUnit 5 і Selenium WebDriver»)  
**Команда:** Команда EduGuard (Команда F, 5 осіб, група ФБ-44)  
**Продукт:** Система дистанційного навчання «ЕдуПортал» (`ua.kpi.tzyapz.eduportal`)  
**Версія модуля:** v1.0.0 (еталонний модуль із закладеними дефектами)  
**Дата виконання прогону:** 13 листопада 2026 року  
**Керівник тестування (Test Lead):** Володимир Костенко (`@vov4ik-qa`)  
**Аудитор-опонент:** Олександр Панайотов (`5ani0chek`)  

---

## 1. Підсумковий протокол швидкого юніт-прогону (`mvn test`)

Команда запуску у корені репозиторію:
```bash
mvn test
```
*(За замовчуванням через `pom.xml` властивість `<excludedGroups>ui</excludedGroups>` виключає тривалі UI-тести й виконує лише швидкий модульний регресійний набір).*

### Фактичний протокол Maven Surefire Plugin (37 тест-методів, 64 тестові прогони):

```text
[INFO] Scanning for projects...
[INFO] -------------------< ua.kpi.tzyapz:eduportal-module >-------------------
[INFO] Building ТЗЯПЗ · Репозиторій автотестів команди EduGuard · «ЕдуПортал» (Спринт 4) 1.0.0
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] --- resources:3.3.1:resources (default-resources) @ eduportal-module ---
[INFO] skip non existing resourceDirectory C:\TZIPZ2\src\main\resources
[INFO] --- compiler:3.13.0:compile (default-compile) @ eduportal-module ---
[INFO] Compiling 7 source files with javac [debug release 17] to target\classes
[INFO] --- resources:3.3.1:testResources (default-testResources) @ eduportal-module ---
[INFO] skip non existing resourceDirectory C:\TZIPZ2\src\test\resources
[INFO] --- compiler:3.13.0:testCompile (default-testCompile) @ eduportal-module ---
[INFO] Compiling 15 source files with javac [debug release 17] to target\test-classes
[INFO] --- surefire:3.2.5:test (default-test) @ eduportal-module ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running ua.kpi.tzyapz.eduportal.EnrollmentValidatorTest
[ERROR] Tests run: 32, Failures: 2, Errors: 0, Skipped: 0, Time elapsed: 1.043 s <<< FAILURE! -- in ua.kpi.tzyapz.eduportal.EnrollmentValidatorTest
[ERROR] ua.kpi.tzyapz.eduportal.EnrollmentValidatorTest.hasCapacityWhenCourseIsFullMustReturnFalse -- Time elapsed: 0.096 s <<< FAILURE!
org.opentest4j.AssertionFailedError: BL-70: коли курс заповнений повністю (enrolled == capacity == 25), hasCapacity має повертати false ==> expected: <false> but was: <true>
	at ua.kpi.tzyapz.eduportal.EnrollmentValidatorTest.hasCapacityWhenCourseIsFullMustReturnFalse(EnrollmentValidatorTest.java:170)
[ERROR] ua.kpi.tzyapz.eduportal.EnrollmentValidatorTest.withinPeriodDayAfterEndMustReturnFalse -- Time elapsed: 0.011 s <<< FAILURE!
org.opentest4j.AssertionFailedError: BL-70: дата за 1 день після завершення періоду (2026-10-01) повинна повертати false ==> expected: <false> but was: <true>
	at ua.kpi.tzyapz.eduportal.EnrollmentValidatorTest.withinPeriodDayAfterEndMustReturnFalse(EnrollmentValidatorTest.java:129)

[INFO] Running ua.kpi.tzyapz.eduportal.GradeCalculatorTest
[ERROR] Tests run: 24, Failures: 2, Errors: 0, Skipped: 0, Time elapsed: 0.289 s <<< FAILURE! -- in ua.kpi.tzyapz.eduportal.GradeCalculatorTest
[ERROR] ua.kpi.tzyapz.eduportal.GradeCalculatorTest.ectsLetterAtBoundary95MustBeA -- Time elapsed: 0.032 s <<< FAILURE!
org.opentest4j.AssertionFailedError: BL-20: для граничного значення 95.0 повинна повертатися літера A ==> expected: <A> but was: <B>
	at ua.kpi.tzyapz.eduportal.GradeCalculatorTest.ectsLetterAtBoundary95MustBeA(GradeCalculatorTest.java:137)
[ERROR] ua.kpi.tzyapz.eduportal.GradeCalculatorTest.latePenaltyTwoDaysLinearDiscountMustBeEighty -- Time elapsed: 0.003 s <<< FAILURE!
org.opentest4j.AssertionFailedError: BL-30: 2 дні запізнення — лінійний штраф 20% (100.0 → 80.00) ==> expected: <80.0> but was: <81.0>
	at ua.kpi.tzyapz.eduportal.GradeCalculatorTest.latePenaltyTwoDaysLinearDiscountMustBeEighty(GradeCalculatorTest.java:180)

[INFO] Running ua.kpi.tzyapz.eduportal.QuizScorerTest
[ERROR] Tests run: 8, Failures: 2, Errors: 0, Skipped: 0, Time elapsed: 0.068 s <<< FAILURE! -- in ua.kpi.tzyapz.eduportal.QuizScorerTest
[ERROR] ua.kpi.tzyapz.eduportal.QuizScorerTest.questionScoreMultipleChoiceMustNotBeNegative -- Time elapsed: 0.014 s <<< FAILURE!
org.opentest4j.AssertionFailedError: BL-50: бал за питання не може бути від'ємним (не менше 0.0) ==> expected: <0.0> but was: <-0.5>
	at ua.kpi.tzyapz.eduportal.QuizScorerTest.questionScoreMultipleChoiceMustNotBeNegative(QuizScorerTest.java:104)
[ERROR] ua.kpi.tzyapz.eduportal.QuizScorerTest.scoreAttemptRoundsToOneDecimalPlace -- Time elapsed: 0.003 s <<< FAILURE!
org.opentest4j.AssertionFailedError: BL-60: підсумок спроби округлюється до 1 десяткового знака (33.3%) ==> expected: <33.3> but was: <33.0>
	at ua.kpi.tzyapz.eduportal.QuizScorerTest.scoreAttemptRoundsToOneDecimalPlace(QuizScorerTest.java:162)

[INFO] 
[INFO] Results:
[INFO] 
[ERROR] Failures: 
[ERROR]   EnrollmentValidatorTest.hasCapacityWhenCourseIsFullMustReturnFalse:170 BL-70: коли курс заповнений повністю (enrolled == capacity == 25), hasCapacity має повертати false ==> expected: <false> but was: <true>
[ERROR]   EnrollmentValidatorTest.withinPeriodDayAfterEndMustReturnFalse:129 BL-70: дата за 1 день після завершення періоду (2026-10-01) повинна повертати false ==> expected: <false> but was: <true>
[ERROR]   GradeCalculatorTest.ectsLetterAtBoundary95MustBeA:137 BL-20: для граничного значення 95.0 повинна повертатися літера A ==> expected: <A> but was: <B>
[ERROR]   GradeCalculatorTest.latePenaltyTwoDaysLinearDiscountMustBeEighty:180 BL-30: 2 дні запізнення — лінійний штраф 20% (100.0 → 80.00) ==> expected: <80.0> but was: <81.0>
[ERROR]   QuizScorerTest.questionScoreMultipleChoiceMustNotBeNegative:104 BL-50: бал за питання не може бути від'ємним (не менше 0.0) ==> expected: <0.0> but was: <-0.5>
[ERROR]   QuizScorerTest.scoreAttemptRoundsToOneDecimalPlace:162 BL-60: підсумок спроби округлюється до 1 десяткового знака (33.3%) ==> expected: <33.3> but was: <33.0>
[INFO] 
[ERROR] Tests run: 64, Failures: 6, Errors: 0, Skipped: 0
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
```

---

## 2. Повний прогін із включенням Selenium WebDriver UI-тестів (`mvn test -DexcludedGroups=none`)

Команда запуску повного комплексного набору:
```bash
mvn test -DexcludedGroups=none
```

### Результати прогону (75 тестових запусків):
- **Загальна кількість запусків (Tests run):** 75 (64 модульні запуски + 11 UI-тестів).
- **Failures:** 6 (виключно закладені дефекти навчального модуля бізнес-логіки v1).
- **Errors:** 0.
- **Skipped:** 0.
- **UI WebDriver тести:** Усі 11 UI-сценаріїв у 4 тестових класах виконано зі статусом **PASSED (100% Green)**:
  1. `MoodleLoginUiTest`: 3 з 3 PASSED (TC-01, TC-02, TC-03)
  2. `MoodleCourseEnrollmentUiTest`: 3 з 3 PASSED (TC-07, TC-08, TC-09)
  3. `MoodleAssignmentUiTest`: 3 з 3 PASSED (TC-12, TC-15, TC-16)
  4. `MoodleGradebookUiTest`: 2 з 2 PASSED (TC-25, TC-26)

---

## 3. Реєстр зафіксованих дефектів модуля версії v1 (Зв'язок тестів із GitHub Issues)

Усі 6 закладених дефектів офіційно зареєстровані в трекері GitHub Issues команди з обов'язковою міткою `module`:

| ID дефекту | GitHub Issue | Вимога SRS | Клас і тестовий метод, що падає | Очікуваний результат (SRS 1.1) | Фактичний результат у v1 | Severity / Priority | Причина дефекту в коді v1 |
|:---:|:---:|:---:|:---|:---|:---|:---:|:---|
| **MOD-BUG-001** | [#11](https://github.com/Sasha26062007/EDUPORTAL/issues/11) | **BL-20** | `GradeCalculatorTest#ectsLetterAtBoundary95MustBeA` | `"A"` ($95.00 \le total \le 100.00$) | `"B"` | Major / P2 | У коді `if (total > 95)` замість `>= 95`. На межі 95.00 оцінка занижується до B. |
| **MOD-BUG-002** | [#12](https://github.com/Sasha26062007/EDUPORTAL/issues/12) | **BL-30** | `GradeCalculatorTest#latePenaltyTwoDaysLinearDiscountMustBeEighty` | `80.00` (-20% лінійний штраф) | `81.00` | Major / P2 | Застосовано геометричну прогресію: `score * Math.pow(0.9, days)` замість лінійного штрафу `score * (1 - days * 0.1)`. |
| **MOD-BUG-003** | [#13](https://github.com/Sasha26062007/EDUPORTAL/issues/13) | **BL-50** | `QuizScorerTest#questionScoreMultipleChoiceMustNotBeNegative` | `0.0` (бал не може бути від'ємним) | `-0.5` | Major / P2 | Відсутнє обмеження знизу `Math.max(0.0, ...)` при відніманні частки неправильних відповідей. |
| **MOD-BUG-004** | [#14](https://github.com/Sasha26062007/EDUPORTAL/issues/14) | **BL-60** | `QuizScorerTest#scoreAttemptRoundsToOneDecimalPlace` | `33.3` (1 знак після коми, HALF_UP) | `33.0` | Normal / P3 | Використано `(double) Math.round(percent);`, що округлює до цілого числа замість 1 знака. |
| **MOD-BUG-005** | [#15](https://github.com/Sasha26062007/EDUPORTAL/issues/15) | **BL-70** | `EnrollmentValidatorTest#withinPeriodDayAfterEndMustReturnFalse` | `false` (дата 2026-10-01 після завершення періоду) | `true` | Critical / P1 | Відсутня перевірка кінця періоду: `!today.isBefore(enrolStart)` без перевірки `!today.isAfter(enrolEnd)`. |
| **MOD-BUG-006** | [#16](https://github.com/Sasha26062007/EDUPORTAL/issues/16) | **BL-70** | `EnrollmentValidatorTest#hasCapacityWhenCourseIsFullMustReturnFalse` | `false` (вільних місць немає при $enrolled == capacity == 25$) | `true` | Major / P2 | Некоректний оператор: `<= capacity` замість `< capacity`. Дозволяє запис на заповнений курс. |

---

## 4. Висновок щодо якості навчального модуля v1

1. **Суворе дотримання правила No Assert Tampering (DoD п.9, п.10):** Жоден тест не було штучно «підігнано» під помилкову поведінку коду v1 і не приховано анотацією `@Disabled`. Кожне падіння строго відповідає специфікації SRS 1.1 і зафіксоване в трекері GitHub Issues.
2. **Готовність регресійного комплексу до Спринту 5:** Набір автотестів повністю готовий. Після передачі виправленої версії модуля v2 у наступному спринті ці ж самі тести мають автоматично стати зеленими (BUILD SUCCESS).
