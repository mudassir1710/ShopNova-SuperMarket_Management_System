import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    // Data fields
    // associations to load data and perform login operations
    Inventory inventory = new Inventory();
    CashierManager cashierManager = new CashierManager();
    // Color scheme constants for consistent styling across the application
    static final Color SIDEBAR = new Color(15, 23, 42);
    static final Color ACCENT = new Color(59, 130, 246);
    static final Color PAGE_BG = new Color(241, 245, 249);
    static final Color CARD_BG = Color.WHITE;
    static final Color TEXT_DK = new Color(15, 23, 42);
    static final Color TEXT_MD = new Color(71, 85, 105);
    // Text fields and buttons for the login form
    JTextField usernameField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JRadioButton adminRadio = new JRadioButton("Admin", true);
    JRadioButton cashierRadio = new JRadioButton("Cashier");

    public LoginFrame() {
        inventory.load(); // Load inventory data from storage file
        cashierManager.load(); // Load cashier data from storage file

        setTitle("Store Management System");// Set the title of the login window
        setDefaultCloseOperation(EXIT_ON_CLOSE); // application exits when the login window is closed
        setSize(820, 540);
        setLocationRelativeTo(null);// Center the login window on the screen
        setResizable(true);

        JPanel root = new JPanel(new BorderLayout());// Root panel to divide into left and right sections

        // Left panel for welcome message and brand naming
        JPanel left = new JPanel(new GridBagLayout());// Using GridBagLayout to center the content vertically and
                                                      // horizontally
        left.setBackground(SIDEBAR);
        left.setPreferredSize(new Dimension(340, 0));

        JPanel brand = new JPanel(new GridLayout(3, 1, 0, 10));// Panel to hold the logo, tagline, and version
        brand.setOpaque(false);
        // Logo label with custom font and color
        JLabel logo = new JLabel("Shop Nova", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        logo.setForeground(Color.WHITE);
        // Tagline label with custom font and color
        JLabel tagline = new JLabel("Store Management System", SwingConstants.CENTER);
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tagline.setForeground(new Color(148, 163, 184));
        // Version label with custom font and color
        JLabel ver = new JLabel("Beta v3.0", SwingConstants.CENTER);
        ver.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ver.setForeground(new Color(71, 85, 105));
        // Adding the logo, tagline, and version labels to the brand panel
        brand.add(logo);
        brand.add(tagline);
        brand.add(ver);
        left.add(brand);

        // Right panel for login form
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(PAGE_BG);
        // Card panel to hold the login form with a white background and border
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(36, 40, 36, 40)));
        card.setPreferredSize(new Dimension(360, 380));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;// Make components stretch horizontally to fill the available space
        gc.insets = new Insets(6, 0, 6, 0);// Add vertical spacing between components

        // Title
        JLabel title = new JLabel("Sign In");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT_DK);
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 2;
        card.add(title, gc);
        // Subtitle label
        JLabel sub = new JLabel("Select your role and enter credentials");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(TEXT_MD);
        gc.gridy = 1;
        card.add(sub, gc);

        // Role selection radio buttons
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rolePanel.setOpaque(false);
        adminRadio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        adminRadio.setBackground(CARD_BG);
        adminRadio.setForeground(TEXT_DK);
        cashierRadio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cashierRadio.setBackground(CARD_BG);
        cashierRadio.setForeground(TEXT_DK);
        // Grouping the radio buttons to allow only one selection at a time
        ButtonGroup group = new ButtonGroup();
        group.add(adminRadio);
        group.add(cashierRadio);
        // Adding the admin radio button to the role panel
        rolePanel.add(adminRadio);
        // Adding horizontal spacing between the two radio buttons
        rolePanel.add(Box.createHorizontalStrut(20));
        // Adding the cashier radio button to the role panel
        rolePanel.add(cashierRadio);
        gc.gridy = 2;
        gc.insets = new Insets(14, 0, 6, 0);
        card.add(rolePanel, gc);

        // Username field with label
        JLabel ul = lbl("Username");
        // Styling the username label with a custom font and color using the lbl method
        // defined below
        gc.gridy = 3;
        gc.insets = new Insets(8, 0, 2, 0);// Adding the username label to the card panel with specified grid position
                                           // and insets for spacing
        card.add(ul, gc);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(280, 40));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));
        gc.gridy = 4;// Adding the username text field to the card panel with specified grid position
                     // and insets for spacing
        gc.insets = new Insets(0, 0, 6, 0);
        card.add(usernameField, gc);

        // Password field with label Same styling and layout as the username field
        JLabel pl = lbl("Password");
        gc.gridy = 5;
        gc.insets = new Insets(8, 0, 2, 0);
        card.add(pl, gc);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(280, 40));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));
        gc.gridy = 6;
        gc.insets = new Insets(0, 0, 6, 0);
        card.add(passwordField, gc);

        // Login button with custom styling for font, color, and size
        JButton loginBtn = new JButton("Login →");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginBtn.setBackground(ACCENT);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// cursor changes to hand when hovering over
                                                                           // the button to indicate it's clickable
        loginBtn.setPreferredSize(new Dimension(280, 44));
        // Adding the login button to the card panel with specified grid position and
        // insets for spacing
        gc.gridy = 7;
        gc.insets = new Insets(18, 0, 0, 0);
        card.add(loginBtn, gc);

        loginBtn.addActionListener(e -> doLogin());
        passwordField.addActionListener(e -> doLogin());
        // Adding the card panel to the right panel and then adding both left and right
        // panels to the root panel
        right.add(card);
        root.add(left, BorderLayout.WEST);
        root.add(right, BorderLayout.CENTER);
        add(root);
        setVisible(true);
    }

    // Method to handle the login when the login button is clicked or when the enter
    // key is pressed in the password field
    void doLogin() {
        String u = usernameField.getText().trim();
        String p = new String(passwordField.getPassword()).trim();
        if (adminRadio.isSelected()) {
            if (u.equals("admin") && p.equals("admin123")) {
                new AdminFrame(inventory, cashierManager);
                dispose();
            } else
                JOptionPane.showMessageDialog(this, "Wrong admin credentials.", "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
        } else {
            Cashier c = cashierManager.login(u, p);
            if (c != null) {
                new CashierFrame(c, inventory);
                dispose();
            } else
                JOptionPane.showMessageDialog(this, "Invalid cashier credentials.", "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
        }
    }

    // lbl method to create a styled JLabel with consistent font and color for form
    // labels
    JLabel lbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        l.setForeground(TEXT_MD);
        return l;
    }

    // Main method to launch the application by creating an instance of the
    // LoginFrame
    public static void main(String[] args) {
        // Using SwingUtilities.invokeLater to ensure that the GUI is created and
        // updated on the Event Dispatch Thread (EDT), which is the proper way to handle
        // Swing components for thread safety
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
