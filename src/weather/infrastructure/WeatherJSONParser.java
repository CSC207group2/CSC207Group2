package weather.infrastructure;

import weather.domain.WeatherDay;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherJSONParser {

    public static ArrayList<WeatherDay> parseWeatherDays(ArrayList<JSONObject> jsonObjects) {
        ArrayList<WeatherDay> result = new ArrayList<>();

        for (JSONObject weatherForecast : jsonObjects) {
            JSONObject forecast = weatherForecast.getJSONObject("forecast");
            JSONArray forecastArray = forecast.getJSONArray("forecastday");

            for (Object obj : forecastArray) {
                JSONObject dayObj = (JSONObject) obj;
                String date = dayObj.getString("date");
                JSONObject day = dayObj.getJSONObject("day");
                double max = day.getDouble("maxtemp_c");
                double min = day.getDouble("mintemp_c");

                result.add(new WeatherDay(date, max, min));
            }
        }

        return result;
    }
}
