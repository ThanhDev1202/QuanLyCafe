package Server.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import shared.TableFood;

public class TableDAO {
    private Connection conn;

    public TableDAO(Connection conn) {
        this.conn = conn;
    }

    public List<TableFood> getAllTables() {
        List<TableFood> list = new ArrayList<>();
        String sql = "SELECT id, ten, status FROM TableFood ORDER BY id";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new TableFood(
                        rs.getInt("id"),
                        rs.getString("ten"),
                        rs.getString("status")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE TableFood SET status = ? WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean bookTable(int id) {
        return updateStatus(id, "Đã đặt");
    }

    public boolean freeTable(int id) {
        return updateStatus(id, "Trống");
    }
}