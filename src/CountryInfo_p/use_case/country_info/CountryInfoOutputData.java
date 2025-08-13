package CountryInfo_p.use_case.country_info;

import java.util.Collection;
import java.util.List;

public class CountryInfoOutputData {
    public final String country;
    public final String capital;
    public final String currency;
    public final List<String> languages;

    public CountryInfoOutputData(String country, String capital, String currency, List<String> languages) {
        this.country = country;
        this.capital = capital;
        this.currency = currency;
        this.languages = languages;
    }

    public String getCountry() {
        return country;
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