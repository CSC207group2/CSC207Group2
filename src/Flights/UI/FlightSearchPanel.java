package Flights.UI;

import javax.swing.*;

import Flights.usecase.FlightSearchInteractor;
import core.*;
import logs.interface_adapter.LabelTextPanel;
import org.jdatepicker.impl.*;
import org.jetbrains.annotations.NotNull;
import weather.ui.DateLabelFormatter;

import java.util.Properties;
import java.text.SimpleDateFormat;


public class FlightSearchPanel extends JFrame {
    JTextField departureField = new JTextField(10);
    LabelTextPanel from = new LabelTextPanel(new JLabel("From: "), departureField);
    JTextField arrivalField = new JTextField(10);
    LabelTextPanel to = new LabelTextPanel(new JLabel("To: "), arrivalField);
    String departureDate;
    String arrivalDate;
    JButton searchButton = new JButton("Search Flights");
    JTextArea resultsArea = new JTextArea(20,80);
    JButton selectButton = new JButton("Select Flight");
    JComboBox<String> flightSelector = new JComboBox<>();
    JButton backButton = new JButton("Go Back to Home Page");

    public FlightSearchPanel() {
        super("Flight Search");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel flightPanel = getjPanel();

        getContentPane().add(flightPanel);
        setVisible(true);
    }

    @NotNull
    private JPanel getjPanel() {
        UtilDateModel departureModel = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        UtilDateModel arrivalModel = new UtilDateModel();
        Properties p2 = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        JDatePanelImpl departureDatePanel = new JDatePanelImpl(departureModel, p);
        JDatePickerImpl departureDatePicker = new JDatePickerImpl(departureDatePanel, new DateLabelFormatter());

        JDatePanelImpl arrivalDatePanel = new JDatePanelImpl(arrivalModel, p2);
        JDatePickerImpl arrivalDatePicker = new JDatePickerImpl(arrivalDatePanel, new DateLabelFormatter());

        Integer[] numbers = {0, 1, 2, 3};
        JComboBox<Integer> stops = new JComboBox<>(numbers);

        JPanel flightPanel = new JPanel();
        flightPanel.add(from);
        flightPanel.add(to);
        flightPanel.add(departureDatePicker);
        flightPanel.add(arrivalDatePicker);
        flightPanel.add(stops);
        flightPanel.add(searchButton);

        resultsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        flightPanel.add(new JLabel("Available Flights:"));
        flightPanel.add(scrollPane);
        flightPanel.add(backButton);
        flightPanel.add(flightSelector);
        flightPanel.add(selectButton);

        backButton.addActionListener(e -> {
            dispose();
            new HomePage(Main.getUsername()).setVisible(true);
        });

        searchButton.addActionListener(e -> {
            java.util.Date departureSelectedDate = (java.util.Date) departureDatePicker.getModel().getValue();
            java.util.Date arrivalSelectedDate = (java.util.Date) arrivalDatePicker.getModel().getValue();
            if (departureSelectedDate != null) {
                departureDate = new SimpleDateFormat("yyyy-MM-dd").format(departureSelectedDate);
            }
            if (arrivalSelectedDate != null){
                arrivalDate = new SimpleDateFormat("yyyy-MM-dd").format(arrivalSelectedDate);
            }
            String dest = departureField.getText();
            String ariv = arrivalField.getText();
            String date = departureDate;
            String returnDate = arrivalDate;
            Integer stop = stops.getSelectedIndex();
            resultsArea.setText("Searching...\n");
            try {
                FlightSearchInteractor.searchFlights(dest, ariv, date, stop, returnDate, resultsArea, selectButton, flightSelector);
            } catch (Exception e1) {
                e1.printStackTrace();
                resultsArea.setText("An error occurred: " + e1.getMessage());
            }
        });
        return flightPanel;
    }

    public static void showPanel(){
        new FlightSearchPanel();
    }
}
