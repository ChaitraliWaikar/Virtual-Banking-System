import javax.swing.*;
import java.awt.*;
import java.sql.*;

class AccountType extends JFrame {

    AccountType(String username) {

        //Fonts
        Font f = new Font("Futura", Font.BOLD, 35);
        Font f2 = new Font("Calibri", Font.PLAIN, 20);

        JLabel title = new JLabel("Update Account Type", JLabel.CENTER);
        title.setFont(f);
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(0, 102, 204));

        JLabel l1 = new JLabel("Select Account Type:");
        JComboBox<String> box = new JComboBox<>(new String[]{
                "Savings", "Current"
        });

        JButton b1 = new JButton("Update");
        JButton b2 = new JButton("Back");

        l1.setFont(f2);
        box.setFont(f2);
        b1.setFont(f2);
        b2.setFont(f2);

        b1.setBackground(new Color(0, 102, 204));
        b1.setForeground(Color.WHITE);
        b1.setFocusPainted(false);

        b2.setBackground(new Color(255, 51, 51));
        b2.setForeground(Color.WHITE);
        b2.setFocusPainted(false);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(title, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(null);
        centerPanel.setBackground(new Color(240, 240, 240));

        l1.setBounds(200, 80, 250, 30);
        box.setBounds(430, 80, 180, 30);
        b1.setBounds(260, 160, 120, 40);
        b2.setBounds(420, 160, 120, 40);

        centerPanel.add(l1);
        centerPanel.add(box);
        centerPanel.add(b1);
        centerPanel.add(b2);

        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        c.add(topPanel, BorderLayout.NORTH);
        c.add(centerPanel, BorderLayout.CENTER);

        //Back button
        b2.addActionListener(a -> {
            new Home(username);
            dispose();
        });

        //Update account type
        b1.addActionListener(a -> {

            String selectedType = box.getSelectedItem().toString();
            String url = "jdbc:mysql://localhost:3306/batch2";

            try (Connection con = DriverManager.getConnection(
                    DBConfig.getURL(),
                    DBConfig.getUser(),
                    DBConfig.getPass()
            )) {

                // Check if account type already exists
                String check = "SELECT COUNT(*) FROM accounts WHERE username=? AND acc_type=?";
                try (PreparedStatement pst = con.prepareStatement(check)) {
                    pst.setString(1, username);
                    pst.setString(2, selectedType);

                    ResultSet rs = pst.executeQuery();
                    rs.next();

                    if (rs.getInt(1) > 0) {
                        JOptionPane.showMessageDialog(null, selectedType + " Account already exists!");
                        return;
                    }
                }

                // Insert new account type
                String insert = "INSERT INTO accounts(username, acc_type, balance) VALUES (?, ?, 0)";
                try (PreparedStatement pst2 = con.prepareStatement(insert)) {
                    pst2.setString(1, username);
                    pst2.setString(2, selectedType);

                    pst2.executeUpdate();

                    JOptionPane.showMessageDialog(null,
                            selectedType + " Account created successfully!");
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }

        });


        setVisible(true);
        setSize(800, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Account Type");
    }

    public static void main(String[] args) {
        new AccountType("Chaitrali");
    }
}
