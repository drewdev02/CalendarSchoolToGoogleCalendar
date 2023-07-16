package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    private List<ClassSchedule> classSchedules = new ArrayList<>();

    public void addClassSchedule(ClassSchedule classSchedule) {
        this.classSchedules.add(classSchedule);
    }
}
