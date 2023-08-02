package excel.services;

import com.google.api.services.calendar.model.Event;
import model.Subject;
import model.TimeSlot;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Interface for mapping data objects to Google Calendar events.
 */
public interface IEventMapper {

    /**
     * Maps a list of Subject objects to a list of Event objects.
     *
     * @param subjects the list of Subject instances
     * @return a list of Event objects
     */
    List<Event> mapToEventDataList(@NotNull List<Subject> subjects);

    /**
     * Gets the time slots for a given date.
     *
     * @param date the date in "dd/MM/yyyy" format
     * @return list of TimeSlot objects
     */
    List<TimeSlot> getTimeSlots(String date);

    /**
     * Maps a data list to a list of Subject objects.
     *
     * @param list      the input data list
     * @param timeSlots list of time slots
     * @return a list of Subject instances
     */
    List<Subject> mapToListOfSubjects(@NotNull List<String> list, List<TimeSlot> timeSlots);

    /**
     * Updates the time slots of a list of Subject objects for a given date.
     *
     * @param subjects list of Subject instances
     * @param date     the date to update time slots
     * @return updated list of Subject objects
     */
    List<Subject> updateSubjectTimeSlots(@NotNull List<Subject> subjects, String date);

}
