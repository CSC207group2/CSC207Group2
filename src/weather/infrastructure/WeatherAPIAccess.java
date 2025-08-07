package weather.infrastructure;

import weather.domain.WeatherService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

public class WeatherAPIAccess implements WeatherService {

    @Override
    public String getWeatherJson(String city, String date) throws Exception {
        LocalDate current = LocalDate.now();
        int elapsedDays = DateCalculator.getDatesBetween(current.toString(), date).size();
        System.out.println(elapsedDays);
        String requestUrl;
        if (elapsedDays <= 14) {
            requestUrl = "https://api.weatherapi.com/v1/forecast.json?key=" +
                    System.getenv("WeatherAPI") + "&q=" + city + "&days=" + elapsedDays;
        }else {
            requestUrl = "https://api.weatherapi.com/v1/future.json?key=" +
                    System.getenv("WeatherAPI") + "&q=" + city + "&dt=" + date;
        }
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