import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import googlecalendar.services.impl.GoogleCalendarServiceBuilder;

public class CalendarQuickstart {

    public static void main(String... args) {
        // Build a new authorized API client service.
        var calendar = new GoogleCalendarServiceBuilder()
                .withAuthorizationFlow()
                .withHttpTransport()
                .build();

        // Crear objeto DateTime para mañana
        var tomorrow = new DateTime(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        System.out.println(tomorrow);

        // Crear objeto Event
        var evento = new Event()
                .setSummary("Nuevo evento")
                .setStart(new EventDateTime().setDateTime(tomorrow))
                .setEnd(new EventDateTime().setDateTime(tomorrow));


        // Llamar al servicio para insertar el evento
        calendar.createEvent(evento);
        System.out.println("evento creado");
        var lista = calendar.listNext10Events();
        System.out.println(lista);


    }
}