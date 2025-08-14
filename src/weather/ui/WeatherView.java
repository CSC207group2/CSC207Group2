package weather.ui;

import core.HomePage;
import weather.domain.WeatherService;
import weather.domain.WeatherDay;
import weather.infrastructure.WeatherAPIAccess;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import weather.usecase.FetchWeatherData;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

/**
 * A Swing-based user interface for selecting a location and date range to view weather forecasts.
 * This class provides a form with location input, start and end date pickers,
 * and displays the retrieved weather data in a scrollable panel.
 */
public class WeatherView {

    private Date pastDate;
    private Date futureDate;
    private JPanel scrollPanel;


    /**
     * Launches the weather view interface for the given user.
     * Allows the user to:
     * Enter a location
     * Select start and end dates using date pickers
     * View weather forecasts for the chosen date range
     * Navigate back to the home page
     *
     * @param user the username of the logged-in user, used for returning to the home page
     */
    public void launch(String user) {

        JFrame frame = new JFrame("Weather");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 250);
        frame.setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);

        JTextField locationField = new JTextField(20);
        JLabel locationLabel = new JLabel("Location:");

        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        UtilDateModel model1 = new UtilDateModel();
        model1.setSelected(true);
        JDatePanelImpl panel1 = new JDatePanelImpl(model1, p);
        JDatePickerImpl picker1 = new JDatePickerImpl(panel1, new DateLabelFormatter());

        UtilDateModel model2 = new UtilDateModel();
        model2.setSelected(true);
        JDatePanelImpl panel2 = new JDatePanelImpl(model2, p);
        JDatePickerImpl picker2 = new JDatePickerImpl(panel2, new DateLabelFormatter());

        JButton confirmationButton = new JButton("Confirm Date");
        JButton backButton = new JButton("Back to Home");
        scrollPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JScrollPane scrollPane = new JScrollPane(scrollPanel);
        scrollPane.setPreferredSize(new Dimension(550, 100));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        picker1.addActionListener(e -> {
            pastDate = (Date) picker1.getModel().getValue();
            System.out.println("Start Date selected: " + pastDate);
        });

        picker2.addActionListener(e -> {
            futureDate = (Date) picker2.getModel().getValue();
            System.out.println("End Date selected: " + futureDate);
        });
        backButton.addActionListener(e -> {
            new HomePage(user).setVisible(true);
            frame.dispose();
            }
        );
        confirmationButton.addActionListener(e -> {
            try {
                String location = locationField.getText();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                if (pastDate == null || futureDate == null) {
                    JOptionPane.showMessageDialog(null, "Please select both start and end dates.");
                    return;
                }

                Date today = new Date();

                if (pastDate.after(futureDate)) {
                    JOptionPane.showMessageDialog(null, "Start date must be before or equal to end date.");
                    return;
                }

                if (pastDate.before(today)) {
                    JOptionPane.showMessageDialog(null, "Start date must be today or a future date.");
                    return;
                }

                WeatherService service = new WeatherAPIAccess();
                FetchWeatherData fetcher = new FetchWeatherData(service);
                ArrayList<WeatherDay> weatherDays = fetcher.execute(
                        location,
                        formatter.format(pastDate),
                        formatter.format(futureDate)
                );
                // rendering
                scrollPanel.removeAll();
                int x = 10;

                for (WeatherDay wd : weatherDays) {
                    JPanel weatherBox = new JPanel();
                    weatherBox.setLayout(new BoxLayout(weatherBox, BoxLayout.Y_AXIS));
                    weatherBox.setBounds(x, 10, 120, 60);
                    weatherBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                    weatherBox.add(new JLabel("Date: " + wd.getDate()));
                    weatherBox.add(new JLabel("High: " + wd.getMaxTemp() + "°C"));
                    weatherBox.add(new JLabel("Low: " + wd.getMinTemp() + "°C"));

                    scrollPanel.add(weatherBox);
                    x += 130;
                }

                scrollPanel.revalidate();
                scrollPanel.repaint();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // Add components to frame
        frame.add(locationLabel);
        frame.add(locationField);
        frame.add(new JLabel("Start Date:"));
        frame.add(picker1);
        frame.add(new JLabel("End Date:"));
        frame.add(picker2);
        frame.add(confirmationButton);
        frame.add(backButton);
        frame.add(scrollPane);

        frame.setVisible(true);
    }

}