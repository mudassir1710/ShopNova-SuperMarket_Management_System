package model;

public class Admin extends Person {
    public Admin(String name,String username,String password){
        super(name,username,password,"Admin");
    }
    @Override
    public String getAccessLevel(){
        return "Full Access";
    }

    @Override
    public boolean canPerformAction(String action) {
        return true;
    }

    @Override
    public String toFileString() {
        return getName() + "," + getUserName() + "," + getPassword() + "," + getRole();
    }

}
