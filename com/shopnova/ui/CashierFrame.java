package com.shopnova.ui;

import com.shopnova.manager.*;
import com.shopnova.model.*;
import com.shopnova.util.ReceiptPrinter;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;

public class CashierFrame extends JFrame {

    // Colours
    static final Color SIDEBAR = new Color(15, 23, 42);
    static final Color ACCENT = new Color(59, 130, 246);
    static final Color CARD_BG = Color.WHITE;
    static final Color PAGE_BG = new Color(241, 245, 249);
    static final Color TEXT_DK = new Color(15, 23, 42);
    static final Color TEXT_MD = new Color(71, 85, 105);
    static final Color SUCCESS = new Color(22, 163, 74);
    static final Color DANGER = new Color(220, 38, 38);
    static final Color AMBER = new Color(202, 138, 4);
    static final Color ORANGE = new Color(194, 65, 12);

    // Fonts
    static final Font F_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    static final Font F_SECT = new Font("Segoe UI", Font.BOLD, 15);
    static final Font F_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    static final Font F_NAV = new Font("Segoe UI", Font.BOLD, 14);
    static final Font F_STAT = new Font("Segoe UI", Font.BOLD, 28);
    static final Font F_MONO = new Font("Monospaced", Font.PLAIN, 13);

    // Aggregation of cashier and inventory
    Cashier cashier;
    Inventory inventory;
    JPanel content;
    JButton activeNavBtn;

    // Constructor

