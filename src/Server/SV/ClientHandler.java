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

    public ClientHandler(Socket soc_client) {
        this.socket = soc_client;
        this.acd = new AccountDAO();
        this.fd = new FoodDAO();
        this.cfd = new CategoryFoodDAO();
        this.tbdao = new TableFoodDAO();
        this.bd = new BillDAO();
        this.bid = new BillInforDAO();
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
            // Thêm tài khoản
            case "ADD ACCOUNT": {
                acd.setConn(conn);
                Account ac = (Account) req.getData();
                if (acd.addAccount(ac)) { // Gọi hàm thêm trong DAO
                    res.setStatus("SUCCESS");
                    res.setMessage("Thêm tài khoản thành công!");
                } else {
                    res.setStatus("FAILED");
                    res.setMessage("Thêm tài khoản thất bại!");
                }
                break;
            }

            // Xóa tài khoản
            case "DELETE ACCOUNT": { // Chú ý: bạn đã viết sai chính tả "ACCOUT" -> sửa thành "ACCOUNT"
                acd.setConn(conn);
                Account ac = (Account) req.getData();
                if (acd.deleteAccount(ac.getId())) { // Gọi hàm xóa theo ID trong DAO
                    res.setStatus("SUCCESS");
                    res.setMessage("Xóa tài khoản thành công!");
                } else {
                    res.setStatus("FAILED");
                    res.setMessage("Xóa tài khoản thất bại!");
                }
                break;
            }

            // Sửa tài khoản
            case "UPDATE ACCOUNT": {
                acd.setConn(conn);
                Account ac = (Account) req.getData();
                if (acd.updateAccount(ac)) { // Gọi hàm sửa trong DAO
                    res.setStatus("SUCCESS");
                    res.setMessage("Cập nhật thành công!");
                } else {
                    res.setStatus("FAILED");
                    res.setMessage("Cập nhật thất bại!");
                }
                break;
            }

            // Lấy danh sách tài khoản
            case "GET ACCOUNTS": {
                acd.setConn(conn);
                List<Account> list = acd.getAllAccounts(); // Gọi hàm lấy danh sách
                if (list != null) {
                    res.setStatus("SUCCESS");
                    res.setData(list);
                } else {
                    res.setStatus("FAILED");
                    res.setMessage("Không thể lấy danh sách tài khoản!");
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
            case "CREATE ORDER": {
                try {
                    // 1. Nhận dữ liệu từ Client: {int tableId, List<BillInfor> list}
                    Object[] data = (Object[]) req.getData();
                    int tableId = (int) data[0];
                    List<BillInfor> listBillInfor = (List<BillInfor>) data[1];
                    java.math.BigDecimal totalFromClient = (java.math.BigDecimal) data[2];
                    // 2. Tắt auto-commit để bắt đầu Transaction
                    conn.setAutoCommit(false);
                    // 3. Tạo Bill mới
                    Bill newBill = new Bill();
                    newBill.setTableID(tableId);
                    newBill.setDateCheckIn(new java.util.Date()); // Thời gian hiện tại
                    newBill.setDiscount(0);
                    newBill.setTotalPrice(totalFromClient);;
                    newBill.setStatus(0); // 0: Chưa thanh toán
                    bd.setConn(conn);
                    int billId = bd.createBillAndGetId(newBill);
                    if (billId != -1) {
                        // 4. Lưu danh sách món ăn vào BillInfo
                        bid.setConn(conn);
                        boolean success = bid.saveBillInfors(billId, listBillInfor);
                        // 5. Cập nhật trạng thái bàn (Giả sử bạn có hàm updateTableStatus trong TableFoodDAO)
                        tbdao.setConn(conn);
                        boolean updateTable = tbdao.updateTableStatus(tableId, "có khách"); // 1: Có khách
                        if (success && updateTable) {
                            conn.commit(); // Lưu tất cả nếu thành công
                            res.setStatus("SUCCESS");
                            res.setMessage("Tạo hóa đơn thành công!");
                        } else {
                            conn.rollback(); // Hủy bỏ nếu có lỗi
                            res.setStatus("FAILED");
                            res.setMessage("Tạo hóa đơn thất bại!");
                        }
                    } else {
                        conn.rollback();
                        res.setStatus("FAILED");
                        res.setMessage("Không thể tạo hóa đơn!");
                    }
                    conn.setAutoCommit(true); // Trả lại trạng thái ban đầu
                } catch (Exception e) {
                    try {
                        conn.rollback();
                        conn.setAutoCommit(true);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    res.setStatus("ERROR");
                    res.setMessage("Lỗi hệ thống: " + e.getMessage());
                }
                break;
            }
            case "PAY BILL": {
                try {
                    // 1. Nhận tableId từ Client
                    int tableId = (int) req.getData();
                    // 2. Bắt đầu Transaction
                    conn.setAutoCommit(false);
                    // 3. Khởi tạo và thực hiện qua DAO
                    bd.setConn(conn);
                    tbdao.setConn(conn);
                    // Thực hiện cập nhật hóa đơn sang "Đã thanh toán" (status = 1)
                    boolean billUpdated = bd.payBill(tableId); // Lưu ý: hàm này cần được định nghĩa trong BillDAO
                    // Thực hiện giải phóng bàn về "Trống"
                    boolean tableUpdated = tbdao.updateTableStatus(tableId, "Trống");
                    if (billUpdated && tableUpdated) {
                        conn.commit(); // Lưu thay đổi
                        res.setStatus("SUCCESS");
                        res.setMessage("Thanh toán thành công và đã giải phóng bàn.");
                    } else {
                        conn.rollback(); // Hủy nếu một trong hai bước thất bại
                        res.setStatus("FAILED");
                        res.setMessage("Thanh toán thất bại: Không tìm thấy hóa đơn hoặc lỗi dữ liệu.");
                    }
                    conn.setAutoCommit(true); // Trả lại chế độ mặc định
                } catch (Exception e) {
                    try {
                        conn.rollback();
                        conn.setAutoCommit(true);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    res.setStatus("ERROR");
                    res.setMessage("Lỗi hệ thống khi thực hiện thanh toán: " + e.getMessage());
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
