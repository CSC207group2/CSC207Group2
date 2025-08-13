package Logs.interface_adapter;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Logs.data_access.FirebaseLogs;
import org.json.JSONObject;

/**
 * RecentLogsWindow
 *
 * Purpose:
 *  - Fetch a user's saved flights & countries from Firebase RTDB (via REST)
 *  - Render them in a human-friendly text view (no braces/JSON)
 *
 * Data shape (written by FirebaseLogs):
 *   /data/{user}/savedFlights/{flightId}    -> { departureAirport, arrivalAirport, departureTime, arrivalTime, [savedAt] }
 *   /data/{user}/savedCountries/{country}  -> { capital, currency, languages, [savedAt] }
 *
 * Notes:
 *  - Uses SwingWorker to keep UI responsive.
 *  - Uses org.json for lightweight parsing (JSONObject).
 *  - Shows messages for empty data or transient HTTP issues.
 */
public class RecentLogsWindow extends JFrame {

    /** Single text area used for the whole window (no shadowed locals). */
    private final JTextArea recentLogs = new JTextArea();

    /** Reasonable network timeouts (ms) for REST calls. */
    private static final int CONNECT_TIMEOUT_MS = 5000;
    private static final int READ_TIMEOUT_MS    = 7000;

    public RecentLogsWindow(String username) {
        super("My Recent Logs");

        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Basic typography; monospaced is nice for alignment, sans-serif reads cleaner for most users—pick one.
        recentLogs.setEditable(false);
        recentLogs.setFont(new Font("SansSerif", Font.PLAIN, 14));
        recentLogs.setText("Loading…");

        add(new JScrollPane(recentLogs), BorderLayout.CENTER);

        // Kick off background fetch
        fetchLogsViaRest(username);

        setVisible(true);
    }

    /**
     * Fetches user's logs from: {DB_ROOT}/data/{safeUser}.json
     * Rest result is parsed and formatted for display.
     */
    private void fetchLogsViaRest(String username) {
        final String safeUser = urlKey(username);
        final String url = FirebaseLogs.DB_ROOT + "/data/" + safeUser + ".json";

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() {
                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                    // Set method + headers expected by Firebase REST
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");
                    // Timeouts so the UI doesn't sit forever on bad networks
                    conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
                    conn.setReadTimeout(READ_TIMEOUT_MS);

                    int code = conn.getResponseCode();
                    InputStream in = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
                    String body = (in != null) ? new String(in.readAllBytes(), StandardCharsets.UTF_8) : "";

                    if (code != 200) {
                        // Keep this message simple & actionable for end users
                        return "Couldn’t load your logs right now (HTTP " + code + "). Please try again.";
                    }
                    if (body == null || body.isBlank() || "null".equalsIgnoreCase(body.trim())) {
                        return "No logs yet. Try saving a flight or a country from the app.";
                    }

                    return formatLogs(body);

                } catch (Exception e) {
                    // Network/parse errors show a friendly line, not a stack trace
                    return "Couldn’t load your logs. Please check your connection and try again.";
                }
            }

            @Override
            protected void done() {
                try {
                    recentLogs.setText(get());
                } catch (Exception e) {
                    recentLogs.setText("Something went wrong showing your logs. Please try again.");
                }
            }
        }.execute();
    }

    /**
     * Convert the raw JSON into readable sections.
     */
    private static String formatLogs(String json) {
        try {
            JSONObject root = new JSONObject(json);
            StringBuilder sb = new StringBuilder();

            // ── Countries section ──────────────────────────────────────────────
            if (root.has("savedCountries")) {
                sb.append("🌍 Saved Countries\n");
                sb.append("────────────────────\n");
                JSONObject countries = root.getJSONObject("savedCountries");

                List<String> names = new ArrayList<>(countries.keySet());
                Collections.sort(names, String::compareToIgnoreCase);

                for (String country : names) {
                    JSONObject info = countries.optJSONObject(country);
                    if (info == null) continue;

                    sb.append("• ").append(country).append("\n");
                    addIfPresent(sb, "    Capital",   info.optString("capital",   null));
                    addIfPresent(sb, "    Currency",  info.optString("currency",  null));
                    addIfPresent(sb, "    Languages", info.optString("languages", null));
                    sb.append("\n");
                }
                sb.append("\n");
            }

            // ── Flights section ────────────────────────────────────────────────
            if (root.has("savedFlights")) {
                sb.append("✈ Saved Flights\n");
                sb.append("────────────────────\n");
                JSONObject flights = root.getJSONObject("savedFlights");

                List<String> codes = new ArrayList<>(flights.keySet());
                Collections.sort(codes, String::compareToIgnoreCase);

                for (String code : codes) {
                    JSONObject info = flights.optJSONObject(code);
                    if (info == null) continue;

                    String depAirport = info.optString("departureAirport", "N/A");
                    String depTime    = info.optString("departureTime",    "N/A");
                    String arrAirport = info.optString("arrivalAirport",   "N/A");
                    String arrTime    = info.optString("arrivalTime",      "N/A");

                    sb.append("• ").append(code).append("\n");
                    sb.append("    From: ").append(depAirport).append(" (").append(depTime).append(")\n");
                    sb.append("    To:   ").append(arrAirport).append(" (").append(arrTime).append(")\n");
                    sb.append("\n");
                }
            }

            if (sb.length() == 0) {
                return "No logs yet. Try saving a flight or a country from the app.";
            }
            return sb.toString();

        } catch (Exception e) {
            // If parsing fails, fall back to a helpful message
            return "Your logs were found but couldn’t be parsed. Please try again.";
        }
    }

    /** Append a labeled line only when a value exists and is non-blank. */
    private static void addIfPresent(StringBuilder sb, String label, String value) {
        if (value != null && !value.isBlank() && !"null".equalsIgnoreCase(value.trim())) {
            sb.append(label).append(": ").append(value).append("\n");
        }
    }

    /**
     * Make a safe Firebase key from arbitrary text.
     * Firebase keys can’t contain /.#$[]; we also normalize spaces/symbols to underscore.
     */
    private static String urlKey(String s) {
        if (s == null) return "";
        return s.trim().replaceAll("[^A-Za-z0-9_-]", "_");
    }
}
