package googlecalendar.services;

import com.google.api.services.calendar.model.Event;

import java.util.List;

public interface CalendarService {
    void createEvent(Event event);

    void createEvents(List<Event> events);

    List<String> listNext10Events();
}

