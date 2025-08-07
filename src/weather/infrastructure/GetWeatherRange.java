package weather.infrastructure;

import org.json.JSONObject;
import weather.domain.WeatherService;
import java.util.ArrayList;

public class GetWeatherRange {
    private final WeatherService weatherService;

    public GetWeatherRange(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public ArrayList<JSONObject> returnWeatherList(ArrayList<String> dateList, String city) throws Exception {
        ArrayList<JSONObject> weatherList = new ArrayList<>();
        for (String date : dateList) {
            JSONObject weatherJson = new JSONObject(weatherService.getWeatherJson(city, date));
            weatherList.add(weatherJson);


        }
        return weatherList;
    }


}
