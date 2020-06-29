package com.ptit.timetable.model;

import java.io.Serializable;

public class ScheduleCourse implements Serializable {
    private static final long serialVersionUID =  13243353554L;
    private Schedule schedule;
    private Integer typeSchedule;

    public ScheduleCourse(Schedule schedule, Integer typeSchedule) {
        this.schedule = schedule;
        this.typeSchedule = typeSchedule;
    }

    public ScheduleCourse() {
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Integer getTypeSchedule() {
        return typeSchedule;
    }

    @Override
    public String toString() {
        return "ScheduleCourse{" +
                "schedule=" + schedule +
                ", typeSchedule=" + typeSchedule +
                '}';
    }
}
