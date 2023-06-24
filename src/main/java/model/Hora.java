package model;

import com.google.api.client.util.DateTime;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Hora {
    private DateTime start;
    private DateTime end;
}
