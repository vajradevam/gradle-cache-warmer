package warmup;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Streams, regex, collections, Optional. */
public final class TextStats {
    private TextStats() {}

    public static Map<String, Long> wordFrequencies(String text) {
        return Arrays.stream(text.toLowerCase(Locale.ROOT).split("\\W+"))
                .filter(w -> !w.isBlank())
                .collect(Collectors.groupingBy(Function.identity(), TreeMap::new, Collectors.counting()));
    }

    public static Optional<String> longestWord(String text) {
        return Arrays.stream(text.split("\\W+"))
                .filter(w -> !w.isBlank())
                .max(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()));
    }

    public static boolean isPalindrome(String s) {
        String t = s.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
        return new StringBuilder(t).reverse().toString().equals(t);
    }

    /** Guava: split a list into chunks of the given size. */
    public static List<List<String>> chunks(List<String> items, int size) {
        return Lists.partition(items, size);
    }
}
