package Server.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import shared.Model.TableFood;

/**
 *
 * @author admin
 */
public class TableFoodDAO {

    private Connection conn = null;

    public TableFoodDAO() {
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public List<TableFood> getAllTables() {
        List<TableFood> tbflist = new ArrayList<>();
        try {
            String sql = "SELECT * FROM TableFood";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TableFood tbf = new TableFood(
                        rs.getInt("id"),
                        rs.getString("ten"),
                        rs.getString("status")
                );
                tbflist.add(tbf);
            }
            System.out.println(conn.getCatalog());
            System.out.println(conn.getMetaData().getURL());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tbflist;
    }

    public boolean addtable(TableFood tbf) {
        try {
            String sql = "INSERT INTO TableFood(ten, status) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tbf.getName());
            ps.setString(2, tbf.getStatus());
            int row = ps.executeUpdate();
            return row > 0; 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deletetable(TableFood tbf) {
        int id = tbf.getId();
        try {
            String sql = "DELETE FROM TableFood WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                // Reseed
                PreparedStatement ps2 = conn.prepareStatement("DBCC CHECKIDENT ('TableFood', RESEED)");
                ps2.execute();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean opentable(TableFood tbf) {
        int id = tbf.getId();
        try {
            String sql = "UPDATE TableFood SET status = N'Có người' WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
