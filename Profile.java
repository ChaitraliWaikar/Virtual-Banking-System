import javax.swing.*;
import java.awt.*;
import java.sql.*;

class Profile extends JFrame {

    String naya = "";

    Profile(String username) {

        // Fonts
        Font f = new Font("Futura", Font.BOLD, 35);
        Font f2 = new Font("Calibri", Font.PLAIN, 20);

        JLabel title = new JLabel("Profile Settings", JLabel.CENTER);
        title.setFont(f);
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(0, 102, 204));

        JLabel l1 = new JLabel("Select Field to Update:");
        JComboBox<String> box = new JComboBox<>(new String[]{"Username", "Password", "Phone", "Email"});

        JLabel l2 = new JLabel("Enter New Value:");
        JTextField t1 = new JTextField(15);

        JButton b1 = new JButton("Update");
        JButton b2 = new JButton("Back");
        JButton b3 = new JButton("Update Account Type");

        l1.setFont(f2);
        box.setFont(f2);
        l2.setFont(f2);
        t1.setFont(f2);
        b1.setFont(f2);
        b2.setFont(f2);
        b3.setFont(f2);

        b1.setBackground(new Color(0, 102, 204));
        b1.setForeground(Color.WHITE);
        b1.setFocusPainted(false);

        b2.setBackground(new Color(255, 51, 51));
        b2.setForeground(Color.WHITE);
        b2.setFocusPainted(false);

        b3.setBackground(new Color(72, 201, 176));
        b3.setForeground(Color.WHITE);
        b3.setFocusPainted(false);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(title, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(null);
        centerPanel.setBackground(new Color(240, 240, 240));

        // positions
        l1.setBounds(200, 60, 200, 30);
        box.setBounds(400, 60, 200, 30);
        l2.setBounds(200, 120, 200, 30);
        t1.setBounds(400, 120, 200, 30);
        b1.setBounds(250, 190, 120, 40);
        b2.setBounds(400, 190, 120, 40);
        b3.setBounds(190, 260, 400, 40);

        centerPanel.add(l1);
        centerPanel.add(box);
        centerPanel.add(l2);
        centerPanel.add(t1);
        centerPanel.add(b1);
        centerPanel.add(b2);
        centerPanel.add(b3);

        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        c.add(topPanel, BorderLayout.NORTH);
        c.add(centerPanel, BorderLayout.CENTER);

        // Back button
        b2.addActionListener(a -> {
            new Home(username);
            dispose();
        });

        // Account Type button
        b3.addActionListener(a -> {
            new AccountType(username);
            dispose(); // ✔ fixed (removed the stray "z")
        });

        // Update button
        b1.addActionListener(a -> {

            String s1 = box.getSelectedItem().toString().toLowerCase();
            String s2 = t1.getText();

            if (s2.isEmpty()) {
                JOptionPane.showMessageDialog(null, "cannot be empty");
                return;
            }

            String url = "jdbc:mysql://localhost:3306/batch2";

            // Username update special case
            if (s1.equals("username")) {

                try (Connection con = DriverManager.getConnection(
                        DBConfig.getURL(),
                        DBConfig.getUser(),
                        DBConfig.getPass()
                )) {

                    String sql1 = "update users set username =? where username=?";
                    try (PreparedStatement pst = con.prepareStatement(sql1)) {
                        pst.setString(1, s2);
                        pst.setString(2, username);
                        pst.executeUpdate();
                    }

                    String sql2 = "update transactions set username=? where username=?";
                    try (PreparedStatement pst2 = con.prepareStatement(sql2)) {
                        pst2.setString(1, s2);
                        pst2.setString(2, username);
                        pst2.executeUpdate();
                    }

                    JOptionPane.showMessageDialog(null, "Username Updated Successfully!");

                    dispose();
                    new Profile(s2);
                    return;

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                    return;
                }
            }

            // General update (password, phone, email)
            try (Connection con = DriverManager.getConnection(
                    DBConfig.getURL(),
                    DBConfig.getUser(),
                    DBConfig.getPass()
            )) {

                String sql = "update users set " + s1 + " =? where username=?";

                try (PreparedStatement pst = con.prepareStatement(sql)) {

                    pst.setString(1, s2);
                    pst.setString(2, username);

                    pst.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Successfully Updated");
                    t1.setText("");
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }

        });

        setVisible(true);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Profile Settings");
    }

    public static void main(String[] args) {
        new Profile("Chaitrali");
    }
}
