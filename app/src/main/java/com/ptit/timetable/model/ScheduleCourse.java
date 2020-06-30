package com.ptit.timetable.model;

import java.io.Serializable;

public class ScheduleCourse implements Serializable {
    private static final long serialVersionUID =  13243353554L;
    private Schedule schedule;
    private Integer typeSchedule;
    private Boolean isAttendanced;

    public ScheduleCourse(Schedule schedule, Integer typeSchedule, Boolean isAttendanced) {
        this.schedule = schedule;
        this.typeSchedule = typeSchedule;
        this.isAttendanced = isAttendanced;
    }

    public ScheduleCourse() {
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Integer getTypeSchedule() {
        return typeSchedule;
    }

    public Boolean getIsAttendanced() {
        return isAttendanced;
    }

    @Override
    public String toString() {
        return "ScheduleCourse{" +
                "schedule=" + schedule +
                ", typeSchedule=" + typeSchedule +
                ", isAttendanced=" + isAttendanced +
                '}';
    }
}
