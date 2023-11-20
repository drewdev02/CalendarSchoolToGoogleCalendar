package model;

import com.google.api.services.calendar.model.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class ClassSchedule {
    private List<Event> events;
}
