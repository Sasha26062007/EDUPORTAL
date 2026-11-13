# Звіт про дефект модуля бізнес-логіки: MOD-BUG-006

**Ідентифікатор дефекту:** `MOD-BUG-006`  
**GitHub Issue:** [#16](https://github.com/Sasha26062007/EDUPORTAL/issues/16)  
**Назва:** Некоректний оператор нестрогого порівняння `<=` у методі `EnrollmentValidator.hasCapacity()`, що дозволяє зарахування на повністю заповнений курс (порушення вимоги BL-70).  
**Компонент модуля:** `ua.kpi.tzyapz.eduportal.EnrollmentValidator` (версія модуля: `v1.0.0`)  
**Пов'язана вимога:** `BL-70` («Умови самозарахування: на курсі є вільні місця: enrolled < capacity»)  
**Тест-клас і метод, що фіксує дефект:**  
`ua.kpi.tzyapz.eduportal.EnrollmentValidatorTest#hasCapacityWhenCourseIsFullMustReturnFalse`  
**Автор звіту:** Олександр Кираківський (`Kyrakivskyi0`), **Інженер з тестування / SDET**  
**Рецензент звіту:** Денис Бучинський (`Denchik`), **Скрам-майстер / Рецензент**  
**Дата реєстрації:** 13 листопада 2026 року  
**Мітки в трекері:** `module`, `severity:major`, `priority:P2`, `status:open`  

---

## 1. Рівні критичності та терміновості

- **Severity (Критичність):** **Major**.  
  *Обґрунтування:* Дефект веде до переповнення навчальних груп понад встановлену максимальну місткість (`capacity == enrolled == 25`).
- **Priority (Пріоритет):** **P2**.  
  *Обґрунтування:* Підлягає обов'язковому виправленню у наступній версії модуля v2.

---

## 2. Тестове середовище (Environment)

- **Модуль бізнес-логіки:** `src/main/java/ua/kpi/tzyapz.eduportal` (v1.0.0)
- **JDK:** Eclipse Temurin 17.0.21
- **Середовище запуску:** Apache Maven 3.9.9, JUnit Jupiter 5.10.2
- **Команда відтворення:**  
  `mvn test -Dtest=EnrollmentValidatorTest#hasCapacityWhenCourseIsFullMustReturnFalse`

---

## 3. Результати

- **Очікуваний результат:** `false` (при заповненості 25 з 25 вільних місць немає).
- **Фактичний результат у v1:** `true`. Тест падає з `AssertionFailedError: expected: <false> but was: <true>`.
