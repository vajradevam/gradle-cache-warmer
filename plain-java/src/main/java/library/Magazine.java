package library;

public class Magazine extends LibraryItem {
    private static final long serialVersionUID = 1L;
    private int issueNumber;
    private String publicationMonth;

    public Magazine(String itemId, String title, String publisher, int year,
                    int issueNumber, String publicationMonth) {
        super(itemId, title, publisher, year);
        this.issueNumber = issueNumber;
        this.publicationMonth = publicationMonth;
    }

    @Override
    public int getLoanPeriod() {
        // TODO: Return the borrowing period for a Magazine.
        return 7;
    }

    @Override
    public String getItemType() { return "Magazine"; }

    public int getIssueNumber() { return issueNumber; }
    public String getPublicationMonth() { return publicationMonth; }
}
