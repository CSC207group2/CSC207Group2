package CountryInfo_p.use_case.country_info;

public interface CountryInfoOutputBoundary {
    void present(CountryInfoOutputData outputData);
    void presentError(String errorMessage);
}