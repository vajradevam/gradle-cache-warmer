package library;

import java.io.*;
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

    // TODO: Add methods to save/load resources, members and transactions
    // using the filenames declared above.
}
