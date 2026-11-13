package ua.kpi.tzyapz.eduportal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юніт-тести для перевірки правил бізнес-логіки BL-70 (умови самозарахування)
 * та BL-80 (фіксація кодів порушень) у модулі «ЕдуПортал».
 *
 * Автор: Інженер з тестування / SDET Олександр Кираківський (Kyrakivskyi0)
 * Рецензент: Скрам-майстер / Рецензент Денис Бучинський (Denchik)
 */
@Tag("unit")
@DisplayName("EnrollmentValidator — правила самозарахування (BL-70, BL-80)")
public class EnrollmentValidatorTest {

    private EnrollmentValidator validator;
    private Course baseCourse;
    private Student qualifiedStudent;

    @BeforeEach
    void setUp() {
        validator = new EnrollmentValidator();
        // Базовий тестовий курс: період 2026-09-01 .. 2026-09-30, місткість 25, зараховано 15, передумова "prog-basics"
        baseCourse = new Course(
                "cs101",
                "edu2026",
                LocalDate.of(2026, 9, 1),
                LocalDate.of(2026, 9, 30),
                25,
                15,
                "prog-basics"
        );
        // Студент, який успішно здав базовий курс на 85 балів
        qualifiedStudent = new Student("stud-01", Map.of("prog-basics", 85.0));
    }

    // =========================================================================
    // 1. BL-70: Перевірка кодового слова (keyMatches)
    // =========================================================================

    @Test
    @DisplayName("BL-70: точний збіг кодового слова повертає true")
    void keyMatchesExactValueReturnsTrue() {
        // Arrange
        String inputKey = "edu2026";

        // Act
        boolean matches = validator.keyMatches(baseCourse, inputKey);

        // Assert
        assertTrue(matches, "Очікувався успішний збіг однакових кодових слів");
    }

    @ParameterizedTest(name = "BL-70: невірний ключ \"{0}\" повертає false (урахування регістру та змісту)")
    @ValueSource(strings = {"EDU2026", "Edu2026", "edu 2026", "wrong_key", ""})
    void keyMatchesCaseSensitiveAndIncorrectKeysReturnFalse(String candidateKey) {
        // Arrange & Act & Assert
        assertFalse(validator.keyMatches(baseCourse, candidateKey),
                "Ключ з іншим регістром або текстом повинен бути відхилений");
    }

    // =========================================================================
    // 2. BL-70: Перевірка періоду зарахування (withinPeriod) — BVA межі дат
    // =========================================================================

    @ParameterizedTest(name = "BL-70: дата {0} у межах періоду (включно) повертає true")
    @CsvSource({
            "2026-09-01", // Межа: перший день періоду зарахування (enrolStart)
            "2026-09-15", // Всередині діапазону
            "2026-09-30"  // Межа: останній день періоду зарахування (enrolEnd)
    })
    void withinPeriodValidDatesReturnTrue(String dateStr) {
        // Arrange
        LocalDate today = LocalDate.parse(dateStr);

        // Act
        boolean inPeriod = validator.withinPeriod(baseCourse, today);

        // Assert
        assertTrue(inPeriod, "Дата у межах періоду зарахування повинна бути валідною");
    }

    @Test
    @DisplayName("BL-70: дата за 1 день до початку періоду зарахування (2026-08-31) повертає false")
    void withinPeriodDayBeforeStartReturnsFalse() {
        // Arrange: точка N - 1 від дати початку 2026-09-01
        LocalDate dayBefore = LocalDate.of(2026, 8, 31);

        // Act
        boolean inPeriod = validator.withinPeriod(baseCourse, dayBefore);

        // Assert
        assertFalse(inPeriod, "Дата до початку періоду не дозволена для зарахування");
    }

    /**
     * Закладений дефект модуля v1:
     * У класі EnrollmentValidator рядок 36 реалізовано:
     *     return !today.isBefore(course.enrolStart());
     * Відсутня перевірка верхньої межі `!today.isAfter(course.enrolEnd())`.
     * За вимогою BL-70 дата 2026-10-01 (день після завершення) МАЄ повертати false.
     * На коді v1 цей тест падає (повертає true замість false).
     * Тест зафіксовано як дефект модуля MOD-BUG-005 (GitHub Issue #15) згідно з розділом 4.9 методички.
     */
    @Test
    @DisplayName("BL-70: дата за 1 день після завершення періоду (2026-10-01) повинна повертати false")
    void withinPeriodDayAfterEndMustReturnFalse() {
        // BUG-MOD-01
        // Arrange: точка N + 1 від дати кінця 2026-09-30
        LocalDate dayAfter = LocalDate.of(2026, 10, 1);

        // Act
        boolean inPeriod = validator.withinPeriod(baseCourse, dayAfter);

        // Assert (вимога BL-70: дата суворо у межах періоду включно, після завершення — false)
        assertFalse(inPeriod, "BL-70 вимагає блокування зарахування після завершення періоду enrolEnd");
    }

