package library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/** Simple Swing front end. Business rules and file I/O stay in the backend. */
public class LibraryGUI extends JFrame {
    private final LibraryController controller;
    private final DefaultTableModel items = model("ID", "Type", "Title", "Author", "Status");
    private final DefaultTableModel loans = model("Transaction ID", "Member ID", "Item ID", "Due Date");
    private JTextField search, id, title, author, memberId, issueItemId, transactionId;
    private JComboBox<String> type;

    private static DefaultTableModel model(String... columns) {
        return new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    public LibraryGUI(LibraryController controller) {
        this.controller = controller;
        setTitle("Library Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(850, 550);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Items", itemsPanel());
        tabs.addTab("Add Item", addPanel());
        tabs.addTab("Issue / Return", loanPanel());
        add(tabs);
        refreshItems();
        refreshLoans();
    }

    private JPanel itemsPanel() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        JPanel top = new JPanel();
        search = new JTextField(22);
        JButton find = new JButton("Search"), all = new JButton("Show All");
        top.add(new JLabel("ID / title / author:"));
        top.add(search); top.add(find); top.add(all);
        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(new JTable(items)), BorderLayout.CENTER);
        find.addActionListener(e -> safe(() -> fill(items, controller.searchItems(search.getText().trim()))));
        all.addActionListener(e -> refreshItems());
        return p;
    }

    private JPanel addPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        type = new JComboBox<>(new String[]{"Book", "Magazine", "Journal"});
        id = new JTextField(18); title = new JTextField(18); author = new JTextField(18);
        addRow(p, 0, "Type:", type); addRow(p, 1, "ID:", id);
        addRow(p, 2, "Title:", title); addRow(p, 3, "Author:", author);
        JButton add = new JButton("Add Item");
        GridBagConstraints c = constraints(4); c.gridx = 1; p.add(add, c);
        add.addActionListener(e -> safe(() -> {
            controller.addItem((String) type.getSelectedItem(), id.getText().trim(),
                    title.getText().trim(), author.getText().trim());
            message("Item added.");
            id.setText(""); title.setText(""); author.setText(""); refreshItems();
        }));
        return p;
    }

    private JPanel loanPanel() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridBagLayout());
        memberId = new JTextField(14); issueItemId = new JTextField(14);
        transactionId = new JTextField(14);
        addRow(form, 0, "Member ID:", memberId);
        addRow(form, 1, "Item ID:", issueItemId);
        JButton issue = new JButton("Issue Item");
        GridBagConstraints c = constraints(2); c.gridx = 1; form.add(issue, c);
        addRow(form, 3, "Transaction ID:", transactionId);
        JButton ret = new JButton("Return Item");
        c = constraints(4); c.gridx = 1; form.add(ret, c);
        JButton refresh = new JButton("Refresh Active Loans");
        c = constraints(5); c.gridx = 1; form.add(refresh, c);
        p.add(form, BorderLayout.NORTH);
        p.add(new JScrollPane(new JTable(loans)), BorderLayout.CENTER);
        issue.addActionListener(e -> safe(() -> {
            controller.issueItem(memberId.getText().trim(), issueItemId.getText().trim());
            message("Item issued."); refreshItems(); refreshLoans();
        }));
        ret.addActionListener(e -> safe(() -> {
            controller.returnItem(transactionId.getText().trim());
            message("Item returned."); transactionId.setText(""); refreshItems(); refreshLoans();
        }));
        refresh.addActionListener(e -> refreshLoans());
        return p;
    }

    private GridBagConstraints constraints(int row) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(7, 7, 7, 7); c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = row; return c;
    }
    private void addRow(JPanel p, int row, String label, Component field) {
        GridBagConstraints c = constraints(row);
        c.gridx = 0; p.add(new JLabel(label), c);
        c.gridx = 1; p.add(field, c);
    }
    private void fill(DefaultTableModel model, List<String[]> rows) {
        model.setRowCount(0);
        for (String[] row : rows) model.addRow(row);
    }
    private void refreshItems() { safe(() -> fill(items, controller.getAllItems())); }
    private void refreshLoans() { safe(() -> fill(loans, controller.getActiveTransactions())); }
    private void safe(Work work) {
        try { work.run(); }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage() == null ? "Operation failed." : ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void message(String s) {
        JOptionPane.showMessageDialog(this, s, "Library", JOptionPane.INFORMATION_MESSAGE);
    }
    @FunctionalInterface private interface Work { void run() throws Exception; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryManager manager = new LibraryManager();
            try {
                manager.loadAll();
            } catch (Exception e) {
                System.out.println("Could not load saved data: " + e.getMessage());
            }
            Main.addSampleMembers(manager);
            LibraryGUI gui = new LibraryGUI(new LibraryManagerAdapter(manager));
            gui.setVisible(true);
        });
    }
}
