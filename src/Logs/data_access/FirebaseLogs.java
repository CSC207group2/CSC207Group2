package Logs.data_access;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * REST helper for writing user logs to Firebase Realtime Database.
 * Layout:
 *   /data/{user}/savedFlights/{flightId}
 *   /data/{user}/savedCountries/{country}
 */
public class FirebaseLogs {

    /** Firebase DB root. */
    public static final String DB_ROOT =
            "https://travelplannerauth-e78d0-default-rtdb.firebaseio.com";

    /**
     * Save one flight at /data/{user}/savedFlights/{flightId}.
     * (Strings are escaped; no trailing commas in JSON.)
     */
    public static void saveFlight(String username,
                                  String flightId,
                                  String depAirport,
                                  String arrAirport,
                                  String depTime,
                                  String arrTime) {
        if (username == null || username.isBlank() ||
                flightId == null || flightId.isBlank()) return;

        String safeUser = urlKey(username);
        String safeId   = urlKey(flightId);
        String path = DB_ROOT + "/data/" + safeUser + "/savedFlights/" + safeId + ".json";

        // Important: no trailing comma after the last field
        String json = String.format("""
        {
          "departureAirport":"%s",
          "arrivalAirport":"%s",
          "departureTime":"%s",
          "arrivalTime":"%s"
        }
        """, esc(depAirport), esc(arrAirport), esc(depTime), esc(arrTime));

        putJson(path, json);
    }

    /**
     * Save a country at /data/{user}/savedCountries/{country}.
     * Builds a compact JSON object from a simple Map<String,String>.
     */
    public static void saveCountry(String username,
                                   String countryName,
                                   Map<String, String> fields) {
        if (username == null || username.isBlank() ||
                countryName == null || countryName.isBlank()) return;

        String safeUser    = urlKey(username);
        String safeCountry = urlKey(countryName);
        String path = DB_ROOT + "/data/" + safeUser + "/savedCountries/" + safeCountry + ".json";

        // Build {"k1":"v1","k2":"v2"} with correct commas and a closing brace
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

        putJson(path, json);
    }


    // ───────────── helpers ─────────────

    /** PUT the given JSON to a Firebase REST endpoint (.json). */
    private static void putJson(String path, String json) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int code = conn.getResponseCode();
            if (code < 200 || code >= 300) {
                try (var es = conn.getErrorStream()) {
                    String msg = (es != null) ? new String(es.readAllBytes(), StandardCharsets.UTF_8) : "";
                    System.err.println("Firebase PUT failed (" + code + "): " + msg);
                }
            }
        } catch (Exception ex) {
            System.err.println("Firebase PUT error: " + ex.getMessage());
        }
    }

    /** Escape JSON string content (quotes, backslashes, line breaks). */
    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    /**
     * Make a safe Firebase key from arbitrary text.
     * Firebase disallows /.#$[]; we also normalize spaces/symbols to underscore.
     */
    private static String urlKey(String s) {
        if (s == null) return "";
        return s.trim().replaceAll("[^A-Za-z0-9_-]", "_");
    }
}
