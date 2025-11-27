import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Passbook extends JFrame {

    Passbook(String username, String accType) {

        Font titleFont = new Font("Futura", Font.BOLD, 40);
        Font tableFont = new Font("Calibri", Font.PLAIN, 18);
        Font buttonFont = new Font("Calibri", Font.BOLD, 20);

        JLabel title = new JLabel("Passbook - " + accType + " Account", JLabel.CENTER);
        title.setFont(titleFont);
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(0, 102, 204));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

        JButton backButton = new JButton("Back");
        backButton.setFont(buttonFont);
        styleButton(backButton, new Color(255, 51, 51));
        backButton.addActionListener(e -> {
            new Home(username);
            dispose();
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(0, 102, 204));
        topPanel.add(title, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(235, 235, 235));
        bottomPanel.add(backButton);

        Container c = getContentPane();
        c.setLayout(new BorderLayout(20, 20));
        c.add(topPanel, BorderLayout.NORTH);
        c.add(scrollPane, BorderLayout.CENTER);
        c.add(bottomPanel, BorderLayout.SOUTH);

        // Load passbook data
        try (Connection con = DriverManager.getConnection(
                DBConfig.getURL(),
                DBConfig.getUser(),
                DBConfig.getPass()
        )) {

            // Fetch all transactions for the account
            String sql = "SELECT date, description, amount " +
                    "FROM transactions WHERE username=? AND acc_type=? ORDER BY date ASC";

            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, username);
                pst.setString(2, accType);

                ResultSet rs = pst.executeQuery();
                double runningBalance = 0.0;

                // Fetch initial balance from accounts table
                String balSql = "SELECT balance FROM accounts WHERE username=? AND acc_type=?";
                try (PreparedStatement pstBal = con.prepareStatement(balSql)) {
                    pstBal.setString(1, username);
                    pstBal.setString(2, accType);
                    ResultSet rsBal = pstBal.executeQuery();
                    if (rsBal.next()) {
                        runningBalance = rsBal.getDouble("balance");
                    }
                }

                // Since we want the Passbook in descending order
                java.util.List<Object[]> rows = new java.util.ArrayList<>();
                while (rs.next()) {
                    String date = rs.getString("date");
                    String desc = rs.getString("description");
                    double amt = rs.getDouble("amount");
                    runningBalance += amt; // update running balance
                    rows.add(new Object[]{date, desc, amt, runningBalance});
                }

                // Add in reverse order so newest on top
                for (int i = rows.size() - 1; i >= 0; i--) {
                    tableModel.addRow(rows.get(i));
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

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
