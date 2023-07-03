package excel.services.impl;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import excel.services.IDataExtractor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import model.TimeSlot;
import org.jetbrains.annotations.NotNull;
import util.HoraUtil;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Slf4j
@Data
@Builder
@AllArgsConstructor
public class EventMapper {
    private IDataExtractor dataExtractor;

    private List<String> extractDataBelowCell(String cellReference) {
        return dataExtractor.extractDataBelowCell(cellReference);
    }

    @SneakyThrows
    public List<TimeSlot> parseTimeData(@NotNull List<String> timeData) {
        return timeData.stream()
                .filter(str -> !str.equalsIgnoreCase("null"))
                .map(srt -> {
                    var parts = srt.split("-");
                    var start = parts[0].trim();
                    var end = parts[1].trim();

                    var startTime = new DateTime(HoraUtil.convertToRFC3339(start, "jj"));
                    var endTime = new DateTime(HoraUtil.convertToRFC3339(end, ""));
                    return TimeSlot.builder()
                            .start(startTime)
                            .end(endTime)
                            .build();

                })
                .filter(Objects::nonNull)
                .toList();
    }

    public List<Event> mapToEventDataList(@NotNull List<String> columnData) {
        var horas = extractDataBelowCell("C21");
        var date = parseTimeData(horas);

        return IntStream.range(0, columnData.size() / 2)
                .mapToObj(i -> new Event()
                        .setSummary(columnData.get(i * 2))
                        .setLocation(columnData.get(i * 2 + 1))
                        .setStart(new EventDateTime().setDateTime(date.get(i).getStart()))
                        .setEnd(new EventDateTime().setDateTime(date.get(i).getEnd())))
                .toList();
    }
}