    // =========================================================================
    // 3. BL-70: Перевірка наявності вільних місць (hasCapacity) — BVA
    // =========================================================================

    @ParameterizedTest(name = "BL-70: вільні місця є (місткість 25, зараховано {0}) → true")
    @ValueSource(ints = {0, 1, 10, 24})
    void hasCapacityWhenFreeSeatsExistReturnsTrue(int enrolled) {
        // Arrange
        Course course = new Course("c1", "k", LocalDate.now(), LocalDate.now().plusDays(10), 25, enrolled, null);

        // Act
        boolean hasSeats = validator.hasCapacity(course);

        // Assert
        assertTrue(hasSeats, "Якщо кількість зарахованих менша за місткість, вільні місця є");
    }

    /**
     * Закладений дефект модуля v1:
     * У класі EnrollmentValidator рядок 43 реалізовано:
     *     return course.enrolled() <= course.capacity();
     * Нестрога нерівність `<=` дозволяє зарахування, коли кількість студентів РІВНА місткості (25 з 25).
     * За вимогою BL-70: «зараховано менше за місткість» (enrolled < capacity).
     * Якщо курс заповнений (25 з 25), вільних місць немає і hasCapacity МАЄ повертати false.
     * На коді v1 тест падає (повертає true).
     * Фіксується у трекері дефектів як MOD-BUG-006 (GitHub Issue #16).
     */
    @Test
    @DisplayName("BL-70: коли курс заповнений повністю (enrolled == capacity == 25), hasCapacity має повертати false")
    void hasCapacityWhenCourseIsFullMustReturnFalse() {
        // BUG-MOD-02
        // Arrange: курс із місткістю 25, на який зараховано рівно 25 студентів (вільних місць 0)
        Course fullCourse = new Course("c1", "k", LocalDate.now(), LocalDate.now().plusDays(10), 25, 25, null);

        // Act
        boolean hasSeats = validator.hasCapacity(fullCourse);

        // Assert (вимога BL-70: менше за місткість, отже при рівності — місць немає)
        assertFalse(hasSeats, "BL-70: якщо enrolled == capacity, вільних місць немає, очікується false");
    }

    @Test
    @DisplayName("BL-70: якщо зараховано більше за місткість (переповнення 26 з 25) → false")
    void hasCapacityWhenOverfilledReturnsFalse() {
        // Arrange
        Course overfilled = new Course("c1", "k", LocalDate.now(), LocalDate.now().plusDays(10), 25, 26, null);

        // Act & Assert
        assertFalse(validator.hasCapacity(overfilled), "При перевищенні місткості місць немає");
    }

    // =========================================================================
    // 4. BL-70: Перевірка передумовного курсу (prerequisiteMet) — BVA
    // =========================================================================

    @Test
    @DisplayName("BL-70: курс без передумови (prerequisiteId == null) доступний будь-якому студенту")
    void prerequisiteMetWhenNoPrerequisiteRequiredReturnsTrue() {
        // Arrange
        Course courseNoPrereq = new Course("c0", "k", LocalDate.now(), LocalDate.now().plusDays(5), 30, 0, null);
        Student studentNoCourses = new Student("s_new", Collections.emptyMap());

        // Act
        boolean passed = validator.prerequisiteMet(studentNoCourses, courseNoPrereq);

        // Assert
        assertTrue(passed, "Якщо передумова відсутня, умова завжди виконана");
    }

    @ParameterizedTest(name = "BL-70: бал за передумову {0} (поріг 60.0) → результат {1}")
    @CsvSource({
            "59.99, false", // Точка N - 1 (не склав)
            "60.00, true",  // Точка N (склав на мінімальний прохідний)
            "60.01, true",  // Точка N + 1 (склав)
            "95.00, true"   // Впевнено склав
    })
    void prerequisiteMetBoundariesAroundSixty(double score, boolean expected) {
        // Arrange
        Student student = new Student("stud", Map.of("prog-basics", score));

        // Act
        boolean passed = validator.prerequisiteMet(student, baseCourse);

        // Assert
        assertEquals(expected, passed, "Перевірка граничних значень прохідного балу передумови (60.0)");
    }

    @Test
    @DisplayName("BL-70: студент не вивчав передумовний курс (оцінка відсутня у базі) → false")
    void prerequisiteMetWhenCourseNotAttemptedReturnsFalse() {
        // Arrange: у студента є оцінки за інші курси, але не за "prog-basics"
        Student studentOtherCourses = new Student("stud", Map.of("math-analysis", 90.0));

        // Act
        boolean passed = validator.prerequisiteMet(studentOtherCourses, baseCourse);

        // Assert
        assertFalse(passed, "Студент без оцінки за передумовний курс не може зарахуватися");
    }

    // =========================================================================
    // 5. BL-70: Підсумкове рішення самозарахування (canEnroll)
    // =========================================================================

