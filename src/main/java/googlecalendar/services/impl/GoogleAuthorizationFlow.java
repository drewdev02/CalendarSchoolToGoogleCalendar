package googlecalendar.services.impl;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import googlecalendar.services.AuthorizationFlow;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Collections;
import java.util.List;

@Data
@Slf4j
@NoArgsConstructor
public class GoogleAuthorizationFlow implements AuthorizationFlow {
    private final String CREDENTIALS_FILE_PATH = "credentials.json";
    private final String TOKENS_DIRECTORY_PATH = "tokens";

    private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final List<String> SCOPES =
            Collections.singletonList(CalendarScopes.CALENDAR);

    public GoogleAuthorizationFlow GoogleAuthorizationFlow() {
        return this;
    }

    @Override
    public Credential getCredentials(NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        log.debug("Loading client secrets from file: {}", CREDENTIALS_FILE_PATH);
        var in = new FileInputStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        var clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        log.debug("Building authorization flow");
        var flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        var receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        // Returns an authorized Credential object.
        log.debug("Authorizing user");
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
}

