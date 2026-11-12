package ua.kpi.tzyapz.eduportal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юніт-тести для перевірки правил бізнес-логіки BL-10, BL-20, BL-30 (розрахунок оцінок та штрафів).
 *
 * Автори:
 * - BL-10: Володимир Костенко (Vov4ik, Test Lead)
 * - BL-20, BL-30: Олександр Пархоменко (Parkhomenko0, Test Analyst)
 * Рецензент: Денис Бучинський (Denchik, Scrum Master)
 */
@Tag("unit")
@DisplayName("GradeCalculator — правила розрахунку оцінок (BL-10, BL-20, BL-30)")
public class GradeCalculatorTest {

    private GradeCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new GradeCalculator();
    }

    // =========================================================================
    // BL-10: Зважений підсумок курсу (Володимир Костенко)
    // =========================================================================

    @Test
    @DisplayName("BL-10: зважений підсумок 80×40% + 90×60% = 86.00")
    void weightedTotalTwoStandardItems() {
        // Arrange
        List<GradeItem> items = List.of(
                new GradeItem("Лабораторні роботи", 80.0, 40.0),
                new GradeItem("Фінальний іспит", 90.0, 60.0)
        );

        // Act
        double total = calculator.weightedTotal(items);

        // Assert
        assertEquals(86.00, total, 0.001, "Зважений підсумок має дорівнювати 86.00");
    }

    @Test
    @DisplayName("BL-10: округлення підсумку до 2 знаків після коми за правилом HALF_UP")
    void weightedTotalRoundsHalfUp() {
        // Arrange: 73.333 * 30% + 84.444 * 70% = 21.9999 + 59.1108 = 81.1107 -> 81.11
        List<GradeItem> items = List.of(
                new GradeItem("Модуль 1", 73.333, 30.0),
                new GradeItem("Модуль 2", 84.444, 70.0)
        );

        // Act
        double total = calculator.weightedTotal(items);

        // Assert
        assertEquals(81.11, total, 0.001);
    }

    @Test
    @DisplayName("BL-10: сума ваг елементів не дорівнює 100 → IllegalArgumentException")
    void weightedTotalThrowsWhenWeightsDoNotSumToHundred() {
        // Arrange: сума ваг 40 + 50 = 90 != 100
        List<GradeItem> items = List.of(
                new GradeItem("Частина 1", 80.0, 40.0),
                new GradeItem("Частина 2", 70.0, 50.0)
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> calculator.weightedTotal(items));
    }

    @Test
    @DisplayName("BL-10: бал елемента виходить за межі 0..100 → IllegalArgumentException")
    void weightedTotalThrowsWhenScoreOutOfBounds() {
        // Arrange: бал 105.0 > 100
        List<GradeItem> items = List.of(
                new GradeItem("Екстра-бал", 105.0, 100.0)
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> calculator.weightedTotal(items));
    }

    @Test
    @DisplayName("BL-10: порожній список елементів → IllegalArgumentException")
    void weightedTotalThrowsWhenListIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> calculator.weightedTotal(Collections.emptyList()));
    }

    // =========================================================================
    // BL-20: Шкала ECTS (Олександр Пархоменко)
    // =========================================================================

    @ParameterizedTest(name = "BL-20: бал {0} відповідає літері ECTS {1}")
    @CsvSource({
            "96.0, A",
            "94.99, B",
            "85.0, B",
            "84.99, C",
            "75.0, C",
            "74.99, D",
            "65.0, D",
            "64.99, E",
            "60.0, E",
            "59.99, Fx",
            "35.0, Fx",
            "34.99, F",
            "0.0, F"
    })
    void ectsLetterStandardBoundaries(double score, String expectedLetter) {
        assertEquals(expectedLetter, calculator.ectsLetter(score));
    }

    /**
     * Закладений дефект модуля v1:
     * У GradeCalculator рядок 57 реалізовано:
     *     if (total > 95) return "A";
     * За вимогою BL-20: поріг A становить ">= 95" (від 95 включно).
     * Для значення 95.0 код повертає "B" замість "A".
     * Фіксується у трекері як BUG-MOD-03.
     */
    @Test
    @DisplayName("BL-20: для граничного значення 95.0 повинна повертатися літера A")
    void ectsLetterAtBoundary95MustBeA() {
        // BUG-MOD-03
        assertEquals("A", calculator.ectsLetter(95.0),
                "BL-20: поріг літери A становить >= 95.0 включно");
    }

    @Test
    @DisplayName("BL-20: бал поза межами 0..100 (-1 або 101) викликає IllegalArgumentException")
    void ectsLetterThrowsWhenOutOfRange() {
        assertAll("BL-20 межі валідації",
                () -> assertThrows(IllegalArgumentException.class, () -> calculator.ectsLetter(-0.1)),
                () -> assertThrows(IllegalArgumentException.class, () -> calculator.ectsLetter(100.1))
        );
    }

    // =========================================================================
    // BL-30: Штраф за запізнення (Олександр Пархоменко)
    // =========================================================================

    @Test
    @DisplayName("BL-30: 0 днів запізнення (вчасно) — бал без змін (100.0 → 100.00)")
    void latePenaltyZeroDaysReturnsFullScore() {
        assertEquals(100.00, calculator.latePenalty(100.0, 0), 0.001);
    }

    @Test
    @DisplayName("BL-30: 1 день запізнення — штраф 10% (100.0 → 90.00)")
    void latePenaltyOneDaySubtractsTenPercent() {
        assertEquals(90.00, calculator.latePenalty(100.0, 1), 0.001);
    }

    /**
     * Закладений дефект модуля v1:
     * У GradeCalculator рядок 94 реалізовано:
     *     score * Math.pow(0.9, cappedDays)
     * Розрахунок ведеться за складною відсотковою ставкою (0.9^days) замість лінійної:
     * За BL-30: "-10 % від балу за кожен день, максимум -50 %"
     * Тобто для 2 днів запізнення штраф має бути 20% (бал = 80.00).
     * Код обчислює 100 * 0.9^2 = 81.00.
     * Фіксується у трекері як BUG-MOD-04.
     */
    @Test
    @DisplayName("BL-30: 2 дні запізнення — лінійний штраф 20% (100.0 → 80.00)")
    void latePenaltyTwoDaysLinearDiscountMustBeEighty() {
        // BUG-MOD-04
        assertEquals(80.00, calculator.latePenalty(100.0, 2), 0.001,
                "BL-30 вимагає -10% за кожен день (лінійно), очікується 80.00");
    }

    @Test
    @DisplayName("BL-30: від'ємна кількість днів запізнення викликає IllegalArgumentException")
    void latePenaltyNegativeDaysThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> calculator.latePenalty(80.0, -1));
    }
}
