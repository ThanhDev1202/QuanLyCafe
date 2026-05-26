package shared.Model;

import java.io.Serializable;

public class Account  implements Serializable{
    private int id;
    private String displayName;
    private String username;
    private String password;
    private int type; //0-staff  1-manager

    public Account(){
    }

    public Account(String displayName, String username, String password, int type) {
        this.displayName = displayName;
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
}
