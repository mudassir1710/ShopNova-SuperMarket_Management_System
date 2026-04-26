package com.shopnova.ui;

import com.shopnova.manager.CashierManager;
import com.shopnova.manager.Inventory;
import com.shopnova.model.Cashier;
import com.shopnova.model.Product;
import com.shopnova.model.Cashier;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class AdminFrame extends JFrame {

    // ── Palette ────────────────────────────────────────────────────
    static final Color SIDEBAR  = new Color(15, 23, 42);
    static final Color ACCENT   = new Color(59, 130, 246);
    static final Color CARD_BG  = Color.WHITE;
    static final Color PAGE_BG  = new Color(241, 245, 249);
    static final Color TEXT_DK  = new Color(15, 23, 42);
    static final Color TEXT_MD  = new Color(71, 85, 105);
    static final Color SUCCESS  = new Color(22, 163, 74);
    static final Color DANGER   = new Color(220, 38, 38);
    static final Color AMBER    = new Color(202, 138, 4);

    // ── Fonts ──────────────────────────────────────────────────────
    static final Font F_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    static final Font F_SECT  = new Font("Segoe UI", Font.BOLD, 15);
    static final Font F_BODY  = new Font("Segoe UI", Font.PLAIN, 14);
    static final Font F_NAV   = new Font("Segoe UI", Font.BOLD, 14);
    static final Font F_STAT  = new Font("Segoe UI", Font.BOLD, 28);

    Inventory      inventory;
    CashierManager cashierManager;
    JPanel         content;
    JButton        activeNavBtn;

    public AdminFrame(Inventory inventory, CashierManager cashierManager) {
        this.inventory      = inventory;
        this.cashierManager = cashierManager;

        setTitle("ShopNova — Admin");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1050, 660);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE_BG);
        root.add(buildSidebar(), BorderLayout.WEST);

        content = new JPanel(new BorderLayout());
        content.setBackground(PAGE_BG);
        root.add(content, BorderLayout.CENTER);

        add(root);
        showDashboard();
        setVisible(true);
    }

    // ── Sidebar ────────────────────────────────────────────────────
    JPanel buildSidebar() {
        JPanel sb = new JPanel();
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));
        sb.setBackground(SIDEBAR);
        sb.setPreferredSize(new Dimension(210, 0));

        JLabel logo = new JLabel(" ShopNova");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        logo.setBorder(BorderFactory.createEmptyBorder(28, 18, 28, 0));
        sb.add(logo);
        sb.add(divider());

        sb.add(navBtn("  Dashboard",   "dash"));
        sb.add(navBtn("  Inventory",   "inv"));
        sb.add(navBtn("  Cashiers",    "cashiers"));
        sb.add(navBtn("  Add Product", "addprod"));
        sb.add(navBtn("  Add Cashier", "addcash"));

        sb.add(Box.createVerticalGlue());
        sb.add(divider());
        JButton logout = navBtn("  Logout", "logout");
        logout.setForeground(new Color(252, 165, 165));
        sb.add(logout);
        sb.add(Box.createVerticalStrut(12));
        return sb;
    }

    JButton navBtn(String label, String key) {
        JButton b = new JButton(label);
        b.setFont(F_NAV);
        b.setForeground(new Color(148, 163, 184));
        b.setBackground(SIDEBAR);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setContentAreaFilled(true);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        b.addActionListener(e -> {
            setActive(b);
            switch (key) {
                case "dash"     -> showDashboard();
                case "inv"      -> showInventory();
                case "addprod"  -> showAddProduct();
                case "cashiers" -> showCashiers();
                case "addcash"  -> showAddCashier();
                case "logout"   -> { new LoginFrame(); dispose(); }
            }
        });
        return b;
    }

    void setActive(JButton b) {
        if (activeNavBtn != null) {
            activeNavBtn.setBackground(SIDEBAR);
            activeNavBtn.setForeground(new Color(148, 163, 184));
        }
        b.setBackground(new Color(30, 58, 95));
        b.setForeground(Color.WHITE);
        activeNavBtn = b;
    }

    JSeparator divider() {
        JSeparator s = new JSeparator();
        s.setForeground(new Color(30, 41, 59));
        s.setBackground(new Color(30, 41, 59));
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return s;
    }

    void swap(JPanel panel) {
        content.removeAll();
        content.add(panel, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }

    // ── Dashboard ──────────────────────────────────────────────────
    void showDashboard() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(PAGE_BG);
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        p.add(pageHeader("Dashboard", "Welcome back, Admin"), BorderLayout.NORTH);

        int totalProd  = inventory.getProducts().size();
        int lowStock   = (int) inventory.getProducts().stream().filter(x -> x.qty <= 5).count();
        int expired    = (int) inventory.getProducts().stream().filter(x -> x.getStatus().equals("EXPIRED")).count();
        int cashierCnt = cashierManager.getCashiers().size();
        double totalSales = cashierManager.getCashiers().stream().mapToDouble(c -> c.totalSales).sum();

        JPanel cards = new JPanel(new GridLayout(1, 5, 18, 0));
        cards.setOpaque(false);
        cards.setBorder(BorderFactory.createEmptyBorder(24, 0, 0, 0));
        cards.add(statCard("Products",  String.valueOf(totalProd),  ACCENT));
        cards.add(statCard("Low Stock", String.valueOf(lowStock),   AMBER));
        cards.add(statCard("Expired",   String.valueOf(expired),    DANGER));
        cards.add(statCard("Cashiers",  String.valueOf(cashierCnt), SUCCESS));
        cards.add(statCard("Total Sales", String.format("Rs %.2f", totalSales), new Color(124, 58, 237)));
        p.add(cards, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(28, 0, 0, 0));
        JLabel recLbl = new JLabel("Recent Inventory");
        recLbl.setFont(F_SECT);
        recLbl.setForeground(TEXT_DK);
        recLbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        bottom.add(recLbl, BorderLayout.NORTH);
        bottom.add(buildInventoryTable(), BorderLayout.CENTER);
        p.add(bottom, BorderLayout.SOUTH);
        swap(p);
    }

    JPanel statCard(String label, String value, Color accent) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(22, 22, 22, 22)));
        JLabel bar = new JLabel();
        bar.setOpaque(true);
        bar.setBackground(accent);
        bar.setPreferredSize(new Dimension(4, 0));
        card.add(bar, BorderLayout.WEST);
        JPanel txt = new JPanel(new GridLayout(2, 1, 0, 6));
        txt.setOpaque(false);
        txt.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 0));
        JLabel valLbl = new JLabel(value);
        valLbl.setFont(F_STAT);
        valLbl.setForeground(accent);
        JLabel nameLbl = new JLabel(label);
        nameLbl.setFont(F_BODY);
        nameLbl.setForeground(TEXT_MD);
        txt.add(valLbl);
        txt.add(nameLbl);
        card.add(txt, BorderLayout.CENTER);
        return card;
    }

    // ── Inventory ──────────────────────────────────────────────────
    void showInventory() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(PAGE_BG);
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        p.add(pageHeader("Inventory", "All products in stock"), BorderLayout.NORTH);

        String[] cols = { "ID", "Name", "Qty", "Category", "Expirable", "Expiry", "Price", "Status" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        refreshInventoryModel(model);

        JTable table = styledTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Color.WHITE);
        p.add(wrapInCard(sp), BorderLayout.CENTER);

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bar.setOpaque(false);
        bar.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));
        JButton updateBtn = actionBtn("Update Selected", AMBER);
        JButton removeBtn = actionBtn("Remove Selected", DANGER);
        bar.add(updateBtn);
        bar.add(removeBtn);
        p.add(bar, BorderLayout.SOUTH);

        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a product first."); return; }
            Product pr = inventory.getProducts().get(row);

            JTextField nameF  = new JTextField(pr.name);
            JTextField qtyF   = new JTextField(String.valueOf(pr.qty));
            JTextField catF   = new JTextField(pr.category);
            JTextField priceF = new JTextField(String.valueOf(pr.price));
            JCheckBox  expChk = new JCheckBox("Product is Expirable", pr.expirable);
            JTextField expF   = new JTextField(pr.expiry != null ? pr.expiry.toString() : "");
            expF.setEnabled(pr.expirable);
            expChk.addActionListener(ev -> expF.setEnabled(expChk.isSelected()));

            JPanel dlg = new JPanel(new GridLayout(12, 1, 4, 6));
            dlg.add(new JLabel("Name:")); dlg.add(nameF);
            dlg.add(new JLabel("Quantity:")); dlg.add(qtyF);
            dlg.add(new JLabel("Category:")); dlg.add(catF);
            dlg.add(new JLabel("Price:")); dlg.add(priceF);
            dlg.add(expChk);
            dlg.add(new JLabel("Expiry (YYYY-MM-DD) — only if expirable:"));
            dlg.add(expF);

            int res = JOptionPane.showConfirmDialog(this, dlg, "Update Product", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    String nm = nameF.getText().trim();
                    int    qt = Integer.parseInt(qtyF.getText().trim());
                    String ct = catF.getText().trim();
                    double price = Double.parseDouble(priceF.getText().trim());
                    boolean expirable = expChk.isSelected();
                    if (nm.isEmpty() || ct.isEmpty()) throw new Exception();
                    if (expirable && expF.getText().trim().isEmpty()) throw new Exception();

                    pr.name     = nm;
                    pr.qty      = qt;
                    pr.category = ct;
                    pr.price    = price;
                    pr.expirable = expirable;
                    pr.expiry    = expirable ? java.time.LocalDate.parse(expF.getText().trim()) : null;

                    inventory.save();
                    refreshInventoryModel(model);
                } catch (Exception ex2) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields correctly.");
                }
            }
        });

        removeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a product first!"); return; }
            String name = inventory.getProducts().get(row).name;
            if (JOptionPane.showConfirmDialog(this, "Remove '" + name + "'?", "Confirm",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                inventory.remove(inventory.getProducts().get(row).id);
                refreshInventoryModel(model);
            }
        });

        swap(p);
    }

    JScrollPane buildInventoryTable() {
        String[] cols = { "ID", "Name", "Qty", "Category", "Expirable", "Expiry", "Price", "Status" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        for (Product pr : inventory.getProducts())
            model.addRow(new Object[]{
                    pr.id, pr.name, pr.qty, pr.category,
                    pr.expirable ? "Yes" : "No",
                    pr.expiry != null ? pr.expiry.toString() : "—",
                    String.format("Rs %.2f", pr.price),
                    pr.getStatus()});
        JTable table = styledTable(model);
        colorStatusColumn(table, 7);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Color.WHITE);
        return sp;
    }

    // ── Add Product — with expiry toggle ──────────────────────────
    void showAddProduct() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(PAGE_BG);
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        p.add(pageHeader("Add Product", "Add a new product to inventory"), BorderLayout.NORTH);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 8, 8, 8);
        gc.fill   = GridBagConstraints.HORIZONTAL;

        JTextField nameF  = field();
        JTextField qtyF   = field();
        JTextField catF   = field();
        JTextField priceF = field();

        // Expiry toggle
        JCheckBox expChk  = new JCheckBox("This product is expirable");
        expChk.setFont(F_BODY);
        expChk.setBackground(CARD_BG);
        expChk.setForeground(TEXT_DK);

        JTextField expF   = field();
        expF.setEnabled(false);
        expF.setBackground(new Color(248, 250, 252));
        expF.setToolTipText("Format: YYYY-MM-DD");

        expChk.addActionListener(e -> {
            boolean checked = expChk.isSelected();
            expF.setEnabled(checked);
            expF.setBackground(checked ? Color.WHITE : new Color(248, 250, 252));
        });

        addRow(card, gc, 0, "Product Name", nameF);
        addRow(card, gc, 1, "Quantity",     qtyF);
        addRow(card, gc, 2, "Category",     catF);
        addRow(card, gc, 3, "Price",        priceF);

        // Expiry checkbox row
        gc.gridx = 0; gc.gridy = 4; gc.gridwidth = 2; gc.weightx = 1;
        card.add(expChk, gc);

        // Expiry date row (only active when expChk is on)
        gc.gridwidth = 1;
        addRow(card, gc, 5, "Expiry Date (YYYY-MM-DD)", expF);

        JButton saveBtn = actionBtn("Add Product", ACCENT);
        gc.gridx = 0; gc.gridy = 6; gc.gridwidth = 2;
        gc.insets = new Insets(20, 8, 8, 8);
        card.add(saveBtn, gc);

        saveBtn.addActionListener(e -> {
            try {
                String  nm         = nameF.getText().trim();
                int     qt         = Integer.parseInt(qtyF.getText().trim());
                String  ct         = catF.getText().trim();
                double  pr         = Double.parseDouble(priceF.getText().trim());
                boolean expirable  = expChk.isSelected();
                String  expiryStr  = expF.getText().trim();

                if (nm.isEmpty() || ct.isEmpty()) throw new Exception("empty fields");
                if (expirable && expiryStr.isEmpty())
                    throw new Exception("expiry required");

                inventory.addProduct(new Product(
                        inventory.getNextId(), nm, qt, ct, expirable, expiryStr, pr));
                JOptionPane.showMessageDialog(this, "Product added successfully!");
                nameF.setText(""); qtyF.setText(""); catF.setText(""); priceF.setText("");
                expChk.setSelected(false);
                expF.setText(""); expF.setEnabled(false);
                expF.setBackground(new Color(248, 250, 252));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Please fill all fields correctly.\n" +
                        "(If expirable is checked, expiry date is required.)");
            }
        });

        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrap.setOpaque(false);
        wrap.add(card);
        p.add(wrap, BorderLayout.CENTER);
        swap(p);
    }

    // ── Cashiers ───────────────────────────────────────────────────
    void showCashiers() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(PAGE_BG);
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        p.add(pageHeader("Cashiers", "Manage cashier accounts"), BorderLayout.NORTH);

        String[] cols = { "#", "Username" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        refreshCashierModel(model);

        JTable table = styledTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        p.add(wrapInCard(sp), BorderLayout.CENTER);

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bar.setOpaque(false);
        bar.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));
        JButton updateBtn = actionBtn("Update Selected", AMBER);
        JButton removeBtn = actionBtn("Remove Selected", DANGER);
        bar.add(updateBtn); bar.add(removeBtn);
        p.add(bar, BorderLayout.SOUTH);

        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a cashier first."); return; }
            Cashier c = cashierManager.getCashiers().get(row);
            JTextField uF = new JTextField(c.username);
            JPasswordField pF = new JPasswordField(c.password);
            JPanel dlg = new JPanel(new GridLayout(4, 1, 4, 8));
            dlg.add(new JLabel("New Username:")); dlg.add(uF);
            dlg.add(new JLabel("New Password:")); dlg.add(pF);
            int res = JOptionPane.showConfirmDialog(this, dlg, "Update Cashier", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                String u = uF.getText().trim(), pw = new String(pF.getPassword()).trim();
                if (u.isEmpty() || pw.isEmpty()) { JOptionPane.showMessageDialog(this, "Fields cannot be empty."); return; }
                c.username = u; c.password = pw;
                cashierManager.save(); refreshCashierModel(model);
            }
        });

        removeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a cashier first."); return; }
            String name = cashierManager.getCashiers().get(row).username;
            if (JOptionPane.showConfirmDialog(this, "Remove '" + name + "'?", "Confirm",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                cashierManager.remove(name); refreshCashierModel(model);
            }
        });
        swap(p);
    }

    // ── Add Cashier ────────────────────────────────────────────────
    void showAddCashier() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(PAGE_BG);
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        p.add(pageHeader("Add Cashier", "Create a new cashier account"), BorderLayout.NORTH);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 8, 8, 8);
        gc.fill   = GridBagConstraints.HORIZONTAL;

        JTextField   uF = field();
        JPasswordField pF = new JPasswordField();
        pF.setFont(F_BODY);
        pF.setPreferredSize(new Dimension(260, 38));

        addRow(card, gc, 0, "Username", uF);
        JLabel pl = new JLabel("Password");
        pl.setFont(F_BODY); pl.setForeground(TEXT_MD);
        gc.gridx = 0; gc.gridy = 1; gc.gridwidth = 1; gc.weightx = 0; card.add(pl, gc);
        gc.gridx = 1; gc.weightx = 1; card.add(pF, gc);

        JButton saveBtn = actionBtn("Add Cashier", ACCENT);
        gc.gridx = 0; gc.gridy = 2; gc.gridwidth = 2;
        gc.insets = new Insets(20, 8, 8, 8);
        card.add(saveBtn, gc);

        saveBtn.addActionListener(e -> {
            String u = uF.getText().trim(), pw = new String(pF.getPassword()).trim();
            if (u.isEmpty() || pw.isEmpty()) { JOptionPane.showMessageDialog(this, "Please fill all fields."); return; }
            cashierManager.add(new Cashier(u, pw));
            JOptionPane.showMessageDialog(this, "Cashier added successfully!");
            uF.setText(""); pF.setText("");
        });

        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrap.setOpaque(false); wrap.add(card);
        p.add(wrap, BorderLayout.CENTER);
        swap(p);
    }

    // ── Helpers ────────────────────────────────────────────────────
    void refreshCashierModel(DefaultTableModel m) {
        m.setRowCount(0);
        int i = 1;
        for (Cashier c : cashierManager.getCashiers()) m.addRow(new Object[]{i++, c.username});
    }

    void refreshInventoryModel(DefaultTableModel m) {
        m.setRowCount(0);
        for (Product pr : inventory.getProducts())
            m.addRow(new Object[]{
                    pr.id, pr.name, pr.qty, pr.category,
                    pr.expirable ? "Yes" : "No",
                    pr.expiry != null ? pr.expiry.toString() : "—",
                    String.format("Rs %.2f", pr.price),
                    pr.getStatus()});
    }

    void colorStatusColumn(JTable table, int colIndex) {
        table.getColumnModel().getColumn(colIndex).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable tb, Object v,
                    boolean sel, boolean foc, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(tb, v, sel, foc, r, c);
                String s = v == null ? "" : v.toString();
                l.setForeground(s.equals("EXPIRED") ? DANGER : s.equals("LOW STOCK") ? AMBER : SUCCESS);
                l.setFont(new Font("Segoe UI", Font.BOLD, 13));
                return l;
            }
        });
    }

    JPanel pageHeader(String title, String sub) {
        JPanel h = new JPanel(new GridLayout(2, 1, 0, 4));
        h.setOpaque(false);
        h.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        JLabel t = new JLabel(title); t.setFont(F_TITLE); t.setForeground(TEXT_DK);
        JLabel s = new JLabel(sub);   s.setFont(F_BODY);  s.setForeground(TEXT_MD);
        h.add(t); h.add(s);
        return h;
    }

    JPanel wrapInCard(Component inner) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        card.add(inner, BorderLayout.CENTER);
        return card;
    }

    JTable styledTable(DefaultTableModel model) {
        JTable t = new JTable(model) { public boolean isCellEditable(int r, int c) { return false; } };
        t.setFont(F_BODY);
        t.setRowHeight(34);
        t.setBackground(Color.WHITE);
        t.setForeground(TEXT_DK);
        t.setShowHorizontalLines(true);
        t.setGridColor(new Color(241, 245, 249));
        t.setSelectionBackground(new Color(219, 234, 254));
        t.setSelectionForeground(TEXT_DK);
        t.setFillsViewportHeight(true);
        JTableHeader h = t.getTableHeader();
        h.setFont(new Font("Segoe UI", Font.BOLD, 13));
        h.setBackground(new Color(248, 250, 252));
        h.setForeground(TEXT_MD);
        h.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(226, 232, 240)));
        h.setPreferredSize(new Dimension(0, 36));
        return t;
    }

    JTextField field() {
        JTextField f = new JTextField();
        f.setFont(F_BODY);
        f.setPreferredSize(new Dimension(260, 38));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        return f;
    }

    void addRow(JPanel p, GridBagConstraints gc, int row, String label, JComponent comp) {
        JLabel l = new JLabel(label);
        l.setFont(F_BODY); l.setForeground(TEXT_MD);
        gc.gridx = 0; gc.gridy = row; gc.gridwidth = 1; gc.weightx = 0; p.add(l, gc);
        gc.gridx = 1; gc.weightx = 1; p.add(comp, gc);
    }

    JButton actionBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(180, 40));
        return b;
    }
}
