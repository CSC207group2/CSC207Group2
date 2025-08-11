package logs.interface_adapter;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;

public class FirebaseLogin {
    private static final String DATABASE_URL = "https://travelplannerauth-e78d0-default-rtdb.firebaseio.com/users.json";

    public static HashMap<String, String> loadUsers() {
        HashMap<String, String> users = new HashMap<>();
        try {
            URL url = new URL(DATABASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder json = new StringBuilder();
            String inputLine;
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

    public static boolean login(String username, String password) {
        HashMap<String, String> users = loadUsers();
        String hashed = hashPassword(password);
        return users.containsKey(username) && users.get(username).equals(hashed);
    }

    public static void addUser(String username, String password) {
        try {
            String hashed = hashPassword(password);
            URL url = new URL("https://travelplannerauth-e78d0-default-rtdb.firebaseio.com/users/" + username + ".json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);

            String jsonInputString = "\"" + hashed + "\"";
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                System.out.println("✅ User added successfully.");
            } else {
                System.out.println("❌ Failed to add user. Response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
