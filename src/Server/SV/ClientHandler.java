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
import java.util.List;
import shared.Model.TableFood;
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
        AccountDAO ud = new AccountDAO(conn);
        TableFoodDAO tbdao = new TableFoodDAO(conn);
        switch (req.getAction()) {
            // LOGIN
            case "LOGIN": {
                AccountDAO UD = new AccountDAO(conn);
                Account ac = (Account) req.getData();
                boolean check_login = UD.checkLogin(ac);
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
                AccountDAO Ud = new AccountDAO(conn);
                Account ac = (Account) req.getData();
                boolean check_register = Ud.register(ac);
                if (check_register) {
                    res.setStatus("SUCCESS");
                    res.setMessage("REGISTER SUCCESSFUL");
                } else {
                    res.setStatus("FAILED");
                    res.setMessage("REGISTER FAILED");
                }
                break;
            }
            case "GET_TABLES": {
                List<TableFood> tbflist = tbdao.getAllTables();
                res.setStatus("SUCCESS");
                res.setData(tbflist);
                res.setMessage("GET TABLE SUCCESS");
                break;
            }
            case "ADD_TABLE": {
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
            case "DELETE_TABLE": {
                TableFood tbf = (TableFood) req.getData();
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
            case "OPEN_TABLE": {
                TableFood tbf = (TableFood) req.getData();
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
            //ADD_FOOD
            case "ADD_FOOD": {

                Object[] data = (Object[]) req.getData();

                int idBill = (int) data[0];
                int idFood = (int) data[1];
                int quantity = (int) data[2];
                double price = (double) data[3];

                BillInforDAO dao = new BillInforDAO(conn);

                dao.insertBillInfo(
                        idBill,
                        idFood,
                        quantity,
                        price);
                res.setStatus("SUCCESS");
                res.setMessage("ADD FOOD SUCCESS");

                break;
            }
            //DELETE_BILLINFO
            case "DELETE_BILLINFO": {

                Object[] data = (Object[]) req.getData();
                int idBill = (int) data[0];
                int idFood = (int) data[1];

                BillInforDAO dao = new BillInforDAO(conn);
                dao.deleteBillInfo(
                        idBill,
                        idFood);
                res.setStatus("SUCCESS");
                res.setMessage("DELETE BILLINFO SUCCESS");

                break;
            }
            //CHECKOUT_BILL
            case "CHECKOUT_BILL": {

                int idBill = (int) req.getData();
                double totalPrice = (double) req.getData();
                BillDAO dao = new BillDAO(conn);
                boolean check = dao.checkOut(idBill, totalPrice);

                if (check) {

                    res.setStatus("SUCCESS");
                    res.setMessage(
                            "CHECKOUT SUCCESS");
                } else {

                    res.setStatus("FAILED");
                    res.setMessage(
                            "CHECKOUT FAILED");
                }

                break;
            }
            //GET_TOTAL_PRICE
            case "GET_TOTAL_PRICE": {

                int idBill = (int) req.getData();
                BillInforDAO dao = new BillInforDAO(conn);
                double totalPrice = dao.getTotalPrice(idBill);
                res.setStatus("SUCCESS");
                res.setData(totalPrice);
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
