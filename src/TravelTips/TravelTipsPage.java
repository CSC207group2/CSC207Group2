package TravelTips;

import core.HomePage;

import javax.swing.*;
import java.awt.*;

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
        JTextField cityField = new JTextField(20);
        JButton searchButton = new JButton("Find Out");

        inputPanel.add(label);
        inputPanel.add(cityField);
        inputPanel.add(searchButton);

        // Center result display with word wrap
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);           // Enable line wrapping
        resultArea.setWrapStyleWord(true);      // Wrap at word boundaries
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
            new HomePage(core.Main.getUsername()).setVisible(true); // Go back to homepage
        });

        searchButton.addActionListener(e -> {
            String input = cityField.getText().trim();

            if (input.isEmpty()) {
                resultArea.setText("Please enter a city or airport name.");
            } else {
                resultArea.setText("Fetching travel tips for " + input + "...\n\nPlease wait.");

                // Run OpenAI call in a new thread to keep UI responsive
                new Thread(() -> {
                    try {
                        String response = OpenAICall.getTravelInsights(input);
                        SwingUtilities.invokeLater(() -> resultArea.setText(response));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        SwingUtilities.invokeLater(() -> resultArea.setText("An error occurred while fetching tips."));
                    }
                }).start();
            }
        });
    }

    public static void showPanel() {
        new TravelTipsPage();
    }
}
