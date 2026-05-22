/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.DAO;

import java.sql.*;
import database.Connect_Disconnect;
import shared.Model.Bill;

/**
 *
 * @author Hiep
 */
public class BillDAO {
     private Connection conn;

    public BillDAO() {
        conn = Connect_Disconnect.getConnection();
    }

    // Tạo bill mới
    public void insertBill(int tableId) {

        String sql =
        "INSERT INTO Bill(idTable, status) VALUES(?, 0)";

        try {

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, tableId);

            ps.executeUpdate();

            System.out.println("Insert bill success");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lấy bill chưa thanh toán theo bàn
    public Bill getUnCheckBillByTableId(int tableId) {

        String sql =
        "SELECT * FROM Bill WHERE idTable = ? AND status = 0";

        try {

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, tableId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Bill b = new Bill();

                b.setId(rs.getInt("id"));
                b.setDateCheckIn(rs.getTimestamp("DateCheckIn"));
                b.setDateCheckOut(rs.getTimestamp("DateCheckOut"));
                b.setIdTable(rs.getInt("idTable"));
                b.setDiscount(rs.getInt("discount"));
                b.setTotalPrice(rs.getDouble("totalPrice"));
                b.setStatus(rs.getInt("status"));

                return b;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Thanh toán bill
    public void checkOut(int billId, double totalPrice) {

        String sql =
        "UPDATE Bill " +
        "SET status = 1, " +
        "DateCheckOut = GETDATE(), " +
        "totalPrice = ? " +
        "WHERE id = ?";

        try {

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setDouble(1, totalPrice);
            ps.setInt(2, billId);

            ps.executeUpdate();

            System.out.println("Checkout success");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
