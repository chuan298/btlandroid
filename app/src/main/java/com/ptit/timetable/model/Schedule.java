package com.ptit.timetable.model;

public class Schedule {
    private Integer id;

    private PracticeGroup practiceGroup;

    public Schedule() {
    }

    public Schedule(Integer id, PracticeGroup practiceGroup) {
        this.id = id;
        this.practiceGroup = practiceGroup;
    }

    public Integer getId() {
        return id;
    }

    public PracticeGroup getPracticeGroup() {
        return practiceGroup;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", practiceGroup=" + practiceGroup +
                '}';
    }
}
