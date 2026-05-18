package Client;

import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import shared.Request;
import shared.Response;
import shared.TableFood;

public class Table extends javax.swing.JFrame {

    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 1234;

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket socket;

    private DefaultTableModel model;

    public Table() {
        initComponents();
        connectServer();
        setupTable();
        loadTables();
    }

    private void setupTable() {
        model = new DefaultTableModel(
            new Object[]{"ID", "Tên bàn", "Trạng thái"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable1.setModel(model);
    }

    private void connectServer() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không kết nối được server!");
            e.printStackTrace();
        }
    }

    private void loadTables() {
        try {
            Request req = new Request("GET_TABLES", null);
            out.writeObject(req);
            out.flush();

            Response res = (Response) in.readObject();

            if ("SUCCESS".equalsIgnoreCase(res.getStatus())) {
                model.setRowCount(0);
                List<TableFood> list = (List<TableFood>) res.getData();

                for (TableFood t : list) {
                    model.addRow(new Object[]{
                        t.getId(),
                        t.getTen(),
                        t.getStatus()
                    });
                }
            } else {
                JOptionPane.showMessageDialog(this, res.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách bàn");
        }
    }

    private void bookSelectedTable() {
        int row = jTable1.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn 1 bàn trước đã!");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        String status = model.getValueAt(row, 2).toString();

        if (!status.equalsIgnoreCase("Trống")) {
            JOptionPane.showMessageDialog(this, "Bàn này không còn trống!");
            return;
        }

        try {
            Request req = new Request("BOOK_TABLE", id);
            out.writeObject(req);
            out.flush();

            Response res = (Response) in.readObject();
            JOptionPane.showMessageDialog(this, res.getMessage());
            loadTables();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi đặt bàn");
        }
    }

    private void freeSelectedTable() {
        int row = jTable1.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn 1 bàn trước đã!");
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        try {
            Request req = new Request("FREE_TABLE", id);
            out.writeObject(req);
            out.flush();

            Response res = (Response) in.readObject();
            JOptionPane.showMessageDialog(this, res.getMessage());
            loadTables();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi trả bàn");
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnLoad = new javax.swing.JButton();
        btnBook = new javax.swing.JButton();
        btnFree = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Quản lý bàn");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Tên bàn", "Trạng thái"}
        ));
        jScrollPane1.setViewportView(jTable1);

        btnLoad.setText("Tải bàn");
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadTables();
            }
        });

        btnBook.setText("Đặt bàn");
        btnBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bookSelectedTable();
            }
        });

        btnFree.setText("Trả bàn");
        btnFree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                freeSelectedTable();
            }
        });

        btnBack.setText("Quay lại");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnLoad)
                        .addGap(18, 18, 18)
                        .addComponent(btnBook)
                        .addGap(18, 18, 18)
                        .addComponent(btnFree)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBack)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLoad)
                    .addComponent(btnBook)
                    .addComponent(btnFree)
                    .addComponent(btnBack))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Table().setVisible(true);
            }
        });
    }

    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnBook;
    private javax.swing.JButton btnFree;
    private javax.swing.JButton btnLoad;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
}