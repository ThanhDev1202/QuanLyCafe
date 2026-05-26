package Server.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BillTableDAO {
    private Connection conn;

    public BillTableDAO() {
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    // Chèn liên kết trung gian giữa Hóa đơn và Bàn vào bảng Bill_Table
    public boolean saveBillTables(int billId, List<Integer> tableIds) {
        String sql = "INSERT INTO BillTable (idBill, idTable) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Integer tableId : tableIds) {
                ps.setInt(1, billId);
                ps.setInt(2, tableId);
                ps.addBatch();
            }
            int[] results = ps.executeBatch();
            for (int res : results) {
                if (res == PreparedStatement.EXECUTE_FAILED) return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật trạng thái bàn sang "Có người" hoặc ngược lại
    public boolean updateTableStatus(int tableId, String status) {
        String sql = "UPDATE TableFood SET status = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, tableId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}