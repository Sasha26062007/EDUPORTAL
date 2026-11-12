# Протокол виконання тестів Surefire (Surefire Execution Summary)

**Дисципліна:** Технології забезпечення якості програмних засобів (ТЗЯПЗ)  
**Спринт №:** 4 («Автоматизація: JUnit 5 і Selenium WebDriver»)  
**Команда:** Команда EduGuard (5 осіб)  
**Продукт:** Система дистанційного навчання «ЕдуПортал» (`ua.kpi.tzyapz.eduportal`)  
**Версія модуля:** v1.0.0 (модуль із закладеними дефектами)  
**Дата виконання прогону:** 10 листопада 2026 року  
**Керівник тестування (Test Lead):** Володимир Костенко (`@vov4ik-qa`)  

---

## 1. Підсумковий протокол швидкого юніт-прогону (`mvn clean test`)

Команда запуску у корені репозиторію:
```bash
mvn clean test
```

### Зведений вивід Maven Surefire Plugin:

```text
[INFO] Scanning for projects...
[INFO] ------------------------------------------------------------------------
[INFO] Building ТЗЯПЗ · Репозиторій автотестів команди EduGuard · «ЕдуПортал» (Спринт 4) 1.0.0
[INFO] ------------------------------------------------------------------------
[INFO] --- maven-compiler-plugin:3.13.0:compile (default-compile) @ qa-eduportal ---
[INFO] Compiling 7 source files to target/classes
[INFO] --- maven-compiler-plugin:3.13.0:testCompile (default-testCompile) @ qa-eduportal ---
[INFO] Compiling 5 test source files to target/test-classes
[INFO] --- maven-surefire-plugin:3.2.5:test (default-test) @ qa-eduportal ---
[INFO] Using auto detected provider: org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO] Running ua.kpi.tzyapz.eduportal.GradeCalculatorTest
>>> Початок тестового набору GradeCalculatorTest
[ERROR] Tests run: 14, Failures: 2, Errors: 0, Skipped: 0, Time elapsed: 0.185 s <<< FAILURE! -- in ua.kpi.tzyapz.eduportal.GradeCalculatorTest
[ERROR] ua.kpi.tzyapz.eduportal.GradeCalculatorTest.ectsLetterBoundaryExact95MustBeA -- Time elapsed: 0.015 s <<< FAILURE!
org.opentest4j.AssertionFailedError: Межа 95.00 балів повинна давати оцінку 'A' за вимогами ECTS (DEF-01) ==> expected: <A> but was: <B>
	at ua.kpi.tzyapz.eduportal.GradeCalculatorTest.ectsLetterBoundaryExact95MustBeA(GradeCalculatorTest.java:166)
[ERROR] ua.kpi.tzyapz.eduportal.GradeCalculatorTest.latePenaltyLinearTwoDaysMustBe80 -- Time elapsed: 0.008 s <<< FAILURE!
org.opentest4j.AssertionFailedError: Штраф за 2 дні має становити 20% (лінійний розрахунок: 80.00, DEF-02) ==> expected: <80.0> but was: <81.0>
	at ua.kpi.tzyapz.eduportal.GradeCalculatorTest.latePenaltyLinearTwoDaysMustBe80(GradeCalculatorTest.java:208)
<<< Завершення набору GradeCalculatorTest

[INFO] Running ua.kpi.tzyapz.eduportal.QuizScorerTest
>>> Початок тестового набору QuizScorerTest
[ERROR] Tests run: 9, Failures: 2, Errors: 0, Skipped: 0, Time elapsed: 0.092 s <<< FAILURE! -- in ua.kpi.tzyapz.eduportal.QuizScorerTest
[ERROR] ua.kpi.tzyapz.eduportal.QuizScorerTest.multipleChoiceNegativeScoreClampedToZero -- Time elapsed: 0.007 s <<< FAILURE!
org.opentest4j.AssertionFailedError: Бал не може бути меншим за 0.0 при штрафах (DEF-03) ==> expected: <0.0> but was: <-0.5>
	at ua.kpi.tzyapz.eduportal.QuizScorerTest.multipleChoiceNegativeScoreClampedToZero(QuizScorerTest.java:114)
[ERROR] ua.kpi.tzyapz.eduportal.QuizScorerTest.quizScoreRoundedToOneDecimalPlace -- Time elapsed: 0.006 s <<< FAILURE!
org.opentest4j.AssertionFailedError: Підсумок спроби повинен зберігати 1 знак після коми: 66.7% (DEF-04) ==> expected: <66.7> but was: <67.0>
	at ua.kpi.tzyapz.eduportal.QuizScorerTest.quizScoreRoundedToOneDecimalPlace(QuizScorerTest.java:164)
<<< Завершення набору QuizScorerTest

[INFO] Running ua.kpi.tzyapz.eduportal.EnrollmentValidatorTest
>>> Початок тестового набору EnrollmentValidatorTest
[ERROR] Tests run: 11, Failures: 2, Errors: 0, Skipped: 0, Time elapsed: 0.084 s <<< FAILURE! -- in ua.kpi.tzyapz.eduportal.EnrollmentValidatorTest
[ERROR] ua.kpi.tzyapz.eduportal.EnrollmentValidatorTest.withinPeriodAfterEnrolEndFails -- Time elapsed: 0.005 s <<< FAILURE!
org.opentest4j.AssertionFailedError: Дата 2026-09-21 є після завершення періоду (2026-09-20), очікується false (DEF-05) ==> expected: <false> but was: <true>
	at ua.kpi.tzyapz.eduportal.EnrollmentValidatorTest.withinPeriodAfterEnrolEndFails(EnrollmentValidatorTest.java:82)
[ERROR] ua.kpi.tzyapz.eduportal.EnrollmentValidatorTest.hasCapacityWhenFullFails -- Time elapsed: 0.006 s <<< FAILURE!
org.opentest4j.AssertionFailedError: При enrolled == capacity вільних місць немає, очікується false (DEF-06) ==> expected: <false> but was: <true>
	at ua.kpi.tzyapz.eduportal.EnrollmentValidatorTest.hasCapacityWhenFullFails(EnrollmentValidatorTest.java:110)
<<< Завершення набору EnrollmentValidatorTest

[INFO] 
[INFO] Results:
[INFO] 
[ERROR] Failures: 6, Errors: 0, Skipped: 0
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
```

