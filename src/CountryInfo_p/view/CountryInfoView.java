package CountryInfo_p.view;

import CountryInfo_p.interface_adapter.country_info.CountryInfoController;
import CountryInfo_p.use_case.country_info.CountryInfoOutputBoundary;
import CountryInfo_p.use_case.country_info.CountryInfoOutputData;
import logs.data_access.FirebaseLogs;
import core.HomePage;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class CountryInfoView extends JFrame implements CountryInfoOutputBoundary {
    private CountryInfoController controller;

    private final JTextField countryInputField;
    private final JButton searchButton;
    private final JTextArea resultArea;

    private final String username;
    private JButton saveButton;
    private CountryInfoOutputData lastResult;

    public CountryInfoView(String username) {
        super("Country Info");
        this.username = username;

        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        countryInputField = new JTextField(20);
        searchButton = new JButton("Search");
        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(new Font("SansSerif", Font.PLAIN, 18));

        // Initialized saveButton here
        saveButton = new JButton("Save Country");
        saveButton.setEnabled(false);
        saveButton.addActionListener(e -> saveCurrentCountry());

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Country Name:"));
        inputPanel.add(countryInputField);
        inputPanel.add(searchButton);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            resultArea.setText("");
            countryInputField.setText("");
            lastResult = null;
            saveButton.setEnabled(false);
        });
        inputPanel.add(clearButton);

        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // --- Bottom bar: Save + Go Back ---
        JButton goBackButton = new JButton("Go Back to Home Page");
        goBackButton.setFocusPainted(false);
        goBackButton.setBackground(Color.WHITE);
        goBackButton.setForeground(new Color(0, 102, 204));
        goBackButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        goBackButton.addActionListener(e -> {
            dispose();
            new HomePage(core.Main.getUsername()).setVisible(true);
        });

        saveButton = new JButton("Save Country");
        saveButton.setEnabled(false); // enabled when we have a result
        saveButton.addActionListener(e -> saveCurrentCountry());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(saveButton);
        bottomPanel.add(goBackButton);
        add(bottomPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> {
            if (controller == null) {
                JOptionPane.showMessageDialog(this, "Controller not set!");
                return;
            }
            String countryName = countryInputField.getText().trim();
            if (countryName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a country name.");
                return;
            }
            resultArea.setText("Loading...");
            saveButton.setEnabled(false);
            lastResult = null;
            controller.fetchCountryInfo(countryName);
        });
    }

    public void setController(CountryInfoController controller) {
        this.controller = controller;
    }

    @Override
    public void present(CountryInfoOutputData outputData) {
        SwingUtilities.invokeLater(() -> {
            lastResult = outputData;
            StringBuilder sb = new StringBuilder();
            sb.append("-------------------------------------------------\n");
            sb.append("Country: ").append(outputData.country).append("\n");
            sb.append("Capital: ").append(outputData.capital).append("\n");
            sb.append("Currency: ").append(outputData.currency).append("\n");
            sb.append("Languages: ").append(String.join(", ", outputData.languages)).append("\n");
            sb.append("-------------------------------------------------\n");
            resultArea.setText(sb.toString());
            saveButton.setEnabled(true);
        });
    }

    @Override
    public void presentError(String errorMessage) {
        SwingUtilities.invokeLater(() -> {
            resultArea.setText("Error: " + errorMessage);
            saveButton.setEnabled(false);
            lastResult = null;
        });
    }

    // --- save helper ---
    private void saveCurrentCountry() {
        if (lastResult == null) {
            JOptionPane.showMessageDialog(this, "Search a country first.");
            return;
        }
        // Build fields map (strings only)
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("capital", nullToDash(lastResult.capital));
        fields.put("currency", nullToDash(lastResult.currency));
        fields.put("languages", String.join(", ", lastResult.languages));

        FirebaseLogs.saveCountry(username, lastResult.country, fields);
        JOptionPane.showMessageDialog(this, "Saved \"" + lastResult.country + "\" to My Recent Logs.");
    }

    private static String nullToDash(String s) {
        return (s == null || s.isBlank()) ? "-" : s;
    }
}
