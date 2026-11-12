# Звіт про дефект модуля бізнес-логіки: BUG-MOD-02

**Ідентифікатор дефекту:** `BUG-MOD-02`  
**Назва:** Некоректний оператор нестрогого порівняння `<=` у методі `EnrollmentValidator.hasCapacity()`, що дозволяє самозарахування на повністю заповнений курс (порушення вимоги BL-70).  
**Компонент модуля:** `ua.kpi.tzyapz.eduportal.EnrollmentValidator` (версія модуля: `v1.0.0`)  
**Пов'язана вимога:** `BL-70` («умови самозарахування: зараховано менше за місткість»)  
**Тест-клас і метод, що фіксує дефект:**  
`ua.kpi.tzyapz.eduportal.EnrollmentValidatorTest#hasCapacityWhenCourseIsFullMustReturnFalse`  
**Автор звіту:** Олександр Кираківський (`Kyrakivskyi0`), **Інженер з тестування / SDET**  
**Рецензент звіту:** Денис Бучинський (`Denchik`), **Скрам-майстер / Рецензент**  
**Дата реєстрації:** 10 листопада 2026 року  
**Мітка в трекері:** `module`, `bl-70`, `bva-error`, `bug-v1`  

---

## 1. Рівні критичності та терміновості

- **Severity (Критичність):** **Major**.  
  *Обґрунтування:* Дефект веде до переповнення навчальних груп понад встановлену максимальну місткість (`capacity`), створюючи надмірне навантаження на інфраструктуру та порушуючи академічні санітарні норми кількості студентів на викладача.
- **Priority (Пріоритет):** **High**.  
  *Обґрунтування:* Необхідно виправити граничну перевірку у версії v2, оскільки цей метод є ключовим блокуючим фільтром підсистеми зарахування.

---

## 2. Тестове середовище (Environment)

- **Модуль бізнес-логіки:** `code/modules/eduportal/v1` (jar: `eduportal-module-1.0.0.jar`)
- **JDK:** Eclipse Temurin 17.0.11
- **Середовище запуску:** Apache Maven 3.9.6, JUnit Jupiter 5.10.2
- **Команда відтворення:**  
  `mvn test -Dtest=EnrollmentValidatorTest#hasCapacityWhenCourseIsFullMustReturnFalse`

---

## 3. Кроки для відтворення (Steps to Reproduce)

1. Створити об'єкт курсу з місткістю 25 осіб, на який вже зараховано рівно 25 осіб (вільних місць 0):
   ```java
   Course fullCourse = new Course("cs101", "key", 
       LocalDate.now(), LocalDate.now().plusDays(10), 25, 25, null);
   ```
2. Створити екземпляр валідатора `EnrollmentValidator validator = new EnrollmentValidator();`.
3. Викликати метод `boolean hasSeats = validator.hasCapacity(fullCourse);`.
4. Перевірити отримане логічне значення.

---

## 4. Результати

- **Очікуваний результат (за специфікацією BL-70):**  
  За правилом BL-70 умова наявності місць формулюється однозначно: *«зараховано менше за місткість»* (строга нерівність $enrolled < capacity$). Якщо $enrolled = 25$ і $capacity = 25$, вільних місць немає, метод повинен повернути `false`.
- **Фактичний результат (на коді модуля v1):**  
  Метод повертає `true`.  
  *Surefire Failure Trace:*  
  `org.opentest4j.AssertionFailedError: BL-70: якщо enrolled == capacity, вільних місць немає, очікується false ==> expected: <false> but was: <true>`

---

## 5. Першопричина дефекту в коді (Root Cause Analysis)

У вихідному файлі `src/main/java/ua/kpi/tzyapz/eduportal/EnrollmentValidator.java` на рядку 42–44 реалізовано:
```java
public boolean hasCapacity(Course course) {
    return course.enrolled() <= course.capacity();
}
```
**Аналіз:** Класична помилка граничного аналізу (Boundary / Off-by-one error): використано оператор `<=` замість `<`, що дозволяє зарахування ще одного (26-го) студента на курс із місткістю 25 осіб.

---

## 6. Рекомендоване виправлення для версії v2

```java
public boolean hasCapacity(Course course) {
    return course.enrolled() < course.capacity();
}
```
