package logs.data_access;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.InputStream;
import java.io.IOException;

public class FirebaseInitializer {

    public static void initialize() {
        try {
            // Load the service account JSON from the classpath
            InputStream serviceAccount = FirebaseInitializer.class.getClassLoader()
                    .getResourceAsStream("firebase/serviceAccountKey.json");

            if (serviceAccount == null) {
                throw new RuntimeException("❌ serviceAccountKey.json not found in classpath.");
            }
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://travelplannerauth-e78d0-default-rtdb.firebaseio.com") // ✅ Your actual DB URL
                    .build();

            // Always initialize explicitly
            FirebaseApp.initializeApp(options);
            System.out.println("✅ Firebase initialized.");

        } catch (IOException e) {
            System.err.println("❌ Failed to initialize Firebase: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Firebase already initialized. Skipping.");
        }
    }
}
