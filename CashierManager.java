import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;

public class CashierManager implements Persistable {
    public ArrayList<Cashier> list = new ArrayList<>();

    @Override
    public void load() {
        try {
            File f = new File("cashiers.txt");
            if (!f.exists()) return;
            Scanner sc = new Scanner(f);
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] d = line.split(",");
                if (d.length >= 2) list.add(new Cashier(d[0], d[1]));
            }
            sc.close();
        } catch (Exception e) { }
    }

    @Override
    public void save() {
        try {
            FileWriter fw = new FileWriter("cashiers.txt");
            for (Cashier c : list) fw.write(c.toFile() + "\n");
            fw.close();
        } catch (Exception e) { }
    }

    public void add(Cashier c) {
        list.add(c);
        save();
    }

    public void remove(String username) {
        list.removeIf(c -> c.username.equals(username));
        save();
    }

    // Polymorphism: iterates over User objects and compares credentials
    public Cashier login(String username, String password) {
        for (Cashier c : list)
            if (c.username.equals(username) && c.password.equals(password)) return c;
        return null;
    }
}
