package com.ptit.timetable.model;

public class Schedule {
    private Integer id;

    private SubjectGroup subjectGroup;

    private PracticeGroup practiceGroup;

    public Schedule() {
    }

    public Schedule(Integer id, SubjectGroup subjectGroup, PracticeGroup practiceGroup) {
        this.id = id;
        this.subjectGroup = subjectGroup;
        this.practiceGroup = practiceGroup;
    }

    public Integer getId() {
        return id;
    }

    public SubjectGroup getSubjectGroup() {
        return subjectGroup;
    }

    public PracticeGroup getPracticeGroup() {
        return practiceGroup;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", subjectGroup=" + subjectGroup +
                ", practiceGroup=" + practiceGroup +
                '}';
    }
}
