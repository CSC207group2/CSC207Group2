package weather.domain;

public class SimpleDayForecast {
    private final String date;
    private final double maxTempC;
    private final double minTempC;
    private final String condition;

    public SimpleDayForecast(String date, double maxTempC, double minTempC, String condition) {
        this.date = date;
        this.maxTempC = maxTempC;
        this.minTempC = minTempC;
        this.condition = condition;
    }

    public String getDate(){return date;}
    public double getMaxTempC() {return maxTempC;}
    public double getMinTempC() {return minTempC;}
    public String getCondition() {return condition;}

}
