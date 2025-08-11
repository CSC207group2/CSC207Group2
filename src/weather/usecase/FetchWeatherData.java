package weather.usecase;

import org.json.JSONObject;
import weather.domain.WeatherDay;
import weather.domain.WeatherService;
import weather.infrastructure.DateCalculator;
import weather.infrastructure.GetWeatherRange;
import weather.infrastructure.WeatherJSONParser;

import java.util.ArrayList;

public class FetchWeatherData {

    private final WeatherService weatherService;

    public FetchWeatherData(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public ArrayList<WeatherDay> execute(String city, String startDate, String endDate) throws Exception {

        ArrayList<String> dates = DateCalculator.getDatesBetween(startDate, endDate);

        GetWeatherRange rangeFetcher = new GetWeatherRange(weatherService);
        ArrayList<JSONObject> jsonWeatherData = rangeFetcher.returnWeatherList(dates, city);


        return WeatherJSONParser.parseWeatherDays(jsonWeatherData);
    }
}
