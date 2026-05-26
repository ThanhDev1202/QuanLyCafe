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
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import shared.Model.*;
import java.io.File;
import java.nio.file.Files;

public class ClientHandler implements Runnable {

    private Socket socket;//socket đại diện cho client
    private Connection conn = null;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private AccountDAO acd;
    private FoodDAO fd;
    private CategoryFoodDAO cfd;
    private TableFoodDAO tbdao;
    private BillDAO bd;
    private BillInforDAO bid;
    private BillTableDAO btd;

    public ClientHandler(Socket soc_client) {
        this.socket = soc_client;
        this.acd = new AccountDAO();
        this.fd = new FoodDAO();
        this.cfd = new CategoryFoodDAO();
        this.tbdao = new TableFoodDAO();
        this.bd = new BillDAO();
        this.bid = new BillInforDAO();
        this.btd = new BillTableDAO();
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
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (conn != null) {
                    Connect_Disconnect.closeConnection();
                }
                if (socket != null) {
                    socket.close();
                }
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
                int role = acd.getRole(ac);
                if (role != -1) {
                    res.setStatus("SUCCESS");
                    res.setMessage("LOGIN SUCCESSFUL");
                    res.setData(role);
                } else {
                    res.setStatus("FAILED");
                    res.setMessage("LOGIN FAILED");
                    res.setData(-1);
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
            case "UPDATE QUANTITY": {
                fd.setConn(conn);
                Food food = (Food) req.getData();
                // Gọi phương thức DAO đã tạo trước đó
                boolean check = fd.updateFoodQuantity(food.getId(), food.getNumbers());
                if (check) {
                    res.setStatus("SUCCESS");
                    res.setMessage("UPDATE QUANTITY SUCCESSFUL");
                } else {
                    res.setStatus("FAILED");
                    res.setMessage("UPDATE QUANTITY FAILED");
                }
                break;
            }
            case "UPLOAD IMAGE": {
                try {
                    fd.setConn(conn);
                    Food food = (Food) req.getData();
                    // tạo tên file tránh trùng
                    String fileName
                            = System.currentTimeMillis()
                            + "_"
                            + food.getImagePath();
                    // nơi lưu ảnh
                    String savePath = "images/" + fileName;
                    // lưu file
                    File dir = new File("images");
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    FileOutputStream fos = new FileOutputStream(savePath);
                    fos.write(food.getImageData());
                    fos.close();
                    // save DB
                    boolean check = fd.updateFoodImage(food.getId(), savePath);
                    if (check) {
                        res.setStatus("SUCCESS");
                        res.setMessage("UPLOAD IMAGE FOOD SUCCESSFUL");
                    } else {
                        res.setStatus("FAILED");
                        res.setMessage("UPLOAD IMAGE FOOD FAILED");
                    }
                } catch (Exception e) {
                    res.setStatus("ERROR");
                    res.setMessage("UPLOAD IMAGE ERROR");
                }
                break;
            }
            case "SHOW IMAGE": {
                try {
                    fd.setConn(conn);
                    int foodId = (int) req.getData();
                    // lấy path ảnh từ DB
                    String imagePath = fd.getImagePath(foodId);
                    File file = new File(imagePath);
                    if (!file.exists()) {
                        res.setStatus("FAILED");
                        res.setMessage("IMAGE NOT FOUND");
                        break;
                    }
                    byte[] imageBytes = Files.readAllBytes(file.toPath());
                    Food food = new Food();
                    food.setId(foodId);
                    food.setImageData(imageBytes);
                    res.setStatus("SUCCESS");
                    res.setData(food);
                } catch (Exception e) {
                    e.printStackTrace();
                    res.setStatus("ERROR");
                    res.setMessage("SHOW IMAGE ERROR");
                }
                break;
            }
            case "DELETE IMAGE": {
                try {
                    fd.setConn(conn);
                    Food food = (Food) req.getData();
                    // lấy path từ DB
                    String imagePath = fd.getImagePath(food.getId());
                    File file = new File(imagePath);
                    // xóa file vật lý
                    if (file.exists()) {
                        file.delete();
                    }
                    // update DB
                    boolean check = fd.deleteFoodImage(food.getId());
                    if (check) {
                        res.setStatus("SUCCESS");
                        res.setMessage("DELETE IMAGE SUCCESS");
                    } else {
                        res.setStatus("FAILED");
                        res.setMessage("DELETE IMAGE FAILED");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    res.setStatus("ERROR");
                    res.setMessage("DELETE IMAGE ERROR");
                }
                break;
            }
            case "GET TABLES": {
                tbdao.setConn(conn);
                List<TableFood> tbflist = tbdao.getAllTables();
                if (tbflist != null) {
                    res.setStatus("SUCCESS");
                    res.setData(tbflist);
                    res.setMessage("GET TABLE SUCCESS");
                } else {
                    res.setStatus("FAILED");
                    res.setMessage("GET TABLE FAILED");
                }
                break;
            }
            case "ADD TABLE": {
                tbdao.setConn(conn);
                TableFood tbf = (TableFood) req.getData();
                boolean ok = tbdao.addtable(tbf);
                if (ok) {
                    res.setStatus("SUCCESS");
                    res.setMessage("ADD TABLE SUCCESSFULLY");
                    res.setData(null);
                } else {
                    res.setStatus("FAILED");
                    res.setMessage("ADD TABLE FAILED");
                    res.setData(null);
                }
                break;
            }
            case "DELETE TABLE": {
                tbdao.setConn(conn);
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
            default: {
                res.setStatus("ERROR");
                res.setMessage("UNKNOWN ACTION: " + req.getAction());
                res.setData(null);
            }
        }
        return res;
    }
}
