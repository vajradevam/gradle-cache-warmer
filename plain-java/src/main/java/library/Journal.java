package library;

public class Journal extends LibraryItem {
    private static final long serialVersionUID = 1L;
    private int volumeNumber;
    private String researchArea;

    public Journal(String itemId, String title, String publisher, int year,
                   int volumeNumber, String researchArea) {
        super(itemId, title, publisher, year);
        this.volumeNumber = volumeNumber;
        this.researchArea = researchArea;
    }

    @Override
    public int getLoanPeriod() {
        // TODO: Return the borrowing period for a Journal.
        return 3;
    }

    @Override
    public String getItemType() { return "Journal"; }

    public int getVolumeNumber() { return volumeNumber; }
    public String getResearchArea() { return researchArea; }
}
