package Flights;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FlightSelectedPanel {
    public FlightSelectedPanel(JsonArray flights, JComboBox<String> flightSelector, Map<String, Map<String, String>>
            departure_info, Map<String, Map<String, String>> arrival_info, List<String> travel_class, String username){
        JButton continueButton = new JButton("Continue");
        JButton backButton = new JButton("Go Back");
        List<JsonObject> selectedFlights = new ArrayList<>();

        int selectedIndex = flightSelector.getSelectedIndex();
        if (selectedIndex >= 0) {
            JsonObject selectedFlight = flights.get(selectedIndex).getAsJsonObject();
            String airline = "Unknown" ;
            String flight_num = "Unknown";
            if (selectedFlight.has("flights")){
                JsonArray flightArray = selectedFlight.getAsJsonArray("flights");
                JsonObject flightDetails = flightArray.get(0).getAsJsonObject();
                airline = flightDetails.get("airline").getAsString();
                flight_num = flightDetails.get("flight_number").getAsString();
            }
            selectedFlights.add(selectedFlight);
            // Do something with selectedFlight:
            JPanel selected = new JPanel();
            selected.setLayout(new BoxLayout(selected, BoxLayout.Y_AXIS));
            JLabel line1 = new JLabel("You Selected: ");
            JLabel line2 = new JLabel("Price: $" + selectedFlight.get("price").getAsInt());
            JLabel line3 = new JLabel("Airline: " + airline);
            JLabel line4 = new JLabel("Flight Number: " + flight_num);
            selected.add(line1);
            selected.add(line2);
            selected.add(line3);
            selected.add(line4);
            JPanel buttons = new JPanel();
            buttons.add(backButton);
            buttons.add(continueButton);
            selected.add(buttons);
            selected.setVisible(true);
            JFrame showSelection = new JFrame();
            showSelection.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            showSelection.getContentPane().add(selected);
            showSelection.setSize(275, 125);
            showSelection.setLocationRelativeTo(null);
            showSelection.setVisible(true);
            backButton.addActionListener(e1 -> {
                selectedFlights.clear();
                showSelection.dispose();
            });

            continueButton.addActionListener(e2 -> {
                showSelection.dispose();
                FlightSelection.selectedFlight(departure_info, arrival_info,
                        Objects.requireNonNull(flightSelector.getSelectedItem()).toString(), travel_class, username);
            });
        }
        }
    }


