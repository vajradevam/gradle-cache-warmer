package exam;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CounterPanel extends JPanel {
    private int count;
    private final JLabel label = new JLabel("0");
    private final JButton inc = new JButton("+");
    private final JButton dec = new JButton("-");

    public CounterPanel() {
        add(dec);
        add(label);
        add(inc);
        inc.addActionListener(e -> set(count + 1));
        dec.addActionListener(e -> set(count - 1));
    }

    private void set(int v) {
        count = v;
        label.setText(Integer.toString(v));
    }

    public JButton getInc() { return inc; }
    public JButton getDec() { return dec; }
    public JLabel getLabel() { return label; }
    public int getCount() { return count; }
}
