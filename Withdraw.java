import javax.swing.*;
import java.awt.*;
import java.sql.*;

class Withdraw extends JFrame {

    Withdraw(String username, String accType) {   // <-- IMPORTANT: pass acc_type also

        Font f = new Font("Futura", Font.BOLD, 40);
        Font f2 = new Font("Calibri", Font.PLAIN, 22);

        JLabel title = new JLabel("Withdraw (" + accType + ")", JLabel.CENTER);
        title.setFont(f);
        title.setOpaque(true);
        title.setBackground(new Color(0, 102, 204));
        title.setForeground(Color.WHITE);

        JLabel label = new JLabel("Enter Amount:");
        JTextField t1 = new JTextField(10);

        JButton b1 = new JButton("Withdraw");
        JButton b2 = new JButton("Back");

        label.setFont(f2);
        t1.setFont(f2);
        b1.setFont(f2);
        b2.setFont(f2);

        b1.setBackground(new Color(0, 153, 76));
        b1.setForeground(Color.WHITE);

        b2.setBackground(new Color(255, 51, 51));
        b2.setForeground(Color.WHITE);

        Container c = getContentPane();
        c.setLayout(null);

        title.setBounds(150, 40, 500, 50);
        label.setBounds(250, 140, 300, 30);
        t1.setBounds(250, 180, 300, 35);
        b1.setBounds(300, 250, 200, 45);
        b2.setBounds(300, 315, 200, 45);

        c.add(title);
        c.add(label);
        c.add(t1);
        c.add(b1);
        c.add(b2);

        // BACK
        b2.addActionListener(a -> {
            new Home(username);
            dispose();
        });

        // WITHDRAW
        b1.addActionListener(a -> {

            double balance = 0;
            double wlimit = 0;

            String url = "jdbc:mysql://localhost:3306/batch2";

            // PART 1 - Fetch balance from accounts & wlimit from users
            try (Connection con = DriverManager.getConnection(
                    DBConfig.getURL(),
                    DBConfig.getUser(),
                    DBConfig.getPass()
            )) {

                // get balance of selected acc type
                String sql1 = "SELECT balance FROM accounts WHERE username=? AND acc_type=?";
                try (PreparedStatement pst = con.prepareStatement(sql1)) {
                    pst.setString(1, username);
                    pst.setString(2, accType);
                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        balance = rs.getDouble("balance");
                    }
                }

                // fetch wlimit from users table
                String sql2 = "SELECT wlimit FROM users WHERE username=?";
                try (PreparedStatement pst = con.prepareStatement(sql2)) {
                    pst.setString(1, username);
                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        wlimit = rs.getDouble("wlimit");
                    }
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                return;
            }

            // PART 2 - Validate
            String s = t1.getText();
            if (s.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Enter amount");
                return;
            }

            double amount = Double.parseDouble(s);

            if (amount > balance) {
                JOptionPane.showMessageDialog(null, "Insufficient balance");
                return;
            }

            if (amount > wlimit) {
                JOptionPane.showMessageDialog(null, "Withdrawal limit exceeded");
                return;
            }

            double newBal = balance - amount;

            // PART 3 - Update accounts table
            try (Connection con = DriverManager.getConnection(
                    DBConfig.getURL(),
                    DBConfig.getUser(),
                    DBConfig.getPass()
            )) {

                String sql = "UPDATE accounts SET balance=? WHERE username=? AND acc_type=?";
                try (PreparedStatement pst = con.prepareStatement(sql)) {
                    pst.setDouble(1, newBal);
                    pst.setString(2, username);
                    pst.setString(3, accType);
                    pst.executeUpdate();
                }

                JOptionPane.showMessageDialog(null, "Withdrawal Successful");
                t1.setText("");

                // Insert into transactions
                updatePassbook(username, accType, "Withdraw", -amount, newBal);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }

        });

        setVisible(true);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Withdraw Money");
    }

    void updatePassbook(String username, String accType, String desc, double amount, double balance) {

        String url = "jdbc:mysql://localhost:3306/batch2";

        try (Connection con = DriverManager.getConnection(
                DBConfig.getURL(),
                DBConfig.getUser(),
                DBConfig.getPass()
        )) {

            String sql = "INSERT INTO transactions(username, description, amount, balance, acc_type) VALUES(?,?,?,?,?)";

            try (PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setString(1, username);
                pst.setString(2, desc);
                pst.setDouble(3, amount);
                pst.setDouble(4, balance);
                pst.setString(5, accType);

                pst.executeUpdate();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Withdraw("Chaitrali", "Savings");
    }
}
