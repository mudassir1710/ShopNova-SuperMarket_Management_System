import java.time.LocalDate;

public class Product implements Priceable {
    public int id;
    public String name;
    public int qty;
    public String category;
    public LocalDate expiry;
    public double price;

    public Product(int id, String name, int qty, String category, String expiry, double price) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.category = category;
        this.price = price;
        try {
            this.expiry = LocalDate.parse(expiry.trim());
        } catch (Exception e) {
            this.expiry = LocalDate.now();
        }
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public double total(int qty) {
        return price * qty;
    }

    public String getStatus() {
        if (expiry.isBefore(LocalDate.now())) return "EXPIRED";
        if (qty <= 5) return "LOW STOCK";
        return "OK";
    }
}
