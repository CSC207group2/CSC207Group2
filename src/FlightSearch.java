import com.google.gson.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlightSearch extends FlightSearchPage{
    public static void searchFlights(String departure, String arrival, String date, Integer stops, String returnDate,
                                     JTextArea resultsArea, JButton selectButton, JComboBox<String> flightSelector) {
        OkHttpClient client = new OkHttpClient();
        String apiKey = "bd819152c9b1d1d216a3283df4f544164f348d2ac206f60e0ff95472c2a0303d"; // Replace with your actual API key
        String url = "https://serpapi.com/search.json?engine=google_flights"
                + "&departure_id=" + departure.toUpperCase()
                + "&arrival_id=" + arrival.toUpperCase()
                + "&outbound_date=" + date
                + "&return_date=" + returnDate
                + "&api_key=" + apiKey
                + "&stops=" + stops;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String jsonData = response.body().string();
                JsonObject json = JsonParser.parseString(jsonData).getAsJsonObject();

                JsonArray flights = json.getAsJsonArray("best_flights");
                JsonArray otherFlights = json.getAsJsonArray("other_flights");

                flights.addAll(otherFlights);

                if (flights.isEmpty()) {
                    resultsArea.setText("No flights found.");
                    return;
                }

                StringBuilder sb = new StringBuilder();

                flightSelector.removeAllItems();

                for (int i = 0; i < flights.size(); i++) {
                    flightSelector.addItem("Flight #" + (i + 1));
                }

                int flight_counter = 0;
                Map<String, List<String>> departure_info = new HashMap<>();
                Map<String, List<String>> arrival_info = new HashMap<>();

                for (JsonElement flightElem : flights) {
                    flight_counter++;

                    JsonObject flight = flightElem.getAsJsonObject();

                    List<String> departure_data = new ArrayList<>();
                    if (flight.has("departure_airport")) {
                        String curr;
                        JsonObject data = flights.get(0).getAsJsonObject();
                        if (data.has("name")) {
                            curr = data.get("name").getAsString();
                            departure_data.add(curr);
                        }
                        if (data.has("time")) {
                            curr = data.get("time").getAsString();
                            departure_data.add(curr);
                        }
                    }
                    departure_info.put("Flight #" + (flight_counter) , departure_data);
                    departure_data.clear();

                    List<String> arrival_data = new ArrayList<>();
                    if (flight.has("arrival_airport")) {
                        String curr;
                        JsonObject data = flights.get(0).getAsJsonObject();
                        if (data.has("name")) {
                            curr = data.get("name").getAsString();
                            arrival_data.add(curr);
                        }
                        if (data.has("time")) {
                            curr = data.get("time").getAsString();
                            arrival_data.add(curr);
                        }
                    }
                    arrival_info.put("Flight #" + (flight_counter) , arrival_data);
                    arrival_data.clear();

                    String price = String.valueOf(flight.get("price"));

                    String airline = "Unknown";
                    int duration;
                    String flight_num = "Unknown";
                    String finalDuration = "Unknown";
                    if (flight.has("flights") && flight.get("flights").isJsonArray()){
                        JsonArray flightsArray = flight.getAsJsonArray("flights");
                            JsonObject firstFlight = flightsArray.get(0).getAsJsonObject();
                            if (firstFlight.has("airline")) {
                                airline = firstFlight.get("airline").getAsString();
                            }
                            if (firstFlight.has("duration")){
                                duration = firstFlight.get("duration").getAsInt();
                                int durationHours = duration / 60;
                                int durationMinute = duration % 60;
                                finalDuration = (durationHours) + "Hrs " + (durationMinute) + "M";
                            }
                            if (firstFlight.has("flight_number")){
                                flight_num = firstFlight.get("flight_number").getAsString();

                        }
                    }

                    if (flight.has("total_duration")){
                        duration = flight.get("total_duration").getAsInt();
                        int durationHours = duration / 60;
                        int durationMinute = duration % 60;
                        finalDuration = (durationHours) + "hrs " + (durationMinute) + "min";
                    }

                    sb.append("✈ Airline: ").append(airline).append("\n")
                            .append("🕒 Total Trip Duration: ").append(finalDuration).append("\n")
                            .append("💰 Price: $").append(price).append("\n").append("Flight Number: ")
                            .append(flight_num).append("\n");


                    int layover_duration;
                    String final_duration = "Unknown";
                    String layover_location = "Unknown" ;

                    if (flight.has("layovers")) {
                        JsonArray flightsArray = flight.getAsJsonArray("layovers");
                            JsonObject firstFlight = flightsArray.get(0).getAsJsonObject();
                            if (firstFlight.has("name")) {
                                layover_location = firstFlight.get("name").getAsString();
                            }
                            if (firstFlight.has("duration")){
                                layover_duration = firstFlight.get("duration").getAsInt();
                                int durationHours = layover_duration / 60;
                                int durationMinute = layover_duration % 60;
                                final_duration = (durationHours) + "hrs " + (durationMinute) + "min";
                            }
                            sb.append("\n").append("Layover Information: ").append("\n").append("🕒 Layover Duration: ").
                                    append(final_duration).append("\n").append("📍 Layover Location: ").
                                    append(layover_location);

                    }


                    sb.append("\n------------------------\n\n");


                }
                resultsArea.setText(sb.toString());



                JButton continueButton = new JButton("Continue");
                JButton backButton = new JButton("Go Back");
                JsonArray finalFlights = flights;
                List<JsonObject> selectedFlights = new ArrayList<>();
                selectButton.addActionListener(e -> {
                    int selectedIndex = flightSelector.getSelectedIndex();
                    if (selectedIndex >= 0) {
                        JsonObject selectedFlight = finalFlights.get(selectedIndex).getAsJsonObject();
                        System.out.println(selectedFlight);
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
                                   flightSelector.getSelectedItem().toString());
                       });
                    }
                });



            } else {
                System.out.println("Request failed: " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultsArea.setText("An error occurred: " + e.getMessage());
        }
    }
}