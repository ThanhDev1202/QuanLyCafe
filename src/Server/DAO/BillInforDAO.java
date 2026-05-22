/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.DAO;

import database.Connect_Disconnect;
import java.sql.*;
import java.util.ArrayList;
import shared.Model.BillInfor;
/**
 *
 * @author Hiep
 */
public class BillInforDAO {
     private Connection conn;

    public BillInforDAO() {
        conn = Connect_Disconnect.getConnection();
    }
// thêm món vào bill
    public void insertBillInfo(
            int idBill,
            int idFood,
            int quantity,
            double price) {

        try {

            // kiểm tra món đã tồn tại chưa
            String checkSql =
            "SELECT * FROM BillInfo " +
            "WHERE idBill = ? AND idFood = ?";

            PreparedStatement checkPs =
                    conn.prepareStatement(checkSql);

            checkPs.setInt(1, idBill);
            checkPs.setInt(2, idFood);

            ResultSet rs = checkPs.executeQuery();

            // nếu món đã tồn tại
            if(rs.next()){

                int currentQuantity =
                        rs.getInt("quantity");

                int newQuantity =
                        currentQuantity + quantity;

                String updateSql =
                "UPDATE BillInfo " +
                "SET quantity = ? " +
                "WHERE idBill = ? AND idFood = ?";

                PreparedStatement updatePs =
                        conn.prepareStatement(updateSql);

                updatePs.setInt(1, newQuantity);
                updatePs.setInt(2, idBill);
                updatePs.setInt(3, idFood);

                updatePs.executeUpdate();

                System.out.println("Update quantity success");
            }

            // nếu chưa có món
            else{

                String insertSql =
                "INSERT INTO BillInfo " +
                "(idBill, idFood, quantity, price) " +
                "VALUES(?,?,?,?)";

                PreparedStatement insertPs =
                        conn.prepareStatement(insertSql);

                insertPs.setInt(1, idBill);
                insertPs.setInt(2, idFood);
                insertPs.setInt(3, quantity);
                insertPs.setDouble(4, price);

                insertPs.executeUpdate();

                System.out.println("Insert BillInfo success");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // lấy danh sách món theo bill
    public ArrayList<BillInfor> getListByBillId(int idBill){

        ArrayList<BillInfor> list = new ArrayList<>();

        String sql =
        "SELECT * FROM BillInfo WHERE idBill = ?";

        try {

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, idBill);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                BillInfor bi = new BillInfor();

                bi.setId(rs.getInt("id"));
                bi.setIdBill(rs.getInt("idBill"));
                bi.setIdFood(rs.getInt("idFood"));
                bi.setQuantity(rs.getInt("quantity"));
                bi.setPrice(rs.getDouble("price"));

                list.add(bi);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // xóa món khỏi bill
    public void deleteBillInfo(int idBill, int idFood){

        String sql =
        "DELETE FROM BillInfo " +
        "WHERE idBill = ? AND idFood = ?";

        try {

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, idBill);
            ps.setInt(2, idFood);

            ps.executeUpdate();

            System.out.println("Delete BillInfo success");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // tính tổng tiền bill
    public double getTotalPrice(int idBill){

        double total = 0;

        String sql =
        "SELECT quantity, price " +
        "FROM BillInfo " +
        "WHERE idBill = ?";

        try {

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, idBill);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                int quantity =
                        rs.getInt("quantity");

                double price =
                        rs.getDouble("price");

                total += quantity * price;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }
}