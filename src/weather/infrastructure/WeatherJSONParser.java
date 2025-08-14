package weather.infrastructure;

import weather.domain.WeatherDay;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Parses JSON weather data into WeatherDay objects.
 */
public class WeatherJSONParser {

    /**
     * Converts a list of weather forecast JSON objects into a list of WeatherDay instances.
     * Extracts the date, maximum temperature, and minimum temperature from each forecast.
     *
     * @param jsonObjects list of weather forecast data as JSONObjects
     * @return list of parsed WeatherDay objects
     */
    public static ArrayList<WeatherDay> parseWeatherDays(ArrayList<JSONObject> jsonObjects) {
        ArrayList<WeatherDay> result = new ArrayList<>();

        for (JSONObject weatherForecast : jsonObjects) {
            JSONObject forecast = weatherForecast.getJSONObject("forecast");
            JSONArray forecastArray = forecast.getJSONArray("forecastday");

            Object obj = forecastArray.get(forecastArray.length() - 1);
                JSONObject dayObj = (JSONObject) obj;
                String date = dayObj.getString("date");
                JSONObject day = dayObj.getJSONObject("day");
                double max = day.getDouble("maxtemp_c");
                double min = day.getDouble("mintemp_c");

                result.add(new WeatherDay(date, max, min));
        }

        return result;
    }
}
