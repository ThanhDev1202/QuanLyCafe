package Server.SV;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;
import shared.Request;
import database.*;
import shared.Account;
import shared.Response;
import Server.DAO.*;

public class ClientHandler implements Runnable {

    private Socket socket;//socket đại diện cho client
    private Connection conn = null;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientHandler(Socket soc_client) {
        this.socket = soc_client;
        this.conn = Connect_Disconnect.getConnection();
    }

    @Override
    public void run() {
        try {
            // tạo IO trước
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            while(true){
                Request req = (Request)in.readObject();//nhận request
                Response res = action(req);//trả response
                out.writeObject(res);
                out.flush();
            }
        } catch (Exception e) {
            System.out.println("client Disconected");
        }
    }
    public Response action(Request req){
        Response res = new Response();
        switch (req.getAction()) {
            case "LOGIN":
                Account ac = (Account)req.getData();
                UserDAO ud = new UserDAO(conn);
                boolean check_login = ud.checkLogin(ac);
                if(check_login == true){
                    res.setStatus("SUCCESS");
                    res.setMessage("LOGIN SUCCESSFUL");
                    
                }
                else{
                    res.setStatus("FAILED");
                    res.setMessage("LOGIN FAILED");
                }
                break;
            default:
                res.setStatus("ERROR");
                res.setMessage("UNKNOWN ACTION");
                res.setData(null);
        }
        return res;
    }
}
