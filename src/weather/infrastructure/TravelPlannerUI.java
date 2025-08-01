package weather.infrastructure;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TravelPlannerUI {

    private static final String CITY = "Toronto";
    private static final String DAYS = "5";
    private static final ArrayList<String> dateList = new ArrayList<>();
    private static final String FUTUREDATE = "2025-08-22";

    public static void main(String[] args) {
        debugPrintWeather();
    }

    private static void debugPrintWeather() {
        try {
            BufferedReader in = getBufferedReader();
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Print the raw JSON response
            System.out.println("Weather data for " + CITY + ":");
            String weatherString = response.toString();
            System.out.println(weatherString);
            JSONObject weather = new JSONObject(weatherString);
            JSONObject forecast = weather.getJSONObject("forecast");
            JSONArray forecastArray = new JSONArray(forecast.getJSONArray("forecastday"));

            for (Object day : forecastArray) {
                JSONObject currentDay = new JSONObject(day.toString());
                System.out.println(currentDay.getString("date"));
                JSONObject dayForecastSimple = currentDay.getJSONObject("day");
                System.out.println(dayForecastSimple.getDouble("maxtemp_c"));
                System.out.println(dayForecastSimple.getDouble("mintemp_c"));
                System.out.println("------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static BufferedReader getBufferedReader() throws IOException {
        String endpoint = "http://api.weatherapi.com/v1/forecast.json?key=" +
                Constants.API_KEY + "&q=" + CITY + "&days=" + DAYS + "&aqi=no&alerts=no";
        String endpointTest = "https://api.weatherapi.com/v1/future.json?key=" +
                Constants.API_KEY + "&q=" + CITY + "&dt=" + FUTUREDATE;
        // Create URL and open connection
        URL url = new URL(endpointTest);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // Read the response
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        return in;
    }
}
