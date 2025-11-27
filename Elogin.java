import javax.swing.*;
import java.awt.*;
import java.sql.*;

class Elogin extends JFrame {
    Elogin() {

        Font f = new Font("Futura", Font.BOLD, 40);
        Font f2 = new Font("Calibri", Font.PLAIN, 22);

        JLabel title = new JLabel("Login", JLabel.CENTER);
        title.setFont(f);
        title.setOpaque(true);
        title.setBackground(new Color(0, 102, 204));
        title.setForeground(Color.WHITE);
        title.setBounds(250, 30, 300, 50);

        JLabel l1 = new JLabel("Enter Username");
        JTextField t1 = new JTextField(10);
        l1.setFont(f2);
        t1.setFont(f2);
        l1.setBounds(250, 100, 300, 30);
        t1.setBounds(250, 140, 300, 30);

        JLabel l2 = new JLabel("Enter Password");
        JPasswordField t2 = new JPasswordField(10);
        l2.setFont(f2);
        t2.setFont(f2);
        l2.setBounds(250, 200, 300, 30);
        t2.setBounds(250, 240, 300, 30);

        JButton b1 = new JButton("Submit");
        JButton b2 = new JButton("Back");

        styleButton(b1, new Color(0, 153, 76));
        styleButton(b2, new Color(255, 51, 51));

        b1.setBounds(300, 300, 200, 40);
        b2.setBounds(300, 360, 200, 40);

        Container c = getContentPane();
        c.setLayout(null);
        c.setBackground(new Color(235, 235, 235));

        c.add(title);
        c.add(l1);
        c.add(t1);
        c.add(l2);
        c.add(t2);
        c.add(b1);
        c.add(b2);

        b2.addActionListener(a -> {
            new Landing();
            dispose();
        });

        b1.addActionListener(a -> {
            String url = "jdbc:mysql://localhost:3306/batch2";

            try (Connection con = DriverManager.getConnection(
                    DBConfig.getURL(),
                    DBConfig.getUser(),
                    DBConfig.getPass()
            )) {

                String sql = "SELECT * FROM users WHERE username=? AND password=?";
                try (PreparedStatement pst = con.prepareStatement(sql)) {

                    String pass = new String(t2.getPassword());

                    pst.setString(1, t1.getText());
                    pst.setString(2, pass);

                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Successful");
                        new Home(t1.getText());
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "User doesn't exist");
                    }
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });

        setVisible(true);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Login");
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
        new Elogin();
    }
}
