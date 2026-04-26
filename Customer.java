public class Customer extends Person {
    public double totalPurchase;
    public int points;

    public Customer(String name) {
        super(name);
        this.totalPurchase = 0;
        this.points = 0;
    }

    public void addPurchase(double amount) {
        totalPurchase += amount;
        points = (int)(totalPurchase / 100);
    }

    public double getDiscount() {
        double discount = points / 5.0;
        if (discount > 20) discount = 20;
        return discount;
    }

    @Override
    public String toFile() {
        return name + "," + totalPurchase + "," + points;
    }
}
