package core;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseInitializer {
    public static void initialize() {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream("resources/firebase/serviceAccountKey.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://your-project-id.firebaseio.com") // Replace with your database URL
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase initialized.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
