package Server.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import shared.Model.BillInfor;

public class BillInforDAO {

    private Connection conn;

    public BillInforDAO() {
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public boolean saveBillInfors(int billId, List<BillInfor> list) {
        // ĐÃ SỬA: Chuyển cột foodname xuống cuối cùng trong cả câu lệnh SQL và VALUES
        String sql = "INSERT INTO BillInfo (idBill, idFood, quantity, price, foodname) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (BillInfor bi : list) {
                ps.setInt(1, billId);
                ps.setInt(2, bi.getIdFood());
                ps.setInt(3, bi.getQuantity());    // Vị trí số 3 ứng với quantity
                ps.setBigDecimal(4, bi.getPrice()); // Vị trí số 4 ứng với price
                ps.setString(5, bi.getFoodName()); // Vị trí số 5 ứng với foodname (ở cuối)
                ps.addBatch(); // Cho vào hàng đợi
            }

            int[] results = ps.executeBatch(); // Thực thi đồng loạt
            for (int res : results) {
                if (res == PreparedStatement.EXECUTE_FAILED) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
