package model;

public class Cashier extends Person {
    private double dailySalesTotal;
    private int transactionCount;

    public Cashier(String name,String username,String password){
        super(name,username,password,"Cashier");
    }
    //for loading from file
    public Cashier(String name, String username, String password,
                   double dailySalesTotal, int transactionCount) {
        super(name, username, password, "Cashier");
        this.dailySalesTotal = dailySalesTotal;
        this.transactionCount = transactionCount;
    }

    public void addSale(double amount) {
        dailySalesTotal += amount;
        transactionCount++;
    }

    public void resetDailySales() {
        dailySalesTotal = 0;
        transactionCount = 0;
    }

    public double getDailySalesTotal() {
        return dailySalesTotal;
    }
    public int getTransactionCount() {
        return transactionCount;
    }
    @Override
    public String getAccessLevel(){
        return "Checkout Only";
    }

    @Override
    public boolean canPerformAction(String action) {
        return action.equals("CHECKOUT") ||
                action.equals("VIEW_PRODUCTS") ||
                action.equals("SEARCH_PRODUCTS");
    }

    @Override
    public String toFileString() {
        return getName() + "," + getUserName() + "," + getPassword() + "," +
                getRole() + "," + dailySalesTotal + "," + transactionCount;
    }
}
