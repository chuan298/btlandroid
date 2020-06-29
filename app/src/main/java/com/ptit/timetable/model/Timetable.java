package com.ptit.timetable.model;

import android.util.Pair;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Timetable implements Serializable {
    private static final long serialVersionUID =  6543353554L;
    private Map<Integer, Map<Integer, List<Pair<Schedule, Integer>>>> timetable;

    public Timetable(Map<Integer, Map<Integer, List<Pair<Schedule, Integer>>>> timetable) {
        this.timetable = timetable;
    }

    public Timetable() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }



    public Map<Integer, Map<Integer, List<Pair<Schedule, Integer>>>> getTimetable() {
        return timetable;
    }

    @Override
    public String toString() {
        return "Timetable{" +
                "timetable=" + timetable +
                '}';
    }
}
