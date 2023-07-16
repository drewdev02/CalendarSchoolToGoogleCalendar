package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subject {
    private String day;
    private int numberClass;
    private String name;
    private TimeSlot timeSlot;
    private String location;
}
