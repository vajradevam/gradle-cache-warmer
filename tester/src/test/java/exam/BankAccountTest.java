package exam;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class BankAccountTest {
    BankAccount acc;

    @BeforeEach
    void setUp() {
        acc = new BankAccount("sam", 100);
    }

    @Test
    void depositAndWithdraw() {
        acc.deposit(50);
        acc.withdraw(30);
        assertEquals(120, acc.getBalance(), 1e-9);
        assertEquals(2, acc.getHistory().size());
    }

    @Test
    void overdraw() {
        assertThrows(IllegalStateException.class, () -> acc.withdraw(101));
    }

    @ParameterizedTest
    @CsvSource({"1,101", "10,110", "0.5,100.5"})
    void deposits(double amt, double expected) {
        acc.deposit(amt);
        assertEquals(expected, acc.getBalance(), 1e-9);
    }

    @Test
    void negativeInitial() {
        assertThrows(IllegalArgumentException.class, () -> new BankAccount("a", -1));
    }
}
