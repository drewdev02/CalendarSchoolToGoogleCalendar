import excel.ExcelReaderBuilder;
import excel.services.impl.EventMapper;
import googlecalendar.GoogleCalendarServiceBuilder;
import lombok.extern.slf4j.Slf4j;
import model.ClassSchedule;
import model.Schedule;

import java.util.List;

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

        calendar.createEvents(eventos);
        log.info("eventos creados");

        var lista = calendar.listNext10Events();
        log.info(lista.toString());


    }
}