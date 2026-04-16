package model;

public class Customer extends Person{
    private int loyaltyPoints =0;
    private double totalSpent =0;
    private int totalTransactions =0;
    private String email;
    private String phone;

    public Customer(String name,String username,String password,String email,String phone){
        super(name,username,password,"Customer");
        this.email=email;
        this.phone=phone;
    }
    // for loading from file
    public Customer(String name, String username, String password,
                    String email, String phone,
                    int loyaltyPoints, double totalSpent, int totalTransactions) {
        super(name, username, password, "Customer");
        this.email             = email;
        this.phone             = phone;
        this.loyaltyPoints     = loyaltyPoints;
        this.totalSpent        = totalSpent;
        this.totalTransactions = totalTransactions;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void addPoints(double purchaseAmount) {
        // 1 point for every Rs. 50 spent
        int earned = (int)(purchaseAmount / 50);
        loyaltyPoints += earned;
    }

    public boolean redeemPoints(int points) {
        if (points > loyaltyPoints) return false;   // not enough points
        loyaltyPoints -= points;
        return true;
    }

    public double getPointsValue(int points) {
        // 100 points = Rs. 50 discount
        return (points / 100.0) * 50;
    }

    public int getLoyaltyPoints() { return loyaltyPoints; }

    // ── Purchase Tracking ────────────────────────────────────────

    public void recordPurchase(double amount) {
        totalSpent += amount;
        totalTransactions++;
        addPoints(amount);
    }
    public double getTotalSpent()        { return totalSpent; }
    public int getTotalTransactions()    { return totalTransactions; }
    @Override
    public String getAccessLevel(){
        return "Customer Only";
    }

    @Override
    public boolean canPerformAction(String action) {
        return action.equals("VIEW_PRODUCTS")    ||
                action.equals("SEARCH_PRODUCTS")  ||
                action.equals("ADD_TO_CART")      ||
                action.equals("VIEW_HISTORY")     ||
                action.equals("VIEW_POINTS")      ||
                action.equals("REDEEM_POINTS");
    }
    // ── File Persistence ─────────────────────────────────────────
    @Override
    public String toFileString() {
        return getName() + "," + getUserName() + "," + getPassword() + "," +
                getRole() + "," + email + "," + phone + "," +
                loyaltyPoints + "," + totalSpent + "," + totalTransactions;
    }

    // ── Display ──────────────────────────────────────────────────

    @Override
    public String toString() {
        return "Name: " + getName() + ", Username: " + getUserName() +
                ", Role: Customer, Points: " + loyaltyPoints +
                ", Total Spent: Rs." + totalSpent;
    }

}
