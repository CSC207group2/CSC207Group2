package weather.infrastructure;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DateCalculator {
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
