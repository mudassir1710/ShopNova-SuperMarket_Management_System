import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class AdminFrame extends JFrame {

    // ── Palette ─────────────────────────────────────────────────────
    // the colors and fonts used throughout the UI for a consistent look (defined as
    // constants for uniform look)
    static final Color SIDEBAR = new Color(15, 23, 42);
    static final Color ACCENT = new Color(59, 130, 246);
    static final Color CARD_BG = Color.WHITE;
    static final Color PAGE_BG = new Color(241, 245, 249);
    static final Color TEXT_DK = new Color(15, 23, 42);
    static final Color TEXT_MD = new Color(71, 85, 105);
    static final Color SUCCESS = new Color(22, 163, 74);
    static final Color DANGER = new Color(220, 38, 38);
    static final Color AMBER = new Color(202, 138, 4);

    static final Font F_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    static final Font F_SECT = new Font("Segoe UI", Font.BOLD, 15);
    static final Font F_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    static final Font F_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    static final Font F_NAV = new Font("Segoe UI", Font.BOLD, 14);
    static final Font F_STAT = new Font("Segoe UI", Font.BOLD, 28);

    // external data from the inventory and cashier manager to load data into tables
    Inventory inventory;
    CashierManager cashierManager;

    // the main content panel
    JPanel content; // every page (dashboard, inventory ) will appear inside this panel when their
                    // corresponding nav button is clicked
    JButton activeNavBtn; // keeps track of which nav button is currently active

    // Constructors of the Admin frame
    public AdminFrame(Inventory inventory, CashierManager cashierManager) {
        // references to the Inventory and cashier to display their data
        this.inventory = inventory;
        this.cashierManager = cashierManager;

        setTitle("Store Management — Admin");// Main title
        setDefaultCloseOperation(EXIT_ON_CLOSE);// close entire app when this window is closed
        setSize(1050, 660); // Size of the admin window
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel root = new JPanel(new BorderLayout()); // the root panel that holds the sidebar and the main content area
        root.setBackground(PAGE_BG); // light gray background for the whole admin area

        root.add(buildSidebar(), BorderLayout.WEST); // the sidebar is built by the buildSidebar() method and placed on
                                                     // the left side of the root panel

        content = new JPanel(new BorderLayout()); // the main content panel
        content.setBackground(PAGE_BG); // same light gray background to blend with the root panel
        root.add(content, BorderLayout.CENTER); // adding content and aligning the content panel to the center of the
                                                // root panel

        add(root); // attaching the root panel to the JFrame
        showDashboard(); // showing the dashboard view
        setVisible(true); // setting true to make it visible when the admin frame is created
    }

    // ════════════════════════════════════════════════════════════════
    // Making Sidebar
    // ════════════════════════════════════════════════════════════════
    JPanel buildSidebar() {
        JPanel sb = new JPanel(); // the sidebar panel that will hold the logo and navigation buttons
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS)); // using BoxLayout to stack components vertically in the
                                                           // sidebar
        sb.setBackground(SIDEBAR);// dark blue background for the sidebar
        sb.setPreferredSize(new Dimension(210, 0)); // Setting dimensions
        sb.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));// no padding around the sidebar

        // Logo area
        JLabel logo = new JLabel(" Shop Nova"); // main logo
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));// setto font and size of the logo
        logo.setForeground(Color.WHITE);// white color for the logo text
        logo.setAlignmentX(Component.LEFT_ALIGNMENT); // aligning logo to the left of the sidebar
        logo.setBorder(BorderFactory.createEmptyBorder(28, 18, 28, 0)); // setting non visible border
        sb.add(logo);

        // Divider below the logo to separate it from the navigation buttons.
        sb.add(divider());

        // Nav buttons. Here the first parameter is the label of the button and the
        // second parameter is a key that identifies which view to show when the button
        // is clicked.
        sb.add(navBtn("  Dashboard", "dash"));
        sb.add(navBtn("  Inventory", "inv"));
        sb.add(navBtn("  Add Product", "addprod"));
        sb.add(navBtn("  Cashiers", "cashiers"));
        sb.add(navBtn("  Add Cashier", "addcash"));

        sb.add(Box.createVerticalGlue()); // this pushes the logout button to the bottom of the sidebar
        sb.add(divider()); // adding another divider above the logout button to separate it from the rest
                           // of the nav buttons

        // Logout Button
        JButton logout = navBtn("  Logout", "logout");
        logout.setForeground(new Color(252, 165, 165)); // red color for the logout button
        sb.add(logout);
        sb.add(Box.createVerticalStrut(12)); // adding some space at the bottom of the sidebar

        return sb;
    }

    // method to create navigation buttons with consistent styling and
    // behavior.
    JButton navBtn(String label, String key) {
        JButton b = new JButton(label);
        // styling the button
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
        // adding action listener to handle button clicks and show the corresponding
        // view based on the key
        b.addActionListener(e -> {
            setActive(b);// set this button as active
            // switch case to find what to display
            switch (key) {

                case "dash" -> showDashboard();
                case "inv" -> showInventory();
                case "addprod" -> showAddProduct();
                case "cashiers" -> showCashiers();
                case "addcash" -> showAddCashier();
                case "logout" -> {
                    new LoginFrame();
                    dispose();
                }
            }
        });
        return b;
    }

    void setActive(JButton b) {
        // re setting the previously active button
        if (activeNavBtn != null) {
            activeNavBtn.setBackground(SIDEBAR);
            activeNavBtn.setForeground(new Color(148, 163, 184));
        }
        b.setBackground(new Color(30, 58, 95)); // darker blue background for the active button
        b.setForeground(Color.WHITE); // white text for the active button
        activeNavBtn = b; // update the active button reference
    }

    // method to create a divider.
    JSeparator divider() {
        JSeparator s = new JSeparator();
        s.setForeground(new Color(30, 41, 59)); // dark blue color for the divider line to blend with the sidebar
                                                // background
        s.setBackground(new Color(30, 41, 59)); // dark blue color for the divider line to blend with the sidebar
                                                // background
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1)); // setting dimension
        return s;
    }

    // ===========================VIEWS================================
    //

    void swap(JPanel panel) {// Swap method to switch between different views in the content panel when nav
                             // buttons are clicked.
        content.removeAll();// remove existing view from the content panel
        content.add(panel, BorderLayout.CENTER); // add the new view to the content panel and align it to the center
        content.revalidate(); // refresh the content panel and show the new view
        content.repaint();
    }

    // ── Dashboard ───────────────────────────────────────────────────
    void showDashboard() {
        JPanel p = new JPanel(new BorderLayout()); // the main panel for the dashboard view
        p.setBackground(PAGE_BG); // light gray background to blend with the root panel
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));// padding around the dashboard content

        p.add(pageHeader("Dashboard", "Welcome back, Admin"), BorderLayout.NORTH);// adding header to the top of the
                                                                                  // dashboard. The pageHeader method
                                                                                  // creates a consistent header with a
                                                                                  // title and subtitle.

        // Stats cards at the top of the dashboard to show values like total products,
        // low
        // stock items, expired items, and number of cashiers. The statCard method
        // creates a styled card for each metric.
        int totalProd = inventory.list.size(); // total number of products in the inventory
        int lowStock = (int) inventory.list.stream().filter(x -> x.qty <= 5).count(); // number of products with low
                                                                                      // stock
        int expired = (int) inventory.list.stream().filter(x -> x.getStatus().equals("EXPIRED")).count(); // number of
                                                                                                          // expired
                                                                                                          // products
        int cashierCnt = cashierManager.list.size(); // number of cashiers

        JPanel cards = new JPanel(new GridLayout(1, 4, 18, 0));
        cards.setOpaque(false);
        cards.setBorder(BorderFactory.createEmptyBorder(24, 0, 0, 0));
        cards.add(statCard("Products", String.valueOf(totalProd), ACCENT)); // total products card with blue accent
        cards.add(statCard("Low Stock", String.valueOf(lowStock), AMBER)); // low stock items card with amber accent
        cards.add(statCard("Expired", String.valueOf(expired), DANGER)); // expired items card with red accent
        cards.add(statCard("Cashiers", String.valueOf(cashierCnt), SUCCESS)); // cashiers card with green accent
        p.add(cards, BorderLayout.CENTER); // adding the stats cards to the center of the dashboard

        // Recent inventory table at the bottom of the dashboard to show a snapshot of
        // the products in stock. The buildInventoryTable method creates a styled table
        // with product data.
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);// transparent background to blend with the dashboard background
        bottom.setBorder(BorderFactory.createEmptyBorder(28, 0, 0, 0));// padding at the top to separate it from the
                                                                       // stats cards

        JLabel recLbl = new JLabel("Recent Inventory"); // label for the recent inventory section
        recLbl.setFont(F_SECT);
        recLbl.setForeground(TEXT_DK);
        recLbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        bottom.add(recLbl, BorderLayout.NORTH);
        bottom.add(buildInventoryTable(), BorderLayout.CENTER);// adding the inventory table(through Method) to the
                                                               // center of the bottom panel
        p.add(bottom, BorderLayout.SOUTH);

        swap(p);// swapping the entire dashboard panel into the content area to display it
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

    // ── Show Inventory Method ───────────────────────────────────────────────────
    void showInventory() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(PAGE_BG);
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        p.add(pageHeader("Inventory", "All products in stock"), BorderLayout.NORTH);

        String[] cols = { "ID", "Name", "Qty", "Category", "Expiry", "Price", "Status" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        refreshInventoryModel(model);

        JTable table = styledTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Color.WHITE);

        JPanel card = wrapInCard(sp);
        p.add(card, BorderLayout.CENTER);

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
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a product first.");
                return;
            }
            Product pr = inventory.list.get(row);
            JTextField nameF = new JTextField(pr.name);
            JTextField qtyF = new JTextField(String.valueOf(pr.qty));
            JTextField catF = new JTextField(pr.category);
            JTextField expF = new JTextField(pr.expiry.toString());
            JTextField priceF = new JTextField(String.valueOf(pr.price));

            JPanel dlg = new JPanel(new GridLayout(10, 1, 4, 8));
            dlg.add(new JLabel("Name:"));
            dlg.add(nameF);
            dlg.add(new JLabel("Quantity:"));
            dlg.add(qtyF);
            dlg.add(new JLabel("Category:"));
            dlg.add(catF);
            dlg.add(new JLabel("Expiry (YYYY-MM-DD):"));
            dlg.add(expF);
            dlg.add(new JLabel("Price:"));
            dlg.add(priceF);

            int res = JOptionPane.showConfirmDialog(this, dlg, "Update Product", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    String nm = nameF.getText().trim();
                    int qt = Integer.parseInt(qtyF.getText().trim());
                    String ct = catF.getText().trim();
                    String ex = expF.getText().trim();
                    double price = Double.parseDouble(priceF.getText().trim());
                    if (nm.isEmpty() || ct.isEmpty() || ex.isEmpty())
                        throw new Exception();

                    pr.name = nm;
                    pr.qty = qt;
                    pr.category = ct;
                    pr.expiry = java.time.LocalDate.parse(ex);
                    pr.price = price;

                    inventory.save();
                    refreshInventoryModel(model);
                } catch (Exception ex2) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields correctly.");
                }
            }
        });

        removeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a product first.");
                return;
            }
            String name = inventory.list.get(row).name;
            if (JOptionPane.showConfirmDialog(this, "Remove '" + name + "'?", "Confirm",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                inventory.remove(inventory.list.get(row).id);
                refreshInventoryModel(model);
            }
        });

        swap(p);
    }

    // _________ Method to Build Table for
    // inventory-----------------------------------
    JScrollPane buildInventoryTable() {
        String[] cols = { "ID", "Name", "Qty", "Category", "Expiry", "Price", "Status" };// array of string column names
                                                                                         // for the inventory table
        DefaultTableModel model = new DefaultTableModel(cols, 0);// Default method to create table
        // adding products from the inventory to the table model.
        for (Product pr : inventory.list)
            model.addRow(new Object[] { pr.id, pr.name, pr.qty, pr.category, pr.expiry,
                    String.format("$%.2f", pr.price), pr.getStatus() });

        JTable table = styledTable(model);
        // Color status column
        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                String s = v == null ? "" : v.toString();
                l.setForeground(s.equals("EXPIRED") ? DANGER : s.equals("LOW STOCK") ? AMBER : SUCCESS);
                l.setFont(new Font("Segoe UI", Font.BOLD, 13));
                return l;
            }
        });
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Color.WHITE);
        return sp;
    }

    // ── Add Product ─────────────────────────────────────────────────
    void showAddProduct() {
        JPanel p = new JPanel(new BorderLayout());// the main panel for the add product view
        p.setBackground(PAGE_BG);
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));// Borders around the add product content
        p.add(pageHeader("Add Product", "Add a new product to inventory"), BorderLayout.NORTH);// adding header to the
                                                                                               // top of the add product
                                                                                               // view

        JPanel card = new JPanel(new GridBagLayout()); // using GridBagLayout for the form layout inside the card
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                // createCompoundBorder method is used to combine two borders into one.
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 8, 8, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;
        // text fields for the product attributes in the add product form
        JTextField nameF = field();
        JTextField qtyF = field();
        JTextField catF = field();
        JTextField expF = field();
        JTextField priceF = field();
        // adding rows to the form using the addRow method for consistent styling and
        // layout
        addRow(card, gc, 0, "Product Name", nameF);
        addRow(card, gc, 1, "Quantity", qtyF);
        addRow(card, gc, 2, "Category", catF);
        addRow(card, gc, 3, "Expiry (YYYY-MM-DD)", expF);
        addRow(card, gc, 4, "Price", priceF);
        // Save button to add the new product to the inventory when clicked
        JButton saveBtn = actionBtn("Add Product", ACCENT); // Accent color for the save button
        // Positioning the save button in the form using GridBagConstraints
        gc.gridx = 0;
        gc.gridy = 5;
        gc.gridwidth = 2;
        gc.insets = new Insets(20, 8, 8, 8);// more space above the button
        card.add(saveBtn, gc); // adding the save button to the form
        // Saving the new product to the inventory when the save button is clicked.
        // It also includes basic validation to ensure all fields are filled correctly
        // shows appropriate messages based on the success or failure of the operation.
        saveBtn.addActionListener(e -> {
            try {
                String nm = nameF.getText().trim();
                int qt = Integer.parseInt(qtyF.getText().trim());
                String ct = catF.getText().trim();
                String ex = expF.getText().trim();
                double pr = Double.parseDouble(priceF.getText().trim());
                if (nm.isEmpty() || ct.isEmpty() || ex.isEmpty())
                    throw new Exception();
                inventory.addProduct(new Product(inventory.idCounter++, nm, qt, ct, ex, pr));
                JOptionPane.showMessageDialog(this, "Product added successfully!");
                nameF.setText("");
                qtyF.setText("");
                catF.setText("");
                expF.setText("");
                priceF.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please fill all fields correctly.");
            }
        });
        // Wrapping the form in a card and adding it to the center of the add product
        // view
        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrap.setOpaque(false);
        wrap.add(card);
        p.add(wrap, BorderLayout.CENTER);
        swap(p);
    }

    // Show Cashiers ────────────────────────────────────────────────────
    void showCashiers() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(PAGE_BG);
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        p.add(pageHeader("Cashiers", "Manage cashier accounts"), BorderLayout.NORTH);

        String[] cols = { "#", "Username" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        refreshCashierModel(model);// loading cashiers from the cashier manager into the table model

        JTable table = styledTable(model);// creating a styled table with the cashier data
        JScrollPane sp = new JScrollPane(table);// adding the table to a scroll pane so that it must be scrolled if
                                                // there are many cashiers
        sp.setBorder(BorderFactory.createEmptyBorder());// no border for the scroll pane

        JPanel card = wrapInCard(sp);// wrapping the table in a styled card
        p.add(card, BorderLayout.CENTER);// adding and aligning the card to the center of the cashiers view

        // Action bar below table with buttons to update or remove the selected cashier.
        // The actionBtn method creates styled buttons.
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
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a cashier first.");
                return;
            }
            Cashier c = cashierManager.list.get(row);
            JTextField uF = new JTextField(c.username);
            JPasswordField pF = new JPasswordField(c.password);
            JPanel dlg = new JPanel(new GridLayout(4, 1, 4, 8));
            dlg.add(new JLabel("New Username:"));// label for the username field in the update dialog
            dlg.add(uF);// text field for the new username in the update dialog
            dlg.add(new JLabel("New Password:"));// label for the password field in the update dialog
            dlg.add(pF);// password field for the new password in the update dialog
            int res = JOptionPane.showConfirmDialog(this, dlg, "Update Cashier", JOptionPane.OK_CANCEL_OPTION);// showing
                                                                                                               // a
                                                                                                               // confirmation
                                                                                                               // dialog
                                                                                                               // with
                                                                                                               // the
                                                                                                               // update
                                                                                                               // form
                                                                                                               // and
                                                                                                               // capturing
                                                                                                               // the
                                                                                                               // user's
                                                                                                               // response
            // If the user clicks OK, update the cashier's username and password with the
            // new values from the form, save the changes to the cashier manager, and
            // refresh the cashier table to reflect the updates.
            if (res == JOptionPane.OK_OPTION) {
                String u = uF.getText().trim(), pw = new String(pF.getPassword()).trim();
                if (u.isEmpty() || pw.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Fields cannot be empty.");
                    return;
                }
                c.username = u;
                c.password = pw;
                cashierManager.save();// save changes to the cashier manager
                refreshCashierModel(model);
            }
        });

        removeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            // If no row is selected, show a message prompting the user to select a cashier
            // first.
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a cashier first.");
                return;
            }
            String name = cashierManager.list.get(row).username;// get the username of the selected cashier to show in
                                                                // the confirmation dialog
            // Show a confirmation dialog asking the user to confirm the removal of the
            // selected cashier. If the user confirms, remove the cashier from the cashier
            // manager and refresh the cashier table to reflect the changes.
            if (JOptionPane.showConfirmDialog(this, "Remove '" + name + "'?", "Confirm",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                cashierManager.remove(name);
                refreshCashierModel(model);
            }
        });

        swap(p);// swapping the entire cashiers panel into the content area to display it
    }

    // Refresh method to reload the cashier data into the table model after updates
    // or removals.
    void refreshCashierModel(DefaultTableModel m) {
        m.setRowCount(0);
        int i = 1;
        for (Cashier c : cashierManager.list)
            m.addRow(new Object[] { i++, c.username });
    }

    // refresh inventory
    void refreshInventoryModel(DefaultTableModel m) {
        m.setRowCount(0);
        for (Product pr : inventory.list)
            m.addRow(new Object[] { pr.id, pr.name, pr.qty, pr.category,
                    pr.expiry, String.format("$%.2f", pr.price), pr.getStatus() });
    }

    // ── Add Cashier ─────────────────────────────────────────────────
    void showAddCashier() {
        JPanel p = new JPanel(new BorderLayout()); // the main panel for the add cashier view
        p.setBackground(PAGE_BG);
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        p.add(pageHeader("Add Cashier", "Create a new cashier account"), BorderLayout.NORTH);// adding header to the top
                                                                                             // of the add cashier view
        // gridbag layout for the add cashier form to align labels and fields in a grid
        // as in the add product form
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 8, 8, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;

        JTextField uF = field();
        JPasswordField pF = new JPasswordField();
        pF.setFont(F_BODY);
        pF.setPreferredSize(new Dimension(260, 38));

        addRow(card, gc, 0, "Username", uF);

        JLabel pl = new JLabel("Password");
        pl.setFont(F_BODY);
        pl.setForeground(TEXT_MD);
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.weightx = 0;
        card.add(pl, gc);
        gc.gridx = 1;
        gc.weightx = 1;
        card.add(pF, gc);

        JButton saveBtn = actionBtn("Add Cashier", ACCENT);
        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = 2;
        gc.insets = new Insets(20, 8, 8, 8);
        card.add(saveBtn, gc);

        saveBtn.addActionListener(e -> {
            String u = uF.getText().trim(), pw = new String(pF.getPassword()).trim();
            if (u.isEmpty() || pw.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }
            cashierManager.add(new Cashier(u, pw));
            JOptionPane.showMessageDialog(this, "Cashier added successfully!");
            uF.setText("");
            pF.setText("");
        });

        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrap.setOpaque(false);
        wrap.add(card);
        p.add(wrap, BorderLayout.CENTER);
        swap(p);
    }

    // ================================================================================
    // SHARED Methods for Consistent Graphics ||
    // ================================================================================

    // Method making header and sub header for each page
    JPanel pageHeader(String title, String sub) {
        JPanel h = new JPanel(new GridLayout(2, 1, 0, 4));// using GridLayout to stack the title and subtitle vertically
                                                          // with some space in between
        h.setOpaque(false);// transparent background to blend with the page background
        h.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));// border at the bottom to separate the header from the
                                                                 // rest of the content
        JLabel t = new JLabel(title);// label for the page title
        t.setFont(F_TITLE);
        t.setForeground(TEXT_DK);
        JLabel s = new JLabel(sub);// label for the page subtitle
        s.setFont(F_BODY);
        s.setForeground(TEXT_MD);
        // adding the title and subtitle to the header panel
        h.add(t);
        h.add(s);
        return h;
    }

    // Method to create a styled card panel
    JPanel wrapInCard(Component inner) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        // adding a border to the card with a light gray line
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        card.add(inner, BorderLayout.CENTER);
        return card;
    }

    // Styling table method to create uniform tables
    JTable styledTable(DefaultTableModel model) {
        JTable t = new JTable(model) {
            public boolean isCellEditable(int r, int c) {// making all cells non editable(iseditable is a method in
                                                         // JTable that can be overridden to specify which cells are
                                                         // editable. Here we return false for all cells to make the
                                                         // entire table non editable)
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
    // Method to add a label and a component (like a text field) as a row

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

    // Creating styled action button
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
