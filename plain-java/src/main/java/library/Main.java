package library;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    private static final Scanner SC = new Scanner(System.in);

    public static void main(String[] args) {
        LibraryManager manager = new LibraryManager();

        try {
            manager.loadAll();
        } catch (Exception e) {
            System.out.println("Could not load saved data: " + e.getMessage());
        }

        boolean running = true;
        while (running) {
            System.out.println("\n===== LIBRARY MANAGEMENT SYSTEM =====");
            System.out.println("1. Add sample resources");
            System.out.println("2. Display all resources");
            System.out.println("3. Search resource by ID");
            System.out.println("4. Issue resource");
            System.out.println("5. Return resource");
            System.out.println("6. Display transactions");
            System.out.println("7. Save data");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            String choice = SC.nextLine();

            try {
                switch (choice) {
                    case "1":
                        addSampleResources(manager);
                        break;
                    case "2":
                        manager.getItems().forEach(System.out::println);
                        break;
                    case "3":
                        System.out.print("Resource ID: ");
                        LibraryItem item =
                                manager.findItem(SC.nextLine());
                        System.out.println(item == null
                                ? "Resource not found." : item);
                        break;
                    case "4":
                        System.out.print("Member ID: ");
                        String memberId = SC.nextLine();
                        System.out.print("Resource ID: ");
                        String resourceId = SC.nextLine();
                        IssueTransaction tx =
                                manager.issueItem(memberId, resourceId);
                        System.out.println(tx == null
                                ? "Issue operation rejected." : tx);
                        break;
                    case "5":
                        System.out.print("Member ID: ");
                        String returnMember = SC.nextLine();
                        System.out.print("Resource ID: ");
                        String returnItem = SC.nextLine();
                        double fine =
                                manager.returnItem(returnMember, returnItem);
                        System.out.println(fine < 0
                                ? "Return operation rejected."
                                : "Return successful. Fine: Rs. " + fine);
                        break;
                    case "6":
                        manager.getTransactions()
                                .forEach(System.out::println);
                        break;
                    case "7":
                        manager.saveAll();
                        System.out.println("Data saved.");
                        break;
                    case "0":
                        manager.saveAll();
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }

        System.out.println("Application closed.");
    }

    /** Members are not editable in the GUI, so make sure a few exist. */
    static void addSampleMembers(LibraryManager manager) {
        manager.addMember(new Member("M001", "Asha Rao", "asha@example.com", "9000000001"));
        manager.addMember(new Member("M002", "Ravi Kumar", "ravi@example.com", "9000000002"));
        manager.addMember(new Member("M003", "Meera Nair", "meera@example.com", "9000000003"));
    }

    private static void addSampleResources(LibraryManager manager) {
        manager.addItem(new Book("B101", "Introduction to Java", "Pearson", 2020,
                "James Gosling", "978-0-00-000001-1"));
        manager.addItem(new Magazine("M201", "Science Today", "Science Media", 2024,
                11, "November"));
        manager.addItem(new Journal("J301", "Journal of Computing", "ACM", 2023,
                5, "Computer Science"));
        addSampleMembers(manager);
        System.out.println("Sample resources and members added.");
    }
}
