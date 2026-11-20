package warmup;

import java.util.HashMap;
import java.util.Map;

/** Collections and exceptions. */
public class Inventory {
    private final Map<String, Integer> stock = new HashMap<>();

    public void add(String item, int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("qty must be positive");
        }
        stock.merge(item, qty, Integer::sum);
    }

    public void remove(String item, int qty) {
        int have = stock.getOrDefault(item, 0);
        if (have < qty) {
            throw new IllegalStateException("not enough " + item);
        }
        if (have == qty) {
            stock.remove(item);
        } else {
            stock.put(item, have - qty);
        }
    }

    public int count(String item) {
        return stock.getOrDefault(item, 0);
    }

    public int total() {
        return stock.values().stream().mapToInt(Integer::intValue).sum();
    }
}
