package Server.SV;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;
import shared.RequestResponse.Request;
import shared.Model.Account;
import shared.RequestResponse.Response;
import Server.DAO.*;
import java.util.ArrayList;
import shared.Model.*;

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
        switch (req.getAction()) {
            // LOGIN
            case "LOGIN": {
                AccountDAO ud = new AccountDAO(conn);
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
            // REGISTER
            case "REGISTER": {
                AccountDAO ud = new AccountDAO(conn);
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
            // INSERT CATEGORY
            case "INSERT CATEGORY": {
                CategoryFoodDAO cd = new CategoryFoodDAO(conn);
                CategoryFood category = (CategoryFood) req.getData();
                boolean check = cd.insertCategory(category);
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
                CategoryFoodDAO cd = new CategoryFoodDAO(conn);
                CategoryFood category = (CategoryFood) req.getData();
                boolean check = cd.deleteCategory(category);
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
                CategoryFoodDAO cd = new CategoryFoodDAO(conn);
                ArrayList<CategoryFood> list = cd.selectCategory();
                if (list == null || list.isEmpty()) {
                    res.setStatus("NO CATEGORY");
                    res.setMessage("CATEGORY NOT EXIST");
                } else {
                    res.setStatus("SUCCESS");
                    res.setMessage("CATEGORY EXIST");
                    res.setData(list);
                }
                break;
            }           
            // INSERT FOOD
            case "INSERT FOOD": {
                FoodDAO fd = new FoodDAO(conn);
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
                FoodDAO fd = new FoodDAO(conn);
                int categoryId = (int) req.getData();
                ArrayList<Food> list = fd.selectFoodByCategory(categoryId);
                if (list == null || list.isEmpty()) {
                    res.setStatus("NO_FOOD");
                    res.setMessage("Category has no food");
                    res.setData(new ArrayList<>());
                } else {
                    res.setStatus("SUCCESS");
                    res.setMessage("OK");
                    res.setData(list);
                }
                break;
            }
            // DELETE FOOD
            case "DELETE FOOD": {
                FoodDAO fd = new FoodDAO(conn);
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
            default: {
                res.setStatus("ERROR");
                res.setMessage("UNKNOWN ACTION: " + req.getAction());
                res.setData(null);
            }
        }

        return res;
    }
}
