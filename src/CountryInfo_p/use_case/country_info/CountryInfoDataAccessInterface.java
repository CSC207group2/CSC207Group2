package CountryInfo_p.use_case.country_info;

import CountryInfo_p.entity.CountryInfo;

public interface CountryInfoDataAccessInterface {
    CountryInfo getCountryInfo(String countryName);
}
