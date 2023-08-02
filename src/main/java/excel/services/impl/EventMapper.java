package excel.services.impl;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import excel.services.IDataExtractor;
import excel.services.IEventMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import model.Subject;
import model.TimeSlot;
import org.jetbrains.annotations.NotNull;
import util.HoraUtil;

import java.util.ArrayList;
import java.util.Collections;
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

    private List<TimeSlot> parseTimeData(@NotNull List<String> timeData, String date) {
        try {
            // Filtra los elementos que no sean "null", los divide en partes y los procesa
            return timeData.stream()
                    .filter(str -> !str.equalsIgnoreCase("null"))
                    .map(srt -> {
                        var parts = srt.split("-");
                        var start = parts[0].trim();
                        var end = parts[1].trim();

                        // Convierte las horas en formato RFC3339
                        var startTime = new DateTime(HoraUtil.convertToRFC3339(date, start));
                        var endTime = new DateTime(HoraUtil.convertToRFC3339(date, end));

                        // Crea un objeto TimeSlot con las horas convertidas
                        return TimeSlot.builder()
                                .start(startTime)
                                .end(endTime)
                                .build();
                    })
                    .filter(Objects::nonNull)
                    .toList();
        } catch (Exception e) {
            log.error("Exception message: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<Event> mapToEventDataList(@NotNull List<Subject> subjects) {
        // Mapea cada objeto Subject a un objeto Event y los agrega a una lista
        return subjects.stream()
                .map(subject -> new Event()
                        .setSummary(subject.getName()) // Establece el nombre del evento
                        .setLocation(subject.getLocation()) // Establece la ubicación del evento
                        .setStart(new EventDateTime()
                                .setDateTime(subject.getTimeSlot().getStart())) // Establece la fecha y hora de inicio del evento
                        .setEnd(new EventDateTime()
                                .setDateTime(subject.getTimeSlot().getEnd()))) // Establece la fecha y hora de fin del evento
                .toList(); // Convierte el flujo de eventos mapeados en una lista
    }

    @Override
    public List<TimeSlot> getTimeSlots(String date) {
        // Extrae los datos de tiempo de las celdas debajo de la celda "C21"
        var timeString = extractDataBelowCell("C21");

        // Parsea los datos de tiempo y los convierte en objetos TimeSlot
        return parseTimeData(timeString, date);
    }

    @Override
    public List<Subject> mapToListOfSubjects(@NotNull List<String> list, List<TimeSlot> horas) {
        // Crear listas para almacenar los nombres de las asignaturas y las ubicaciones
        var listSubject = new ArrayList<String>();
        var listLocation = new ArrayList<String>();

        // Iterar sobre la lista de entrada y dividirla en nombres de asignaturas y ubicaciones
        IntStream.range(0, list.size() / 2)
                .forEach(i -> {
                    if (i % 2 == 0) listSubject.add(list.get(i));
                    else listLocation.add(list.get(i));
                });
        return IntStream.range(0, listSubject.size())
                .mapToObj(i -> Subject.builder()// Mapear las listas de nombres de asignaturas y ubicaciones a objetos Subject
                        .numberClass(i + 1)
                        .name(listSubject.get(i))
                        .timeSlot(horas.get(i))
                        .location(listLocation.get(i))
                        .build())
                .toList();
    }

    @Override
    public List<Subject> updateSubjectTimeSlots(@NotNull List<Subject> subjects, String date) {
        // Mapear cada objeto Subject de la lista subjects a un nuevo objeto Subject
        return subjects.stream()
                .map(subject -> {
                    // Obtener el TimeSlot del objeto Subject actual
                    var timeSlot = subject.getTimeSlot();

                    // Extraer la hora de inicio y fin del TimeSlot
                    var start = HoraUtil.extractTime(timeSlot.getStart());
                    var end = HoraUtil.extractTime(timeSlot.getEnd());

                    // Convertir las horas extraídas a formato RFC3339 con la fecha proporcionada
                    var newStart = new DateTime(HoraUtil.convertToRFC3339(date, start));
                    var newEnd = new DateTime(HoraUtil.convertToRFC3339(date, end));

                    // Construir un nuevo objeto Subject con las nuevas fechas de inicio y fin
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



