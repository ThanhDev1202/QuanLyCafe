package Server.SV;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;
import shared.RequestResponse.Request;
import shared.Model.Account;
import shared.RequestResponse.Response;
import Server.DAO.*;
import database.Connect_Disconnect;
import java.util.ArrayList;
import java.util.List;
import shared.Model.*;
public class ClientHandler implements Runnable {

    private Socket socket;//socket đại diện cho client
    private Connection conn = null;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    
    private AccountDAO acd;
    private FoodDAO fd;
    private CategoryFoodDAO cfd;
    private TableFoodDAO tbdao;

    public ClientHandler(Socket soc_client) {
        this.socket = soc_client;
        this.acd = new AccountDAO();
        this.fd =  new FoodDAO();
        this.cfd = new CategoryFoodDAO();
        this.tbdao = new TableFoodDAO(conn);
        this.conn = Connect_Disconnect.getConnection();
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
        } finally{
            try {
                if(in != null) in.close();
                if(out != null) out.close();
                if(conn != null) Connect_Disconnect.closeConnection();
                if(socket != null) socket.close();                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Response action(Request req) {
        Response res = new Response();
        switch (req.getAction()) {
            // LOGIN
            case "LOGIN": {
                acd.setConn(conn);
                Account ac = (Account) req.getData();
                boolean check_login = acd.checkLogin(ac);
                if (check_login) {
                    res.setStatus("SUCCESS");
                    res.setMessage("LOGIN SUCCESSFUL");
                } else {
                    res.setStatus("FAILED");
                    res.setMessage("LOGIN FAILED");
                }
                break;
            }
            // REGISTER
            case "REGISTER": {
                acd.setConn(conn);
                Account ac = (Account) req.getData();
                boolean check_register = acd.register(ac);
                if (check_register) {
                    res.setStatus("SUCCESS");
                    res.setMessage("REGISTER SUCCESSFUL");
                } else {
                    res.setStatus("FAILED");
                    res.setMessage("REGISTER FAILED");
                }
                break;
            }
            // INSERT CATEGORY
            case "INSERT CATEGORY": {
                cfd.setConn(conn);
                CategoryFood category = (CategoryFood) req.getData();
                boolean check = cfd.insertCategory(category);
                if (check) {
                    res.setStatus("SUCCESS");
                    res.setMessage("INSERT CATEGORY SUCCESSFUL");
                } else {
                    res.setStatus("FAILED");
                    res.setMessage("INSERT CATEGORY FAILED");
                }
                break;
            }
            // DELETE CATEGORY
            case "DELETE CATEGORY": {
                cfd.setConn(conn);
                CategoryFood category = (CategoryFood) req.getData();
                boolean check = cfd.deleteCategory(category);
                if (check) {
                    res.setStatus("SUCCESS");
                    res.setMessage("DELETE CATEGORY SUCCESSFUL");
                } else {
                    res.setStatus("FAILED");
                    res.setMessage("DELETE CATEGORY FAILED");
                }
                break;
            }
            // SELECT CATEGORY
            case "SELECT CATEGORY": {
                cfd.setConn(conn);
                ArrayList<CategoryFood> list = cfd.selectCategory();
                if (list == null || list.isEmpty()) {
                    res.setStatus("NO CATEGORY");
                    res.setMessage("CATEGORY NOT EXIST");
                } else {
                    res.setStatus("SUCCESS");
                    res.setMessage("SELECT CATEGORY SUCCESSFUL");
                    res.setData(list);
                }
                break;
            }           
            // INSERT FOOD
            case "INSERT FOOD": {
                fd.setConn(conn);
                Food food = (Food) req.getData();
                CategoryFood category = new CategoryFood();
                category.setId(food.getIdcategory());
                boolean check = fd.insertFood(category, food);
                if (check) {
                    res.setStatus("SUCCESS");
                    res.setMessage("INSERT FOOD SUCCESSFUL");
                } else {
                    res.setStatus("FAILED");
                    res.setMessage("INSERT FOOD FAILED");
                }
                break;
            }
            // SELECT FOOD
            case "SELECT FOOD BY CATEGORY": {
                fd.setConn(conn);
                int categoryId = (int) req.getData();
                ArrayList<Food> list = fd.selectFoodByCategory(categoryId);
                if (list == null || list.isEmpty()) {
                    res.setStatus("NO FOOD");
                    res.setMessage("CATEGORY HAS NO FOOD");
                    res.setData(new ArrayList<>());
                } else {
                    res.setStatus("SUCCESS");
                    res.setMessage("SELECT FOOD BY CATEGORY SUCCESSFUL");
                    res.setData(list);
                }
                break;
            }
            // DELETE FOOD
            case "DELETE FOOD": {
                fd.setConn(conn);
                Food food = (Food) req.getData();
                boolean check = fd.deleteFood(food);
                if (check) {
                    res.setStatus("SUCCESS");
                    res.setMessage("DELETE FOOD SUCCESSFUL");
                } else {
                    res.setStatus("FAILED");
                    res.setMessage("DELETE FOOD FAILED");
                }
                break;
            }
            case "GET TABLES":
                {          
                List<TableFood> tbflist = tbdao.getAllTables();
                res.setStatus("SUCCESS");
                res.setData(tbflist);
                res.setMessage("GET TABLE SUCCESS");
                return res;        
                }
            case "ADD TABLE":
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
            case "DELETE TABLE":
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
            case "OPEN TABLE":
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
            default: {
                res.setStatus("ERROR");
                res.setMessage("UNKNOWN ACTION: " + req.getAction());
                res.setData(null);
            }
        }
        return res;
    }
}
