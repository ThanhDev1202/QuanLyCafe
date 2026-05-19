package shared.Model;

import java.io.Serializable;

public class CategoryFood implements Serializable{
    private int id;
    private String ten;

    public CategoryFood() {
    }

    public CategoryFood(String ten) {
        this.ten = ten;
    }
    public String getTen() {
        return ten;
    }
    public void setTen(String ten) {
        this.ten = ten;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    } 
}
