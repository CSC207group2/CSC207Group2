package weather.infrastructure;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Utility class for performing date range calculations.
 * <p>
 * This class provides methods to generate lists of dates between
 * a given start and end date. Dates are handled using
 * java.time.
 */
public class DateCalculator {

    /**
     * Returns all dates between the given start and end dates, inclusive.
     * Dates must be in {@code yyyy-MM-dd} format.
     *
     * @param startDate start date in {@code yyyy-MM-dd} format
     * @param endDate   end date in {@code yyyy-MM-dd} format
     * @return list of dates from start to end, inclusive
     */
    public static ArrayList<String> getDatesBetween(String startDate, String endDate) {
        ArrayList<String> dates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        while (!start.isAfter(end)) {
            dates.add(start.format(formatter));
            start = start.plusDays(1);
        }

        return dates;
    }
}
