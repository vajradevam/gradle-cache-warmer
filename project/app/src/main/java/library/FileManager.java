package library;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;

public class FileManager {
    private static final String RESOURCE_FILE = "resources.dat";
    private static final String MEMBER_FILE = "members.dat";
    private static final String TRANSACTION_FILE = "transactions.dat";

    // Generic helper methods are provided as a starting point.
    public static void saveObject(String filename, Object object)
            throws IOException {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(object);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T loadObject(String filename, T defaultValue)
            throws IOException, ClassNotFoundException {
        File file = new File(filename);
        if (!file.exists()) {
            return defaultValue;
        }
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(file))) {
            return (T) in.readObject();
        }
    }

    private static String path(Path dir, String name) {
        return dir.resolve(name).toString();
    }

    public static void saveResources(Path dir, ArrayList<LibraryItem> items)
            throws IOException {
        saveObject(path(dir, RESOURCE_FILE), items);
    }

    public static ArrayList<LibraryItem> loadResources(Path dir)
            throws IOException, ClassNotFoundException {
        return loadObject(path(dir, RESOURCE_FILE), new ArrayList<>());
    }

    public static void saveMembers(Path dir, ArrayList<Member> members)
            throws IOException {
        saveObject(path(dir, MEMBER_FILE), members);
    }

    public static ArrayList<Member> loadMembers(Path dir)
            throws IOException, ClassNotFoundException {
        return loadObject(path(dir, MEMBER_FILE), new ArrayList<>());
    }

    public static void saveTransactions(Path dir,
            ArrayList<IssueTransaction> transactions) throws IOException {
        saveObject(path(dir, TRANSACTION_FILE), transactions);
    }

    public static ArrayList<IssueTransaction> loadTransactions(Path dir)
            throws IOException, ClassNotFoundException {
        return loadObject(path(dir, TRANSACTION_FILE), new ArrayList<>());
    }
}
