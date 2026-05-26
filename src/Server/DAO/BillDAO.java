package Server.DAO;

import java.math.BigDecimal;
import java.sql.*;
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
        String sql = "INSERT INTO Bill (DateCheckIn, discount, totalPrice, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, new Timestamp(bill.getDateCheckIn().getTime()));
            ps.setInt(2, bill.getDiscount());
            ps.setBigDecimal(3, bill.getTotalPrice());
            ps.setInt(4, bill.getStatus());
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Trả về ID tự động tăng
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Thất bại
    }
}