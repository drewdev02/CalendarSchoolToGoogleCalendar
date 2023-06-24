package util;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

@Slf4j
public class HoraUtil {
    private HoraUtil() {
    }

    public static @Nullable String convertToRFC3339(String dateString, String timeString) {
        try {
            var date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            var formattedTime = convertToTwoDigitFormat(timeString);
            var time = LocalTime.parse(formattedTime, DateTimeFormatter.ofPattern("HH:mm"));
            var dateTime = date.atTime(time);
            var zoneOffset = ZoneOffset.UTC; // Especificamos la zona horaria como UTC
            var offsetDateTime = OffsetDateTime.of(dateTime, zoneOffset);
            return offsetDateTime.toInstant().toString();
        } catch (DateTimeParseException e) {
            // Manejar el error de análisis de fecha o hora aquí
            log.error("Error al convertir a formato RFC3339: {}", e.getMessage());
            return null;
        }
    }

    public static List<String> algo(String dateString, List<String> timeStrings, BiFunction<String, String, String> function) {
        return timeStrings.stream()
                .map(timeString -> function.apply(dateString, timeString))
                .filter(Objects::nonNull)
                .toList();

    }

    public static BiFunction<String, String, String> convertToRFC3339() {
        return HoraUtil::convertToRFC3339;
    }

    public static List<String> algo(String dateString, List<String> timeStrings) {
        return timeStrings.stream()
                .map(timeString -> convertToRFC3339(dateString, timeString))
                .filter(Objects::nonNull)
                .toList();

    }

    public static @Nullable String convertToTwoDigitFormat(String timeString) {
        try {
            var time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("H:mm"));
            return time.format(DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            log.error("Error al convertir a formato de dos dígitos: {}", e.getMessage());
            return null;
        }
    }

    public static @NotNull List<String> generateDateList(String startDate) {
        List<String> dateList = new ArrayList<>();

        try {
            var initialDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            for (var i = 1; i <= 5; i++) {
                var currentDate = initialDate.plusDays(i);
                var formattedDate = currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                dateList.add(formattedDate);
            }
        } catch (Exception e) {
            // Manejar el error de análisis de fecha aquí
            log.error("Error al generar la lista de fechas: {}", e.getMessage());
        }
        return dateList;
    }

    public static List<String> addTimeSuffix(List<String> strings) {
        return IntStream.range(0, strings.size())
                .mapToObj(i -> {
                    var string = strings.get(i);
                    var suffix = (i >= strings.size() - 5) ? "p.m" : "a.m";
                    return string + " " + suffix;
                })
                .toList();
    }

    public static void main(String[] args) {
        var fechas = generateDateList("18/06/2023");
        System.out.println(fechas);
    }
}

