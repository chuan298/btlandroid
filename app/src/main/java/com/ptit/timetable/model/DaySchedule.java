package com.ptit.timetable.model;

import android.util.Pair;

import java.util.List;

public class DaySchedule {

    private List<Pair<Schedule, Integer>> schedule;

    public DaySchedule(List<Pair<Schedule, Integer>> schedule) {

        this.schedule = schedule;
    }

    public DaySchedule() {
    }

    public List<Pair<Schedule, Integer>> getSchedule() {
        return schedule;
    }

    @Override
    public String toString() {
        return "DaySchedule{" +
                "schedule=" + schedule +
                '}';
    }
}
