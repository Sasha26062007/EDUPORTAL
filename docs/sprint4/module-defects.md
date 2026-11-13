# Реєстр звітів про дефекти модуля бізнес-логіки (Module Defect Reports)

**Дисципліна:** Технології забезпечення якості програмних засобів (ТЗЯПЗ)  
**Проєкт:** Автоматизована система дистанційного навчання «ЕдуПортал» (LMS)  
**Компонент:** Модуль бізнес-логіки `ua.kpi.tzyapz.eduportal` (версія v1)  
**Спринт 4:** «Автоматизація: JUnit 5 і Selenium WebDriver»  
**Тест-аналітик (Reporter):** Олександр Пархоменко (`Parkhomenko0`), група ФБ-44  
**Рецензент (Reviewer):** Денис Бучинський (`Denchik`), Скрам-майстер / Рецензент  
**Аудитор-опонент:** Олександр Панайотов (`5ani0chek`)  
**Дата актуалізації:** 13 листопада 2026 року  

---

## 1. Загальний огляд виявлених дефектів

Під час виконання набору модульних тестів JUnit 5 для модуля `ua.kpi.tzyapz.eduportal` (версія коду v1) на основі специфікації **SRS 1.1 (розділ 5 BL)** було виявлено, локалізовано та верифіковано **6 закладених дефектів вихідного коду**.

Відповідно до §4.9 та §12 методичних вказівок, падаючі тести не підганялися під некоректну поведінку програми, а були оформлені як офіційні звіти про дефекти у GitHub Issues репозиторію з обов'язковою міткою `module`.

