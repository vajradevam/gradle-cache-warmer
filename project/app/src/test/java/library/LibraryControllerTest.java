package library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Functional tests TC01-TC16, driven through the LibraryController interface. */
class LibraryControllerTest {

    /** A clock whose date the tests can move forward. */
    static class TestClock extends Clock {
        private LocalDate today = LocalDate.of(2026, 1, 1);
        void advanceDays(int days) { today = today.plusDays(days); }
        LocalDate today() { return today; }
        @Override public ZoneId getZone() { return ZoneOffset.UTC; }
        @Override public Clock withZone(ZoneId zone) { return this; }
        @Override public Instant instant() { return today.atStartOfDay(ZoneOffset.UTC).toInstant(); }
    }

    @TempDir Path dir;
    TestClock clock;
    LibraryManager manager;
    LibraryController controller;

    @BeforeEach
    void setUp() {
        clock = new TestClock();
        manager = new LibraryManager(dir, clock);
        manager.addMember(new Member("M1", "Asha", "asha@example.com", "1"));
        manager.addMember(new Member("M2", "Ravi", "ravi@example.com", "2"));
        controller = new LibraryManagerAdapter(manager);
    }

    private void addFour() throws Exception {
        controller.addItem("Book", "B1", "Java Basics", "Gosling");
        controller.addItem("Book", "B2", "Clean Code", "Martin");
        controller.addItem("Magazine", "M1", "Science Today", "Editors");
        controller.addItem("Journal", "J1", "Computing Journal", "ACM");
    }

    private String txnOf(String itemId) throws Exception {
        return controller.getActiveTransactions().stream()
                .filter(r -> r[2].equals(itemId)).findFirst().orElseThrow()[0];
    }

    @Test void tc01_addBook() throws Exception {
        controller.addItem("Book", "B1", "Java Basics", "Gosling");
        List<String[]> rows = controller.getAllItems();
        assertEquals(1, rows.size());
        assertArrayEquals(new String[]{"B1", "Book", "Java Basics", "Gosling", "Available"}, rows.get(0));
    }

    @Test void tc02_addMagazine() throws Exception {
        controller.addItem("Magazine", "M1", "Science Today", "Editors");
        assertEquals("Magazine", controller.getAllItems().get(0)[1]);
    }

    @Test void tc03_addJournal() throws Exception {
        controller.addItem("Journal", "J1", "Computing Journal", "ACM");
        assertEquals("Journal", controller.getAllItems().get(0)[1]);
    }

    @Test void tc04_duplicateIdRejected() throws Exception {
        controller.addItem("Book", "B1", "Java Basics", "Gosling");
        assertThrows(IllegalArgumentException.class,
                () -> controller.addItem("Magazine", "B1", "Other", "X"));
        assertEquals(1, controller.getAllItems().size());
    }

    @Test void tc05_searchById() throws Exception {
        addFour();
        List<String[]> rows = controller.searchItems("J1");
        assertEquals(1, rows.size());
        assertEquals("Computing Journal", rows.get(0)[2]);
    }

    @Test void tc06_searchByTitle() throws Exception {
        addFour();
        List<String[]> rows = controller.searchItems("java");
        assertEquals(1, rows.size());
        assertEquals("B1", rows.get(0)[0]);
        assertTrue(controller.searchItems("nothing-matches").isEmpty());
    }

    @Test void tc07_issueAvailableBook() throws Exception {
        addFour();
        controller.issueItem("M1", "B1");
        assertEquals(1, controller.getActiveTransactions().size());
        assertEquals("Issued", controller.searchItems("B1").get(0)[4]);
    }

    @Test void tc08_issueAlreadyIssuedRejected() throws Exception {
        addFour();
        controller.issueItem("M1", "B1");
        assertThrows(IllegalArgumentException.class, () -> controller.issueItem("M2", "B1"));
        assertEquals(1, controller.getActiveTransactions().size());
    }

    @Test void tc09_invalidMemberRejected() throws Exception {
        addFour();
        assertThrows(IllegalArgumentException.class, () -> controller.issueItem("NOPE", "B1"));
        assertTrue(controller.getActiveTransactions().isEmpty());
    }

    @Test void tc10_threeLoansSucceed() throws Exception {
        addFour();
        controller.issueItem("M1", "B1");
        controller.issueItem("M1", "B2");
        controller.issueItem("M1", "M1");
        assertEquals(3, controller.getActiveTransactions().size());
    }

    @Test void tc11_fourthLoanRejected() throws Exception {
        addFour();
        controller.issueItem("M1", "B1");
        controller.issueItem("M1", "B2");
        controller.issueItem("M1", "M1");
        assertThrows(IllegalArgumentException.class, () -> controller.issueItem("M1", "J1"));
        assertEquals(3, controller.getActiveTransactions().size());
    }

    @Test void tc12_returnMakesAvailable() throws Exception {
        addFour();
        controller.issueItem("M1", "B1");
        controller.returnItem(txnOf("B1"));
        assertEquals("Available", controller.searchItems("B1").get(0)[4]);
        assertTrue(controller.getActiveTransactions().isEmpty());
    }

    @Test void tc13_returnTwiceRejected() throws Exception {
        addFour();
        controller.issueItem("M1", "B1");
        String txn = txnOf("B1");
        controller.returnItem(txn);
        assertThrows(IllegalArgumentException.class, () -> controller.returnItem(txn));
        assertThrows(IllegalArgumentException.class, () -> controller.returnItem("T999"));
    }

    @Test void tc14_fineIsFiveRupeesPerOverdueDay() throws Exception {
        addFour();
        controller.issueItem("M1", "B1");           // Book: due in 14 days
        clock.advanceDays(14 + 4);                  // returned 4 days late
        assertEquals(20.0, manager.returnItem("M1", "B1"));

        controller.issueItem("M1", "B2");
        clock.advanceDays(14);                      // returned exactly on the due date
        assertEquals(0.0, manager.returnItem("M1", "B2"));

        controller.issueItem("M1", "J1");           // Journal: due in 3 days
        clock.advanceDays(3 + 2);
        assertEquals(10.0, manager.returnItem("M1", "J1"));
    }

    @Test void tc15_restartRestoresSavedRecords() throws Exception {
        addFour();
        controller.issueItem("M1", "B1");

        LibraryManager restarted = new LibraryManager(dir, clock);
        restarted.loadAll();
        LibraryController after = new LibraryManagerAdapter(restarted);

        assertEquals(4, after.getAllItems().size());
        assertEquals(1, after.getActiveTransactions().size());
        assertEquals("Issued", after.searchItems("B1").get(0)[4]);
        assertNotNull(restarted.findMember("M2"));
    }

    @Test void tc16_loanPeriodsPerType() throws Exception {
        addFour();
        controller.issueItem("M1", "B1");
        controller.issueItem("M1", "M1");
        controller.issueItem("M1", "J1");
        LocalDate today = clock.today();
        assertEquals(today.plusDays(14).toString(), dueDateOf("B1"));
        assertEquals(today.plusDays(7).toString(), dueDateOf("M1"));
        assertEquals(today.plusDays(3).toString(), dueDateOf("J1"));
    }

    private String dueDateOf(String itemId) throws Exception {
        return controller.getActiveTransactions().stream()
                .filter(r -> r[2].equals(itemId)).findFirst().orElseThrow()[3];
    }
}
