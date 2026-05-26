package Server.DAO;
import java.sql.*;
import shared.Model.Account;
public class AccountDAO {
    private Connection conn = null;
    
    public AccountDAO(Connection conn){
        this.conn = conn;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public AccountDAO() {
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
    public int getRole(Account ac) {
        try {
            String sql = "SELECT type FROM Account WHERE username=? AND pass=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ac.getUsername());
            ps.setString(2, ac.getPassword());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("type");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }    
    public boolean register(Account acc){
        int id = acc.getId();
        String username = acc.getUsername();
        String password = acc.getPassword();
        String displayname = acc.getDisplayName();
        int type = acc.getType();
            try {
            String sql ="insert into Account values (?,?,?,?)";
            PreparedStatement ps =conn.prepareStatement(sql);
            ps.setString(1, displayname);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setInt(4, type);
            int result = ps.executeUpdate();
            if(result > 0) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;    
    }
}
