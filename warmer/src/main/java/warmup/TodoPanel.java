package warmup;

import java.awt.BorderLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/** Swing: a small to-do list panel. */
public class TodoPanel extends JPanel {
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JTextField input = new JTextField(20);
    private final JButton addButton = new JButton("Add");
    private final JList<String> list = new JList<>(model);

    public TodoPanel() {
        super(new BorderLayout());
        JPanel top = new JPanel();
        top.add(input);
        top.add(addButton);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);
        addButton.addActionListener(e -> addCurrent());
    }

    public void addCurrent() {
        String text = input.getText().trim();
        if (!text.isEmpty()) {
            model.addElement(text);
            input.setText("");
        }
    }

    public JTextField getInput() { return input; }
    public JButton getAddButton() { return addButton; }
    public DefaultListModel<String> getModel() { return model; }
    public JList<String> getList() { return list; }
}