    @Test
    @DisplayName("BL-70: усі 4 умови виконано одночасно → зарахування дозволено (canEnroll == true)")
    void canEnrollWhenAllFourConditionsMetReturnsTrue() {
        // Arrange
        LocalDate today = LocalDate.of(2026, 9, 10);
        String correctKey = "edu2026";

        // Act
        boolean allowed = validator.canEnroll(qualifiedStudent, baseCourse, today, correctKey);

        // Assert
        assertTrue(allowed, "Зарахування повинно бути успішним при виконанні всіх 4 умов");
    }

    @Test
    @DisplayName("BL-70: невірний ключ при виконанні інших 3 умов → зарахування заборонено")
    void canEnrollFailsWhenKeyIsWrong() {
        // Arrange
        LocalDate today = LocalDate.of(2026, 9, 10);

        // Act
        boolean allowed = validator.canEnroll(qualifiedStudent, baseCourse, today, "wrong_key");

        // Assert
        assertFalse(allowed, "Зарахування повинно бути заблоковано через невірний ключ");
    }

    @Test
    @DisplayName("BL-70: невиконання передумови при виконанні інших 3 умов → зарахування заборонено")
    void canEnrollFailsWhenPrerequisiteNotMet() {
        // Arrange: студент отримав лише 55 балів за "prog-basics"
        Student weakStudent = new Student("s_weak", Map.of("prog-basics", 55.0));
        LocalDate today = LocalDate.of(2026, 9, 10);

        // Act
        boolean allowed = validator.canEnroll(weakStudent, baseCourse, today, "edu2026");

        // Assert
        assertFalse(allowed, "Студент з оцінкою нижче 60 балів не має права на самозарахування");
    }

    // =========================================================================
    // 6. BL-80: Порядок та повнота кодів порушень (violations)
    // =========================================================================

    @Test
    @DisplayName("BL-80: при успішному зарахуванні violations повертає порожній список")
    void violationsReturnsEmptyListWhenNoViolations() {
        // Arrange
        LocalDate today = LocalDate.of(2026, 9, 10);

        // Act
        List<String> list = validator.violations(qualifiedStudent, baseCourse, today, "edu2026");

        // Assert
        assertNotNull(list);
        assertTrue(list.isEmpty(), "Для валідних даних список порушень має бути порожнім");
    }

    @Test
    @DisplayName("BL-80: фіксація множинних порушень у сталому порядку: KEY → PERIOD → CAPACITY → PREREQUISITE")
    void violationsReturnsAllCodesInExactSpecificationOrder() {
        // Arrange: порушено ВСІ 4 умови:
        // 1) невірний ключ ("bad_key")
        // 2) дата до початку періоду (2026-08-15)
        // 3) курс переповнений (26 з 25)
        // 4) передумова не здана (40 балів)
        Course invalidCourse = new Course(
                "cs999",
                "secret",
                LocalDate.of(2026, 9, 1),
                LocalDate.of(2026, 9, 30),
                25,
                26,
                "prog-basics"
        );
        Student failingStudent = new Student("s_fail", Map.of("prog-basics", 40.0));
        LocalDate earlyDate = LocalDate.of(2026, 8, 15);

        // Act
        List<String> violations = validator.violations(failingStudent, invalidCourse, earlyDate, "bad_key");

        // Assert (м'яка перевірка assertAll для перевірки списку порушень)
        assertAll("Коди порушень BL-80",
                () -> assertEquals(4, violations.size(), "Має бути зафіксовано рівно 4 порушення"),
                () -> assertEquals(EnrollmentValidator.KEY, violations.get(0), "Першим має бути KEY"),
                () -> assertEquals(EnrollmentValidator.PERIOD, violations.get(1), "Другим має бути PERIOD"),
                () -> assertEquals(EnrollmentValidator.CAPACITY, violations.get(2), "Третім має бути CAPACITY"),
                () -> assertEquals(EnrollmentValidator.PREREQUISITE, violations.get(3), "Четвертим має бути PREREQUISITE")
        );
    }

    // =========================================================================
    // 7. Валідація null-аргументів (assertThrows)
    // =========================================================================

    @ParameterizedTest(name = "BL-80: null-параметри викликають IllegalArgumentException ({0})")
    @ValueSource(strings = {"nullStudent", "nullCourse", "nullDate", "nullKey"})
    void violationsWithNullArgumentsThrowsException(String nullCase) {
        // Arrange
        LocalDate today = LocalDate.of(2026, 9, 10);
        Student s = "nullStudent".equals(nullCase) ? null : qualifiedStudent;
        Course c = "nullCourse".equals(nullCase) ? null : baseCourse;
        LocalDate d = "nullDate".equals(nullCase) ? null : today;
        String k = "nullKey".equals(nullCase) ? null : "edu2026";

        // Act & Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> validator.violations(s, c, d, k),
                "Будь-який null-параметр повинен викликати IllegalArgumentException"
        );
        assertNotNull(ex.getMessage(), "Повідомлення винятку не повинно бути порожнім");
    }
}
