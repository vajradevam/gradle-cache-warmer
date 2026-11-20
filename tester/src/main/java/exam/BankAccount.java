package exam;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {
    private final String owner;
    private double balance;
    private final List<String> history = new ArrayList<>();

    public BankAccount(String owner, double initial) {
        if (initial < 0) {
            throw new IllegalArgumentException("negative initial balance");
        }
        this.owner = owner;
        this.balance = initial;
    }

    public void deposit(double amt) {
        if (amt <= 0) throw new IllegalArgumentException("amount must be positive");
        balance += amt;
        history.add("D:" + amt);
    }

    public void withdraw(double amt) {
        if (amt > balance) throw new IllegalStateException("insufficient funds");
        balance -= amt;
        history.add("W:" + amt);
    }

    public String getOwner() { return owner; }
    public double getBalance() { return balance; }
    public List<String> getHistory() { return List.copyOf(history); }
}
