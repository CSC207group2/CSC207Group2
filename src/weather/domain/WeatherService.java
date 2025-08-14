package weather.domain;

/**
 * Interface for a weather service that provides weather data in JSON format.
 */
public interface WeatherService {

    /**
     * Retrieves weather data for a given city and date.
     *
     * @param city the name of the city
     * @param date the target date in {@code yyyy-MM-dd} format
     * @return weather data as a JSON string
     * @throws Exception if the request fails or the data cannot be retrieved
     */
    String getWeatherJson(String city, String date) throws Exception;
}
