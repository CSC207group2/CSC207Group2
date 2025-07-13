import javax.swing.*;
import java.awt.*;

public class HomePage extends JFrame {
    public HomePage(String username) {
        super("Home - Travel Planner");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // The main panel with a BoxLayout
        JPanel mainPanel = new JPanel() {
            // paint the background sky blue
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(new Color(135, 206, 250));    // light sky blue
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // The Welcome Label
        JLabel welcome = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcome.setFont(new Font("SansSerif", Font.BOLD, 20));
        welcome.setForeground(Color.WHITE);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 0));
        buttonPanel.setOpaque(false);   // setting transparent panel to show background
        JButton planTripButton = styledButton("Plan a Trip");
        JButton viewTripButton = styledButton("View Trips");
        JButton logoutButton = styledButton("Logout");

        buttonPanel.add(planTripButton);
        buttonPanel.add(viewTripButton);
        buttonPanel.add(logoutButton);

        // adding components to the main panel
        mainPanel.add(welcome);
        // adding spacer between components in main panel
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));   // 0 pixels wide, 30 pixels tall
        mainPanel.add(buttonPanel);

        add(mainPanel);

        logoutButton.addActionListener(e -> {
            dispose();  // close the HomePage window
            Main.showLoginScreen();  // re-open the login screen
        });

    }

    // creating a stylish button template
    private JButton styledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(0, 102, 204)); // nice blue color
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
        return button;
    }

}