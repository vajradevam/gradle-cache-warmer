package library;

import java.io.Serializable;

public abstract class LibraryItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String itemId;
    private String title;
    private String publisher;
    private int publicationYear;
    private boolean issued;

    public LibraryItem(String itemId, String title, String publisher,
                       int publicationYear) {
        this.itemId = itemId;
        this.title = title;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.issued = false;
    }

    public abstract int getLoanPeriod();
    public abstract String getItemType();

    public String getItemId() { return itemId; }
    public String getTitle() { return title; }
    public String getPublisher() { return publisher; }
    public int getPublicationYear() { return publicationYear; }
    public boolean isIssued() { return issued; }

    public void setIssued(boolean issued) { this.issued = issued; }

    @Override
    public String toString() {
        return itemId + " | " + getItemType() + " | " + title
                + " | " + (issued ? "Issued" : "Available");
    }
}
