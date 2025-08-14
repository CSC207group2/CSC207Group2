package CountryInfo_p.entity;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CountryInfoTest {

    @Test
    void getCountryName() {
        CountryInfo info = new CountryInfo("Canada", "Ottawa", "CAD",
                Arrays.asList("English", "French"));
        assertEquals("Canada", info.getCountryName());
    }

    @Test
    void getCapital() {
        CountryInfo info = new CountryInfo("Canada", "Ottawa", "CAD",
                Arrays.asList("English", "French"));
        assertEquals("Ottawa", info.getCapital());
    }

    @Test
    void getCurrency() {
        CountryInfo info = new CountryInfo("Canada", "Ottawa", "CAD",
                Arrays.asList("English", "French"));
        assertEquals("CAD", info.getCurrency());
    }

    @Test
    void getLanguages() {
        List<String> languages = Arrays.asList("English", "French");
        CountryInfo info = new CountryInfo("Canada", "Ottawa", "CAD", languages);
        assertEquals(languages, info.getLanguages());
        assertTrue(info.getLanguages().contains("English"));
        assertTrue(info.getLanguages().contains("French"));
    }
}