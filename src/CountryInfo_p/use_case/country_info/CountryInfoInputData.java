package CountryInfo_p.use_case.country_info;

public class CountryInfoInputData {
    private final String countryName;

    public CountryInfoInputData(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryName() {
        return countryName;
    }
}