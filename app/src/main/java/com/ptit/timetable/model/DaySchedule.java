package com.ptit.timetable.model;

import android.util.Pair;

import java.util.List;
import java.util.Map;

public class DaySchedule {
    private Integer day_number;
    private List<ScheduleCourse> scheduleCourseList;

    public DaySchedule(Integer day_number, List<ScheduleCourse> scheduleCourseList) {
        this.day_number = day_number;
        this.scheduleCourseList = scheduleCourseList;
    }

    public DaySchedule() {
    }

    public Integer getDay_number() {
        return day_number;
    }

    public List<ScheduleCourse> getScheduleCourseList() {
        return scheduleCourseList;
    }

    @Override
    public String toString() {
        return "DaySchedule{" +
                "day_number=" + day_number +
                ", scheduleCourseList=" + scheduleCourseList +
                '}';
    }
}
