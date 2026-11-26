package library;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LibraryManagerTest {
    @TempDir Path dir;

    @Test void loanPeriodsArePolymorphic() {
        LibraryItem[] items = {
            new Book("B", "t", "p", 2000, "a", "i"),
            new Magazine("M", "t", "p", 2000, 1, "May"),
            new Journal("J", "t", "p", 2000, 1, "CS")};
        assertArrayEquals(new int[]{14, 7, 3},
                new int[]{items[0].getLoanPeriod(), items[1].getLoanPeriod(), items[2].getLoanPeriod()});
    }

    @Test void overdueDaysNeverNegativeAndFineIsFivePerDay() {
        IssueTransaction tx = new IssueTransaction("T1", "M", "B",
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 15));
        tx.markReturned(LocalDate.of(2026, 1, 10));
        assertEquals(0, tx.getOverdueDays());
        assertEquals(0.0, tx.calculateFine());
        IssueTransaction late = new IssueTransaction("T2", "M", "B",
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 15));
        late.markReturned(LocalDate.of(2026, 1, 19));
        assertEquals(20.0, late.calculateFine());
    }

    @Test void duplicatesAndNullsRejected() {
        LibraryManager m = new LibraryManager(dir, Clock.systemDefaultZone());
        assertFalse(m.addItem(null));
        assertTrue(m.addItem(new Book("B1", "t", "p", 2000, "a", "i")));
        assertFalse(m.addItem(new Book("b1", "t2", "p", 2000, "a", "i")));
        assertTrue(m.addMember(new Member("M1", "n", "e", "c")));
        assertFalse(m.addMember(new Member("M1", "n2", "e", "c")));
    }

    @Test void issuedItemCannotBeRemoved() {
        LibraryManager m = new LibraryManager(dir, Clock.systemDefaultZone());
        m.addItem(new Book("B1", "t", "p", 2000, "a", "i"));
        m.addMember(new Member("M1", "n", "e", "c"));
        assertNotNull(m.issueItem("M1", "B1"));
        assertFalse(m.removeItem("B1"));
        assertEquals(-1, m.returnItem("M2", "B1"));
        m.returnItem("M1", "B1");
        assertTrue(m.removeItem("B1"));
        assertFalse(m.removeItem("B1"));
    }

    @Test void loadWithNoFilesGivesEmptyCollections() throws Exception {
        LibraryManager m = new LibraryManager(dir, Clock.systemDefaultZone());
        m.loadAll();
        assertTrue(m.getItems().isEmpty());
        assertTrue(m.getTransactions().isEmpty());
    }
}
