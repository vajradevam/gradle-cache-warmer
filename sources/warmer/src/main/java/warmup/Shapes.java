package warmup;

/** Sealed interface, records, pattern-matching switch. */
public final class Shapes {
    private Shapes() {}

    public sealed interface Shape permits Circle, Rect, Square {}

    public record Circle(double r) implements Shape {}

    public record Rect(double w, double h) implements Shape {}

    public record Square(double s) implements Shape {}

    public static double area(Shape shape) {
        return switch (shape) {
            case Circle c -> Math.PI * c.r() * c.r();
            case Rect r -> r.w() * r.h();
            case Square s -> s.s() * s.s();
        };
    }
}
