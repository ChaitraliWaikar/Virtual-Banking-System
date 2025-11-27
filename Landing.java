import javax.swing.*;
import java.awt.*;

class Landing extends JFrame {
    Landing() {

        Font f = new Font("Futura", Font.BOLD, 40);
        Font f2 = new Font("Calibri", Font.PLAIN, 22);

        JLabel l1 = new JLabel("Virtual Banking System", JLabel.CENTER);
        l1.setFont(f);
        l1.setOpaque(true);
        l1.setBackground(new Color(0, 102, 204));
        l1.setForeground(Color.WHITE);
        l1.setBounds(150, 50, 500, 50);

        JButton b1 = new JButton("Admin");
        JButton b2 = new JButton("Existing Customer");
        JButton b3 = new JButton("New Customer");

        styleButton(b1, new Color(72, 201, 176));
        styleButton(b2, new Color(0, 170, 204));
        styleButton(b3, new Color(255, 153, 60));

        b1.setBounds(300, 150, 200, 50);
        b2.setBounds(300, 230, 200, 50);
        b3.setBounds(300, 310, 200, 50);

        Container c = getContentPane();
        c.setLayout(null);
        c.setBackground(new Color(235, 235, 235));

        c.add(l1);
        c.add(b1);
        c.add(b2);
        c.add(b3);

        b1.addActionListener(a -> {
            new Alogin();   // Admin login page
            dispose();
        });

        b2.addActionListener(a -> {
            new Elogin();   // Existing customer login
            dispose();
        });

        b3.addActionListener(a -> {
            new Nlogin();   // New customer registration
            dispose();
        });

        setVisible(true);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Landing Page");
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
        new Landing();
    }
}
