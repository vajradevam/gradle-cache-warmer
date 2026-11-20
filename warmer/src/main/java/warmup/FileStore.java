package warmup;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** java.nio.file. */
public final class FileStore {
    private FileStore() {}

    public static void save(Path p, List<String> lines) throws IOException {
        Files.write(p, lines, StandardCharsets.UTF_8);
    }

    public static List<String> load(Path p) throws IOException {
        return Files.readAllLines(p, StandardCharsets.UTF_8);
    }
}
