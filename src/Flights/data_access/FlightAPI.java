package Flights.data_access;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.swing.*;
import java.io.IOException;

public class FlightAPI {

    public JsonArray searchFlights(String departure, String arrival, String date,
                                   String returnDate, Integer stops) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String apiKey = "bd819152c9b1d1d216a3283df4f544164f348d2ac206f60e0ff95472c2a0303d";
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

                return flights;
            }
        }
            return null;
    }
}

