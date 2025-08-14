package weather;

import weather.ui.WeatherView;

/**
 * Main method used purely for debugging the Weather portion of the application
 */
public class main {
    public static void main(String[] args) throws Exception {
        WeatherView datePickerExample = new WeatherView();
        datePickerExample.launch("debug");
    }
}
