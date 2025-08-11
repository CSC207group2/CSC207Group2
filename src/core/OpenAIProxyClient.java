package core;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;

public class OpenAIProxyClient {
    private final String proxyUrl;
    private final String authToken;
    private final OkHttpClient http = new OkHttpClient();

    /**
     * Constructor for the proxy client
     * @param proxyUrl The base URL of your Node.js proxy (e.g., "http://localhost:3000")
     * @param authToken The BACKEND_AUTH_TOKEN from your .env file in the Node.js server
     */
    public OpenAIProxyClient(String proxyUrl, String authToken) {
        this.proxyUrl = proxyUrl;
        this.authToken = authToken;
    }

    /**
     * Sends a chat message to the OpenAI API through your proxy
     * @param userMessage The text prompt to send
     * @return The model's reply as a String
     * @throws IOException if there is a network or server error
     */
    public String chat(String userMessage) throws IOException {
        JSONObject body = new JSONObject();
        JSONArray messages = new JSONArray()
                .put(new JSONObject().put("role", "user").put("content", userMessage));
        body.put("messages", messages);

        Request request = new Request.Builder()
                .url(proxyUrl + "/api/chat")
                .addHeader("Authorization", "Bearer " + authToken)
                .post(RequestBody.create(body.toString(), MediaType.parse("application/json")))
                .build();

        try (Response res = http.newCall(request).execute()) {
            if (!res.isSuccessful()) {
                String err = res.body() != null ? res.body().string() : "";
                throw new IOException("HTTP " + res.code() + ": " + err);
            }
            JSONObject json = new JSONObject(res.body().string());
            return json.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");
        }
    }

    // Quick standalone test
    public static void main(String[] args) throws Exception {
        OpenAIProxyClient client = new OpenAIProxyClient("http://localhost:3000", "dheiainw");
        String reply = client.chat("Say hello in one short sentence.");
        System.out.println("AI says: " + reply);
    }
}
