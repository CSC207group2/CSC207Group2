// AllTestsExtended.java
// Combined JUnit 5 tests for updated weather package
// SAFE SECTION = No UI popups or network calls
// UNSAFE SECTION = May launch UI or attempt HTTP calls; disabled by default

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import weather.domain.WeatherDay;
import weather.domain.WeatherService;
import weather.infrastructure.DateCalculator;
import weather.infrastructure.GetWeatherRange;
import weather.infrastructure.WeatherAPIAccess;
import weather.infrastructure.WeatherJSONParser;
import weather.ui.DateLabelFormatter;
import weather.usecase.FetchWeatherData;

public class WeatherTests {

    // ======== SAFE TESTS ========

    @Test
    public void testWeatherDay_getters() {
        WeatherDay wd = new WeatherDay("2025-01-01", 25.0, 15.0);
        assertEquals("2025-01-01", wd.getDate());
        assertEquals(25.0, wd.getMaxTemp(), 1e-9);
        assertEquals(15.0, wd.getMinTemp(), 1e-9);
    }

    @Test
    public void testDateCalculator_getDatesBetween() {
        ArrayList<String> dates = DateCalculator.getDatesBetween("2025-01-01", "2025-01-03");
        assertEquals(3, dates.size());
        assertEquals("2025-01-01", dates.get(0));
        assertEquals("2025-01-02", dates.get(1));
        assertEquals("2025-01-03", dates.get(2));
    }

    @Test
    public void testGetWeatherRange_normalAndError() throws Exception {
        WeatherService okService = (city, date) -> "{}";
        GetWeatherRange getter = new GetWeatherRange(okService);
        ArrayList<String> dates = new ArrayList<>();
        dates.add("2025-01-01");
        assertEquals(1, getter.returnWeatherList(dates, "City").size());

        WeatherService badService = (city, date) -> "{\"error\":{\"message\":\"Invalid\"}}";
        GetWeatherRange badGetter = new GetWeatherRange(badService);

        boolean threw = false;
        try {
            badGetter.returnWeatherList(dates, "City");
        } catch (Exception ex) {
            threw = ex.getMessage().contains("Invalid city");
        }
        assertEquals(true, threw);
    }

    @Test
    public void testWeatherJSONParser_structure() {
        JSONObject day = new JSONObject().put("maxtemp_c", 10.0).put("mintemp_c", 5.0);
        JSONObject dayObj = new JSONObject().put("date", "2025-01-01").put("day", day);
        JSONArray arr = new JSONArray().put(dayObj);
        JSONObject forecast = new JSONObject().put("forecastday", arr);
        JSONObject root = new JSONObject().put("forecast", forecast);

        ArrayList<JSONObject> list = new ArrayList<>();
        list.add(root);

        ArrayList<WeatherDay> parsed = WeatherJSONParser.parseWeatherDays(list);
        assertEquals(1, parsed.size());
        assertEquals("2025-01-01", parsed.get(0).getDate());
    }

    @Test
    public void testDateLabelFormatter_roundtrip() throws Exception {
        DateLabelFormatter fmt = new DateLabelFormatter();
        Object parsed = fmt.stringToValue("2025-07-04");
        assertEquals(true, parsed != null);

        Calendar cal = Calendar.getInstance();
        cal.set(2025, Calendar.JULY, 4, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        assertEquals("2025-07-04", fmt.valueToString(cal));
        assertEquals("", fmt.valueToString(null));
    }

    @Test
    public void testFetchWeatherData_usecase() throws Exception {
        // Stub WeatherService for predictable data
        WeatherService stubService = (city, date) -> {
            JSONObject day = new JSONObject().put("maxtemp_c", 20.0).put("mintemp_c", 10.0);
            JSONObject dayObj = new JSONObject().put("date", date).put("day", day);
            JSONArray arr = new JSONArray().put(dayObj);
            JSONObject forecast = new JSONObject().put("forecastday", arr);
            JSONObject root = new JSONObject().put("forecast", forecast);
            return root.toString();
        };
        FetchWeatherData usecase = new FetchWeatherData(stubService);
        ArrayList<WeatherDay> days = usecase.execute("City", "2025-01-01", "2025-01-02");
        assertEquals(2, days.size());
        assertEquals(20.0, days.get(0).getMaxTemp(), 1e-9);
    }

}
