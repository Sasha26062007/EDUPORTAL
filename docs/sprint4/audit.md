# Акт аудиту якості автотестів та верифікації дефектів (Quality Audit Report)

**Дисципліна:** Технології забезпечення якості програмних засобів (ТЗЯПЗ)  
**Проєкт:** Автоматизована система дистанційного навчання «ЕдуПортал» (LMS)  
**Компонент:** Тестовий комплекс JUnit 5 та Selenium WebDriver (Спринт 4)  
**Аудитор-опонент:** **Олександр Панайотов** (`5ani0chek`, Саша26062007), група ФБ-44  
**Об'єкт аудиту:** Репозиторій автотестів `ua.kpi.tzyapz.eduportal` (коміт поточного інкременту)  
**Дата проведення аудиту:** 13 листопада 2026 року  
**Вердикт аудитора:** **ПОВНІСТЮ ЗАТВЕРДЖЕНО (DO D CONFIRMED — 100% ВІДПОВІДНІСТЬ)**  

---

## 1. Мета та методологія аудиту

Метою аудиту є незалежний контроль якості створеного автоматизованого тестового комплексу на відповідність вимогам специфікації SRS 1.1, регламентам §4.9, §5.3, §6, §8 та критеріям Definition of Done (DoD §7) Спринту 4:
1. **Верифікація простежуваності (Traceability):** перевірка наскрізного зв'язку між вимогами SRS 1.1 (BL-10..BL-80, F-10..F-100), тест-кейсами Спринту 3 (TC-01..TC-26), кодом автотестів у `src/test/java/` та звітами про дефекти.
2. **Контроль правила No Assert Tampering (§4.9):** підтвердження того, що тести для дефектних фрагментів коду `v1` сформульовані строго за вимогами SRS 1.1 і залишені «червоними» (FAIL), а не були штучно підігнані або приховані `@Disabled`.
3. **Контроль якості UI-тестів (§5.3):** перевірка відсутності крихких патернів (`Thread.sleep`, неявні очікування, абсолютні XPath, «пусті» перевірки).
4. **Контроль реєстрації дефектів модуля у GitHub Issues:** перевірка створення карток дефектів з обов'язковою міткою `module`.

---

## 2. Результати верифікації середовища та запуску тестування

Аудитор провів незалежне клонування та запуск репозиторію на тестовій машині:
- **Операційна система:** Windows 11 Pro 64-bit
- **Java Development Kit:** Eclipse Temurin OpenJDK 17.0.21
- **Інструмент збірки:** Apache Maven 3.9.9
- **Браузер для UI:** Google Chrome 130+ (режим `--headless=new`)

### 2.1. Швидкий юніт-прогін (`mvn test`)
- Виконано команду: `mvn test`
- **Tests run:** 64 запуски (37 унікальних тест-методів у 3 тестових класах).
- **Failures:** 6 (рівно 6 закладених дефектів модуля v1).
- **Errors / Skipped:** 0 / 0.
- **Час виконання:** ~1.5 с.
- **Висновок:** Швидкий модульний цикл ізольований через властивість `excludedGroups=ui` у `pom.xml`, збирається чисто і стабільно.

### 2.2. Повний прогін із включенням UI (`mvn test -DexcludedGroups=none`)
- Виконано команду: `mvn test -DexcludedGroups=none`
- **Tests run:** 75 запусків (64 модульні + 11 UI-сценаріїв).
- **UI Tests:** 11 з 11 PASSED (100% Green, 0 failures, 0 errors, 0 timeouts).
- **Failures:** 6 (виключно закладені дефекти модуля бізнес-логіки).
- **Висновок:** Усі 11 UI-сценаріїв стабільно проходять на демо-стенді Moodle LMS без жодних зависань.

---

## 3. Аудит простежуваності (Traceability Verification)

### 3.1. Модульні тести бізнес-логіки (BL-10..BL-80)
Аудитор особисто перевірив наявність у Java-коді кожного з 37 тест-методів, зазначених у матриці покриття `docs/sprint4/coverage-matrix.md`:
* **GradeCalculatorTest (12 методів):**
  - BL-10: `weightedTotalTwoStandardItems()`, `weightedTotalRoundsHalfUp()`, `weightedTotalThrowsWhenWeightsDoNotSumToHundred()`, `weightedTotalThrowsWhenScoreOutOfBounds()`, `weightedTotalThrowsWhenListIsEmpty()`.
  - BL-20: `ectsLetterStandardBoundaries(double, String)`, `ectsLetterAtBoundary95MustBeA()`, `ectsLetterThrowsWhenOutOfRange(double)`.
  - BL-30: `latePenaltyZeroDaysReturnsFullScore()`, `latePenaltyOneDaySubtractsTenPercent()`, `latePenaltyTwoDaysLinearDiscountMustBeEighty()`, `latePenaltyNegativeDaysThrowsException()`.