### Зведена таблиця закладених дефектів модуля версії v1:
| ID дефекту | GitHub Issue | Вимога SRS | Клас та метод модуля v1 | Падаючий автотест JUnit 5 | Severity | Priority | Статус у v1 |
|:---:|:---:|:---:|:---|:---|:---:|:---:|:---:|
| **MOD-BUG-001** | [#11](https://github.com/Sasha26062007/EDUPORTAL/issues/11) | **BL-20** | `GradeCalculator.ectsLetter` | `GradeCalculatorTest.ectsLetterAtBoundary95MustBeA` | **Major** | **P2** | Open (red-test) |
| **MOD-BUG-002** | [#12](https://github.com/Sasha26062007/EDUPORTAL/issues/12) | **BL-30** | `GradeCalculator.latePenalty` | `GradeCalculatorTest.latePenaltyTwoDaysLinearDiscountMustBeEighty` | **Major** | **P2** | Open (red-test) |
| **MOD-BUG-003** | [#13](https://github.com/Sasha26062007/EDUPORTAL/issues/13) | **BL-50** | `QuizScorer.questionScore` | `QuizScorerTest.questionScoreMultipleChoiceMustNotBeNegative` | **Major** | **P2** | Open (red-test) |
| **MOD-BUG-004** | [#14](https://github.com/Sasha26062007/EDUPORTAL/issues/14) | **BL-60** | `QuizScorer.score` | `QuizScorerTest.scoreAttemptRoundsToOneDecimalPlace` | **Normal** | **P3** | Open (red-test) |
| **MOD-BUG-005** | [#15](https://github.com/Sasha26062007/EDUPORTAL/issues/15) | **BL-70** | `EnrollmentValidator.withinPeriod` | `EnrollmentValidatorTest.withinPeriodDayAfterEndMustReturnFalse` | **Critical** | **P1** | Open (red-test) |
| **MOD-BUG-006** | [#16](https://github.com/Sasha26062007/EDUPORTAL/issues/16) | **BL-70** | `EnrollmentValidator.hasCapacity` | `EnrollmentValidatorTest.hasCapacityWhenCourseIsFullMustReturnFalse` | **Major** | **P2** | Open (red-test) |

---

## 2. Детальні звіти про дефекти модуля v1

### Звіт про дефект MOD-BUG-001
* **ID:** MOD-BUG-001 (GitHub Issue [#11](https://github.com/Sasha26062007/EDUPORTAL/issues/11))
* **Заголовок:** `[MOD-BUG-001] [BL-20] GradeCalculator.ectsLetter: для граничного значення 95.0 повертає 'B' замість 'A'`
* **Мітки в трекері:** `module`, `severity:major`, `priority:P2`, `status:open`
* **Пов'язана вимога:** SRS розділ 5, вимога **BL-20** («Шкала ECTS»)
* **Падаючий автотест:** `ua.kpi.tzyapz.eduportal.GradeCalculatorTest#ectsLetterAtBoundary95MustBeA`
* **Опис дефекту:** При обчисленні літери шкали ECTS за підсумковим балом метод `ectsLetter(double total)` повертає `"B"` для результату рівно 95.0 балів. За специфікацією SRS BL-20, бал 95.0 є нижньою включною межею оцінки `"A"` ($95.0 \le total \le 100.0$).
* **Причина в коді:** У `GradeCalculator.java` (рядок 45) використано строгу нерівність `if (total > 95)` замість `>= 95`.
* **Severity / Priority:** Major / P2 (спотворює підсумкову оцінку студента без аварійного завершення програми).
* **Команда відтворення:**
  ```bash
  mvn test -Dtest=GradeCalculatorTest#ectsLetterAtBoundary95MustBeA
  ```

---

### Звіт про дефект MOD-BUG-002
* **ID:** MOD-BUG-002 (GitHub Issue [#12](https://github.com/Sasha26062007/EDUPORTAL/issues/12))
* **Заголовок:** `[MOD-BUG-002] [BL-30] GradeCalculator.latePenalty: розрахунок за формулою складного відсотка замість лінійного штрафу 10% на добу`
* **Мітки в трекері:** `module`, `severity:major`, `priority:P2`, `status:open`
* **Пов'язана вимога:** SRS розділ 5, вимога **BL-30** («Штраф за запізнення робіт»)
* **Падаючий автотест:** `ua.kpi.tzyapz.eduportal.GradeCalculatorTest#latePenaltyTwoDaysLinearDiscountMustBeEighty`
* **Опис дефекту:** Метод `latePenalty(double score, int daysLate)` розраховує штраф за формулою геометричної прогресії `score * Math.pow(0.9, cappedDays)`. За бізнес-правилом SRS BL-30, штраф є фіксованим лінійним: мінус 10% за кожен день (2 дні = -20% -> 80.00 балів зі 100, а код повертає $100 \times 0.9^2 = 81.00$).
* **Причина в коді:** У `GradeCalculator.java` (рядок 58) застосовано експоненційну формулу `score * Math.pow(0.9, daysLate)`.
* **Severity / Priority:** Major / P2 (порушує затверджений навчальний регламент нарахування штрафів).
* **Команда відтворення:**
  ```bash
  mvn test -Dtest=GradeCalculatorTest#latePenaltyTwoDaysLinearDiscountMustBeEighty
  ```

---

### Звіт про дефект MOD-BUG-003
* **ID:** MOD-BUG-003 (GitHub Issue [#13](https://github.com/Sasha26062007/EDUPORTAL/issues/13))
* **Заголовок:** `[MOD-BUG-003] [BL-50] QuizScorer.questionScore: можливість отримання від'ємного балу за питання з кількома відповідями`
* **Мітки в трекері:** `module`, `severity:major`, `priority:P2`, `status:open`
* **Пов'язана вимога:** SRS розділ 5, вимога **BL-50** («Питання з кількома відповідями: частка правильних мінус частка неправильних, але не менше 0»)
* **Падаючий автотест:** `ua.kpi.tzyapz.eduportal.QuizScorerTest#questionScoreMultipleChoiceMustNotBeNegative`
* **Опис дефекту:** У методі `QuizScorer.questionScore` при обчисленні балу за питання з кількома варіантами відповідей відсутнє обмеження знизу 0.0. Коли студент обирає лише неправильні варіанти, повертається від'ємне число (наприклад, -0.5), що порушує SRS BL-50.
* **Причина в коді:** У `QuizScorer.java` (рядок 42) повертається `correctShare - wrongShare` без виклику `Math.max(0.0, ...)`.
* **Severity / Priority:** Major / P2 (неприпустимі від'ємні бали за елементи тесту).
* **Команда відтворення:**
  ```bash
  mvn test -Dtest=QuizScorerTest#questionScoreMultipleChoiceMustNotBeNegative
  ```

---

### Звіт про дефект MOD-BUG-004
* **ID:** MOD-BUG-004 (GitHub Issue [#14](https://github.com/Sasha26062007/EDUPORTAL/issues/14))
* **Заголовок:** `[MOD-BUG-004] [BL-60] QuizScorer.score: округлення результату тесту до цілого числа замість 1 десяткового знака`
* **Мітки в трекері:** `module`, `severity:normal`, `priority:P3`, `status:open`
* **Пов'язана вимога:** SRS розділ 5, вимога **BL-60** («Підсумок спроби тесту: округлення до 1 десяткового знака за правилом HALF_UP»)
* **Падаючий автотест:** `ua.kpi.tzyapz.eduportal.QuizScorerTest#scoreAttemptRoundsToOneDecimalPlace`
* **Опис дефекту:** Метод `QuizScorer.score` виконує округлення `(double) Math.round(percent)`, відкидаючи дробову частину до цілого числа (33.0 замість 33.3 при результаті $1/3 \times 100$). За SRS BL-60 вимагається округлення до 1 знака після коми за правилом HALF_UP.
* **Причина в коді:** У `QuizScorer.java` (рядок 63) використано `Math.round(percent)`.
* **Severity / Priority:** Normal / P3 (похибка форматування відсотка успішності).
* **Команда відтворення:**
  ```bash
  mvn test -Dtest=QuizScorerTest#scoreAttemptRoundsToOneDecimalPlace
  ```

---

### Звіт про дефект MOD-BUG-005
* **ID:** MOD-BUG-005 (GitHub Issue [#15](https://github.com/Sasha26062007/EDUPORTAL/issues/15))
* **Заголовок:** `[MOD-BUG-005] [BL-70] EnrollmentValidator.withinPeriod: ігнорування дати завершення періоду прийому (enrolEnd)`
* **Мітки в трекері:** `module`, `severity:critical`, `priority:P1`, `status:open`
* **Пов'язана вимога:** SRS розділ 5, вимога **BL-70** («Умови самозарахування: реєстрація дозволена лише у межах [enrolStart, enrolEnd] включно»)
* **Падаючий автотест:** `ua.kpi.tzyapz.eduportal.EnrollmentValidatorTest#withinPeriodDayAfterEndMustReturnFalse`
* **Опис дефекту:** Метод `EnrollmentValidator.withinPeriod` перевіряє тільки умову `!date.isBefore(course.enrolStart())`, не перевіряючи `course.enrolEnd()`. Внаслідок цього студент може зареєструватися на курс після закриття набору (наприклад, 2026-10-01 при дедлайні 2026-09-30).
* **Причина в коді:** У `EnrollmentValidator.java` (рядок 51) відсутня перевірка кінцевої дати.
* **Severity / Priority:** Critical / P1 (блокуючий бізнес-дефект, порушує часові рамки прийому).
* **Команда відтворення:**
  ```bash
  mvn test -Dtest=EnrollmentValidatorTest#withinPeriodDayAfterEndMustReturnFalse
  ```

---

### Звіт про дефект MOD-BUG-006
* **ID:** MOD-BUG-006 (GitHub Issue [#16](https://github.com/Sasha26062007/EDUPORTAL/issues/16))
* **Заголовок:** `[MOD-BUG-006] [BL-70] EnrollmentValidator.hasCapacity: дозвіл самозарахування на повністю заповнений курс при enrolled == capacity`
* **Мітки в трекері:** `module`, `severity:major`, `priority:P2`, `status:open`
* **Пов'язана вимога:** SRS розділ 5, вимога **BL-70** («Умови самозарахування: на курсі є вільні місця: enrolled < capacity»)
* **Падаючий автотест:** `ua.kpi.tzyapz.eduportal.EnrollmentValidatorTest#hasCapacityWhenCourseIsFullMustReturnFalse`
* **Опис дефекту:** Метод `EnrollmentValidator.hasCapacity` використовує нестрогу нерівність `enrolled <= capacity`, дозволяючи реєстрацію 26-го студента при місткості 25 осіб. За SRS BL-70 обов'язковою умовою є наявність вільних місць ($enrolled < capacity$).
* **Причина в коді:** У `EnrollmentValidator.java` (рядок 58) використано `<= capacity` замість `< capacity`.
* **Severity / Priority:** Major / P2 (переповнення академічних груп понад ліміт).
* **Команда відтворення:**
  ```bash
  mvn test -Dtest=EnrollmentValidatorTest#hasCapacityWhenCourseIsFullMustReturnFalse
  ```
