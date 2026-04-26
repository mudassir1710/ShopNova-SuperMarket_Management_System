package com.shopnova.model;

import java.time.LocalDate;

public class Product implements Priceable {
    public int id;
    public String name;
    public int qty;
    public String category;
    public boolean expirable;   // NEW: whether this product can expire
    public LocalDate expiry;    // only meaningful when expirable == true
    public double price;

    // Full constructor (used when loading from file)
    public Product(int id, String name, int qty, String category,
                   boolean expirable, String expiry, double price) {
        this.id         = id;
        this.name       = name;
        this.qty        = qty;
        this.category   = category;
        this.expirable  = expirable;
        this.price      = price;
        if (expirable) {
            try {
                this.expiry = LocalDate.parse(expiry.trim());
            } catch (Exception e) {
                this.expiry = LocalDate.now().plusYears(1); // safe fallback
            }
        } else {
            this.expiry = null; // non-expirable → no date needed
        }
    }
    public Product copy() {
    return new Product(id, name, qty, category, expirable, 
                       (expiry == null ? "N/A" : expiry.toString()), price);
}

    @Override
    public double getPrice()        {
        return price; }
    @Override
    public double total(int qty)    {
         return price * qty; }

    
    public String getStatus() {
        if (expirable && expiry != null && expiry.isBefore(LocalDate.now()))
            return "EXPIRED";
        if (qty <= 5) return "LOW STOCK";
        return "OK";
    }
    public double getAutoDiscount() {
    if (!expirable || expiry == null) return 0.0;
    long days = java.time.temporal.ChronoUnit.DAYS.between(java.time.LocalDate.now(), expiry);
    if (days < 7)  return 0.50;
    if (days < 15) return 0.30;
    if (days < 30) return 0.10;
    return 0.0;
}

    public String toFileLine() {
        String expiryStr = (expirable && expiry != null) ? expiry.toString() : "N/A";
        return id + "," + name + "," + qty + "," + category + ","
                + expirable + "," + expiryStr + "," + price;
    }
}
