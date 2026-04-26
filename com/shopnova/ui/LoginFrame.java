package com.shopnova.ui;

import com.shopnova.manager.CashierManager;
import com.shopnova.manager.Inventory;
import com.shopnova.model.Cashier;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    Inventory       inventory      = new Inventory();
    CashierManager  cashierManager = new CashierManager();

    static final Color SIDEBAR  = new Color(15, 23, 42);
    static final Color ACCENT   = new Color(59, 130, 246);
    static final Color PAGE_BG  = new Color(241, 245, 249);
    static final Color CARD_BG  = Color.WHITE;
    static final Color TEXT_DK  = new Color(15, 23, 42);
    static final Color TEXT_MD  = new Color(71, 85, 105);

    JTextField    usernameField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JRadioButton  adminRadio    = new JRadioButton("Admin", true);
    JRadioButton  cashierRadio  = new JRadioButton("Cashier");

    public LoginFrame() {
        inventory.load();
        cashierManager.load();

        setTitle("ShopNova — Supermarket Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(820, 540);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel root = new JPanel(new BorderLayout());

        // ── Left branding panel ──────────────────────────────────
        JPanel left = new JPanel(new GridBagLayout());
        left.setBackground(SIDEBAR);
        left.setPreferredSize(new Dimension(340, 0));

        JPanel brand = new JPanel();
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        brand.setOpaque(false);

        JLabel iconLabel = new JLabel("🛒");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logo = new JLabel("ShopNova");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tagline = new JLabel("Supermarket Management System");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tagline.setForeground(new Color(226, 232, 240));
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255, 255, 255, 80));
        sep.setMaximumSize(new Dimension(240, 1));
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel features = new JPanel(new GridLayout(5, 1, 0, 10));
        features.setOpaque(false);
        features.setAlignmentX(Component.CENTER_ALIGNMENT);
        features.setMaximumSize(new Dimension(210, 150));
        for (String f : new String[]{
                "●  Inventory management",
                "●  Checkout & billing",
                "●  Loyalty points",
                "●  Expiry tracking",
                "●  PDF receipts"}) {
            JLabel lbl = new JLabel(f);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lbl.setForeground(new Color(148, 163, 184));
            features.add(lbl);
        }

        brand.add(iconLabel);
        brand.add(Box.createRigidArea(new Dimension(0, 15)));
        brand.add(logo);
        brand.add(Box.createRigidArea(new Dimension(0, 5)));
        brand.add(tagline);
        brand.add(Box.createRigidArea(new Dimension(0, 25)));
        brand.add(sep);
        brand.add(Box.createRigidArea(new Dimension(0, 25)));
        brand.add(features);
        left.add(brand);

        // ── Right login panel ─────────────────────────────────────
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(PAGE_BG);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(36, 40, 36, 40)));
        card.setPreferredSize(new Dimension(360, 390));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill    = GridBagConstraints.HORIZONTAL;
        gc.insets  = new Insets(6, 0, 6, 0);

        JLabel title = new JLabel("Sign In");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT_DK);
        gc.gridx = 0; gc.gridy = 0; gc.gridwidth = 2;
        card.add(title, gc);

        JLabel sub = new JLabel("Select your role and enter credentials:");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(TEXT_MD);
        gc.gridy = 1;
        card.add(sub, gc);

        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rolePanel.setOpaque(false);
        for (JRadioButton rb : new JRadioButton[]{adminRadio, cashierRadio}) {
            rb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            rb.setBackground(CARD_BG);
            rb.setForeground(TEXT_DK);
        }
        ButtonGroup grp = new ButtonGroup();
        grp.add(adminRadio); grp.add(cashierRadio);
        rolePanel.add(adminRadio);
        rolePanel.add(Box.createHorizontalStrut(20));
        rolePanel.add(cashierRadio);
        gc.gridy = 2; gc.insets = new Insets(14, 0, 6, 0);
        card.add(rolePanel, gc);

        gc.gridy = 3; gc.insets = new Insets(8, 0, 2, 0);
        card.add(lbl("Username"), gc);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(280, 40));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));
        gc.gridy = 4; gc.insets = new Insets(0, 0, 6, 0);
        card.add(usernameField, gc);

        gc.gridy = 5; gc.insets = new Insets(8, 0, 2, 0);
        card.add(lbl("Password"), gc);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(280, 40));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));
        gc.gridy = 6; gc.insets = new Insets(0, 0, 6, 0);
        card.add(passwordField, gc);

        JButton loginBtn = new JButton("Login →");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginBtn.setBackground(ACCENT);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginBtn.setPreferredSize(new Dimension(280, 44));
        gc.gridy = 7; gc.insets = new Insets(18, 0, 0, 0);
        card.add(loginBtn, gc);

        loginBtn.addActionListener(e -> doLogin());
        passwordField.addActionListener(e -> doLogin());

        right.add(card);
        root.add(left, BorderLayout.WEST);
        root.add(right, BorderLayout.CENTER);
        add(root);
        setVisible(true);
    }

    void doLogin() {
        String u = usernameField.getText().trim();
        String p = new String(passwordField.getPassword()).trim();
        if (adminRadio.isSelected()) {
            if (u.equals("admin") && p.equals("admin123")) {
                new AdminFrame(inventory, cashierManager);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Wrong admin credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            Cashier c = cashierManager.login(u, p);
            if (c != null) { new CashierFrame(c, inventory); dispose(); }
            else JOptionPane.showMessageDialog(this,
                    "Invalid cashier credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    JLabel lbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        l.setForeground(TEXT_MD);
        return l;
    }
}
