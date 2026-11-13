# Звіт про дефект модуля бізнес-логіки: MOD-BUG-002

**Ідентифікатор дефекту:** `MOD-BUG-002`  
**GitHub Issue:** [#12](https://github.com/Sasha26062007/EDUPORTAL/issues/12)  
**Назва:** Розрахунок штрафу за запізнення за формулою складного відсотка замість лінійного віднімання 10% на добу у методі `GradeCalculator.latePenalty()` (порушення вимоги BL-30).  
**Компонент модуля:** `ua.kpi.tzyapz.eduportal.GradeCalculator` (версія модуля: `v1.0.0`)  
**Пов'язана вимога:** `BL-30` («Штраф за запізнення робіт: 10% на добу, максимум 50%»)  
**Тест-клас і метод, що фіксує дефект:**  
`ua.kpi.tzyapz.eduportal.GradeCalculatorTest#latePenaltyTwoDaysLinearDiscountMustBeEighty`  
**Автор звіту:** Олександр Пархоменко (`Parkhomenko0`), **Тест-аналітик**  
**Рецензент звіту:** Денис Бучинський (`Denchik`), **Скрам-майстер / Рецензент**  
**Дата реєстрації:** 13 листопада 2026 року  
**Мітки в трекері:** `module`, `severity:major`, `priority:P2`, `status:open`  

---

## 1. Рівні критичності та терміновості

- **Severity (Критичність):** **Major**.  
  *Обґрунтування:* Дефект порушує регламент лінійного розрахунку академічних штрафів, завищуючи бал при несвоєчасній здачі завдань.
- **Priority (Пріоритет):** **P2**.  
  *Обґрунтування:* Потребує усунення у релізі v2 до розрахунку семестрових балів.

---

## 2. Тестове середовище (Environment)

- **Модуль бізнес-логіки:** `src/main/java/ua/kpi/tzyapz/eduportal` (v1.0.0)
- **JDK:** Eclipse Temurin 17.0.21
- **Середовище запуску:** Apache Maven 3.9.9, JUnit Jupiter 5.10.2
- **Команда відтворення:**  
  `mvn test -Dtest=GradeCalculatorTest#latePenaltyTwoDaysLinearDiscountMustBeEighty`

---

## 3. Кроки для відтворення (Steps to Reproduce)

1. Створити екземпляр `GradeCalculator calc = new GradeCalculator();`.
2. Викликати метод `double score = calc.latePenalty(100.0, 2);`.
3. Перевірити повернений результат.

---

## 4. Результати

- **Очікуваний результат:** `80.00` балів ($100 \times (1 - 0.20)$).
- **Фактичний результат у v1:** `81.00` бал ($100 \times 0.9^2$). Тест падає з `AssertionFailedError: expected: <80.0> but was: <81.0>`.
