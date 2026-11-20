package warmup;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MainTest {
    @Test
    void addWorks() {
        assertEquals(5, Main.add(2, 3));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void addZero(int n) {
        assertEquals(n, Main.add(n, 0));
    }
}
