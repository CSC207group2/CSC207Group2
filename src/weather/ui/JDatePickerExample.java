package weather.ui;

import weather.domain.WeatherService;
import weather.infrastructure.DateCalculator;
import weather.infrastructure.GetWeatherRange;
import weather.domain.WeatherDay;
import weather.infrastructure.WeatherAPIAccess;
import weather.infrastructure.WeatherJSONParser;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class JDatePickerExample {

    private Date pastDate;
    private Date futureDate;
    private JPanel scrollPanel;
    private JScrollPane scrollPane;

    public void launch() {

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Weather");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new FlowLayout());

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

        scrollPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        scrollPane = new JScrollPane(scrollPanel);
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

        confirmationButton.addActionListener(e -> {
            try {
                String location = locationField.getText();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                ArrayList<String> dateList = DateCalculator.getDatesBetween(
                        formatter.format(pastDate), formatter.format(futureDate)
                );

                WeatherService weatherService = new WeatherAPIAccess();
                GetWeatherRange rangeGetter = new GetWeatherRange(weatherService);
                ArrayList<JSONObject> rawJsonList = rangeGetter.returnWeatherList(dateList, location);
                ArrayList<WeatherDay> weatherDays = WeatherJSONParser.parseWeatherDays(rawJsonList);

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
        frame.add(scrollPane);

        frame.setVisible(true);
    }

}