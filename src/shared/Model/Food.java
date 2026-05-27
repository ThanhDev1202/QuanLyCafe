package shared.Model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Food implements Serializable {
    private int id;
    private String nameFood;
    private int idcategory;
    private BigDecimal priceIn;
    private BigDecimal priceOut;
    private int numbers;
    private String imagePath;
    private byte[] imageData;
    public Food() {
    }
    public Food(int id, String nameFood, int idcategory, BigDecimal priceIn, BigDecimal priceOut, int numbers, String imagePath, byte[] imageData) {
        this.id = id;
        this.nameFood = nameFood;
        this.idcategory = idcategory;
        this.priceIn = priceIn;
        this.priceOut = priceOut;
        this.numbers = numbers;
        this.imagePath = imagePath;
        this.imageData = imageData;
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
    public BigDecimal getPriceIn() {
        return priceIn;
    }
    public void setPriceIn(BigDecimal priceIn) {
        this.priceIn = priceIn;
    }
    public BigDecimal getPriceOut() {
        return priceOut;
    }
    public void setPriceOut(BigDecimal priceOut) {
        this.priceOut = priceOut;
    }
    public int getIdcategory() {
        return idcategory;
    }
    public void setIdcategory(int idcategory) {
        this.idcategory = idcategory;
    }
    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public byte[] getImageData() {
        return imageData;
    }
    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
    public int getNumbers() {
        return numbers;
    }
    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }
   
}
