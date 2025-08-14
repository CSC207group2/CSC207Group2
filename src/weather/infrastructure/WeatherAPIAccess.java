package weather.infrastructure;

import weather.domain.WeatherService;
import weather.infrastructure.APIKey;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

/**
 * Provides access to the WeatherAPI service for retrieving weather data.
 */
public class WeatherAPIAccess implements WeatherService {

    /**
     * Retrieves weather data for the specified city and date in JSON format.
     * Uses WeatherAPI's forecast endpoint for dates up to 14 days ahead,
     * and the future endpoint for later dates.
     *
     * @param city name of the city to fetch weather for
     * @param date target date in {@code yyyy-MM-dd} format
     * @return weather data as a JSON string
     * @throws Exception if an HTTP request fails or another error occurs
     */
    @Override
    public String getWeatherJson(String city, String date) throws Exception {
        APIKey apiKey = new APIKey();
        LocalDate current = LocalDate.now();
        int elapsedDays = DateCalculator.getDatesBetween(current.toString(), date).size();
        System.out.println(elapsedDays);
        String requestUrl;
        if (elapsedDays <= 14) {
            requestUrl = "https://api.weatherapi.com/v1/forecast.json?key=" +
                    apiKey.APIKey + "&q=" + city + "&days=" + elapsedDays;
        }else {
            requestUrl = "https://api.weatherapi.com/v1/future.json?key=" +
                    apiKey.APIKey + "&q=" + city + "&dt=" + date;
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