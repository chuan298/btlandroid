package com.ptit.timetable.model;

public class Subject {
    private Integer id;


    private String name;


    private String code;

    private Integer creditNumber;

    private Integer lessonNumber1;

    private Integer lessonNumber2;

    public Subject() {
    }

    public Subject(Integer id, String name, String code, Integer creditNumber, Integer lessonNumber1, Integer lessonNumber2) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.creditNumber = creditNumber;
        this.lessonNumber1 = lessonNumber1;
        this.lessonNumber2 = lessonNumber2;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public Integer getCreditNumber() {
        return creditNumber;
    }

    public Integer getLessonNumber1() {
        return lessonNumber1;
    }

    public Integer getLessonNumber2() {
        return lessonNumber2;
    }
}
