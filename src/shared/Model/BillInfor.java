package shared.Model;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 *
 * @author Hiep
 */
public class BillInfor implements Serializable{
    private int id;
    private int idBill;
    
    //từng food trong hóa đơn
    private int idFood;
    private int quantity;
    private String foodName;
    private BigDecimal price;

    public BillInfor() {
    }

    public BillInfor(int id, int idBill, int idFood, String foodName, int quantity, BigDecimal price) {
       this.id = id;
       this.idBill = idBill;
       this.idFood = idFood;
       this.foodName = foodName;
       this.quantity = quantity;
       this.price = price;
   }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getIdBill() {
        return idBill;
    }

    public void setIdBill(int idBill) {
        this.idBill = idBill;
    }

    public int getIdFood() {
        return idFood;
    }

    public void setIdFood(int idFood) {
        this.idFood = idFood;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
