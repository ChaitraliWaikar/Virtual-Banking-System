import javax.swing.*;
import java.awt.*;
import java.sql.*;

class Transfer extends JFrame {

    Transfer(String username) {

        Font f = new Font("Futura", Font.BOLD, 35);
        Font f2 = new Font("Calibri", Font.PLAIN, 20);

        JLabel title = new JLabel("Transfer Funds", JLabel.CENTER);
        title.setFont(f);
        title.setOpaque(true);
        title.setForeground(Color.WHITE);
        title.setBackground(new Color(0, 102, 204));

        JLabel l1 = new JLabel("Receiver Username:");
        JTextField t1 = new JTextField(10);

        JLabel l2 = new JLabel("Amount:");
        JTextField t2 = new JTextField(10);

        JLabel l3 = new JLabel("Your Account Type:");
        JComboBox<String> box1 = new JComboBox<>(new String[]{"Savings", "Current"});

        JLabel l4 = new JLabel("Receiver Account Type:");
        JComboBox<String> box2 = new JComboBox<>(new String[]{"Savings", "Current"});

        JButton b1 = new JButton("Transfer");
        JButton b2 = new JButton("Back");

        l1.setFont(f2); t1.setFont(f2);
        l2.setFont(f2); t2.setFont(f2);
        l3.setFont(f2); box1.setFont(f2);
        l4.setFont(f2); box2.setFont(f2);
        b1.setFont(f2); b2.setFont(f2);

        b1.setBackground(new Color(0,102,204));
        b1.setForeground(Color.WHITE);
        b2.setBackground(new Color(255,51,51));
        b2.setForeground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(title, BorderLayout.CENTER);

        JPanel center = new JPanel();
        center.setLayout(null);
        center.setBackground(new Color(240, 240, 240));

        int lx = 190, tx = 420, y = 70, h = 35, gap = 55;

        l1.setBounds(lx, y, 200, h);
        t1.setBounds(tx, y, 200, h);

        l2.setBounds(lx, y + gap, 200, h);
        t2.setBounds(tx, y + gap, 200, h);

        l3.setBounds(lx, y + 2*gap, 200, h);
        box1.setBounds(tx, y + 2*gap, 200, h);

        l4.setBounds(lx, y + 3*gap, 200, h);
        box2.setBounds(tx, y + 3*gap, 200, h);

        b1.setBounds(250, y + 4*gap + 20, 150, 45);
        b2.setBounds(440, y + 4*gap + 20, 150, 45);

        center.add(l1); center.add(t1);
        center.add(l2); center.add(t2);
        center.add(l3); center.add(box1);
        center.add(l4); center.add(box2);
        center.add(b1); center.add(b2);

        Container c = getContentPane();
        c.add(topPanel, BorderLayout.NORTH);
        c.add(center, BorderLayout.CENTER);

        // Back
        b2.addActionListener(a -> {
            new Home(username);
            dispose();
        });


        // Transfer Button
        b1.addActionListener(a -> {

            String receiver = t1.getText();
            String amountStr = t2.getText();
            String senderAcc = box1.getSelectedItem().toString();
            String receiverAcc = box2.getSelectedItem().toString();

            if(receiver.isEmpty() || amountStr.isEmpty()){
                JOptionPane.showMessageDialog(null, "Fill all fields!");
                return;
            }

            double amount = Double.parseDouble(amountStr);
            if(amount <= 0){
                JOptionPane.showMessageDialog(null, "Invalid amount!");
                return;
            }

            double senderBalance = fetchBalance(username, senderAcc);
            if(senderBalance < amount){
                JOptionPane.showMessageDialog(null, "Insufficient Balance!");
                return;
            }

            double receiverBalance = fetchBalance(receiver, receiverAcc);
            if(receiverBalance == -1){
                JOptionPane.showMessageDialog(null, "Receiver does not exist!");
                return;
            }

            // Update balances
            updateBalance(username, senderAcc, senderBalance - amount);
            updateBalance(receiver, receiverAcc, receiverBalance + amount);

            // Update Passbook
            updatePassbook(username, senderAcc,
                    "Transfer to " + receiver,
                    -amount,
                    senderBalance - amount
            );

            updatePassbook(receiver, receiverAcc,
                    "Transfer from " + username,
                    amount,
                    receiverBalance + amount
            );

            JOptionPane.showMessageDialog(null, "Transfer Successful!");

            t1.setText("");
            t2.setText("");
        });

        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    // Fetch balance from accounts table
    double fetchBalance(String username, String accType){
        String url = "jdbc:mysql://localhost:3306/batch2";
        try(Connection con = DriverManager.getConnection(
                DBConfig.getURL(),
                DBConfig.getUser(),
                DBConfig.getPass()
        )){
            String sql = "SELECT balance FROM accounts WHERE username=? AND acc_type=?";
            try(PreparedStatement pst = con.prepareStatement(sql)){
                pst.setString(1, username);
                pst.setString(2, accType);
                ResultSet rs = pst.executeQuery();
                if(rs.next()) return rs.getDouble("balance");
                return -1; // user or account not found
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
            return -1;
        }
    }


    // Update balance in accounts table
    void updateBalance(String username, String accType, double total){
        String url = "jdbc:mysql://localhost:3306/batch2";
        try(Connection con = DriverManager.getConnection(
                DBConfig.getURL(),
                DBConfig.getUser(),
                DBConfig.getPass()
        )){
            String sql = "UPDATE accounts SET balance=? WHERE username=? AND acc_type=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setDouble(1, total);
            pst.setString(2, username);
            pst.setString(3, accType);
            pst.executeUpdate();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }


    // Insert record in transactions table
    void updatePassbook(String username, String accType, String desc, double amt, double bal){
        String url = "jdbc:mysql://localhost:3306/batch2";
        try(Connection con = DriverManager.getConnection(
                DBConfig.getURL(),
                DBConfig.getUser(),
                DBConfig.getPass()
        )){
            String sql = "INSERT INTO transactions(username, acc_type, description, amount, balance) VALUES(?,?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, accType);
            pst.setString(3, desc);
            pst.setDouble(4, amt);
            pst.setDouble(5, bal);
            pst.executeUpdate();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }


    public static void main(String[] args) {
        new Transfer("Chaitrali");
    }
}
