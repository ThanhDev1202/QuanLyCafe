
package Server.SV;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {
    private static final int port = 1234;
    private static ArrayList<ClientHandler> list = new ArrayList<>();
    public static void main(String[] args) {
        System.out.println("Server opend on port " + port);
        try {
            ServerSocket sv_soc = new ServerSocket(port);
            while(true){   
                //lắng nghe  kết nối
                Socket cl_soc = sv_soc.accept();
                System.err.println("1 client conneted:" + cl_soc.getInetAddress());
                //tạo client handler
                ClientHandler ch = new ClientHandler(cl_soc); //chuyển client->clienthandler xử lý
                list.add(ch); //thêm vào danh sách quản lý
                Thread t = new Thread(ch);
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
