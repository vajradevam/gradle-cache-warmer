package exam;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;

class CounterPanelTest {
    @Test
    void clicks() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            CounterPanel p = new CounterPanel();
            p.getInc().doClick(0);
            p.getInc().doClick(0);
            p.getDec().doClick(0);
            assertEquals(1, p.getCount());
            assertEquals("1", p.getLabel().getText());
        });
    }
}
