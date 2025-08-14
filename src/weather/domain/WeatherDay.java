package weather.domain;

/**
 * Represents the weather data for a single day, including the date,
 * maximum temperature, and minimum temperature.
 */

public class WeatherDay {
    private final String date;
    private final double maxTemp;
    private final double minTemp;

    /**
     * Creates a new WeatherDay instance.
     *
     * @param date    the date in {@code yyyy-MM-dd} format
     * @param maxTemp the maximum temperature for the day in Celsius
     * @param minTemp the minimum temperature for the day in Celsius
     */
    public WeatherDay(String date, double maxTemp, double minTemp) {
        this.date = date;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
    }

    /**
     * Returns the date of this weather data.
     *
     * @return the date as a string in {@code yyyy-MM-dd} format
     */
    public String getDate() { return date; }

    /**
     * Returns the maximum temperature for this day.
     *
     * @return maximum temperature in Celsius
     */
    public double getMaxTemp() { return maxTemp; }

    /**
     * Returns the minimum temperature for this day.
     *
     * @return minimum temperature in Celsius
     */
    public double getMinTemp() { return minTemp; }
}
