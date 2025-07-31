package CountryInfo_p.interface_adapter.country_info;

import CountryInfo_p.use_case.country_info.CountryInfoInputBoundary;
import CountryInfo_p.use_case.country_info.CountryInfoInputData;

public class CountryInfoController {
    private final CountryInfoInputBoundary interactor;

    public CountryInfoController(CountryInfoInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void fetchCountryInfo(String countryName) {
        if (countryName == null || countryName.trim().isEmpty()) {
            System.err.println("Please enter a valid country name.");
            return;
        }
        interactor.execute(new CountryInfoInputData(countryName.trim()));
    }
}