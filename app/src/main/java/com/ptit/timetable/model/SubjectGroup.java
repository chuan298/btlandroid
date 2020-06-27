package com.ptit.timetable.model;

public class SubjectGroup {
    private Integer id;

    private Subject subject;

    private Room room;

    private Shift shift1;

    private Shift shift2;

    private Integer day;

    private String week1;

    private String week2;

    private Integer groupNumber;

    private String code;

    public SubjectGroup() {
    }

    public SubjectGroup(Integer id, Subject subject, Room room, Shift shift1, Shift shift2, Integer day, String week1, String week2, Integer groupNumber, String code) {
        this.id = id;
        this.subject = subject;
        this.room = room;
        this.shift1 = shift1;
        this.shift2 = shift2;
        this.day = day;
        this.week1 = week1;
        this.week2 = week2;
        this.groupNumber = groupNumber;
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public Subject getSubject() {
        return subject;
    }

    public Room getRoom() {
        return room;
    }

    public Shift getShift1() {
        return shift1;
    }

    public Shift getShift2() {
        return shift2;
    }

    public Integer getDay() {
        return day;
    }

    public String getWeek1() {
        return week1;
    }

    public String getWeek2() {
        return week2;
    }

    public Integer getGroupNumber() {
        return groupNumber;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "SubjectGroup{" +
                "id=" + id +
                ", subject=" + subject +
                ", room=" + room +
                ", shift1=" + shift1 +
                ", shift2=" + shift2 +
                ", day=" + day +
                ", week1='" + week1 + '\'' +
                ", week2='" + week2 + '\'' +
                ", groupNumber=" + groupNumber +
                ", code='" + code + '\'' +
                '}';
    }
}
