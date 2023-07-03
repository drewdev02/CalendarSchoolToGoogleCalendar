package model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Subject {
    private String day;
    private String name;
    private String location;
    private TimeSlot hora;
}
