package com.shopnova.model;

public class CartItem {
    public Product product;
    public int qty;

    public CartItem(Product product, int qty) {
        this.product = product;
        this.qty     = qty;
    }

    /** Delegates to Product's Priceable.total() */
    public double total() {
        return product.total(qty);
    }
}
