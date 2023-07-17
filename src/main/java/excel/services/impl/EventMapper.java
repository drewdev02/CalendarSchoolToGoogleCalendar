package excel.services.impl;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import excel.services.IDataExtractor;
import excel.services.IEventMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import model.Subject;
import model.TimeSlot;
import org.jetbrains.annotations.NotNull;
import util.HoraUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;


@Data
@Slf4j
@Builder
@AllArgsConstructor
public class EventMapper implements IEventMapper {
    private IDataExtractor dataExtractor;

    private List<String> extractDataBelowCell(String cellReference) {
        return dataExtractor.extractDataBelowCell(cellReference);
    }

    @SneakyThrows
    private List<TimeSlot> parseTimeData(@NotNull List<String> timeData, String date) {
        return timeData.stream()
                .filter(str -> !str.equalsIgnoreCase("null"))
                .map(srt -> {
                    var parts = srt.split("-");
                    var start = parts[0].trim();
                    var end = parts[1].trim();

                    var startTime = new DateTime(HoraUtil.convertToRFC3339(date, start));
                    var endTime = new DateTime(HoraUtil.convertToRFC3339(date, end));
                    return TimeSlot.builder()
                            .start(startTime)
                            .end(endTime)
                            .build();
                })
                .filter(Objects::nonNull)
                .toList();
    }

    public List<Event> mapToEventDataList(@NotNull List<Subject> subjects) {
        return subjects.stream().map(subject -> new Event()
                        .setSummary(subject.getName())
                        .setLocation(subject.getLocation())
                        .setStart(new EventDateTime().setDateTime(subject.getTimeSlot().getStart()))
                        .setEnd(new EventDateTime().setDateTime(subject.getTimeSlot().getEnd())))
                .toList();
    }

    public List<TimeSlot> getTimeSlots(String date) {
        var timeString = extractDataBelowCell("C21");
        return parseTimeData(timeString, date);
    }

    public List<Subject> mapToListOfSubjects(@NotNull List<String> list, List<TimeSlot> horas) {
        var listSubjet = new ArrayList<String>();
        var listLocation = new ArrayList<String>();
        IntStream.range(0, list.size() / 2)
                .forEach(i -> {
                    if (i % 2 == 0) listSubjet.add(list.get(i));
                    else listLocation.add(list.get(i));
                });
        return IntStream.range(0, listSubjet.size())
                .mapToObj(i -> Subject.builder()
                        .numberClass(i + 1)
                        .name(listSubjet.get(i))
                        .timeSlot(horas.get(i))
                        .location(listLocation.get(i))
                        .build())
                .toList();
    }

    public List<Subject> algo(@NotNull List<Subject> subjects, String date) {
        return subjects.stream()
                .map(subject -> {
                    var timeSlot = subject.getTimeSlot();
                    var start = HoraUtil.extractTime(timeSlot.getStart());
                    var end = HoraUtil.extractTime(timeSlot.getEnd());
                    var newStart = new DateTime(HoraUtil.convertToRFC3339(date, start));
                    var newEnd = new DateTime(HoraUtil.convertToRFC3339(date, end));
                    return Subject.builder()
                            .numberClass(subject.getNumberClass())
                            .name(subject.getName())
                            .timeSlot(TimeSlot.builder()
                                    .start(newStart)
                                    .end(newEnd)
                                    .build())
                            .location(subject.getLocation())
                            .build();
                })
                .toList();

    }
}



