package core;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Writes user-specific logs to Firebase Realtime Database via REST.
 * Paths used:
 *  - users/{username}/savedFlights/{flightId}
 *  - users/{username}/savedCountries/{countryName}
 *
 * Notes:
 *  - We use PUT to write to an exact key (create/replace).
 *  - Keys are sanitized with urlKey(...) to avoid Firebase key errors.
 *  - JSON string values are escaped with esc(...).
 *  - We print URL/BODY and error body on failure to debug quickly.
 */
public class FirebaseLogs {

    // Our database root (no trailing slash)
    private static final String DB_ROOT =
            "https://travelplannerauth-e78d0-default-rtdb.firebaseio.com";

    /**
     * Save a single flight under users/{username}/savedFlights/{flightId}.
     * Uses PUT so the entry is replaced/created at that exact key.
     */
    public static void saveFlight(String username,
                                  String flightId,
                                  String depAirport,
                                  String arrAirport,
                                  String depTime,
                                  String arrTime) {

        if (username == null || username.isBlank()) {
            System.out.println("❌ saveFlight called with empty username. Aborting.");
            return;
        }

        try {
            // 1) Make keys safe for Firebase paths
            String safeUser = urlKey(username);
            String safeId   = urlKey(flightId);

            // 2) Build the exact node we’re writing to
            String path = String.format("%s/data/%s/savedFlights/%s.json",
                    DB_ROOT, safeUser, safeId);

            // 3) Minimal JSON payload for this flight
            String json = String.format("""
            {
              "departureAirport":"%s",
              "arrivalAirport":"%s",
              "departureTime":"%s",
              "arrivalTime":"%s"
            }
            """, esc(depAirport), esc(arrAirport), esc(depTime), esc(arrTime));

            // 4) PUT the JSON to Firebase with proper headers
            HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            // For Debugging
            System.out.println("PUT → " + path);
            System.out.println("BODY → " + json);

            int code = conn.getResponseCode();
            if (code == 200 || code == 204) {
                System.out.println("✅ Flight saved.");
            } else {
                System.out.println("❌ Flight save failed. HTTP " + code);
                try (var es = conn.getErrorStream()) {
                    if (es != null) {
                        System.out.println("ERROR BODY → " +
                                new String(es.readAllBytes(), StandardCharsets.UTF_8));
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Save country info under users/{username}/savedCountries/{countryName}.
     * Example fields might be capital, population, region, etc.
     */
    public static void saveCountry(String username,
                                   String countryName,
                                   java.util.Map<String, String> fields) {
        try {
            String safeUser = urlKey(username);
            String safeCountry = urlKey(countryName);

            String path = String.format("%s/data/%s/savedCountries/%s.json",
                    DB_ROOT, safeUser, safeCountry);

            // Build a tiny JSON object from the map (escape all values)
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (var e : fields.entrySet()) {
                if (!first) sb.append(',');
                sb.append('"').append(esc(e.getKey())).append('"')
                        .append(':')
                        .append('"').append(esc(e.getValue())).append('"');
                first = false;
            }
            sb.append('}');
            String json = sb.toString();

            HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            // Debug: show exactly what we sent
            System.out.println("PUT → " + path);
            System.out.println("BODY → " + json);

            int code = conn.getResponseCode();
            if (code == 200 || code == 204) {
                System.out.println("✅ Country saved.");
            } else {
                System.out.println("❌ Country save failed. HTTP " + code);
                try (var es = conn.getErrorStream()) {
                    if (es != null) {
                        System.out.println("ERROR BODY → " +
                                new String(es.readAllBytes(), StandardCharsets.UTF_8));
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // === helpers ===

    /**
     * Escape for JSON string values (quotes, backslashes, line breaks).
     */
    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    /**
     * Firebase keys can't contain /.#$[] and shouldn't include spaces or odd symbols.
     * Keep only [A-Za-z0-9_-]; replace everything else with underscore.
     */
    private static String urlKey(String s) {
        if (s == null) return "";
        return s.trim().replaceAll("[^A-Za-z0-9_-]", "_");
    }
}
