# Звіт про дефект модуля бізнес-логіки: BUG-MOD-01

**Ідентифікатор дефекту:** `BUG-MOD-01`  
**Назва:** Відсутність валідації кінцевої дати періоду зарахування (`enrolEnd`) у методі `EnrollmentValidator.withinPeriod()` (порушення вимоги BL-70).  
**Компонент модуля:** `ua.kpi.tzyapz.eduportal.EnrollmentValidator` (версія модуля: `v1.0.0`)  
**Пов'язана вимога:** `BL-70` («умови самозарахування: дата у межах періоду зарахування курсу включно з датами початку й кінця»)  
**Тест-клас і метод, що фіксує дефект:**  
`ua.kpi.tzyapz.eduportal.EnrollmentValidatorTest#withinPeriodDayAfterEndMustReturnFalse`  
**Автор звіту:** Олександр Кираківський (`Kyrakivskyi0`), **Інженер з тестування / SDET**  
**Рецензент звіту:** Денис Бучинський (`Denchik`), **Скрам-майстер / Рецензент**  
**Дата реєстрації:** 10 листопада 2026 року  
**Мітка в трекері:** `module`, `bl-70`, `bug-v1`  

---

## 1. Рівні критичності та терміновості

- **Severity (Критичність):** **Major**.  
  *Обґрунтування:* Дефект порушує фундаментальну логіку регламенту навчального процесу, дозволяючи студентам несанкціоновано реєструватися на курс після офіційного закриття набору (`today > enrolEnd`), що створює хаос в адмініструванні груп та плануванні викладацького навантаження.
- **Priority (Пріоритет):** **High**.  
  *Обґрунтування:* Потрібно виправити валідатор дат у версії v2 модуля до інтеграції компонента самозарахування з веб-інтерфейсом навчального порталу.

---

## 2. Тестове середовище (Environment)

- **Модуль бізнес-логіки:** `code/modules/eduportal/v1` (jar: `eduportal-module-1.0.0.jar`)
- **JDK:** Eclipse Temurin 17.0.11
- **Середовище запуску:** Apache Maven 3.9.6, JUnit Jupiter 5.10.2
- **Команда відтворення:**  
  `mvn test -Dtest=EnrollmentValidatorTest#withinPeriodDayAfterEndMustReturnFalse`

---

## 3. Кроки для відтворення (Steps to Reproduce)

1. Створити об'єкт курсу `Course` з періодом зарахування з `2026-09-01` по `2026-09-30`:
   ```java
   Course course = new Course("cs101", "edu2026", 
       LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30), 25, 15, "prog-basics");
   ```
2. Задати поточну дату тестування `today` як `2026-10-01` (точка $N+1$ — перший день після завершення періоду прийому).
3. Створити екземпляр валідатора `EnrollmentValidator validator = new EnrollmentValidator();`.
4. Викликати метод `boolean inPeriod = validator.withinPeriod(course, today);`.
5. Перевірити повернене логічне значення.

---

## 4. Результати

- **Очікуваний результат (за специфікацією BL-70):**  
  Оскільки `2026-10-01` пізніше за дату завершення `2026-09-30`, метод повинен повернути `false`. Зарахування поза межами встановленого терміну заборонено.
- **Фактичний результат (на коді модуля v1):**  
  Метод повертає `true`.  
  *Surefire Failure Trace:*  
  `org.opentest4j.AssertionFailedError: BL-70 вимагає блокування зарахування після завершення періоду enrolEnd ==> expected: <false> but was: <true>`

---

## 5. Першопричина дефекту в коді (Root Cause Analysis)

У вихідному файлі `src/main/java/ua/kpi/tzyapz/eduportal/EnrollmentValidator.java` на рядку 35–37 реалізовано:
```java
public boolean withinPeriod(Course course, LocalDate today) {
    return !today.isBefore(course.enrolStart());
}
```
**Аналіз:** Розробник перевірив лише умову, що дата не передує початку періоду (`!today.isBefore(enrolStart)`), але повністю забув додати перевірку кінцевої межі `!today.isAfter(course.enrolEnd())`.

---

## 6. Рекомендоване виправлення для версії v2

```java
public boolean withinPeriod(Course course, LocalDate today) {
    return !today.isBefore(course.enrolStart()) && !today.isAfter(course.enrolEnd());
}
```
