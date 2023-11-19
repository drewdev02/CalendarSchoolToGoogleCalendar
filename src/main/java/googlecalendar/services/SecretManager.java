package googlecalendar.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersionName;
import lombok.AllArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

@AllArgsConstructor
public class SecretManager {
    private static final String projectId = "calendar-390122";
    private static final String secretId = "76017159543-np73daebva0nfb6d96o85a0upvgogg63.apps.googleusercontent.com";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    ;

    public static GoogleClientSecrets getSecret() throws IOException {
        // Crea un cliente de Secret Manager
        try (var client = SecretManagerServiceClient.create()) {
            // Especifica el nombre del proyecto, el nombre del secreto y el número de versión
            var secretVersionName = SecretVersionName.of(projectId, secretId, "latest");

            // Accede al secreto
            var response = client.accessSecretVersion(secretVersionName);

            // Devuelve el valor del secreto
            return GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(
                    new ByteArrayInputStream(response.getPayload().getData().toByteArray())));
        }
    }
}
