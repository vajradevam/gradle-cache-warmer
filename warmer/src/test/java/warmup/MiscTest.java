package warmup;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.io.TempDir;
import warmup.Shapes.*;

class MiscTest {
    @Nested
    class InventoryTests {
        Inventory inv;

        @BeforeEach
        void setUp() {
            inv = new Inventory();
            inv.add("apple", 5);
        }

        @Test
        void addAndCount() {
            inv.add("apple", 2);
            assertEquals(7, inv.count("apple"));
            assertEquals(7, inv.total());
        }

        @Test
        void removeTooMany() {
            IllegalStateException e = assertThrows(IllegalStateException.class, () -> inv.remove("apple", 6));
            assertTrue(e.getMessage().contains("apple"));
        }

        @Test
        void removeAll() {
            inv.remove("apple", 5);
            assertEquals(0, inv.total());
        }

        @Test
        void badQty() {
            assertThrows(IllegalArgumentException.class, () -> inv.add("x", 0));
        }
    }

    @Test
    void dates() {
        LocalDate b = LocalDate.of(2000, 2, 29);
        assertEquals(24, DateUtil.ageOn(b, LocalDate.of(2025, 2, 28)));
        assertEquals(25, DateUtil.ageOn(b, LocalDate.of(2025, 3, 1)));
        assertTrue(DateUtil.isWeekend(LocalDate.of(2025, 11, 22)));
        assertEquals("2025-11-22", DateUtil.iso(LocalDate.of(2025, 11, 22)));
        assertThrows(IllegalArgumentException.class, () -> DateUtil.ageOn(b, b.minusDays(1)));
    }

    @Test
    void shapes() {
        assertEquals(9.0, Shapes.area(new Square(3)), 1e-9);
        assertEquals(6.0, Shapes.area(new Rect(2, 3)), 1e-9);
        assertEquals(Math.PI, Shapes.area(new Circle(1)), 1e-9);
    }

    @Test
    void files(@TempDir Path dir) throws IOException {
        Path p = dir.resolve("a.txt");
        FileStore.save(p, List.of("x", "y", "z"));
        assertEquals(List.of("x", "y", "z"), FileStore.load(p));
    }

    @TestFactory
    Stream<DynamicTest> dynamic() {
        return Stream.of(1, 2, 3).map(n -> dynamicTest("square " + n, () -> assertEquals(n * n, Shapes.area(new Square(n)), 1e-9)));
    }

    @RepeatedTest(3)
    @Timeout(value = 2, unit = TimeUnit.SECONDS)
    @Tag("fast")
    void repeated() {
        assertEquals(4, 2 + 2);
    }
}
