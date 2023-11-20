package model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Schedule {
    private List<ClassSchedule> classSchedules;
}
