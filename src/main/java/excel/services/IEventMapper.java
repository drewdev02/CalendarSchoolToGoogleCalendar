package excel.services;

import com.google.api.services.calendar.model.Event;
import model.Subject;
import model.TimeSlot;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IEventMapper {
    List<Event> mapToEventDataList(@NotNull List<Subject> subjects);

    List<TimeSlot> getTimeSlots(String date);

    List<Subject> mapToListOfSubjects(@NotNull List<String> list, List<TimeSlot> horas);

    List<Subject> updateSubjectTimeSlots(@NotNull List<Subject> subjects, String date);
}
