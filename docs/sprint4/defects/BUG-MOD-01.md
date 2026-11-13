# Звіт про дефект модуля бізнес-логіки: MOD-BUG-001

**Ідентифікатор дефекту:** `MOD-BUG-001`  
**GitHub Issue:** [#11](https://github.com/Sasha26062007/EDUPORTAL/issues/11)  
**Назва:** Некоректне визначення оцінки 'A' за шкалою ECTS для граничного підсумку 95.0 балів у методі `GradeCalculator.ectsLetter()` (порушення вимоги BL-20).  
**Компонент модуля:** `ua.kpi.tzyapz.eduportal.GradeCalculator` (версія модуля: `v1.0.0`)  
**Пов'язана вимога:** `BL-20` («Шкала ECTS: A ≥ 95.0, B 85.0–94.99...»)  
**Тест-клас і метод, що фіксує дефект:**  
`ua.kpi.tzyapz.eduportal.GradeCalculatorTest#ectsLetterAtBoundary95MustBeA`  
**Автор звіту:** Олександр Панайотов (`5ani0chek`), **Аудитор-опонент**  
**Рецензент звіту:** Денис Бучинський (`Denchik`), **Скрам-майстер / Рецензент**  
**Дата реєстрації:** 13 листопада 2026 року  
**Мітки в трекері:** `module`, `severity:major`, `priority:P2`, `status:open`  

---

## 1. Рівні критичності та терміновості

- **Severity (Критичність):** **Major**.  
  *Обґрунтування:* Дефект системно спотворює підсумкову академічну оцінку студента на граничній межі найвищого балу ECTS, понижуючи оцінку до «B».
- **Priority (Пріоритет):** **P2**.  
  *Обґрунтування:* Підлягає обов'язковому виправленню у наступній версії модуля v2.

---

## 2. Тестове середовище (Environment)

- **Модуль бізнес-логіки:** `src/main/java/ua/kpi/tzyapz/eduportal` (v1.0.0)
- **JDK:** Eclipse Temurin 17.0.21
- **Середовище запуску:** Apache Maven 3.9.9, JUnit Jupiter 5.10.2
- **Команда відтворення:**  
  `mvn test -Dtest=GradeCalculatorTest#ectsLetterAtBoundary95MustBeA`

---

## 3. Кроки для відтворення (Steps to Reproduce)

1. Створити екземпляр `GradeCalculator calc = new GradeCalculator();`.
2. Викликати метод `String letter = calc.ectsLetter(95.00);`.
3. Перевірити отримане значення.

---

## 4. Результати

- **Очікуваний результат:** `"A"`.
- **Фактичний результат у v1:** `"B"`. Тест падає з помилкою `AssertionFailedError: expected: <A> but was: <B>`.
