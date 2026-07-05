package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connect_Disconnect {


    public static Connection getConnection() {
        try {
            String url =
                "jdbc:sqlserver://localhost:1433;"
                + "databaseName=quanlyquancf;"
                + "encrypt=true;"
                + "trustServerCertificate=true";

            String user = "sa";
            String pass = "1";

            return DriverManager.getConnection(url, user, pass);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}