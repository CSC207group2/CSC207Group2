package TravelTips;

import Flights.FlightSearchPage;
import core.HomePage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TravelTipsPage extends JFrame {

    public TravelTipsPage() {
        setTitle("Travel Tips & Warnings");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen

        // Main panel setup
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Top input panel
        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("City or Airport Name:");
        JTextField countryField = new JTextField(20);
        JButton searchButton = new JButton("Find Out");

        inputPanel.add(label);
        inputPanel.add(countryField);
        inputPanel.add(searchButton);

        // Center result display
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Bottom panel with styled "Go Back to Home Page" button
        JPanel bottomPanel = new JPanel();
        JButton goBackButton = new JButton("Go Back to Home Page");
        goBackButton.setFocusPainted(false);
        goBackButton.setBackground(Color.WHITE);
        goBackButton.setForeground(new Color(0, 102, 204));
        goBackButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        bottomPanel.add(goBackButton);

        // Add components to the main panel
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);

        // Button listeners
        goBackButton.addActionListener(e -> {
            dispose(); // Close current window
            new HomePage(core.Main.getUsername()).setVisible(true); // Go back to home (assuming it exists)
        });

        searchButton.addActionListener(e -> {
            String country = countryField.getText();
            if (country.isEmpty()) {
                resultArea.setText("Please enter a country name.");
            } else {
                // Placeholder logic
                resultArea.setText("Travel tips and warnings for " + country + ":\n\n- Tip 1\n- Tip 2\n- Warning 1");
            }
        });


    }

    public static void showPanel() {
        new TravelTipsPage();
    }
}
