package weather.domain;


public class WeatherDay {
    private final String date;
    private final double maxTemp;
    private final double minTemp;

    public WeatherDay(String date, double maxTemp, double minTemp) {
        this.date = date;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
    }

    public String getDate() { return date; }
    public double getMaxTemp() { return maxTemp; }
    public double getMinTemp() { return minTemp; }
}
