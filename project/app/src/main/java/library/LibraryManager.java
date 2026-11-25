package library;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LibraryManager {
    private ArrayList<LibraryItem> items;
    private ArrayList<Member> members;
    private ArrayList<IssueTransaction> transactions;

    public LibraryManager() {
        items = new ArrayList<>();
        members = new ArrayList<>();
        transactions = new ArrayList<>();
    }

    public List<LibraryItem> getItems() { return items; }
    public List<Member> getMembers() { return members; }
    public List<IssueTransaction> getTransactions() { return transactions; }

    public boolean addItem(LibraryItem item) {
        // TODO: Reject null items and duplicate resource IDs.
        return false;
    }

    public boolean removeItem(String itemId) {
        // TODO: Remove only if the resource exists and is not issued.
        return false;
    }

    public LibraryItem findItem(String itemId) {
        // TODO: Search by ID; return null if not found.
        return null;
    }

    public List<LibraryItem> searchByTitle(String keyword) {
        // TODO: Case-insensitive partial title search.
        return new ArrayList<>();
    }

    public boolean addMember(Member member) {
        // TODO: Reject duplicate member IDs.
        return false;
    }

    public Member findMember(String memberId) {
        // TODO: Search by member ID.
        return null;
    }

    public IssueTransaction issueItem(String memberId, String itemId) {
        // TODO:
        // 1. Validate member and resource.
        // 2. Check resource availability.
        // 3. Check member's active-loan limit (maximum 3).
        // 4. Calculate due date using item.getLoanPeriod().
        // 5. Create transaction and update resource status.
        // 6. Return the transaction, or null if issue is rejected.
        return null;
    }

    public double returnItem(String memberId, String itemId) {
        // TODO:
        // 1. Find the active transaction for this member and resource.
        // 2. Mark it returned and update resource availability.
        // 3. Return the fine amount.
        // Return -1 if the return operation is invalid.
        return -1;
    }

    public int countActiveLoans(String memberId) {
        // TODO: Count transactions for this member that are not returned.
        return 0;
    }

    public void saveAll() throws IOException {
        // TODO: Persist all three collections using FileManager.
    }

    public void loadAll() throws IOException, ClassNotFoundException {
        // TODO: Load all three collections using FileManager.
        // Missing files should result in empty collections.
    }
}