---

## 2. Повний прогін із WebDriver UI-тестами (`mvn test -DexcludedGroups=none`)

Команда запуску повного регресійного комплекту:
```bash
mvn test -DexcludedGroups=none
```

### Результати прогону:
- **Загальна кількість тестів (Tests run):** 42 (з розкриттям усіх ітерацій параметризованих тестів) / 32 окремі тест-методи.
- **Failures:** 6 (виключно закладені дефекти модуля бізнес-логіки v1).
- **Errors:** 0 (жодної помилки компіляції або непередбачених runtime-винятків).
- **Skipped:** 0.
- **UI WebDriver тести:** Усі 8 UI-тестів (`LoginTest`: 3, `CourseUiTest`: 5) виконано зі статусом **PASSED (100% Green)**.

---

## 3. Реєстр зафіксованих дефектів модуля версії 1 (Зв'язок тестів із баг-трекером)

| ID дефекту | Вимога SRS | Клас і тестовий метод, що впав | Очікуваний результат (BL) | Фактичний результат (v1) | Severity | Причина дефекту в коді v1 |
|:---:|:---:|:---|:---|:---|:---:|:---|
| **DEF-01** | BL-20 | `GradeCalculatorTest#ectsLetterBoundaryExact95MustBeA` | `"A"` ($95.00 \le total \le 100.00$) | `"B"` | High | У коді `if (total > 95)` замість `>= 95`. Рівно на межі 95.00 оцінка занижується до B. |
| **DEF-02** | BL-30 | `GradeCalculatorTest#latePenaltyLinearTwoDaysMustBe80` | `80.00` (-20% лінійний штраф) | `81.00` | High | Застосовано складний відсоток: `score * Math.pow(0.9, days)` замість `score * (1 - days * 0.1)`. |
| **DEF-03** | BL-50 | `QuizScorerTest#multipleChoiceNegativeScoreClampedToZero` | `0.0` (бал не може бути від'ємним) | `-0.5` | High | Відсутнє обмеження знизу 0.0: `return correctShare - wrongShare;` без `Math.max(0.0, ...)`. |
| **DEF-04** | BL-60 | `QuizScorerTest#quizScoreRoundedToOneDecimalPlace` | `66.7` (1 знак після коми, HALF_UP) | `67.0` | Medium | Використано `(double) Math.round(percent);`, що округлює до цілого числа замість 1 знака. |
| **DEF-05** | BL-70 | `EnrollmentValidatorTest#withinPeriodAfterEnrolEndFails` | `false` (дата після завершення періоду) | `true` | Critical | Пропущено перевірку кінцевої дати: `return !today.isBefore(course.enrolStart());` ігнорує `enrolEnd`. |
| **DEF-06** | BL-70 | `EnrollmentValidatorTest#hasCapacityWhenFullFails` | `false` (місць немає, $enrolled == capacity$) | `true` | High | Некоректний оператор: `<= capacity` замість `< capacity`. Дозволяє перевищення ліміту студентів. |

---

## 4. Висновок щодо якості модуля версії 1

1. **Всі «падіння» тестів є обґрунтованими дефектами:** Жоден тест не було «підігнано» під некоректну поведінку коду модуля v1. Кожне падіння строго простежується до пункту специфікації BL-10..BL-80.
2. **Готовність до регресійного тестування (Спринт 5):** Створений набір тестів залишається незмінним у репозиторії. У спринті 5 модуль буде оновлено до версії v2, де всі 6 тестів мають перейти у зелений стан (Passed), підтвердивши виправлення дефектів без регресії.
