package library;

public class Book extends LibraryItem {
    private static final long serialVersionUID = 1L;
    private String author;
    private String isbn;

    public Book(String itemId, String title, String publisher, int year,
                String author, String isbn) {
        super(itemId, title, publisher, year);
        this.author = author;
        this.isbn = isbn;
    }

    @Override
    public int getLoanPeriod() {
        // TODO: Return the borrowing period for a Book.
        return 14;
    }

    @Override
    public String getItemType() { return "Book"; }

    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
}
