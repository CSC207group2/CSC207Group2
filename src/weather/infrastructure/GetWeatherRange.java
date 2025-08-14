package weather.infrastructure;

import org.json.JSONObject;
import weather.domain.WeatherService;
import java.util.ArrayList;

/**
 * Retrieves weather data for a range of dates for a specified city.
 */
public class GetWeatherRange {
    private final WeatherService weatherService;

    public GetWeatherRange(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Fetches weather information for each date in the given list and returns it as JSON objects.
     *
     * @param dateList list of dates in {@code yyyy-MM-dd} format
     * @param city     name of the city to fetch weather for
     * @return list of weather data as {@link org.json.JSONObject}
     * @throws Exception if the city is invalid or an error occurs during retrieval
     */
    public ArrayList<JSONObject> returnWeatherList(ArrayList<String> dateList, String city) throws Exception {
        ArrayList<JSONObject> weatherList = new ArrayList<>();
        for (String date : dateList) {
            JSONObject weatherJson = new JSONObject(weatherService.getWeatherJson(city, date));
            weatherList.add(weatherJson);
            if (weatherJson.has("error")) {
                String message = weatherJson.getJSONObject("error").getString("message");
                throw new Exception("Invalid city: " + message);
            }

        }
        return weatherList;
    }


}
