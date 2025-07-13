import javax.swing.*;

public class HomePage extends JFrame {
    public HomePage(String username) {
        super("Home - Travel Planner");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel welcome = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        welcome.setFont(welcome.getFont().deriveFont(18f));
        add(welcome);
    }
}