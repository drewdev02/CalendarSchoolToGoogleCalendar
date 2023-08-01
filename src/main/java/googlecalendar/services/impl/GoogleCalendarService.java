package googlecalendar.services.impl;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import googlecalendar.services.AuthorizationFlow;
import googlecalendar.services.CalendarService;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Data
@Slf4j
public class GoogleCalendarService implements CalendarService {
    private final String APPLICATION_NAME = "Google Calendar API Java Quickstart";

    private AuthorizationFlow authorizationFlow;

    private Calendar service;

    @SneakyThrows
    public GoogleCalendarService(NetHttpTransport httpTransport, @NotNull AuthorizationFlow authorizationFlow) {
        this.authorizationFlow = authorizationFlow;
        this.service = buildCalendarService(httpTransport);
    }

    @SneakyThrows
    private @NotNull Calendar buildCalendarService(NetHttpTransport httpTransport) {
        var credential = authorizationFlow.getCredentials(httpTransport);
        return new Calendar.Builder(httpTransport, credential.getJsonFactory(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


    public void createEvent(Event event) {
        try {
            service.events().insert("primary", event).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.debug("Event created: {}", event);
    }


    public void createEvents(List<Event> events) {
        events.forEach(event -> {
            if (!event.getSummary().equals("null") && !event.getLocation().equals("null")) {
                try {
                    service.events().insert("primary", event).execute();
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
                log.debug("Event created: {}", event);
            }
        });
    }

    @SneakyThrows
    public List<String> listNext10Events() {
        var now = new DateTime(System.currentTimeMillis());
        var events = service.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        var items = events.getItems();
        if (items == null || items.isEmpty()) {
            log.debug("No upcoming events found");
            return Collections.emptyList();
        } else {
            return items.stream()
                    .map(event -> {
                        var start = event.getStart().getDateTime();
                        if (start == null) {
                            start = event.getStart().getDate();
                        }
                        var eventSummary = String.format("%s (%s)", event.getSummary(), start);
                        log.debug("Upcoming event: {}", eventSummary);
                        return eventSummary;
                    })
                    .toList();
        }
    }

}