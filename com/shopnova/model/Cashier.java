package com.shopnova.model;

import java.util.ArrayList;

public class Cashier extends User {
    public ArrayList<CartItem> cart = new ArrayList<>();
    public double totalSales = 0.0;

    public Cashier(String username, String password) {
        super(username, password);
    }

    @Override 
    public String toFile()   {
         return username + "," + password + "," + totalSales; }
    @Override 
    public String getRole()  {
         return "Cashier"; }
}
