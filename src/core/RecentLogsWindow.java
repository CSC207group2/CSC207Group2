package core;

import javax.swing.*;
import java.awt.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * This class opens a window to show the user's saved flight and country info logs.
 */
public class RecentLogsWindow extends JFrame {

    /**
     * Constructor initializes the UI and fetches logs from Firebase.
     */
    public RecentLogsWindow(String username) {
        setTitle("My Recent Logs");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // UI component to display logs
        JTextArea recentLogs = new JTextArea();
        recentLogs.setEditable(false);
        JScrollPane recentLogsScrollPane = new JScrollPane(recentLogs);
        add(recentLogsScrollPane, BorderLayout.CENTER);

        // Fetch and display logs
        fetchLogsFromFirebase(username, recentLogs);

        setVisible(true);
    }

    /**
     * Fetches the saved flight and country logs from Firebase for the given user.
     *
     * @param username the Firebase username
     * @param textArea the JTextArea to populate with logs
     */
    private void fetchLogsFromFirebase(String username, JTextArea textArea) {
        // 🔗 Get reference to this user in Firebase DB
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("data")
                .child(username);

        // 👂 Read data once using ValueEventListener
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StringBuilder logs = new StringBuilder();

                // ✈ Flights log
                if (dataSnapshot.hasChild("savedFlights")) {
                    logs.append("✈ Flights:\n");
                    for (DataSnapshot flightSnapshot : dataSnapshot.child("savedFlights").getChildren()) {
                        for (DataSnapshot field : flightSnapshot.getChildren()) {
                            logs.append(field.getKey()).append(": ")
                                    .append(field.getValue().toString()).append("\n");
                        }
                        logs.append("\n");
                    }
                }

                // 🌍 Country log
                if (dataSnapshot.hasChild("savedCountries")) {
                    logs.append("🌍 Country Info:\n");
                    for (DataSnapshot countrySnapshot : dataSnapshot.child("savedCountries").getChildren()) {
                        logs.append(countrySnapshot.getKey()).append(": ")
                                .append(countrySnapshot.getValue().toString()).append("\n");
                    }
                }

                if (!dataSnapshot.hasChild("savedFlights") && !dataSnapshot.hasChild("savedCountries")) {
                    textArea.setText("No logs yet. Try saving a flight or a country from the app.");
                    return;
                }

                // 📝 Update the text area with logs
                textArea.setText(logs.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                textArea.setText("Error loading logs: " + error.getMessage());
            }
        });
    }
}
