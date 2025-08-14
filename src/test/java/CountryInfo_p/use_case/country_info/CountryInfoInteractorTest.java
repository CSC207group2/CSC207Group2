package CountryInfo_p.use_case.country_info;

import CountryInfo_p.data_access.RESTCountriesAPI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountryInfoInteractorTest {

    @Test
    void TestCountrySuccess() {
        CountryInfoInputData inputData = new CountryInfoInputData("Canada");
        CountryInfoDataAccessInterface repo = new RESTCountriesAPI();

        CountryInfoOutputBoundary successPresenter = new CountryInfoOutputBoundary() {
            @Override
            public void present(CountryInfoOutputData out) {
                assertEquals("Canada", out.getCountry());
                assertEquals("Ottawa", out.getCapital());
                assertEquals("CAD", out.getCurrency());
                assertNotNull(out.getLanguages());
                assertFalse(out.getLanguages().isEmpty());
                assertTrue(out.getLanguages().stream()
                        .anyMatch(s -> s.equalsIgnoreCase("english")));
                assertTrue(out.getLanguages().stream()
                        .anyMatch(s -> s.equalsIgnoreCase("french")));
            }

            @Override
            public void presentError(String message) {
                fail("Use case failure is unexpected: " + message);
            }
        };

        CountryInfoInputBoundary interactor = new CountryInfoInteractor(repo, successPresenter);
        interactor.execute(inputData);
    }

    @Test
    void TestFailureUnknownCountry() {
        CountryInfoInputData inputData = new CountryInfoInputData("NOTACOUNTRY");
        CountryInfoDataAccessInterface repo = new RESTCountriesAPI();

        CountryInfoOutputBoundary failurePresenter = new CountryInfoOutputBoundary() {
            @Override
            public void present(CountryInfoOutputData out) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void presentError(String message) {
                assertEquals("Country not found or network error.", message);
            }
        };

        CountryInfoInputBoundary interactor = new CountryInfoInteractor(repo, failurePresenter);
        interactor.execute(inputData);
    }
}