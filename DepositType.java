import javax.swing.*;
import java.awt.*;

class DepositType extends JFrame {
    DepositType(String username) {

        Font f = new Font("Futura", Font.BOLD, 40);
        Font f2 = new Font("Calibri", Font.PLAIN, 22);

        JLabel title = new JLabel("Select Account", JLabel.CENTER);
        title.setFont(f);
        title.setOpaque(true);
        title.setBackground(new Color(0, 102, 204));
        title.setForeground(Color.WHITE);

        JButton b1 = new JButton("Savings");
        JButton b2 = new JButton("Current");
        JButton back = new JButton("Back");

        b1.setFont(f2);
        b2.setFont(f2);
        back.setFont(f2);

        Container c = getContentPane();
        c.setLayout(null);

        title.setBounds(200, 40, 400, 50);
        b1.setBounds(280, 150, 250, 45);
        b2.setBounds(280, 220, 250, 45);
        back.setBounds(280, 300, 250, 45);

        c.add(title);
        c.add(b1);
        c.add(b2);
        c.add(back);

        b1.addActionListener(a -> {
            new Deposit(username);   // Deposit already has dropdown → no need to send type
            dispose();
        });

        b2.addActionListener(a -> {
            new Deposit(username);
            dispose();
        });

        back.addActionListener(a -> {
            new Home(username);
            dispose();
        });

        setVisible(true);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
