import static org.junit.jupiter.api.Assertions.*;

import Logs.data_access.FirebaseLogs;
import Logs.interface_adapter.FirebaseLogin;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class LogsAndSaveTest {

    @Test
    void testLoginSuccessWithValidCredentials() {
        FirebaseLogin.addUser("testuser", "testpass");
        boolean result = FirebaseLogin.login("testuser", "testpass");
        assertTrue(result, "Login should succeed with correct credentials");
    }

    @Test
    void testLoginFailsWithWrongPassword() {
        FirebaseLogin.addUser("testuser2", "testpass");
        boolean result = FirebaseLogin.login("testuser2", "wrongpass");
        assertFalse(result, "Login should fail with wrong password");
    }

    @Test
    void testSaveFlightWithValidData() {
        FirebaseLogs.saveFlight("testuser", "FL123", "JFK", "LAX", "10:00", "13:00");
        assertTrue(true, "Flight save should complete without errors");
    }

    @Test
    void testSaveCountryWithValidData() {
        Map<String, String> countryData = new HashMap<>();
        countryData.put("capital", "Tokyo");
        countryData.put("currency", "Yen");
        FirebaseLogs.saveCountry("testuser", "Japan", countryData);
        assertTrue(true, "Country save should complete without errors");
    }

    @Test
    void testPasswordHashingConsistency() {
        String password = "mypassword123";
        String hash1 = FirebaseLogin.hashPassword(password);
        String hash2 = FirebaseLogin.hashPassword(password);

        assertEquals(hash1, hash2, "Same password should produce same hash");
        assertNotEquals(password, hash1, "Hash should not be the same as plain password");
    }

    @Test
    void testEmptyInputHandling() {
        FirebaseLogs.saveFlight("", "", "", "", "", "");
        FirebaseLogs.saveCountry("", "", new HashMap<>());
        assertTrue(true, "Empty inputs should be handled");
    }
}