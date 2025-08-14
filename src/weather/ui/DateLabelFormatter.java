package weather.ui;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Formats and parses dates in yyyy-mm-dd format for use in Swing date inputs.
 */
public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
    private final String datePattern = "yyyy-MM-dd";
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

    /**
     * Parses a date string in {@code yyyy-MM-dd} format into a {@link java.util.Date} object.
     *
     * @param text date string to parse
     * @return parsed date object
     */
    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parse(text);
    }

    /**
     * Formats a calendar value into a string in yyyy-mm-dd format.
     *
     * @param value date value to format
     * @return formatted date string
     */
    @Override
    public String valueToString(Object value) {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }

        return "";
    }
}
