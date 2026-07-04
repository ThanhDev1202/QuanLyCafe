/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Client.ManagerGUI;

import Client.ClientConnection;
import java.awt.BorderLayout;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JOptionPane;

import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import org.jfree.data.category.DefaultCategoryDataset;
import shared.Model.Account;
import shared.Model.Bill;
import shared.Model.BillInfor;
import shared.RequestResponse.Request;
import shared.RequestResponse.Response;

/**
 *
 * @author admin
 */
public class GeneralGUI extends javax.swing.JPanel {

    private List<Bill> bills;
    private Map<String, Integer> foodData = new TreeMap<>();
    private List<Account> allAccounts = new java.util.ArrayList<>();

    public GeneralGUI(String displayname) {
        initComponents();
        jLabel2.setText(displayname);
        loadData();
        loadFoodData();
        load();
    }

    private void loadData() {
        try {
            Request req = new Request("GET ALL BILLS", null);
            Response res = (Response) ClientConnection.sendRequest(req);
            if (res != null && "SUCCESS".equals(res.getStatus())) {
                this.bills = (List<Bill>) res.getData();
                updateCharts();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFoodData() {
        try {
            Request req = new Request("GET FOOD FREQ", null);
            Response res = (Response) ClientConnection.sendRequest(req);

            if (res != null && "SUCCESS".equals(res.getStatus())) {
                List<BillInfor> list = (List<BillInfor>) res.getData();
                foodData.clear();

                for (BillInfor b : list) {
                    foodData.put(
                            b.getFoodName(),
                            foodData.getOrDefault(b.getFoodName(), 0) + b.getQuantity()
                    );
                }
                showFoodBarChart(); // Gọi hàm vẽ với dữ liệu đã được cập nhật vào map foodData
            } else {
                JOptionPane.showMessageDialog(this, "Không lấy được dữ liệu sản phẩm");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCharts() {
        showRevenueChart();
        showFoodBarChart();
    }

    private void showRevenueChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Nhóm doanh thu theo ngày
        Map<String, BigDecimal> map = new TreeMap<>();
        for (Bill b : bills) {
            if (b.getStatus() == 1 && b.getDateCheckOut() != null) {
                String date = sdf.format(b.getDateCheckOut());
                map.put(date, map.getOrDefault(date, BigDecimal.ZERO).add(b.getTotalPrice()));
            }
        }

        for (var entry : map.entrySet()) {
            dataset.addValue(entry.getValue(), "VNĐ", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createLineChart("Doanh thu", "Ngày", "VNĐ", dataset);
        renderToPanel(pnlRevenueChart, chart);
    }

    private void showFoodBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> e : foodData.entrySet()) {
            dataset.addValue(e.getValue(), "Số lượng", e.getKey());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Top sản phẩm bán chạy",
                "Món ăn",
                "Số lượng",
                dataset
        );

        renderToPanel(pnlFoodChart, barChart);
    }

    private void renderToPanel(JPanel panel, JFreeChart chart) {
        panel.removeAll();
        panel.setLayout(new java.awt.BorderLayout());

        // 1. Khởi tạo đối tượng ChartPanel
        ChartPanel chartPanel = new ChartPanel(chart);

        // 2. Thiết lập kích thước mong muốn cho nó
        chartPanel.setPreferredSize(new java.awt.Dimension(1000, 500));

        // 3. Thêm vào panel chính
        panel.add(chartPanel, java.awt.BorderLayout.CENTER);

        panel.revalidate();
        panel.repaint();
    }

    public void load() {
        try {
            Request req = new Request("GET ACCOUNTS", null);
            ClientConnection.getOut().writeObject(req);
            ClientConnection.getOut().flush();
            Response res = (Response) ClientConnection.getIn().readObject();

            if (res.getStatus().equals("SUCCESS")) {

                allAccounts = (List<Account>) res.getData(); // LƯU LẠI

                renderTable(allAccounts);

            } else {
                JOptionPane.showMessageDialog(this, "Lỗi: " + res.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderTable(List<Account> list) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        for (Account ac : list) {
            String role = ac.getType() == 0 ? "Staff" : "Manager";

            model.addRow(new Object[]{
                ac.getId(),
                ac.getUsername(),
                role
            });
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        pnlRevenueChart = new javax.swing.JPanel();
        pnlFoodChart = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel1.setText("chào mừng");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel2.setText("....");

        javax.swing.GroupLayout pnlRevenueChartLayout = new javax.swing.GroupLayout(pnlRevenueChart);
        pnlRevenueChart.setLayout(pnlRevenueChartLayout);
        pnlRevenueChartLayout.setHorizontalGroup(
            pnlRevenueChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 402, Short.MAX_VALUE)
        );
        pnlRevenueChartLayout.setVerticalGroup(
            pnlRevenueChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 251, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlFoodChartLayout = new javax.swing.GroupLayout(pnlFoodChart);
        pnlFoodChart.setLayout(pnlFoodChartLayout);
        pnlFoodChartLayout.setHorizontalGroup(
            pnlFoodChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 402, Short.MAX_VALUE)
        );
        pnlFoodChartLayout.setVerticalGroup(
            pnlFoodChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 282, Short.MAX_VALUE)
        );

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("THÔNG TIN NHANH");

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setText("Nguyễn Chí Thanh - CT090241\nVũ Minh Hiệp - CT090218\nPhạm Hoàng Hải - CT090217\nĐoàn Đức Anh - CT");
        jScrollPane1.setViewportView(jTextArea1);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel4.setText("Thành viên nhóm");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel5.setText("Thông Tin về app");

        jTextArea2.setEditable(false);
        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextArea2.setRows(5);
        jTextArea2.setText("APP không được sử dụng cho mục đích\nthương mại. Mọi chức năng và dữ liệu\ntrong ứng dụng chỉ mang tính chất \nminh họa ,phục vụ việc tìm hiểu và\nđánh giá quá trình phát triển phần mềm\n\nGiao diện Manager là khu vực dành riêng\ncho người quản lý, cung cấp các chức \nnăng nghiệp vụ phục vụ công tác điều \nhành và quản lý quán cà phê");
        jScrollPane2.setViewportView(jTextArea2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 468, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 392, Short.MAX_VALUE)
        );

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Tài khoản");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Username", "Role"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2))
                            .addComponent(jLabel3)))
                    .addComponent(pnlRevenueChart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlFoodChart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(620, 620, 620)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addGap(27, 27, 27)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pnlRevenueChart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pnlFoodChart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(69, 69, 69)
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JPanel pnlFoodChart;
    private javax.swing.JPanel pnlRevenueChart;
    // End of variables declaration//GEN-END:variables
}
