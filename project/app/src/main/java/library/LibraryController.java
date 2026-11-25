package library;
import java.util.List;

public interface LibraryController {
    List<String[]> getAllItems() throws Exception;
    List<String[]> searchItems(String keyword) throws Exception;
    void addItem(String type, String id, String title, String author) throws Exception;
    void issueItem(String memberId, String itemId) throws Exception;
    void returnItem(String transactionId) throws Exception;
    List<String[]> getActiveTransactions() throws Exception;
}