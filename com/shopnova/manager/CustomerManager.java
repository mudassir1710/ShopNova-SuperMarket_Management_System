package com.shopnova.manager;

import com.shopnova.model.Customer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomerManager {

    private static final String FILE_NAME = "customers.txt";

    // ── Replaced Map<String, Customer> with ArrayList<Customer> ──
    private final ArrayList<Customer> customers = new ArrayList<>();

    public CustomerManager() {
        load();
    }

    // ── Returns unmodifiable view (safe read-only access) ─────────
    public List<Customer> getCustomers() {
        return Collections.unmodifiableList(customers);
    }

    // ── Find customer by name (linear search) ─────────────────────
    //    Returns null if not found
    public Customer findByName(String name) {
        for (Customer c : customers) {
            if (c.name.equalsIgnoreCase(name.trim())) {
                return c;
            }
        }
        return null;
    }

    // ── Find existing customer or register a new one ───────────────
    public Customer findOrCreate(String name) {
        Customer existing = findByName(name);
        if (existing != null) {
            return existing;              // returning customer
        }
        Customer newCustomer = new Customer(name.trim());
        customers.add(newCustomer);       // register brand-new customer
        return newCustomer;
    }

    // ── Save a single customer's updated data then persist all ─────
    public void saveCustomer(Customer updated) {
        // Replace existing entry if found, otherwise add
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).name.equalsIgnoreCase(updated.name)) {
                customers.set(i, updated);
                saveAll();
                return;
            }
        }
        // Not found — add as new
        customers.add(updated);
        saveAll();
    }

    // ── Remove a customer by name ──────────────────────────────────
    public boolean remove(String name) {
        boolean removed = customers.removeIf(
            c -> c.name.equalsIgnoreCase(name.trim())
        );
        if (removed) saveAll();
        return removed;
    }

    // ── Load all customers from file into ArrayList ────────────────
    private void load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try {
            List<String> lines = Files.readAllLines(
                file.toPath(), StandardCharsets.UTF_8
            );
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String name   = parts[0].trim();
                    double total  = Double.parseDouble(parts[1].trim());
                    int    points = Integer.parseInt(parts[2].trim());
                    customers.add(new Customer(name, total, points));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ── Write all customers to file atomically ─────────────────────
    private synchronized void saveAll() {
        List<String> lines = new ArrayList<>();
        for (Customer c : customers) {
            lines.add(c.toFile());        // name,totalPurchase,points
        }
        Path temp   = Path.of(FILE_NAME + ".tmp");
        Path target = Path.of(FILE_NAME);
        try {
            Files.write(temp, lines, StandardCharsets.UTF_8);
            Files.move(temp, target,
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
