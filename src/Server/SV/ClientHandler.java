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
import java.util.List;
import shared.TableFood;
public class ClientHandler implements Runnable {

    private Socket socket;//socket đại diện cho client
    private Connection conn = null;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientHandler(Socket soc_client, Connection conn) {
        this.socket = soc_client;
        this.conn = conn;
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
        Account ac = (Account)req.getData();
        UserDAO ud = new UserDAO(conn);
        switch (req.getAction()) {
            case "LOGIN":
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
            case "REGISTER":         
                boolean check_register = ud.register(ac);
                if(check_register == true){
                    res.setStatus("SUCCESS");
                    res.setMessage("REGISTER SUCCESSFUL");
                }else{
                    res.setStatus("FAILED");
                    res.setMessage("REGISTER FAILED"); 
                }
                break;
            default:
                res.setStatus("ERROR");
                res.setMessage("UNKNOWN ACTION");
                res.setData(null);
        }
        return res;
    }

    switch (req.getAction()) {
        case "LOGIN": {
            Account ac = (Account) req.getData();

            UserDAO ud = new UserDAO(conn);
            boolean check_login = ud.checkLogin(ac);

            if (check_login) {
                res.setStatus("SUCCESS");
                res.setMessage("LOGIN SUCCESSFUL");
            } else {
                res.setStatus("FAILED");
                res.setMessage("LOGIN FAILED");
            }
            break;
        }

        case "GET_TABLES": {
            TableDAO tdao = new TableDAO(conn);
            List<TableFood> list = tdao.getAllTables();
            res.setStatus("SUCCESS");
            res.setMessage("GET TABLE SUCCESS");
            res.setData(list);
            break;
        }

        case "BOOK_TABLE": {
            int id = (Integer) req.getData();
            TableDAO tdao = new TableDAO(conn);
            boolean ok = tdao.bookTable(id);

            if (ok) {
                res.setStatus("SUCCESS");
                res.setMessage("Đặt bàn thành công");
            } else {
                res.setStatus("FAILED");
                res.setMessage("Đặt bàn thất bại");
            }
            break;
        }

        case "FREE_TABLE": {
            int id = (Integer) req.getData();
            TableDAO tdao = new TableDAO(conn);
            boolean ok = tdao.freeTable(id);

            if (ok) {
                res.setStatus("SUCCESS");
                res.setMessage("Trả bàn thành công");
            } else {
                res.setStatus("FAILED");
                res.setMessage("Trả bàn thất bại");
            }
            break;
        }

        default:
            res.setStatus("ERROR");
            res.setMessage("UNKNOWN ACTION: " + req.getAction());
    }

    return res;
}
}