* **QuizScorerTest (8 методів):**
  - BL-40: `questionScoreSingleChoiceCorrectReturnsOne()`, `questionScoreSingleChoiceIncorrectOrEmptyReturnsZero()`.
  - BL-50: `questionScoreMultipleChoicePartialCredit()`, `questionScoreMultipleChoiceMustNotBeNegative()`, `questionScoreUnknownOptionThrows()`.
  - BL-60: `scoreAttemptStandardCalculation()`, `scoreAttemptRoundsToOneDecimalPlace()`, `scoreAttemptEmptyKeyThrows()`.
* **EnrollmentValidatorTest (17 методів):**
  - BL-70: `keyMatchesExactValueReturnsTrue()`, `keyMatchesCaseSensitiveAndIncorrectKeysReturnFalse(String)`, `withinPeriodValidDatesReturnTrue(LocalDate)`, `withinPeriodDayBeforeStartReturnsFalse()`, `withinPeriodDayAfterEndMustReturnFalse()`, `hasCapacityWhenFreeSeatsExistReturnsTrue(int)`, `hasCapacityWhenCourseIsFullMustReturnFalse()`, `hasCapacityWhenOverfilledReturnsFalse()`, `prerequisiteMetWhenNoPrerequisiteRequiredReturnsTrue()`, `prerequisiteMetBoundariesAroundSixty(double, boolean)`, `prerequisiteMetWhenCourseNotAttemptedReturnsFalse()`, `canEnrollWhenAllFourConditionsMetReturnsTrue()`, `canEnrollFailsWhenKeyIsWrong()`, `canEnrollFailsWhenPrerequisiteNotMet()`.
  - BL-80: `violationsReturnsEmptyListWhenNoViolations()`, `violationsReturnsAllCodesInExactSpecificationOrder()`, `violationsWithNullArgumentsThrowsException()`.

**Висновок:** 100% методів матриці присутні в коді `src/test/java/`, назви та вхідні дані збігаються до літери.

### 3.2. Автоматизовані сценарії WebDriver (TC-01..TC-26)
Аудитор верифікував відповідність 11 UI-методів тест-кейсам Спринту 3:
* **MoodleLoginUiTest:** `validStudentLoginOpensDashboard` (TC-01), `invalidPasswordShowsErrorMessage` (TC-02), `emptyCredentialsValidationBlocksSubmission` (TC-03).
* **MoodleCourseEnrollmentUiTest:** `searchNonExistentCourseShowsNoResults` (TC-07), `openCoursePageDisplaysSectionsAndContent` (TC-08), `courseEnrollmentStatusVerification` (TC-09).
* **MoodleAssignmentUiTest:** `assignmentPageDisplaysTitleAndStatus` (TC-12), `submissionStatusContainsGradingAndTimingInfo` (TC-15), `teacherCanAccessSubmissionsOverview` (TC-16).
* **MoodleGradebookUiTest:** `studentCanViewPersonalGradesReport` (TC-25), `teacherCanAccessCourseGraderReport` (TC-26).

**Висновок:** Усі 11 сценаріїв мають явні очікування `WebDriverWait`, використовують Page Object Model і повністю простежуються до Спринту 3.

---

## 4. Верифікація закладених дефектів модуля та зв'язок із трекером

Аудитор підтвердив реєстрацію всіх 6 закладених дефектів модуля v1 у веб-трекері GitHub Issues репозиторію з обов'язковою міткою `module`:

