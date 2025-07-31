package weather;

import weather.infrastructure.WeatherAPIAccess;
import weather.ui.JDatePickerExample;

public class main {
    public static void main(String[] args) throws Exception {
        JDatePickerExample datePickerExample = new JDatePickerExample();
        datePickerExample.launch();
        WeatherAPIAccess.getWeatherJson("Toronto", "2025-08-24");
    }
}
