package util;

import com.google.api.client.util.DateTime;
import excel.services.impl.ExcelFileLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Slf4j
public class HoraUtil {

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    private static final String TIME_FORMAT = "HH:mm";

    private HoraUtil() {
    }

    public static String convertToRFC3339(String dateString, String timeString) {
        try {
            final var date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(DATE_FORMAT));
            final var formattedTime = convertToTwoDigitFormat(timeString);
            final var time = LocalTime.parse(Objects.requireNonNull(formattedTime), DateTimeFormatter.ofPattern(TIME_FORMAT));
            final var dateTime = date.atTime(time);
            final var zone = ZoneId.of("America/Havana"); // Zona Horaria de Cuba (CST)
            final var zonedDateTime = ZonedDateTime.of(dateTime, zone);
            return zonedDateTime.toInstant().toString();
        } catch (DateTimeParseException e) {
            log.error("Error al convertir a formato RFC3339: {}", e.getMessage());
            return null;
        }
    }

    public static @Nullable String convertToTwoDigitFormat(String timeString) {
        try {
            var primerDijito = Integer.parseInt(timeString.split(":")[0]);
            if (primerDijito >= 1 && primerDijito <= 5) {
                //convertir en formato 24h
                return convertTo24HourFormat(timeString);
            } else {
                final var time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("H:mm"));
                return time.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
            }
        } catch (DateTimeParseException e) {
            log.error("Error al convertir a formato de dos dígitos: {}", e.getMessage());
            return null;
        }
    }

    public static String formatDate(String date) {
        if (date.length() == 6) {
            log.debug("Formateando fecha...");
            final var localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("ddMMyy"));
            final var formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            return localDate.format(formatter);
        } else {
            log.debug("No es necesario formatear la fecha");
            return date;
        }
    }

    public static String extractTime(DateTime dateTime) {
        final var timeString = dateTime.toString(); // Convertir el objeto DateTime a una cadena de texto
        final var pattern = "\\d{2}:\\d{2}"; // Expresión regular para buscar el formato "HH:mm"
        final var regex = Pattern.compile(pattern);
        final var matcher = regex.matcher(timeString);
        if (matcher.find()) {
            return matcher.group(); // Devolver la primera coincidencia encontrada
        } else {
            log.error("No se pudo extraer la hora en formato HH:mm");
            throw new IllegalArgumentException("No se pudo extraer la hora en formato HH:mm");
        }
    }

    @SuppressWarnings("unused")
    public static @NotNull List<String> generateDateList(String startDate) {
        try {
            final var initialDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern(DATE_FORMAT));
            return IntStream.range(0, 5)
                    .mapToObj(i -> initialDate.plusDays(i + 1))
                    .map(date -> date.format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
                    .toList();
        } catch (Exception e) {
            log.error("Error al generar la lista de fechas: {}", e.getMessage());
            return Collections.emptyList();
        }

    }

    public static List<String> generateDateList(String startDate, String endDate) {
        //comprobar el formato de las fechas
        if (startDate.length() != 8 || endDate.length() != 8) {
            log.debug("Formato de fechas incorrecto, se intentará formatear");
            startDate = formatDate(startDate);
            endDate = formatDate(endDate);
        }

        try {
            // 1. Parsear fechas inicial y final desde cadenas
            final var initial = LocalDate.parse(startDate, DateTimeFormatter.ofPattern(DATE_FORMAT));
            final var end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern(DATE_FORMAT));

            // 2. Calcular total de días entre las dos fechas
            final var daysBetween = ChronoUnit.DAYS.between(initial, end);

            // 3. Generar stream de números enteros del 0 al total de días
            return LongStream.rangeClosed(0, daysBetween)
                    .mapToObj(initial::plusDays)// 4. Mapear cada número a una fecha, sumando días a la inicial
                    .map(date -> date.format(DateTimeFormatter.ofPattern(DATE_FORMAT))) // 5. Mapear cada fecha a String con formato
                    .toList();
        } catch (DateTimeParseException e) {
            log.error("Error al generar la lista de fechas: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public static String convertTo24HourFormat(String timeString) {
        String[] timeParts = timeString.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1].substring(0, 2));
        hours += 12;
        return String.format("%02d:%02d", hours, minutes);
    }

    public static Pair<String, String> getTimeSlotBounds() {
        final var timeSlot = ExcelFileLoader.getFile().split("_")[3];

        final var inicio = timeSlot.split("-")[0];

        final var finalSemana = timeSlot.split("-")[1].split("\\.")[0];

        return new Pair<>(inicio, finalSemana);
    }
}