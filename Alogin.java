// ADMIN LOGIN PAGE
import javax.swing.*;
import java.awt.*;

class Alogin extends JFrame {

    Alogin() {

        // Fonts
        Font f = new Font("Futura", Font.BOLD, 40);
        Font f2 = new Font("Calibri", Font.PLAIN, 22);

        // Components
        JLabel title = new JLabel("Admin Login", JLabel.CENTER);
        JLabel l1 = new JLabel("Enter Username");
        JTextField t1 = new JTextField(10);
        JLabel l2 = new JLabel("Enter Password");
        JPasswordField p1 = new JPasswordField(10);
        JButton b1 = new JButton("Submit");
        JButton b2 = new JButton("Back");

        // Set Fonts
        title.setFont(f);
        l1.setFont(f2);
        t1.setFont(f2);
        l2.setFont(f2);
        p1.setFont(f2);
        b1.setFont(f2);
        b2.setFont(f2);

        // Styling
        getContentPane().setBackground(new Color(245, 245, 255));
        title.setForeground(new Color(70, 70, 180));

        l1.setForeground(new Color(40, 40, 90));
        l2.setForeground(new Color(40, 40, 90));

        t1.setBackground(Color.white);
        p1.setBackground(Color.white);

        b1.setBackground(new Color(90, 140, 255));
        b1.setForeground(Color.white);
        b1.setFocusPainted(false);

        b2.setBackground(new Color(255, 130, 130));
        b2.setForeground(Color.white);
        b2.setFocusPainted(false);

        // Borders
        t1.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 220), 2));
        p1.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 220), 2));

        // Layout setup
        Container c = getContentPane();
        c.setLayout(null);

        // Positioning
        title.setBounds(250, 30, 300, 50);
        l1.setBounds(250, 100, 300, 30);
        t1.setBounds(250, 140, 300, 30);
        l2.setBounds(250, 200, 300, 30);
        p1.setBounds(250, 240, 300, 30);
        b1.setBounds(300, 300, 200, 40);
        b2.setBounds(300, 360, 200, 40);

        // Add to frame
        c.add(title);
        c.add(l1);
        c.add(t1);
        c.add(l2);
        c.add(p1);
        c.add(b1);
        c.add(b2);

        // Frame settings
        setVisible(true);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Admin Login");

        // Back button action
        b2.addActionListener(a -> {
            new Landing();
            dispose();
        });

        // Login authentication
        b1.addActionListener(a -> {
            String pass = new String(p1.getPassword());

            if (t1.getText().equals("admin") && pass.equals("pass")) {
                new Adashboard();
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Admin login failed");
            }
        });
    }

    public static void main(String[] args) {
        new Alogin();
    }
}
