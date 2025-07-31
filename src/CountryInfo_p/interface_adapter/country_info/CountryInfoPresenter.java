package CountryInfo_p.interface_adapter.country_info;

import CountryInfo_p.use_case.country_info.CountryInfoOutputBoundary;
import CountryInfo_p.use_case.country_info.CountryInfoOutputData;
import CountryInfo_p.view.CountryInfoView;

public class CountryInfoPresenter implements CountryInfoOutputBoundary {
    private final CountryInfoView view;

    public CountryInfoPresenter(CountryInfoView view) {
        this.view = view;
    }

    @Override
    public void present(CountryInfoOutputData outputData) {
        view.present(outputData);
    }

    @Override
    public void presentError(String errorMessage) {
        view.presentError(errorMessage);
    }
}