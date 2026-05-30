package Server.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import shared.Model.CategoryFood;
import shared.Model.Food;
import java.math.BigDecimal;

public class FoodDAO {

    private Connection conn = null;

    public FoodDAO() {
    }

    public FoodDAO(Connection conn) {
        this.conn = conn;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    //nhập food
    public boolean insertFood(CategoryFood category, Food food) {
        String ten = food.getNameFood();
        int idCategory = category.getId();
        BigDecimal pricein = food.getPriceIn();
        BigDecimal priceout = food.getPriceOut();
        int soLuong = food.getNumbers();
        try {
            String sql = "INSERT INTO Food(ten, idCategory, priceIn  , soLuong , priceOut) VALUES (?, ?, ?,? ,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ten);
            ps.setInt(2, idCategory);
            ps.setBigDecimal(3, pricein);
            ps.setInt(4, soLuong);
            ps.setBigDecimal(5, priceout);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // lấy danh sách food theo category
    public ArrayList<Food> selectFoodByCategory(int categoryID) {
        ArrayList<Food> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Food WHERE idCategory = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, categoryID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Food food = new Food();
                food.setId(rs.getInt("id"));
                food.setNameFood(rs.getString("ten"));
                food.setPriceIn(rs.getBigDecimal("priceIn"));
                food.setIdcategory(rs.getInt("idCategory"));
                food.setNumbers(rs.getInt("soLuong"));
                food.setPriceOut(rs.getBigDecimal("priceOut"));
                food.setImagePath(rs.getString("imagePath"));
                list.add(food);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    // sửa số lượng food
    public boolean updateFoodQuantity(int foodId, int newQuantity) {
        try {
            String sql = "UPDATE Food SET soLuong = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, newQuantity);
            ps.setInt(2, foodId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // xóa food
    public boolean deleteFood(Food food) {
        int idFood = food.getId();
        try {
            String sql = "DELETE FROM Food WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idFood);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //thêm ảnh cho food
    public boolean updateFoodImage(int foodId, String imagePath) {
        try {
            String sql = "UPDATE Food SET imagePath = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, imagePath);
            ps.setInt(2, foodId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //xóa ảnh cho food
    public boolean deleteFoodImage(int foodId) {
        try {
            String sql = "UPDATE Food SET imagePath = NULL WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, foodId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //lấy đường dẫn 
    public String getImagePath(int foodId) {
        try {
            String sql = "SELECT imagePath FROM Food WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, foodId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("imagePath");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
