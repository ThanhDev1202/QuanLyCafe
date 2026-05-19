package shared.Model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Food implements Serializable{
    private int id;
    private String nameFood;
    private int idcategory;
    private BigDecimal price;

    public Food() {
    }

    public Food(String nameFood, int idcategory, BigDecimal price) {
        this.nameFood = nameFood;
        this.idcategory = idcategory;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    
    public String getNameFood() {
        return nameFood;
    }

    public void setNameFood(String nameFood) {
        this.nameFood = nameFood;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getIdcategory() {
        return idcategory;
    }

    public void setIdcategory(int idcategory) {
        this.idcategory = idcategory;
    }

}
