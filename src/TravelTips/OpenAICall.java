package TravelTips;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.time.Duration;

public class OpenAICall {

    // Deployed proxy URL (Render)
    private static final String PROXY_URL = "https://openai-proxy-3toa.onrender.com/api/chat";
    // Must match BACKEND_AUTH_TOKEN set in Render (consider rotating to a longer token)
    private static final String PROXY_TOKEN = "dheiainw";

    private static final MediaType JSON = MediaType.parse("application/json");

    // Small timeouts so the UI doesn’t hang forever on network issues
    private static final OkHttpClient HTTP = new OkHttpClient.Builder()
            .callTimeout(Duration.ofSeconds(45))
            .connectTimeout(Duration.ofSeconds(20))
            .readTimeout(Duration.ofSeconds(40))
            .writeTimeout(Duration.ofSeconds(40))
            .build();

    public static String getTravelInsights(String airportOrCity) {
        try {
            String prompt =
                    "Write me a 100 word summary of common scams, places to avoid, and common tourist attractions "
                            + "in the following airport or city. If input is not a city or an airport name, then reply with "
                            + "\"Incorrect Input, Please submit an Airport or a City\": " + airportOrCity + ".";

            JSONObject body = new JSONObject();
            body.put("messages", new JSONArray()
                    .put(new JSONObject().put("role", "user").put("content", prompt)));

            Request request = new Request.Builder()
                    .url(PROXY_URL)
                    .addHeader("Authorization", "Bearer " + PROXY_TOKEN)
                    .post(RequestBody.create(body.toString(), JSON))
                    .build();

            try (Response res = HTTP.newCall(request).execute()) {
                if (!res.isSuccessful()) {
                    String err = res.body() != null ? res.body().string() : "";
                    throw new IOException("HTTP " + res.code() + ": " + err);
                }
                String resp = res.body().string();
                JSONObject json = new JSONObject(resp);
                return json.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
