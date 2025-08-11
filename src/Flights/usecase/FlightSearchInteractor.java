package Flights.usecase;

import Flights.UI.FlightSelectedPanel;
import Flights.data_access.FlightAPI;
import com.google.gson.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlightSearchInteractor{

    private static String username;

    public static void setUsername(String u) {
        username = u;
    }

    public static void searchFlights(String departure, String arrival, String date, Integer stops, String returnDate,
                                     JTextArea resultsArea, JButton selectButton, JComboBox<String> flightSelector) throws IOException {
        FlightAPI flightAPI = new FlightAPI();
        JsonArray flights = flightAPI.searchFlights(departure, arrival, date, returnDate, stops);

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
        Map<String, Map<String, String>> departure_info = new HashMap<>();
        Map<String, Map<String, String>> arrival_info = new HashMap<>();
        List<String> travel_class = new ArrayList<>();

        getFlightSearch(flights, flight_counter, departure_info, arrival_info, travel_class, sb);
        resultsArea.setText(sb.toString());

        selectButton.addActionListener(e -> {
            new FlightSelectedPanel(flights, flightSelector, departure_info, arrival_info, travel_class, username);
        });
    }

    private static void getFlightSearch(JsonArray flights, int flight_counter, Map<String, Map<String, String>> departure_info, Map<String, Map<String, String>> arrival_info, List<String> travel_class, StringBuilder sb) {
        for (JsonElement flightElem : flights) {
            flight_counter++;

            JsonObject flight = flightElem.getAsJsonObject();

            if (flight.has("flights")) {
                String curr;
                JsonArray data = flight.getAsJsonArray("flights");
                JsonObject flight_info = data.get(0).getAsJsonObject();

                Map<String, String> departure_data = new HashMap<>();
                Map<String, String> arrival_data = new HashMap<>();

                if (flight_info.has("departure_airport")){
                    JsonObject dep = flight_info.get("departure_airport").getAsJsonObject();
                    if (dep.has("name")) {
                        curr = dep.get("name").getAsString();
                        departure_data.put("name", curr);
                    }
                    if (dep.has("time")) {
                        curr = dep.get("time").getAsString();
                        departure_data.put("time", curr);
                    }
                }
                if (flight_info.has("arrival_airport")){
                    JsonObject ari = flight_info.get("arrival_airport").getAsJsonObject();
                    if (ari.has("name")) {
                        curr = ari.get("name").getAsString();
                        arrival_data.put("name", curr);
                    }
                    if (ari.has("time")) {
                        curr = ari.get("time").getAsString();
                        arrival_data.put("time", curr);
                    }
                }
                if (flight_info.has("flight_number")){
                    curr = flight_info.get("flight_number").getAsString();
                    arrival_data.put("flight_id", curr);
                }
                departure_info.put("Flight #" + flight_counter, departure_data);
                arrival_info.put("Flight #" + flight_counter, arrival_data);
            }

            String price = String.valueOf(flight.get("price"));
            travel_class.add(String.valueOf(flight.get("travel_class")));

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

            sb.append("Flight #").append(flight_counter).append("\n").append("✈ Airline: ").append(airline)
                    .append("\n").append("🕒 Total Trip Duration: ").append(finalDuration).append("\n")
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
                if (firstFlight.has("duration")) {
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

    }
}