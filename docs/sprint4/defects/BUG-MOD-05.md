# Звіт про дефект модуля бізнес-логіки: MOD-BUG-005

**Ідентифікатор дефекту:** `MOD-BUG-005`  
**GitHub Issue:** [#15](https://github.com/Sasha26062007/EDUPORTAL/issues/15)  
**Назва:** Відсутність валідації кінцевої дати періоду зарахування (`enrolEnd`) у методі `EnrollmentValidator.withinPeriod()` (порушення вимоги BL-70).  
**Компонент модуля:** `ua.kpi.tzyapz.eduportal.EnrollmentValidator` (версія модуля: `v1.0.0`)  
**Пов'язана вимога:** `BL-70` («Умови самозарахування: дата у межах періоду [enrolStart, enrolEnd] включно»)  
**Тест-клас і метод, що фіксує дефект:**  
`ua.kpi.tzyapz.eduportal.EnrollmentValidatorTest#withinPeriodDayAfterEndMustReturnFalse`  
**Автор звіту:** Олександр Кираківський (`Kyrakivskyi0`), **Інженер з тестування / SDET**  
**Рецензент звіту:** Денис Бучинський (`Denchik`), **Скрам-майстер / Рецензент**  
**Дата реєстрації:** 13 листопада 2026 року  
**Мітки в трекері:** `module`, `severity:critical`, `priority:P1`, `status:open`  

---

## 1. Рівні критичності та терміновості

- **Severity (Критичність):** **Critical**.  
  *Обґрунтування:* Дефект дозволяє несанкціоноване самозарахування студентів на навчальні курси після закінчення офіційного терміну прийому заявок.
- **Priority (Пріоритет):** **P1**.  
  *Обґрунтування:* Блокуючий дефект, вимагає першочергового виправлення у v2.

---

## 2. Тестове середовище (Environment)

- **Модуль бізнес-логіки:** `src/main/java/ua/kpi/tzyapz.eduportal` (v1.0.0)
- **JDK:** Eclipse Temurin 17.0.21
- **Середовище запуску:** Apache Maven 3.9.9, JUnit Jupiter 5.10.2
- **Команда відтворення:**  
  `mvn test -Dtest=EnrollmentValidatorTest#withinPeriodDayAfterEndMustReturnFalse`

---

## 3. Результати

- **Очікуваний результат:** `false` (дата 2026-10-01 при дедлайні 2026-09-30 має відхилятися).
- **Фактичний результат у v1:** `true`. Тест падає з `AssertionFailedError: expected: <false> but was: <true>`.
