package util;

import com.google.api.services.calendar.model.EventAttendee;

import java.util.List;
import java.util.Set;

public class Attendee {
    private static final Set<String> attendees = Set.of(
            "javierc.ps16@gmail.com",
            "eismerlobaina@gmail.com",
            "voidtracer6@gmail.com"
    );

    private Attendee() {
    }

    public static List<EventAttendee> getAttendees() {
        return attendees.stream()
                .map(guestEmail -> new EventAttendee().setEmail(guestEmail))
                .toList();
    }
}
