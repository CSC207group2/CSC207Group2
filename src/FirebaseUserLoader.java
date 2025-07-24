import java.io.*;
import java.net.*;
import java.util.*;

public class FirebaseUserLoader {
    private static final String DATABASE_URL = "https://your-project-id.firebaseio.com/users.json";

    public static HashMap<String, String> loadUsers() {
        HashMap<String, String> users = new HashMap<>();
        try {
            URL url = new URL(DATABASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder json = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();

            String jsonString = json.toString().replaceAll("[{}\"]", "");
            String[] entries = jsonString.split(",");
            for (String entry : entries) {
                String[] pair = entry.split(":");
                if (pair.length == 2) {
                    users.put(pair[0].trim(), pair[1].trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
}
