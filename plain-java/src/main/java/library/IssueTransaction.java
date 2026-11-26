package library;

import java.io.Serializable;
import java.time.LocalDate;

public class IssueTransaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private String transactionId;
    private String memberId;
    private String resourceId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate; // null until the item is returned

    public IssueTransaction(String transactionId, String memberId,
                            String resourceId, LocalDate issueDate,
                            LocalDate dueDate) {
        this.transactionId = transactionId;
        this.memberId = memberId;
        this.resourceId = resourceId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = null;
    }

    public String getTransactionId() { return transactionId; }
    public String getMemberId() { return memberId; }
    public String getResourceId() { return resourceId; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public boolean isReturned() { return returnDate != null; }

    public void markReturned(LocalDate date) {
        // TODO: Record the return date.
        this.returnDate = date;
    }

    public long getOverdueDays() {
        // TODO: Calculate overdue days. Do not return a negative value.
        LocalDate effectiveReturnDate =
                (returnDate == null) ? LocalDate.now() : returnDate;
        return Math.max(0, java.time.temporal.ChronoUnit.DAYS
                .between(dueDate, effectiveReturnDate));
    }

    public double calculateFine() {
        // TODO: Fine is Rs. 5 per overdue day.
        return getOverdueDays() * 5.0;
    }

    @Override
    public String toString() {
        return transactionId + " | Member: " + memberId
                + " | Resource: " + resourceId
                + " | Due: " + dueDate
                + " | " + (isReturned() ? "Returned" : "Not returned");
    }
}
