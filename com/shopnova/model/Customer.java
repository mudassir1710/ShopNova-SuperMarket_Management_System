package com.shopnova.model;

public class Customer extends Person {
    public double totalPurchase;
    public int points;

    public Customer(String name) {
        super(name);
        this.totalPurchase = 0;
        this.points        = 0;
    }

    public Customer(String name, double totalPurchase, int points) {
        super(name);
        this.totalPurchase = totalPurchase;
        this.points        = points;
    }

    /**
     * Adds a purchase amount and earns loyalty points.
     * Rule: 1 point earned per Rs 100 spent.
     */
    public void addPurchase(double amount) {
        totalPurchase += amount;
        points        += (int) (amount / 100);
    }

    /**
     * Redeems points chosen by the customer.
     * Rule: each point = Rs 1 off the bill.
     * Returns the actual rupee value redeemed.
     */
    public double redeemPoints(int pointsToRedeem) {
        if (pointsToRedeem <= 0 || pointsToRedeem > points) return 0;
        points -= pointsToRedeem;
        return pointsToRedeem; // 1 point = Rs 1
    }

    /** Serialise to file: name,totalPurchase,points */
    @Override
    public String toFile() {
        return name + "," + totalPurchase + "," + points;
    }
}
