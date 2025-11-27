import javax.swing.*;
import java.awt.*;
import java.sql.*;

class Deposit extends JFrame {

    Deposit(String username) {

        Font f = new Font("Futura", Font.BOLD, 40);
        Font f2 = new Font("Calibri", Font.PLAIN, 22);

        JLabel title = new JLabel("Deposit Money", JLabel.CENTER);
        JLabel labelType = new JLabel("Select Account Type:");
        JLabel labelAmount = new JLabel("Enter Amount:");

        String[] types = {"Savings", "Current"};
        JComboBox<String> accTypeBox = new JComboBox<>(types);

        JTextField t1 = new JTextField(10);
        JButton b1 = new JButton("Deposit");
        JButton b2 = new JButton("Back");

        title.setFont(f);
        labelType.setFont(f2);
        labelAmount.setFont(f2);
        accTypeBox.setFont(f2);
        t1.setFont(f2);
        b1.setFont(f2);
        b2.setFont(f2);

        Container c = getContentPane();
        c.setLayout(null);
        c.setBackground(new Color(230, 230, 230));

        title.setOpaque(true);
        title.setBackground(new Color(0, 102, 204));
        title.setForeground(Color.WHITE);
        title.setBounds(200, 30, 400, 60);

        labelType.setBounds(250, 120, 300, 30);
        accTypeBox.setBounds(250, 155, 300, 32);

        labelAmount.setBounds(250, 210, 300, 30);
        t1.setBounds(250, 245, 300, 32);
        t1.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        styleButton(b1, new Color(0, 153, 76));
        styleButton(b2, new Color(255, 51, 51));

        b1.setBounds(300, 310, 200, 45);
        b2.setBounds(300, 370, 200, 45);

        c.add(title);
        c.add(labelType);
        c.add(accTypeBox);
        c.add(labelAmount);
        c.add(t1);
        c.add(b1);
        c.add(b2);

        b2.addActionListener(a -> {
            new Home(username);
            dispose();
        });

        b1.addActionListener(a -> {

            String accType = accTypeBox.getSelectedItem().toString();
            String amtText = t1.getText();

            if (amtText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter amount");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amtText);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Invalid Amount");
                return;
            }

            double balance = 0.0;

            String url = "jdbc:mysql://localhost:3306/batch2";

            try (Connection con = DriverManager.getConnection(
                    DBConfig.getURL(),
                    DBConfig.getUser(),
                    DBConfig.getPass()
            )) {

                String sql = "SELECT balance FROM accounts WHERE username=? AND acc_type=?";
                PreparedStatement pst = con.prepareStatement(sql);

                pst.setString(1, username);
                pst.setString(2, accType);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    balance = rs.getDouble("balance");
                } else {
                    JOptionPane.showMessageDialog(null, "Account type not found!");
                    return;
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                return;
            }

            double newBalance = balance + amount;

            try (Connection con = DriverManager.getConnection(
                    DBConfig.getURL(),
                    DBConfig.getUser(),
                    DBConfig.getPass()
            )) {

                String sql2 = "UPDATE accounts SET balance=? WHERE username=? AND acc_type=?";
                PreparedStatement pst2 = con.prepareStatement(sql2);

                pst2.setDouble(1, newBalance);
                pst2.setString(2, username);
                pst2.setString(3, accType);
                pst2.executeUpdate();

                JOptionPane.showMessageDialog(null, "Amount Deposited Successfully!");

                updatePassbook(username, accType, "Deposit", amount);

                t1.setText("");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }

        });

        setVisible(true);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Deposit Money");
    }

    void updatePassbook(String username, String accType, String desc, double amount) {

        String url = "jdbc:mysql://localhost:3306/batch2";

        try (Connection con = DriverManager.getConnection(
                DBConfig.getURL(),
                DBConfig.getUser(),
                DBConfig.getPass()
        )) {

            String sql = "INSERT INTO transactions(username, acc_type, description, amount, txn_date, date) VALUES(?,?,?,?,NOW(),CURDATE())";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, username);
            pst.setString(2, accType);
            pst.setString(3, desc);
            pst.setDouble(4, amount);

            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void styleButton(JButton b, Color bg) {
        b.setForeground(Color.WHITE);
        b.setBackground(bg);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        b.setFont(new Font("Calibri", Font.BOLD, 20));

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
        new Deposit("Chaitrali");
    }
}
