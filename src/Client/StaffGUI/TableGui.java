package Client.StaffGUI;

import java.awt.Color;
import Client.ClientConnection;
import com.formdev.flatlaf.FlatLightLaf;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import shared.Model.Bill;
import shared.Model.BillInfor;
import shared.Model.TableFood;
import shared.RequestResponse.Request;
import shared.RequestResponse.Response;
import java.text.DecimalFormat;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author admin
 */
public class TableGui extends javax.swing.JPanel { //chịu trách nhiệm hiển thị danh sách bàn ăn

    private final java.util.Map<Integer, ImageIcon> qrCache
            = new java.util.concurrent.ConcurrentHashMap<>();
    /**
     * Creates new form TableGui
     */
    private final DecimalFormat df = new DecimalFormat("#,###");

    public TableGui() {

        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        initComponents();

    }

     public void loadTables() {

        new Thread(() -> {
            try {
                // Gọi qua lớp ClientConnection đã được synchronized
                Response res = (Response) ClientConnection.sendRequest(new Request("GET TABLES", null));
                List<TableFood> tableList = (List<TableFood>) res.getData();
                javax.swing.SwingUtilities.invokeLater(() -> {
                    removeAll();
                    setLayout(new GridLayout(0, 4, 20, 20));
                    setBackground(new Color(245, 235, 230));
                    setBorder(new EmptyBorder(15, 15, 15, 15));
                    String styleAvailable = "arc: 999; borderWidth: 2; "
                            + "borderColor: #A5D6A7; background: #E8F5E9; foreground: #2E7D32; "
                            + "hoverBackground: #C8E6C9; hoverBorderColor: #4CAF50; "
                            + "pressedBackground: #A5D6A7;"; // Cảm giác nhấn: Nền lún đậm xuống màu xanh cỏ úa

                    String styleOccupied = "arc: 999; borderWidth: 2; "
                            + "borderColor: #FFCDD2; background: #FFEBEE; foreground: #C62828; "
                            + "hoverBackground: #FFCDD2; hoverBorderColor: #E53935; "
                            + "pressedBackground: #EF9A9A;"; // Cảm giác nhấn: Nền lún đậm xuống màu đỏ hồng
                    ImageIcon tableIcon = new ImageIcon(getClass().getResource("/Icon/coffee-table.png"));

                    for (TableFood table : tableList) {
                        JButton btn = new JButton(table.getName());
                        btn.setOpaque(true);
                        btn.setIcon(tableIcon);
                        btn.setHorizontalTextPosition(SwingConstants.CENTER);
                        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
                        btn.setIconTextGap(10);
                        btn.setPreferredSize(new Dimension(100, 80));
                        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));

                        btn.setFocusable(false);

                        boolean isAvailable = table.getStatus().equals("Trống");
                        if (isAvailable) {
                            btn.setToolTipText("Trạng thái: Trống");
                            btn.putClientProperty("FlatLaf.style", styleAvailable);
                        } else {
                            btn.setToolTipText("Trạng thái: Đang sử dụng");
                            btn.putClientProperty("FlatLaf.style", styleOccupied);
                        }

                        // CẤU HÌNH ĐIỀU KIỆN CHỌN BÀN
                        btn.addActionListener(e -> {
                            if (isAvailable) {
                                // Nếu bàn trống, cho phép chuyển sang menu
                                java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(TableGui.this);
                                if (window instanceof StaffGUI) {
                                    ((StaffGUI) window).showMenuForTable(table.getId());
                                }
                            } else {

                                int confirm = javax.swing.JOptionPane.showConfirmDialog(TableGui.this,
                                        "Bàn " + table.getName() + " đang có khách. Bạn có muốn thanh toán không?",
                                        "Xác nhận thanh toán",
                                        javax.swing.JOptionPane.YES_NO_OPTION);
                                if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                                    showQrPayment(table.getId());
                                }
                            }
                        });

                        add(btn);
                    }
                    revalidate();
                    repaint();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handlePayment(int tableId) {
        try {
            Request req = new Request("PAY BILL", tableId);
            Response res = (Response) ClientConnection.sendRequest(req);

            if (res != null && "SUCCESS".equals(res.getStatus())) {

                Bill bill = (Bill) res.getData();

                Response detailRes = (Response) ClientConnection.sendRequest(
                        new Request("GET BILL DETAIL", bill.getId()));

                List<BillInfor> details
                        = (List<BillInfor>) detailRes.getData();

                shared.PrintInvoice.saveInvoice2(
                        bill,
                        details,
                        "Bàn " + tableId
                );

                JOptionPane.showMessageDialog(this,
                        "Thanh toán thành công!");

                loadTables();

            } else {
                JOptionPane.showMessageDialog(this,
                        "Thanh toán thất bại: " + res.getMessage());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showQrPayment(int tableId) {

        new Thread(() -> {
            try {

                // Lấy hóa đơn hiện tại của bàn
                Response billRes
                        = (Response) ClientConnection.sendRequest(
                                new Request("GET BILL BY TABLE", tableId));

                if (billRes == null
                        || !"SUCCESS".equals(billRes.getStatus())
                        || billRes.getData() == null) {

                    SwingUtilities.invokeLater(()
                            -> JOptionPane.showMessageDialog(
                                    this,
                                    "Không tìm thấy hóa đơn!",
                                    "Lỗi",
                                    JOptionPane.ERROR_MESSAGE));

                    return;
                }

                Bill bill = (Bill) billRes.getData();

                // Lấy chi tiết món ăn
                Response detailRes
                        = (Response) ClientConnection.sendRequest(
                                new Request(
                                        "GET BILL DETAIL",
                                        bill.getId()));

                if (detailRes == null
                        || !"SUCCESS".equals(detailRes.getStatus())
                        || detailRes.getData() == null) {

                    SwingUtilities.invokeLater(()
                            -> JOptionPane.showMessageDialog(
                                    this,
                                    "Không lấy được chi tiết hóa đơn!",
                                    "Lỗi",
                                    JOptionPane.ERROR_MESSAGE));

                    return;
                }

                List<BillInfor> details
                        = (List<BillInfor>) detailRes.getData();

                // Tạo QR
                String qrUrl
                        = "https://img.vietqr.io/image/TCB-6042088888-compact2.png"
                        + "?amount=" + bill.getTotalPrice().toPlainString()
                        + "&addInfo=BILL_" + bill.getId();

                ImageIcon tempIcon = qrCache.get(bill.getId());

                if (tempIcon == null) {

                    BufferedImage qrImage
                            = ImageIO.read(new URL(qrUrl));

                    // phóng to QR
                    Image scaled
                            = qrImage.getScaledInstance(
                                    400, // chiều rộng
                                    500, // chiều cao
                                    Image.SCALE_SMOOTH);

                    tempIcon = new ImageIcon(scaled);

                    qrCache.put(
                            bill.getId(),
                            tempIcon);
                }

                final ImageIcon icon = tempIcon;

                SwingUtilities.invokeLater(() -> {

                    JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));

                    JTextArea txtBill
                            = new JTextArea();

                    txtBill.setEditable(false);

                    txtBill.setFont(
                            new java.awt.Font(
                                    "Monospaced",
                                    java.awt.Font.PLAIN,
                                    14));

                    txtBill.append(
                            "HÓA ĐƠN #" + bill.getId());

                    txtBill.append(
                            "\n\n");

                    for (BillInfor item : details) {

                        BigDecimal lineTotal
                                = item.getPrice().multiply(
                                        BigDecimal.valueOf(item.getQuantity())
                                );

                        txtBill.append(
                                String.format(
                                        "%-15s %10s x%-3d %10s\n",
                                        item.getFoodName(), // %s
                                        df.format(item.getPrice()), // %s
                                        item.getQuantity(), // %d ✔ (phải là int)
                                        df.format(lineTotal) // %s
                                ));
                    }

                    txtBill.append(
                            "\n--------------------------------\n");

                    txtBill.append(
                            "Tổng tiền: "
                            + df.format(bill.getTotalPrice())
                            + " VND"
                    );
                    JScrollPane billScroll
                            = new JScrollPane(txtBill);

                    JPanel leftPanel
                            = new JPanel(
                                    new BorderLayout());

                    leftPanel.add(
                            billScroll,
                            BorderLayout.CENTER);

                    // ==========================
                    // BÊN PHẢI: QR
                    // ==========================
                    JLabel qrLabel
                            = new JLabel(icon);

                    qrLabel.setHorizontalAlignment(
                            SwingConstants.CENTER);

                    JPanel rightPanel
                            = new JPanel(
                                    new BorderLayout());

                    rightPanel.add(
                            qrLabel,
                            BorderLayout.CENTER);

                    panel.add(leftPanel);
                    panel.add(rightPanel);

                    JOptionPane optionPane
                            = new JOptionPane(
                                    panel,
                                    JOptionPane.PLAIN_MESSAGE,
                                    JOptionPane.YES_NO_OPTION);

                    JDialog dialog
                            = optionPane.createDialog(
                                    this,
                                    "Thanh toán QR");

                    dialog.setSize(900, 700);

                    dialog.setLocationRelativeTo(
                            this);

                    dialog.setVisible(true);

                    Object value
                            = optionPane.getValue();

                    if (value instanceof Integer
                            && ((Integer) value)
                            == JOptionPane.YES_OPTION) {

                        handlePayment(tableId);
                    }
                });

            } catch (Exception ex) {

                ex.printStackTrace();

                SwingUtilities.invokeLater(()
                        -> JOptionPane.showMessageDialog(
                                this,
                                "Không thể tạo QR thanh toán!",
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.GridLayout(6, 0));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
