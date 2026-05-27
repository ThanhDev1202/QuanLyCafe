package shared.Model;

import java.io.Serializable;

/*
 * @author Hiep
 */
public class BillTable implements Serializable { 
    private int id;       
    private int idBill;   
    private int idTable;  

    public BillTable() {
    }

    public BillTable(int id, int idBill, int idTable) {
        this.id = id;
        this.idBill = idBill;
        this.idTable = idTable;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdBill() {
        return idBill;
    }

    public void setIdBill(int idBill) {
        this.idBill = idBill;
    }

    public int getIdTable() {
        return idTable;
    }

    public void setIdTable(int idTable) {
        this.idTable = idTable;
    }
}