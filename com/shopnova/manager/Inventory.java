package com.shopnova.manager;

import com.shopnova.model.Persistable;
import com.shopnova.model.Product;

import java.io.*;
import java.util.*;

public class Inventory implements Persistable {
    private final List<Product> products = new ArrayList<>();
    private int idCounter = 1;
    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public int getNextId() {
        return idCounter++;
    }
    @Override
    public void load() {
        File f = new File("inventory.txt");
        if (!f.exists()) return;
        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] d = line.split(",");
                //format: id,name,qty,category,expirable,expiry,price  (7 fields)
                if (d.length == 7) {
                    boolean expirable = Boolean.parseBoolean(d[4].trim());
                    Product p = new Product(
                            Integer.parseInt(d[0].trim()), d[1].trim(),
                            Integer.parseInt(d[2].trim()), d[3].trim(),
                            expirable, d[5].trim(),
                            Double.parseDouble(d[6].trim()));
                    products.add(p);
                    idCounter = Math.max(idCounter, p.id + 1);
                } else if (d.length == 6) {
                    Product p = new Product(
                            Integer.parseInt(d[0].trim()), d[1].trim(),
                            Integer.parseInt(d[2].trim()), d[3].trim(),
                            true, d[4].trim(),
                            Double.parseDouble(d[5].trim()));
                    products.add(p);
                    idCounter = Math.max(idCounter, p.id + 1);
                }
            }
        } catch (Exception e) { 
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        try (FileWriter fw = new FileWriter("inventory.txt")) {
            for (Product p : products)
                fw.write(p.toFileLine() + "\n");
        } catch (Exception e) {
             e.printStackTrace();
        }
    }

    public void addProduct(Product np) {
        for (Product p : products) {
            boolean sameExpiry = (!np.expirable && !p.expirable)
                    || (np.expirable && p.expirable && np.expiry != null && np.expiry.equals(p.expiry));
            if (p.name.equalsIgnoreCase(np.name) && p.category.equalsIgnoreCase(np.category) && sameExpiry) {
                p.qty += np.qty;
                save();
                return;
            }
        }
        products.add(np);
        save();
    }

    public Product findById(int id) {
        for (Product p : products)
            if (p.id == id) {
                return p;}
        return null;
    }
    public boolean reduceStock(int productId, int qty) {
        Product p = findById(productId);
        if (p == null || p.qty < qty) return false;
        p.qty -= qty;
        save();
        return true;
    }
    public void remove(int id) {
        products.removeIf(p -> p.id == id);
        save();
    }
}
