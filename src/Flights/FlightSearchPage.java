package Flights;

import javax.swing.*;
import core.*;

public class FlightSearchPage extends JFrame {
    JTextField departureField = new JTextField(10);
    LabelTextPanel from = new LabelTextPanel(new JLabel("From: "),departureField);
    JTextField arrivalField = new JTextField(10);
    LabelTextPanel to = new LabelTextPanel(new JLabel("To: "),arrivalField);
    JTextField departureDateField = new JTextField("YYYY-MM-DD");
    JTextField returnDateField = new JTextField("YYYY-MM-DD");
    JButton searchButton = new JButton("Search Flights");
    JTextArea resultsArea = new JTextArea(20,50);
    JButton selectButton = new JButton("Select Flight");
    JComboBox<String> flightSelector = new JComboBox<>();
    JButton backButton = new JButton("Go Back to Home Page");

    public FlightSearchPage() {
        super("Flight Search");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Integer[] numbers = {0, 1, 2, 3};
        JComboBox<Integer> stops = new JComboBox<>(numbers);

        JPanel flightPanel = new JPanel();
        flightPanel.add(from);
        flightPanel.add(to);
        flightPanel.add(departureDateField);
        flightPanel.add(returnDateField);
        flightPanel.add(stops);
        flightPanel.add(searchButton);

        resultsArea.setEditable(false); // users shouldn't type into it
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        flightPanel.add(new JLabel("Available Flights:"));
        flightPanel.add(scrollPane);
        flightPanel.add(selectButton);
        flightPanel.add(flightSelector);
        flightPanel.add(backButton);

        backButton.addActionListener(e -> {
            dispose();
            new HomePage(Main.getUsername()).setVisible(true);
        });

        searchButton.addActionListener(e -> {
            String dest = departureField.getText();
            String ariv = arrivalField.getText();
            String date = departureDateField.getText().trim();
            String returnDate = returnDateField.getText().trim();
            Integer stop = stops.getSelectedIndex();
            resultsArea.setText("Searching...\n");
            FlightSearch.searchFlights(dest, ariv, date, stop, returnDate, resultsArea, selectButton, flightSelector);
        });


        getContentPane().add(flightPanel);
        setVisible(true);
    }

    public static void showPanel(){
        new FlightSearchPage();
    }
}
