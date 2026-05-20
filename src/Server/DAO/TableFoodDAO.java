/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

    public TableFoodDAO(Connection conn) {
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

    public TableFood addtable(TableFood tbf) {
        String name = tbf.getName();
        String status = tbf.getStatus();
        try {
            String sql = "INSERT INTO TableFood(ten, status) OUTPUT INSERTED.id VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, status);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tbf.setId(rs.getInt("id")); // lấy ID SQL vừa tạo
            }
            return tbf;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
