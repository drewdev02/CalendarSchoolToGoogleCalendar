package googlecalendar.services.impl;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import googlecalendar.services.AuthorizationFlow;
import googlecalendar.services.CalendarService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j

public class GoogleCalendarServiceBuilder {
    private AuthorizationFlow authorizationFlow;
    private NetHttpTransport httpTransport;

    public GoogleCalendarServiceBuilder withAuthorizationFlow() {
        this.authorizationFlow = new GoogleAuthorizationFlow();
        log.debug("AuthorizationFlow set");
        return this;
    }

    @SneakyThrows
    public GoogleCalendarServiceBuilder withHttpTransport() {
        this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        log.debug("HttpTransport set");
        return this;
    }

    public CalendarService build() {
        if (httpTransport == null) {
            throw new IllegalStateException("NetHttpTransport is not set. Call withHttpTransport() before calling build().");
        }
        if (authorizationFlow == null) {
            throw new IllegalStateException("AuthorizationFlow is not set. Call withAuthorizationFlow() before calling build().");
        }
        log.debug("Building GoogleCalendarService");
        return new GoogleCalendarService(httpTransport, authorizationFlow);
    }
}


