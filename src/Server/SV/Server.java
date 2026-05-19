package Server.SV;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.sql.*;
import database.Connect_Disconnect;
public class Server {
    private static final int port = 1234;
    private static ArrayList<ClientHandler> list = new ArrayList<>();
    private static Connection conn = null;
    public static void main(String[] args) {
        System.out.println("Server opend on port " + port);
        try {
            ServerSocket sv_soc = new ServerSocket(port);
            conn = Connect_Disconnect.getConnection();
            while(true){   
                //lắng nghe  kết nối
                Socket cl_soc = sv_soc.accept();
                System.out.println("1 client conneted:" + cl_soc.getInetAddress());
                //tạo client handler
                ClientHandler ch = new ClientHandler(cl_soc,conn); //chuyển client->clienthandler xử lý
                list.add(ch); //thêm vào danh sách quản lý
                Thread t = new Thread(ch);
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
