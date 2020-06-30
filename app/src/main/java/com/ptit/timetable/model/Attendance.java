package com.ptit.timetable.model;

public class Attendance {
    private Integer id;

    private StudentCourse studentCourse;

    private String time;

    private String image;

    private Integer type_schedule;

    private Schedule schedule;

    public Attendance() {
    }

    public Attendance(Integer id, StudentCourse studentCourse, String time, String image, Integer type_schedule, Schedule schedule) {
        this.id = id;
        this.studentCourse = studentCourse;
        this.time = time;
        this.image = image;
        this.type_schedule = type_schedule;
        this.schedule = schedule;
    }

    public Integer getId() {
        return id;
    }

    public StudentCourse getStudentCourse() {
        return studentCourse;
    }

    public String getTime() {
        return time;
    }

    public String getImage() {
        return image;
    }

    public Integer getType_schedule() {
        return type_schedule;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "id=" + id +
                ", studentCourse=" + studentCourse +
                ", time='" + time + '\'' +
                ", image='" + image + '\'' +
                ", type_schedule=" + type_schedule +
                '}';
    }
}
