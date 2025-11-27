import javax.swing.*;
import java.awt.*;

class Apage extends JFrame {
    Apage() {
        Font f = new Font("Futura", Font.BOLD, 40);
        Font f2 = new Font("Calibri", Font.PLAIN, 22);

        JLabel l1 = new JLabel("Welcome Admin", JLabel.CENTER);
        JLabel l2 = new JLabel();
        JButton b1 = new JButton("Logout");
        JButton b2 = new JButton("Show All Users");

        l1.setFont(f);
        b1.setFont(f2);
        b2.setFont(f2);

        // Styled colors
        l1.setOpaque(true);
        l1.setBackground(new Color(0, 102, 204));
        l1.setForeground(Color.WHITE);

        b1.setBackground(new Color(255, 51, 51));
        b1.setForeground(Color.WHITE);
        b1.setFocusPainted(false);

        b2.setBackground(new Color(0, 153, 76));
        b2.setForeground(Color.WHITE);
        b2.setFocusPainted(false);

        Container c = getContentPane();
        c.setLayout(null);
        c.setBackground(new Color(235, 235, 235));

        l1.setBounds(110, 30, 600, 60);
        b2.setBounds(250, 120, 300, 45);
        b1.setBounds(250, 180, 300, 45);
        l2.setBounds(250, 240, 300, 30);

        c.add(l1);
        c.add(b2);
        c.add(b1);
        c.add(l2);

        b1.addActionListener(
                a->{
                    new Landing();
                    dispose();
                }
        );

        setVisible(true);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Admin Page");
    }

    public static void main(String[] args) {
        new Apage();
    }
}