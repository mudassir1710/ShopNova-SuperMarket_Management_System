package com.shopnova.manager;

import com.shopnova.model.Cashier;
import com.shopnova.model.Persistable;

import java.io.*;
import java.util.*;

public class CashierManager implements Persistable {
    public List<Cashier> cashiers = new ArrayList<>();

    public List<Cashier> getCashiers() {
        return Collections.unmodifiableList(cashiers);
    }

    @Override
    public void load() {
    File f = new File("cashiers.txt");
    if (!f.exists())
        return;
    cashiers.clear();
    try (Scanner sc = new Scanner(f)) {
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty())
                continue;
            String[] d = line.split(",");
            if (d.length >= 2) {
                double sales = d.length >= 3 ? Double.parseDouble(d[2].trim()) : 0.0;
                cashiers.add(new Cashier(d[0], d[1]));
                cashiers.get(cashiers.size() - 1).totalSales = sales;
            }
        }
    } catch (Exception e) {
        System.out.println("Error loading cashiers: " + e.getMessage());
    }
}
    

    @Override
    public void save() {
        try {
            FileWriter fw = new FileWriter("cashiers.txt");
            for (Cashier c : cashiers) {
                fw.write(c.toFile() + "\n");
            }
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(Cashier c) {
        cashiers.add(c);
        save();
    }

    public void remove(String username) {
        cashiers.removeIf(c -> c.username.equals(username));
        save();
    }

    public Cashier login(String username, String password) {
        for (Cashier c : cashiers) {
            if (c.username.equals(username) && c.password.equals(password)) {
                return c;
            }
        }
        return null;
    }
}