    public CashierFrame(Cashier cashier, Inventory inventory) {
        this.cashier = cashier;
        this.inventory = inventory;

        setTitle("ShopNova — Cashier: " + cashier.username); // title with cashier's username
        setDefaultCloseOperation(EXIT_ON_CLOSE);// when the user clicks the X button, the whole program ends
        setSize(1050, 660);
        setLocationRelativeTo(null);// center on screen
        setResizable(true);// allow maximizing

        // A root JPanel is created
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE_BG);
        root.add(buildSidebar(), BorderLayout.WEST); // sidebar on the west side (left)
        // Content panel is created that displays the current screen (dashboard,
        // inventory, etc)
        content = new JPanel(new BorderLayout());
        content.setBackground(PAGE_BG);
        root.add(content, BorderLayout.CENTER);
        // both side bar adn content are added to the root panel and this panel is added
        // to the frame using add(root)
        add(root);
        // loading the page
        showDashboard();
        // making frame visible
        setVisible(true);
    }

    // Method to Build side bar

    JPanel buildSidebar() {
        JPanel sb = new JPanel();
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS)); // BoxLayout with Y_AXIS means every line is like a column.
        sb.setBackground(SIDEBAR);
        sb.setPreferredSize(new Dimension(210, 0));

        // two lbels are added
        // label # 1

        JLabel logo = new JLabel("Shop Nova  ");// showing logo

        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        logo.setBorder(BorderFactory.createEmptyBorder(28, 18, 10, 0));
        sb.add(logo);// adding logo label to the sidebar

        // label # 2

        JLabel role = new JLabel("  Cashier: " + cashier.username);// showing the role of logged in
        role.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        role.setForeground(new Color(100, 116, 139));
        role.setAlignmentX(Component.LEFT_ALIGNMENT);
        role.setBorder(BorderFactory.createEmptyBorder(0, 18, 20, 0));
        sb.add(role);// adding role label to the sidebar

        // Divider line to separate the header from the navigation buttons

        sb.add(divider());

        // Navigation buttons(each has a label and a key that is used to identify which
        // page to show when clicked)
        sb.add(navBtn("  Dashboard", "dash"));
        sb.add(navBtn("  Inventory", "inv"));
        sb.add(navBtn("  Add to Cart", "addcart"));
        sb.add(navBtn("  View Cart", "cart"));
        sb.add(navBtn("  Checkout", "checkout"));
        // Vertical glue is added (spring to keep remaining things at the bottom)
        sb.add(Box.createVerticalGlue());
        //// divider to separate the logout button from everything above
        sb.add(divider());
        // making logout button
        JButton logout = navBtn("  Logout", "logout");
        logout.setForeground(new Color(252, 165, 165));
        sb.add(logout);
        // vertical strut is added to give some space at the bottom
        sb.add(Box.createVerticalStrut(12));
        return sb;
    }

    // Method to create the buttons

    JButton navBtn(String label, String key) {// have name and key(identifying action)
        JButton b = new JButton(label);
        // designing the button using AWT
        b.setFont(F_NAV);
        b.setForeground(new Color(148, 163, 184));
        b.setBackground(SIDEBAR);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setContentAreaFilled(true);// ?
        b.setHorizontalAlignment(SwingConstants.LEFT);
        // full width, 46px tall
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        // text aligned to the left
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        // hover effect
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // non vivisble border
        b.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        // Action listener through lambda
        b.addActionListener(e -> {// explain what is e?
            // highlights the button and removes the highlight from previous.
            setActive(b);
            // switch case to determine which page to show based on the key
            switch (key) {
                case "dash" -> showDashboard();
                case "inv" -> showInventory();
                case "addcart" -> showAddToCart();
                case "cart" -> showCart();
                case "checkout" -> showCheckout();
                case "logout" -> {
                    cashier.cart.clear();
                    new LoginFrame();
                    dispose();// close the current frame
                }
            }
        });
        return b;
    }

    void saveCashierSales() {
        try (java.io.FileWriter fw = new java.io.FileWriter("cashiers.txt")) {
            fw.write(cashier.username + "," + cashier.password + "," + cashier.totalSales + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------Set Active Method(basically highlights the active button)-------
    void setActive(JButton b) {
        if (activeNavBtn != null) {
            activeNavBtn.setBackground(SIDEBAR);
            activeNavBtn.setForeground(new Color(148, 163, 184));
        }
        b.setBackground(new Color(30, 58, 95));
        b.setForeground(Color.WHITE);
        activeNavBtn = b;
    }

    // building driver method for divider (JSeparator) to be used in the sidebar
    JSeparator divider() {
        JSeparator s = new JSeparator();
        s.setForeground(new Color(30, 41, 59));
        s.setBackground(new Color(30, 41, 59));
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return s;
    }

    // method to swap cuurent page eith new page
    void swap(JPanel panel) {
        // emoves every component currently inside the content panel
        content.removeAll();
        // places the new page in the center of the content panel
        content.add(panel, BorderLayout.CENTER);

        content.revalidate();// tells the layout manager to recalculate all the sizes and positions
        content.repaint();// redraws the panel on screen so the user actually sees the change
    }

    // Create Main Dashboard called once from the constructor when the window
    // first opens and at the end of showCheckout() after a sale is completed to
    // bring the cashier back to the home screen.)

    void showDashboard() {
        JPanel p = new JPanel(new BorderLayout());// Main panel for dashboard
        p.setBackground(PAGE_BG);
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // adding Header at top (build using pageHeader method that takes title and
        // subtitle as parameters)

        p.add(pageHeader("Dashboard", "Hello, " + cashier.username + " — Here's Your overview: "), BorderLayout.NORTH);

        double cartTotal = cashier.cart.stream().mapToDouble(CartItem::total).sum();

        // Creating 3 statistic cards in the center using a GridLayout of g panel

        JPanel cards = new JPanel(new GridLayout(1, 4, 18, 0));
        // Designing the cards panel using AWT
        cards.setOpaque(false);
        cards.setBorder(BorderFactory.createEmptyBorder(24, 0, 0, 0));

        // Adding statistic cards to the cards panel using the statCard method that
        // takes a label, value, and accent color as parameters
        cards.add(statCard("Cart Items", String.valueOf(cashier.cart.size()), ACCENT));
        cards.add(statCard("Cart Total", String.format("Rs %.2f", cartTotal), SUCCESS));
        cards.add(statCard("In Stock", String.valueOf(inventory.getProducts().size()) + " products", AMBER));
        cards.add(statCard("My Sales", String.format("Rs %.2f", cashier.totalSales), new Color(124, 58, 237)));
        // Adding the cards panel to the main dashboard panel in the center
        p.add(cards, BorderLayout.CENTER);

        // Mini cart summary at the bottom
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(28, 0, 0, 0));
        JLabel cl = new JLabel("Current Cart");
        cl.setFont(F_SECT);
        cl.setForeground(TEXT_DK);
        cl.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        // aading label and cart table to bottom panel
        bottom.add(cl, BorderLayout.NORTH);
        bottom.add(buildCartTable(), BorderLayout.CENTER);
        // adding the bottom panel to the main dashboard panel at the south position
        p.add(bottom, BorderLayout.SOUTH);
        // called swap to display this page in the content area
        swap(p);
    }

    // Mthod to make statcard
    // called three times in the dashboard to make the three statistic cards
    // takes a label, value, and accent color as parameters
    JPanel statCard(String label, String value, Color accent) {
        // panel with white Bg and compound border (line border and empty border )
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)), // outer gray line and and inner empty border
                BorderFactory.createEmptyBorder(22, 22, 22, 22)));

        JLabel bar = new JLabel();
        bar.setOpaque(true);// to show the background color(default for JLabel is false)
        bar.setBackground(accent);
        bar.setPreferredSize(new Dimension(4, 0));
        card.add(bar, BorderLayout.WEST);// added bar to left of each card
        // new panel to to show value and label of stat card into row format using grid
        // layout
        JPanel txt = new JPanel(new GridLayout(2, 1, 0, 6));
        txt.setOpaque(false);// to show the background of the card instead of txt panel
        txt.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 0));// to give some space between the bar and the text
        // value label and design
        JLabel valLbl = new JLabel(value);
        valLbl.setFont(F_STAT);
        valLbl.setForeground(accent);
        // name label and design
        JLabel namLbl = new JLabel(label);
        namLbl.setFont(F_BODY);
        namLbl.setForeground(TEXT_MD);
        // adding value and label to the txt panel
        txt.add(valLbl);
        txt.add(namLbl);
        // adding the txt panel to the center of the card panel
        card.add(txt, BorderLayout.CENTER);
        return card;
    }

    // ── Inventory ───────────────────────────────────────────────────
    void showInventory() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(PAGE_BG);
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        // add header
        p.add(pageHeader("Inventory", "Browse available products"), BorderLayout.NORTH);
        // Adding inventry table wrpped in card and plcing in center, why wrapped in
        // card? to give it a white background and border to make it look like a card
        // and to separate it from the page background
        p.add(wrapInCard(buildInventoryTable()), BorderLayout.CENTER);
        swap(p);
    }

    JScrollPane buildInventoryTable() {
        // Defining column names
        String[] cols = { "ID", "Name", "Qty", "Category", "Expiry", "Price", "Status" };
        // creating a table model with the column names and 0 rows initially
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        // lloping through every product and adding new row for each product in list
        for (Product pr : inventory.getProducts())
            model.addRow(new Object[] { pr.id, pr.name, pr.qty, pr.category, pr.expiry,
                    String.format("Rs %.2f", pr.price), pr.getStatus() });
        // passing to style table method for designing
        JTable t = styledTable(model);
        // ?the Status column (column index 6) gets special treatment — a custom cell
        // renderer is attached to it. This renderer is an anonymous class that extends
        // DefaultTableCellRenderer and overrides getTableCellRendererComponent(). Every
        // time the table renders a cell in column 6, it calls this method. It first
        // calls super to get the default JLabel that would normally render the cell,
        // then it checks the status string and colors the label red for EXPIRED, amber
        // for LOW STOCK, and green for anything else.
        t.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable tb, Object v,
                    boolean sel, boolean foc, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(tb, v, sel, foc, r, c);
                String s = v == null ? "" : v.toString();
                l.setForeground(s.equals("EXPIRED") ? DANGER : s.equals("LOW STOCK") ? AMBER : SUCCESS);
                l.setFont(new Font("Segoe UI", Font.BOLD, 13));
                return l;
            }
        });
        // wrapping in scroll pane
        JScrollPane sp = new JScrollPane(t);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Color.WHITE);
        return sp;
    }

    // ── Add To Cart ─────────────────────────────────────────────────
    void showAddToCart() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(PAGE_BG);
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        p.add(pageHeader("Add to Cart", "Click a row then enter quantity"), BorderLayout.NORTH);

        String[] cols = { "ID", "Name", "Qty", "Category", "Price", "Status" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        for (Product pr : inventory.getProducts())
            model.addRow(new Object[] { pr.id, pr.name, pr.qty, pr.category,
                    String.format("Rs %.2f", pr.price), pr.getStatus() });

        JTable table = styledTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Color.WHITE);
        JPanel tableCard = wrapInCard(sp);

        JTextField idField = field();
        idField.setEditable(false);
        idField.setBackground(new Color(248, 250, 252));
        JTextField qtyField = field();

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0)
                idField.setText(model.getValueAt(table.getSelectedRow(), 0).toString());
        });

        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(CARD_BG);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 8, 6, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;
        addRow(formCard, gc, 0, "Selected Product ID", idField);
        addRow(formCard, gc, 1, "Quantity", qtyField);

        JButton addBtn = actionBtn("Add to Cart", ACCENT);
        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = 2;
        gc.insets = new Insets(16, 8, 8, 8);
        formCard.add(addBtn, gc);

        // ★ CORRECTION: completely rewritten action listener
        addBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                int qty = Integer.parseInt(qtyField.getText().trim());
                if (qty <= 0) {
                    JOptionPane.showMessageDialog(this, "Quantity must be > 0.");
                    return;
                }
                Product pr = inventory.findById(id);
                if (pr == null) {
                    JOptionPane.showMessageDialog(this, "Product not found.");
                    return;
                }
                if (pr.getStatus().equals("EXPIRED")) {
                    JOptionPane.showMessageDialog(this, "Cannot add expired product.");
                    return;
                }

                // Check how many are already reserved in the cart
                int alreadyInCart = 0;
                for (CartItem ci : cashier.cart) {
                    if (ci.product.id == pr.id)
                        alreadyInCart += ci.qty;
                }
                if (pr.qty < alreadyInCart + qty) {
                    JOptionPane.showMessageDialog(this,
                            "Not enough stock. Available: " + (pr.qty - alreadyInCart));
                    return;
                }

                // Merge with existing item if present
                boolean merged = false;
                for (CartItem ci : cashier.cart) {
                    if (ci.product.id == pr.id) {
                        ci.qty += qty;
                        merged = true;
                        break;
                    }
                }
                if (!merged) {
                    cashier.cart.add(new CartItem(pr.copy(), qty)); // snapshot copy
                }

                // DO NOT reduce pr.qty – stock stays untouched until checkout.
                // Refresh the table (stock stays the same, only status may change visually)
                for (int row = 0; row < model.getRowCount(); row++)
                    if (Integer.parseInt(model.getValueAt(row, 0).toString()) == id) {
                        model.setValueAt(pr.qty, row, 2);
                        model.setValueAt(pr.getStatus(), row, 5);
                    }
                qtyField.setText("");
                idField.setText("");
                table.clearSelection();
                double disc = pr.getAutoDiscount();
                if (disc > 0) {
                    long days = java.time.temporal.ChronoUnit.DAYS.between(java.time.LocalDate.now(), pr.expiry);
                    JOptionPane.showMessageDialog(this,
                            String.format(
                                    "Added! ⚠ Expiry discount: %.0f%% off\nExpires in %d day(s)\nPrice: Rs %.2f → Rs %.2f",
                                    disc * 100, days, pr.price, pr.price * (1 - disc)),
                            "Discount Applied", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Added to cart!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter valid numbers.");
            }
        });

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableCard, formCard);
        split.setDividerLocation(330);
        split.setDividerSize(6);
        split.setBorder(null);
        split.setOpaque(false);
        p.add(split, BorderLayout.CENTER);
        swap(p);
    }

    // ── View Cart ───────────────────────────────────────────────────
    // ── View Cart ★ CORRECTED ★
    // ───────────────────────────────────────────────────
    void showCart() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(PAGE_BG);
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        p.add(pageHeader("View Cart", "Items currently in your cart"), BorderLayout.NORTH);

        String[] cols = { "Product", "Unit Price", "Qty", "Total" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        double[] grand = { 0 };
        for (CartItem ci : cashier.cart) {
            model.addRow(new Object[] { ci.product.name,
                    String.format("Rs %.2f", ci.product.price), ci.qty,
                    String.format("Rs %.2f", ci.total()) });
            grand[0] += ci.total();
        }

        JTable table = styledTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Color.WHITE);
        p.add(wrapInCard(sp), BorderLayout.CENTER);

        JLabel totalLbl = new JLabel("  Grand Total:  " + String.format("Rs %.2f", grand[0]));
        totalLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalLbl.setForeground(TEXT_DK);
        JButton removeBtn = actionBtn("Remove Selected", ORANGE);
        JButton clearBtn = actionBtn("Clear Cart", DANGER);

        // ★ CORRECTION: remove does NOT touch stock – it was never reduced.
        removeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select an item to remove.");
                return;
            }
            CartItem ci = cashier.cart.get(row);
            // stock remains unchanged; just remove the cart line
            cashier.cart.remove(row);
            model.removeRow(row);
            grand[0] = cashier.cart.stream().mapToDouble(CartItem::total).sum();
            totalLbl.setText("  Grand Total:  " + String.format("Rs %.2f", grand[0]));
        });

        // ★ CORRECTION: clear cart without restoring any stock
        clearBtn.addActionListener(e -> {
            if (cashier.cart.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Cart is already empty.");
                return;
            }
            cashier.cart.clear();
            model.setRowCount(0);
            grand[0] = 0;
            totalLbl.setText("  Grand Total:  Rs 0.00");
        });

        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        bar.add(totalLbl, BorderLayout.WEST);
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btns.setOpaque(false);
        btns.add(removeBtn);
        btns.add(clearBtn);
        bar.add(btns, BorderLayout.EAST);
        p.add(bar, BorderLayout.SOUTH);
        swap(p);
    }

    // ── Checkout ────────────────────────────────────────────────────
    void showCheckout() {
        // if cart is empty on checkout page shows message
        if (cashier.cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty. Add products first.");
            return;
        }
        // creating main panel for checkout page
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(PAGE_BG);
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        p.add(pageHeader("Checkout", "Complete the sale"), BorderLayout.NORTH);
        double subtotal = cashier.cart.stream()
            .mapToDouble(ci -> ci.product.price * (1 - ci.product.getAutoDiscount()) * ci.qty)
            .sum();

        // Card panel to hold checkout form, name, subtotal, cash field, confirm button
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 8, 8, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;

        // name field to enter customer's name
        JTextField nameField = field();
        // label to show the subtotal
        JLabel subLbl = new JLabel(String.format("Rs %.2f", subtotal));
        subLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subLbl.setForeground(ACCENT);
        // cash field for cash paid
        JTextField cashField = field();

        addRow(card, gc, 0, "Customer Name", nameField);

        // subtotal row (label only)
        JLabel sl = new JLabel("Subtotal");
        sl.setFont(F_BODY);
        sl.setForeground(TEXT_MD);
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.weightx = 0;
        card.add(sl, gc);
        gc.gridx = 1;
        gc.weightx = 1;
        card.add(subLbl, gc);

        addRow(card, gc, 2, "Cash Paid", cashField);

        // confirm button
        JButton confirmBtn = actionBtn("Confirm & Print Receipt", SUCCESS);
        confirmBtn.setPreferredSize(new Dimension(260, 42));
        gc.gridx = 0;
        gc.gridy = 3;
        gc.gridwidth = 2;
        gc.insets = new Insets(20, 8, 8, 8);
        card.add(confirmBtn, gc);

        // ★ CORRECTION: completely rewritten action listener
        confirmBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter customer name.");
                return;
            }
            double cash;
            try {
                cash = Double.parseDouble(cashField.getText().trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter valid cash amount.");
                return;
            }

            CustomerManager custManager = new CustomerManager();
            Customer customer = custManager.findOrCreate(name);

            int pointsToRedeem = 0;
            if (customer.points > 0) {
                String input = JOptionPane.showInputDialog(this,
                        "Available points: " + customer.points +
                                "\nEnter points to redeem (0-" + customer.points + "):",
                        "0");
                if (input == null)
                    return;
                try {
                    pointsToRedeem = Integer.parseInt(input.trim());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Enter a valid whole number for points.");
                    return;
                }
                if (pointsToRedeem < 0 || pointsToRedeem > customer.points) {
                    JOptionPane.showMessageDialog(this,
                            "Points must be between 0 and " + customer.points + ".");
                    return;
                }
            }

            double discAmt = customer.redeemPoints(pointsToRedeem);
            double total = subtotal - discAmt;
            if (total < 0)
                total = 0;
            double change = cash - total;
            if (change < 0) {
                JOptionPane.showMessageDialog(this,
                        "Insufficient cash. Need Rs " + String.format("%.2f", total));
                return;
            }

            for (CartItem ci : cashier.cart) {
                boolean ok = inventory.reduceStock(ci.product.id, ci.qty);
                if (!ok) {
                    JOptionPane.showMessageDialog(this,
                            "Stock error for " + ci.product.name + ". Sale cancelled.");
                    return; // in a real system you'd rollback previous reductions
                }
            }

            customer.addPurchase(subtotal);
            custManager.saveCustomer(customer);

            int pointsEarned = (int) (subtotal / 100);
            int newBalance = customer.points;

            String receipt = ReceiptPrinter.buildReceiptText(
                    name, cashier.username, cashier.cart, subtotal,
                    pointsToRedeem, discAmt, total, cash, change,
                    pointsEarned, newBalance);

            // Show receipt on screen
            JTextArea receiptArea = new JTextArea(receipt);
            receiptArea.setFont(F_MONO);
            receiptArea.setEditable(false);
            receiptArea.setBackground(new Color(250, 250, 250));
            JScrollPane rsp = new JScrollPane(receiptArea);
            rsp.setPreferredSize(new Dimension(320, 320));
            JOptionPane.showMessageDialog(this, rsp, "Receipt", JOptionPane.PLAIN_MESSAGE);

            // ★ NEW: Offer to save receipt as file
            int saveChoice = JOptionPane.showConfirmDialog(this,
                    "Save receipt as file?", "Save Receipt", JOptionPane.YES_NO_OPTION);
            if (saveChoice == JOptionPane.YES_OPTION) {
                ReceiptPrinter.printToPDF(this, receipt);
            }
            cashier.totalSales += total;
            saveCashierSales();
            cashier.cart.clear();
            showDashboard();
        });

        // wrap in another panel (centered)
        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrap.setOpaque(false);
        wrap.add(card);
        p.add(wrap, BorderLayout.CENTER);
        swap(p);
    }

    // Designing methods (similar as Admin)

    JScrollPane buildCartTable() {
        String[] cols = { "Product", "Qty", "Unit Price", "Total" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        for (CartItem ci : cashier.cart)
            model.addRow(new Object[] { ci.product.name, ci.qty,
                    String.format("Rs %.2f", ci.product.price), String.format("Rs %.2f", ci.total()) });
        JTable t = styledTable(model);
        JScrollPane sp = new JScrollPane(t);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Color.WHITE);
        return sp;
    }

    JPanel pageHeader(String title, String sub) {
        JPanel h = new JPanel(new GridLayout(2, 1, 0, 4));
        h.setOpaque(false);
        h.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        JLabel t = new JLabel(title);
        t.setFont(F_TITLE);
        t.setForeground(TEXT_DK);
        JLabel s = new JLabel(sub);
        s.setFont(F_BODY);
        s.setForeground(TEXT_MD);
        h.add(t);
        h.add(s);
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
        JTable t = new JTable(model) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
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
        l.setFont(F_BODY);
        l.setForeground(TEXT_MD);
        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 1;
        gc.weightx = 0;
        p.add(l, gc);
        gc.gridx = 1;
        gc.weightx = 1;
        p.add(comp, gc);
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
