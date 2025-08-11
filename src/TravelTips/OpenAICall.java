package TravelTips;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class OpenAICall {

    // Your Node proxy (change to your deployed URL later)
    private static final String PROXY_URL = "http://localhost:3000/api/chat";
    // Must match BACKEND_AUTH_TOKEN in your Node .env
    private static final String PROXY_TOKEN = "dheiainw";

    private static final MediaType JSON = MediaType.parse("application/json");
    private static final OkHttpClient HTTP = new OkHttpClient();

    public static String getTravelInsights(String airportOrCity) {
        try {
            String prompt = "Write me a 100 word summary of common scams, places to avoid, and common tourist attractions in the following airport or city. If input its not a city or an airport name, then reply with Incorrect Input, Please submit a Airport or a City "
                    + airportOrCity + ".";

            // The proxy expects { "messages": [...] }
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
                    throw new RuntimeException("HTTP " + res.code() + ": " + err);
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
