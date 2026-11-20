package warmup;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class TextStatsTest {
    @Test
    @DisplayName("word frequencies are case-insensitive")
    void frequencies() {
        Map<String, Long> f = TextStats.wordFrequencies("The cat, the DOG; the end");
        assertAll(
                () -> assertEquals(3L, f.get("the")),
                () -> assertEquals(1L, f.get("cat")),
                () -> assertEquals(4, f.size()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"racecar", "A man, a plan, a canal: Panama", "", "x"})
    void palindromes(String s) {
        assertTrue(TextStats.isPalindrome(s));
    }

    @ParameterizedTest
    @CsvSource({"hello,false", "abca,false", "noon,true"})
    void palindromeCsv(String s, boolean expected) {
        assertEquals(expected, TextStats.isPalindrome(s));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void longestWordEmpty(String s) {
        if (s == null) {
            assertThrows(NullPointerException.class, () -> TextStats.longestWord(s));
        } else {
            assertTrue(TextStats.longestWord(s).isEmpty());
        }
    }

    static Stream<Arguments> longest() {
        return Stream.of(
                Arguments.of("a bb ccc", "ccc"),
                Arguments.of("one two three", "three"),
                Arguments.of("tie ant bat", "tie"));
    }

    @ParameterizedTest
    @MethodSource("longest")
    void longestWord(String text, String expected) {
        assertEquals(expected, TextStats.longestWord(text).orElseThrow());
    }

    @Test
    void chunksUsesGuava() {
        assertEquals(
                List.of(List.of("a", "b"), List.of("c")),
                TextStats.chunks(List.of("a", "b", "c"), 2));
    }
}
