package googlecalendar.services;

import com.google.api.services.calendar.model.Event;

import java.util.List;

public interface CalendarService {
    void createEvent(Event event);

    List<String> listNext10Events();
}

