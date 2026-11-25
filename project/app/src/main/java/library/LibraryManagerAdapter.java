package library;

import java.util.List;

/**
 * Integration template. Implement each method using your existing LibraryManager.
 * Convert backend objects into String[] rows in the column order documented
 * in LibraryController.java. Do not duplicate business rules here.
 */
public class LibraryManagerAdapter implements LibraryController {
    private final LibraryManager manager;
    public LibraryManagerAdapter(LibraryManager manager) { this.manager = manager; }

    public List<String[]> getAllItems() throws Exception {
        // TODO: Convert manager.getItems() into rows:
        // {id, class simple name, title, author, availability}
        throw new UnsupportedOperationException("Connect getAllItems() to LibraryManager.");
    }
    public List<String[]> searchItems(String keyword) throws Exception {
        // TODO: Call your manager's search method and convert the results.
        throw new UnsupportedOperationException("Connect searchItems() to LibraryManager.");
    }
    public void addItem(String type, String id, String title, String author) throws Exception {
        // TODO: Construct Book / Magazine / Journal and call manager.addItem(...).
        throw new UnsupportedOperationException("Connect addItem() to LibraryManager.");
    }
    public void issueItem(String memberId, String itemId) throws Exception {
        // TODO: Call your manager's issue/borrow method.
        throw new UnsupportedOperationException("Connect issueItem() to LibraryManager.");
    }
    public void returnItem(String transactionId) throws Exception {
        // TODO: Call your manager's return method.
        throw new UnsupportedOperationException("Connect returnItem() to LibraryManager.");
    }
    public List<String[]> getActiveTransactions() throws Exception {
        // TODO: Convert active loans into {transactionId, memberId, itemId, dueDate}.
        throw new UnsupportedOperationException("Connect getActiveTransactions() to LibraryManager.");
    }
}