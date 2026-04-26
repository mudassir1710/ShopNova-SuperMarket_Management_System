import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;

public class Inventory implements Persistable {
    public ArrayList<Product> list = new ArrayList<>();
    public int idCounter = 1;

    @Override
    public void load() {
        try {
            File f = new File("inventory.txt");
            if (!f.exists()) return;
            Scanner sc = new Scanner(f);
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] d = line.split(",");
                if (d.length < 6) continue;
                Product p = new Product(Integer.parseInt(d[0]), d[1], Integer.parseInt(d[2]),
                        d[3], d[4], Double.parseDouble(d[5]));
                list.add(p);
                idCounter = Math.max(idCounter, p.id + 1);
            }
            sc.close();
        } catch (Exception e) { }
    }

    @Override
    public void save() {
        try {
            FileWriter fw = new FileWriter("inventory.txt");
            for (Product p : list)
                fw.write(p.id + "," + p.name + "," + p.qty + "," + p.category + "," + p.expiry + "," + p.price + "\n");
            fw.close();
        } catch (Exception e) { }
    }

    public void addProduct(Product np) {
        for (Product p : list) {
            if (p.name.equalsIgnoreCase(np.name) && p.category.equalsIgnoreCase(np.category)
                    && p.expiry.equals(np.expiry)) {
                p.qty += np.qty;
                save();
                return;
            }
        }
        list.add(np);
        save();
    }

    public Product findById(int id) {
        for (Product p : list)
            if (p.id == id) return p;
        return null;
    }

    public void remove(int id) {
        list.removeIf(p -> p.id == id);
        save();
    }
}
