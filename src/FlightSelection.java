import javax.swing.*;
import java.util.List;
import java.util.Map;

public class FlightSelection extends JFrame {
    static JTextArea flightInfoArea = new JTextArea(20, 50);
    public static void selectedFlight(Map<String, List<String>> departure_data, Map<String, List<String>> arrival_info,
                                      String flight) {
        StringBuilder sb = new StringBuilder();

        for (String element: departure_data.get(flight)){
            sb.append("Leaving From:").append(element).append("\n");
        }
    }
}

