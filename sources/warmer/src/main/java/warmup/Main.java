package warmup;

import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Main {
    public static void main(String[] args) {
        boolean noGui = args.length > 0 && args[0].equals("--no-gui");
        if (noGui || GraphicsEnvironment.isHeadless()) {
            System.out.println("Cache warmed (no GUI). Area of square 3: "
                    + Shapes.area(new Shapes.Square(3)));
            return;
        }
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Todo");
            f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            f.add(new TodoPanel());
            f.pack();
            f.setVisible(true);
        });
    }
}
