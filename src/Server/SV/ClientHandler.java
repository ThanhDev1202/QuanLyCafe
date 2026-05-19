package Server.SV;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;
import shared.RequestResponse.Request;
import database.*;
import shared.Model.Account;
import shared.RequestResponse.Response;
import Server.DAO.*;
import java.util.ArrayList;
import java.util.List;
import shared.Model.TableFood;

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
            while (true) {
                Request req = (Request) in.readObject();//nhận request
                Response res = action(req);//trả response
                out.writeObject(res);
                out.flush();
            }
        } catch (Exception e) {
            System.out.println("client Disconected");
        }
    }

    public Response action(Request req) {
        Response res = new Response();
        AccountDAO ud = new AccountDAO(conn);
TableFoodDAO tbdao = new TableFoodDAO(conn);
        switch (req.getAction()) {

            case "LOGIN": {//đăng nhập
                Account ac = (Account) req.getData();

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
            case "REGISTER": {//đăng ký
                Account ac = (Account) req.getData();

                boolean check_register = ud.register(ac);
                if (check_register) {
                    res.setStatus("SUCCESS");
                    res.setMessage("REGISTER SUCCESSFUL");
                } else {
                    res.setStatus("FAILED");
                    res.setMessage("REGISTER FAILED");
                }
                break;
            }
            case "GET_TABLES":
            {          
            List<TableFood> tbflist = tbdao.getAllTables();
            res.setStatus("SUCCESS");
            res.setData(tbflist);
            res.setMessage("GET TABLE SUCCESS");
            return res;        
            }
            case "ADD_TABLE":
            {
                TableFood tbf = (TableFood) req.getData();
                TableFood checkaddtable = tbdao.addtable(tbf);
                 if (checkaddtable != null) {
                    res.setStatus("SUCCESS");
                    res.setMessage("ADD TABLE SUCCESSFULLY");
                    res.setData(tbf);
                } else {
                    res.setStatus("FAILED");
                    res.setMessage("ADD TABLE FAILED");
                }
                break;
            }
            case "DELETE_TABLE":
            {
            TableFood tbf =(TableFood) req.getData();
            boolean checkdelete = tbdao.deletetable(tbf);
              if (checkdelete) {
                    res.setStatus("SUCCESS");
                    res.setMessage("DELETE TABLE SUCCESSFULLY");
                    res.setData(tbf);
                } else {
                    res.setStatus("FAILED");
                    res.setMessage("DELETE TABLE FAILED");
                }
                break;
            
            }
            case "OPEN_TABLE":
            {
            TableFood tbf =(TableFood) req.getData();
            boolean checkopen = tbdao.opentable(tbf);
              if (checkopen) {
                    res.setStatus("SUCCESS");
                    res.setMessage("OPEN TABLE SUCCESSFULLY");
                    res.setData(tbf);
                } else {
                    res.setStatus("FAILED");
                    res.setMessage("OPEN TABLE FAILED");
                }
                break;
            
            }
            default:
                res.setStatus("ERROR");
                res.setMessage("UNKNOWN ACTION: " + req.getAction());
                res.setData(null);
        }
        return res;
    }
}
