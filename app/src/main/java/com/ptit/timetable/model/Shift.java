package com.ptit.timetable.model;

public class Shift {
    private Integer id;

    private Integer startLesson;

    private Integer endLesson;

    public Shift(Integer id, Integer startLesson, Integer endLesson) {
        this.id = id;
        this.startLesson = startLesson;
        this.endLesson = endLesson;
    }

    public Shift() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStartLesson() {
        return startLesson;
    }

    public void setStartLesson(Integer startLesson) {
        this.startLesson = startLesson;
    }

    public Integer getEndLesson() {
        return endLesson;
    }

    public void setEndLesson(Integer endLesson) {
        this.endLesson = endLesson;
    }

    @Override
    public String toString() {
        return "Shift{" +
                "id=" + id +
                ", startLesson=" + startLesson +
                ", endLesson=" + endLesson +
                '}';
    }
}
