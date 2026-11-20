package warmup;

public class Main {
    public static int add(int a, int b) {
        return a + b;
    }

    public static void main(String[] args) {
        System.out.println("Cache warmed: 2 + 3 = " + add(2, 3));
    }
}
