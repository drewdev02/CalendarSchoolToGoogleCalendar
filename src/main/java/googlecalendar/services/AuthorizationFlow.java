package googlecalendar.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;

public interface AuthorizationFlow {
    Credential getCredentials(NetHttpTransport httpTransport) throws IOException;
}

