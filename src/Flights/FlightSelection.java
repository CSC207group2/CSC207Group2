package Flights;

import javax.swing.*;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Map;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import core.*;

public class FlightSelection extends JFrame {
    public static void selectedFlight(Map<String, Map<String, String>> departure_data, Map<String,
            Map<String, String>> arrival_data, String flight, List<String> travel_class){

        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();

        String depAirport = departure_data.get(flight).get("name");
        String depTime = departure_data.get(flight).get("time");
        String arrAirport = arrival_data.get(flight).get("name");
        String arrTime = arrival_data.get(flight).get("time");
        String flight_id = arrival_data.get(flight).get("flight_id");

        System.out.println(flight_id);

        sb.append("Leaving From: ").append(depAirport).append(" at ").append(depTime);
        sb1.append("Arriving To: ").append(arrAirport).append(" at ").append(arrTime);

        JPanel selected = new JPanel();
        selected.setLayout(new BoxLayout(selected, BoxLayout.Y_AXIS));
        JLabel dep = new JLabel(String.valueOf(sb));
        JLabel travelClass = new JLabel("Travel Class: Economy"); //Can be changed if we have time to filter by travel class
        JLabel ari = new JLabel(String.valueOf(sb1));
        selected.add(dep);
        selected.add(travelClass);
        selected.add(ari);
        selected.add(travelClass);
        JButton saveButton = new JButton("Save Flight");
        JButton backButton = new JButton("Go Back");
        JPanel buttons = new JPanel();
        buttons.add(backButton);
        buttons.add(saveButton);
        selected.add(buttons);
        selected.setVisible(true);
        JFrame showSelection = new JFrame();
        showSelection.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        showSelection.getContentPane().add(selected);
        showSelection.setSize(600, 120);
        showSelection.setLocationRelativeTo(null);
        showSelection.setVisible(true);
        backButton.addActionListener(e1 -> showSelection.dispose());
        saveButton.addActionListener(e -> {;
//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference ref = database.getReference("users").child(Main.getUsername()).child("savedFlights");
//
            SavedFlights chosenFlight = new SavedFlights(flight_id, depAirport, arrAirport, depTime, arrTime);
//            ref.child(flight_id).setValueAsync(flight);

            System.out.println(chosenFlight.print());;
        });
    }
}

