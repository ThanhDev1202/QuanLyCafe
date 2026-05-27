package database;
import java.sql.Connection;
import java.sql.DriverManager;
public class Connect_Disconnect {
    public static Connection conn = null;
    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                String url =
                    "jdbc:sqlserver://localhost:1433;"
                    + "databaseName=quanlyquancf;"
                    + "encrypt=true;"
                    + "trustServerCertificate=true";
                String user = "sa";
                String pass = "Thanh2006@";
                conn = DriverManager.getConnection(url,user,pass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
    public static void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}