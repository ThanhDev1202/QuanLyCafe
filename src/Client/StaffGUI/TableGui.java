package Client.StaffGUI;

import java.awt.Color;
import Client.ClientConnection;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.ui.FlatBorder;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import shared.Model.TableFood;
import shared.RequestResponse.Request;
import shared.RequestResponse.Response;

/**
 *
 * @author admin
 */
public class TableGui extends javax.swing.JPanel { //chịu trách nhiệm hiển thị danh sách bàn ăn

    /**
     * Creates new form TableGui
     */
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
                    setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
                    setBackground(new Color(245, 235, 230));

                    for (TableFood table : tableList) {
                        JButton btn = new JButton(table.getName());
                        btn.setOpaque(true);
                        ImageIcon tableIcon = new ImageIcon(getClass().getResource("/Icon/bill.png"));
                        btn.setIcon(tableIcon);
                        btn.setHorizontalTextPosition(SwingConstants.CENTER);
                        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
                        btn.setIconTextGap(10);
                        btn.setPreferredSize(new Dimension(240, 180));
                        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
                        btn.putClientProperty(
                                "JButton.buttonType",
                                "roundRect"
                        );

                        btn.putClientProperty(
                                "JComponent.outline",
                                "success"
                        );

                        // Kiểm tra trạng thái để xác định màu và khả năng nhấn
                        boolean isAvailable = table.getStatus().equals("Trống");
                        if (isAvailable) {

                            btn.setBackground(new Color(232, 245, 233));
                            btn.setForeground(new Color(46, 125, 50));
                            btn.setToolTipText("Trạng thái: Trống");

                        } else {

                            btn.setBackground(new Color(255, 235, 238));
                            btn.setForeground(new Color(198, 40, 40));
                            btn.setToolTipText("Trạng thái: Đang sử dụng");
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
                                    handlePayment(table.getId());
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
            // Gửi yêu cầu "PAY BILL" kèm tableId
            Request req = new Request("PAY BILL", tableId);
            Response res = (Response) ClientConnection.sendRequest(req);

            if (res != null && "SUCCESS".equals(res.getStatus())) {
                javax.swing.JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
                loadTables(); // Tải lại danh sách bàn để cập nhật màu từ Đỏ sang Xanh
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Thanh toán thất bại: " + res.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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

        setLayout(new java.awt.GridLayout(6, 0));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
