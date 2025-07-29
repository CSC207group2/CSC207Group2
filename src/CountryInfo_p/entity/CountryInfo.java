package CountryInfo_p.entity;

import java.util.List;

public class CountryInfo {
    private final String countryName;
    private final String capital;
    private final String currency;
    private final List<String> languages;

    public CountryInfo(String countryName, String capital, String currency, List<String> languages) {
        this.countryName = countryName;
        this.capital = capital;
        this.currency = currency;
        this.languages = languages;
    }

    public String getCountryName() {
        return countryName;
    }
    public String getCapital() {
        return capital;
    }
    public String getCurrency() {
        return currency;
    }
    public List<String> getLanguages() {
        return languages;
    }
}