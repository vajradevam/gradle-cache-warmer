package library;
import java.util.ArrayList;
import java.util.List;

/** In-memory demo so the GUI runs immediately. Not the final backend. */
public class DemoLibraryController implements LibraryController {
    private final List<String[]> items = new ArrayList<>();
    private final List<String[]> loans = new ArrayList<>();
    private int nextId = 1;

    public DemoLibraryController() {
        items.add(new String[]{"B101","Book","Introduction to Java","James Gosling","Available"});
        items.add(new String[]{"M201","Magazine","Science Today","Editorial Team","Available"});
    }
    private List<String[]> copy(List<String[]> in) {
        List<String[]> out = new ArrayList<>();
        for (String[] r : in) out.add(r.clone());
        return out;
    }
    public List<String[]> getAllItems() { return copy(items); }
    public List<String[]> searchItems(String q) {
        List<String[]> out = new ArrayList<>();
        for (String[] r : items)
            if (String.join(" ", r).toLowerCase().contains(q.toLowerCase())) out.add(r.clone());
        return out;
    }
    public void addItem(String type, String id, String title, String author) {
        if (id.isBlank() || title.isBlank() || author.isBlank())
            throw new IllegalArgumentException("Fill in all item fields.");
        for (String[] r : items) if (r[0].equalsIgnoreCase(id))
            throw new IllegalArgumentException("Duplicate item ID.");
        items.add(new String[]{id,type,title,author,"Available"});
    }
    public void issueItem(String member, String itemId) {
        if (member.isBlank() || itemId.isBlank()) throw new IllegalArgumentException("Enter member and item IDs.");
        for (String[] r : items) if (r[0].equalsIgnoreCase(itemId)) {
            if (!r[4].equals("Available")) throw new IllegalArgumentException("Item is not available.");
            r[4] = "Issued";
            loans.add(new String[]{"T"+nextId++,member,itemId,"Backend due date"});
            return;
        }
        throw new IllegalArgumentException("Item not found.");
    }
    public void returnItem(String txn) {
        for (int i=0; i<loans.size(); i++) if (loans.get(i)[0].equalsIgnoreCase(txn)) {
            String itemId = loans.remove(i)[2];
            for (String[] r : items) if (r[0].equals(itemId)) r[4] = "Available";
            return;
        }
        throw new IllegalArgumentException("Active transaction not found.");
    }
    public List<String[]> getActiveTransactions() { return copy(loans); }
}