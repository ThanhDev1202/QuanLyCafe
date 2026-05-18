package Server.DAO;
import java.sql.*;
import shared.Account;
public class UserDAO {
    private Connection conn = null;
    public UserDAO(Connection conn){
        this.conn = conn;
    }
    
    public boolean checkLogin(Account acc){
        String username = acc.getUsername();
        String password = acc.getPassword();
        try {
            String sql ="SELECT * FROM Account WHERE username = ? AND pass = ?";
            PreparedStatement ps =conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
}
