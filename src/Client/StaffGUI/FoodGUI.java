package Client.StaffGUI;

import Client.ClientConnection;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.List;
import javax.swing.*;
import shared.Model.*;
import shared.RequestResponse.*;

/**
 *
 * @author admin
 */
public class FoodGUI extends javax.swing.JPanel { //chịu trách nhiệm hiển thị danh sách món ăn

    /**
     * Creates new form FoodGUI
     */
    private FoodAdditionListener listener;
    private JPanel categoryPanel;
    private JPanel foodPanel;

    public FoodGUI() {
        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        initComponents();
        setLayout(new BorderLayout());
        categoryPanel = new JPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
        JScrollPane categoryScrollPane = new JScrollPane(categoryPanel);
        categoryScrollPane.setPreferredSize(new Dimension(140, 0));

        foodPanel = new JPanel();
        foodPanel.setLayout(new GridLayout(0, 3, 15, 15));
        JScrollPane foodScrollPane = new JScrollPane(foodPanel);
        add(categoryScrollPane, BorderLayout.WEST);
        add(foodScrollPane, BorderLayout.CENTER);

        //gui
        setBackground(new Color(245, 235, 230));
        setOpaque(true);

        categoryPanel.setBackground(new Color(245, 235, 230));

        foodPanel.setBackground(new Color(245, 235, 230));

        loadCategories();
    }

    public void setOrderGui(FoodAdditionListener listener) {
        this.listener = listener;
    }

    public void loadCategories() {

        new SwingWorker<List<CategoryFood>, Void>() {
            //swingworker: chạy tác vụ nặng background thread
            @Override
            protected List<CategoryFood> doInBackground() throws Exception {//làm trong background
                // Sử dụng lớp ClientConnection đã đồng bộ hóa
                Response res = (Response) ClientConnection.sendRequest(new Request("SELECT CATEGORY", null));
                return (List<CategoryFood>) res.getData();
            }

            @Override
            protected void done() {//quay lại UI
                try {
                    List<CategoryFood> list = get();
                    categoryPanel.removeAll();
                    for (CategoryFood c : list) {
                        JButton btn = new JButton(c.getTen());
                        btn.addActionListener(e -> loadFoodsByCategory(c.getId()));
                        btn.setMaximumSize(new Dimension(120, 40));
                        btn.setAlignmentX(CENTER_ALIGNMENT);

                        btn.putClientProperty("FlatLaf.style","arc:20");
                        categoryPanel.add(btn);
                    }
                    categoryPanel.revalidate();
                    categoryPanel.repaint();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    public void loadFoodsByCategory(int categoryId) {
        new SwingWorker<List<Food>, Void>() {
            @Override
            protected List<Food> doInBackground() throws Exception {
                // Sử dụng lớp ClientConnection đã đồng bộ hóa
                Response res = (Response) ClientConnection.sendRequest(new Request("SELECT FOOD BY CATEGORY", categoryId));
                return (List<Food>) res.getData();
            }

            @Override
            protected void done() {
                try {
                    List<Food> foods = get();
                    foodPanel.removeAll();
                    for (Food f : foods) {
                        JButton btn = createFoodButton(f);
                        foodPanel.add(btn);
                    }
                    foodPanel.revalidate();
                    foodPanel.repaint();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private JButton createFoodButton(Food f) {
        JButton btn = new JButton();

        btn.setPreferredSize(new Dimension(180, 220));

        btn.setFocusPainted(false);
        btn.putClientProperty("FlatLaf.style", "arc:15");

        btn.setBackground(Color.WHITE);

        btn.setForeground(
                new Color(108, 67, 44));

        // 1. Cấu hình hiển thị Text
        btn.setText("<html><center>" + f.getNameFood() + "<br/>" + f.getPriceOut() + "</center></html>");
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);

        // 2. Cấu hình hiển thị Ảnh (Đọc từ đường dẫn hệ thống)
        String imagePath = f.getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            // Thay đổi đường dẫn này thành thư mục chứa ảnh thực tế của bạn
            String fullPath = System.getProperty("user.dir") + "/" + imagePath;
            java.io.File imgFile = new java.io.File(fullPath);
            if (imgFile.exists()) {
                ImageIcon icon = new ImageIcon(fullPath);
                Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                btn.setIcon(new ImageIcon(img));
            } else {
                btn.setText("<html><center>No Image<br/>" + f.getNameFood() + "</center></html>");
            }
        }
        btn.addActionListener(e -> {
            if (listener != null) {
                listener.addFoodToTempList(f);
            }
        });
        return btn;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setOpaque(false);
        setLayout(new java.awt.GridLayout(1, 0));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
