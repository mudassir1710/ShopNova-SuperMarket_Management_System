public abstract class User {
    public String username;
    public String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Each subclass defines how it serializes itself to a file line
    public abstract String toFile();

    // Each subclass defines its role label
    public abstract String getRole();
}
