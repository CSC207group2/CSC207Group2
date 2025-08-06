package Flights;

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
                Map<String, Map<String, String>> departure_info = new HashMap<>();
                Map<String, Map<String, String>> arrival_info = new HashMap<>();
                List<String> travel_class = new ArrayList<>();

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
                        departure_info.put("Flight #" + (flight_counter) , departure_data);
                        arrival_info.put("Flight #" + (flight_counter) , arrival_data);
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

                selectButton.addActionListener(e -> {
                    FlightSelectedPanel flightSelectedPanel = new FlightSelectedPanel(flights, flightSelector,
                            departure_info, arrival_info, travel_class);

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