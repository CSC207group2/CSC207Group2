package CountryInfo_p.data_access;

import CountryInfo_p.entity.CountryInfo;
import org.json.JSONArray;
import org.json.JSONObject;
import CountryInfo_p.use_case.country_info.CountryInfoDataAccessInterface;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class RESTCountriesAPI implements CountryInfoDataAccessInterface {

    @Override
    public CountryInfo getCountryInfo(String country) {
        try {
            JSONObject countryJson = getJsonObject(country);

            String countryName = countryJson.getJSONObject("name").getString("common");
            String capital = countryJson.getJSONArray("capital").getString(0);
            JSONObject currencies = countryJson.getJSONObject("currencies");
            String currencyCode = currencies.keys().next();

            JSONObject languagesJSON = countryJson.getJSONObject("languages");
            List<String> languages = new ArrayList<>(languagesJSON.toMap().values().stream()
                    .map(Object::toString)
                    .toList());

            return new CountryInfo(countryName, capital, currencyCode, languages);

        } catch (Exception e) {
            throw new RuntimeException("API call failed: " + e.getMessage());
        }
    }

    private static JSONObject getJsonObject(String country) throws IOException {
        String urlStr = "https://restcountries.com/v3.1/name/" + country;
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        InputStream response = connection.getInputStream();
        Scanner scanner = new Scanner(response);
        StringBuilder json = new StringBuilder();
        while (scanner.hasNext()) {
            json.append(scanner.nextLine());
        }

        JSONArray array = new JSONArray(json.toString());
        JSONObject countryJson = array.getJSONObject(0);
        return countryJson;
    }
}