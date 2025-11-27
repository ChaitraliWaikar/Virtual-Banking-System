import javax.swing.*;
import java.awt.*;
import java.security.MessageDigest;
import java.sql.*;

class Nlogin extends JFrame {

    Nlogin() {

        Font f = new Font("Futura", Font.BOLD, 35);
        Font f2 = new Font("Calibri", Font.PLAIN, 20);

        JLabel title = new JLabel("Signup", JLabel.CENTER);
        title.setFont(f);
        title.setOpaque(true);
        title.setBackground(new Color(0, 102, 204));
        title.setForeground(Color.WHITE);
        title.setBounds(250, 20, 300, 50);

        JLabel l1 = new JLabel("Set Username");
        JTextField t1 = new JTextField(10);

        JLabel l2 = new JLabel("Set Password");
        JPasswordField t2 = new JPasswordField(10);

        JLabel l3 = new JLabel("Confirm Password");
        JPasswordField t3 = new JPasswordField(10);

        JLabel l4 = new JLabel("Phone");
        JTextField t4 = new JTextField(15);

        JLabel l5 = new JLabel("Email");
        JTextField t5 = new JTextField(20);

        JLabel l6 = new JLabel("Gender");
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"male", "female", "other"});

        JButton b1 = new JButton("Submit");
        JButton b2 = new JButton("Back");
        styleButton(b1, new Color(0, 153, 76));
        styleButton(b2, new Color(255, 51, 51));

        l1.setFont(f2); t1.setFont(f2);
        l2.setFont(f2); t2.setFont(f2);
        l3.setFont(f2); t3.setFont(f2);
        l4.setFont(f2); t4.setFont(f2);
        l5.setFont(f2); t5.setFont(f2);
        l6.setFont(f2); genderBox.setFont(f2);

        Container c = getContentPane();
        c.setLayout(null);
        c.setBackground(new Color(235, 235, 235));

        int labelX = 200, fieldX = 400, yStart = 100, gap = 50;

        title.setBounds(250, 20, 300, 50);

        l1.setBounds(labelX, yStart, 180, 30);
        t1.setBounds(fieldX, yStart, 220, 30);

        l2.setBounds(labelX, yStart + gap, 180, 30);
        t2.setBounds(fieldX, yStart + gap, 220, 30);

        l3.setBounds(labelX, yStart + 2 * gap, 180, 30);
        t3.setBounds(fieldX, yStart + 2 * gap, 220, 30);

        l4.setBounds(labelX, yStart + 3 * gap, 180, 30);
        t4.setBounds(fieldX, yStart + 3 * gap, 220, 30);

        l5.setBounds(labelX, yStart + 4 * gap, 180, 30);
        t5.setBounds(fieldX, yStart + 4 * gap, 220, 30);

        l6.setBounds(labelX, yStart + 5 * gap, 180, 30);
        genderBox.setBounds(fieldX, yStart + 5 * gap, 220, 30);

        b1.setBounds(250, yStart + 6 * gap + 20, 150, 45);
        b2.setBounds(430, yStart + 6 * gap + 20, 150, 45);

        c.add(title);
        c.add(l1); c.add(t1);
        c.add(l2); c.add(t2);
        c.add(l3); c.add(t3);
        c.add(l4); c.add(t4);
        c.add(l5); c.add(t5);
        c.add(l6); c.add(genderBox);
        c.add(b1); c.add(b2);

        b2.addActionListener(a -> {
            new Landing();
            dispose();
        });

        b1.addActionListener(a -> {

            String user = t1.getText();
            String pass = new String(t2.getPassword());
            String confirm = new String(t3.getPassword());

            if (!pass.equals(confirm)) {
                JOptionPane.showMessageDialog(null, "Passwords do not match");
                return;
            }

            if (!t4.getText().matches("\\d{10}")) {
                JOptionPane.showMessageDialog(null, "Enter a valid 10-digit phone number");
                return;
            }

            if (!t5.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(null, "Invalid Email");
                return;
            }

            String hashedPassword = hashPassword(pass);

            String url = "jdbc:mysql://localhost:3306/batch2";

            try (Connection con = DriverManager.getConnection(
                    DBConfig.getURL(),
                    DBConfig.getUser(),
                    DBConfig.getPass()
            )) {

                // 1) Insert user
                String sql = "INSERT INTO users(username,password,phone,email,gender) VALUES(?, ?, ?, ?, ?)";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, user);
                pst.setString(2, hashedPassword);
                pst.setString(3, t4.getText());
                pst.setString(4, t5.getText());
                pst.setString(5, genderBox.getSelectedItem().toString());
                pst.executeUpdate();

                // 2) Create both account types for this user
                String accSql = "INSERT INTO accounts(username, acc_type, balance) VALUES(?, ?, 0)";
                PreparedStatement acc = con.prepareStatement(accSql);

                acc.setString(1, user);
                acc.setString(2, "Savings");
                acc.executeUpdate();

                acc.setString(1, user);
                acc.setString(2, "Current");
                acc.executeUpdate();

                JOptionPane.showMessageDialog(null, "Signup Successful!");

                new Home(user);
                dispose();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });

        setVisible(true);
        setSize(800, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Signup");
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

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hash)
                sb.append(String.format("%02x", b));

            return sb.toString();
        } catch (Exception e) {
            return password;
        }
    }

    public static void main(String[] args) {
        new Nlogin();
    }
}
