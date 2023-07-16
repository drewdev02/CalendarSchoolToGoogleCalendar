package model;

import com.google.api.services.calendar.model.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassSchedule {
    private String day;
    private List<Event> events;

    public void addEvent(Event event) {
        this.events.add(event);
    }
}
