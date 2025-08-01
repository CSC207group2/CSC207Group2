package weather.infrastructure;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

public class WeatherAPIAccess {

    public static String getWeatherJson(String city, String date) throws Exception {
        LocalDate current = LocalDate.now();
        int elapsedDays = DateCalculator.getDatesBetween(current.toString(), date).size();

        if (elapsedDays < 14) {
            String requestUrl = "https://api.weatherapi.com/v1/forecast.json?key=" +
                    Constants.API_KEY + "&q=" + city + "&days=" + elapsedDays;
        }

        String requestUrl = "https://api.weatherapi.com/v1/future.json?key=" +
                Constants.API_KEY + "&q=" + city + "&dt=" + date;

        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Optional: handle non-200 responses
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            System.out.println(requestUrl);
            throw new RuntimeException("HTTP GET Request Failed with Code: " + responseCode);

        }

        // Read the JSON response
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder json = new StringBuilder();
        String line;

        while ((line = in.readLine()) != null) {
            json.append(line);
        }
        in.close();
        return json.toString();
    }
}