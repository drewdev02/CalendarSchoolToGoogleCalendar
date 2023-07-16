package util;

import com.google.api.client.util.DateTime;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.regex.Pattern;

@Slf4j
public class HoraUtil {

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    private static final String TIME_FORMAT = "HH:mm";

    private HoraUtil() {
    }

    public static String convertToRFC3339(String dateString, String timeString) {
        try {
            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(DATE_FORMAT));
            String formattedTime = convertToTwoDigitFormat(timeString);
            LocalTime time = LocalTime.parse(formattedTime, DateTimeFormatter.ofPattern(TIME_FORMAT));
            LocalDateTime dateTime = date.atTime(time);
            ZoneId zone = ZoneId.of("America/Havana"); // Zona Horaria de Cuba (CST)
            ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, zone);
            return zonedDateTime.toInstant().toString();
        } catch (DateTimeParseException e) {
            // Manejar el error
            System.out.println("Error al convertir a formato RFC3339: " + e.getMessage());
            return null;
        }
    }

    public static @Nullable String convertToTwoDigitFormat(String timeString) {
        try {
            var time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("H:mm"));
            return time.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
        } catch (DateTimeParseException e) {
            log.error("Error al convertir a formato de dos dígitos: {}", e.getMessage());
            return null;
        }
    }

    public static String extractTime(DateTime dateTime) {
        var timeString = dateTime.toString(); // Convertir el objeto DateTime a una cadena de texto
        var pattern = "\\d{2}:\\d{2}"; // Expresión regular para buscar el formato "HH:mm"
        var regex = Pattern.compile(pattern);
        var matcher = regex.matcher(timeString);
        if (matcher.find()) {
            return matcher.group(); // Devolver la primera coincidencia encontrada
        } else {
            throw new IllegalArgumentException("No se pudo extraer la hora en formato HH:mm");
        }
    }

}

