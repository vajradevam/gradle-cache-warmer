package library;

import java.util.ArrayList;
import java.util.List;

/**
 * Connects the GUI to LibraryManager. Converts objects to String[] rows and
 * turns rejected operations into exceptions; business rules stay in the manager.
 * Every change is saved immediately so data survives a restart.
 */
public class LibraryManagerAdapter implements LibraryController {
    private final LibraryManager manager;
    public LibraryManagerAdapter(LibraryManager manager) { this.manager = manager; }

    private static String creator(LibraryItem item) {
        return item instanceof Book b ? b.getAuthor() : item.getPublisher();
    }

    private static String[] row(LibraryItem item) {
        return new String[]{item.getItemId(), item.getItemType(), item.getTitle(),
                creator(item), item.isIssued() ? "Issued" : "Available"};
    }

    public List<String[]> getAllItems() {
        List<String[]> rows = new ArrayList<>();
        for (LibraryItem item : manager.getItems()) rows.add(row(item));
        return rows;
    }

    public List<String[]> searchItems(String keyword) {
        List<String[]> rows = new ArrayList<>();
        String k = keyword == null ? "" : keyword.trim().toLowerCase();
        for (LibraryItem item : manager.getItems()) {
            if (item.getItemId().toLowerCase().contains(k)
                    || item.getTitle().toLowerCase().contains(k)
                    || creator(item).toLowerCase().contains(k)) {
                rows.add(row(item));
            }
        }
        return rows;
    }

    public void addItem(String type, String id, String title, String author) throws Exception {
        if (isBlank(id) || isBlank(title) || isBlank(author)) {
            throw new IllegalArgumentException("Fill in all item fields.");
        }
        id = id.trim(); title = title.trim(); author = author.trim();
        LibraryItem item;
        if ("Book".equals(type)) {
            item = new Book(id, title, "Unknown", 0, author, "N/A");
        } else if ("Magazine".equals(type)) {
            item = new Magazine(id, title, author, 0, 0, "N/A");
        } else if ("Journal".equals(type)) {
            item = new Journal(id, title, author, 0, 0, "N/A");
        } else {
            throw new IllegalArgumentException("Unknown item type.");
        }
        if (!manager.addItem(item)) {
            throw new IllegalArgumentException("Duplicate item ID.");
        }
        manager.saveAll();
    }

    public void issueItem(String memberId, String itemId) throws Exception {
        if (isBlank(memberId) || isBlank(itemId)) {
            throw new IllegalArgumentException("Enter member and item IDs.");
        }
        if (manager.findMember(memberId) == null) {
            throw new IllegalArgumentException("Member not found.");
        }
        if (manager.findItem(itemId) == null) {
            throw new IllegalArgumentException("Item not found.");
        }
        if (manager.issueItem(memberId, itemId) == null) {
            throw new IllegalArgumentException(
                    "Issue rejected: item unavailable or loan limit ("
                    + LibraryManager.MAX_LOANS + ") reached.");
        }
        manager.saveAll();
    }

    public void returnItem(String transactionId) throws Exception {
        for (IssueTransaction tx : manager.getTransactions()) {
            if (tx.getTransactionId().equalsIgnoreCase(transactionId == null ? "" : transactionId.trim())) {
                if (tx.isReturned()) {
                    throw new IllegalArgumentException("Transaction already returned.");
                }
                manager.returnItem(tx.getMemberId(), tx.getResourceId());
                manager.saveAll();
                return;
            }
        }
        throw new IllegalArgumentException("Active transaction not found.");
    }

    public List<String[]> getActiveTransactions() {
        List<String[]> rows = new ArrayList<>();
        for (IssueTransaction tx : manager.getTransactions()) {
            if (!tx.isReturned()) {
                rows.add(new String[]{tx.getTransactionId(), tx.getMemberId(),
                        tx.getResourceId(), tx.getDueDate().toString()});
            }
        }
        return rows;
    }

    private static boolean isBlank(String s) { return s == null || s.isBlank(); }
}
