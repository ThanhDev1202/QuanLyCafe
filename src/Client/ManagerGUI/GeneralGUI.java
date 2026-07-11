/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Client.ManagerGUI;

import Client.ClientConnection;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

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
        String cardStyle = "arc:25;";

        setupCard(pnlRevenueChart);
        setupCard(pnlFoodChart);
        setupCard(ThanhVien);
        setupCard(ThongTin);
        setupCard(Account);

        jLabel2.setText(displayname);

        loadData();
        loadFoodData();
        load();
        setupTableStyle();

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
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 400));

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

        jPanel2 = new javax.swing.JPanel();
        PanelLeft = new javax.swing.JPanel();
        pnlRevenueChart = new javax.swing.JPanel();
        pnlFoodChart = new javax.swing.JPanel();
        PanelRight = new javax.swing.JPanel();
        ThanhVien = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        ThongTin = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        Account = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        HeadPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(245, 235, 230));
        setLayout(new java.awt.BorderLayout());

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.BorderLayout(20, 20));

        PanelLeft.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 20, 15));
        PanelLeft.setOpaque(false);
        PanelLeft.setLayout(new java.awt.GridLayout(2, 1, 10, 10));

        javax.swing.GroupLayout pnlRevenueChartLayout = new javax.swing.GroupLayout(pnlRevenueChart);
        pnlRevenueChart.setLayout(pnlRevenueChartLayout);
        pnlRevenueChartLayout.setHorizontalGroup(
            pnlRevenueChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 402, Short.MAX_VALUE)
        );
        pnlRevenueChartLayout.setVerticalGroup(
            pnlRevenueChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2378, Short.MAX_VALUE)
        );

        PanelLeft.add(pnlRevenueChart);

        javax.swing.GroupLayout pnlFoodChartLayout = new javax.swing.GroupLayout(pnlFoodChart);
        pnlFoodChart.setLayout(pnlFoodChartLayout);
        pnlFoodChartLayout.setHorizontalGroup(
            pnlFoodChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 402, Short.MAX_VALUE)
        );
        pnlFoodChartLayout.setVerticalGroup(
            pnlFoodChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2378, Short.MAX_VALUE)
        );

        PanelLeft.add(pnlFoodChart);

        jPanel2.add(PanelLeft, java.awt.BorderLayout.WEST);

        PanelRight.setBackground(new java.awt.Color(255, 204, 102));
        PanelRight.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 20, 15));
        PanelRight.setOpaque(false);
        PanelRight.setLayout(new java.awt.GridLayout(3, 1, 0, 20));

        ThanhVien.setLayout(new java.awt.BorderLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(62, 39, 35));
        jLabel4.setText("Thành viên nhóm");
        jLabel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 10, 1));
        ThanhVien.add(jLabel4, java.awt.BorderLayout.NORTH);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("\n\nNguyễn Chí Thanh - CT090241\n\nVũ Minh Hiệp - CT090218\n\nPhạm Hoàng Hải - CT090217\n\nĐoàn Đức Anh - CT090205\n\n\n");
        jScrollPane1.setViewportView(jTextArea1);

        ThanhVien.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        PanelRight.add(ThanhVien);

        ThongTin.setLayout(new java.awt.BorderLayout());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(62, 39, 35));
        jLabel5.setText("Thông Tin về app");
        jLabel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 10, 1));
        ThongTin.add(jLabel5, java.awt.BorderLayout.NORTH);

        jTextArea2.setEditable(false);
        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextArea2.setRows(5);
        jTextArea2.setText("APP không được sử dụng cho mục đích thương mại. Mọi chức năng và dữ\nliệu trong ứng dụng chỉ mang tính chất minh họa ,phục vụ việc tìm hiểu và\nđánh giá quá trình phát triển phần mềm\n\nGiao diện Manager là khu vực dành riêng cho người quản lý, cung cấp các\nchức năng nghiệp vụ phục vụ công tác điều hành và quản lý quán cà phê");
        jScrollPane2.setViewportView(jTextArea2);

        ThongTin.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        PanelRight.add(ThongTin);

        Account.setLayout(new java.awt.BorderLayout());

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(62, 39, 35));
        jLabel6.setText("Tài khoản");
        jLabel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 10, 1));
        Account.add(jLabel6, java.awt.BorderLayout.NORTH);

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

        Account.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        PanelRight.add(Account);

        jPanel2.add(PanelRight, java.awt.BorderLayout.CENTER);

        add(jPanel2, java.awt.BorderLayout.CENTER);

        HeadPanel.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(62, 39, 35));
        jLabel1.setText("Chào mừng");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 51, 51));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/hi.png"))); // NOI18N
        jLabel2.setText("....");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel2.setIconTextGap(8);

        javax.swing.GroupLayout HeadPanelLayout = new javax.swing.GroupLayout(HeadPanel);
        HeadPanel.setLayout(HeadPanelLayout);
        HeadPanelLayout.setHorizontalGroup(
            HeadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HeadPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addContainerGap(924, Short.MAX_VALUE))
        );
        HeadPanelLayout.setVerticalGroup(
            HeadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HeadPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(HeadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        add(HeadPanel, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void setupTableStyle() {
        JTableHeader header = jTable1.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setPreferredSize(new Dimension(80, 30));
        header.setBackground(new Color(78, 46, 42));
        header.setForeground(Color.WHITE);

        jTable1.setRowHeight(35);

        DefaultTableCellRenderer center
                = new DefaultTableCellRenderer();

        center.setHorizontalAlignment(
                SwingConstants.CENTER);

        for (int i = 0; i < jTable1.getColumnCount(); i++) {
            jTable1.getColumnModel().getColumn(i).setCellRenderer(center);
        }

    }

    private void setupCard(JPanel panel) {

        panel.setOpaque(true);
        panel.setBackground(Color.WHITE);

        panel.putClientProperty(
                FlatClientProperties.STYLE,
                "arc:20");

        panel.setBorder(BorderFactory.createEmptyBorder(
                15, 15, 15, 15));
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Account;
    private javax.swing.JPanel HeadPanel;
    private javax.swing.JPanel PanelLeft;
    private javax.swing.JPanel PanelRight;
    private javax.swing.JPanel ThanhVien;
    private javax.swing.JPanel ThongTin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
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
