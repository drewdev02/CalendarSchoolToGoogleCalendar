import excel.ExcelReaderBuilder;
import excel.services.IDataExtractor;
import excel.services.IEventMapper;
import googlecalendar.GoogleCalendarServiceBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import model.ClassSchedule;
import model.Schedule;
import util.Names;

import java.util.List;
import java.util.stream.IntStream;

import static util.HoraUtil.generateDateList;
import static util.HoraUtil.getTimeSlotBounds;

@Slf4j
public class CalendarStart implements Runnable {

    private List<String> getAlphabet() {
        return IntStream.rangeClosed('A', 'Z')
                .mapToObj(c -> String.valueOf((char) c))
                .toList();
    }

    @SneakyThrows
    private List<ClassSchedule> generateClassSchedules(IDataExtractor data, IEventMapper mapper, String startDate,
                                                       String endDate) {

        var fechas = generateDateList(startDate, endDate);
        log.debug("Fechas Generadas");

        var horas = fechas.stream()
                .map(mapper::getTimeSlots)
                .toList();

        var rowNumber = "21";
        var colLetter = "D";

        var semana = IntStream.range(0, fechas.size())
                .mapToObj(i -> {
                    var index = getAlphabet().indexOf(colLetter);
                    return getAlphabet().get(index + i);
                })
                .map(letra -> data.extractDataBelowCell(letra + rowNumber))
                .map(lista -> lista.stream().map(Names::reemplaceNames).toList())
                .toList();

        return IntStream.range(0, semana.size())
                .mapToObj(i -> mapper.mapToListOfSubjects(semana.get(i), horas.get(i)))
                .map(mapper::mapToEventDataList)
                .map(lista -> ClassSchedule.builder().events(lista).build())
                .toList();
    }

    @Override
    public void run() {
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

        var timeSlotBounds = getTimeSlotBounds();

        var inicio = timeSlotBounds.getFirst();

        var Semana = timeSlotBounds.getSecond();

        var mapper = excel.getEventMapper();

        var data = excel.getDataExtractor();

        var classSchedules = generateClassSchedules(data, mapper, inicio, Semana);

        var schedule = Schedule.builder()
                .classSchedules(classSchedules)
                .build();
        calendar.createEvents(schedule);
        log.info("task completed");
    }

    public static void main(String[] args) {
        new CalendarStart().run();
    }
}
