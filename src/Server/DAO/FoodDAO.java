package Server.DAO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import shared.Model.CategoryFood;
import shared.Model.Food;
import java.math.BigDecimal;
public class FoodDAO {
    private Connection conn = null;
    public FoodDAO(Connection conn){
        this.conn = conn;
    }    
    //nhập food
    public boolean insertFood(CategoryFood category, Food food){
        String ten = food.getNameFood();
        int idCategory = category.getId();
        BigDecimal price = food.getPrice();
        try {
            String sql = "INSERT INTO Food(ten, price, idCategory) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ten);
            ps.setBigDecimal(2, price);
            ps.setInt(3, idCategory);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    // lấy danh sách food theo category
    public ArrayList<Food> selectFoodByCategory(int categoryID){
        ArrayList<Food> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Food WHERE idCategory = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, categoryID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Food food = new Food();
                food.setId(rs.getInt("id"));
                food.setNameFood(rs.getString("ten"));
                food.setPrice(rs.getBigDecimal("price"));
                food.setIdcategory(rs.getInt("idCategory"));
                list.add(food);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // xóa food
    public boolean deleteFood(Food food){
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
}
