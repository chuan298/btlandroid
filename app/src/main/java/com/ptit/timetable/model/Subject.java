package com.ptit.timetable.model;

/**
 * Created by Ulan on 07.09.2018.
 */
public class Subject {

    private String subject_name, fragment, teacher, room, fromtime, totime;
    private int id, color;

    public Subject() {}

    public Subject(String subject, String teacher, String room, String fromtime, String totime, int color,String fragment) {
        this.subject_name = subject;
        this.teacher = teacher;
        this.room = room;
        this.fromtime = fromtime;
        this.totime = totime;
        this.color = color;
        this.fragment = fragment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }

    public String getFromTime() {
        return fromtime;
    }

    public void setFromTime(String fromtime) {
        this.fromtime = fromtime;
    }

    public String getToTime() {
        return totime;
    }

    public void setToTime(String totime) {
        this.totime = totime;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject) {
        this.subject_name = subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String toString() {
        return subject_name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
