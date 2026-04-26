import java.util.ArrayList;

public class Cashier extends User {
    public ArrayList<CartItem> cart = new ArrayList<>();

    public Cashier(String username, String password) {
        super(username, password);
    }

    @Override
    public String toFile() {
        return username + "," + password;
    }

    @Override
    public String getRole() {
        return "Cashier";
    }
}
