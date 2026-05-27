package Server.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import shared.Model.Account;

public class AccountDAO {

    private Connection conn = null;

    public AccountDAO() {
    }

    public AccountDAO(Connection conn) {
        this.conn = conn;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public boolean checkLogin(Account acc) {
        String username = acc.getUsername();
        String password = acc.getPassword();
        try {
            String sql = "SELECT * FROM Account WHERE username = ? AND pass = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
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
// 1. Lấy tất cả tài khoản

    public List<Account> getAllAccounts() {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM Account";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Account ac = new Account();
                ac.setId(rs.getInt("id"));
                ac.setUsername(rs.getString("username"));
                ac.setDisplayName(rs.getString("displayname"));
                ac.setType(rs.getInt("type"));
                // Lưu ý: không nên lấy mật khẩu về client nếu không cần thiết
                list.add(ac);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

// 2. Thêm tài khoản
    public boolean addAccount(Account ac) {
        String sql = "INSERT INTO Account (username, pass, displayname, type) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ac.getUsername());
            ps.setString(2, ac.getPassword());
            ps.setString(3, ac.getDisplayName());
            ps.setInt(4, ac.getType());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

// 3. Sửa tài khoản (Update type)
    public boolean updateAccount(Account ac) {
        String sql = "UPDATE Account SET type = ? WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ac.getType());
            ps.setInt(2, ac.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

// 4. Xóa tài khoản
    public boolean deleteAccount(int id) {
        String sql = "DELETE FROM Account WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
