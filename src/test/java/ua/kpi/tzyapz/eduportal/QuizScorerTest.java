package ua.kpi.tzyapz.eduportal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юніт-тести для перевірки правил бізнес-логіки BL-40, BL-50, BL-60 (оцінювання тестів).
 *
 * Автори:
 * - BL-40, BL-50, BL-60: Денис Бучинський (Denchik, Scrum Master / Reviewer)
 * - Граничні випадки: Олександр Панайотов (5ani0chek, Quality Auditor)
 * Рецензент: Олександр Кираківський (Kyrakivskyi0, SDET)
 */
@Tag("unit")
@DisplayName("QuizScorer — правила оцінювання тестів (BL-40, BL-50, BL-60)")
public class QuizScorerTest {

    private QuizScorer scorer;

    @BeforeEach
    void setUp() {
        scorer = new QuizScorer();
    }

    // =========================================================================
    // BL-40: Питання з однією правильною відповіддю (Денис Бучинський)
    // =========================================================================

    @Test
    @DisplayName("BL-40: правильна відповідь на питання з 1 варіантом дає рівно 1.0 бал")
    void questionScoreSingleChoiceCorrectReturnsOne() {
        // Arrange
        QuizQuestion q = new QuizQuestion("q1", Set.of("A", "B", "C"), Set.of("B"));

        // Act
        double score = scorer.questionScore(q, Set.of("B"));

        // Assert
        assertEquals(1.0, score, 0.001);
    }

    @Test
    @DisplayName("BL-40: помилкова відповідь або відсутність відповіді дає 0.0 балів")
    void questionScoreSingleChoiceIncorrectOrEmptyReturnsZero() {
        QuizQuestion q = new QuizQuestion("q1", Set.of("A", "B", "C"), Set.of("B"));

        assertAll("BL-40 нульові бали",
                () -> assertEquals(0.0, scorer.questionScore(q, Set.of("A")), 0.001),
                () -> assertEquals(0.0, scorer.questionScore(q, Collections.emptySet()), 0.001),
                () -> assertEquals(0.0, scorer.questionScore(q, null), 0.001)
        );
    }

    // =========================================================================
    // BL-50: Питання з кількома правильними відповідями (Денис Бучинський)
    // =========================================================================

    @Test
    @DisplayName("BL-50: частка обраних правильних мінус частка обраних неправильних")
    void questionScoreMultipleChoicePartialCredit() {
        // Arrange: 4 варіанти (A, B, C, D), правильні: A, B. Неправильні: C, D.
        QuizQuestion q = new QuizQuestion("q2", Set.of("A", "B", "C", "D"), Set.of("A", "B"));

        // Студент обрав 1 правильний (A) і 0 неправильних:
        // correctShare = 1/2 = 0.5, wrongShare = 0/2 = 0.0 -> бал = 0.5
        double score1 = scorer.questionScore(q, Set.of("A"));
        assertEquals(0.5, score1, 0.001);

        // Студент обрав обидва правильні (A, B) і 0 неправильних -> бал = 1.0
        double scoreFull = scorer.questionScore(q, Set.of("A", "B"));
        assertEquals(1.0, scoreFull, 0.001);
    }

    /**
     * Закладений дефект модуля v1:
     * У QuizScorer рядок 42 реалізовано:
     *     return correctShare - wrongShare;
     * Якщо студент обрав 0 правильних і 1 неправильний,
     * correctShare = 0.0, wrongShare = 0.5 -> метод повертає -0.5.
     * Але за вимогою BL-50: «але не менше 0».
     * Тест падає на версії v1 (дефект MOD-BUG-003, GitHub Issue #13).
     */
    @Test
    @DisplayName("BL-50: бал за питання не може бути від'ємним (не менше 0.0)")
    void questionScoreMultipleChoiceMustNotBeNegative() {
        // BUG-MOD-05
        QuizQuestion q = new QuizQuestion("q2", Set.of("A", "B", "C", "D"), Set.of("A", "B"));

        // Студент обрав лише неправильний варіант C:
        double score = scorer.questionScore(q, Set.of("C"));

        // За BL-50: бал має бути обмежений знизу 0.0 (не -0.5)
        assertEquals(0.0, score, 0.001, "BL-50: бал не може опускатися нижче 0.0");
    }

    @Test
    @DisplayName("BL-50: вибір неіснуючого варіанта викликає IllegalArgumentException")
    void questionScoreUnknownOptionThrows() {
        QuizQuestion q = new QuizQuestion("q1", Set.of("A", "B"), Set.of("A"));
        assertThrows(IllegalArgumentException.class, () -> scorer.questionScore(q, Set.of("UNKNOWN_XYZ")));
    }

    // =========================================================================
    // BL-60: Підсумок спроби тесту (Денис Бучинський / Олександр Панайотов)
    // =========================================================================

    @Test
    @DisplayName("BL-60: підсумок спроби = сума балів / кількість питань × 100")
    void scoreAttemptStandardCalculation() {
        // Arrange: 2 питання, обидва відповіли правильно -> 100.0%
        QuizQuestion q1 = new QuizQuestion("q1", Set.of("A", "B"), Set.of("A"));
        QuizQuestion q2 = new QuizQuestion("q2", Set.of("A", "B"), Set.of("B"));
        List<QuizQuestion> key = List.of(q1, q2);

        Map<String, Set<String>> answers = Map.of(
                "q1", Set.of("A"),
                "q2", Set.of("B")
        );

        // Act
        double attemptScore = scorer.score(answers, key);

        // Assert
        assertEquals(100.0, attemptScore, 0.001);
    }

    /**
     * Закладений дефект модуля v1:
     * У QuizScorer рядок 63 реалізовано:
     *     return (double) Math.round(percent);
     * Метод округлює підсумок до цілих (0 знаків) замість 1 знака (HALF_UP):
     * При 1 правильному з 3 питань результат = 33.333...%
     * За вимогою BL-60: округлення до 1 знака -> має бути 33.3%.
     * Код v1 повертає 33.0.
     * Фіксується у трекері як MOD-BUG-004 (GitHub Issue #14).
     */
    @Test
    @DisplayName("BL-60: підсумок спроби округлюється до 1 десяткового знака (33.3%)")
    void scoreAttemptRoundsToOneDecimalPlace() {
        // BUG-MOD-06
        QuizQuestion q1 = new QuizQuestion("q1", Set.of("A", "B"), Set.of("A"));
        QuizQuestion q2 = new QuizQuestion("q2", Set.of("A", "B"), Set.of("A"));
        QuizQuestion q3 = new QuizQuestion("q3", Set.of("A", "B"), Set.of("A"));
        List<QuizQuestion> key = List.of(q1, q2, q3);

        // 1 з 3 питань правильне = 33.333% -> за BL-60 має бути 33.3
        Map<String, Set<String>> answers = Map.of("q1", Set.of("A"));

        double attemptScore = scorer.score(answers, key);

        assertEquals(33.3, attemptScore, 0.001, "BL-60: підсумок спроби округлюється до 1 знака (33.3%)");
    }

    @Test
    @DisplayName("BL-60: порожній ключ тесту викликає IllegalArgumentException")
    void scoreAttemptEmptyKeyThrows() {
        assertThrows(IllegalArgumentException.class, () -> scorer.score(Map.of(), Collections.emptyList()));
    }
}
