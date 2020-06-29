package com.ptit.timetable.model;

public class PracticeGroup {
    private Integer id;

    private SubjectGroup subjectGroup;

    private Shift practiceShift;

    private Room practiceRoom;

    private String weekPractice;

    private Integer groupPractice;

    private Integer dayPractice;

    public PracticeGroup() {
    }

    public PracticeGroup(Integer id, SubjectGroup subjectGroup, Shift practiceShift, Room practiceRoom, String weekPractice, Integer groupPractice, Integer dayPractice) {
        this.id = id;
        this.subjectGroup = subjectGroup;
        this.practiceShift = practiceShift;
        this.practiceRoom = practiceRoom;
        this.weekPractice = weekPractice;
        this.groupPractice = groupPractice;
        this.dayPractice = dayPractice;
    }

    public Integer getId() {
        return id;
    }

    public SubjectGroup getSubjectGroup() {
        return subjectGroup;
    }

    public Shift getPracticeShift() {
        return practiceShift;
    }

    public Room getPracticeRoom() {
        return practiceRoom;
    }

    public String getWeekPractice() {
        return weekPractice;
    }

    public Integer getGroupPractice() {
        return groupPractice;
    }

    public Integer getDayPractice() {
        return dayPractice;
    }

    @Override
    public String toString() {
        return "PracticeGroup{" +
                "id=" + id +
                ", subjectGroup=" + subjectGroup +
                ", practiceShift=" + practiceShift +
                ", practiceRoom=" + practiceRoom +
                ", weekPractice='" + weekPractice + '\'' +
                ", groupPractice=" + groupPractice +
                ", dayPractice=" + dayPractice +
                '}';
    }
}
