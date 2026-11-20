package warmup;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;

class SwingTest {
    /** Lightweight components work even without a display. */
    @Test
    void addItemViaButton() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            TodoPanel p = new TodoPanel();
            p.getInput().setText("  buy milk ");
            p.getAddButton().doClick(0);
            assertEquals(1, p.getModel().size());
            assertEquals("buy milk", p.getModel().get(0));
            assertEquals("", p.getInput().getText());
        });
    }

    @Test
    void blankIsIgnored() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            TodoPanel p = new TodoPanel();
            p.getInput().setText("   ");
            p.addCurrent();
            assertEquals(0, p.getModel().size());
        });
    }

    /** Needs a real display; skipped on headless machines. */
    @Test
    void frameShowsPanel() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "no display");
        SwingUtilities.invokeAndWait(() -> {
            JFrame f = new JFrame("t");
            f.add(new TodoPanel());
            f.pack();
            f.setVisible(true);
            assertTrue(f.isShowing());
            assertTrue(f.getWidth() > 0);
            f.dispose();
        });
    }
}
