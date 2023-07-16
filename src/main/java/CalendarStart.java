import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import excel.ExcelReaderBuilder;
import excel.services.impl.EventMapper;
import googlecalendar.GoogleCalendarServiceBuilder;
import lombok.extern.slf4j.Slf4j;
import model.ClassSchedule;
import model.Schedule;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
public class CalendarStart {

    public static void main(String[] args) {
        // Build a new authorized API client service.
        var calendar = new GoogleCalendarServiceBuilder()
                .withAuthorizationFlow()
                .withHttpTransport()
                .build();

        var excel = new ExcelReaderBuilder()
                .withExcelFileLoader()
                .withDataExtractor()
                .withEventMapper()
                .build();

        var mapper = (EventMapper) excel.getEventMapper();

        var data = excel.getDataExtractor();

        var monday = data.extractDataBelowCell("D21");

        var horas = mapper.getTimeSlots("17/07/2023");

        var mondaySubjet = mapper.mapToListOfSubjects(monday, horas);

        var mondayturns = ClassSchedule.builder()
                .day("17/07/2023")
                .build();

        var horario = Schedule.builder()
                .classSchedules(List.of(mondayturns))
                .build();

        var eventos = mapper.mapToEventDataList(mondaySubjet);

        var a = mapper.algo(mondaySubjet, "17/07/2023");
        System.out.println(a);


        System.out.println(mondaySubjet);
        System.out.println(horas);
        mondaySubjet.forEach(System.out::println);

        calendar.createEvents(eventos);


        /*// Crear objeto DateTime para mañana
        var tomorrow = new DateTime(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        log.debug(String.valueOf(tomorrow));
        log.debug(String.valueOf(System.currentTimeMillis() * 60 * 60 * 1000));

        // Crear objeto Event
        var evento = new Event()
                .setSummary("Nuevo evento")
                .setStart(new EventDateTime().setDateTime(tomorrow))
                .setEnd(new EventDateTime().setDateTime(tomorrow));*/


        // Llamar al servicio para insertar el evento
        //  calendar.createEvent(evento);
        log.debug("evento creado");
        var lista = calendar.listNext10Events();
        log.debug(lista.toString());


    }
}