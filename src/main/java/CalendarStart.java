import com.google.api.services.calendar.model.Event;
import excel.ExcelReaderBuilder;
import excel.services.IDataExtractor;
import excel.services.IEventMapper;
import excel.services.impl.EventMapper;
import excel.services.impl.ExcelFileLoader;
import googlecalendar.GoogleCalendarServiceBuilder;
import lombok.extern.slf4j.Slf4j;
import util.HoraUtil;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
public class CalendarStart {
    public static List<String> getAlphabet() {
        return IntStream.rangeClosed('A', 'Z')
                .mapToObj(c -> String.valueOf((char) c))
                .toList();
    }

    public static List<List<Event>> algo(IDataExtractor data, IEventMapper mapper, String startDate, String endDate) {

        final var fechas = HoraUtil.generateDateList(startDate, endDate);
        log.debug("Fechas Generadas");

        final var horas = fechas.stream()
                .map(mapper::getTimeSlots)
                .toList();

        final var rowNumber = "21";
        final var colLetter = "D";

        final var semana = IntStream.range(0, fechas.size())
                .mapToObj(i -> {
                    var index = getAlphabet().indexOf(colLetter);
                    return getAlphabet().get(index + i);
                })
                .map(letra -> data.extractDataBelowCell(letra + rowNumber))
                .toList();

        return IntStream.range(0, semana.size())
                .mapToObj(i -> mapper.mapToListOfSubjects(semana.get(i), horas.get(i)))
                .map(mapper::mapToEventDataList)
                .toList();
    }

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

        var timeSlot = ExcelFileLoader.DATA_FILE_PATH.split("_")[3];

        var inicio = timeSlot.split("-")[0];

        var finalSemana = timeSlot.split("-")[1].split("\\.")[0];

        var mapper = excel.getEventMapper();

        var data = excel.getDataExtractor();

        var semana = algo(data, mapper, inicio, finalSemana);


        semana.forEach(calendar::createEvents);
        log.info("eventos creados");

        var lista = calendar.listNext10Events();
        log.info(lista.toString());


    }
}