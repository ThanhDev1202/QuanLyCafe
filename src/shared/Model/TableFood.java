
package shared.Model;
import java.io.Serializable;
public class TableFood implements Serializable{   
    private int id;
    private String name;
    private String status;
    public TableFood() {
    }
    public TableFood(int id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
