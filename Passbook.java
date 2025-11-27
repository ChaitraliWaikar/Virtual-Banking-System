//TIP: Passbook with account type filtering

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Passbook extends JFrame {

    Passbook(String username, String accType) {

        // Fonts
        Font titleFont = new Font("Futura", Font.BOLD, 40);
        Font tableFont = new Font("Calibri", Font.PLAIN, 18);
        Font buttonFont = new Font("Calibri", Font.BOLD, 20);

        // Title with account type
        JLabel title = new JLabel("Passbook - " + accType + " Account", JLabel.CENTER);
        title.setFont(titleFont);
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(0, 102, 204));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table setup
        String[] columnNames = {"Date & Time", "Description", "Amount", "Balance"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        JTable table = new JTable(tableModel);
        table.setFont(tableFont);
        table.setRowHeight(30);
        table.setGridColor(new Color(200, 200, 200));

        table.getTableHeader().setFont(new Font("Calibri", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(0, 102, 204));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setFont(buttonFont);
        styleButton(backButton, new Color(255, 51, 51));

        backButton.addActionListener(e -> {
            new Home(username);
            dispose();
        });

        // Panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(0, 102, 204));
        topPanel.add(title, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(235, 235, 235));
        bottomPanel.add(backButton);

        // Layout
        Container c = getContentPane();
        c.setLayout(new BorderLayout(20, 20));
        c.add(topPanel, BorderLayout.NORTH);
        c.add(scrollPane, BorderLayout.CENTER);
        c.add(bottomPanel, BorderLayout.SOUTH);

        // Load passbook data
        String url = "jdbc:mysql://localhost:3306/batch2";

        try (Connection con = DriverManager.getConnection(
                DBConfig.getURL(),
                DBConfig.getUser(),
                DBConfig.getPass()
        )) {

            String sql = "SELECT * FROM transactions WHERE username=? AND acc_type=? ORDER BY date DESC";

            try (PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setString(1, username);
                pst.setString(2, accType);

                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    String s1 = rs.getString("date");
                    String s2 = rs.getString("description");
                    double d1 = rs.getDouble("amount");
                    double d2 = rs.getDouble("balance");

                    tableModel.addRow(new Object[]{s1, s2, d1, d2});
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        // Frame settings
        setTitle("Passbook - " + accType);
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void styleButton(JButton b, Color bg) {
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

    public static void main(String[] args) {
        new Passbook("Chaitrali", "Savings");
    }
}
