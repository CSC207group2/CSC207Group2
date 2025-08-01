package CountryInfo_p.view;

import CountryInfo_p.interface_adapter.country_info.CountryInfoController;
import CountryInfo_p.use_case.country_info.CountryInfoOutputBoundary;
import CountryInfo_p.use_case.country_info.CountryInfoOutputData;

import javax.swing.*;
import java.awt.*;

public class CountryInfoView extends JFrame implements CountryInfoOutputBoundary {
    private CountryInfoController controller;

    private final JTextField countryInputField;
    private final JButton searchButton;
    private final JTextArea resultArea;

    public CountryInfoView() {
        super("Country Info");

        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        countryInputField = new JTextField(20);
        searchButton = new JButton("Search");
        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Country Name:"));
        inputPanel.add(countryInputField);
        inputPanel.add(searchButton);

        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(e -> {
            if (controller == null) {
                JOptionPane.showMessageDialog(this, "Controller not set!");
                return;
            }
            String countryName = countryInputField.getText();
            resultArea.setText("Loading...");
            controller.fetchCountryInfo(countryName);
        });
    }

    public void setController(CountryInfoController controller) {
        this.controller = controller;
    }

    @Override
    public void present(CountryInfoOutputData outputData) {
        SwingUtilities.invokeLater(() -> {
            StringBuilder sb = new StringBuilder();
            sb.append("Country: ").append(outputData.country).append("\n");
            sb.append("Capital: ").append(outputData.capital).append("\n");
            sb.append("Currency: ").append(outputData.currency).append("\n");
            sb.append("Languages: ").append(String.join(", ", outputData.languages));
            resultArea.setText(sb.toString());
        });
    }

    @Override
    public void presentError(String errorMessage) {
        SwingUtilities.invokeLater(() -> {
            resultArea.setText("Error: " + errorMessage);
        });
    }
}
