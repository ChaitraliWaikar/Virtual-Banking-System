import javax.swing.*;
import java.awt.*;
import java.sql.*;

class Home extends JFrame {
    Home(String username) {

        double totalBalance = 0.0;

        Font f = new Font("Futura", Font.BOLD, 40);
        Font f2 = new Font("Calibri", Font.PLAIN, 22);

        JLabel title = new JLabel("Welcome " + username, JLabel.CENTER);
        title.setOpaque(true);
        title.setBackground(new Color(0, 170, 204));
        title.setForeground(Color.WHITE);

        JLabel balanceLabel = new JLabel("Balance: ₹0.00", JLabel.CENTER);
        balanceLabel.setForeground(Color.BLUE);

        JButton b1 = new JButton("Deposit");
        JButton b2 = new JButton("Withdraw");
        JButton b3 = new JButton("Profile Settings");
        JButton b4 = new JButton("Transfer");
        JButton b5 = new JButton("Passbook");
        JButton b6 = new JButton("Logout");
        JButton b7 = new JButton("Account Type");

        title.setFont(f);
        balanceLabel.setFont(f2);
        b1.setFont(f2);
        b2.setFont(f2);
        b3.setFont(f2);
        b4.setFont(f2);
        b5.setFont(f2);
        b6.setFont(f2);
        b7.setFont(f2);

        Color btnColor = new Color(72, 201, 176);

        b1.setBackground(btnColor);
        b2.setBackground(btnColor);
        b3.setBackground(btnColor);
        b4.setBackground(btnColor);
        b5.setBackground(btnColor);
        b6.setBackground(btnColor);
        b7.setBackground(btnColor);

        JButton[] btns = {b1, b2, b3, b4, b5, b6, b7};
        for (JButton b : btns) b.setFocusPainted(false);

        Container c = getContentPane();
        c.setLayout(null);

        title.setBounds(150, 30, 500, 50);
        balanceLabel.setBounds(150, 100, 500, 30);

        b1.setBounds(150, 160, 200, 45);
        b2.setBounds(450, 160, 200, 45);

        b3.setBounds(150, 230, 200, 45);
        b4.setBounds(450, 230, 200, 45);

        b5.setBounds(150, 300, 200, 45);
        b6.setBounds(450, 300, 200, 45);

        b7.setBounds(300, 370, 200, 45);

        c.add(title);
        c.add(balanceLabel);
        c.add(b1);
        c.add(b2);
        c.add(b3);
        c.add(b4);
        c.add(b5);
        c.add(b6);
        c.add(b7);


        // BUTTON ACTIONS
        // DEPOSIT
        b1.addActionListener(a -> {
            new DepositType(username);   // <-- FIX: choose Savings/Current
            dispose();
        });

        // WITHDRAW
        b2.addActionListener(a -> {
            new WithdrawType(username);  // <-- FIX: choose Savings/Current
            dispose();
        });

        // Profile
        b3.addActionListener(a -> {
            new Profile(username);
            dispose();
        });

        // Transfer
        b4.addActionListener(a -> {
            new Transfer(username);
            dispose();
        });

        // PASSBOOK → get first acc_type
        b5.addActionListener(a -> {

            String accType = "";
            String url = "jdbc:mysql://localhost:3306/batch2";

            try (Connection con = DriverManager.getConnection(
                    DBConfig.getURL(),
                    DBConfig.getUser(),
                    DBConfig.getPass()
            )) {

                String sql = "SELECT acc_type FROM accounts WHERE username=? LIMIT 1";

                try (PreparedStatement pst = con.prepareStatement(sql)) {
                    pst.setString(1, username);
                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        accType = rs.getString("acc_type");
                    } else {
                        JOptionPane.showMessageDialog(null, "No account type found!");
                        return;
                    }
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                return;
            }

            new Passbook(username, accType);
            dispose();
        });

        // Logout
        b6.addActionListener(a -> {
            new Landing();
            dispose();
        });

        // Account Type Display
        b7.addActionListener(a -> {
            new AccountType(username);
            dispose();
        });


        // FETCH BALANCE
        String url = "jdbc:mysql://localhost:3306/batch2";

        try (Connection con = DriverManager.getConnection(
                DBConfig.getURL(),
                DBConfig.getUser(),
                DBConfig.getPass()
        )) {

            String sql = "SELECT SUM(balance) AS total FROM accounts WHERE username=?";

            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, username);

                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    totalBalance = rs.getDouble("total");
                }
                balanceLabel.setText("Balance: ₹" + totalBalance);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }


        setVisible(true);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Home");
    }

    public static void main(String[] args) {
        new Home("Chaitrali");
    }
}
