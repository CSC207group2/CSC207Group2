package TravelTips;

import javax.net.ssl.HttpsURLConnection;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class OpenAICall {

    private  static  final String API_KEY = "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";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public static String getTravelInsights(String airport_or_city) {
        try
        {
            String prompt = "Write me a 100 word summary of common scams, places to avoid, and common tourist attractions in the following airport or city: " + airport_or_city + ".";
            String payload = "{\n" +
                    "  \"model\": \"gpt-4o-mini\",\n" +
                    "  \"messages\": [\n" +
                    "    {\"role\": \"user\", \"content\": \"" + prompt + "\"}\n" +
                    "  ]\n" +
                    "}";
            String response = sendPostRequest(API_URL,payload);

            return OpenAICall.extractContent(response);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return "Error: Ran out of tokens";
    }

    private static String sendPostRequest(String apiUrl, String payload) throws  Exception{
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        connection.setRequestProperty("Authorization","Bearer "+API_KEY);
        connection.setRequestProperty("Content-Type","application/json");

        try(OutputStream os = connection.getOutputStream()){
            os.write((payload.getBytes()));
            os.flush();
        }

        StringBuilder response = new StringBuilder();
        try(Scanner scanner = new Scanner(connection.getInputStream())) {
            while ((scanner.hasNext())){
                response.append((scanner.nextLine()));
            }
        }

        return  response.toString();
    }

    private static String extractContent(String input) {
        String key = "\"content\":";
        int startIndex = input.indexOf(key);
        if (startIndex == -1) {
            return null; // content field not found
        }

        // Move to the first quote after "content":
        startIndex = input.indexOf("\"", startIndex + key.length());
        if (startIndex == -1) {
            return null;
        }

        // Find the ending quote of the content
        int endIndex = input.indexOf("\"", startIndex + 1);
        StringBuilder content = new StringBuilder();

        while (endIndex != -1) {
            // Check if the quote is escaped
            if (input.charAt(endIndex - 1) != '\\') {
                // Unescaped quote found -> end of content
                break;
            }
            // Escaped quote, continue searching
            endIndex = input.indexOf("\"", endIndex + 1);
        }

        if (endIndex == -1) {
            return null; // No closing quote found
        }

        // Extract the raw content string
        String rawContent = input.substring(startIndex + 1, endIndex);

        // Unescape any escaped characters like \" or \\n
        return rawContent.replace("\\n", "\n").replace("\\\"", "\"").replace("\\\\", "\\");
    }

}


