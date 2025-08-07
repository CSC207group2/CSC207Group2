package weather.domain;

public interface WeatherService {
    String getWeatherJson(String city, String date) throws Exception;
}
