package Server.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import shared.Model.Bill;

public class BillDAO {

    private Connection conn;

    public BillDAO() {
    }

    public BillDAO(Connection conn) {
        this.conn = conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    // Tạo hóa đơn mới và trả về ID vừa tạo
    public int createBillAndGetId(Bill bill) {
        // Thêm idTable vào câu lệnh SQL
        String sql = "INSERT INTO Bill (DateCheckIn, discount, totalPrice, status, idTable) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, new Timestamp(bill.getDateCheckIn().getTime()));
            ps.setInt(2, bill.getDiscount());
            ps.setBigDecimal(3, bill.getTotalPrice());
            ps.setInt(4, bill.getStatus());
            ps.setInt(5, bill.getTableID()); // Lưu ý: Cần thêm dòng này

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Tạo hóa đơn mới và trả về ID vừa tạo
    public int createBillAndGetId_NoTable(Bill bill) {
        // Thêm idTable vào câu lệnh SQL
        String sql = "INSERT INTO Bill (DateCheckIn, discount, totalPrice, status, DateCheckOut) VALUES (?, ?, ?, ?, ? )";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, new Timestamp(bill.getDateCheckIn().getTime()));
            ps.setInt(2, bill.getDiscount());
            ps.setBigDecimal(3, bill.getTotalPrice());
            ps.setInt(4, bill.getStatus());
            ps.setTimestamp(5, new Timestamp(bill.getDateCheckOut().getTime()));
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean payBill(int tableId) {
        // 1. Cập nhật hóa đơn sang trạng thái 1 (Đã thanh toán) cho bàn cụ thể
        String sql = "UPDATE Bill SET status = 1, DateCheckOut = GETDATE() "
                + "WHERE idTable = ? AND status = 0";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tableId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Bill> getAllBills() {
        List<Bill> list = new ArrayList<>();

        String sql = "SELECT * FROM Bill";

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Bill bill = new Bill();

                bill.setId(rs.getInt("id"));
                bill.setDateCheckIn(rs.getTimestamp("DateCheckIn"));
                bill.setDateCheckOut(rs.getTimestamp("DateCheckOut"));
                bill.setDiscount(rs.getInt("discount"));
                bill.setTotalPrice(rs.getBigDecimal("totalPrice"));
                bill.setStatus(rs.getInt("status"));
                bill.setTableID(rs.getInt("idTable"));

                list.add(bill);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