1. **[MOD-BUG-001]** (Issue [#11](https://github.com/Sasha26062007/EDUPORTAL/issues/11)) — `GradeCalculatorTest#ectsLetterAtBoundary95MustBeA` (BL-20, межа 95.0 дає 'B' замість 'A').
2. **[MOD-BUG-002]** (Issue [#12](https://github.com/Sasha26062007/EDUPORTAL/issues/12)) — `GradeCalculatorTest#latePenaltyTwoDaysLinearDiscountMustBeEighty` (BL-30, штраф за формулою складного відсотка).
3. **[MOD-BUG-003]** (Issue [#13](https://github.com/Sasha26062007/EDUPORTAL/issues/13)) — `QuizScorerTest#questionScoreMultipleChoiceMustNotBeNegative` (BL-50, від'ємний бал -0.5 замість 0.0).
4. **[MOD-BUG-004]** (Issue [#14](https://github.com/Sasha26062007/EDUPORTAL/issues/14)) — `QuizScorerTest#scoreAttemptRoundsToOneDecimalPlace` (BL-60, округлення до цілого 33.0 замість 33.3).
5. **[MOD-BUG-005]** (Issue [#15](https://github.com/Sasha26062007/EDUPORTAL/issues/15)) — `EnrollmentValidatorTest#withinPeriodDayAfterEndMustReturnFalse` (BL-70, ігнорування `enrolEnd`).
6. **[MOD-BUG-006]** (Issue [#16](https://github.com/Sasha26062007/EDUPORTAL/issues/16)) — `EnrollmentValidatorTest#hasCapacityWhenCourseIsFullMustReturnFalse` (BL-70, дозвіл запису при заповненій групі 25 з 25).

У коментарях до кожного падаючого методу в Java-коді зафіксовано ID звіту `MOD-BUG-001`–`MOD-BUG-006` та номер відповідного GitHub Issue.

---

## 5. Підсумковий чек-лист критеріїв Definition of Done (DoD §7)

| № п/п | Пункт Definition of Done (DoD) | Норматив методички | Фактичний стан у проєкті | Статус верифікації |
|:---:|:---|:---:|:---:|:---:|
| 1 | Репозиторій містить Maven-проєкт із чіткою інструкцією запуску в README | §8 | Корінь репозиторію, повна інструкція | **ПІДТВЕРДЖЕНО (Pass)** |
| 2 | `mvn clean test` виконується однією командою без ручних налаштувань | §8 | Виконується, excludedGroups=ui працює | **ПІДТВЕРДЖЕНО (Pass)** |
| 3 | Кількість юніт-тестів відповідає нормативу команди (5 осіб) | $\ge 20$ тестів | **37 методів (64 прогони)** | **ПЕРЕВИКОНАНО (+85%)** |
| 4 | Кількість UI-сценаріїв відповідає нормативу команди | $\ge 8$ сценаріїв | **11 сценаріїв** (усі 100% Green) | **ПЕРЕВИКОНАНО (+37%)** |
| 5 | Матриця покриття BL пов'язує кожне правило з тестами | 100% BL-10..80 | Усі 8 правил, повна простежуваність | **ПІДТВЕРДЖЕНО (Pass)** |
| 6 | Специфікація UI пов'язує кожен автотест із кейсом Спринту 3 | 100% зв'язок | Усі 11 тестів простежуються до TC-xx | **ПІДТВЕРДЖЕНО (Pass)** |
| 7 | Всі тести використовують Page Object Model | Обов'язково | 6 POM-класів у пакеті `ui/pages/` | **ПІДТВЕРДЖЕНО (Pass)** |
| 8 | Дефекти модуля заведені в трекер із міткою `module` | Обов'язково | Створено GitHub Issues #11–#16 | **ПІДТВЕРДЖЕНО (Pass)** |
| 9 | Правило No Assert Tampering виконано | Обов'язково | 6 red-тестів зафіксовано за SRS 1.1 | **ПІДТВЕРДЖЕНО (Pass)** |
| 10 | Жоден падаючий тест не замасковано `@Disabled` | Заборонено | `@Disabled` відсутній у коді | **ПІДТВЕРДЖЕНО (Pass)** |
| 11 | Відсутні `Thread.sleep` та неявні очікування в UI | Заборонено | Лише явні `WebDriverWait` | **ПІДТВЕРДЖЕНО (Pass)** |
| 12 | Проєкт Selenium IDE (`.side`) та експорт присутні | Обов'язково | `selenium-ide/eduportal.side` + `export/` | **ПІДТВЕРДЖЕНО (Pass)** |
| 13 | Модуль `v1` залишено незмінним | Обов'язково | 100% побайтовий збіг із v1 | **ПІДТВЕРДЖЕНО (Pass)** |
| 14 | Зведений протокол Surefire відповідає фактичному запуску | Обов'язково | Повна відповідність логам | **ПІДТВЕРДЖЕНО (Pass)** |

---

## 6. Резолюція Аудитора-опонента

Інкремент Спринту 4 «Автоматизація тестування: JUnit 5 та Selenium WebDriver» повністю відповідає всім інженерним нормативам, академічним вимогам методичних вказівок та критеріям Definition of Done. Усі зауваження попереднього рев'ю виправлено на 100%. Комплекс автотестів готовий до демонстрації та фінальної здачі.

**Аудитор-опонент:** Олександр Панайотов (`5ani0chek`)  
**Підпис:** *Панайотов О.*  
**Дата:** 13 листопада 2026 року
