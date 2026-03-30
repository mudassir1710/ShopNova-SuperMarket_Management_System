package model;

public abstract class Person {
    private String name;
    private String username;
    private String password;
    private String role;

    public Person(String name,String username,String password,String role){
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name cannot be empty");
        if (username == null || username.trim().isEmpty())
            throw new IllegalArgumentException("Username cannot be empty");
        if (password == null || password.length() < 4)
            throw new IllegalArgumentException("Password too short");
        this.name=name.trim();
        this.username=username.trim();
        this.password=password;
        this.role=role;
    }

    public String getName(){
        return name;
    }
    public String getUserName(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String getRole(){
        return role;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password){
        this.password=password;
    }

    public boolean isAdmin() {
        return role.equals("Admin");
    }
    public boolean isCashier() {
        return role.equals("Cashier");
    }

    public boolean isCustomer() {
        return role.equals("Customer");
    }
    public abstract String getAccessLevel();
    public abstract boolean canPerformAction(String action);
    public abstract String toFileString() ;
    @Override
    public String toString(){
        return "Name: "+ name+ ", Username: "+ username + ", Role: "+ role;
    }
}
