import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.Scanner;

public class CashierFrame extends JFrame {

    // ======= Designing font and colors using AWT (static to share and final for
    // remaining uniform)====

    // colors====================================
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

    // fonts====================================
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
    JButton activeNavBtn; // Currently highlighted sidebar button

    // Constructor takes a Cashier and Inventory object

    public CashierFrame(Cashier cashier, Inventory inventory) {
        this.cashier = cashier; // aggregation: CashierFrame "has-a" Cashier
        this.inventory = inventory; // aggregation: CashierFrame "has-a" Inventory

        setTitle("Store Management — Cashier: " + cashier.username); // title with cashier's username
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
        // designing the logo using AWT

        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        logo.setBorder(BorderFactory.createEmptyBorder(28, 18, 10, 0));
        sb.add(logo);// adding logo label to the sidebar

        // label # 2

        JLabel role = new JLabel("  Cashier: " + cashier.username);// showing the role of logged in
        // designing the role label using AWT
        role.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        role.setForeground(new Color(100, 116, 139));
        role.setAlignmentX(Component.LEFT_ALIGNMENT);
        role.setBorder(BorderFactory.createEmptyBorder(0, 18, 20, 0));
        sb.add(role);// adding role label to the sidebar

        // Divider (JSeparator) line to separate the header from the navigation buttons

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

    // =============================================================
    // ── Create Main Dashboard(called once from the constructor when the window
    // first opens and at the end of showCheckout() after a sale is completed to
    // bring the cashier back to the home screen.)
    // ───────────────────────────────────────────────────
    // =============================================================
    void showDashboard() {
        JPanel p = new JPanel(new BorderLayout());// Main panel for dashboard
        p.setBackground(PAGE_BG);
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // adding Header at top (build using pageHeader method that takes title and
        // subtitle as parameters)

        p.add(pageHeader("Dashboard", "Hello, " + cashier.username + " — here's your overview"), BorderLayout.NORTH);

        double cartTotal = cashier.cart.stream().mapToDouble(CartItem::total).sum(); // ?

        // Creating 3 statistic cards in the center using a GridLayout of g panel

        JPanel cards = new JPanel(new GridLayout(1, 3, 18, 0));
        // Designing the cards panel using AWT
        cards.setOpaque(false);
        cards.setBorder(BorderFactory.createEmptyBorder(24, 0, 0, 0));

        // Adding statistic cards to the cards panel using the statCard method that
        // takes a label, value, and accent color as parameters
        cards.add(statCard("Cart Items", String.valueOf(cashier.cart.size()), ACCENT));
        cards.add(statCard("Cart Total", String.format("Rs %.2f", cartTotal), SUCCESS));
        cards.add(statCard("In Stock", String.valueOf(inventory.list.size()) + " products", AMBER));

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
        // create paneel
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
        for (Product pr : inventory.list)
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
        JPanel p = new JPanel(new BorderLayout(0, 16));// dividing the page into two sections
        p.setBackground(PAGE_BG);
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        p.add(pageHeader("Add to Cart", "Click a row then enter quantity"), BorderLayout.NORTH);

        // Inventory table (top)
        String[] cols = { "ID", "Name", "Qty", "Category", "Price", "Status" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        for (Product pr : inventory.list)
            model.addRow(new Object[] { pr.id, pr.name, pr.qty, pr.category,
                    String.format("Rs %.2f", pr.price), pr.getStatus() });

        JTable table = styledTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Color.WHITE);

        JPanel tableCard = wrapInCard(sp);

        // Bottom section
        JTextField idField = field();// id field to enter the id of th product to cart
        idField.setEditable(false);
        idField.setBackground(new Color(248, 250, 252));
        JTextField qtyField = field();// to enter the quantity of the product to add to cart

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
                if (pr.qty < qty) {
                    JOptionPane.showMessageDialog(this, "Only " + pr.qty + " in stock.");
                    return;
                }
                cashier.cart.add(new CartItem(pr, qty));
                pr.qty -= qty;
                inventory.save();
                // refresh table row
                for (int row = 0; row < model.getRowCount(); row++)
                    if (Integer.parseInt(model.getValueAt(row, 0).toString()) == id) {
                        model.setValueAt(pr.qty, row, 2);
                        model.setValueAt(pr.getStatus(), row, 5);
                    }
                qtyField.setText("");
                idField.setText("");
                table.clearSelection();
                JOptionPane.showMessageDialog(this, "Added to cart!");
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
    void showCart() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        // design
        p.setBackground(PAGE_BG);
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        p.add(pageHeader("View Cart", "Items currently in your cart"), BorderLayout.NORTH);
        // definig column for cart table
        String[] cols = { "Product", "Unit Price", "Qty", "Total" };
        // table model with column names and 0 rows
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        // accumulating grand total in an array to modify inside lambda
        // Java does not allow lambdas to modify plain local variables so we use an
        // array to hold the grand total which can be modified inside the lambda
        // expressions for the remove and clear buttons later on.
        double[] grand = { 0 };
        // looping through cart itmems in cashiers cart
        for (CartItem ci : cashier.cart) {
            // adding row for each item
            model.addRow(new Object[] { ci.product.name,
                    String.format("Rs %.2f", ci.product.price), ci.qty,
                    String.format("Rs %.2f", ci.total()) });
            grand[0] += ci.total();
        }
        // Diplaying the cart items
        JTable table = styledTable(model);// passing to design
        JScrollPane sp = new JScrollPane(table);// wrapping in scroll panel
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Color.WHITE);
        p.add(wrapInCard(sp), BorderLayout.CENTER);

        // Bottom bar
        // grand total label with design
        JLabel totalLbl = new JLabel("  Grand Total:  " + String.format("Rs %.2f", grand[0]));
        totalLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalLbl.setForeground(TEXT_DK);
        // Remove and Clear buttons with design
        JButton removeBtn = actionBtn("Remove Selected", ORANGE);
        JButton clearBtn = actionBtn("Clear Cart", DANGER);
        // Remove button action performed
        removeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select an item to remove.");
                return;
            }
            CartItem ci = cashier.cart.get(row);
            ci.product.qty += ci.qty;
            inventory.save();
            cashier.cart.remove(row);
            model.removeRow(row);
            grand[0] = cashier.cart.stream().mapToDouble(CartItem::total).sum();
            totalLbl.setText("  Grand Total:  " + String.format("Rs %.2f", grand[0]));
        });
        // Clear button action performed
        clearBtn.addActionListener(e -> {
            if (cashier.cart.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Cart is already empty.");
                return;
            }
            for (CartItem ci : cashier.cart)
                ci.product.qty += ci.qty;
            inventory.save();
            cashier.cart.clear();
            model.setRowCount(0);
            grand[0] = 0;
            totalLbl.setText("  Grand Total:  RS 0.00");
        });
        // building bottom bar
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        bar.add(totalLbl, BorderLayout.WEST);
        // panl to hold buttons and align them to the right
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btns.setOpaque(false);
        // adding buttons to the button panel and button panel to the bottom bar
        btns.add(removeBtn);
        btns.add(clearBtn);
        bar.add(btns, BorderLayout.EAST);
        p.add(bar, BorderLayout.SOUTH);
        swap(p);
    }

    // ── Checkout ────────────────────────────────────────────────────
    void showCheckout() {
        // if cart is empty on checkout page shaows message
        if (cashier.cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty. Add products first.");
            return;
        }
        // creating main panel for checkout page
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(PAGE_BG);
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        p.add(pageHeader("Checkout", "Complete the sale"), BorderLayout.NORTH);

        double subtotal = cashier.cart.stream().mapToDouble(CartItem::total).sum();// method .stream() converts the cart
                                                                                   // list into a stream which allows us
                                                                                   // to perform operations on it.
                                                                                   // mapToDouble(CartItem::total)
                                                                                   // applies the total() method to each
                                                                                   // CartItem in the stream which
                                                                                   // return price multiplied by
                                                                                   // quantity and .sum() adds all those
                                                                                   // double to get subtotal
        // Card panel to hold chekout form , name ,subtotal as well as cash field and
        // confirm button
        JPanel card = new JPanel(new GridBagLayout());// GridBagLayout let us place components at exact row and column
                                                      // positions with precise control over spacing and sizing.
        // designing
        card.setBackground(CARD_BG);// CRD BG holds thw white color through Color.white
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)));

        GridBagConstraints gc = new GridBagConstraints();
        // spaces aorund each component
        gc.insets = new Insets(8, 8, 8, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;// strech components to fill whole space

        // name filed to enter cutomers name
        JTextField nameField = field();
        // lbl subtotal to show the subtotal
        JLabel subLbl = new JLabel(String.format("Rs %.2f", subtotal));
        subLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subLbl.setForeground(ACCENT);
        // cash field to enter the cash paid by customer
        JTextField cashField = field();
        // add row takeing card panel into which we are adding our constraints at first
        // row position with label Customer Name and the name field
        addRow(card, gc, 0, "Customer Name", nameField);

        // Adding subtotal row withot input field just a label to show the subtotal
        // amount
        JLabel sl = new JLabel("Subtotal");
        sl.setFont(F_BODY);
        sl.setForeground(TEXT_MD);
        gc.gridx = 0;// first column
        gc.gridy = 1;// second row
        gc.gridwidth = 1;// one column wide
        gc.weightx = 0;// does not take extra horizontal space
        // adds the subtotal label in column 0and row 1 in card
        card.add(sl, gc);
        // adding the subtotal amount label in column 1, row 1
        gc.gridx = 1;
        gc.weightx = 1;
        card.add(subLbl, gc);

        // adding cash paid row with label and input field using addRow method at row
        // position 2
        addRow(card, gc, 2, "Cash Paid", cashField);
        // confirm button
        JButton confirmBtn = actionBtn("Confirm & Print Receipt", SUCCESS);
        // designing
        confirmBtn.setPreferredSize(new Dimension(260, 42));
        gc.gridx = 0;
        gc.gridy = 3;
        gc.gridwidth = 2;
        gc.insets = new Insets(20, 8, 8, 8);
        // adding confirm button to the card panel at row 3
        card.add(confirmBtn, gc);
        // action performed
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
            // creating customer object to calculate discount and save purchase history
            Customer customer = new Customer(name);

            // Load old total
            double oldTotal = 0;
            try {
                Scanner sc = new Scanner(new File("customers.txt"));
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    if (line.startsWith(name + ",")) {
                        oldTotal = Double.parseDouble(line.split(",")[1].trim());
                        break;
                    }
                }
                sc.close();
            } catch (Exception ignored) {
            }

            // Add old + new together
            customer.addPurchase(oldTotal + subtotal);
            double discPct = customer.getDiscount();
            double discAmt = subtotal * discPct / 100;
            double total = subtotal - discAmt;
            double change = cash - total;
            if (change < 0) {
                JOptionPane.showMessageDialog(this, "Insufficient cash. Need Rs " + String.format("%.2f", total));
                return;
            }

            // Save updated file
            try {
                File file = new File("customers.txt");
                String content = "";
                boolean found = false;

                if (file.exists()) {
                    // oprning the file and reading it line by line through scanner object
                    Scanner sc = new Scanner(file);
                    while (sc.hasNextLine()) {
                        // reading next line and storing in line
                        String line = sc.nextLine();
                        // checking the line if it belongs to corrent customer
                        if (line.startsWith(name + ",")) {
                            // replacing with new points using method toFile in customer class to get
                            // formatted line to add o the file
                            content += customer.toFile() + "\n";
                            found = true;
                        }
                        // if line does not belong to current customer keep it as it is
                        else {
                            content += line + "\n";
                        }
                    }
                    sc.close();
                }

                if (!found)
                    content += customer.toFile() + "\n";

                FileWriter fw = new FileWriter(file, false);
                fw.write(content);
                fw.close();

            } catch (Exception ignored) {
            }
            // building reciept string using string builder
            StringBuilder sb = new StringBuilder();
            sb.append("==============================\n");
            sb.append("        STORE RECEIPT         \n");
            sb.append("==============================\n");
            sb.append("Customer : ").append(name).append("\n");
            sb.append("Date     : ").append(LocalDate.now()).append("\n");
            sb.append("Cashier  : ").append(cashier.username).append("\n");
            sb.append("------------------------------\n");
            for (CartItem ci : cashier.cart)
                sb.append(String.format("%-14s x%d = RS%.2f\n", ci.product.name, ci.qty, ci.total()));
            sb.append("------------------------------\n");
            sb.append(String.format("Subtotal : RS%.2f\n", subtotal));
            sb.append(String.format("Discount : %.1f%% (-RS%.2f)\n", discPct, discAmt));
            sb.append(String.format("TOTAL    : RS%.2f\n", total));
            sb.append(String.format("Cash     : RS%.2f\n", cash));
            sb.append(String.format("Change   : RS%.2f\n", change));
            sb.append("==============================\n");
            // making text area to show reciept
            JTextArea receipt = new JTextArea(sb.toString());// converting to pliin string
            receipt.setFont(F_MONO);
            receipt.setEditable(false);
            receipt.setBackground(new Color(250, 250, 250));
            // adding to scroll pane to show
            JScrollPane rsp = new JScrollPane(receipt);
            rsp.setPreferredSize(new Dimension(320, 320));
            JOptionPane.showMessageDialog(this, rsp, "Receipt", JOptionPane.PLAIN_MESSAGE);

            cashier.cart.clear();
            showDashboard();
        });
        // wraping in other panel
        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER));// aarranging components in a row left to right and
                                                                    // centering them horizontally.
        wrap.setOpaque(false);
        wrap.add(card);
        // adding the wrap panel to the center of the main checkout panel
        p.add(wrap, BorderLayout.CENTER);
        // swapping to show checkout page
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
