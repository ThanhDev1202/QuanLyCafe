package Server.DAO;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import shared.Model.CategoryFood;

public class CategoryFoodDAO {
    private Connection conn = null;
    public CategoryFoodDAO(Connection conn){
        this.conn = conn;
    }     
    
    //nhập category
    public boolean insertCategory(CategoryFood category){
        String nameCategory = category.getTen();
        try {
            String sql = "INSERT INTO CategoryFood VALUES (?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nameCategory);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    //lấy danh sách category
    public ArrayList<CategoryFood> selectCategory() {
        ArrayList<CategoryFood> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM CategoryFood";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                CategoryFood category = new CategoryFood();
                category.setId(rs.getInt("id"));
                category.setTen(rs.getString("ten"));
                list.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }   
    //xóa category
    public boolean deleteCategory(CategoryFood category){
        int idCategory = category.getId();

        try {
            String sql = "DELETE FROM CategoryFood WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idCategory);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
    