package weather.usecase;

import org.json.JSONObject;
import weather.domain.WeatherDay;
import weather.domain.WeatherService;
import weather.infrastructure.DateCalculator;
import weather.infrastructure.GetWeatherRange;
import weather.infrastructure.WeatherJSONParser;

import java.util.ArrayList;

/**
 * Use case class for fetching weather data for a city over a specified date range.
 * Coordinates between the date calculation, API data retrieval, and JSON parsing
 * to return structured weather information.
 */
public class FetchWeatherData {

    private final WeatherService weatherService;

    /**
     * Creates a new fetcher using the specified weather service.
     *
     * @param weatherService the service used to retrieve weather data
     */
    public FetchWeatherData(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Fetches and parses weather data for the given city and date range.
     *
     * @param city      the name of the city
     * @param startDate start date in {@code yyyy-MM-dd} format
     * @param endDate   end date in {@code yyyy-MM-dd} format
     * @return list of WeatherDay objects representing weather forecasts
     * @throws Exception if an error occurs during data retrieval or parsing
     */
    public ArrayList<WeatherDay> execute(String city, String startDate, String endDate) throws Exception {

        ArrayList<String> dates = DateCalculator.getDatesBetween(startDate, endDate);

        GetWeatherRange rangeFetcher = new GetWeatherRange(weatherService);
        ArrayList<JSONObject> jsonWeatherData = rangeFetcher.returnWeatherList(dates, city);


        return WeatherJSONParser.parseWeatherDays(jsonWeatherData);
    }
}
