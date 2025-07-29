package CountryInfo_p.use_case.country_info;

import CountryInfo_p.entity.CountryInfo;

public class CountryInfoInteractor implements CountryInfoInputBoundary {
    private final CountryInfoDataAccessInterface dataAccess;
    private final CountryInfoOutputBoundary presenter;

    public CountryInfoInteractor(CountryInfoDataAccessInterface dataAccess, CountryInfoOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(CountryInfoInputData inputData) {
        try {
            CountryInfo info = dataAccess.getCountryInfo(inputData.getCountryName());

            CountryInfoOutputData outputData = new CountryInfoOutputData(
                    info.getCountryName(),
                    info.getCapital(),
                    info.getCurrency(),
                    info.getLanguages()
            );

            presenter.present(outputData);

        } catch (Exception e) {
            presenter.presentError("Country not found or network error.");
        }
    }
}