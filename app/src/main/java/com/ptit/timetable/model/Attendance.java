package com.ptit.timetable.model;

public class Attendance {
    private Integer id;

    private StudentCourse studentCourse;

    private String time;

    public Attendance() {
    }

    public Attendance(Integer id, StudentCourse studentCourse, String time) {
        this.id = id;
        this.studentCourse = studentCourse;
        this.time = time;
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

    @Override
    public String toString() {
        return "Attendance{" +
                "id=" + id +
                ", studentCourse=" + studentCourse +
                ", time='" + time + '\'' +
                '}';
    }
}
