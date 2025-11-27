import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

class Adashboard extends JFrame {

    Adashboard() {

        // FONTS
        Font titleFont = new Font("Futura", Font.BOLD, 40);
        Font tableFont = new Font("Calibri", Font.PLAIN, 18);
        Font buttonFont = new Font("Calibri", Font.BOLD, 20);

        // TITLE BAR
        JLabel title = new JLabel("Admin Dashboard", JLabel.CENTER);
        title.setFont(titleFont);
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(0, 102, 204));
        title.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // TABLE MODEL (NOTE: total_balance instead of balance)
        String[] columnNames = {"Username", "Total Balance", "Phone", "Email", "Gender", "WLimit"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setFont(tableFont);
        table.setRowHeight(32);
        table.setGridColor(new Color(200, 200, 200));

        table.getTableHeader().setFont(new Font("Calibri", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(0, 102, 204));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

        // INPUT FIELDS
        JTextField t1 = new JTextField(10);
        JTextField t2 = new JTextField(10);

        styleTextField(t1);
        styleTextField(t2);

        // FILTER BUTTON
        JButton b1 = new JButton("Filter");
        styleMainButton(b1, new Color(0, 153, 76));

        // BACK BUTTON
        JButton b2 = new JButton("Back");
        styleMainButton(b2, new Color(255, 51, 51));

        b2.addActionListener(e -> {
            new Alogin();
            dispose();
        });

        // FILTER PANEL
        JPanel filterPanel = new JPanel();
        filterPanel.setBackground(new Color(240, 240, 240));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        filterPanel.add(new JLabel("Min Balance:"));
        filterPanel.add(t1);
        filterPanel.add(new JLabel("Max Balance:"));
        filterPanel.add(t2);
        filterPanel.add(b1);

        // LAYOUT PANELS
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(title, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(240, 240, 240));
        bottomPanel.add(b2);

        JPanel centerPanel = new JPanel(new BorderLayout(20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerPanel.add(filterPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // ADDING TO FRAME
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        c.add(topPanel, BorderLayout.NORTH);
        c.add(centerPanel, BorderLayout.CENTER);
        c.add(bottomPanel, BorderLayout.SOUTH);

        // FILTER ACTION — FIXED FOR NEW SCHEMA
        b1.addActionListener(a -> {
            tableModel.setRowCount(0);

            String v1 = t1.getText();
            String v2 = t2.getText();

            double min = v1.isEmpty() ? 0.0 : Double.parseDouble(v1);
            double max = v2.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(v2);

            String url = "jdbc:mysql://localhost:3306/batch2";

            try (Connection con = DriverManager.getConnection(
                    DBConfig.getURL(),
                    DBConfig.getUser(),
                    DBConfig.getPass()
            )) {

                String sql =
                        "SELECT u.username, " +
                                "       COALESCE(SUM(a.balance), 0) AS total_balance, " +
                                "       u.phone, u.email, u.gender, u.wlimit " +
                                "FROM users u " +
                                "LEFT JOIN accounts a ON u.username = a.username " +
                                "GROUP BY u.username " +
                                "HAVING total_balance BETWEEN ? AND ?";

                PreparedStatement pst = con.prepareStatement(sql);

                pst.setDouble(1, min);
                pst.setDouble(2, max);

                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            rs.getString("username"),
                            rs.getDouble("total_balance"),
                            rs.getLong("phone"),
                            rs.getString("email"),
                            rs.getString("gender"),
                            rs.getDouble("wlimit")
                    });
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });

        // LOAD ALL USERS INITIALLY
        loadAllUsers(tableModel);

        // FRAME SETTINGS
        setTitle("Admin Dashboard");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(230, 230, 230));
        setVisible(true);
    }

    // LOAD USER LIST (with total balance)
    private void loadAllUsers(DefaultTableModel tableModel) {
        String url = "jdbc:mysql://localhost:3306/batch2";

        try (Connection con = DriverManager.getConnection(
                DBConfig.getURL(),
                DBConfig.getUser(),
                DBConfig.getPass()
        )) {

            String sql =
                    "SELECT u.username, " +
                            "       COALESCE(SUM(a.balance), 0) AS total_balance, " +
                            "       u.phone, u.email, u.gender, u.wlimit " +
                            "FROM users u " +
                            "LEFT JOIN accounts a ON u.username = a.username " +
                            "GROUP BY u.username";

            PreparedStatement pst = con.prepareStatement(sql);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("username"),
                        rs.getDouble("total_balance"),
                        rs.getLong("phone"),
                        rs.getString("email"),
                        rs.getString("gender"),
                        rs.getDouble("wlimit")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    // BUTTON STYLE
    private void styleMainButton(JButton b, Color bg) {
        b.setFont(new Font("Calibri", Font.BOLD, 20));
        b.setForeground(Color.WHITE);
        b.setBackground(bg);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                b.setBackground(bg.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                b.setBackground(bg);
            }
        });
    }

    // TEXT FIELD STYLE
    private void styleTextField(JTextField t) {
        t.setFont(new Font("Calibri", Font.PLAIN, 18));
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(160, 160, 160)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }

    public static void main(String[] args) {
        new Adashboard();
    }
}
