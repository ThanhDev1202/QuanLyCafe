package shared.Model;

import java.io.Serializable;

/*
 * @author Hiep
 */
public class BillTable implements Serializable { // Implement Serializable nếu bạn dùng Socket/RMI
    private int id;       // Khóa chính của bảng BillTable
    private int idBill;   // Mã hóa đơn
    private int idTable;  // Mã bàn

    // Constructor không tham số
    public BillTable() {
    }

    // Constructor đầy đủ tham số
    public BillTable(int id, int idBill, int idTable) {
        this.id = id;
        this.idBill = idBill;
        this.idTable = idTable;
    }

    // Constructor nhanh khi cần thêm mới (Database tự tăng ID này)
    public BillTable(int idBill, int idTable) {
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