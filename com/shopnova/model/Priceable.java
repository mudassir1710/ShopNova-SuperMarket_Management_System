package com.shopnova.model;

public interface Priceable {
    double getPrice();
    double total(int qty);
}
