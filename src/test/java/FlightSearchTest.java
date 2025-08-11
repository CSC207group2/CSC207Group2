import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import com.google.gson.JsonArray;
import Flights.data_access.FlightAPI;

import java.io.IOException;

public class FlightSearchTest {

    @Test
    void testValidSearchReturnsResults() throws Exception {
        FlightAPI api = new FlightAPI();
        JsonArray results = api.searchFlights("JFK", "LAX", "2025-08-20", "2025-08-25", 0);
        assertNotNull(results, "Results should not be null");
        assertFalse(results.isEmpty(), "Should return at least one flight");
    }

    @Test
    void testWrongAirportCode() throws Exception{
        FlightAPI api = new FlightAPI();
        JsonArray result = api.searchFlights("XXX", "YYY", "2025-08-20", "2025-08-25", 0);
        assertNull(result, "Should return 0 flights");
    }

    @Test
    void testInvalidDates() throws IOException {
        FlightAPI api = new FlightAPI();
        JsonArray result = api.searchFlights("XXX", "YYY", "2025-08-20", "2025-08-25", 0);
        assertNull(result, "Should return 0 flights");
    }
}
