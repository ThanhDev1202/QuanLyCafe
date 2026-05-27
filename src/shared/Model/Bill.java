package shared.Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class Bill implements Serializable{

    public static final int UNPAID = 0;
    public static final int PAID = 1;

    private int id;
    private Date dateCheckIn;
    private Date dateCheckOut; 
    private ArrayList<Integer> tableIds;// nhiều bàn   
    private ArrayList<BillInfor> billInfors;// nhiều món
    private int discount;
    private BigDecimal totalPrice;
    private int status;
    public Bill() {
        tableIds = new ArrayList<>();
        billInfors = new ArrayList<>();
    }

    public Bill(int id,
                Date dateCheckIn,
                Date dateCheckOut,
                ArrayList<Integer> tableIds,
                ArrayList<BillInfor> billInfors,
                int discount,
                BigDecimal totalPrice,
                int status) {
        this.id = id;
        this.dateCheckIn = dateCheckIn;
        this.dateCheckOut = dateCheckOut;
        this.tableIds = tableIds;
        this.billInfors = billInfors;
        this.discount = discount;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateCheckIn() {
        return dateCheckIn;
    }

    public void setDateCheckIn(Date dateCheckIn) {
        this.dateCheckIn = dateCheckIn;
    }

    public Date getDateCheckOut() {
        return dateCheckOut;
    }

    public void setDateCheckOut(Date dateCheckOut) {
        this.dateCheckOut = dateCheckOut;
    }

    public ArrayList<Integer> getTableIds() {
        return tableIds;
    }

    public void setTableIds(ArrayList<Integer> tableIds) {
        this.tableIds = tableIds;
    }

    public ArrayList<BillInfor> getBillInfors() {
        return billInfors;
    }

    public void setBillInfors(ArrayList<BillInfor> billInfors) {
        this.billInfors = billInfors;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public void addTableId(int tableId) {
        if (this.tableIds == null) {
            this.tableIds = new ArrayList<>();
        }
        this.tableIds.add(tableId);
    }

    public void addBillInfor(BillInfor infor) {
        if (this.billInfors == null) {
            this.billInfors = new ArrayList<>();
        }
        this.billInfors.add(infor);
    }
}