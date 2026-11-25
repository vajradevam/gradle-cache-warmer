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

    private static void addSampleResources(LibraryManager manager) {
        // TODO: Add sample Book, Magazine and Journal objects.
        // Also add at least one sample Member.
        System.out.println("Implement sample data creation here.");
    }
}
