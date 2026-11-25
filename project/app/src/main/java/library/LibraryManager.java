package library;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LibraryManager {
    public static final int MAX_LOANS = 3;

    private ArrayList<LibraryItem> items;
    private ArrayList<Member> members;
    private ArrayList<IssueTransaction> transactions;
    private final Path dataDir;
    private final Clock clock;

    public LibraryManager() {
        this(Path.of("."), Clock.systemDefaultZone());
    }

    /** Data files live in dataDir; clock supplies "today" (injectable for tests). */
    public LibraryManager(Path dataDir, Clock clock) {
        items = new ArrayList<>();
        members = new ArrayList<>();
        transactions = new ArrayList<>();
        this.dataDir = dataDir;
        this.clock = clock;
    }

    public List<LibraryItem> getItems() { return items; }
    public List<Member> getMembers() { return members; }
    public List<IssueTransaction> getTransactions() { return transactions; }

    public boolean addItem(LibraryItem item) {
        if (item == null || item.getItemId() == null
                || item.getItemId().isBlank() || findItem(item.getItemId()) != null) {
            return false;
        }
        items.add(item);
        return true;
    }

    public boolean removeItem(String itemId) {
        LibraryItem item = findItem(itemId);
        if (item == null || item.isIssued()) {
            return false;
        }
        return items.remove(item);
    }

    public LibraryItem findItem(String itemId) {
        if (itemId == null) return null;
        for (LibraryItem item : items) {
            if (item.getItemId().equalsIgnoreCase(itemId.trim())) return item;
        }
        return null;
    }

    public List<LibraryItem> searchByTitle(String keyword) {
        List<LibraryItem> result = new ArrayList<>();
        if (keyword == null) return result;
        String k = keyword.trim().toLowerCase();
        for (LibraryItem item : items) {
            if (item.getTitle().toLowerCase().contains(k)) result.add(item);
        }
        return result;
    }

    public boolean addMember(Member member) {
        if (member == null || member.getMemberId() == null
                || member.getMemberId().isBlank()
                || findMember(member.getMemberId()) != null) {
            return false;
        }
        members.add(member);
        return true;
    }

    public Member findMember(String memberId) {
        if (memberId == null) return null;
        for (Member m : members) {
            if (m.getMemberId().equalsIgnoreCase(memberId.trim())) return m;
        }
        return null;
    }

    public IssueTransaction issueItem(String memberId, String itemId) {
        Member member = findMember(memberId);
        LibraryItem item = findItem(itemId);
        if (member == null || item == null || item.isIssued()
                || countActiveLoans(member.getMemberId()) >= MAX_LOANS) {
            return null;
        }
        LocalDate today = LocalDate.now(clock);
        IssueTransaction tx = new IssueTransaction(
                "T" + (transactions.size() + 1), member.getMemberId(),
                item.getItemId(), today, today.plusDays(item.getLoanPeriod()));
        transactions.add(tx);
        item.setIssued(true);
        return tx;
    }

    public double returnItem(String memberId, String itemId) {
        if (memberId == null || itemId == null) return -1;
        for (IssueTransaction tx : transactions) {
            if (!tx.isReturned()
                    && tx.getMemberId().equalsIgnoreCase(memberId.trim())
                    && tx.getResourceId().equalsIgnoreCase(itemId.trim())) {
                tx.markReturned(LocalDate.now(clock));
                LibraryItem item = findItem(itemId);
                if (item != null) item.setIssued(false);
                return tx.calculateFine();
            }
        }
        return -1;
    }

    public int countActiveLoans(String memberId) {
        int count = 0;
        for (IssueTransaction tx : transactions) {
            if (!tx.isReturned() && tx.getMemberId().equalsIgnoreCase(memberId)) count++;
        }
        return count;
    }

    public void saveAll() throws IOException {
        FileManager.saveResources(dataDir, items);
        FileManager.saveMembers(dataDir, members);
        FileManager.saveTransactions(dataDir, transactions);
    }

    public void loadAll() throws IOException, ClassNotFoundException {
        items = FileManager.loadResources(dataDir);
        members = FileManager.loadMembers(dataDir);
        transactions = FileManager.loadTransactions(dataDir);
    }
}
