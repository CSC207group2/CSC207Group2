package weather.infrastructure;

import org.json.JSONObject;

import java.util.ArrayList;

public class GetWeatherRange {
    private static final ArrayList<String> dateList = new ArrayList<>();

    public static ArrayList<JSONObject> returnWeatherList(ArrayList<String> dateList, String city) throws Exception {
        ArrayList<JSONObject> weatherList = new ArrayList<>();

        for (String date : dateList) {
            JSONObject weatherJson = new JSONObject(WeatherAPIAccess.getWeatherJson(city, date));
            weatherList.add(weatherJson);


        }
        return weatherList;
    }


}
